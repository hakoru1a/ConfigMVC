/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.utils;

/**
 *
 * @author DinhChuong
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.enums.AuthenProviderEnums;
import com.social.pojo.Google;
import com.social.pojo.User;
import com.social.services.RoleService;
import com.social.services.UserService;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class GoogleUtils {

    public static String GOOGLE_CLIENT_ID = "510556489503-gtmvki42khmh32mfnrl51qunq5islnvt.apps.googleusercontent.com";
    public static String GOOGLE_CLIENT_SECRET = "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";
    public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/login-google";
    public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String GOOGLE_GRANT_TYPE = "authorization_code";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public String getToken(final String code) throws ClientProtocolException, IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GOOGLE_CLIENT_ID)
                        .add("client_secret", GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    public Google getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        Google googlePojo = mapper.readValue(response, Google.class);
        System.out.println(googlePojo);
        return googlePojo;
    }

    public UserDetails buildUser(Google googlePojo) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // default start test
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(googlePojo.getEmail(),
                "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        return userDetail;
    }

    public UserDetails buildUser(User u) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getRole().getName()));
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(u.getUsername(),
                "".toString(), authorities);
        return userDetail;
    }

    public Boolean createNewUserByGoogleAccount(Google googlePojo) {
        String username = googlePojo.getEmail().split("@")[0];
        // Check username is Exit
        Random rand = new Random();
        while (userService.isExistByUsername(username)) {
            username = username + rand.nextInt(100);
        }
        if (!this.userService.isExistByEmail(googlePojo.getEmail())) {
            User newUser = new User();
            newUser.setEmail(googlePojo.getEmail());
            newUser.setUsername(username);
            newUser.setProviderId(AuthenProviderEnums.GOOGLE.getValue());
            newUser.setPassword(passwordEncoder.encode(""));
            newUser.setRole(roleService.getRoleById(2));
            userService.save(newUser);
            return true;
        }

        return false;
    }
}

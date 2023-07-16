/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.social.enums.AuthenProviderEnums;
import com.social.pojo.User;
import com.social.services.RoleService;
import com.social.services.UserService;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author DinhChuong
 */
@Component
public class FacebookUtils {

    public static String FACEBOOK_APP_ID = "1352583565680412";
    public static String FACEBOOK_APP_SECRET = "707c51aa16127114d6a257d28aa820ea";
    public static String FACEBOOK_REDIRECT_URL = "http://localhost:8080/login-facebook";
    public static String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public String getToken(final String code) throws ClientProtocolException, IOException {
        String link = String.format(FACEBOOK_LINK_GET_TOKEN, FACEBOOK_APP_ID, FACEBOOK_APP_SECRET,
                FACEBOOK_REDIRECT_URL, code);
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    public com.restfb.types.User getUserInfo(final String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, FACEBOOK_APP_SECRET,
                Version.LATEST);
        return facebookClient.fetchObject("me", com.restfb.types.User.class);
    }

    public UserDetails buildUser(com.restfb.types.User user) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // default role
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(user.getId(), "", enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        return userDetail;
    }

    public UserDetails buildUser(User u) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole().getName()));
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(u.getUsername(),
                "", authorities);
        return userDetail;
    }

    public boolean createNewUserByFacebookAccount(com.restfb.types.User facebookPojo) {
        String username = facebookPojo.getName();
        if (!userService.isExistByEmail(facebookPojo.getEmail())) {
            User newUser = new User();
            newUser.setEmail(facebookPojo.getEmail());
            newUser.setUsername(StringUtils.removeDiacriticsAndSpaces(username + facebookPojo.getId()));
            newUser.setProviderId(AuthenProviderEnums.FACEBOOK.getValue());
            newUser.setPassword(passwordEncoder.encode(""));
            newUser.setRole(roleService.getRoleById(2)); // USER_ROLE
            userService.save(newUser);
            return true;
        }
        return false;
    }


}

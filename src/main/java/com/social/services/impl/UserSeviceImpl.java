/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.services.impl;

import com.social.dto.response.ModelResponse;
import com.social.enums.AuthenProviderEnums;
import com.social.jwt.JwtTokenProvider;
import com.social.pojo.Google;
import com.social.pojo.User;
import com.social.repositories.UserRepository;
import com.social.services.UserService;
import com.social.utils.FacebookUtils;
import com.social.utils.GoogleUtils;
import com.social.utils.StringUtils;
import freemarker.template.utility.StringUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DinhChuong
 */
@Service
@Transactional
public class UserSeviceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private GoogleUtils googleUtils;

    @Autowired
    private FacebookUtils facebookUtils;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public boolean isExistByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        return user != null;
    }

    @Override
    public boolean isExistByEmail(String email) {
        List<User> users = userRepository.getUserByEmail(email);
        boolean rs = users.size() > 0;
        return users.size() > 0;
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean isExitByEmailAndAuthProvider(String email, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<ModelResponse> loginByGoogle(Google googlePojo) {
        List<User> users = this.getUserByEmail(googlePojo.getEmail());
        String username = googlePojo.getEmail().split("@")[0];
        if (!this.isExistByEmail(googlePojo.getEmail())) {
            // Create user and save db
            googleUtils.createNewUserByGoogleAccount(googlePojo);
        } else {
           User currentUser = userRepository.getUserByUsernameAndAuthProvider(username, AuthenProviderEnums.GOOGLE.getValue());
            if (currentUser != null){
                UserDetails userDetail = googleUtils.buildUser(googlePojo);
                return jwtTokenProvider.createJwtResponseSuccess(userDetail);
            }
            return jwtTokenProvider.createJwtResponseFail();
        }
//        List<User> users = this.getUserByEmail(googlePojo.getEmail());
//        UserDetails userDetail = googleUtils.buildUser(users.get(0));
        UserDetails userDetail = googleUtils.buildUser(googlePojo);
        return jwtTokenProvider.createJwtResponseSuccess(userDetail);
    }

    @Override
    public ResponseEntity<ModelResponse> loginByFacebook(com.restfb.types.User facebookPojo) {
        List<User> users = this.getUserByEmail(facebookPojo.getEmail());
        String username = StringUtils.convert(facebookPojo.getName() + facebookPojo.getId());
        if (!this.isExistByUsername(username)) {
            // Create user and save db
            facebookUtils.createNewUserByFacebookAccount(facebookPojo);
        } else {
            User currentUser = userRepository.getUserByUsernameAndAuthProvider(username, AuthenProviderEnums.FACEBOOK.getValue());
            if (currentUser != null){
                UserDetails userDetail = facebookUtils.buildUser(facebookPojo);
                return jwtTokenProvider.createJwtResponseSuccess(userDetail);
            }
            return jwtTokenProvider.createJwtResponseFail();
        }
//        List<User> users = this.getUserByEmail(googlePojo.getEmail());
//        UserDetails userDetail = googleUtils.buildUser(users.get(0));
        UserDetails userDetail = facebookUtils.buildUser(facebookPojo);
        return jwtTokenProvider.createJwtResponseSuccess(userDetail);
    }

    @Override
    public User getUserByUsernameAndAuthProvider(String username, Integer id) {
        
        return userRepository.getUserByUsernameAndAuthProvider(username, id);
    }

}

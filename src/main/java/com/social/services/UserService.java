/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.services;

import com.social.dto.response.ModelResponse;
import com.social.pojo.Google;
import com.social.pojo.User;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author DinhChuong
 */
public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);
    
    User getUserByUsernameAndAuthProvider(String username, Integer id);
    
    boolean isExistByUsername(String username);

    List<User> getUserByEmail(String email);

    boolean isExistByEmail(String email);

    boolean isExitByEmailAndAuthProvider(String email, Long id);

    User save(User user);

    ResponseEntity<ModelResponse> loginByGoogle(Google googlePojo);
    
    
    ResponseEntity<ModelResponse> loginByFacebook(com.restfb.types.User facebookPojo);
}

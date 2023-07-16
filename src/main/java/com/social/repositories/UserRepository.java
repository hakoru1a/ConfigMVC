/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.repositories;

import com.social.pojo.User;
import java.util.List;

/**
 *
 * @author DinhChuong
 */
public interface UserRepository {
    
    User getUserByUsername(String username);
    
    List<User> getUserByEmail(String email);
    
    
    User save(User user);
    
    User getUserByUsernameAndAuthProvider(String username, Integer id);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.enums;

/**
 *
 * @author DinhChuong
 */
public enum AuthenProviderEnums {
    SYSTEM(1),
    GOOGLE(2),
    FACEBOOK(3);
    
    private final int value;
    
    AuthenProviderEnums(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

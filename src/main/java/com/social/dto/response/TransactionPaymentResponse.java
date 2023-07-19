/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author DinhChuong
 */
@Getter
@Setter
public class TransactionPaymentResponse {

    private String status;
    private String message;
    private String data;
}

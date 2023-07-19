///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.social.apis;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.social.dto.response.PaymentResponse;
//import com.social.dto.response.TransactionPaymentResponse;
//import com.social.payment.VnPayConfig;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.TimeZone;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// *
// * @author DinhChuong
// */
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentAPI {
//
//    @GetMapping("/create-payment")
//    public ResponseEntity<PaymentResponse> createPayment(HttpServletRequest req) throws UnsupportedEncodingException {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
////        String orderType = req.getParameter("ordertype");
//        long amount = 1000000;
////        String bankCode = req.getParameter("bankCode");
//        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
//        String vnp_IpAddr = VnPayConfig.getIpAddress(req);
//        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_BankCode", "NCB");
////        if (bankCode != null && !bankCode.isEmpty()) {
////            vnp_Params.put("vnp_BankCode", bankCode);
////        }
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
////        vnp_Params.put("vnp_OrderType", orderType);
//
//        vnp_Params.put("vnp_Locale", "vn");
//
////        String locate = req.getParameter("language");
////        if (locate != null && !locate.isEmpty()) {
////            vnp_Params.put("vnp_Locale", locate);
////        } else {
////            vnp_Params.put("vnp_Locale", "vn");
////        }
//        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
//
//        PaymentResponse res = new PaymentResponse();
//        res.setURL(paymentUrl);
//        res.setMessage("Thanh toán thành công");
//        res.setStatus("Success");
//
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//    }
//
////    @GetMapping("/payment-info")
////    public ResponseEntity<TransactionPaymentResponse> createPayment(
////            @RequestParam(value = "vnp_Amount", required = false) String amount,
////            @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
////            @RequestParam(value = "vnp_OrderInfo", required = false) String order,
////            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode
////    ) {
////        TransactionPaymentResponse res = new TransactionPaymentResponse();
////        if (responseCode.equals("00")) {
////            res.setStatus("Success");
////            res.setMessage("Thanh toán thành công");
////        } else {
////            res.setStatus("Fail");
////            res.setMessage("Thanh toán không thành công");
////        }
////        return ResponseEntity.status(HttpStatus.OK).body(res);
////    }
//}

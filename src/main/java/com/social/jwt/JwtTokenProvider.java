/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.jwt;

/**
 *
 * @author DinhChuong
 */
import com.social.dto.response.JwtResponse;
import com.social.dto.response.ModelResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component
public class JwtTokenProvider implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 60 * 60;

    public static final long JWT_REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 30;
//    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private String secret = "wypStTfghXLnNwGLfxASZIorpg5vPO+bSMAG0da9h+9ZDtVawaoDwbMYJ55EqmsT";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails, int type) {
        Map<String, Object> claims = new HashMap<>();
        long expireTime = type == 0 ? JWT_TOKEN_VALIDITY : JWT_REFRESH_TOKEN_VALIDITY;
        return doGenerateToken(claims, userDetails.getUsername(), expireTime);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long expireTime) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public ResponseEntity<ModelResponse> getJwtResponse(String token, String msg, String code, HttpStatus statusCode, String refreshToken) {
        final Date expirationDate = this.getExpirationDateFromToken(token);
        String ex = expirationDate.toString();

        return ResponseEntity.status(statusCode).body(new ModelResponse(code, msg, new JwtResponse(token, ex, refreshToken)));
    }

    public ResponseEntity<ModelResponse> createJwtResponseSuccess(UserDetails userDetail) {
        final String token = this.generateToken(userDetail, 1);
        final String refreshToken = this.generateToken(userDetail, 2);
        return getJwtResponse(token, "Get auth token successfully", "200", HttpStatus.OK, refreshToken);
    }

    public ResponseEntity<ModelResponse> createJwtResponseFail() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ModelResponse("400", "Something went wrong about authentication", null));
    }

}

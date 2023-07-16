/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.apis;

import com.social.jwt.JwtTokenProvider;
import com.social.dto.request.UserCredentialRequest;
import com.social.dto.response.JwtResponse;
import com.social.dto.response.ModelResponse;
import com.social.pojo.Google;
import com.social.services.UserService;
import com.social.utils.FacebookUtils;
import com.social.utils.GoogleUtils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author DinhChuong
 */
@RestController
@CrossOrigin
public class AuthenticateAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private GoogleUtils googleUtils;
    
    @Autowired
    private FacebookUtils facebookUtils;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<ModelResponse> createAuthenticationToken(@RequestBody UserCredentialRequest authenticationRequest) throws Exception {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = userService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenProvider.generateToken(userDetails, 0);
            final String refershToken = jwtTokenProvider.generateToken(userDetails, 1);
            final Date expirationDate = this.jwtTokenProvider.getExpirationDateFromToken(token);
            String ex = expirationDate.toString();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ModelResponse("200", "Get auth token successfully",
                            new JwtResponse(token, ex, refershToken))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ModelResponse("400", "Get auth token failed", null)
            );
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.POST)
    public ResponseEntity<ModelResponse> refreshtoken(@RequestBody String refreshToken) {
        Boolean isExpiredToken = jwtTokenProvider.isTokenExpired(refreshToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ModelResponse("401", "Refresh token failed", null)
            );
        }
        if (isExpiredToken) {
            refreshToken = jwtTokenProvider.generateToken(userDetails, 1);
        }
        String newRefreshToken = jwtTokenProvider.generateToken(userDetails, 0);
        final Date expirationDate = this.jwtTokenProvider.getExpirationDateFromToken(newRefreshToken);
        String ex = expirationDate.toString();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ModelResponse("200", "Get auth token successfully",
                        new JwtResponse(newRefreshToken, ex, refreshToken))
        );
    }

    @RequestMapping("/login-google")
    public ResponseEntity<ModelResponse> loginGoogle(HttpServletRequest request) {
        try {
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ModelResponse("400", "Get auth token fail", null));
            }
            String accessToken = googleUtils.getToken(code);
            Google googlePojo = googleUtils.getUserInfo(accessToken);
            return userService.loginByGoogle(googlePojo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ModelResponse("400", "Get auth token fail", null));
        }
    }
    
    @RequestMapping("/login-facebook")
    public ResponseEntity<ModelResponse> loginFacebook(HttpServletRequest request) {
        try {
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ModelResponse("400", "Get auth token fail", null));
            }
            
            String accessToken = facebookUtils.getToken(code);
            com.restfb.types.User  facebookInfo =  facebookUtils.getUserInfo(accessToken);
            return userService.loginByFacebook(facebookInfo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ModelResponse("400", "Get auth token fail", null));
        }
    }
}

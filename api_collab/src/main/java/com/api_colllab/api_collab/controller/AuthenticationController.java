package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.AuthLoginRequest;
import com.api_colllab.api_collab.controller.dto.AuthRegisterRequest;
import com.api_colllab.api_collab.controller.dto.AuthResponse;
import com.api_colllab.api_collab.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRegisterRequest authRegisterUser){
        try{
            return new ResponseEntity<>(this.userDetailsService.register(authRegisterUser),HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(null, e.getMessage(), null,null,false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.login(userRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        try {
            return new ResponseEntity<>(this.userDetailsService.refresh(refreshToken), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Refresh token inválido", null, null,false));
        }
    }



}

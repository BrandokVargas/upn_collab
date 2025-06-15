package com.api_colllab.api_collab.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAuthController {


    @GetMapping("/pong")
    public String ApiRunCorrect(){
        return "PONG";
    }

    @GetMapping("/pong_secured")
    public String ApiRunCorrectSecured(){
        return "PONG SECURED";
    }

    @GetMapping("/pong_users")
    public String SecuredUser(){
        return "PONG SECURED USER";
    }

    @GetMapping("/pong_admin")
    public String SecuredAdmin(){
        return "PONG SECURED ADMIN";
    }
}

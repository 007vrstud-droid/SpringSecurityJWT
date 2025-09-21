package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/user")
    public String getUser(){
        return "Im user";
    }
    @GetMapping("/moderator")
    public String getModerator(){
        return "Im moderator";
    }
    @GetMapping("/admin")
    public String getAdmin(){
        return "Im admin";
    }


}

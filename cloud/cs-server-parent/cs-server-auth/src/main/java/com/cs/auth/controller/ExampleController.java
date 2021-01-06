package com.cs.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ExampleController {
    @RequestMapping("/example")
    public String mi(){
        return "hi body!";
    }
    @GetMapping("/testpost")
    public String testpost(){
        return "hi body!";
    }

    @PostMapping("/tp")
    public String postTest(){
        return "hi body~~~~!";
    }

    @PostMapping("/tp2")
    public String postTest2(){
        return "hi body2~~~~!"+ LocalDateTime.now();
    }
}

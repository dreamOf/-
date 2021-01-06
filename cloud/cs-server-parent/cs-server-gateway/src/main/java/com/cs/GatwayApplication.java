package com.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
public class GatwayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatwayApplication.class,args);
    }

    @Controller
    class TestController{
        @RequestMapping("/gay")
        public String mi(){
            return "hi body!";
        }
    }

}

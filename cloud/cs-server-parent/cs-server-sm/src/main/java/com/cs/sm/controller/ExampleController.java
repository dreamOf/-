package com.cs.sm.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/sm")
@Api(tags = "系统测试")
public class ExampleController {
    @GetMapping("/ex")
    public String hi(){
        return "当前时间"+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

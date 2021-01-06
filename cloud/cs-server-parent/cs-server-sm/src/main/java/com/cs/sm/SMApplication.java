package com.cs.sm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SMApplication {
    public static void main(String[] args) {
        SpringApplication.run(SMApplication.class,args);
    }
}

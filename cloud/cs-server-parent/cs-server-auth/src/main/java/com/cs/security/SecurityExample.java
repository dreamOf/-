package com.cs.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityExample {
    public static void example23() {
        User user = (User) User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("user")
                .build();
        System.out.println("user's passowrd is :"+user.getPassword());


    }

    /**
     * 1秒钟验证密码
     */
    public static void bCryptPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode("1234");
        System.out.println("===开始解码"+result);
        System.out.println(encoder.matches("secret", result));
    }
    /**
     * 1秒钟验证密码
     */
    public static void argon2PasswordEncoder (){

    }

    public static void main(String[] args) {
        bCryptPasswordEncoder();
    }
}

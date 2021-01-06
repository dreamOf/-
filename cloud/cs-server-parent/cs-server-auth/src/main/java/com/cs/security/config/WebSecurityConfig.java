package com.cs.security.config;

import com.cs.auth.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean // password 模式需要此bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyUserDetailService userDetailService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
      .authorizeRequests()
               .anyRequest().permitAll()
       .and().formLogin()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1内存方式配置用户信息
      /*  auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder.encode("1234"))
            .authorities("product");*/
        //2数据库方式
        auth.userDetailsService(userDetailService);
    }





}

package com.cs.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RES_ID = "RES";
    @Autowired
    private TokenStore tokenStore;

    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId(RES_ID)
                .tokenStore(tokenStore)
               // .tokenServices(tokenServices())
                .stateless(true);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                mvcMatchers("/sm/**","/v2/**").permitAll().
                anyRequest().authenticated();
    }

   /* @Bean
    public ResourceServerTokenServices tokenServices() {
       //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl("http://localhost:8086/oauth/check_token");
        service.setClientId("c1");
        service.setClientSecret("secret");
        return service;
    }*/
}

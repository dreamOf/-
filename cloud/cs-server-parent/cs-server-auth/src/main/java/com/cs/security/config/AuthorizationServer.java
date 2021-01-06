package com.cs.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    //令牌的存储策略，endpoint使用
    @Autowired
    private TokenStore tokenStore;
    /*@Autowired
    private ClientDetailsService clientDetailsServices;*/
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired//authorization_code类型使用
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired//密码模式使用
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;

    //认证配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        // 认证后可访问 /oauth/check_token , 默认拒绝访问
        security.checkTokenAccess("isAuthenticated()");
        //允许表单认证
        security.allowFormAuthenticationForClients();
    }

    //配置用户信息
    @Override
    public void configure(ClientDetailsServiceConfigurer client) throws Exception {
       /* client.inMemory()
                .withClient("c1")
                .secret(new BCryptPasswordEncoder().encode("secret"))
                .resourceIds("RES")
                .authorizedGrantTypes("authorization_code",
                        "password", "client_credentials", "implicit", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                .redirectUris("http://www.baidu.com");*/
       client.withClientDetails(clientDetailsServices());
    }

    //访问端点控制
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoint) throws Exception {
        endpoint.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(tokenService())//令牌管理服务，各模式都需要
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);//
    }

   /* @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//客户端信息
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        service.setAccessTokenValiditySeconds(7200);//令牌有效时间：2小时
        service.setRefreshTokenValiditySeconds(7200);//刷新令牌默认有效时间：2小时
        return service;
    }*/
   @Autowired
   private JwtAccessTokenConverter accessTokenConverter;
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsServices());
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    @Bean
    public ClientDetailsService clientDetailsServices() {
        /*ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService)clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;*/
        ClientDetailsService clientDetailsService =new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService)clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

}

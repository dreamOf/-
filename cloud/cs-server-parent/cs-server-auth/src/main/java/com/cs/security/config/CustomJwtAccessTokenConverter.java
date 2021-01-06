package com.cs.security.config;

import com.cs.auth.entity.SysUserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 *自定义JwtAccessTokenConverter转换器，向token中添加额外信息
 * csq
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
    private static String USER_INFO = "user_info";
    /**
     * 生成token
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

        Object obj = authentication.getPrincipal();
        if(obj instanceof SysUserEntity){
            // 设置额外用户信息
            SysUserEntity sysUserEntity = (SysUserEntity) authentication.getPrincipal();
            //清空额外用户信息的权限
            sysUserEntity.setAuthorities(null);
            String str = objToStr(sysUserEntity);
            // 将用户信息添加到token额外信息中
            defaultOAuth2AccessToken.getAdditionalInformation().put(USER_INFO, str);
        }
        return super.enhance(defaultOAuth2AccessToken, authentication);
    }

    /**
     * 解析token
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map){
        OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
        return oauth2AccessToken;
    }

    private void convertData(OAuth2AccessToken accessToken,  Map<String, ?> map) {
        accessToken.getAdditionalInformation().put(USER_INFO,convertUserData(map.get(USER_INFO)));
    }

    private SysUserEntity convertUserData(Object userInfo)  {
        ObjectMapper objectMapper = new ObjectMapper();
        SysUserEntity sysUserEntity = null;
        try {
              sysUserEntity = objectMapper.readValue(userInfo.toString(), SysUserEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
         return sysUserEntity;
    }

    private String objToStr(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        String str=null;
        try {
            str=objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
    private String strToOjbe(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        String str=null;
        try {
            str=objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}

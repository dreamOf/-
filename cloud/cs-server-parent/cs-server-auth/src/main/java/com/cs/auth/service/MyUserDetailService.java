package com.cs.auth.service;

import com.cs.auth.dao.SysUserDao;
import com.cs.auth.entity.SysAuthoritiesEntity;
import com.cs.auth.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity sysUserEntity = queryUser(username);
        if(sysUserEntity == null){
            throw  new UsernameNotFoundException("用户名或密码错误！");
        }
        List<SysAuthoritiesEntity> auths = queryAuthirities(username);
        sysUserEntity.setAuthorities(auths);
        //如果是超级管理员拥有所有权限

        return sysUserEntity;
    }
    private SysUserEntity queryUser(String username){
        /*SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUsername("admin");
        String psw = passwordEncoder.encode("1234");
        sysUserEntity.setPassword(psw);
        sysUserEntity.setState(0);
        System.out.println("获得用户信息。");*/
        SysUserEntity sysUserEntity = sysUserDao.queryUser(username);
        return sysUserEntity;
    }
    private List<SysAuthoritiesEntity> queryAuthirities(String username){
         return sysUserDao.queryUserAuthorities(username);
    }


}

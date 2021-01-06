package com.cs.auth.dao;

import com.cs.auth.entity.SysAuthoritiesEntity;
import com.cs.auth.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SysUserEntity queryUser(String username){
        String sql ="select * from sys_user where username =?";
        return jdbcTemplate.queryForObject(sql,new Object[]{username}, BeanPropertyRowMapper.newInstance(SysUserEntity.class));
    }

    public List<SysAuthoritiesEntity> queryUserAuthorities(String username){
        String sql ="select * from sys_role_authority a " +
                "where exists(select 1 from sys_role_authority b " +
                "               where a.id = b.authority_id " +
                "                 and b.role_Id in(select roleId from sys_user_role c where c.userid = ?)" +
                ")";
        return jdbcTemplate.queryForList(sql,new Object[]{username}, SysAuthoritiesEntity.class);
    }
}

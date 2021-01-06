package com.cs.auth.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SysUserEntity  implements UserDetails {

    private String id;
    private String username;
    private String password;
    private String nickName;
    private String avatar;
    private String sex;
    private String phone;
    private String email;
    private Integer emailVerified;
    private String trueName;
    private String idCard;
    private LocalDate birthday;
    /**状态，0正常，1冻结*/
    private Integer state;
    private String createName;
    private String createBy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String sysOrgCode;
    private List<SysAuthoritiesEntity> authorities;  // 权限
    private List<SysRoleEntity> roles;  // 角色

    @Override
    public List<SysAuthoritiesEntity> getAuthorities() {
        return this.authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

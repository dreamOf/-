package com.cs.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class SysAuthoritiesEntity implements GrantedAuthority {
    private String authority;
}

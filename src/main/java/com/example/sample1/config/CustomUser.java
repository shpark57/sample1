package com.example.sample1.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    private String tenantId;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String tenantId) {
        super(username, password, authorities);
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}

package com.mirna.busmanagement.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_PASSENGER,
    ROLE_DRIVER;

    @Override
    public String getAuthority() {
        return name();
    }
}

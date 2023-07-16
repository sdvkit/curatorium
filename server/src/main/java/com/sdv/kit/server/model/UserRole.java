package com.sdv.kit.server.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    ADMIN, TEACHER, STUDENT;

    @Override
    public String getAuthority() {
        return name();
    }
}

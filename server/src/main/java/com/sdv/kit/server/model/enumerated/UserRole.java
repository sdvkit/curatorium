package com.sdv.kit.server.model.enumerated;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    TEACHER, STUDENT, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

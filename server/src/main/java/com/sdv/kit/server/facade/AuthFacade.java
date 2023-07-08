package com.sdv.kit.server.facade;

import org.springframework.security.core.Authentication;

public interface AuthFacade {

    Authentication getAuthentication();

    String getName();
}

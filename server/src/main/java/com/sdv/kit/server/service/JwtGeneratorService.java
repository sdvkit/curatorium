package com.sdv.kit.server.service;

import com.sdv.kit.server.model.Jwt;
import org.springframework.security.core.Authentication;

public interface JwtGeneratorService {
    Jwt generate(Authentication authentication);
}

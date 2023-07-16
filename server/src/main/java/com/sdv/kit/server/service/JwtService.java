package com.sdv.kit.server.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public interface JwtService {

    String extractUsername(String token);

    Date extractExpiration(String token);

    Claims extractAllClaims(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token, UserDetails userDetails);

    String generateToken(String userName);

    String createToken(Map<String, Object> claims, String userName);

    Key getSignKey();
}

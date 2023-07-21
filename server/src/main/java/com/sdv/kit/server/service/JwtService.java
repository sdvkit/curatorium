package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.UserDto;
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

    String generateToken(UserDto userDto);

    String createToken(Map<String, Object> claims, UserDto userDto);

    Key getSignKey();
}

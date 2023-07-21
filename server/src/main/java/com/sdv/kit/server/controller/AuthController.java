package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserLoginDto;
import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.model.Jwt;
import com.sdv.kit.server.service.JwtService;
import com.sdv.kit.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/login")
    @SneakyThrows
    public ResponseEntity<Jwt> login(@RequestBody UserLoginDto userLoginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.username(), userLoginDto.password()));

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("There's no user with this username");
        }

        final UserDto userInfoDto = userService.getUserInfo(userLoginDto.username()).get();
        final String tokenValue = jwtService.generateToken(userInfoDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Jwt(tokenValue));
    }

    @PostMapping("/registration")
    @SneakyThrows
    public ResponseEntity<UserDto> registration(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService
                        .register(userRegistrationDto)
                        .get());
    }
}

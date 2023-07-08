package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.user.UserLoginDto;
import com.sdv.kit.server.dto.user.UserRegistrationDto;
import com.sdv.kit.server.model.Jwt;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.service.JwtService;
import com.sdv.kit.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Jwt> login(@RequestBody UserLoginDto userLoginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.username(), userLoginDto.password()));

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user request");
        }

        final String tokenValue = jwtService.generateToken(userLoginDto.username());
        return new ResponseEntity<>(new Jwt(tokenValue), HttpStatus.OK);
    }

    @PostMapping("/registration")
    @SneakyThrows
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Optional<User> registeredUser = userService.register(userRegistrationDto);
        return registeredUser.isPresent()
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}

package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserRenameDto;
import com.sdv.kit.server.model.Jwt;
import com.sdv.kit.server.service.JwtService;
import com.sdv.kit.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @PatchMapping("/{username}")
    @SneakyThrows
    public ResponseEntity<Jwt> renameUser(@PathVariable String username,
                                          @RequestBody @Valid UserRenameDto userRenameDto,
                                          BindingResult bindingResult,
                                          Authentication authentication) {
        if (bindingResult.hasErrors() || !username.equals(authentication.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final UserDto renamedUserDto = userService.rename(username, userRenameDto).get();
        final String tokenValue = jwtService.generateToken(renamedUserDto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Jwt(tokenValue));
    }
}

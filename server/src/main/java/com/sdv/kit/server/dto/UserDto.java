package com.sdv.kit.server.dto;

import com.sdv.kit.server.model.UserRole;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UserDto(@NotNull Long id,
                      @NotNull String fullName,
                      @NotNull String username,
                      @NotNull UserRole role,
                      @NotNull Boolean isLoggedIn) implements Serializable {
}
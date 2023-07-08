package com.sdv.kit.server.dto.user;

import com.sdv.kit.server.model.enumerated.UserRole;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UserDto(@NotNull Long id,
                      @NotNull String fullName,
                      @NotNull String username,
                      @NotNull UserRole role) implements Serializable {
}
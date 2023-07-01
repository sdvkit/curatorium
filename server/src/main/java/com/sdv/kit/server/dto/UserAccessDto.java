package com.sdv.kit.server.dto;

import com.sdv.kit.server.model.enumerated.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserAccessDto(@NotNull @Size(min = 4, max = 15) @NotEmpty String username,
                            @NotNull UserRole role) implements Serializable {
}
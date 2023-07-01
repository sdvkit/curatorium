package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserRegistrationDto(@NotNull @Size(min = 4, max = 25) String fullName,
                                  @NotNull @Size(min = 4, max = 15) String username,
                                  @NotNull @Size(min = 6, max = 30) String password) implements Serializable {
}
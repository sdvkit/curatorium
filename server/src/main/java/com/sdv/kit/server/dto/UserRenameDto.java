package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserRenameDto(@NotNull @Size(min = 4, max = 25) String fullName,
                            @NotNull @Size(min = 4, max = 15) String username) implements Serializable {
}
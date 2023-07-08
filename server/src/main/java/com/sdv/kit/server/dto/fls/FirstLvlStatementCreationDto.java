package com.sdv.kit.server.dto.fls;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record FirstLvlStatementCreationDto(@NotNull @Size(min = 4, max = 50) String name) implements Serializable {
}
package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public record SubjectNestedDto(@NotNull @Positive Long id) implements Serializable {
}
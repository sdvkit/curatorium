package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record SubjectRenameDto(@NotNull @Size(min = 4, max = 50) String name) implements Serializable {
}
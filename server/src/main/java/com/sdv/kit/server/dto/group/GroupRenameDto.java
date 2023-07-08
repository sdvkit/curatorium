package com.sdv.kit.server.dto.group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record GroupRenameDto(@NotNull @Size(min = 4, max = 15) String name) implements Serializable {
}
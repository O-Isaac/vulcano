package io.github.isaac.vulcano.dtos.objeto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CreateRequest(
        @NotNull(message = "El nombre es obligatorio ")
        @NotEmpty(message = "El nombre no puede estar vacio")
        String nombre,
        @NotNull(message = "La descripción es obligatoria")
        @NotEmpty(message = "La descripción no puede estar vacia.")
        String desc
) implements Serializable { }
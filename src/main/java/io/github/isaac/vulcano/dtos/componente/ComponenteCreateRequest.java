package io.github.isaac.vulcano.dtos.componente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Componente}
 */
public record ComponenteCreateRequest(
        @Positive(message = "La cantidad debe ser mayor a cero")
        @NotNull(message = "La cantidad es obligatoria")
        Integer cantidad,
        @NotNull(message = "El id de recurso es obligatorio")
        Integer recursoId
        ) implements Serializable {
}
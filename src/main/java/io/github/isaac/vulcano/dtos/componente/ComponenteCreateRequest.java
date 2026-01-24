package io.github.isaac.vulcano.dtos.componente;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Componente}
 */
public record ComponenteCreateRequest(
        @NegativeOrZero(message = "La cantidad no puede ser negativa")
        @NotNull(message = "La cantidad es obligatoria")
        Integer cantidad
) implements Serializable {
}
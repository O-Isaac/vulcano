package io.github.isaac.vulcano.dtos.plano;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Plano}
 */
public record PlanoCreateRequest(
        @NotNull(message = "El coste no puede ser nulo")
        @PositiveOrZero(message = "El coste debe ser igual o mayor a cero")
        Long coste,

        @NotNull(message = "La descripción es obligatoria")
        @NotEmpty(message = "La descripción no puede estar vacía")
        String desc,

        @NotNull(message = "El nombre es obligatorio")
        @NotEmpty(message = "El nombre no puede estar vacío")
        String nombre,

        @NotNull(message = "El tiempo de construcción es obligatorio")
        @PositiveOrZero(message = "El tiempo de construcción no puede ser negativo")
        Long tiempoConstrucion,

        @NotNull(message = "El recursoFabricadoId es obligatorio")
        @Positive(message = "El recursoFabricadoId debe ser mayor a cero")
        Integer recursoFabricadoId
) implements Serializable {
}
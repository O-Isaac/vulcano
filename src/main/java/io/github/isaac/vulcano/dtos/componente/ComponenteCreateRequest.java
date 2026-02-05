package io.github.isaac.vulcano.dtos.componente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Componente}
 */
@Schema(description = "Datos para crear un componente (recurso requerido por un plano)")
public record ComponenteCreateRequest(
        @Schema(description = "Cantidad del recurso requerida", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "La cantidad debe ser mayor a cero")
        @NotNull(message = "La cantidad es obligatoria")
        Integer cantidad,

        @Schema(description = "ID del recurso necesario", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El id de recurso es obligatorio")
        Integer recursoId
) implements Serializable {
}
package io.github.isaac.vulcano.dtos.queue;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Queue}
 */
@Schema(description = "Datos para iniciar la construcción de un objeto según un plano")
public record QueueCreateRequest(
        @Schema(description = "ID del plano a fabricar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El id del plano es obligatorio")
        @Positive(message = "El id tiene que ser mayor que 0")
        Integer planoId
) implements Serializable {
}
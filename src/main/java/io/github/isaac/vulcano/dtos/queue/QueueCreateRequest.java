package io.github.isaac.vulcano.dtos.queue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Queue}
 */
public record QueueCreateRequest(
        @NotNull(message = "El id del plano es obligatorio")
        @Positive(message = "El id tiene que ser mayor que 0")
        Integer planoId
) implements Serializable {
}
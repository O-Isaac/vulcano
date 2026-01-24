package io.github.isaac.vulcano.dtos.queue;

import io.github.isaac.vulcano.dtos.jugador.JugadorResponse;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Queue}
 */
public record QueueResponse(
        Integer id,
        Instant inicioTime,
        Instant finalTime,
        String estado,
        JugadorResponse jugador,
        PlanoResponse plano
) implements Serializable {}
package io.github.isaac.vulcano.dtos.jugador;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Jugador}
 */
public record JugadorResponse(Integer id, Integer nivel, String correo, Long creditos, String role) implements Serializable {
}
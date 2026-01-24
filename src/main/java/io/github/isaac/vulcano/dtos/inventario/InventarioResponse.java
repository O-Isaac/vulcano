package io.github.isaac.vulcano.dtos.inventario;

import io.github.isaac.vulcano.dtos.jugador.JugadorResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Inventario}
 */
public record InventarioResponse(RecursoResponse recurso, JugadorResponse jugador,
                                 Integer cantidad) implements Serializable {
}
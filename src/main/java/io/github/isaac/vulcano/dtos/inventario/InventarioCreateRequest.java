package io.github.isaac.vulcano.dtos.inventario;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Inventario}
 */
public record InventarioCreateRequest(Integer recursoId, Integer jugadorId,
                                      Integer cantidad) implements Serializable {
}
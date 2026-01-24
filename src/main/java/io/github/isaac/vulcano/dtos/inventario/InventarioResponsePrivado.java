package io.github.isaac.vulcano.dtos.inventario;

import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Inventario}
 */
public record InventarioResponsePrivado(RecursoResponse recurso, Integer cantidad) implements Serializable {
}
package io.github.isaac.vulcano.dtos.recursos;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Recurso}
 */
public record RecursoUpdateRequest(String nombre, String desc, String rareza) implements Serializable {
}
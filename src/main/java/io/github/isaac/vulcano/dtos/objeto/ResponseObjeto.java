package io.github.isaac.vulcano.dtos.objeto;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Objeto}
 */
public record ResponseObjeto(Integer id, String nombre, String desc) implements Serializable {
}
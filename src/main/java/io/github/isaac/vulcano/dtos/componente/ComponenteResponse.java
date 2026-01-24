package io.github.isaac.vulcano.dtos.componente;

import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Componente}
 */
public record ComponenteResponse(Integer id, Integer cantidad, PlanoResponse plano,
                                 RecursoResponse recurso) implements Serializable {
}
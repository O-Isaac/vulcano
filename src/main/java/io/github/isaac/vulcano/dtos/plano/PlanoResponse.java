package io.github.isaac.vulcano.dtos.plano;

import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;


import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Plano}
 */
public record PlanoResponse(
        Integer id,
        Long coste,
        String desc,
        String nombre,
        Long tiempoConstrucion,
        RecursoResponse recursoFabricado,
        List<ComponenteResponse> componentes
) implements Serializable {}
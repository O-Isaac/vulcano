package io.github.isaac.vulcano.dtos.plano;

import io.github.isaac.vulcano.entities.Componente;
import io.github.isaac.vulcano.entities.Recurso;

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
        Recurso recursoFabricado,
        List<Componente> componentes
) implements Serializable {}
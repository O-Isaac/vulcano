package io.github.isaac.vulcano.dtos.plano;

import io.github.isaac.vulcano.dtos.objeto.ResponseObjeto;
import io.github.isaac.vulcano.entities.Componente;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Plano}
 */
public record PlanoResponse(Integer id, Long coste, String desc, String nombre, Long tiempoConstrucion, List<Componente> componentes) implements Serializable {
  }
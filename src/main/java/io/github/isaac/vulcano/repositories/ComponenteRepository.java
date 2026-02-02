package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Componente;
import io.github.isaac.vulcano.entities.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponenteRepository extends JpaRepository<Componente, Integer> {
    List<Componente> findByPlanoId(Integer planoId);

    boolean existsByPlanoIdAndRecursoId(Integer planoId, Integer recursoId);
}
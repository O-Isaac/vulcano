package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Componente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponenteRepository extends JpaRepository<Componente, Integer> {
}
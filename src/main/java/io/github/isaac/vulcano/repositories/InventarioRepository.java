package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Inventario;
import io.github.isaac.vulcano.entities.InventarioId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, InventarioId> {
}
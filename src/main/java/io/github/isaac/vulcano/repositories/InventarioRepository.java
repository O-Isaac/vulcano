package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Inventario;
import io.github.isaac.vulcano.entities.InventarioId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, InventarioId> {
    Optional<Inventario> findByJugador_CorreoAndRecurso_Id(String correo, Integer recursoId);
    List<Inventario> findByJugadorId(Integer jugadorId);
}
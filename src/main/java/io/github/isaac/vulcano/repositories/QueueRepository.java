package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.dtos.queue.QueueResponse;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.Plano;
import io.github.isaac.vulcano.entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Integer> {
    boolean existsByJugadorAndPlanoAndEstado(Jugador jugador, Plano plano, String estado);
    List<Queue> findByEstadoAndFinalTimeBefore(String estado, Instant finalTimeBefore);
    List<Queue> findByJugador_Id(Integer jugadorId);
}
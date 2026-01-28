package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.Plano;
import io.github.isaac.vulcano.entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Integer> {
    boolean existsByJugadorAndPlanoAndEstado(Jugador jugador, Plano plano, String estado);
    List<Queue> findByJugador_Id(Integer jugadorId);

    @Query("SELECT q FROM Queue q " +
            "JOIN FETCH q.plano p " +
            "JOIN FETCH q.jugador j " +
            "WHERE q.estado = :estado AND q.finalTime < :time")
    List<Queue> findByEstadoAndFinalTimeBefore(@Param("estado") String estado, @Param("time") Instant time);


    @Query("SELECT q FROM Queue q " +
            "JOIN FETCH q.plano p " +
            "JOIN FETCH p.recursoFabricado r " +
            "WHERE q.jugador.id = :jugadorId " +
            "AND q.estado = 'EN_CONSTRUCCION'")
    List<Queue> findAllActivos(@Param("jugadorId") Integer jugadorId);
}
package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByJugador(Jugador jugador);
}
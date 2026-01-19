package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JugadoreRepository extends JpaRepository<Jugador, Integer> {
    Optional<Jugador> findByCorreo(String correo);
}
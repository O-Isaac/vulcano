package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.exceptions.BadRequestException;
import io.github.isaac.vulcano.exceptions.UserAlreadyExistsException;
import io.github.isaac.vulcano.mappers.JugadorMapper;
import io.github.isaac.vulcano.repositories.JugadoreRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JugadorService implements UserDetailsService {

    private final JugadoreRepository jugadoreRepository;
    private final JugadorMapper jugadorMapper;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jugadoreRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public Jugador registrar(RegisterRequest request) {
        if (!request.password().equals(request.secondPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        if (jugadoreRepository.findByCorreo(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("El correo " + request.username() + " ya está en uso");
        }

        Jugador nuevoJugador = jugadorMapper.toEntity(request);
        return jugadoreRepository.save(nuevoJugador);
    }
}
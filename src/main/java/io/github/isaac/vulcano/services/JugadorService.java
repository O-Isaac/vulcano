package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.dtos.jugador.JugadorResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public List<JugadorResponse> getJugadores() {
        return jugadoreRepository.findAll()
                .stream()
                .map(jugadorMapper::toResponse)
                .toList();
    }

    @Transactional
    public JugadorResponse addCreditos(Integer jugadorId, Long cantidad) {
        Jugador jugador = jugadoreRepository.findById(jugadorId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        jugador.setCreditos(jugador.getCreditos() + cantidad);

        return jugadorMapper.toResponse(jugadoreRepository.save(jugador));
    }
}
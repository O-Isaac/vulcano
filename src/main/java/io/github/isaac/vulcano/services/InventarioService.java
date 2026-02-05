package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponsePrivado;
import io.github.isaac.vulcano.entities.Inventario;
import io.github.isaac.vulcano.entities.InventarioId;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.mappers.InventarioMapper;
import io.github.isaac.vulcano.repositories.InventarioRepository;
import io.github.isaac.vulcano.repositories.JugadoreRepository;
import io.github.isaac.vulcano.repositories.RecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final JugadoreRepository jugadoreRepository;
    private final RecursoRepository recursoRepository;

    public List<InventarioResponse> listar() {
        return inventarioRepository.findAll()
                .stream()
                .map(inventarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventarioResponsePrivado> listarPrivado(Jwt jwt) {
        Jugador jugador = jugadoreRepository.findByCorreo(jwt.getSubject())
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));

        return inventarioRepository.findByJugadorId(jugador.getId())
                .stream()
                .map(inventarioMapper::toResponsePublic)
                .toList();
    }

    @Transactional
    public InventarioResponse crearInventario(Integer jugadorId, Integer recursoId) {
        Inventario inventario = obtenerOInicializar(jugadorId, recursoId);

        inventario.setCantidad(0);

        return inventarioMapper.toResponse(inventarioRepository.save(inventario));
    }

    @Transactional
    public InventarioResponsePrivado crearInventarioPrivado(Jwt jwt, Integer recursoId) {
        Jugador jugador = jugadoreRepository.findByCorreo(jwt.getSubject())
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));

        Inventario inventario = obtenerOInicializar(jugador.getId(), recursoId);
        inventario.setCantidad(0);

        return inventarioMapper.toResponsePublic(
                inventarioRepository.save(inventario)
        );
    }

    private Inventario obtenerOInicializar(Integer jugadorId, Integer recursoId) {
        InventarioId id = new InventarioId(recursoId, jugadorId);

        return inventarioRepository.findById(id)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setId(id);
                    log.info("Creando Inventario al jugador {}", jugadorId);
                    // Usamos referencias para no cargar las entidades completas de la BD
                    nuevo.setJugador(jugadoreRepository.getReferenceById(jugadorId));
                    nuevo.setRecurso(recursoRepository.getReferenceById(recursoId));
                    nuevo.setCantidad(0);

                    return nuevo;
                });
    }

    @Transactional
    public InventarioResponse actualizarCantidad(Integer jugadorId, Integer recursoId, Integer nuevaCantidad) {
        InventarioId id = new InventarioId(recursoId, jugadorId);

        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el inventario para actualizar"));

        inventario.setCantidad(nuevaCantidad);

        return inventarioMapper.toResponse(inventarioRepository.save(inventario));
    }

    public Optional<InventarioResponse> buscarPorId(Integer jugadorId, Integer recursoId) {
        return inventarioRepository.findById(new InventarioId(recursoId, jugadorId))
                .map(inventarioMapper::toResponse);
    }

    @Transactional
    public void eliminar(Integer jugadorId, Integer recursoId) {
        InventarioId id = new InventarioId(recursoId, jugadorId);
        inventarioRepository.deleteById(id);
    }
}
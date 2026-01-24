package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.inventario.InventarioCreateRequest;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.entities.Inventario;
import io.github.isaac.vulcano.entities.InventarioId;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.Recurso;
import io.github.isaac.vulcano.mappers.InventarioMapper;
import io.github.isaac.vulcano.repositories.InventarioRepository;
import io.github.isaac.vulcano.repositories.JugadoreRepository;
import io.github.isaac.vulcano.repositories.RecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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

    @Transactional
    public InventarioResponse entregarRecurso(InventarioCreateRequest request) {
        Inventario inventario = obtenerOInicializar(request.jugadorId(), request.recursoId());

        // Simplemente actualizamos la cantidad
        inventario.setCantidad(inventario.getCantidad() + request.cantidad());

        return inventarioMapper.toResponse(inventarioRepository.save(inventario));
    }

    /**
     * Método privado para encapsular la lógica de búsqueda o creación.
     * Mantiene el método principal enfocado en la lógica de negocio.
     */
    private Inventario obtenerOInicializar(Integer jugadorId, Integer recursoId) {
        InventarioId id = new InventarioId(recursoId, jugadorId);

        return inventarioRepository.findById(id)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setId(id);
                    // Usamos referencias para no cargar las entidades completas de la BD
                    nuevo.setJugador(jugadoreRepository.getReferenceById(jugadorId));
                    nuevo.setRecurso(recursoRepository.getReferenceById(recursoId));
                    nuevo.setCantidad(0);
                    return nuevo;
                });
    }

    public Optional<InventarioResponse> buscarPorId(Integer jugadorId, Integer recursoId) {
        return inventarioRepository.findById(new InventarioId(recursoId, jugadorId))
                .map(inventarioMapper::toResponse);
    }
}
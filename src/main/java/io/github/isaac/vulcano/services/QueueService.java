package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.queue.QueueResponse;
import io.github.isaac.vulcano.entities.*;
import io.github.isaac.vulcano.exceptions.BadRequestException;
import io.github.isaac.vulcano.mappers.QueueMapper;
import io.github.isaac.vulcano.repositories.InventarioRepository;
import io.github.isaac.vulcano.repositories.JugadoreRepository;
import io.github.isaac.vulcano.repositories.PlanoRepository;
import io.github.isaac.vulcano.repositories.QueueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final JugadoreRepository jugadoreRepository;
    private final PlanoRepository planoRepository;
    private final InventarioRepository inventarioRepository;
    private final QueueMapper queueMapper;
    private final QueueRepository queueRepository;

    /**
     * Verifica si el jugador ya tiene este plano específico en fabricación.
     */
    private void validarConstruccionEnProgreso(Jugador jugador, Plano plano) {
        boolean yaConstruyendo = queueRepository.existsByJugadorAndPlanoAndEstado(jugador, plano, "EN_CONSTRUCCION");
        if (yaConstruyendo) {
            throw new BadRequestException("Ya tienes un " + plano.getNombre() + " en la fundición.");
        }
    }

    /**
     * Valida y resta los componentes necesarios del inventario del jugador.
     */
    private void descontarRecursosDelInventario(Jugador jugador, Plano plano) {
        for (Componente requisito : plano.getComponentes()) {
            Inventario itemInventario = inventarioRepository
                    .findByJugador_CorreoAndRecurso_Id(jugador.getCorreo(), requisito.getRecurso().getId())
                    .orElseThrow(() -> new BadRequestException("No posees el recurso: " + requisito.getRecurso().getNombre()));

            if (itemInventario.getCantidad() < requisito.getCantidad()) {
                throw new BadRequestException("Recursos insuficientes: " + requisito.getRecurso().getNombre() +
                        " (Requerido: " + requisito.getCantidad() + ")");
            }

            itemInventario.setCantidad(itemInventario.getCantidad() - requisito.getCantidad());
            // No es necesario llamar a save() aquí si estamos en una @Transactional (Dirty Checking)
        }
    }

    /**
     * Instancia el objeto Queue calculando los tiempos de forma atómica.
     */
    private Queue crearNuevaEntradaQueue(Jugador jugador, Plano plano) {
        Instant ahora = Instant.now();

        Queue queue = new Queue();
        queue.setJugador(jugador);
        queue.setPlano(plano);
        queue.setEstado("EN_CONSTRUCCION");
        queue.setInicioTime(ahora);

        // Aseguramos que el tiempo de construcción se trate como milisegundos
        queue.setFinalTime(ahora.plusMillis(plano.getTiempoConstrucion()));

        return queue;
    }


    @Transactional
    public QueueResponse iniciarConstruccion(Jwt userJwt, Integer planoId) {
        // 1. Recuperación de entidades principales
        Jugador jugador = jugadoreRepository.findByCorreo(userJwt.getSubject())
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new EntityNotFoundException("Plano con ID " + planoId + " no encontrado"));

        // 2. Validaciones de negocio
        validarConstruccionEnProgreso(jugador, plano);

        // 3. Consumo de recursos (Lógica extraída para mayor claridad)
        descontarRecursosDelInventario(jugador, plano);

        // 4. Creación de la entrada en la cola
        Queue nuevaCola = crearNuevaEntradaQueue(jugador, plano);

        log.info("Construcción iniciada: {} para el jugador {}", plano.getNombre(), jugador.getCorreo());

        return queueMapper.toResponse(queueRepository.save(nuevaCola));
    }

    private void entregarRecurso(Jugador jugador, Recurso recurso) {
        InventarioId id = new InventarioId(recurso.getId(), jugador.getId());

        Inventario inventario = inventarioRepository.findById(id)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setId(id);
                    nuevo.setJugador(jugador);
                    nuevo.setRecurso(recurso);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        inventario.setCantidad(inventario.getCantidad() + 1);
        inventarioRepository.save(inventario);

        log.info("Recurso {} entregado al jugador {}", recurso.getNombre(), jugador.getNombre());
    }

    @Transactional
    public void finalizarTareasCompletadas() {
        // 1. Buscar lo que ya terminó
        List<Queue> terminados = queueRepository.findByEstadoAndFinalTimeBefore("EN_CONSTRUCCION", Instant.now());

        for (Queue cola : terminados) {
            // 2. Cambiar estado
            cola.setEstado("FINALIZADO");

            // 3. Entregar el producto al inventario
            entregarRecurso(cola.getJugador(), cola.getPlano().getRecursoFabricado());
        }
    }

    public List<QueueResponse> obtenerTodasConstruciones(String estado) {
        return queueRepository.findAll()
                .stream()
                .filter(q -> q.getEstado().equalsIgnoreCase(estado))
                .map(queueMapper::toResponse)
                .toList();
    }

    public List<QueueResponse> obtenerTodasConstrucionesJugador(Integer jugadorId) {
        return queueRepository.findByJugador_Id(jugadorId)
                .stream()
                .map(queueMapper::toResponse)
                .toList();
    }
}

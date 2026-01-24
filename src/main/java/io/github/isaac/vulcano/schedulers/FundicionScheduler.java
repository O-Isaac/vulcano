package io.github.isaac.vulcano.schedulers;

import io.github.isaac.vulcano.entities.*;
import io.github.isaac.vulcano.repositories.InventarioRepository;
import io.github.isaac.vulcano.repositories.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundicionScheduler {

    private final QueueRepository queueRepository;
    private final InventarioRepository inventarioRepository;

    // Se ejecuta cada 60 segundos
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void verificarConstruccionesFinalizadas() {
        Instant ahora = Instant.now();

        // 1. Buscamos colas en estado 'EN_CONSTRUCCION' cuyo final_time ya pasó
        List<Queue> completadas = queueRepository.findByEstadoAndFinalTimeBefore("EN_CONSTRUCCION", ahora);

        if (completadas.isEmpty()) return;

        log.info("Finalizando {} construcciones en la fundición...", completadas.size());

        for (Queue cola : completadas) {
            entregarRecurso(cola);
        }
    }

    private void entregarRecurso(Queue cola) {
        Jugador jugador = cola.getJugador();
        Recurso producto = cola.getPlano().getRecursoFabricado();

        // 2. Localizar o crear el hueco en el inventario
        InventarioId id = new InventarioId();

        id.setJugadorId(jugador.getId());
        id.setRecursoId(producto.getId());

        Inventario inventario = inventarioRepository.findById(id)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();

                    nuevo.setId(id);
                    nuevo.setJugador(jugador);
                    nuevo.setRecurso(producto);
                    nuevo.setCantidad(0);

                    return nuevo;
                });

        // 3. Incrementar cantidad y marcar como finalizado
        inventario.setCantidad(inventario.getCantidad() + 1);
        cola.setEstado("FINALIZADO");

        // Al estar en @Transactional, los cambios se guardan solos al terminar
        log.info("¡{} ha terminado! Añadido al inventario de {}", producto.getNombre(), jugador.getNombre());
    }
}
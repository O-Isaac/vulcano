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

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final JugadoreRepository jugadoreRepository;
    private final PlanoRepository planoRepository;
    private final InventarioRepository inventarioRepository;
    private final QueueMapper queueMapper;
    private final QueueRepository queueRepository;

    // C
    @Transactional
    public QueueResponse iniciarConstruccion(Jwt userJwt, Integer planoId) {
        // 1. Obtenemos jugador y el plano a construir
        Jugador jugador = jugadoreRepository.findByCorreo(userJwt.getSubject())
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new EntityNotFoundException("Plano no encontrado"));


        // 1.1 Validamos que no tenga el mismo plano en construcion
        boolean yaConstruyendo = queueRepository.existsByJugadorAndPlanoAndEstado(jugador, plano, "EN_CONSTRUCCION");

        if (yaConstruyendo) {
            throw new BadRequestException("Ya tienes un " + plano.getNombre() + " en la fundición.");
        }

        // 2. Validamos y modificamos en memoria
        for (Componente requisito : plano.getComponentes()) {
            Inventario itemInventario = inventarioRepository
                    .findByJugador_CorreoAndRecurso_Id(jugador.getCorreo(), requisito.getRecurso().getId())
                    .orElseThrow(() -> new BadRequestException("No tienes el recurso: " + requisito.getRecurso().getNombre()));

            if (itemInventario.getCantidad() < requisito.getCantidad()) {
                throw new BadRequestException("Cantidad insuficiente de " + requisito.getRecurso().getNombre());
            }

            itemInventario.setCantidad(itemInventario.getCantidad() - requisito.getCantidad());
        }

        // 3. Creamos la cola
        Queue nuevaCola = new Queue();
        nuevaCola.setJugador(jugador);
        nuevaCola.setPlano(plano);
        nuevaCola.setEstado("EN_CONSTRUCCION");
        nuevaCola.setInicioTime(Instant.now());
        nuevaCola.setFinalTime(Instant.now().plusMillis(plano.getTiempoConstrucion()));

        // 4. Hacemos commit de todos los cambios en la transaccion
        return queueMapper.toResponse(queueRepository.save(nuevaCola));
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

        // Nota: No hace falta queueRepository.save(cola) por el @Transactional (Dirty Checking)
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

        log.info("Recurso {} entregado al jugador {}", jugador.getNombre(), recurso.getNombre());
    }
}

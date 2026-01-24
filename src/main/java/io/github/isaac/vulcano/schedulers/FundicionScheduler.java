package io.github.isaac.vulcano.schedulers;

import io.github.isaac.vulcano.entities.*;
import io.github.isaac.vulcano.services.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FundicionScheduler {

    private final QueueService queueService;

    @Scheduled(fixedRate = 30000) // Cada 30 segundos
    public void ejecutarRevision() {
        log.info("Scheduler: Revisando fundición para completar construcciones...");
        queueService.finalizarTareasCompletadas();
    }
}
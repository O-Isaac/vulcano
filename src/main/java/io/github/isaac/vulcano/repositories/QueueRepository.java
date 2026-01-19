package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Integer> {
}
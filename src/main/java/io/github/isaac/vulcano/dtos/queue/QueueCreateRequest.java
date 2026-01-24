package io.github.isaac.vulcano.dtos.queue;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Queue}
 */
public record QueueCreateRequest(Integer planoId) implements Serializable {
}
package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.queue.QueueCreateRequest;
import io.github.isaac.vulcano.dtos.queue.QueueResponse;
import io.github.isaac.vulcano.entities.Queue;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QueueMapper {
    // Create
    @Mapping(source = "planoId", target = "plano.id")
    Queue toEntity(QueueCreateRequest queueCreateRequest);

    // Response
    QueueResponse toResponse(Queue queue);
}
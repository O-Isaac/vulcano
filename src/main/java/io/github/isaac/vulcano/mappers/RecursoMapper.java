package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.recurso.RecursoCreateRequest;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoUpdateRequest;
import io.github.isaac.vulcano.entities.Recurso;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecursoMapper {
    // Create request
    Recurso toEntity(RecursoCreateRequest recursoCreateRequest);

    // Response
    RecursoResponse toResponse(Recurso recurso);

    // Update request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(RecursoUpdateRequest recursoUpdateRequest, @MappingTarget Recurso recurso);
}
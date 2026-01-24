package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.entities.Componente;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PlanoMapper.class, RecursoMapper.class}
)
public interface ComponenteMapper {
    // Respuesta
    ComponenteResponse toResponse(Componente componente);

    // Crear
    Componente toEntity(ComponenteCreateRequest request);

    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(ComponenteCreateRequest request, @MappingTarget Componente componente);
}
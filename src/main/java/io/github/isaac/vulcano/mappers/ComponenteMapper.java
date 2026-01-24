package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.entities.Componente;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ComponenteMapper {

    ComponenteResponse toResponse(Componente componente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurso", ignore = true) // No mapear el objeto Recurso automáticamente
    @Mapping(target = "plano", ignore = true)   // No mapear el objeto Plano automáticamente
    Componente toEntity(ComponenteCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "plano", ignore = true)
    void partialUpdate(ComponenteCreateRequest request, @MappingTarget Componente componente);
}
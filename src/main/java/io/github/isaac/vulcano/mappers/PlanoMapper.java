package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.plano.PlanoCreateRequest;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.entities.Plano;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RecursoMapper.class})
public interface PlanoMapper {
    // Response
    PlanoResponse toResponse(Plano plano);

    // Create
    @Mapping(target = "recursoFabricado", ignore = true)
    Plano toEntity(PlanoCreateRequest planoCreateRequest);

    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "recursoFabricado", ignore = true)
    void partialUpdate(PlanoCreateRequest request, @MappingTarget Plano plano);
}
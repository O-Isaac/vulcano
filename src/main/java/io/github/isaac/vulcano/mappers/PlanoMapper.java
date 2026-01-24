package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.plano.PlanoCreateRequest;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.entities.Plano;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ObjetoMapper.class})
public interface PlanoMapper {
    // Response
    PlanoResponse toResponse(Plano plano);

    // Create
    Plano toEntity(PlanoCreateRequest planoCreateRequest);

    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(PlanoCreateRequest request, @MappingTarget Plano plano);
}
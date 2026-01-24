package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.inventario.InventarioCreateRequest;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.entities.Inventario;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RecursoMapper.class})
public interface InventarioMapper {
    // Response
    InventarioResponse toResponse(Inventario inventario);

    @Mapping(source = "jugadorId", target = "id.jugadorId")
    @Mapping(source = "recursoId", target = "id.recursoId")
    Inventario toEntity(InventarioCreateRequest inventarioCreateRequest);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(InventarioCreateRequest inventarioCreateRequest, @MappingTarget Inventario inventario);
}
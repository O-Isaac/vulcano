package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponsePrivado;
import io.github.isaac.vulcano.entities.Inventario;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RecursoMapper.class})
public interface InventarioMapper {
    // Response
    InventarioResponse toResponse(Inventario inventario);
    InventarioResponsePrivado toResponsePublic(Inventario inventario);

}
package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.objeto.CreateRequest;
import io.github.isaac.vulcano.dtos.objeto.ResponseObjeto;
import io.github.isaac.vulcano.dtos.objeto.UpdateRequest;
import io.github.isaac.vulcano.entities.Objeto;
import io.github.isaac.vulcano.entities.response.ResponseListEntity;
import org.mapstruct.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjetoMapper {
    // Para convertir en entidades
    Objeto toEntity(CreateRequest createRequest);
    Objeto toEntity(ResponseObjeto responseObjeto);

    // Para convertir en dtos
    ResponseObjeto toDto(Objeto objeto);

    // Para convertir en respuestas automaticamente
    default ResponseEntity<ResponseObjeto> toResponse (Objeto objeto) {
        return ResponseEntity.ok(toDto(objeto));
    }

    default ResponseListEntity<ResponseObjeto> toResponseList(List<Objeto> objetos){
        return ResponseListEntity.ok(objetos.stream().map(this::toDto).toList());
    }

    // Para actualizar parcialmente un objeto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Objeto partialUpdate(UpdateRequest request, @MappingTarget Objeto objeto);


}
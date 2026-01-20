package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.objeto.CreateRequest;
import io.github.isaac.vulcano.dtos.objeto.UpdateRequest;
import io.github.isaac.vulcano.entities.Objeto;
import io.github.isaac.vulcano.exceptions.BadRequestException;
import io.github.isaac.vulcano.mappers.ObjetoMapper;
import io.github.isaac.vulcano.repositories.ObjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObjetoService {

    private final ObjetoMapper objetoMapper;
    private final ObjetoRepository objetoRepository;

    // C

    public Objeto crearObjeto(CreateRequest request) {
        Objeto objeto = objetoMapper.toEntity(request);
        return objetoRepository.save(objeto);
    }

    // R

    public List<Objeto> getAll() {
        return objetoRepository.findAll();
    }

    public Optional<Objeto> getById(Integer id) {
        return objetoRepository.findById(id);
    }

    // U

    public Objeto updateObjeto(Integer id, UpdateRequest request) {
        if (id == null) {
            throw new BadRequestException("Es obligatorio una id");
        }

        Objeto objeto = objetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Objeto no encontrado"));

        objetoMapper.partialUpdate(
                request,
                objeto
        );

        return objetoRepository.save(objeto);
    }

    // D

    public void deleteObjeto(Integer id) {
        if (id == null) {
            throw new BadRequestException("Es obligatorio una id");
        }

        objetoRepository.deleteById(id);
    }

}

package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.auth.LoginRequest;
import io.github.isaac.vulcano.dtos.objeto.CreateRequest;
import io.github.isaac.vulcano.dtos.objeto.ResponseObjeto;
import io.github.isaac.vulcano.dtos.objeto.UpdateRequest;
import io.github.isaac.vulcano.entities.Objeto;
import io.github.isaac.vulcano.entities.response.ResponseListEntity;
import io.github.isaac.vulcano.mappers.ObjetoMapper;
import io.github.isaac.vulcano.services.ObjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/objetos")
@PreAuthorize("hasRole('ADMIN')")
public class ObjetoController {

    private final ObjetoService objetoService;
    private final ObjetoMapper objetoMapper;

    @GetMapping
    public ResponseListEntity<ResponseObjeto> listar() {
        return objetoMapper.toResponseList(objetoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObjeto> obtenerPorId(@PathVariable Integer id) {
        return objetoService.getById(id)
                .map(objetoMapper::toResponse)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResponseObjeto> crear(@Valid @RequestBody CreateRequest request) {
        return objetoMapper.toResponse(objetoService.crearObjeto(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObjeto> actualizar(@PathVariable Integer id, @Valid @RequestBody UpdateRequest request) {
        return objetoMapper.toResponse(objetoService.updateObjeto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        objetoService.deleteObjeto(id);
        return ResponseEntity.ok("Eliminar objeto con id: " + id);
    }

}


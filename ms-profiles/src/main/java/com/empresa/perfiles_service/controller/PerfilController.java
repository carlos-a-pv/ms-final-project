package com.empresa.perfiles_service.controller;

import com.empresa.perfiles_service.dto.ApiResponse;
import com.empresa.perfiles_service.dto.PerfilResponseDTO;
import com.empresa.perfiles_service.dto.PerfilUpdateDTO;
import com.empresa.perfiles_service.mapper.PerfilMapper;
import com.empresa.perfiles_service.model.Perfil;
import com.empresa.perfiles_service.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    private final PerfilService service;
    private final PerfilMapper mapper;

    public PerfilController(PerfilService service, PerfilMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // 🔹 Obtener todos los perfiles
    @GetMapping
    public ResponseEntity<ApiResponse<List<PerfilResponseDTO>>> listarPerfiles() {

        List<Perfil> perfiles = service.listarPerfiles();
        List<PerfilResponseDTO> perfilesDto = mapper.toResponseList(perfiles);

        ApiResponse<List<PerfilResponseDTO>> response =
                new ApiResponse<>(true, "Perfiles encontrados", perfilesDto);

        return ResponseEntity.ok(response);
    }

    // 🔹 Obtener perfil por empleadoId
    @GetMapping("/{empleadoId}")
    public ResponseEntity<ApiResponse<PerfilResponseDTO>> obtenerPerfil(@PathVariable Long empleadoId) {

        Perfil perfil = service.obtenerPerfil(empleadoId);
        PerfilResponseDTO dto = mapper.toResponse(perfil);

        ApiResponse<PerfilResponseDTO> response =
                new ApiResponse<>(true, "Perfil encontrado", dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{empleadoId}")
    public ResponseEntity<ApiResponse<PerfilResponseDTO>> actualizarPerfil(
            @PathVariable Long empleadoId,
            @Valid @RequestBody PerfilUpdateDTO dto) {

        Perfil perfilActualizado = service.actualizarPerfil(empleadoId, dto);
        PerfilResponseDTO responseDto = mapper.toResponse(perfilActualizado);

        ApiResponse<PerfilResponseDTO> response =
                new ApiResponse<>(true, "Perfil actualizado", responseDto);

        return ResponseEntity.ok(response);
    }
}
package com.empresa.perfiles_service.dto;

import com.empresa.perfiles_service.model.State;
import lombok.Data;

@Data
public class PerfilResponseDTO {

    private String id;
    private String empleadoId;
    private String nombre;
    private String email;
    private String telefono;
    private String ciudad;
    private String biografia;
    private State state;
}
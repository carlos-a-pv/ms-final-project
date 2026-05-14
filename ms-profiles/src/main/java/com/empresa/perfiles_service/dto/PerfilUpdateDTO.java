package com.empresa.perfiles_service.dto;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PerfilUpdateDTO {

    @Size(max = 10, message = "El teléfono no puede tener más de 10 caracteres")
    private String telefono;

    @Size(max = 100)
    private String direccion;

    @Size(max = 50)
    private String ciudad;

    @Size(max = 255)
    private String biografia;

    // Getters y setters
}

package com.uq.msdepartments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Department", description = "Representación de un departamento.")
public class DepartmentDTO {

    @Schema(description = "Identificador único del departamento.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre del departamento.", example = "Ingeniería")
    private String nombre;

    @NotBlank
    @Schema(description = "Descripción del departamento.", example = "Departamento encargado de procesos de ingeniería.")
    private String descripcion;
}

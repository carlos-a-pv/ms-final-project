package com.uq.msdepartments.controller;

import com.uq.msdepartments.dto.DepartmentDTO;
import com.uq.msdepartments.error.APIError;
import com.uq.msdepartments.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments", description = "Endpoints para gestionar departamentos")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Crear un departamento",
            description = "Crea un nuevo departamento. El campo id se ignora si es enviado en el body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Departamento creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class),
                            examples = @ExampleObject(name = "created", value = "{\"id\": 1, \"nombre\": \"Ingeniería\", \"descripcion\": \"Departamento encargado de procesos de ingeniería.\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad Request. Body inválido o violación de validaciones (@NotBlank).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class),
                            examples = @ExampleObject(name = "validation", value = "{\"timestamp\":\"2026-03-25T02:15:23.123-05:00\",\"status\":400,\"error\":\"Bad Request\",\"message\":\"Validación fallida\",\"path\":\"/api/departments\",\"traceId\":\"b3a6f1c1-1a0a-4b3a-8bb8-4c7a7fbac4b9\",\"fieldErrors\":[{\"field\":\"nombre\",\"rejectedValue\":\"\",\"message\":\"must not be blank\"}]}"))),
            @ApiResponse(responseCode = "409", description = "Conflict. Conflicto al persistir el recurso (por ejemplo, violación de constraints).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class),
                            examples = @ExampleObject(name = "conflict", value = "{\"timestamp\":\"2026-03-25T02:15:23.123-05:00\",\"status\":409,\"error\":\"Conflict\",\"message\":\"Conflicto al persistir el recurso\",\"path\":\"/api/departments\",\"traceId\":\"b3a6f1c1-1a0a-4b3a-8bb8-4c7a7fbac4b9\",\"fieldErrors\":null}"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class)))
    })
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody DepartmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar un departamento por id",
            description = "Retorna el departamento asociado al id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class),
                            examples = @ExampleObject(name = "found", value = "{\"id\": 10, \"nombre\": \"Ingeniería\", \"descripcion\": \"Departamento encargado de procesos de ingeniería.\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad Request. El parámetro id no es válido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found. No existe departamento con ese id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class),
                            examples = @ExampleObject(name = "not_found", value = "{\"timestamp\":\"2026-03-25T02:15:23.123-05:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Department not found with id: 10\",\"path\":\"/api/departments/10\",\"traceId\":\"b3a6f1c1-1a0a-4b3a-8bb8-4c7a7fbac4b9\",\"fieldErrors\":null}"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class)))
    })
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los departamentos",
            description = "Retorna el listado completo de departamentos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de departamentos",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DepartmentDTO.class)),
                            examples = @ExampleObject(name = "list", value = "[{\"id\": 1, \"nombre\": \"Ingeniería\", \"descripcion\": \"...\"}]"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class)))
    })
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un departamento",
            description = "Actualiza nombre y descripción del departamento asociado al id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request. El parámetro id o el body no es válido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found. No existe departamento con ese id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "409", description = "Conflict. Conflicto al persistir el recurso (por ejemplo, violación de constraints).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class)))
    })
    public ResponseEntity<DepartmentDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un departamento",
            description = "Elimina el departamento asociado al id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Departamento eliminado"),
            @ApiResponse(responseCode = "400", description = "Bad Request. El parámetro id no es válido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found. No existe departamento con ese id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIError.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

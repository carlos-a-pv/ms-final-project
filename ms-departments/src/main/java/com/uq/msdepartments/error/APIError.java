package com.uq.msdepartments.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "APIError", description = "Modelo estándar de error para todas las respuestas no exitosas (4xx/5xx).")
public class APIError {

    @Schema(description = "Fecha/hora del error en formato ISO-8601 (con offset).", example = "2026-03-25T02:15:23.123-05:00")
    private OffsetDateTime timestamp;

    @Schema(description = "Código de estado HTTP.", example = "400")
    private int status;

    @Schema(description = "Texto corto del estado HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje legible del error.", example = "Validación fallida")
    private String message;

    @Schema(description = "Ruta solicitada.", example = "/api/departments/10")
    private String path;

    @Schema(description = "Identificador de correlación para rastreo. Se genera por respuesta si no hay uno disponible.", example = "b3a6f1c1-1a0a-4b3a-8bb8-4c7a7fbac4b9")
    private String traceId;

    @Schema(description = "Lista de errores de validación por campo (cuando aplica).")
    private List<FieldValidationError> fieldErrors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "FieldValidationError", description = "Detalle de error de validación asociado a un campo.")
    public static class FieldValidationError {

        @Schema(description = "Nombre del campo con error.", example = "nombre")
        private String field;

        @Schema(description = "Valor rechazado (si está disponible).", example = "")
        private Object rejectedValue;

        @Schema(description = "Mensaje de validación.", example = "must not be blank")
        private String message;
    }
}

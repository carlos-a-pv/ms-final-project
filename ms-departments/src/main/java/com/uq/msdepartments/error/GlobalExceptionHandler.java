package com.uq.msdepartments.error;

import com.uq.msdepartments.exception.DepartmentNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRACE_ID_ATTRIBUTE = "traceId";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<APIError.FieldValidationError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldValidationError)
                .toList();

        APIError body = baseError(HttpStatus.BAD_REQUEST, "Validación fallida", request)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<APIError> handleNotFound(DepartmentNotFoundException ex, HttpServletRequest request) {
        APIError body = baseError(HttpStatus.NOT_FOUND, ex.getMessage(), request).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        APIError body = baseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Parámetro inválido: " + ex.getName();
        APIError body = baseError(HttpStatus.BAD_REQUEST, message, request).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIError> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        APIError body = baseError(HttpStatus.BAD_REQUEST, "JSON inválido o malformado", request).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIError> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        APIError body = baseError(HttpStatus.CONFLICT, "Conflicto al persistir el recurso", request).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleGeneric(Exception ex, HttpServletRequest request) {
        APIError body = baseError(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", request).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private APIError.APIErrorBuilder baseError(HttpStatus status, String message, HttpServletRequest request) {
        return APIError.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .traceId(resolveOrCreateTraceId(request));
    }

    private String resolveOrCreateTraceId(HttpServletRequest request) {
        Object existing = request.getAttribute(TRACE_ID_ATTRIBUTE);
        if (existing != null) {
            return String.valueOf(existing);
        }
        String generated = UUID.randomUUID().toString();
        request.setAttribute(TRACE_ID_ATTRIBUTE, generated);
        return generated;
    }

    private APIError.FieldValidationError toFieldValidationError(FieldError fe) {
        String message = fe.getDefaultMessage();
        if (message == null) {
            message = "Validation error";
        }
        return APIError.FieldValidationError.builder()
                .field(fe.getField())
                .rejectedValue(fe.getRejectedValue())
                .message(message)
                .build();
    }
}

package com.example.jwt_security.exception;

import com.example.jwt_security.dto.response.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("An unexpected error occurred", 500);
        return ResponseEntity.status(500).body(errorResponse);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistException(AlreadyExistException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }
}

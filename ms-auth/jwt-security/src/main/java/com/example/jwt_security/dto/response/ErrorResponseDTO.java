package com.example.jwt_security.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {

    private String message;
    private int statusCode;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}

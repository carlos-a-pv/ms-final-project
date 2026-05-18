package com.example.jwt_security.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
        private int status;
        private String message;
        private String path;
        private LocalDateTime timestamp;

}

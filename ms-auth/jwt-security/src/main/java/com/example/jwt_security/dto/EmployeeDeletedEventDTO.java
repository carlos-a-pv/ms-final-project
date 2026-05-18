package com.example.jwt_security.dto;

import lombok.Builder;

@Builder
public record EmployeeDeletedEventDTO(
        Long id,
        String name,
        String email
) {
}

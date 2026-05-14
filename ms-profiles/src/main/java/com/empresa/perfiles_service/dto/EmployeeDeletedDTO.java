package com.empresa.perfiles_service.dto;

import jakarta.validation.constraints.Email;

public record EmployeeDeletedDTO(
        Long id,
        String name,
        @Email
        String email
) {
}

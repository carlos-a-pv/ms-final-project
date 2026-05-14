package co.edu.uniquindio.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record EmployeeDeletedEventDTO(
        Long id,
        String name,
        @Email
        String email
) {
}

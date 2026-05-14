package co.edu.uniquindio.dto;

import lombok.Builder;

@Builder
public record MessageProfileDTO(
        Long id,
        String name,
        String email
) {
}

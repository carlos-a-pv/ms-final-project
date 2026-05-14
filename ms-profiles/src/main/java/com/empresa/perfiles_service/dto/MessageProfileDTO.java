package com.empresa.perfiles_service.dto;

import lombok.Builder;

@Builder
public record MessageProfileDTO(
        Long id,
        String name,
        String email
) {
}

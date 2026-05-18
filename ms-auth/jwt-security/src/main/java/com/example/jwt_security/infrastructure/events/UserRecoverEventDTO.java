package com.example.jwt_security.infrastructure.events;

import lombok.Builder;
import lombok.Data;

@Builder
public record UserRecoverEventDTO(
        Long id,
        String recoverToken,
        String email
) {
}

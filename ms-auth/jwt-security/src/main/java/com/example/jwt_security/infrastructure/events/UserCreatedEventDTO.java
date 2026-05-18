package com.example.jwt_security.infrastructure.events;

import com.example.jwt_security.entity.User;
import lombok.Builder;

@Builder
public record UserCreatedEventDTO(
        Long id,
        String email,
        String token
) {
}

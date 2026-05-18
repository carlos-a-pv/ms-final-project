package com.example.jwt_security.infrastructure.events;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ProcessedEventDTO(
        Long id,
        String eventId,
        Instant timestamp,
        String consumer
) {
}

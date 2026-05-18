package com.example.jwt_security.dto;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        String eventId,
        String eventType,
        Instant timestamp,
        String source,
        T data
) {
    public static <T> EventEnvelope<T> of(String eventType, String source, T data) {
        return new EventEnvelope<>(
                UUID.randomUUID().toString(),
                eventType,
                Instant.now(),
                source,
                data
        );
    }
}

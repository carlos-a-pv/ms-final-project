package com.example.jwt_security.infrastructure.events;

import com.example.jwt_security.entity.ProcessedEvent;

public record ProcessedEventCreated(
        ProcessedEvent processedEvent
) {
}

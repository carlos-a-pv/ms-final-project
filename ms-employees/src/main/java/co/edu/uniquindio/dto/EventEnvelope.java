package co.edu.uniquindio.dto;

import com.rabbitmq.client.Envelope;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        String eventId,
        String eventType,
        Instant timeStamp,
        String source,
        T data
) {
    public static <T> EventEnvelope<T> of(String eventType, String source, T data){
        return new EventEnvelope<>(
                UUID.randomUUID().toString(),
                eventType,
                Instant.now(),
                source,
                data
        );
    }
}

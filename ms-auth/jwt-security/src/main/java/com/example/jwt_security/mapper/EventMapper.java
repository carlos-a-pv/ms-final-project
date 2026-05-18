package com.example.jwt_security.mapper;

import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.entity.ProcessedEvent;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.infrastructure.events.ProcessedEventDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public ProcessedEvent toEntity(ProcessedEventDTO processedEventDTO) {
        if(processedEventDTO == null) {
            return null;
        }
        return ProcessedEvent.builder()
                .eventId(processedEventDTO.eventId())
                .timestamp(processedEventDTO.timestamp())
                .consumer(processedEventDTO.consumer())
                .build();
    }

    public ProcessedEventDTO toDTO(ProcessedEvent processedEvent) {
        if(processedEvent == null) {
            return null;
        }
        return ProcessedEventDTO.builder()
                .id(processedEvent.getId())
                .eventId(processedEvent.getEventId())
                .timestamp(processedEvent.getTimestamp())
                .consumer(processedEvent.getConsumer())
                .build();
    }
}

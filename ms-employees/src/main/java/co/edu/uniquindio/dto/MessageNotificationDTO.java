package co.edu.uniquindio.dto;

import lombok.Builder;

@Builder
public record MessageNotificationDTO(
        String type,
        String receiver,
        String message
) {
}

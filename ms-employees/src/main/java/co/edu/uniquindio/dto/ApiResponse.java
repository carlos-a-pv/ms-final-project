package co.edu.uniquindio.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ApiResponse(
         int status,
         String path,
         LocalDateTime timestamp,
         String data
) {
}

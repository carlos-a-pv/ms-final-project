package co.edu.uniquindio.error;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ApiError {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}

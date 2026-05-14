package co.edu.uniquindio.dto;

import lombok.*;

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DepartmentDTO {
    private Long id;
    private String name;
    private String description;
}

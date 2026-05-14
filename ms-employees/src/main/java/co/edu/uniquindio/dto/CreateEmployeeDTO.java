package co.edu.uniquindio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateEmployeeDTO {
    @NotBlank(message = "The name is required")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    private String name;
    @NotBlank(message = "The position is required")
    @Size(min = 2, max = 30, message = "Position must be between 2 and 30 characters")
    private String position;
    @Email
    private String email;

    private Long departmentId;

    private Date hiringDate;
}

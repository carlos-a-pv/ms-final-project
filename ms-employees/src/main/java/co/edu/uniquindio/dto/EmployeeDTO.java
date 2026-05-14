package co.edu.uniquindio.dto;

import co.edu.uniquindio.model.State;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;


@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(name = "Employee", description = "Representation of a employee")
public class EmployeeDTO {
    @Schema(description = "unique identification of employee", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of employee", example = "Carlos")
    private String name;

    @Schema(description = "Role of employee", example = "Developer")
    private String position;

    @Schema(description = "email of employee", example = "email@example.com")
    private String email;

    @Schema(description = "Department id of the department to which the user belongs", example = "01")
    private Long departmentId;

    @Schema(description = "Hiring date of the employee was contracted", example = "02-02-2026")
    private Date hiringDate;

    @Schema(description = "State of employee actually", example = "HIRED")
    private State state;
}

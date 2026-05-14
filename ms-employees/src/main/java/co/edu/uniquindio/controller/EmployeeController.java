package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.CreateEmployeeDTO;
import co.edu.uniquindio.dto.DepartmentDTO;
import co.edu.uniquindio.dto.EmployeeDTO;
import co.edu.uniquindio.error.ApiError;
import co.edu.uniquindio.service.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name="Employees", description = "Endpoint for management employees")
public class EmployeeController {


    private final IEmployeeService employeeService;

    public  EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Operation(
            summary = "Creating a new employee",
            description = "Creating a new employee using the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class),
                            examples = @ExampleObject(name = "Created", value = "{\"id\": 1, \"name\": \"Carlos\", \"position\": \"Developer.\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "Bad request", value = ""))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Parameter(
                    description = "Employee data to be created",
                    required = true
            )
            @Valid
            @RequestBody CreateEmployeeDTO createEmployeeDTO){
        EmployeeDTO employeeCreated = employeeService.createEmployee(createEmployeeDTO);
        return ResponseEntity.created(URI.create("/api/employees/" + employeeCreated.getId())).body(employeeCreated);
    }
    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves a specific employee using their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "Employee not found", value = " {\"status\":404, \"message\": \"User with id specified not found\", \"path\": \"/api/employees/{id}\" }")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> queryEmployeeById(
            @Parameter(
                    description = "Unique identifier of the employee",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id){
        EmployeeDTO employeeFound = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeFound);
    }
    @Operation(
            summary = "Delete employee by ID",
            description = "Deletes a specific employee using their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee successfully deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = co.edu.uniquindio.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Employee successfully deleted",
                                    value = "{\"status\":200, \"path\": \"/api/employees/{id}\", \"data\": \"Employee successfully deleted\" }"
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "Employee not found",
                                    value = "{\"status\":404, \"message\": \"User with id specified not found\", \"path\": \"/api/employees/{id}\" }"
                            )
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<co.edu.uniquindio.dto.ApiResponse> deleteEmployee(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getEmployees());
    }
}

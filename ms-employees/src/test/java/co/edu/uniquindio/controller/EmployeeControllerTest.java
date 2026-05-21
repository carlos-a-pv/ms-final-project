package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.ApiResponse;
import co.edu.uniquindio.dto.CreateEmployeeDTO;
import co.edu.uniquindio.dto.EmployeeDTO;
import co.edu.uniquindio.model.State;
import co.edu.uniquindio.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO employeeDTO;
    private CreateEmployeeDTO createEmployeeDTO;
    private Date hiringDate;

    @BeforeEach
    void setUp() {
        hiringDate = new Date();
        employeeDTO = EmployeeDTO.builder()
                .id(10L)
                .name("Carlos")
                .position("Developer")
                .email("carlos@example.com")
                .departmentId(1L)
                .hiringDate(hiringDate)
                .state(State.HIRED)
                .build();

        createEmployeeDTO = CreateEmployeeDTO.builder()
                .name("Carlos")
                .position("Developer")
                .email("carlos@example.com")
                .departmentId(1L)
                .hiringDate(hiringDate)
                .build();
    }

    @Nested
    @DisplayName("createEmployee")
    class CreateEmployee {

        @Test
        @DisplayName("devuelve 201 Created con URI de ubicación y cuerpo del empleado")
        void shouldReturnCreatedWithLocationAndBody() {
            when(employeeService.createEmployee(createEmployeeDTO)).thenReturn(employeeDTO);

            ResponseEntity<EmployeeDTO> response = employeeController.createEmployee(createEmployeeDTO);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).hasToString("/api/employees/10");
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(10L);
            assertThat(response.getBody().getName()).isEqualTo("Carlos");
            assertThat(response.getBody().getEmail()).isEqualTo("carlos@example.com");

            verify(employeeService).createEmployee(createEmployeeDTO);
        }
    }

    @Nested
    @DisplayName("queryEmployeeById")
    class QueryEmployeeById {

        @Test
        @DisplayName("devuelve 200 OK con el empleado encontrado")
        void shouldReturnOkWithEmployee() {
            when(employeeService.getEmployeeById(10L)).thenReturn(employeeDTO);

            ResponseEntity<EmployeeDTO> response = employeeController.queryEmployeeById(10L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(10L);
            assertThat(response.getBody().getState()).isEqualTo(State.HIRED);

            verify(employeeService).getEmployeeById(10L);
        }
    }

    @Nested
    @DisplayName("deleteEmployee")
    class DeleteEmployee {

        @Test
        @DisplayName("devuelve 200 OK con la respuesta del servicio")
        void shouldReturnOkWithApiResponse() {
            ApiResponse apiResponse = new ApiResponse(
                    200,
                    "api/employees",
                    LocalDateTime.now(),
                    "Employee deleted successfully"
            );
            when(employeeService.deleteEmployee(10L)).thenReturn(apiResponse);

            ResponseEntity<ApiResponse> response = employeeController.deleteEmployee(10L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().status()).isEqualTo(200);
            assertThat(response.getBody().path()).isEqualTo("api/employees");
            assertThat(response.getBody().data()).isEqualTo("Employee deleted successfully");

            verify(employeeService).deleteEmployee(10L);
        }
    }

    @Nested
    @DisplayName("getAllEmployees")
    class GetAllEmployees {

        @Test
        @DisplayName("devuelve 200 OK con la lista de empleados")
        void shouldReturnOkWithEmployeeList() {
            EmployeeDTO secondEmployee = EmployeeDTO.builder()
                    .id(11L)
                    .name("Ana")
                    .position("Analyst")
                    .email("ana@example.com")
                    .departmentId(1L)
                    .hiringDate(hiringDate)
                    .state(State.HIRED)
                    .build();
            when(employeeService.getEmployees()).thenReturn(List.of(employeeDTO, secondEmployee));

            ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getId()).isEqualTo(10L);
            assertThat(response.getBody().get(1).getId()).isEqualTo(11L);

            verify(employeeService).getEmployees();
        }

        @Test
        @DisplayName("devuelve 200 OK con lista vacía cuando no hay empleados")
        void shouldReturnOkWithEmptyList() {
            when(employeeService.getEmployees()).thenReturn(List.of());

            ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(employeeService).getEmployees();
        }
    }
}

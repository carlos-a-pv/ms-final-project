package co.edu.uniquindio.service;

import co.edu.uniquindio.client.DepartmentClient;
import co.edu.uniquindio.dto.ApiResponse;
import co.edu.uniquindio.dto.CreateEmployeeDTO;
import co.edu.uniquindio.dto.DepartmentDTO;
import co.edu.uniquindio.dto.EmployeeDTO;
import co.edu.uniquindio.exception.UserEmailAlreadyExists;
import co.edu.uniquindio.exception.UserHiredAlready;
import co.edu.uniquindio.exception.UserNotFoundException;
import co.edu.uniquindio.infrastructure.events.EmployeeCreatedEvent;
import co.edu.uniquindio.infrastructure.events.EmployeeDeletedEvent;
import co.edu.uniquindio.model.Employee;
import co.edu.uniquindio.model.State;
import co.edu.uniquindio.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentClient departmentClient;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee hiredEmployee;
    private Employee layoffEmployee;
    private DepartmentDTO departmentDTO;
    private Date hiringDate;

    @BeforeEach
    void setUp() {
        hiringDate = new Date();
        departmentDTO = DepartmentDTO.builder()
                .id(1L)
                .name("Engineering")
                .description("Software department")
                .build();

        hiredEmployee = Employee.builder()
                .id(10L)
                .name("Carlos")
                .position("Developer")
                .email("carlos@example.com")
                .departmentId(1L)
                .hiringDate(hiringDate)
                .state(State.HIRED)
                .build();

        layoffEmployee = Employee.builder()
                .id(11L)
                .name("Ana")
                .position("Analyst")
                .email("ana@example.com")
                .departmentId(1L)
                .hiringDate(hiringDate)
                .state(State.LAYOFF)
                .build();
    }

    @Nested
    @DisplayName("getEmployees")
    class GetEmployees {

        @Test
        @DisplayName("devuelve la lista de empleados mapeados a DTO")
        void shouldReturnAllEmployeesAsDto() {
            when(employeeRepository.findAll()).thenReturn(List.of(hiredEmployee, layoffEmployee));

            List<EmployeeDTO> result = employeeService.getEmployees();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(10L);
            assertThat(result.get(0).getName()).isEqualTo("Carlos");
            assertThat(result.get(0).getState()).isEqualTo(State.HIRED);
            assertThat(result.get(1).getId()).isEqualTo(11L);
            assertThat(result.get(1).getState()).isEqualTo(State.LAYOFF);
            verify(employeeRepository).findAll();
        }

        @Test
        @DisplayName("devuelve lista vacía cuando no hay empleados")
        void shouldReturnEmptyListWhenNoEmployees() {
            when(employeeRepository.findAll()).thenReturn(List.of());

            List<EmployeeDTO> result = employeeService.getEmployees();

            assertThat(result).isEmpty();
            verify(employeeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getEmployeeById")
    class GetEmployeeById {

        @Test
        @DisplayName("devuelve el empleado cuando existe")
        void shouldReturnEmployeeWhenFound() {
            when(employeeRepository.findById(10L)).thenReturn(Optional.of(hiredEmployee));

            EmployeeDTO result = employeeService.getEmployeeById(10L);

            assertThat(result.getId()).isEqualTo(10L);
            assertThat(result.getEmail()).isEqualTo("carlos@example.com");
            assertThat(result.getState()).isEqualTo(State.HIRED);
            verify(employeeRepository).findById(10L);
        }

        @Test
        @DisplayName("lanza UserNotFoundException cuando el empleado no existe")
        void shouldThrowWhenEmployeeNotFound() {
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                    .isInstanceOf(UserNotFoundException.class);
            verify(employeeRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("createEmployee")
    class CreateEmployee {

        private CreateEmployeeDTO createEmployeeDTO;

        @BeforeEach
        void setUpCreate() {
            createEmployeeDTO = CreateEmployeeDTO.builder()
                    .name("Carlos")
                    .position("Developer")
                    .email("new@example.com")
                    .departmentId(1L)
                    .hiringDate(hiringDate)
                    .build();
        }

        @Test
        @DisplayName("crea el empleado, publica evento y devuelve DTO")
        void shouldCreateEmployeeSuccessfully() {
            when(departmentClient.getDepartmentById(1L)).thenReturn(departmentDTO);
            when(employeeRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
                Employee emp = invocation.getArgument(0);
                emp.setId(20L);
                return emp;
            });

            EmployeeDTO result = employeeService.createEmployee(createEmployeeDTO);

            assertThat(result.getId()).isEqualTo(20L);
            assertThat(result.getName()).isEqualTo("Carlos");
            assertThat(result.getEmail()).isEqualTo("new@example.com");
            assertThat(result.getDepartmentId()).isEqualTo(1L);
            assertThat(result.getState()).isEqualTo(State.HIRED);

            ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(employeeCaptor.capture());
            Employee saved = employeeCaptor.getValue();
            assertThat(saved.getState()).isEqualTo(State.HIRED);
            assertThat(saved.getDepartmentId()).isEqualTo(1L);

            ArgumentCaptor<EmployeeCreatedEvent> eventCaptor = ArgumentCaptor.forClass(EmployeeCreatedEvent.class);
            verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
            assertThat(eventCaptor.getValue().employee().getId()).isEqualTo(20L);

            verify(departmentClient).getDepartmentById(1L);
            verify(employeeRepository).existsByEmail("new@example.com");
        }

        @Test
        @DisplayName("lanza UserEmailAlreadyExists cuando el email ya está registrado")
        void shouldThrowWhenEmailAlreadyExists() {
            when(departmentClient.getDepartmentById(1L)).thenReturn(departmentDTO);
            when(employeeRepository.existsByEmail("new@example.com")).thenReturn(true);

            assertThatThrownBy(() -> employeeService.createEmployee(createEmployeeDTO))
                    .isInstanceOf(UserEmailAlreadyExists.class);

            verify(employeeRepository, never()).save(any());
            verify(applicationEventPublisher, never()).publishEvent(any());
        }
    }

    @Nested
    @DisplayName("deleteEmployee")
    class DeleteEmployee {

        @Test
        @DisplayName("marca como LAYOFF, publica evento y devuelve respuesta exitosa")
        void shouldLayoffEmployeeSuccessfully() {
            when(employeeRepository.findById(10L)).thenReturn(Optional.of(hiredEmployee));
            when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ApiResponse response = employeeService.deleteEmployee(10L);

            assertThat(response.status()).isEqualTo(200);
            assertThat(response.path()).isEqualTo("api/employees");
            assertThat(response.data()).isEqualTo("Employee deleted successfully");

            ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(employeeCaptor.capture());
            assertThat(employeeCaptor.getValue().getState()).isEqualTo(State.LAYOFF);

            ArgumentCaptor<EmployeeDeletedEvent> eventCaptor = ArgumentCaptor.forClass(EmployeeDeletedEvent.class);
            verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
            assertThat(eventCaptor.getValue().employee().getId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("lanza UserNotFoundException cuando el empleado no existe")
        void shouldThrowWhenEmployeeNotFound() {
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                    .isInstanceOf(UserNotFoundException.class);

            verify(employeeRepository, never()).save(any());
            verify(applicationEventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("lanza UserHiredAlready cuando el empleado ya está en LAYOFF")
        void shouldThrowWhenEmployeeAlreadyLayoff() {
            when(employeeRepository.findById(11L)).thenReturn(Optional.of(layoffEmployee));

            assertThatThrownBy(() -> employeeService.deleteEmployee(11L))
                    .isInstanceOf(UserHiredAlready.class);

            verify(employeeRepository, never()).save(any());
            verify(applicationEventPublisher, never()).publishEvent(any());
        }
    }
}

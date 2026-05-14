package co.edu.uniquindio.service;

import co.edu.uniquindio.client.DepartmentClient;
import co.edu.uniquindio.dto.*;
import co.edu.uniquindio.exception.UserEmailAlreadyExists;
import co.edu.uniquindio.exception.UserHiredAlready;
import co.edu.uniquindio.exception.UserNotFoundException;
import co.edu.uniquindio.infrastructure.events.EmployeeCreatedEvent;
import co.edu.uniquindio.infrastructure.events.EmployeeDeletedEvent;
import co.edu.uniquindio.infrastructure.messaging.MessageProducerService;
import co.edu.uniquindio.mapper.Mapper;
import co.edu.uniquindio.model.Employee;
import co.edu.uniquindio.model.State;
import co.edu.uniquindio.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService{
    private final EmployeeRepository employeeRepository;
    private final DepartmentClient departmentClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public EmployeeService(EmployeeRepository employeeRepository,  DepartmentClient departmentClient,  MessageProducerService messageProducerService, ApplicationEventPublisher applicationEventPublisher) {
        this.employeeRepository = employeeRepository;
        this.departmentClient = departmentClient;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<EmployeeDTO> getEmployees() {
        return employeeRepository.findAll().stream().map(Mapper::toDTO).toList();
    }
    @Transactional
    @Override
    public EmployeeDTO createEmployee(CreateEmployeeDTO employeeDTO) {

        DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employeeDTO.getDepartmentId());

        if(employeeRepository.existsByEmail(employeeDTO.getEmail())){
            throw new UserEmailAlreadyExists(employeeDTO.getEmail(), "");
        }

        Employee newEmployee = Employee.builder()
                .name(employeeDTO.getName())
                .position(employeeDTO.getPosition())
                .email(employeeDTO.getEmail())
                .departmentId(departmentDTO.getId())
                .hiringDate(employeeDTO.getHiringDate())
                .state(State.HIRED)
                .build();

        Employee employeeSaved =  employeeRepository.save(newEmployee);
        applicationEventPublisher.publishEvent(new EmployeeCreatedEvent(employeeSaved));
        return Mapper.toDTO(employeeSaved);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        return null;
    }

    @Transactional
    @Override
    public ApiResponse deleteEmployee(Long id) {
        Optional<Employee> employee =Optional.of(employeeRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id, "/api/employees")));
        Employee employeeFound = employee.get();

        if(employeeFound.getState().equals(State.LAYOFF)){
            throw new UserHiredAlready(employeeFound.getId(), "/api/employees/");
        }
        employeeFound.setState(State.LAYOFF);
        employeeRepository.save(employeeFound);

        applicationEventPublisher.publishEvent(new EmployeeDeletedEvent(employeeFound));

        return ApiResponse.builder()
                .status(200)
                .path("api/employees")
                .timestamp(LocalDateTime.now())
                .data("Employee deleted successfully")
                .build();
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Optional<Employee> emp = Optional.of(employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id, "api/employees")));
        return Mapper.toDTO(emp.get());
    }
}

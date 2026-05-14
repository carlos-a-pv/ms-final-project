package co.edu.uniquindio.service;


import co.edu.uniquindio.dto.ApiResponse;
import co.edu.uniquindio.dto.CreateEmployeeDTO;
import co.edu.uniquindio.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    List<EmployeeDTO> getEmployees();

    EmployeeDTO createEmployee(CreateEmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    ApiResponse deleteEmployee(Long id);

    EmployeeDTO getEmployeeById(Long id);

}

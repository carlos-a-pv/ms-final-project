package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.EmployeeDTO;
import co.edu.uniquindio.model.Employee;

public class Mapper {

    public static EmployeeDTO toDTO(Employee e){
        if(e == null) return null;

        return EmployeeDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .position(e.getPosition())
                .email(e.getEmail())
                .departmentId(e.getDepartmentId())
                .hiringDate(e.getHiringDate())
                .state(e.getState())
                .build();
    }
}

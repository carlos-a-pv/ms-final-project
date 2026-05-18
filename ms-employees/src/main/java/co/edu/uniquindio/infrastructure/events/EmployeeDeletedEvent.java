package co.edu.uniquindio.infrastructure.events;

import co.edu.uniquindio.model.Employee;

public record EmployeeDeletedEvent(Employee employee) {

}

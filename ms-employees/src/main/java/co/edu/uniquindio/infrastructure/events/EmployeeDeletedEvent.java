package co.edu.uniquindio.infrastructure.events;

import co.edu.uniquindio.model.Employee;
import lombok.Getter;

@Getter
public class EmployeeDeletedEvent {

    private final Employee employee;

    public EmployeeDeletedEvent(Employee employee) {
        this.employee = employee;
    }
}

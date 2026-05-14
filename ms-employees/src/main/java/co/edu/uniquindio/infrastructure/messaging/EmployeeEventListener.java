package co.edu.uniquindio.infrastructure.messaging;

import co.edu.uniquindio.dto.EmployeeCreatedEventDTO;
import co.edu.uniquindio.dto.EmployeeDeletedEventDTO;
import co.edu.uniquindio.exception.EventPublisingException;
import co.edu.uniquindio.infrastructure.events.EmployeeCreatedEvent;
import co.edu.uniquindio.infrastructure.events.EmployeeDeletedEvent;
import co.edu.uniquindio.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeEventListener {

    private final MessageProducerService messageProducerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeEventListener.class);

    public EmployeeEventListener(MessageProducerService messageProducerService) {
        this.messageProducerService = messageProducerService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployeeCreated(EmployeeCreatedEvent event){
        try{
            Employee employee = event.getEmployee();

            messageProducerService.sendMessage(
                    EmployeeCreatedEventDTO.builder()
                            .id(employee.getId())
                            .name(employee.getName())
                            .email(employee.getEmail())
                            .departmentId(employee.getDepartmentId())
                            .hiringDate(employee.getHiringDate())
                            .build());
        } catch (EventPublisingException ex){
            LOGGER.error("Error enviando evento a RabbitMQ", ex);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployeeDeleted(EmployeeDeletedEvent event){
        try {
            Employee employee = event.getEmployee();

            messageProducerService.sendMessage(
                    EmployeeDeletedEventDTO.builder()
                            .id(employee.getId())
                            .name(employee.getName())
                            .email(employee.getEmail())
                            .build()
            );
        }catch (EventPublisingException ex){
            LOGGER.error("Error enviando evento a RabbitMQ", ex);
        }
    }
}

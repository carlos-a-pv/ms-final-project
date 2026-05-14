package co.edu.uniquindio.infrastructure.messaging;


import co.edu.uniquindio.config.RabbitMQConfig;
import co.edu.uniquindio.dto.*;
import co.edu.uniquindio.exception.EventPublisingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService implements IMessageProducerService {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(EmployeeCreatedEventDTO message) {
        try{
            EventEnvelope<EmployeeCreatedEventDTO> event = EventEnvelope.of("EMPLOYEE_CREATED", "employee-service", message);
            rabbitTemplate.convertAndSend(RabbitMQConfig.ONBOARDING_EXCHANGE, "", event);
        }catch(Exception e){
            throw new EventPublisingException("Error enviando evento EMPLOYEE_CREATED", e);
        }

    }

    @Override
    public void sendMessage(EmployeeDeletedEventDTO message) {
        EventEnvelope<EmployeeDeletedEventDTO> event = EventEnvelope.of("EMPLOYEE_DELETED", "employee-service", message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.OFFBOARDING_EXCHANGE, "", event);
    }
}

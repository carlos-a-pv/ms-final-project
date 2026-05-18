package com.example.jwt_security.infrastructure;

import com.example.jwt_security.config.RabbitConfig;
import com.example.jwt_security.dto.EmployeeCreatedEventDTO;
import com.example.jwt_security.dto.EmployeeDeletedEventDTO;
import com.example.jwt_security.dto.EventEnvelope;
import com.example.jwt_security.infrastructure.events.ProcessedEventDTO;
import com.example.jwt_security.service.IAuthService;
import com.example.jwt_security.service.impl.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class RabbitmqEventListener {
    private final IAuthService authService;
    private final ObjectMapper objectMapper;


    public  RabbitmqEventListener(IAuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    private <T> T convert(Object data, Class<T> clazz) {
        return objectMapper.convertValue(data, clazz);
    }

    @RabbitListener(queues = RabbitConfig.AUTH_ONBOARDING_QUEUE)
    public void employeeCreated(EventEnvelope<?> event){
        switch (event.eventType()){
            case "EMPLOYEE_CREATED" ->{
                EmployeeCreatedEventDTO data = convert(event.data(), EmployeeCreatedEventDTO.class);
                authService.createDefaultUser(data.email(), data.id());
            }

            case "EMPLOYEE_DELETED" -> {
                EmployeeDeletedEventDTO data = convert(event.data(), EmployeeDeletedEventDTO.class);
                authService.removeAccessCredientals(data.email());
            }
        }
    }
}

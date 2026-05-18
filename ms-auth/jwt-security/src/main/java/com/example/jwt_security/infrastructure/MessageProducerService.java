package com.example.jwt_security.infrastructure;

import com.example.jwt_security.config.RabbitConfig;
import com.example.jwt_security.dto.EventEnvelope;
import com.example.jwt_security.exception.EventPublishingException;
import com.example.jwt_security.infrastructure.events.UserCreatedEventDTO;
import com.example.jwt_security.infrastructure.events.UserRecoverEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(UserCreatedEventDTO message){
        try{
            EventEnvelope<UserCreatedEventDTO> event = EventEnvelope.of("USER_CREATED", "auth-service",  message);
            rabbitTemplate.convertAndSend(RabbitConfig.ONBOARDING_EXCHANGE, "",  event);
        }catch (Exception e){
            throw new EventPublishingException("Error enviando evento USER_CREATED", e);
        }
    }

    public void sendMessage(UserRecoverEventDTO message){
        try{
            EventEnvelope<UserRecoverEventDTO> event = EventEnvelope.of("USER_RECOVER", "auth-service",  message);
            rabbitTemplate.convertAndSend(RabbitConfig.ONBOARDING_EXCHANGE, "",  event);
        }catch (Exception e){
            throw new EventPublishingException("Error enviando evento USER_RECOVER", e);
        }
    }
}

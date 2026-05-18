package com.example.jwt_security.infrastructure;


import com.example.jwt_security.entity.User;
import com.example.jwt_security.exception.EventPublishingException;
import com.example.jwt_security.infrastructure.events.UserCreatedEvent;
import com.example.jwt_security.infrastructure.events.UserCreatedEventDTO;
import com.example.jwt_security.infrastructure.events.UserRecoverEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class EventProcessingService {
    private final MessageProducerService messageProducerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessingService.class);

    public EventProcessingService(
            MessageProducerService messageProducerService
    ) {
        this.messageProducerService = messageProducerService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processEmployeeCreated(UserCreatedEvent event) {
        try{
            User user = event.user();
            String token = event.token();

            messageProducerService.sendMessage(
                    UserCreatedEventDTO.builder()
                            .id(user.getId())
                            .email(user.getUsername())
                            .token(token)
                            .build()
            );
        }catch (EventPublishingException ex){
            LOGGER.error("Error enviando evento a RabbitMQ", ex);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void recoverPassword(UserRecoverEventDTO event) {
        try{
            messageProducerService.sendMessage(
                    UserRecoverEventDTO.builder()
                            .id(event.id())
                            .email(event.email())
                            .recoverToken(event.recoverToken())
                            .build()
            );
        }catch (EventPublishingException ex){
            LOGGER.error("Error enviando evento a RabbitMQ", ex);
        }
    }
}

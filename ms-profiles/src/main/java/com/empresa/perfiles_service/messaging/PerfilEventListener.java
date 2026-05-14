package com.empresa.perfiles_service.messaging;

import com.empresa.perfiles_service.config.RabbitConfig;
import com.empresa.perfiles_service.dto.EmployeeCreatedEventDTO;
import com.empresa.perfiles_service.dto.EmployeeDeletedDTO;
import com.empresa.perfiles_service.dto.EventEnvelope;
import com.empresa.perfiles_service.dto.MessageProfileDTO;
import com.empresa.perfiles_service.service.PerfilService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

/**
 * Este componente escucha los eventos relacionados con empleados que se crean
 * y automáticamente genera un perfil por defecto para cada nuevo empleado.
 */
@Component
public class PerfilEventListener {

    private final PerfilService perfilService;
    private final ObjectMapper objectMapper;

    public PerfilEventListener(PerfilService perfilService,  ObjectMapper objectMapper) {
        this.perfilService = perfilService;
        this.objectMapper = objectMapper;
    }


    private <T> T convert(Object data, Class<T> clazz) {
        return objectMapper.convertValue(data, clazz);
    }


    @RabbitListener(queues = RabbitConfig.PROFILE_ONBOARDING_QUEUE)
    public void recibirEmpleadoCreado(EventEnvelope<?> event) {

        switch (event.eventType()) {

            case "EMPLOYEE_CREATED" -> {
                EmployeeCreatedEventDTO data = convert(event.data(), EmployeeCreatedEventDTO.class);
                perfilService.crearPerfilPorDefecto(
                        data.id(),
                        data.name(),
                        data.email()
                );
            }

            case "EMPLOYEE_DELETED" -> {
                EmployeeDeletedDTO data = convert(event.data(), EmployeeDeletedDTO.class);
                perfilService.desactivarPerfil(data.id());
            }
        }
    }
}
package co.edu.uniquindio.infrastructure.messaging;

import co.edu.uniquindio.dto.EmployeeCreatedEventDTO;
import co.edu.uniquindio.dto.EmployeeDeletedEventDTO;

public interface IMessageProducerService {

    void sendMessage(EmployeeCreatedEventDTO message);

    void sendMessage(EmployeeDeletedEventDTO message);

}

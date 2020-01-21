package com.metalheart.integration.gateway;


import com.metalheart.model.RegistrationResponse;
import com.metalheart.model.User;
import java.util.UUID;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.metalheart.integration.RegistrationFlowIntegration.REGISTRATION_ERROR_CHANNEL;


@MessagingGateway(errorChannel = REGISTRATION_ERROR_CHANNEL)
public interface RegistrationGateway {

    @Gateway(requestChannel = "registrationFlow.input")
    RegistrationResponse startRegistration(User user);

    @Gateway(requestChannel = "confirmRegistrationFlow.input")
    RegistrationResponse confirmRegistration(UUID taskId);
}

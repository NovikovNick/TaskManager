package com.metalheart.integration;

import com.metalheart.config.AppProperties;
import com.metalheart.model.DelayedTask;
import com.metalheart.model.RegistrationResponse;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import com.metalheart.service.DelayedTaskService;
import com.metalheart.service.EmailService;
import com.metalheart.service.UserService;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.web.util.UriComponentsBuilder;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan("com.metalheart.integration")
@ComponentScan("com.metalheart.integration")
public class RegistrationFlowIntegration {

    public static final String REGISTRATION_ERROR_CHANNEL = "REGISTRATION_ERROR_CHANNEL";

    @Autowired
    private AppProperties appProperties;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private DelayedTaskService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Bean
    public IntegrationFlow registrationFlow() {

        return f -> f
            .transform(user -> conversionService.convert(user, UserRegistrationDelayedTask.class))
            .<UserRegistrationDelayedTask>handle((task, headers) -> {

                task.setTaskId(UUID.randomUUID());
                tokenService.add(task);

                String confirmationLink = UriComponentsBuilder
                    .fromHttpUrl(appProperties.getRest().getBackUrl())
                    .path(task.getTaskId().toString())
                    .toUriString();

                emailService.sendRegistration(task.getEmail(), confirmationLink);

                log.info("Registration request has been sent");
                return new RegistrationResponse(null);
            });
    }

    @Bean
    public IntegrationFlow confirmRegistrationFlow() {

        return f -> f
            .<UUID>handle((taskId, headers) -> {

                DelayedTask task = tokenService.get(taskId);
                if (Objects.isNull(task) || !(task instanceof UserRegistrationDelayedTask)) {
                    return null;
                }
                User user = conversionService.convert(task, User.class);
                user = userService.createUser(user);

                return new RegistrationResponse(user);
            });
    }

    @Bean
    public IntegrationFlow transferErrorFlow() {
        return IntegrationFlows
            .from(REGISTRATION_ERROR_CHANNEL)
            .handle((message, headers) -> {
                MessageHandlingException e = (MessageHandlingException) message;
                log.error(e.getMessage(), e);
                return new RegistrationResponse(null);
            })
            .get();
    }
}

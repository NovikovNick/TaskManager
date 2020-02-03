package com.metalheart.service.impl;

import com.metalheart.config.AppProperties;
import com.metalheart.exception.SMTPException;
import com.metalheart.log.LogOperationContext;
import com.metalheart.model.DelayedTask;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import com.metalheart.service.DelayedTaskService;
import com.metalheart.service.EmailService;
import com.metalheart.service.RegistrationService;
import com.metalheart.service.UserService;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Slf4j
@Component
public class RegistrationServiceImpl implements RegistrationService {

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

    @LogOperationContext
    @Override
    public boolean startRegistration(User user) {

        log.info("Start registration");

        var task = conversionService.convert(user, UserRegistrationDelayedTask.class);
        task.setTaskId(UUID.randomUUID());

        tokenService.add(task);

        String confirmationLink = UriComponentsBuilder
            .fromHttpUrl(appProperties.getRest().getBackUrl())
            .path(task.getTaskId().toString())
            .toUriString();

        try {

            emailService.sendRegistration(task.getEmail(), confirmationLink);
            log.info("Registration process started");
            return true;

        } catch (SMTPException e) {
            log.error(e.getMessage(), e);
            tokenService.remove(task.getTaskId());
            return false;
        }
    }

    @Override
    public Optional<User> confirmRegistration(UUID taskId) {

        DelayedTask task = tokenService.get(taskId);
        if (Objects.isNull(task) || !(task instanceof UserRegistrationDelayedTask)) {
            return Optional.empty();
        }
        User user = conversionService.convert(task, User.class);
        user = userService.createUser(user);

        return Optional.of(user);
    }
}

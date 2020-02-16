package com.metalheart.service.impl;

import com.metalheart.config.AppProperties;
import com.metalheart.exception.SMTPException;
import com.metalheart.model.request.ChangePasswordDelayedTask;
import com.metalheart.service.ChangePasswordService;
import com.metalheart.service.DelayedTaskService;
import com.metalheart.service.EmailService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class ChangePasswordImpl implements ChangePasswordService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private DelayedTaskService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean startChangePasswordProcessing(String email) {
        log.info("Start change password process");

        ChangePasswordDelayedTask task = new ChangePasswordDelayedTask(email);
        task.setTaskId(UUID.randomUUID());

        tokenService.add(task);

        String confirmationLink = UriComponentsBuilder
            .fromHttpUrl(appProperties.getRest().getBackUrl())
            .path(task.getTaskId().toString())
            .toUriString();

        try {

            emailService.sendResetPassword(email, confirmationLink);
            log.info("Change password  process started");
            return true;

        } catch (SMTPException e) {
            log.error(e.getMessage(), e);
            tokenService.remove(task.getTaskId());
            return false;
        }
    }
}

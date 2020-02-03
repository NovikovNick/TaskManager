package com.metalheart.service;

import com.metalheart.exception.SMTPException;

public interface EmailService {

    void sendResetPassword(String email, String link) throws SMTPException;

    void sendRegistration(String email, String link) throws SMTPException;
}

package com.metalheart.service;

public interface EmailService {

    void sendResetPassword(String email, String link);

    void sendRegistration(String email, String link);
}

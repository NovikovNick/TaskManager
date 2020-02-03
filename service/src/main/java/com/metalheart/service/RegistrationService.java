package com.metalheart.service;

import com.metalheart.model.User;
import java.util.Optional;
import java.util.UUID;

public interface RegistrationService {

    boolean startRegistration(User user);

    Optional<User> confirmRegistration(UUID taskId);
}

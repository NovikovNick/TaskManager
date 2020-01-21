package com.metalheart.model.request;

import com.metalheart.model.DelayedTask;
import lombok.Data;

@Data
public class UserRegistrationDelayedTask extends DelayedTask {

    private String username;

    private String email;

    private String password;
}

package com.metalheart.model.request;

import com.metalheart.model.DelayedTask;
import lombok.Data;

@Data
public class ChangePasswordDelayedTask extends DelayedTask {
    private String email;

    public ChangePasswordDelayedTask(String email) {
        this.email = email;
    }
}

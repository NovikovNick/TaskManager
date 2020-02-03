package com.metalheart.service;

import com.metalheart.model.DelayedTask;
import java.util.UUID;


public interface DelayedTaskService {

    DelayedTask add(DelayedTask task);

    DelayedTask get(UUID taskId);

    boolean remove(UUID taskId);
}

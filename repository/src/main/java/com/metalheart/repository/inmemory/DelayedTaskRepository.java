package com.metalheart.repository.inmemory;

import com.metalheart.model.DelayedTask;
import java.util.Collection;
import java.util.UUID;


public interface DelayedTaskRepository {

    <T extends DelayedTask> T add(T delayedTask);

    <T extends DelayedTask> T get(UUID taskId);

    Collection<DelayedTask> getAll();

    boolean remove(UUID taskId);
}

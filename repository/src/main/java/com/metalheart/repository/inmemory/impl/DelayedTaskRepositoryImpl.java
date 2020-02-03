package com.metalheart.repository.inmemory.impl;

import com.metalheart.model.DelayedTask;
import com.metalheart.repository.inmemory.DelayedTaskRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class DelayedTaskRepositoryImpl implements DelayedTaskRepository {

    Map<UUID, DelayedTask> tokens = Collections.synchronizedMap(new HashMap<>());

    @Override
    public <T extends DelayedTask> T add(T delayedTask) {
        tokens.put(delayedTask.getTaskId(), delayedTask);
        return delayedTask;
    }

    @Override
    public <T extends DelayedTask> T get(UUID taskId) {
        return (T) tokens.get(taskId);
    }

    @Override
    public boolean remove(UUID taskId) {
        return Objects.nonNull(tokens.remove(taskId));
    }
}

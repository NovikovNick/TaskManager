package com.metalheart.service.impl;

import com.metalheart.model.DelayedTask;
import com.metalheart.repository.inmemory.DelayedTaskRepository;
import com.metalheart.service.DelayedTaskService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelayedTaskServiceImpl implements DelayedTaskService {

    @Autowired
    private DelayedTaskRepository delayedTaskRepository;

    @Override
    public DelayedTask add(DelayedTask task) {
        return delayedTaskRepository.add(task);
    }

    @Override
    public DelayedTask get(UUID taskId) {
        return delayedTaskRepository.get(taskId);
    }
}

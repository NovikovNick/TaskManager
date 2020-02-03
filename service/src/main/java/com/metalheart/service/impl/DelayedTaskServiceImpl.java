package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.DelayedTask;
import com.metalheart.repository.inmemory.DelayedTaskRepository;
import com.metalheart.service.DelayedTaskService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DelayedTaskServiceImpl implements DelayedTaskService {

    @Autowired
    private DelayedTaskRepository delayedTaskRepository;

    @LogOperationContext
    @Override
    public DelayedTask add(DelayedTask task) {
        DelayedTask add = delayedTaskRepository.add(task);
        log.info("Delayed task has been saved");
        return add;
    }

    @Override
    public DelayedTask get(UUID taskId) {
        return delayedTaskRepository.get(taskId);
    }

    @Override
    public boolean remove(UUID taskId) {

        boolean removed = delayedTaskRepository.remove(taskId);

        if(removed) {
            log.info("Delayed task has been removed");
        }
        return removed;
    }
}

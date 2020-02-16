package com.metalheart.service.impl;

import com.metalheart.config.AppProperties;
import com.metalheart.log.LogOperationContext;
import com.metalheart.model.DelayedTask;
import com.metalheart.repository.inmemory.DelayedTaskRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.DelayedTaskService;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DelayedTaskServiceImpl implements DelayedTaskService {

    @Autowired
    private DelayedTaskRepository delayedTaskRepository;

    @Autowired
    private AppProperties properties;

    @Autowired
    private DateService dateService;

    @LogOperationContext
    @Override
    public DelayedTask add(DelayedTask task) {

        ZonedDateTime now = dateService.now();
        Duration taskTimeout = properties.getTaskTimeout();
        task.setExpiredAt(now.plus(taskTimeout).toInstant());

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

        if (removed) {
            log.info("Delayed " + taskId + " task has been removed");
        }
        return removed;
    }

    @Scheduled(fixedDelayString = "${runninglist.removeExpiredTaskJobDelayInMs}")
    public void removeExpired() {

        Instant now = dateService.now().toInstant();

        List<DelayedTask> tasks = delayedTaskRepository.getAll()
            .stream()
            .filter(task -> now.isAfter(task.getExpiredAt()))
            .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(tasks)) {

            log.info(String.format("Try to remove %d expired tasks", tasks.size()));
            tasks.stream()
                .map(DelayedTask::getTaskId)
                .forEach(this::remove);
        }
    }
}

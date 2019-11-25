package com.metalheart.repository.inmemory.impl;

import com.metalheart.repository.inmemory.TaskPriorityRepository;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class TaskPriorityRepositoryImpl implements TaskPriorityRepository {

    private AtomicInteger integer = new AtomicInteger();

    @Override
    public Integer incrementAndGetMaxPriority() {
        return integer.incrementAndGet();
    }

    @Override
    public void setMaxPriority(int newValue) {
        integer.set(newValue);
    }
}

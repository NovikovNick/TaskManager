package com.metalheart.repository.inmemory;

public interface TaskPriorityRepository {
    Integer incrementAndGetMaxPriority();

    void setMaxPriority(int newValue);
}

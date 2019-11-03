package com.metalheart.repository.inmemory;

public interface ITaskPriorityRepository {
    Integer incrementAndGetMaxPriority();

    void setMaxPriority(int newValue);
}

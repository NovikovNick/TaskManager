package com.metalheart.service;

import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekId;

public interface RunningListCommandService {

    Task createTask(Task request);

    void changeTaskStatus(Integer taskId, Integer dayIndex, TaskStatus status);

    void delete(Integer taskId);

    void update(Task request);

    void archive(Integer userId, WeekId weekId) throws RunningListArchiveAlreadyExistException;

    void reorderTask(Integer userId, Integer startIndex, Integer endIndex);
}
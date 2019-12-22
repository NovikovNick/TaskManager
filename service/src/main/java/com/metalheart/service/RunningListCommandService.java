package com.metalheart.service;

import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekId;
import com.metalheart.model.rest.request.CreateTaskRequest;

public interface RunningListCommandService {

    Task createTask(CreateTaskRequest request);

    void changeTaskStatus(Integer taskId, Integer dayIndex, TaskStatus status);

    void delete(Integer taskId);

    void update(Task request);

    void archive(WeekId weekId) throws RunningListArchiveAlreadyExistException;

    void reorderTask(Integer startIndex, Integer endIndex);
}
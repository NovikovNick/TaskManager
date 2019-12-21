package com.metalheart.service;

import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekId;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import com.metalheart.model.service.TaskModel;

public interface RunningListCommandService {

    TaskModel createTask(CreateTaskRequest request);

    void changeTaskStatus(Integer taskId, Integer dayIndex, TaskStatus status);

    void delete(Integer taskId);

    void update(UpdateTaskRequest request);

    void archive(WeekId weekId) throws RunningListArchiveAlreadyExistException;

    void reorderTask(Integer startIndex, Integer endIndex);
}
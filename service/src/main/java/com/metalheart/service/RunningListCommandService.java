package com.metalheart.service;

import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;

public interface RunningListCommandService {
    Task createTask(CreateTaskRequest request);

    void changeTaskStatus(ChangeTaskStatusRequest request);
}

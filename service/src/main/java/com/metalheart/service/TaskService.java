package com.metalheart.service;

import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import java.util.List;

public interface TaskService {

    List<TaskViewModel> getTaskList();

    void createTask(CreateTaskRequest request);

    void delete(Integer taskId);

    void changeTaskStatus(ChangeTaskStatusRequest request);

    void update(UpdateTaskRequest request);

    void reorderTask(ChangeTaskPriorityRequest request);
}

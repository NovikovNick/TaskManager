package com.metalheart.service;

import com.metalheart.model.ChangeTaskPriorityRequest;
import com.metalheart.model.ChangeTaskStatusRequest;
import com.metalheart.model.CreateTaskRequest;
import com.metalheart.model.TaskViewModel;
import com.metalheart.model.UpdateTaskRequest;
import java.util.List;

public interface TaskService {

    List<TaskViewModel> getTaskList();

    void createTask(CreateTaskRequest request);

    void delete(Integer taskId);

    void changeTaskStatus(ChangeTaskStatusRequest request);

    void update(UpdateTaskRequest request);

    void reorderTask(ChangeTaskPriorityRequest request);
}

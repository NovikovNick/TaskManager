package com.metalheart.service;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService {

    List<Task> getAllTasks();

    /**
     *
     * @param request
     * @return
     */
    Task createTask(CreateTaskRequest request);

    @Transactional
    TaskModel getTask(Integer taskId);

    /**
     * Can be undone
     * @param taskId
     */
    void delete(Integer taskId);

    void update(UpdateTaskRequest request);

    void changeTaskStatus(ChangeTaskStatusRequest request);

    void reorderTask(ChangeTaskPriorityRequest request);
}

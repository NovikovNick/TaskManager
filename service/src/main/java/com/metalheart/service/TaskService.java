package com.metalheart.service;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    TaskModel getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    /**
     * Can be undone
     * @param request
     * @return
     */
    Task createTask(CreateTaskRequest request);

    /**
     * Can be undone
     * @param taskId
     */
    void delete(Integer taskId);

    /**
     * Can be undone
     * @param request
     * @return
     */
    void update(UpdateTaskRequest request);

    /**
     * Can be undone
     * @param request
     * @return
     */
    void changeTaskStatus(ChangeTaskStatusRequest request);

    void reorderTask(ChangeTaskPriorityRequest request);
}

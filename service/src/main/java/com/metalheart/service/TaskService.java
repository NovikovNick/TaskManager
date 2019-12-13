package com.metalheart.service;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.service.DeleteTaskRequest;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    TaskModel getTaskModel(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    Task create(CreateTaskRequest request);

    void delete(Task task);

    void save(Task task);

    void deleteTaskWithWorklog(DeleteTaskRequest request);

    void undoRemoving(DeleteTaskRequest request);

    void reorderTask(ChangeTaskPriorityRequest request);
}

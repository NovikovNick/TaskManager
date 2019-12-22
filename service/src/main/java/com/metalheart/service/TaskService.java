package com.metalheart.service;

import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.DeleteTaskRequest;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    Task create(CreateTaskRequest request);

    void delete(Task task);

    void save(Task task);

    void deleteTaskWithWorklog(DeleteTaskRequest request);

    void undoRemoving(DeleteTaskRequest request);

    void save(List<Task> tasks);
}

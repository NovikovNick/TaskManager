package com.metalheart.service;

import com.metalheart.model.DeleteTaskRequest;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    Task create(Task request);

    void delete(Task task);

    void save(Task task);

    void deleteTaskWithWorklog(DeleteTaskRequest request);

    void undoRemoving(DeleteTaskRequest request);

    void save(List<Task> tasks);
}

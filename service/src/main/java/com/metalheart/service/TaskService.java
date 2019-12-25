package com.metalheart.service;

import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    Task create(Task request);

    Task save(Task task);

    void delete(Integer taskId);

    void undoRemoving(Integer taskId);

    void save(List<Task> tasks);
}

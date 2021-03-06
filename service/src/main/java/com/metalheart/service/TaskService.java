package com.metalheart.service;

import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    void reorder(Integer userId);

    List<Task> getTasks(Integer userId);

    Task getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    Task create(Task request);

    Task save(Task task);

    void remove(Integer taskId);

    void undoRemoving(Integer taskId);

    void save(List<Task> tasks);
}

package com.metalheart.service;

import com.metalheart.model.service.TaskModel;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.DeleteTaskRequest;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskModel> getAllTasks();

    TaskModel getTask(Integer taskId);

    Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex);

    TaskModel create(CreateTaskRequest request);

    void delete(TaskModel task);

    void save(TaskModel task);

    void deleteTaskWithWorklog(DeleteTaskRequest request);

    void undoRemoving(DeleteTaskRequest request);

    void save(List<TaskModel> tasks);
}

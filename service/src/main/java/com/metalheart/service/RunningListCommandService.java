package com.metalheart.service;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekId;
import java.util.List;

public interface RunningListCommandService {

    Task createTask(Integer userId, Task request);

    void changeTaskStatus(Integer userId, Integer taskId, Integer dayIndex, TaskStatus status);

    void delete(Integer userId, Integer taskId);

    void update(Integer userId, Task request);

    void archive(Integer userId, WeekId weekId);

    void reorderTask(Integer userId, Integer startIndex, Integer endIndex);

    void updateProfile(Integer userId, String username, String email, List<Tag> tags);
}
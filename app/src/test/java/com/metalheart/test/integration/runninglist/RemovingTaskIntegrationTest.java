package com.metalheart.test.integration.runninglist;

import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RemovingTaskIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Test
    public void testRemoving() {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();
        Task task = taskService.createTask(request);
        Integer taskId = task.getId();

        // act
        taskService.delete(taskId);

        // assert
        Assert.assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    public void testUndoRemoving() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();
        Task task = taskService.createTask(request);

        // act
        taskService.delete(task.getId());
        commandManager.undo();

        // assert
        List<Task> tasks = taskService.getAllTasks();
        Assert.assertFalse(tasks.isEmpty());
        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();
        Task task = taskService.createTask(request);
        Integer taskId = task.getId();

        // act
        taskService.delete(taskId);
        commandManager.undo();
        commandManager.redo();

        // assert
        Assert.assertTrue(taskService.getAllTasks().isEmpty());
    }
}

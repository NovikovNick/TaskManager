package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RemovingTaskIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Test
    public void testRemoving() {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();
        Task task = runningListCommandService.createTask(request);
        Integer taskId = task.getId();

        // act
        runningListCommandService.delete(taskId);

        // assert
        Assert.assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    public void testUndoRemoving() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();
        Task task = runningListCommandService.createTask(request);

        // act
        runningListCommandService.delete(task.getId());
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
        Task task = runningListCommandService.createTask(request);
        Integer taskId = task.getId();

        // act
        runningListCommandService.delete(taskId);
        commandManager.undo();
        commandManager.redo();

        // assert
        Assert.assertTrue(taskService.getAllTasks().isEmpty());
    }
}

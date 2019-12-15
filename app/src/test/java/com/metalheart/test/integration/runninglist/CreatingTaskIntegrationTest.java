package com.metalheart.test.integration.runninglist;

import com.metalheart.model.service.TaskModel;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatingTaskIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Test
    public void testCreating() {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        runningListCommandService.createTask(request);

        // assert

        List<TaskModel> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        runningListCommandService.createTask(request);
        commandManager.undo();

        // assert

        List<TaskModel> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.isEmpty());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        runningListCommandService.createTask(request);
        commandManager.undo();
        commandManager.redo();

        // assert

        List<TaskModel> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }
}

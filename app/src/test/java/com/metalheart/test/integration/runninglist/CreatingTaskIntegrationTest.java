package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
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
        Task request = generateRandomTask();

        // act
        runningListCommandService.createTask(request);

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        Task request = generateRandomTask();

        // act
        runningListCommandService.createTask(request);
        commandManager.undo();

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.isEmpty());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        Task request = generateRandomTask();

        // act
        runningListCommandService.createTask(request);
        commandManager.undo();
        commandManager.redo();

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }
}

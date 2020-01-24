package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.model.User;
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
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);

        // act
        runningListCommandService.createTask(userId, request);

        // assert

        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);

        // act
        runningListCommandService.createTask(userId, request);
        commandManager.undo(userId);

        // assert

        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.isEmpty());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);

        // act
        runningListCommandService.createTask(userId, request);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert

        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }
}

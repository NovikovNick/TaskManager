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
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);
        Task task = runningListCommandService.createTask(userId, request);
        Integer taskId = task.getId();

        // act
        runningListCommandService.delete(userId, taskId);

        // assert
        Assert.assertTrue(taskService.getTasks(userId).isEmpty());
    }

    @Test
    public void testUndoRemoving() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);
        Task task = runningListCommandService.createTask(userId, request);

        // act
        runningListCommandService.delete(userId, task.getId());
        commandManager.undo(userId);

        // assert
        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertFalse(tasks.isEmpty());
        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = generateRandomTask(userId);
        Task task = runningListCommandService.createTask(userId, request);
        Integer taskId = task.getId();

        // act
        runningListCommandService.delete(userId, taskId);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        Assert.assertTrue(taskService.getTasks(userId).isEmpty());
    }
}

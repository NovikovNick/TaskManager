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

public class UpdateTaskIntegrationTest extends BaseIntegrationTest {

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
        Task createRequest = getTask(userId, "Created task");
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        Task updateRequest = Task.builder()
            .id(createdTask.getId())
            .title("Updated title")
            .description("Updated description")
            .build();

        // act

        runningListCommandService.update(userId, updateRequest);

        // assert
        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(updateRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(updateRequest.getDescription(), tasks.get(0).getDescription());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task createRequest = getTask(userId, "Created task");
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        Task updateRequest = Task.builder()
            .id(createdTask.getId())
            .title("Updated title")
            .description("Updated description")
            .build();

        // act
        runningListCommandService.update(userId, updateRequest);
        commandManager.undo(userId);

        // assert
        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(createRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(createRequest.getDescription(), tasks.get(0).getDescription());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task createRequest = getTask(userId, "Created task");
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        Task updateRequest = Task.builder()
            .id(createdTask.getId())
            .title("Updated title")
            .description("Updated description")
            .build();

        // act
        runningListCommandService.update(userId, updateRequest);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        List<Task> tasks = taskService.getTasks(userId);
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(updateRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(updateRequest.getDescription(), tasks.get(0).getDescription());
    }
}

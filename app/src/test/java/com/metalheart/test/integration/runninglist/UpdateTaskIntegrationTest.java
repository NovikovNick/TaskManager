package com.metalheart.test.integration.runninglist;

import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
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
        CreateTaskRequest createRequest = getCreateTaskRequest("Created task");
        Task createdTask = runningListCommandService.createTask(createRequest);

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setId(createdTask.getId());
        updateRequest.setTitle("Updated title");
        updateRequest.setDescription("Updated description");

        // act

        taskService.update(updateRequest);

        // assert
        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(updateRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(updateRequest.getDescription(), tasks.get(0).getDescription());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        CreateTaskRequest createRequest = getCreateTaskRequest("Created task");
        Task createdTask = runningListCommandService.createTask(createRequest);

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setId(createdTask.getId());
        updateRequest.setTitle("Updated title");
        updateRequest.setDescription("Updated description");

        // act
        taskService.update(updateRequest);
        commandManager.undo();

        // assert
        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(createRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(createRequest.getDescription(), tasks.get(0).getDescription());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        CreateTaskRequest createRequest = getCreateTaskRequest("Created task");
        Task createdTask = runningListCommandService.createTask(createRequest);

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setId(createdTask.getId());
        updateRequest.setTitle("Updated title");
        updateRequest.setDescription("Updated description");

        // act
        taskService.update(updateRequest);
        commandManager.undo();
        commandManager.redo();

        // assert
        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(updateRequest.getTitle(), tasks.get(0).getTitle());
        Assert.assertEquals(updateRequest.getDescription(), tasks.get(0).getDescription());
    }
}

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

public class CreatingTaskIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskJpaRepository taskRepository;

    @Autowired
    private RunningListCommandManager commandManager;

    @Test
    public void testCreating() {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        taskService.createTask(request);

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        taskService.createTask(request);
        commandManager.undo();

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);
        Assert.assertTrue(tasks.isEmpty());
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        CreateTaskRequest request = generateRandomCreateTaskRequest();

        // act
        taskService.createTask(request);
        commandManager.undo();
        commandManager.redo();

        // assert

        List<Task> tasks = taskService.getAllTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(request.getTitle(), tasks.get(0).getTitle());
    }
}

package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        List<Task> fetchedTasks = taskService.getTasks(userId);
        Assert.assertTrue(fetchedTasks.isEmpty());
    }

    @Test
    public void testOrderAfterRemoving() {

        // arrange
        Integer userId = generateUser();

        List<Task> tasks = IntStream.range(0, 20)
            .mapToObj(i -> runningListCommandService.createTask(userId, generateRandomTask(userId)))
            .collect(Collectors.toList());


        // act
        IntStream.range(0, 5).forEach(i -> runningListCommandService.delete(userId, tasks.get(i).getId()));


        // assert
        Task createdTask = runningListCommandService.createTask(userId, generateRandomTask(userId));

        List<Task> fetchedTasks = taskService.getTasks(userId);
        Task lastTask = fetchedTasks.get(fetchedTasks.size() - 1);

        Assert.assertEquals(lastTask.getId(), createdTask.getId());
    }

    @Test
    public void testOrderAfterUndoRemoving() throws Exception{

        // arrange
        Integer userId = generateUser();

        List<Task> tasks = IntStream.range(0, 20)
            .mapToObj(i -> runningListCommandService.createTask(userId, generateRandomTask(userId)))
            .collect(Collectors.toList());

        IntStream.range(0, 5).forEach(i -> runningListCommandService.delete(userId, tasks.get(i).getId()));


        // act
        for (int i = 0; i < 5; i++) {
            commandManager.undo(userId);
        }


        // assert
        List<Task> fetchedTasks = taskService.getTasks(userId);

        Assert.assertEquals(tasks.size(), fetchedTasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            Assert.assertEquals(tasks.get(i).getPriority(), fetchedTasks.get(i).getPriority());
        }
    }

    @Test
    public void testOrderAfterRedoRemoving() throws Exception{

        // arrange
        Integer userId = generateUser();

        List<Task> tasks = IntStream.range(0, 20)
            .mapToObj(i -> runningListCommandService.createTask(userId, generateRandomTask(userId)))
            .collect(Collectors.toList());

        IntStream.range(0, 5).forEach(i -> runningListCommandService.delete(userId, tasks.get(i).getId()));


        // act
        for (int i = 0; i < 5; i++) {
            commandManager.undo(userId);
        }
        for (int i = 0; i < 5; i++) {
            commandManager.redo(userId);
        }

        // assert
        Task createdTask = runningListCommandService.createTask(userId, generateRandomTask(userId));

        List<Task> fetchedTasks = taskService.getTasks(userId);
        Task lastTask = fetchedTasks.get(fetchedTasks.size() - 1);

        Assert.assertEquals(lastTask.getId(), createdTask.getId());
    }
}

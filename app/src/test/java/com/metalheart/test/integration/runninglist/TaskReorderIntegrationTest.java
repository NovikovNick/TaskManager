package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskReorderIntegrationTest extends BaseIntegrationTest {

    public static final String PREFIX = "task_";

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Test
    public void testOrderAfterTaskCreation() {

        // arrange
        // act
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 0, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(4).getTitle());
    }

    @Test
    public void testOrderFromTopToBottom() {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 0, 4);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 1, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(4).getTitle());
    }

    @Test
    public void testOrderFromBottomToTop() {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 4, 0);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 4, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(4).getTitle());
    }

    @Test
    public void testOrderFromBottomToMiddle() {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 4, 2);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 0, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(4).getTitle());
    }

    @Test
    public void testOrderFromTopToMiddle() {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 0, 2);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 1, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(4).getTitle());
    }

    @Test
    public void testUndoOrderFromTopToMiddle() throws Exception {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 0, 2);
        commandManager.undo(userId);


        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 0, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(4).getTitle());
    }

    @Test
    public void testRedoOrderFromTopToMiddle() throws Exception {

        // arrange
        Integer userId = generateUser();
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(userId, 0, 2);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 1, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(4).getTitle());
    }
}

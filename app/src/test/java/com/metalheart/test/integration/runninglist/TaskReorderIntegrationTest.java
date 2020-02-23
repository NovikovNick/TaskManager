package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.IntStream.range;

public class TaskReorderIntegrationTest extends BaseIntegrationTest {

    public static final String PREFIX = "task_";

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Test
    public void testOrderAfterTaskCreation() {

        // arrange
        // act
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));


        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
    }

    @Test
    public void testOrderFromTopToBottom() {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 0, 4);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
    }

    @Test
    public void testOrderFromBottomToTop() {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 4, 0);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
    }

    @Test
    public void testOrderFromBottomToMiddle() {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 4, 2);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
    }

    @Test
    public void testOrderFromTopToMiddle() {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 0, 2);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
    }

    @Test
    public void testUndoOrderFromTopToMiddle() throws Exception {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 0, 2);
        commandManager.undo(userId);


        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
    }

    @Test
    public void testRedoOrderFromTopToMiddle() throws Exception {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(userId, 0, 2);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        int i = 0;
        Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
    }

    @Test
    public void testMaxPriority() throws Exception {

        // arrange
        Integer userId = generateUser();
        range(0, 5).forEach(i -> runningListCommandService.createTask(userId, getTask(userId, PREFIX + i)));

        // act
        Integer maxPriority = taskJpaRepository.getMaxPriority(userId);

        // assert
        Assert.assertEquals(Integer.valueOf(5), maxPriority);
    }

    @Test
    public void testOrderForSeveralUsers() {

        // arrange
        Integer user1 = generateUser();
        Integer user2 = generateUser();

        range(0, 5).forEach(i -> runningListCommandService.createTask(user1, getTask(user1, PREFIX + i)));
        range(0, 5).forEach(i -> runningListCommandService.createTask(user2, getTask(user2, PREFIX + i)));

        // act
        runningListCommandService.reorderTask(user1, 4, 0);
        runningListCommandService.reorderTask(user2, 4, 0);

        // assert
        {// user  1
            List<Task> tasks = runningListService.getRunningList(user1, 0).getTasks();
            int i = 0;
            Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        }

        {// user 2
            List<Task> tasks = runningListService.getRunningList(user2, 0).getTasks();
            int i = 0;
            Assert.assertEquals(PREFIX + 4, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 0, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 1, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 2, tasks.get(i++).getTitle());
            Assert.assertEquals(PREFIX + 3, tasks.get(i++).getTitle());
        }
    }
}

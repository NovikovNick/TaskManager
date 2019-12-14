package com.metalheart.test.integration.runninglist;

import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
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
    private TaskService taskService;

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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(0).endIndex(4).build());

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(4).endIndex(0).build());

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(4).endIndex(2).build());

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(0).endIndex(2).build());

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(0).endIndex(2).build());
        commandManager.undo();


        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
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
        IntStream.range(0, 5)
            .mapToObj(i -> runningListCommandService.createTask(getCreateTaskRequest(PREFIX + i)))
            .collect(Collectors.toList());

        // act
        runningListCommandService.reorderTask(ChangeTaskPriorityRequest.builder().startIndex(0).endIndex(2).build());
        commandManager.undo();
        commandManager.redo();

        // assert
        List<TaskViewModel> tasks = runningListService.getRunningList().getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(5, tasks.size());
        Assert.assertEquals(PREFIX + 1, tasks.get(0).getTitle());
        Assert.assertEquals(PREFIX + 2, tasks.get(1).getTitle());
        Assert.assertEquals(PREFIX + 0, tasks.get(2).getTitle());
        Assert.assertEquals(PREFIX + 3, tasks.get(3).getTitle());
        Assert.assertEquals(PREFIX + 4, tasks.get(4).getTitle());
    }
}

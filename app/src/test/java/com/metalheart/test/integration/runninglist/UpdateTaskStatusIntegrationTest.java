package com.metalheart.test.integration.runninglist;

import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateTaskStatusIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListService runningListService;

    @Test
    public void testCreating() {

        // arrange
        CreateTaskRequest createRequest = generateCreateTaskRequest("Created task");
        Task createdTask = taskService.createTask(createRequest);
        ChangeTaskStatusRequest updateRequest = new ChangeTaskStatusRequest();
        updateRequest.setTaskId(createdTask.getId());
        updateRequest.setDayIndex(0);
        updateRequest.setStatus(TaskStatus.DONE);

        // act
        taskService.changeTaskStatus(updateRequest);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertTrue(runningList.getTasks().get(0).getStatus().get(0).equals(TaskStatus.DONE.toString()));
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        CreateTaskRequest createRequest = generateCreateTaskRequest("Created task");
        Task createdTask = taskService.createTask(createRequest);
        ChangeTaskStatusRequest updateRequest = new ChangeTaskStatusRequest();
        updateRequest.setTaskId(createdTask.getId());
        updateRequest.setDayIndex(0);
        updateRequest.setStatus(TaskStatus.DONE);

        // act
        taskService.changeTaskStatus(updateRequest);
        commandManager.undo();

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertTrue(runningList.getTasks().get(0).getStatus().get(0).equals(TaskStatus.NONE.toString()));
    }

    @Test
    public void testUndoAfterUpdateStatusOperation() throws Exception {

        // arrange
        CreateTaskRequest createRequest = generateCreateTaskRequest("Created task");
        Task createdTask = taskService.createTask(createRequest);

        ChangeTaskStatusRequest todoStatusRequest = new ChangeTaskStatusRequest();
        todoStatusRequest.setTaskId(createdTask.getId());
        todoStatusRequest.setDayIndex(0);
        todoStatusRequest.setStatus(TaskStatus.TO_DO);

        ChangeTaskStatusRequest doneStatusRequest = new ChangeTaskStatusRequest();
        doneStatusRequest.setTaskId(createdTask.getId());
        doneStatusRequest.setDayIndex(0);
        doneStatusRequest.setStatus(TaskStatus.DONE);

        // act
        taskService.changeTaskStatus(todoStatusRequest);
        taskService.changeTaskStatus(doneStatusRequest);
        commandManager.undo();

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertTrue(runningList.getTasks().get(0).getStatus().get(0).equals(TaskStatus.TO_DO.toString()));
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        CreateTaskRequest createRequest = generateCreateTaskRequest("Created task");
        Task createdTask = taskService.createTask(createRequest);
        ChangeTaskStatusRequest updateRequest = new ChangeTaskStatusRequest();
        updateRequest.setTaskId(createdTask.getId());
        updateRequest.setDayIndex(0);
        updateRequest.setStatus(TaskStatus.DONE);

        // act
        taskService.changeTaskStatus(updateRequest);
        commandManager.undo();
        commandManager.redo();
        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertTrue(runningList.getTasks().get(0).getStatus().get(0).equals(TaskStatus.DONE.toString()));
    }
}

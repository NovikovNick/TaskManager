package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.metalheart.model.TaskStatus.CANCELED;
import static com.metalheart.model.TaskStatus.DELAYED;
import static com.metalheart.model.TaskStatus.DONE;
import static com.metalheart.model.TaskStatus.IN_PROGRESS;
import static com.metalheart.model.TaskStatus.NONE;
import static com.metalheart.model.TaskStatus.TO_DO;
import static org.junit.Assert.assertEquals;

public class RunningListIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @MockBean
    private DateService dateService;

    @Test
    public void testTodoStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, TO_DO);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(TO_DO, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testInProcessStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, IN_PROGRESS);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(IN_PROGRESS, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testNoneStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, TO_DO);
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, NONE);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(NONE, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testDelayedStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);

        setDate(this.dateService, 2019, 1, 3);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, TO_DO);
        runningListCommandService.changeTaskStatus(createdTask.getId(), 1, TO_DO);
        runningListCommandService.changeTaskStatus(createdTask.getId(), 2, TO_DO);
        runningListCommandService.changeTaskStatus(createdTask.getId(), 3, TO_DO);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(DELAYED, DELAYED, DELAYED, TO_DO, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testDoneStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);
        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 0, DONE);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(DONE, DONE, DONE, DONE, DONE, DONE, DONE), statuses);
    }

    @Test
    public void testCancelStatus() {

        // arrange
        Task createRequest = generateRandomTask();
        Task createdTask = runningListCommandService.createTask(createRequest);
        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(createdTask.getId(), 2, CANCELED);

        // assert
        RunningListViewModel runningList = runningListService.getRunningList();
        List<String> statuses = runningList.getTasks().get(0).getStatus();
        assertEquals(toStingList(NONE, NONE, CANCELED, CANCELED, CANCELED, CANCELED, CANCELED), statuses);
    }
}

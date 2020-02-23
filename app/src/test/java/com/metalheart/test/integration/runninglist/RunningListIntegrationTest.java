package com.metalheart.test.integration.runninglist;

import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekWorkLog;
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
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
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
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, TO_DO);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());

        assertEquals(asList(TO_DO, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testInProcessStatus() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, IN_PROGRESS);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(IN_PROGRESS, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testNoneStatus() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, TO_DO);
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, NONE);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testDelayedStatus() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 3);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, TO_DO);
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 1, TO_DO);
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 2, TO_DO);
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 3, TO_DO);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(DELAYED, DELAYED, DELAYED, TO_DO, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testDoneStatus() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);
        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, DONE);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(DONE, DONE, DONE, DONE, DONE, DONE, DONE), statuses);
    }

    @Test
    public void testCancelStatus() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);
        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 2, CANCELED);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, 0);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(NONE, NONE, CANCELED, CANCELED, CANCELED, CANCELED, CANCELED), statuses);
    }
}

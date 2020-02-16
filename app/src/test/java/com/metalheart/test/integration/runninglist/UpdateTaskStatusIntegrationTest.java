package com.metalheart.test.integration.runninglist;

import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekWorkLog;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.metalheart.model.TaskStatus.DONE;
import static com.metalheart.model.TaskStatus.IN_PROGRESS;
import static com.metalheart.model.TaskStatus.NONE;
import static com.metalheart.model.TaskStatus.TO_DO;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class UpdateTaskStatusIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @MockBean
    private DateService dateService;

    @Test
    public void simpleUpdateStatusTest() {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, IN_PROGRESS);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, null);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(IN_PROGRESS, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testUndoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, DONE);
        commandManager.undo(userId);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, null);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testUndoAfterUpdateStatusOperation() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, TO_DO);
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, DONE);
        commandManager.undo(userId);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, null);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(TO_DO, NONE, NONE, NONE, NONE, NONE, NONE), statuses);
    }

    @Test
    public void testRedoCreating() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task createRequest = generateRandomTask(userId);
        Task createdTask = runningListCommandService.createTask(userId, createRequest);

        setDate(this.dateService, 2019, 1, 0);

        // act
        runningListCommandService.changeTaskStatus(userId, createdTask.getId(), 0, DONE);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        RunningList runningList = runningListService.getRunningList(userId, null);
        List<TaskStatus> statuses = runningList.getTasks().get(0).getStatus()
            .stream().map(WeekWorkLog::getStatus)
            .collect(toList());
        assertEquals(asList(DONE, DONE, DONE, DONE, DONE, DONE, DONE), statuses);
    }
}

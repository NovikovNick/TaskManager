package com.metalheart.test.integration.runninglist;

import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.WeekId;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RunningListArchiveIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    private DateService dateService;

    @Test
    public void testPreviousArchive() {

        // arrange
        Integer userId = generateUser();
        WeekId weekId = dateService.getCurrentWeekId(null);

        // act
        boolean hasPreviousArchive = archiveService.hasPreviousArchive(userId, weekId);

        // assert
        Assert.assertFalse(hasPreviousArchive);
    }

    @Test
    public void testArchive() throws Exception {

        // arrange
        Integer userId = generateUser();
        WeekId weekId = dateService.getCurrentWeekId(null);
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(userId, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(userId, request);


        // act
        runningListCommandService.archive(userId, previousWeekId);

        // assert

        Assert.assertTrue(archiveService.hasPreviousArchive(userId, weekId));

        Optional<RunningList> archive = archiveService.getPrev(userId, weekId, null);
        Assert.assertTrue(archive.isPresent());

        List<Task> tasks = archive.get().getTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(taskTitle, tasks.get(0).getTitle());
        Assert.assertEquals(taskDescription, tasks.get(0).getDescription());
    }

    @Test
    public void testUndoArchiveOperation() throws Exception {

        // arrange

        Integer userId = generateUser();
        WeekId weekId = dateService.getCurrentWeekId(null);
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(userId, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(userId, request);


        // act
        runningListCommandService.archive(userId, previousWeekId);
        commandManager.undo(userId);


        // assert

        Assert.assertFalse(archiveService.hasPreviousArchive(userId, weekId));
    }

    @Test
    public void testRedoArchiveOperation() throws Exception {

        // arrange

        Integer userId = generateUser();

        WeekId weekId = dateService.getCurrentWeekId(null);
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(userId, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(userId, request);


        // act
        runningListCommandService.archive(userId, previousWeekId);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert

        Assert.assertTrue(archiveService.hasPreviousArchive(userId, weekId));

        Optional<RunningList> archive = archiveService.getPrev(userId, weekId, null);
        Assert.assertTrue(archive.isPresent());

        List<Task> tasks = archive.get().getTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(taskTitle, tasks.get(0).getTitle());
        Assert.assertEquals(taskDescription, tasks.get(0).getDescription());
    }

    @Test
    public void testUndoSeveralArchiveOperation() throws Exception {

        // arrange

        Integer userId = generateUser();
        WeekId weekId = dateService.getCurrentWeekId(null);
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);


        runningListCommandService.createTask(userId, getTask(userId, "task 1"));

        runningListCommandService.archive(userId, previousWeekId);
        RunningList runningList1 = archiveService.getPrev(userId, weekId, null).get();

        runningListCommandService.createTask(userId, getTask(userId, "task 2"));

        runningListCommandService.archive(userId, previousWeekId);
        RunningList runningList2 = archiveService.getPrev(userId, weekId, null).get();

        runningListCommandService.createTask(userId, getTask(userId, "task 3"));

        runningListCommandService.archive(userId, previousWeekId);
        RunningList runningList3 = archiveService.getPrev(userId, weekId, null).get();


        // act
        commandManager.undo(userId);
        RunningList runningList4 = archiveService.getPrev(userId, weekId, null).get();

        commandManager.undo(userId);
        commandManager.undo(userId);
        RunningList runningList5 = archiveService.getPrev(userId, weekId, null).get();


        // assert
        Assert.assertEquals(3, runningList3.getTasks().size());
        Assert.assertEquals(2, runningList4.getTasks().size());
        Assert.assertEquals(1, runningList5.getTasks().size());
    }
}

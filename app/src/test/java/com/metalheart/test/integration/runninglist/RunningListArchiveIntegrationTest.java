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
        WeekId weekId = dateService.getCurrentWeekId();

        // act
        boolean hasPreviousArchive = archiveService.hasPreviousArchive(weekId);

        // assert
        Assert.assertFalse(hasPreviousArchive);
    }

    @Test
    public void testArchive() throws Exception {

        // arrange

        WeekId weekId = dateService.getCurrentWeekId();
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(1, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(request);


        // act
        runningListCommandService.archive(1, previousWeekId);

        // assert

        Assert.assertTrue(archiveService.hasPreviousArchive(weekId));

        RunningList archive = archiveService.getPrev(weekId);
        Assert.assertNotNull(archive);

        List<Task> tasks = archive.getTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(taskTitle, tasks.get(0).getTitle());
        Assert.assertEquals(taskDescription, tasks.get(0).getDescription());
    }

    @Test
    public void testUndoArchiveOperation() throws Exception {

        // arrange

        WeekId weekId = dateService.getCurrentWeekId();
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(1, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(request);


        // act
        runningListCommandService.archive(1, previousWeekId);
        commandManager.undo();


        // assert

        Assert.assertFalse(archiveService.hasPreviousArchive(weekId));
    }

    @Test
    public void testRedoArchiveOperation() throws Exception {

        // arrange

        WeekId weekId = dateService.getCurrentWeekId();
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        String taskTitle = RandomStringUtils.random(30);
        String taskDescription = RandomStringUtils.random(300);

        Task request = getTask(1, taskTitle);
        request.setDescription(taskDescription);

        runningListCommandService.createTask(request);


        // act
        runningListCommandService.archive(1, previousWeekId);
        commandManager.undo();
        commandManager.redo();

        // assert

        Assert.assertTrue(archiveService.hasPreviousArchive(weekId));

        RunningList archive = archiveService.getPrev(weekId);
        Assert.assertNotNull(archive);

        List<Task> tasks = archive.getTasks();
        Assert.assertNotNull(tasks);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(taskTitle, tasks.get(0).getTitle());
        Assert.assertEquals(taskDescription, tasks.get(0).getDescription());
    }
}

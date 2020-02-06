package com.metalheart.test.integration.runninglist;

import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.User;
import com.metalheart.model.WeekId;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TaskService;
import com.metalheart.service.UserService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIsolationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;


    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    private DateService dateService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreating() {

        // arrange

        User user1 = userService.createUser(createUser("user1"));
        User user2 = userService.createUser(createUser("user2"));

        runningListCommandService.createTask(1, getTask(user1.getId(), "First user1 task"));
        runningListCommandService.createTask(1, getTask(user1.getId(), "Second user1 task"));
        runningListCommandService.createTask(1, getTask(user2.getId(), "First user2 task"));


        // act

        List<Task> user1Tasks = taskService.getTasks(user1.getId());
        List<Task> user2Tasks = taskService.getTasks(user2.getId());


        // assert

        Assert.assertNotNull(user1Tasks);
        Assert.assertNotNull(user2Tasks);

        Assert.assertEquals(2, user1Tasks.size());
        Assert.assertEquals(1, user2Tasks.size());

        Assert.assertEquals("First user1 task", user1Tasks.get(0).getTitle());
        Assert.assertEquals("Second user1 task", user1Tasks.get(1).getTitle());
        Assert.assertEquals("First user2 task", user2Tasks.get(0).getTitle());

    }

    @Test
    public void testArchive() throws Exception {

        // arrange
        Integer user1Id = generateUser();
        Integer user2Id = generateUser();

        WeekId weekId = dateService.getCurrentWeekId();
        WeekId previousWeekId = dateService.getPreviousWeekId(weekId);

        runningListCommandService.createTask(user1Id, getTask(user1Id, "User 1 task"));
        runningListCommandService.createTask(user2Id, getTask(user2Id, "User 2 task"));


        // act
        runningListCommandService.archive(user1Id, previousWeekId);
        runningListCommandService.archive(user2Id, previousWeekId);

        // assert

        Assert.assertTrue(archiveService.hasPreviousArchive(user1Id, weekId));
        Assert.assertTrue(archiveService.hasPreviousArchive(user2Id, weekId));

        RunningList archive1 = archiveService.getPrev(user1Id, weekId).get();
        RunningList archive2 = archiveService.getPrev(user2Id, weekId).get();

        Assert.assertNotNull(archive1);
        Assert.assertNotNull(archive2);

        Assert.assertEquals(1, archive1.getTasks().size());
        Assert.assertEquals(1, archive2.getTasks().size());

        Assert.assertEquals("User 1 task", archive1.getTasks().get(0).getTitle());
        Assert.assertEquals("User 2 task", archive2.getTasks().get(0).getTitle());

    }


    @Test
    public void testSameTaskTitle() {

        // arrange

        User user1 = userService.createUser(createUser("user1"));
        User user2 = userService.createUser(createUser("user2"));

        String title = "First user1 task";

        runningListCommandService.createTask(1, getTask(user1.getId(), title));
        runningListCommandService.createTask(1, getTask(user2.getId(), title));


        // act

        List<Task> user1Tasks = taskService.getTasks(user1.getId());
        List<Task> user2Tasks = taskService.getTasks(user2.getId());


        // assert

        Assert.assertNotNull(user1Tasks);
        Assert.assertNotNull(user2Tasks);

        Assert.assertEquals(1, user1Tasks.size());
        Assert.assertEquals(1, user2Tasks.size());

        Assert.assertEquals(title, user1Tasks.get(0).getTitle());
        Assert.assertEquals(title, user2Tasks.get(0).getTitle());

    }
}

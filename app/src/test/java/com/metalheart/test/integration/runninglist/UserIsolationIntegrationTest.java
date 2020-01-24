package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Task;
import com.metalheart.model.User;
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
    private UserService userService;

    @Test
    public void testCreating() {

        // arrange

        User user1 = userService.createUser(createUser("user1"));
        User user2 = userService.createUser(createUser("user2"));

        runningListCommandService.createTask(getTask(user1.getId(), "First user1 task"));
        runningListCommandService.createTask(getTask(user1.getId(), "Second user1 task"));
        runningListCommandService.createTask(getTask(user2.getId(), "First user2 task"));


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


}

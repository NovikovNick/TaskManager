package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatingTaskWithTagIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Test
    public void testCreatingWithTags() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = getTask(userId, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(userId, request);


        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertFalse(CollectionUtils.isEmpty(tasks));

        List<Tag> tags = tasks.get(0).getTags();
        Assert.assertEquals(2, tags.size());
    }

    @Test
    public void testUndoCreatingWithTags() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = getTask(userId, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(userId, request);
        commandManager.undo(userId);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertTrue(CollectionUtils.isEmpty(tasks));
    }

    @Test
    public void testRedoCreatingWithTags() throws Exception {

        // arrange
        Integer userId = generateUser();
        Task request = getTask(userId, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(userId, request);
        commandManager.undo(userId);
        commandManager.redo(userId);

        // assert
        List<Task> tasks = runningListService.getRunningList(userId, 0).getTasks();
        Assert.assertFalse(CollectionUtils.isEmpty(tasks));

        List<Tag> tags = tasks.get(0).getTags();
        Assert.assertEquals(2, tags.size());
    }
}

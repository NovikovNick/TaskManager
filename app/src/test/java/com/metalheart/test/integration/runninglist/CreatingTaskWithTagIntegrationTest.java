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
        Task request = getTask(1, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(request);


        // assert
        List<Task> tasks = runningListService.getRunningList(1).getTasks();
        Assert.assertFalse(CollectionUtils.isEmpty(tasks));

        List<Tag> tags = tasks.get(0).getTags();
        Assert.assertEquals(2, tags.size());
    }

    @Test
    public void testUndoCreatingWithTags() throws Exception {

        // arrange
        Task request = getTask(1, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(request);
        commandManager.undo();

        // assert
        List<Task> tasks = runningListService.getRunningList(1).getTasks();
        Assert.assertTrue(CollectionUtils.isEmpty(tasks));
    }

    @Test
    public void testRedoCreatingWithTags() throws Exception {

        // arrange
        Task request = getTask(1, "task", "tag1", "tag2");


        // act
        runningListCommandService.createTask(request);
        commandManager.undo();
        commandManager.redo();

        // assert
        List<Task> tasks = runningListService.getRunningList(1).getTasks();
        Assert.assertFalse(CollectionUtils.isEmpty(tasks));

        List<Tag> tags = tasks.get(0).getTags();
        Assert.assertEquals(2, tags.size());
    }
}

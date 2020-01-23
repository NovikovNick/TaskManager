package com.metalheart.test.integration.runninglist;

import com.metalheart.model.RunningList;
import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public class TagIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Test
    public void testCreating() {

        // arrange
        Task createRequest = getTask(1, "Created task");
        Task createdTask = runningListCommandService.createTask(createRequest);

        // act
        tagService.addTagToTask("tag1", createdTask.getId());

        // assert
        List<Tag> tags = tagService.getTags(1);
        Task fetchedTask = taskService.getTask(createdTask.getId());
        Assert.assertNotNull(fetchedTask);
        Assert.assertFalse(CollectionUtils.isEmpty(fetchedTask.getTags()));
        Assert.assertFalse(CollectionUtils.isEmpty(tags));
        Assert.assertTrue(fetchedTask.getTags().containsAll(tags));
    }

    @Test
    public void testSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask(1, "task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask(1, "task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask(1, "task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());


        // act
        tagService.selectTag(1, "tag1");

        // assert

        RunningList runningList = runningListService.getRunningList(1);
        Assert.assertEquals(2, runningList.getTasks().size());
    }

    @Test
    public void testSeveralTagSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask(1, "task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask(1, "task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask(1, "task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());
        tagService.addTagToTask("tag3", createdTask3.getId());


        // act
        tagService.selectTag(1, "tag1");

        // assert
        Assert.assertEquals(1, tagService.getSelectedTags(1).size());

        RunningList runningList = runningListService.getRunningList(1);

        List<Task> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }

    @Test
    public void testSeveralTagSelection2() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask(1, "task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask(1, "task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask(1, "task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());
        tagService.addTagToTask("tag3", createdTask3.getId());


        // act
        tagService.selectTag(1, "tag1");
        RunningList runningList = runningListService.getRunningList(1);
        tagService.selectTag(1, "tag2");
        runningList = runningListService.getRunningList(1);

        // assert
        Assert.assertEquals(2, tagService.getSelectedTags(1).size());



        List<Task> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }

    @Test
    public void testStrictSeveralTagSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask(1, "task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask(1, "task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask(1, "task3"));

        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());

        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());

        tagService.addTagToTask("tag1", createdTask3.getId());


        // act
        tagService.selectTag(1, "tag1");
        tagService.selectTag(1, "tag2");

        // assert
        Assert.assertEquals(2, tagService.getSelectedTags(1).size());

        RunningList runningList = runningListService.getRunningList(1);
        List<Task> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }
}

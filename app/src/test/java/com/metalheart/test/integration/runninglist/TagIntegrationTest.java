package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.rest.response.TaskViewModel;
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
        Task createRequest = getTask("Created task");
        Task createdTask = runningListCommandService.createTask(createRequest);

        // act
        tagService.addTagToTask("tag1", createdTask.getId());

        // assert
        List<Tag> tags = tagService.getAllTags();
        Task fetchedTask = taskService.getTask(createdTask.getId());
        Assert.assertNotNull(fetchedTask);
        Assert.assertFalse(CollectionUtils.isEmpty(fetchedTask.getTags()));
        Assert.assertFalse(CollectionUtils.isEmpty(tags));
        Assert.assertTrue(fetchedTask.getTags().containsAll(tags));
    }

    @Test
    public void testSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask("task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask("task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask("task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());


        // act
        tagService.selectTag("tag1");

        // assert

        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertEquals(2, runningList.getTasks().size());
    }

    @Test
    public void testSeveralTagSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask("task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask("task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask("task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());
        tagService.addTagToTask("tag3", createdTask3.getId());


        // act
        tagService.selectTag("tag1");

        // assert
        Assert.assertEquals(1, tagService.getSelectedTags().size());

        RunningListViewModel runningList = runningListService.getRunningList();

        List<TaskViewModel> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }

    @Test
    public void testSeveralTagSelection2() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask("task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask("task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask("task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());
        tagService.addTagToTask("tag3", createdTask3.getId());


        // act
        tagService.selectTag("tag1");
        RunningListViewModel runningList = runningListService.getRunningList();
        tagService.selectTag("tag2");
        runningList = runningListService.getRunningList();

        // assert
        Assert.assertEquals(2, tagService.getSelectedTags().size());



        List<TaskViewModel> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }

    @Test
    public void testStrictSeveralTagSelection() {

        // arrange
        Task createdTask1 = runningListCommandService.createTask(getTask("task1"));
        Task createdTask2 = runningListCommandService.createTask(getTask("task2"));
        Task createdTask3 = runningListCommandService.createTask(getTask("task3"));

        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag2", createdTask1.getId());

        tagService.addTagToTask("tag1", createdTask2.getId());
        tagService.addTagToTask("tag2", createdTask2.getId());

        tagService.addTagToTask("tag1", createdTask3.getId());


        // act
        tagService.selectTag("tag1");
        tagService.selectTag("tag2");

        // assert
        Assert.assertEquals(2, tagService.getSelectedTags().size());

        RunningListViewModel runningList = runningListService.getRunningList();
        List<TaskViewModel> tasks = runningList.getTasks();
        Assert.assertEquals(2, tasks.size());
        Assert.assertEquals(2, tasks.get(0).getTags().size());
        Assert.assertEquals(2, tasks.get(1).getTags().size());
    }
}

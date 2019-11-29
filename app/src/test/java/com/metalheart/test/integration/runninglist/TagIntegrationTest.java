package com.metalheart.test.integration.runninglist;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.rest.response.TagViewModel;
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

    @Test
    public void testCreating() {

        // arrange
        CreateTaskRequest createRequest = getCreateTaskRequest("Created task");
        Task createdTask = taskService.createTask(createRequest);

        // act
        tagService.addTagToTask("tag1", createdTask.getId());

        // assert
        List<TagViewModel> tags = tagService.getAllTags();
        TaskModel fetchedTask = taskService.getTask(createdTask.getId());
        Assert.assertNotNull(fetchedTask);
        Assert.assertFalse(CollectionUtils.isEmpty(fetchedTask.getTags()));
        Assert.assertFalse(CollectionUtils.isEmpty(tags));
        Assert.assertTrue(fetchedTask.getTags().containsAll(tags));
    }

    @Test
    public void testSelection() {

        // arrange
        Task createdTask1 = taskService.createTask(getCreateTaskRequest("task1"));
        Task createdTask2 = taskService.createTask(getCreateTaskRequest("task2"));
        Task createdTask3 = taskService.createTask(getCreateTaskRequest("task3"));
        tagService.addTagToTask("tag1", createdTask1.getId());
        tagService.addTagToTask("tag1", createdTask2.getId());


        // act
        tagService.selectTag("tag1");

        // assert

        RunningListViewModel runningList = runningListService.getRunningList();
        Assert.assertEquals(2, runningList.getTasks().size());
    }

}

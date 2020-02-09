package com.metalheart.test.integration.runninglist;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.jpa.TagJpa;
import com.metalheart.repository.jpa.TagJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import com.metalheart.service.TaskService;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;
import static java.util.stream.IntStream.range;

public class ProfileIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private RunningListService runningListService;

    @Test
    public void testUpdateTagsWithSelected() throws Exception {

        // arrange
        Integer userId1 = generateUser();

        List<TagJpa> tagsUser1 = range(0, 5)
            .mapToObj(i -> TagJpa.builder().userId(userId1).title("tag" + i).deleted(false).build())
            .map(tagJpaRepository::save)
            .collect(Collectors.toList());

        List<Tag> tagsToUpdate = new ArrayList<>();
        tagsToUpdate.add(conversionService.convert(tagsUser1.get(0), Tag.class));
        tagsToUpdate.add(conversionService.convert(tagsUser1.get(1), Tag.class));
        tagsToUpdate.add(Tag.builder().title("newTag0").build());
        tagsToUpdate.add(Tag.builder().title("newTag1").build());


        // act
        runningListCommandService.updateProfile(userId1, tagsToUpdate);

        { // assert
            List<Tag> allTagsUser1 = tagService.getTags(userId1);

            Assert.assertNotNull(allTagsUser1);
            Assert.assertEquals(4, allTagsUser1.size());
            int i = 0;
            Assert.assertEquals("tag0", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag1", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("newTag0", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("newTag1", allTagsUser1.get(i++).getTitle());
        }


        // act
        commandManager.undo(userId1);

        { // assert
            List<Tag> allTagsUser1 = tagService.getTags(userId1);

            Assert.assertNotNull(allTagsUser1);
            Assert.assertEquals(5, allTagsUser1.size());
            int i = 0;
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
        }


        // act
        commandManager.redo(userId1);

        { // assert
            List<Tag> allTagsUser1 = tagService.getTags(userId1);

            Assert.assertNotNull(allTagsUser1);
            Assert.assertEquals(4, allTagsUser1.size());
            int i = 0;
            Assert.assertEquals("tag0", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag1", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("newTag0", allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("newTag1", allTagsUser1.get(i++).getTitle());
        }
    }

    @Test
    public void testRedoneTaskTag() throws Exception {

        // arrange
        Integer userId1 = generateUser();

        Task task = runningListCommandService.createTask(userId1, getTask(userId1, "task","tag0", "tag1"));

        List<Tag> tagsToUpdate = new ArrayList<>();
        tagsToUpdate.add(Tag.builder().title("tag0").build());


        // act
        runningListCommandService.updateProfile(userId1, tagsToUpdate);
        runningListService.getRunningList(userId1);

        { // assert
            List<Tag> allTagsUser1 = tagService.getTags(userId1);

            Assert.assertNotNull(allTagsUser1);
            Assert.assertEquals(1, allTagsUser1.size());
            Assert.assertEquals("tag0", allTagsUser1.get(0).getTitle());

            List<Task> tasks = taskService.getTasks(userId1);
            Assert.assertNotNull(tasks);

            List<Tag> taskTags = tasks.get(0).getTags();
            Assert.assertNotNull(taskTags);
            Assert.assertEquals(1, taskTags.size());
            Assert.assertEquals("tag0", taskTags.get(0).getTitle());
        }

        // act
        commandManager.undo(userId1);


        { // assert
            List<Tag> allTagsUser1 = tagService.getTags(userId1);

            Assert.assertNotNull(allTagsUser1);
            Assert.assertEquals(2, allTagsUser1.size());
            int i = 0;
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());
            Assert.assertEquals("tag" + i, allTagsUser1.get(i++).getTitle());

            List<Task> tasks = taskService.getTasks(userId1);
            Assert.assertNotNull(tasks);

            List<Tag> taskTags = tasks.get(0).getTags();
            Assert.assertNotNull(taskTags);
            Assert.assertEquals(2, taskTags.size());
            Assert.assertEquals("tag0", taskTags.get(0).getTitle());
            Assert.assertEquals("tag1", taskTags.get(1).getTitle());
        }

    }
}

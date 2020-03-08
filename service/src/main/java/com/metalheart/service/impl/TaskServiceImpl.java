package com.metalheart.service.impl;

import com.metalheart.log.LogContextField;
import com.metalheart.log.LogOperationContext;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.jpa.TagJpa;
import com.metalheart.model.jpa.TaskJpa;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.jpa.TagJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.TaskService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;
import static com.metalheart.log.LogContextField.Field.USER_ID;

@Slf4j
@Component
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private SelectedTagRepository selectedTagRepository;

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @LogOperationContext
    @Override
    public void reorder(@LogContextField(USER_ID) Integer userId) {

        List<Task> taskList = getTasks(userId);

        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setPriority(i);
        }
        save(taskList);

        log.info("Tasks have been reordered");
    }

    @Override
    public List<Task> getTasks(Integer userId) {

        List<Integer> selectedTags = selectedTagRepository.getSelectedTags(userId);

        List<TaskJpa> res;
        if (selectedTags.isEmpty()) {
            res = taskJpaRepository.findAllByUserIdOrderByPriorityAsc(userId);
        } else {
            res = taskJpaRepository.findAllByTags(selectedTags, Long.valueOf(selectedTags.size()));
        }

        return res.stream()
            .map(task -> conversionService.convert(task, Task.class))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex) {

        WeekWorkLogJpaPK id = WeekWorkLogJpaPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<WeekWorkLogJpa> status = weekWorkLogJpaRepository.findById(id);

        if (status.isPresent()) {

            return Optional.ofNullable(status.get().getStatus());
        } else {

            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Task getTask(Integer taskId) {
        TaskJpa task = taskJpaRepository.getOne(taskId);

        return (Task) conversionService.convert(
            task,
            TypeDescriptor.valueOf(TaskJpa.class),
            TypeDescriptor.valueOf(Task.class));
    }


    @LogOperationContext
    @Override
    public Task create(Task task) {

        Integer userId = task.getUserId();

        task = task.toBuilder()
            .createdAt(ZonedDateTime.now())
            .priority(taskJpaRepository.getMaxPriority(userId))
            .build();

        if (CollectionUtils.isNotEmpty(task.getTags())) {
            task.getTags().forEach(tag -> tag.setUserId(userId));
        }

        task = save(task);

        return task;
    }

    @LogOperationContext
    @Override
    public Task save(Task task) {

        TaskJpa entity = conversionService.convert(task, TaskJpa.class);
        entity.setDeleted(false);

        if (CollectionUtils.isNotEmpty(task.getTags())) {

            List<TagJpa> tags = task.getTags().stream()
                .map(tag -> {
                    Integer userId = task.getUserId();
                    TagJpa tagJpa = tagJpaRepository.findTagByUserIdAndTitle(userId, tag.getTitle());
                    if (Objects.isNull(tagJpa)) {
                        tagJpa = conversionService.convert(tag, TagJpa.class);
                        tagJpa.setUserId(userId);
                        tagJpa = tagJpaRepository.save(tagJpa);
                    }
                    return tagJpa;
                })
                .collect(Collectors.toList());
            entity.setTags(tags);
        }

        entity = taskJpaRepository.save(entity);
        return conversionService.convert(entity, Task.class);
    }

    @Transactional
    @LogOperationContext
    @Override
    public void remove(Integer taskId) {
        taskJpaRepository.setDeleted(taskId, true);
    }

    @Transactional
    @LogOperationContext
    @Override
    public void undoRemoving(Integer taskId) {
        taskJpaRepository.setDeleted(taskId, false);
    }

    @Override
    public void save(List<Task> tasks) {
        List<TaskJpa> entities = tasks.stream()
            .map(task -> conversionService.convert(task, TaskJpa.class))
            .collect(Collectors.toList());
        taskJpaRepository.saveAll(entities);
    }
}

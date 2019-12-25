package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.jpa.TagJpa;
import com.metalheart.model.jpa.TaskJpa;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.inmemory.TaskPriorityRepository;
import com.metalheart.repository.jpa.TagJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.TaskService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private SelectedTagRepository selectedTagRepository;

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @PostConstruct
    public void reorder() {

        List<Task> taskList = getAllTasks();
        int maxPriority = taskList.size();
        taskPriorityRepository.setMaxPriority(maxPriority);

        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setPriority(i);
        }
        save(taskList);
    }

    @Override
    public List<Task> getAllTasks() {

        List<Integer> selectedTags = selectedTagRepository.getSelectedTags();

        List<TaskJpa> res;
        if (selectedTags.isEmpty()) {
            res = taskJpaRepository.findAllByOrderByPriorityAsc();
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

        task = task.toBuilder()
            .createdAt(ZonedDateTime.now())
            .priority(taskPriorityRepository.incrementAndGetMaxPriority())
            .build();

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
                    TagJpa tagJpa = tagJpaRepository.findTagByTitle(tag.getTitle());
                    if (Objects.isNull(tagJpa)) {
                        tagJpa = tagJpaRepository.save(conversionService.convert(tag, TagJpa.class));
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
    public void delete(Integer taskId) {
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

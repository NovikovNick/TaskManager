package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.service.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.TaskJpa;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.DeleteTaskRequest;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.inmemory.TaskPriorityRepository;
import com.metalheart.repository.jooq.TaskJooqRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.TagService;
import com.metalheart.service.TaskService;
import java.time.OffsetDateTime;
import java.util.List;
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
    private TaskJooqRepository taskJooqRepository;

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private SelectedTagRepository selectedTagRepository;

    @Autowired
    private TagService tagService;

    @PostConstruct
    public void reorder() {

        List<TaskModel> taskList = getAllTasks();
        int maxPriority = taskList.size();
        taskPriorityRepository.setMaxPriority(maxPriority);

        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setPriority(i);
        }
        save(taskList);
    }

    @Override
    public List<TaskModel> getAllTasks() {

        List<Integer> selectedTags = selectedTagRepository.getSelectedTags();
        List<TaskJpa> res;

        if (selectedTags.isEmpty()) {
            res = taskJpaRepository.findAllByOrderByPriorityAsc();
        } else {
            res = taskJpaRepository.findAllByTags(selectedTags, Long.valueOf(selectedTags.size()));
        }

        return res.stream()
            .map(task -> conversionService.convert(task, TaskModel.class))
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
    public TaskModel getTask(Integer taskId) {
        TaskJpa task = taskJpaRepository.getOne(taskId);

        return (TaskModel) conversionService.convert(
            task,
            TypeDescriptor.valueOf(TaskJpa.class),
            TypeDescriptor.valueOf(TaskModel.class));
    }

    @LogOperationContext
    @Override
    public TaskModel create(CreateTaskRequest request) {

        TaskRecord record = new TaskRecord();
        record.setId(request.getTaskId());
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setCreatedAt(OffsetDateTime.now());
        record.setPriority(taskPriorityRepository.incrementAndGetMaxPriority());

        taskJooqRepository.saveAndGenerateIdIfNotPresent(record);
        if (CollectionUtils.isNotEmpty(request.getTags())) {
            request.getTags().stream()
                .map(tag -> tagService.getTag(tag.getText()))
                .forEach(tag -> tagService.addTagToTask(tag.getTitle(), record.getId()));
        }
        return conversionService.convert(record, TaskModel.class);
    }

    @LogOperationContext
    @Override
    public void delete(TaskModel task) {

        if (CollectionUtils.isNotEmpty(task.getTags())) {
            task.getTags().forEach(tag -> tagService.removeTagFromTask(tag.getText(), task.getId()));
        }
        taskJpaRepository.deleteById(task.getId());
    }

    @LogOperationContext
    @Override
    public void save(TaskModel task) {
        taskJpaRepository.save(conversionService.convert(task, TaskJpa.class));
    }

    @LogOperationContext
    @Override
    public void deleteTaskWithWorklog(DeleteTaskRequest request) {
        taskJpaRepository.deleteById(request.getTask().getId());
        weekWorkLogJpaRepository.deleteAll(request.getWorkLogs().stream()
            .map(weekWorkLog -> conversionService.convert(weekWorkLog, WeekWorkLogJpa.class))
            .collect(Collectors.toList()));
    }

    @LogOperationContext
    @Override
    public void undoRemoving(DeleteTaskRequest request) {

        TaskRecord taskRecord = conversionService.convert(request.getTask(), TaskRecord.class);
        taskJooqRepository.saveAndGenerateIdIfNotPresent(taskRecord);
        weekWorkLogJpaRepository.saveAll(request.getWorkLogs().stream()
            .map(weekWorkLog -> conversionService.convert(weekWorkLog, WeekWorkLogJpa.class))
            .collect(Collectors.toList()));

    }

    @Override
    public void save(List<TaskModel> tasks) {
        taskJpaRepository.saveAll(tasks.stream()
            .map(task -> conversionService.convert(task, TaskJpa.class))
            .collect(Collectors.toList()));
    }
}

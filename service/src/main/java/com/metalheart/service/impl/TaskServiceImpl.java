package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.service.DeleteTaskRequest;
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

        List<Task> taskList = getAllTasks();
        int maxPriority = taskList.size();
        taskPriorityRepository.setMaxPriority(maxPriority);

        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setPriority(i);
        }
        taskJpaRepository.saveAll(taskList);
    }

    @Override
    public List<Task> getAllTasks() {

        List<Integer> selectedTags = selectedTagRepository.getSelectedTags();

        if (selectedTags.isEmpty()) {

            return taskJpaRepository.findAllByOrderByPriorityAsc();
        } else {
            return taskJpaRepository.findAllByTags(selectedTags, Long.valueOf(selectedTags.size()));
        }
    }

    @Override
    public Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex) {

        WeekWorkLogPK id = WeekWorkLogPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<WeekWorkLog> status = weekWorkLogJpaRepository.findById(id);

        if (status.isPresent()) {

            return Optional.ofNullable(status.get().getStatus());
        } else {

            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public TaskModel getTaskModel(Integer taskId) {
        Task task = taskJpaRepository.getOne(taskId);

        return (TaskModel) conversionService.convert(
            task,
            TypeDescriptor.valueOf(Task.class),
            TypeDescriptor.valueOf(TaskModel.class));
    }

    @LogOperationContext
    @Override
    public Task create(CreateTaskRequest request) {

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
        return conversionService.convert(record, Task.class);
    }

    @LogOperationContext
    @Override
    public void delete(Task task) {

        if (CollectionUtils.isNotEmpty(task.getTags())) {
            task.getTags().forEach(tag -> tagService.removeTagFromTask(tag.getTitle(), task.getId()));
        }
        taskJpaRepository.deleteById(task.getId());
    }

    @LogOperationContext
    @Override
    public void save(Task task) {
        taskJpaRepository.save(task);
    }

    @LogOperationContext
    @Override
    public void deleteTaskWithWorklog(DeleteTaskRequest request) {
        taskJpaRepository.deleteById(request.getTask().getId());
        weekWorkLogJpaRepository.deleteAll(request.getWorkLogs());
    }

    @LogOperationContext
    @Override
    public void undoRemoving(DeleteTaskRequest request) {

        TaskRecord taskRecord = conversionService.convert(request.getTask(), TaskRecord.class);
        taskJooqRepository.saveAndGenerateIdIfNotPresent(taskRecord);
        weekWorkLogJpaRepository.saveAll(request.getWorkLogs());

    }

    @Override
    public void reorderTask(ChangeTaskPriorityRequest request) {

        List<Task> tasks = getAllTasks();

        List<Integer> previousPriorities = tasks.stream()
            .map(Task::getPriority)
            .collect(Collectors.toList());

        Task moved = tasks.get(request.getStartIndex());
        tasks.remove(moved);
        tasks.add(request.getEndIndex(), moved);

        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPriority(previousPriorities.get(i));
        }
        taskJpaRepository.saveAll(tasks);
    }
}

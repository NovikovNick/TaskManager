package com.metalheart.service.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.model.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import com.metalheart.repository.inmemory.TaskPriorityRepository;
import com.metalheart.repository.jooq.TaskJooqRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.TaskService;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
    private RunningListCommandManager runningListCommandManager;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private TaskService self;


    @PostConstruct
    public void reorder() {

        List<Task> taskList = getAllTasks();
        int maxPriority = taskList.size();
        taskPriorityRepository.setMaxPriority(maxPriority);

        saveTaskList(taskList);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskJpaRepository.findAllByOrderByPriorityAsc();
    }

    @Override
    public Optional<TaskStatus> getTaskDayStatus(Integer taskId, Integer dayIndex){

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
    public TaskModel getTask(Integer taskId) {
        Task task = taskJpaRepository.getOne(taskId);

        return (TaskModel) conversionService.convert(
            task,
            TypeDescriptor.valueOf(Task.class),
            TypeDescriptor.valueOf(TaskModel.class));
    }

    @Override
    public Task createTask(CreateTaskRequest request) {

        return runningListCommandManager.execute(new RunningListAction<>() {

            private TaskRecord task;

            @Override
            public Task execute() {
                if (this.task == null) {

                    TaskRecord record = new TaskRecord();
                    record.setTitle(request.getTitle());
                    record.setDescription(request.getDescription());
                    record.setCreatedAt(OffsetDateTime.now());
                    record.setPriority(taskPriorityRepository.incrementAndGetMaxPriority());

                    this.task = record;

                    taskJooqRepository.saveAndGenerateIdIfNotPresent(record);
                    log.info("New task has been created {}", this.task);

                } else {

                    taskJooqRepository.saveAndGenerateIdIfNotPresent(this.task);
                    log.info("Undone operation of task creating was redone {}", this.task);
                }
                return conversionService.convert(this.task, Task.class);
            }

            @Override
            public void undo() {

                taskJpaRepository.deleteById(this.task.getId());
                log.info("Operation of task creating was undone {}", this.task);
            }
        });

    }

    @Override
    public void delete(Integer taskId) {

        runningListCommandManager.execute(new RunningListAction<Void>() {

            private TaskModel removedTask;

            private List<WeekWorkLog> workLogs;

            @Override
            public Void execute() {

                if (removedTask == null) {

                    removedTask = self.getTask(taskId);
                    workLogs = weekWorkLogJpaRepository.findAllByTaskId(taskId);

                    taskJpaRepository.deleteById(taskId);
                    weekWorkLogJpaRepository.deleteAll(workLogs);

                    log.info("Task has been removed {}", removedTask);
                } else {

                    taskJpaRepository.deleteById(removedTask.getId());
                    weekWorkLogJpaRepository.deleteAll(workLogs);
                    log.info("Undone operation of task removing was redone {}", removedTask);
                }
                return null;
            }

            @Override
            public void undo() {

                TaskRecord taskRecord = conversionService.convert(removedTask, TaskRecord.class);
                taskJooqRepository.saveAndGenerateIdIfNotPresent(taskRecord);

                workLogs = weekWorkLogJpaRepository.saveAll(workLogs);

                log.info("Operation of task removing was undone {}", removedTask);
            }
        });
    }

    @Override
    public void changeTaskStatus(ChangeTaskStatusRequest request) {


        Integer taskId = request.getTaskId();
        Integer dayIndex = request.getDayIndex();
        TaskStatus status = request.getStatus();

        WeekWorkLogPK id = WeekWorkLogPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<TaskStatus> previousStatus = getTaskDayStatus(taskId, dayIndex);

        runningListCommandManager.execute(new RunningListAction<Void>() {

            private WeekWorkLog workLog;
            @Override
            public Void execute() {

                if (Objects.isNull(workLog)) {


                    workLog = weekWorkLogJpaRepository.save(WeekWorkLog.builder()
                        .id(id)
                        .status(status)
                        .build());

                    log.info("Task status has been updated {}", workLog);

                } else {

                    workLog = weekWorkLogJpaRepository.save(WeekWorkLog.builder()
                        .id(id)
                        .status(status)
                        .build());

                    log.info("Undone operation of task status updating was redone {}", workLog);
                }
                return null;
            }

            @Override
            public void undo() {

                if (previousStatus.isPresent()) {

                    workLog = weekWorkLogJpaRepository.save(WeekWorkLog.builder()
                        .id(id)
                        .status(previousStatus.get())
                        .build());
                    log.info("Operation of task status updating was undone {}", previousStatus.get());

                } else {
                    weekWorkLogJpaRepository.deleteById(id);
                    log.info("Operation of task status updating was undone {}", id);
                }
            }
        });


    }

    @Transactional
    @Override
    public void update(UpdateTaskRequest request) {

        TaskModel previousState = self.getTask(request.getId());

        Task task = taskJpaRepository.getOne(request.getId());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModifiedAt(ZonedDateTime.now());

        runningListCommandManager.execute(new RunningListAction<Void>() {

            private TaskModel taskModel;

            @Override
            public Void execute() {

                if (taskModel == null) {

                    taskModel = previousState;

                    taskJpaRepository.save(task);

                    log.info("Task has been updated {}", task);

                } else {

                    taskJpaRepository.save(task);
                    log.info("Undone operation of task updating was redone {}", task);
                }

                return null;
            }

            @Override
            public void undo() {

                Task task = taskJpaRepository.getOne(taskModel.getId());
                task.setTitle(taskModel.getTitle());
                task.setDescription(taskModel.getDescription());
                task.setModifiedAt(taskModel.getModifiedAt());
                taskJpaRepository.save(task);
                log.info("Operation of task updating was undone {}", task);
            }
        });
    }

    @Override
    public void reorderTask(ChangeTaskPriorityRequest request) {

        List<Task> tasks = getAllTasks();

        Task moved = tasks.get(request.getStartIndex());

        tasks.remove(moved);
        tasks.add(request.getEndIndex(), moved);
        saveTaskList(tasks);
    }

    private void saveTaskList(List<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setPriority(i);
        }
        taskJpaRepository.saveAll(taskList);
    }
}

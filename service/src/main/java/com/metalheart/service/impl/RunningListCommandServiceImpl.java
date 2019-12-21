package com.metalheart.service.impl;

import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.RunningListAction;
import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.service.TaskModel;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.DeleteTaskRequest;
import com.metalheart.model.service.WeekWorkLogUpdateRequest;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TaskService;
import com.metalheart.service.WorkLogService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class RunningListCommandServiceImpl implements RunningListCommandService {

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private RunningListCommandManager runningListCommandManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RunningListArchiveService runningListArchiveService;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private ConversionService conversionService;

    @Override
    public TaskModel createTask(CreateTaskRequest request) {

        return runningListCommandManager.execute(new RunningListAction<>() {

            private TaskModel task;

            @Override
            public TaskModel execute() {
                task = taskService.create(request);
                log.info("New task has been created");

                return task;
            }

            @Override
            public void redo() {
                taskService.create(request);
                log.info("Undone operation of task creating was redone");
            }

            @Override
            public void undo() {
                taskService.delete(task);
                log.info("Operation of task creating was undone");
            }
        });
    }

    @Override
    public void changeTaskStatus(ChangeTaskStatusRequest request) {

        Integer taskId = request.getTaskId();
        Integer dayIndex = request.getDayIndex();
        TaskStatus status = request.getStatus();

        WeekWorkLogJpaPK id = WeekWorkLogJpaPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<TaskStatus> previousStatus = taskService.getTaskDayStatus(taskId, dayIndex);

        WeekWorkLogUpdateRequest updateRequest = WeekWorkLogUpdateRequest.builder()
            .taskId(taskId)
            .dayIndex(dayIndex)
            .status(status)
            .build();

        runningListCommandManager.execute(new RunningListAction<Void>() {

            @Override
            public Void execute() {
                workLogService.save(updateRequest);
                log.info("Task status has been updated");
                return null;
            }

            @Override
            public void redo() {
                workLogService.save(updateRequest);
                log.info("Undone operation of task status updating was redone");
            }

            @Override
            public void undo() {

                if (previousStatus.isPresent()) {
                    workLogService.save(WeekWorkLogUpdateRequest.builder()
                        .taskId(taskId)
                        .dayIndex(dayIndex)
                        .status(previousStatus.get())
                        .build());
                    log.info("Operation of task status updating was undone");

                } else {
                    weekWorkLogJpaRepository.deleteById(id);
                    log.info("Operation of task status updating was undone");
                }
            }
        });
    }

    @Override
    public void delete(Integer taskId) {

        List<WeekWorkLog> workLogs = weekWorkLogJpaRepository.findAllByTaskId(taskId)
            .stream()
            .map(weekWorkLogJpa -> conversionService.convert(weekWorkLogJpa, WeekWorkLog.class))
            .collect(toList());

        DeleteTaskRequest deleteRequest = DeleteTaskRequest.builder()
            .task(taskService.getTask(taskId))
            .workLogs(workLogs)
            .build();

        runningListCommandManager.execute(new RunningListAction<Void>() {

            @Override
            public Void execute() {
                taskService.deleteTaskWithWorklog(deleteRequest);
                log.info("Task has been removed ");
                return null;
            }

            @Override
            public void redo() {
                taskService.deleteTaskWithWorklog(deleteRequest);
                log.info("Undone operation of task removing was redone");
            }

            @Override
            public void undo() {
                taskService.undoRemoving(deleteRequest);
                log.info("Operation of task removing was undone");
            }
        });
    }

    @Override
    public void update(UpdateTaskRequest request) {

        TaskModel previousState = taskService.getTask(request.getId());

        TaskModel task = previousState.clone();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModifiedAt(ZonedDateTime.now());
        task.setTags(request.getTags());

        runningListCommandManager.execute(new RunningListAction<Void>() {

            @Override
            public Void execute() {
                taskService.save(task);
                log.info("Task has been updated");
                return null;
            }

            @Override
            public void redo() {
                taskService.save(task);
                log.info("Undone operation of task updating was redone");
            }

            @Override
            public void undo() {
                taskService.save(previousState);
                log.info("Operation of task updating was undone");
            }
        });
    }

    @Override
    public void archive(WeekId weekId) throws RunningListArchiveAlreadyExistException {

        if (runningListArchiveService.isArchiveExist(weekId)) {
            throw new RunningListArchiveAlreadyExistException(weekId);
        }

        RunningListViewModel runningList = runningListService.getRunningList();

        RunningListArchiveJpa archiveToSave = RunningListArchiveJpa.builder()
            .id(conversionService.convert(weekId, RunningListArchiveJpaPK.class))
            .archive(conversionService.convert(runningList, String.class))
            .build();

        runningListCommandManager.execute(new RunningListAction<Void>() {

            private RunningListArchiveJpa archive;

            @Override
            public Void execute() {
                this.archive = runningListArchiveService.save(archiveToSave);
                log.info("Archive has been saved");
                return null;
            }

            @Override
            public void redo() {
                this.archive = runningListArchiveService.save(archiveToSave);
                log.info("Undone operation of archive saving was redone");
            }

            @Override
            public void undo() {
                runningListArchiveService.delete(this.archive);
                log.info("Operation of archive saving was undone");
            }
        });
    }

    @Override
    public void reorderTask(ChangeTaskPriorityRequest request) {

        List<TaskModel> tasks = taskService.getAllTasks();
        List<TaskModel> previousTaskOrder = tasks.stream()
            .map(TaskModel::clone)
            .collect(toList());


        List<Integer> previousPriorities = tasks.stream()
            .map(TaskModel::getPriority)
            .collect(toList());

        TaskModel moved = tasks.get(request.getStartIndex());
        tasks.remove(moved);
        tasks.add(request.getEndIndex(), moved);

        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPriority(previousPriorities.get(i));
        }

        runningListCommandManager.execute(new RunningListAction<Void>() {

            @Override
            public Void execute() {
                taskService.save(tasks);
                log.info("Tasks have been reordered");
                return null;
            }

            @Override
            public void redo() {
                taskService.save(tasks);
                log.info("Undone operation of tasks reordering was redone");
            }

            @Override
            public void undo() {
                taskService.save(previousTaskOrder);
                log.info("Operation of tasks reordering was undone");
            }
        });
    }
}
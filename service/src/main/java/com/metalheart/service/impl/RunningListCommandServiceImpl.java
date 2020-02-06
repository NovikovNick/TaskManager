package com.metalheart.service.impl;

import com.metalheart.model.RunningList;
import com.metalheart.model.RunningListAction;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.model.request.WeekWorkLogUpdateRequest;
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

    @Override
    public Task createTask(Integer userId, Task request) {

        return runningListCommandManager.execute(userId, new RunningListAction<>() {

            private Task task;

            @Override
            public Task execute() {
                task = taskService.create(request);
                log.info("New task has been created");

                return task;
            }

            @Override
            public void redo() {
                taskService.undoRemoving(task.getId());
                log.info("Undone operation of task creating was redone");
            }

            @Override
            public void undo() {
                taskService.delete(task.getId());
                log.info("Operation of task creating was undone");
            }
        });
    }

    @Override
    public void changeTaskStatus(Integer userId, Integer taskId, Integer dayIndex, TaskStatus status) {

        WeekWorkLogJpaPK id = WeekWorkLogJpaPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<TaskStatus> previousStatus = taskService.getTaskDayStatus(taskId, dayIndex);

        WeekWorkLogUpdateRequest updateRequest = WeekWorkLogUpdateRequest.builder()
            .taskId(taskId)
            .dayIndex(dayIndex)
            .status(status)
            .build();

        runningListCommandManager.execute(userId, new RunningListAction<Void>() {

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
    public void delete(Integer userId, Integer taskId) {

        runningListCommandManager.execute(userId, new RunningListAction<Void>() {

            @Override
            public Void execute() {
                taskService.delete(taskId);
                log.info("Task has been removed ");
                return null;
            }

            @Override
            public void redo() {
                taskService.delete(taskId);
                log.info("Undone operation of task removing was redone");
            }

            @Override
            public void undo() {
                taskService.undoRemoving(taskId);
                log.info("Operation of task removing was undone");
            }
        });
    }

    @Override
    public void update(Integer userId, Task request) {

        Task previousState = taskService.getTask(request.getId());

        Task task = previousState.clone();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModifiedAt(ZonedDateTime.now());
        task.setTags(request.getTags());

        runningListCommandManager.execute(userId, new RunningListAction<Void>() {

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
    public void archive(Integer userId, WeekId weekId) {

        RunningList newRunningList = runningListService.getRunningList(userId);
        newRunningList.setWeekId(weekId);

        Optional<RunningList> oldRunningList = runningListArchiveService.getArchive(userId, weekId);

        runningListCommandManager.execute(userId, new RunningListAction<Void>() {

            @Override
            public Void execute() {
                runningListArchiveService.save(userId, newRunningList);
                log.info("Archive has been saved");
                return null;
            }

            @Override
            public void redo() {
                runningListArchiveService.save(userId, newRunningList);
                log.info("Undone operation of archive saving was redone");
            }

            @Override
            public void undo() {

                if (oldRunningList.isPresent()) {

                    runningListArchiveService.save(userId, oldRunningList.get());

                } else {

                    runningListArchiveService.delete(userId, newRunningList.getWeekId());
                }

                log.info("Operation of archive saving was undone");
            }
        });
    }

    @Override
    public void reorderTask(Integer userId, Integer startIndex, Integer endIndex) {

        List<Task> tasks = taskService.getTasks(userId);
        List<Task> previousTaskOrder = tasks.stream()
            .map(Task::clone)
            .collect(toList());


        List<Integer> previousPriorities = tasks.stream()
            .map(Task::getPriority)
            .collect(toList());

        Task moved = tasks.get(startIndex);
        tasks.remove(moved);
        tasks.add(endIndex, moved);

        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPriority(previousPriorities.get(i));
        }

        runningListCommandManager.execute(userId, new RunningListAction<Void>() {

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

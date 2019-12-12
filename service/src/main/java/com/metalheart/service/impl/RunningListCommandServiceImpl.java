package com.metalheart.service.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.model.WeekWorkLogUpdateRequest;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TaskService;
import com.metalheart.service.WorkLogService;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public Task createTask(CreateTaskRequest request) {

        return runningListCommandManager.execute(new RunningListAction<>() {

            private Task task;

            @Override
            public Task execute() {

                String info;
                if (Objects.isNull(this.task)) {

                    info = "New task has been created";

                } else {
                    request.setTaskId(this.task.getId());
                    info = "Undone operation of task creating was redone";
                }

                Task created = taskService.create(request);

                log.info(info);

                return this.task = created;
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

        WeekWorkLogPK id = WeekWorkLogPK.builder().taskId(taskId).dayIndex(dayIndex).build();

        Optional<TaskStatus> previousStatus = taskService.getTaskDayStatus(taskId, dayIndex);

        runningListCommandManager.execute(new RunningListAction<Void>() {

            private WeekWorkLog workLog;

            @Override
            public Void execute() {

                WeekWorkLog updated = workLogService.save(WeekWorkLogUpdateRequest.builder()
                    .taskId(taskId)
                    .dayIndex(dayIndex)
                    .status(status)
                    .build());

                if (Objects.isNull(workLog)) {
                    log.info("Task status has been updated");
                } else {
                    log.info("Undone operation of task status updating was redone");
                }

                workLog = updated;
                return null;
            }

            @Override
            public void undo() {

                if (previousStatus.isPresent()) {

                    workLog = workLogService.save(WeekWorkLogUpdateRequest.builder()
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
}

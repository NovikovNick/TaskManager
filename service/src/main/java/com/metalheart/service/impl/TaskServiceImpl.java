package com.metalheart.service.impl;

import com.metalheart.model.RunningListAction;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.jpa.TaskStatus;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import com.metalheart.repository.inmemory.ITaskPriorityRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.TaskService;
import java.time.ZonedDateTime;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Autowired
    private RunningListCommandManager runningListCommandManager;

    @Autowired
    private ITaskPriorityRepository taskPriorityRepository;


    @PostConstruct
    public void reorder() {

        List<Task> taskList = taskJpaRepository.findAllByOrderByPriorityAsc();
        int maxPriority = taskList.size();
        taskPriorityRepository.setMaxPriority(maxPriority);

        saveTaskList(taskList);
    }

    @Override
    public void createTask(CreateTaskRequest request) {
        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(taskPriorityRepository.incrementAndGetMaxPriority())
            .createdAt(ZonedDateTime.now())
            .build();
        taskJpaRepository.save(task);
    }

    @Override
    public void delete(Integer taskId) {

        runningListCommandManager.execute(new RunningListAction() {

            private Task task;
            private List<WeekWorkLog> workLogs;

            @Override
            public void execute() {

                if (task == null) {
                    task = taskJpaRepository.getOne(taskId);
                    workLogs = weekWorkLogJpaRepository.findAllByTaskId(taskId);
                }
                taskJpaRepository.delete(task);
                weekWorkLogJpaRepository.deleteAll(workLogs);
            }

            @Override
            public void undo() {
                task = taskJpaRepository.save(task);

                workLogs.forEach(workLog -> workLog.getId().setTaskId(task.getId()));
                workLogs = weekWorkLogJpaRepository.saveAll(workLogs);
            }
        });
    }

    @Override
    public void changeTaskStatus(ChangeTaskStatusRequest request) {

        Integer taskId = request.getTaskId();
        Integer dayIndex = request.getDayIndex();
        TaskStatus status = request.getStatus();

        weekWorkLogJpaRepository.save(WeekWorkLog.builder()
            .id(WeekWorkLogPK.builder().taskId(taskId).dayIndex(dayIndex).build())
            .status(status)
            .build());
    }

    @Override
    public void update(UpdateTaskRequest request) {
        Task task = taskJpaRepository.getOne(request.getId());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModifiedAt(ZonedDateTime.now());
        taskJpaRepository.save(task);
    }

    @Override
    public void reorderTask(ChangeTaskPriorityRequest request) {

        List<Task> tasks = taskJpaRepository.findAllByOrderByPriorityAsc();

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

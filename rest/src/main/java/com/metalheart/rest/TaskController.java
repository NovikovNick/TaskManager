package com.metalheart.rest;

import com.metalheart.EndPoint;
import com.metalheart.model.ChangeTaskPriorityRequest;
import com.metalheart.model.ChangeTaskStatusRequest;
import com.metalheart.model.CreateTaskRequest;
import com.metalheart.model.TaskViewModel;
import com.metalheart.model.UpdateTaskRequest;
import com.metalheart.service.TaskService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(path = EndPoint.GET_TASK_LIST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskViewModel>> getTaskList() {

        return ResponseEntity.ok(taskService.getTaskList());
    }

    @PostMapping(path = EndPoint.CREATE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createTask(@Valid @RequestBody CreateTaskRequest request) {

        taskService.createTask(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("x", "created"));
    }

    @PostMapping(path = EndPoint.CHANGE_TASK_STATUS, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    public ResponseEntity changeStatus(@Valid @RequestBody ChangeTaskStatusRequest request) {

        taskService.changeTaskStatus(request);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("x", "updated"));
    }

    @PutMapping(
        path = EndPoint.CHANGE_TASK_PRIORITY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskViewModel>> reorderTask(@Valid @RequestBody ChangeTaskPriorityRequest request) {

        taskService.reorderTask(request);

        return ResponseEntity.ok(taskService.getTaskList());
    }


    @DeleteMapping(path = EndPoint.DELETE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("taskId") Integer taskId) {

        taskService.delete(taskId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("x", "removed"));
    }

    @PutMapping(
        path = EndPoint.UPDATE_TASK,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateTask(@Valid @RequestBody UpdateTaskRequest request) {

        taskService.update(request);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("x", "updated"));
    }
}

package com.metalheart.rest;

import com.metalheart.EndPoint;
import com.metalheart.model.rest.request.AddTagToTaskRequest;
import com.metalheart.model.rest.request.ChangeTaskPriorityRequest;
import com.metalheart.model.rest.request.ChangeTaskStatusRequest;
import com.metalheart.model.rest.request.CreateTaskRequest;
import com.metalheart.model.rest.request.RemoveTagFromTaskRequest;
import com.metalheart.model.rest.request.UpdateTaskRequest;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.model.rest.response.TagViewModel;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import com.metalheart.service.TaskService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private TagService tagService;


    @PostMapping(path = EndPoint.CREATE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel createTask(@Valid @RequestBody CreateTaskRequest request) {

        runningListCommandService.createTask(request);

        return runningListService.getRunningList();
    }

    @PostMapping(path = EndPoint.CHANGE_TASK_STATUS, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    public RunningListViewModel changeStatus(@Valid @RequestBody ChangeTaskStatusRequest request) {

        runningListCommandService.changeTaskStatus(request);

        return runningListService.getRunningList();
    }

    @PutMapping(
        path = EndPoint.CHANGE_TASK_PRIORITY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel reorderTask(@Valid @RequestBody ChangeTaskPriorityRequest request) {

        taskService.reorderTask(request);

        return runningListService.getRunningList();
    }


    @DeleteMapping(path = EndPoint.DELETE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel delete(@PathVariable("taskId") Integer taskId) {

        runningListCommandService.delete(taskId);

        return runningListService.getRunningList();
    }

    @PutMapping(
        path = EndPoint.UPDATE_TASK,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel updateTask(@Valid @RequestBody UpdateTaskRequest request) {

        taskService.update(request);

        return runningListService.getRunningList();
    }

    @PostMapping(
        path = EndPoint.ADD_TAG_TO_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add tag to task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> addTaskTag(AddTagToTaskRequest request) {

        tagService.addTagToTask(request.getTag(), request.getTaskId());
        return ResponseEntity.ok(runningListService.getRunningList());
    }

    @DeleteMapping(
        path = EndPoint.REMOVE_TAG_FROM_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> removeTaskTag(RemoveTagFromTaskRequest request) {

        tagService.removeTagFromTask(request.getTag(), request.getTaskId());
        return ResponseEntity.ok(runningListService.getRunningList());
    }

    @GetMapping(
        path = EndPoint.GET_TASK_TAG_LIST,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = TagViewModel.class, responseContainer = "List")
    public List<TagViewModel> getTags() {

        return tagService.getAllTags();
    }
}

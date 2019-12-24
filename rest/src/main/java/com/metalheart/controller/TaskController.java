package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.request.AddTagToTaskRequest;
import com.metalheart.model.request.ChangeTaskPriorityRequest;
import com.metalheart.model.request.ChangeTaskStatusRequest;
import com.metalheart.model.request.CreateTaskRequest;
import com.metalheart.model.request.RemoveTagFromTaskRequest;
import com.metalheart.model.request.UpdateTaskRequest;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.TagViewModel;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping(path = EndPoint.CREATE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel createTask(@Valid @RequestBody CreateTaskRequest request) {

        runningListCommandService.createTask(conversionService.convert(request, Task.class));

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }

    @PostMapping(path = EndPoint.CHANGE_TASK_STATUS, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    public RunningListViewModel changeStatus(@Valid @RequestBody ChangeTaskStatusRequest request) {

        runningListCommandService.changeTaskStatus(request.getTaskId(), request.getDayIndex(), request.getStatus());

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }

    @PutMapping(
        path = EndPoint.CHANGE_TASK_PRIORITY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel reorderTask(@Valid @RequestBody ChangeTaskPriorityRequest request) {

        runningListCommandService.reorderTask(request.getStartIndex(), request.getEndIndex());

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }


    @DeleteMapping(path = EndPoint.DELETE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel delete(@PathVariable("taskId") Integer taskId) {

        runningListCommandService.delete(taskId);

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }

    @PutMapping(
        path = EndPoint.UPDATE_TASK,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel updateTask(@Valid @RequestBody UpdateTaskRequest request) {


        runningListCommandService.update(conversionService.convert(request, Task.class));

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }

    @PostMapping(
        path = EndPoint.ADD_TAG_TO_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add tag to task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> addTaskTag(AddTagToTaskRequest request) {

        tagService.addTagToTask(request.getTag(), request.getTaskId());
        RunningList runningList = runningListService.getRunningList();
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }

    @DeleteMapping(
        path = EndPoint.REMOVE_TAG_FROM_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> removeTaskTag(RemoveTagFromTaskRequest request) {

        tagService.removeTagFromTask(request.getTag(), request.getTaskId());
        RunningList runningList = runningListService.getRunningList();
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }

    @GetMapping(
        path = EndPoint.GET_TASK_TAG_LIST,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = TagViewModel.class, responseContainer = "List")
    public List<TagViewModel> getTags() {

        return tagService.getAllTags().stream()
            .map(tag -> conversionService.convert(tag, TagViewModel.class))
            .collect(Collectors.toList());
    }
}

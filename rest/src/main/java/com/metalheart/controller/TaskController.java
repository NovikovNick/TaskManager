package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.model.RunningList;
import com.metalheart.model.Task;
import com.metalheart.model.User;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;
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
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @PostMapping(path = EndPoint.CREATE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel createTask(@AuthenticationPrincipal User user,
                                           @Valid @RequestBody CreateTaskRequest request) {

        Task task = conversionService.convert(request, Task.class);
        task.setUserId(user.getId());
        runningListCommandService.createTask(task);

        return conversionService.convert(runningListService.getRunningList(user.getId()), RunningListViewModel.class);
    }

    @PostMapping(path = EndPoint.CHANGE_TASK_STATUS, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    public RunningListViewModel changeStatus(@AuthenticationPrincipal User user,
                                             @Valid @RequestBody ChangeTaskStatusRequest request) {

        runningListCommandService.changeTaskStatus(request.getTaskId(), request.getDayIndex(), request.getStatus());

        return conversionService.convert(runningListService.getRunningList(user.getId()), RunningListViewModel.class);
    }

    @PutMapping(
        path = EndPoint.CHANGE_TASK_PRIORITY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel reorderTask(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody ChangeTaskPriorityRequest request) {

        runningListCommandService.reorderTask(user.getId(), request.getStartIndex(), request.getEndIndex());

        return conversionService.convert(runningListService.getRunningList(user.getId()), RunningListViewModel.class);
    }


    @DeleteMapping(path = EndPoint.DELETE_TASK, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel delete(@AuthenticationPrincipal User user, @PathVariable("taskId") Integer taskId) {

        runningListCommandService.delete(taskId);

        return conversionService.convert(runningListService.getRunningList(user.getId()), RunningListViewModel.class);
    }

    @PutMapping(
        path = EndPoint.UPDATE_TASK,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public RunningListViewModel updateTask(@AuthenticationPrincipal User user,
                                           @Valid @RequestBody UpdateTaskRequest request) {


        runningListCommandService.update(conversionService.convert(request, Task.class));

        return conversionService.convert(runningListService.getRunningList(user.getId()), RunningListViewModel.class);
    }

    @PostMapping(
        path = EndPoint.ADD_TAG_TO_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add tag to task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> addTaskTag(@AuthenticationPrincipal User user,
                                                           AddTagToTaskRequest request) {

        tagService.addTagToTask(request.getTag(), request.getTaskId());
        RunningList runningList = runningListService.getRunningList(user.getId());
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }

    @DeleteMapping(
        path = EndPoint.REMOVE_TAG_FROM_TASK,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> removeTaskTag(@AuthenticationPrincipal User user,
                                                              RemoveTagFromTaskRequest request) {

        tagService.removeTagFromTask(request.getTag(), request.getTaskId());
        RunningList runningList = runningListService.getRunningList(user.getId());
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }

    @GetMapping(
        path = EndPoint.GET_TASK_TAG_LIST,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag from task", response = TagViewModel.class, responseContainer = "List")
    public List<TagViewModel> getTags(@AuthenticationPrincipal User user) {

        return tagService.getTags(user.getId()).stream()
            .map(tag -> conversionService.convert(tag, TagViewModel.class))
            .collect(Collectors.toList());
    }
}

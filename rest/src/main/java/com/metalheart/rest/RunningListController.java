package com.metalheart.rest;

import com.metalheart.EndPoint;
import com.metalheart.RestConstants;
import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import com.metalheart.model.request.CRUDTagRequest;
import com.metalheart.model.request.GetArchiveRequest;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@Api(tags = "Running List")
public class RunningListController {

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    private DateService dateService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private TagService tagService;

    @GetMapping(path = EndPoint.RUNNING_LIST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get running list for current week", response = RunningListViewModel.class)
    public RunningListViewModel getTaskList() {

        return conversionService.convert(runningListService.getRunningList(), RunningListViewModel.class);
    }

    @PostMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Archive current week tasks", response = RunningListViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = RestConstants.HTTP_UNPROCESSABLE_ENTITY,
            message = "If running list archive has already exist")
    })
    public ResponseEntity<RunningListViewModel> archive() {

        try {
            WeekId weekId = dateService.getCurrentWeekId();
            runningListCommandService.archive(weekId);
            RunningList runningList = runningListService.getRunningList();
            RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
            return ResponseEntity.ok(viewModel);
        } catch (RunningListArchiveAlreadyExistException e) {
            log.warn(e.getMessage(), e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE_NEXT,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get archive for next week", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> getNextArchive(@Valid GetArchiveRequest request) {

        try {
            WeekId weekId = conversionService.convert(request, WeekId.class);
            RunningList runningList = archiveService.getNext(weekId);
            RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
            return ResponseEntity.ok(viewModel);
        } catch (NoSuchRunningListArchiveException e) {
            log.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE_PREV,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get archive for previous week", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> getPrevArchive(@Valid GetArchiveRequest request) {

        try {
            WeekId weekId = conversionService.convert(request, WeekId.class);
            RunningList runningList = archiveService.getPrev(weekId);
            RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
            return ResponseEntity.ok(viewModel);
        } catch (NoSuchRunningListArchiveException e) {
            log.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(
        path = EndPoint.RUNNING_LIST_UNDO,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Undo last operation", response = RunningListViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = RestConstants.HTTP_NOT_ACCEPTABLE,
            message = "If there are no previous operations to undo")
    })
    public ResponseEntity<RunningListViewModel> undo() {

        try {
            commandManager.undo();
            RunningList runningList = runningListService.getRunningList();
            RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
            return ResponseEntity.ok(viewModel);
        } catch (UnableToUndoException e) {
            log.warn(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping(
        path = EndPoint.RUNNING_LIST_REDO,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Redo undone operation", response = RunningListViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = RestConstants.HTTP_NOT_ACCEPTABLE,
            message = "If there are no undone operations to redo")
    })
    public ResponseEntity<RunningListViewModel> redo() {

        try {
            commandManager.redo();
            RunningList runningList = runningListService.getRunningList();
            RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
            return ResponseEntity.ok(viewModel);
        } catch (UnableToRedoException e) {
            log.warn(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PutMapping(
        path = EndPoint.ADD_TASK_TAG,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add tag", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> addTaskTag(@Valid @RequestBody CRUDTagRequest tag) {

        tagService.selectTag(tag.getTag());
        RunningList runningList = runningListService.getRunningList();
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }

    @DeleteMapping(
        path = EndPoint.REMOVE_TASK_TAG,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag", response = RunningListViewModel.class)
    public ResponseEntity<RunningListViewModel> removeTaskTag(@Valid @RequestBody CRUDTagRequest tag) {

        tagService.removeSelectedTag(tag.getTag());
        RunningList runningList = runningListService.getRunningList();
        RunningListViewModel viewModel = conversionService.convert(runningList, RunningListViewModel.class);
        return ResponseEntity.ok(viewModel);
    }
}

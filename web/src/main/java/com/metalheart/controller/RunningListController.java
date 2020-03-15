package com.metalheart.controller;

import com.metalheart.EndPoint;
import com.metalheart.HTTPConstants;
import com.metalheart.exception.UnableToRedoException;
import com.metalheart.exception.UnableToUndoException;
import com.metalheart.model.RunningList;
import com.metalheart.model.User;
import com.metalheart.model.WeekId;
import com.metalheart.model.request.ArchiveRequest;
import com.metalheart.model.request.CRUDTagRequest;
import com.metalheart.model.request.GetArchiveRequest;
import com.metalheart.model.response.Response;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListCommandManager;
import com.metalheart.service.RunningListCommandService;
import com.metalheart.service.TagService;
import com.metalheart.service.WebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.metalheart.HTTPConstants.HEADER_TIMEZONE_OFFSET;
import static com.metalheart.HTTPConstants.MSG_OPERATION_ARCHIVE;
import static com.metalheart.HTTPConstants.MSG_OPERATION_REDONE;
import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@Api(tags = "Running List")
public class RunningListController {

    @Autowired
    private RunningListCommandManager commandManager;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Autowired
    private RunningListCommandService runningListCommandService;

    @Autowired
    private TagService tagService;

    @Autowired
    private WebService webService;


    @GetMapping(path = EndPoint.RUNNING_LIST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get running list for current week", response = RunningListViewModel.class)
    public ResponseEntity<Response> getTaskList(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                            @AuthenticationPrincipal User user) {

        return webService.getResponseBuilder()
            .runningList(user.getId(), timezoneOffset)
            .build();
    }

    @GetMapping(path = EndPoint.RUNNING_LIST_ARCHIVE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get running list for current week", response = RunningListViewModel.class)
    public ResponseEntity<Response> getExistingArchivesWeekIds(@AuthenticationPrincipal User user) {

        return webService.getResponseBuilder()
            .archives(user.getId())
            .build();
    }

    @PostMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Archive current week tasks", response = RunningListViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = HTTPConstants.HTTP_UNPROCESSABLE_ENTITY,
            message = "If running list archive has already exist")
    })
    public ResponseEntity<Response> archive(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                            @AuthenticationPrincipal User user,
                                            @Valid @RequestBody ArchiveRequest request,
                                            HttpServletRequest httpRequest) {
        try {

            runningListCommandService.archive(user.getId(), conversionService.convert(request, WeekId.class));

            return webService.getResponseBuilder()
                .message(MSG_OPERATION_ARCHIVE, httpRequest.getLocale())
                .archives(user.getId())
                .build();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE_NEXT,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get archive for next week", response = RunningListViewModel.class)
    public ResponseEntity<Response> getNextArchive(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                               @AuthenticationPrincipal User user,
                                                               @Valid GetArchiveRequest request) {

        WeekId weekId = conversionService.convert(request, WeekId.class);
        Optional<RunningList> runningList = archiveService.getNext(user.getId(), weekId, timezoneOffset);

        if (runningList.isPresent()) {
            return webService.getResponseBuilder()
                .runningList(runningList.get())
                .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(
        path = EndPoint.RUNNING_LIST_ARCHIVE_PREV,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get archive for previous week", response = RunningListViewModel.class)
    public ResponseEntity<Response> getPrevArchive(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                               @AuthenticationPrincipal User user,
                                                               @Valid GetArchiveRequest request) {

        WeekId weekId = conversionService.convert(request, WeekId.class);
        Optional<RunningList> runningList = archiveService.getPrev(user.getId(), weekId, timezoneOffset);

        if (runningList.isPresent()) {
            return webService.getResponseBuilder()
                .runningList(runningList.get())
                .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(
        path = EndPoint.RUNNING_LIST_UNDO,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Undo last operation", response = RunningListViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = HTTPConstants.HTTP_NOT_ACCEPTABLE,
            message = "If there are no previous operations to undo")
    })
    public ResponseEntity<Response> undo(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                         @AuthenticationPrincipal User user,
                                         HttpServletRequest httpRequest) {

        try {

            commandManager.undo(user.getId());

            return webService.getResponseBuilder()
                .message(HTTPConstants.MSG_OPERATION_UNDONE, httpRequest.getLocale())
                .user(user.getId())
                .runningList(user.getId(), timezoneOffset)
                .archives(user.getId())
                .build();

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
            code = HTTPConstants.HTTP_NOT_ACCEPTABLE,
            message = "If there are no undone operations to redo")
    })
    public ResponseEntity<Response> redo(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                         @AuthenticationPrincipal User user,
                                         HttpServletRequest httpRequest) {

        try {

            commandManager.redo(user.getId());

            return webService.getResponseBuilder()
                .message(MSG_OPERATION_REDONE, httpRequest.getLocale())
                .user(user.getId())
                .runningList(user.getId(), timezoneOffset)
                .archives(user.getId())
                .build();

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
    public ResponseEntity<Response> addTaskTag(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                               @AuthenticationPrincipal User user,
                                               @Valid @RequestBody CRUDTagRequest tag) {

        tagService.selectTag(user.getId(), tag.getTag());

        return webService.getResponseBuilder()
            .runningList(user.getId(), timezoneOffset)
            .build();
    }

    @DeleteMapping(
        path = EndPoint.REMOVE_TASK_TAG,
        produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove tag", response = RunningListViewModel.class)
    public ResponseEntity<Response> removeTaskTag(@RequestHeader(HEADER_TIMEZONE_OFFSET) Integer timezoneOffset,
                                                  @AuthenticationPrincipal User user,
                                                  @Valid @RequestBody CRUDTagRequest tag) {

        tagService.removeSelectedTag(user.getId(), tag.getTag());

        return webService.getResponseBuilder()
            .runningList(user.getId(), timezoneOffset)
            .build();
    }
}

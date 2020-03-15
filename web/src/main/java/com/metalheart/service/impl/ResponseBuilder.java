package com.metalheart.service.impl;

import com.metalheart.model.RunningList;
import com.metalheart.model.User;
import com.metalheart.model.WeekId;
import com.metalheart.model.response.Message;
import com.metalheart.model.response.Response;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.UserViewModel;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.UserService;
import java.util.List;
import java.util.Locale;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private final RunningListService runningListService;

    private final RunningListArchiveService archiveService;

    private final UserService userService;

    private final ConversionService conversionService;

    private MessageSourceAccessor messageSourceAccessor;

    private Response.ResponseBuilder builder;

    private HttpStatus status;

    public ResponseBuilder(RunningListService runningListService,
                           RunningListArchiveService archiveService,
                           ConversionService conversionService,
                           UserService userService,
                           MessageSourceAccessor messageSourceAccessor) {

        this.runningListService = runningListService;
        this.conversionService = conversionService;
        this.archiveService = archiveService;
        this.userService = userService;
        this.messageSourceAccessor = messageSourceAccessor;

        this.builder = Response.builder();
        this.status = HttpStatus.OK;
    }

    public ResponseBuilder user(Integer userId) {
        User fetchedUser = userService.get(userId);
        builder.user(conversionService.convert(fetchedUser, UserViewModel.class));
        return this;
    }

    public ResponseBuilder user(User user) {
        builder.user(conversionService.convert(user, UserViewModel.class));
        return this;
    }

    public ResponseBuilder runningList(Integer userId, Integer timezoneOffset) {
        RunningList fetchedRunningList = runningListService.getRunningList(userId, timezoneOffset);
        builder.runningList(conversionService.convert(fetchedRunningList, RunningListViewModel.class));
        return this;
    }

    public ResponseBuilder runningList(RunningList runningList) {
        builder.runningList(conversionService.convert(runningList, RunningListViewModel.class));
        return this;
    }

    public ResponseBuilder archives(Integer userId) {
        List<WeekId> fetchedArchives = archiveService.getExistingArchivesWeekIds(userId);
        builder.archives(fetchedArchives);
        return this;
    }

    public ResponseBuilder message(String msgCode, Locale locale) {
        builder.message(new Message(messageSourceAccessor.getMessage(msgCode, locale)));
        return this;
    }

    public ResponseBuilder error(String msgCode, Locale locale) {
        Message message = new Message(messageSourceAccessor.getMessage(msgCode, locale));
        message.setType(Message.Type.ERROR);
        builder.message(message);
        return this;
    }

    public ResponseBuilder status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ResponseEntity<Response> build() {
        return ResponseEntity.status(status).body(builder.build());
    }
}
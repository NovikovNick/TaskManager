package com.metalheart.service.impl;

import com.metalheart.model.RunningList;
import com.metalheart.model.User;
import com.metalheart.model.WeekId;
import com.metalheart.model.response.RunningListDataViewModel;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.UserViewModel;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.UserService;
import com.metalheart.service.WebService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Component
public class WebServiceImpl implements WebService {

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private RunningListArchiveService archiveService;

    @Autowired
    private UserService userService;


    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Override
    public RunningListDataViewModel geRunningListDataViewModel(User user, Integer timezoneOffset) {

        Integer userId = user.getId();

        RunningList fetchedRunningList = runningListService.getRunningList(userId, timezoneOffset);
        User fetchedUser = userService.get(userId);
        List<WeekId> fetchedArchives = archiveService.getExistingArchivesWeekIds(userId);

        RunningListDataViewModel res = RunningListDataViewModel.builder()
            .user(conversionService.convert(fetchedUser, UserViewModel.class))
            .runningList(conversionService.convert(fetchedRunningList, RunningListViewModel.class))
            .archives(fetchedArchives)
            .build();
        return res;
    }
}

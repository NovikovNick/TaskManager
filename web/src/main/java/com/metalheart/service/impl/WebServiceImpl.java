package com.metalheart.service.impl;

import com.metalheart.model.RunningList;
import com.metalheart.model.User;
import com.metalheart.model.response.RunningListDataViewModel;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.UserViewModel;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.WebService;
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
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Override
    public RunningListDataViewModel geRunningListDataViewModel(User user, Integer timezoneOffset) {

        RunningList runningList = runningListService.getRunningList(user.getId(), timezoneOffset);

        RunningListDataViewModel res = RunningListDataViewModel.builder()
            .user(conversionService.convert(user, UserViewModel.class))
            .runningList(conversionService.convert(runningList, RunningListViewModel.class))
            .archives(archiveService.getExistingArchivesWeekIds(user.getId()))
            .build();
        return res;
    }
}

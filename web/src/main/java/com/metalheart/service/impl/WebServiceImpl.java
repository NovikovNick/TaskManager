package com.metalheart.service.impl;

import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import com.metalheart.service.UserService;
import com.metalheart.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
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
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Override
    public ResponseBuilder getResponseBuilder() {
        return new ResponseBuilder(runningListService,
            archiveService, conversionService, userService, messageSourceAccessor);
    }


}

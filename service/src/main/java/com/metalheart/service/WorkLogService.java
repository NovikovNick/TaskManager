package com.metalheart.service;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.WeekWorkLogUpdateRequest;
import com.metalheart.model.jpa.WeekWorkLog;

public interface WorkLogService {

    @LogOperationContext
    WeekWorkLog save(WeekWorkLogUpdateRequest request);
}

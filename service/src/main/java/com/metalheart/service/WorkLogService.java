package com.metalheart.service;

import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.service.WeekWorkLogUpdateRequest;

public interface WorkLogService {

    WeekWorkLog save(WeekWorkLogUpdateRequest request);
}

package com.metalheart.service;

import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.service.WeekWorkLogUpdateRequest;

public interface WorkLogService {

    WeekWorkLogJpa save(WeekWorkLogUpdateRequest request);
}

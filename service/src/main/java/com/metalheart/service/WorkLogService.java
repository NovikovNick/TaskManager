package com.metalheart.service;

import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.WeekWorkLogUpdateRequest;
import java.util.List;

public interface WorkLogService {

    WeekWorkLogJpa save(WeekWorkLogUpdateRequest request);

    List<WeekWorkLog> get(Integer taskId, Integer currentDay);
}

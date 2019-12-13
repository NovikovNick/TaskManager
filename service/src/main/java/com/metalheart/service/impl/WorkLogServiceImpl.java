package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.service.WeekWorkLogUpdateRequest;
import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.WorkLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkLogServiceImpl implements WorkLogService {

    @Autowired
    private WeekWorkLogJpaRepository weekWorkLogJpaRepository;

    @Override
    @LogOperationContext
    public WeekWorkLog save(WeekWorkLogUpdateRequest request) {


        WeekWorkLogPK id = WeekWorkLogPK.builder()
            .taskId(request.getTaskId())
            .dayIndex(request.getDayIndex())
            .build();

        return weekWorkLogJpaRepository.save(WeekWorkLog.builder()
            .id(id)
            .status(request.getStatus())
            .build());
    }
}

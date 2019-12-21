package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.service.WeekWorkLogUpdateRequest;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
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
    public WeekWorkLogJpa save(WeekWorkLogUpdateRequest request) {


        WeekWorkLogJpaPK id = WeekWorkLogJpaPK.builder()
            .taskId(request.getTaskId())
            .dayIndex(request.getDayIndex())
            .build();

        return weekWorkLogJpaRepository.save(WeekWorkLogJpa.builder()
            .id(id)
            .status(request.getStatus())
            .build());
    }
}

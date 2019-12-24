package com.metalheart.service.impl;

import com.metalheart.log.LogOperationContext;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import com.metalheart.model.WeekWorkLogUpdateRequest;
import com.metalheart.repository.jpa.WeekWorkLogJpaRepository;
import com.metalheart.service.WorkLogService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.metalheart.model.TaskStatus.CANCELED;
import static com.metalheart.model.TaskStatus.DELAYED;
import static com.metalheart.model.TaskStatus.DONE;
import static com.metalheart.model.TaskStatus.NONE;
import static com.metalheart.model.TaskStatus.TO_DO;

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

    @Override
    public List<WeekWorkLog> get(Integer taskId, Integer currentDay) {

        List<WeekWorkLogJpa> taskWorkLog = weekWorkLogJpaRepository.findAllByTaskId(taskId);

        List<WeekWorkLog> res = new ArrayList<>();

        TaskStatus previous = NONE;

        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            TaskStatus status = getStatus(taskWorkLog, dayIndex);

            if (NONE.equals(status) && previous.equals(NONE)) {

                status = NONE;

            } else if (NONE.equals(status) && previous.equals(DONE)) {

                status = DONE;

            } else if (DONE.equals(status)) {

                if (!DONE.equals(previous)) {
                    previous = DONE;
                }
                status = DONE;

            } else if (NONE.equals(status) && previous.equals(CANCELED)) {

                status = CANCELED;

            } else if (CANCELED.equals(status)) {

                if (!CANCELED.equals(previous)) {
                    previous = CANCELED;
                }
                status = CANCELED;

            } else if (TO_DO.equals(status) && currentDay > dayIndex) {

                status = DELAYED;
            }

            res.add(WeekWorkLog.builder()
                .taskId(taskId)
                .dayIndex(dayIndex)
                .status(status)
                .build());
        }
        return res;
    }

    private TaskStatus getStatus(List<WeekWorkLogJpa> taskWorkLog, int day) {
        for (WeekWorkLogJpa log : taskWorkLog) {
            if (log.getId().getDayIndex() == day) {
                return log.getStatus();
            }
        }
        return NONE;
    }
}

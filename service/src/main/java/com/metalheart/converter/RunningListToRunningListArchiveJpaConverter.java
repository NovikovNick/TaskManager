package com.metalheart.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.converter.mapper.RunningListArchivePKToWeekIdMapper;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListToRunningListArchiveJpaConverter implements Converter<RunningList, RunningListArchiveJpa> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public RunningListArchiveJpa convert(RunningList src) {

        WeekId weekId = src.getWeekId();
        RunningListArchiveJpa dst = RunningListArchiveJpa.builder()
            .id(RunningListArchivePKToWeekIdMapper.INSTANCE.convert(weekId))
            .archive(toJson(src))
            .build();
        return dst;
    }

    private String toJson(RunningList runningList) {
        try {
            return objectMapper.writeValueAsString(runningList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

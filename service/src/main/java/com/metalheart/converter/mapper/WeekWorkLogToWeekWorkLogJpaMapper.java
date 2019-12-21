package com.metalheart.converter.mapper;

import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeekWorkLogToWeekWorkLogJpaMapper {

    WeekWorkLogToWeekWorkLogJpaMapper INSTANCE = Mappers.getMapper(WeekWorkLogToWeekWorkLogJpaMapper.class);

    @Mapping(source = "id.taskId", target = "taskId")
    @Mapping(source = "id.dayIndex", target = "dayIndex")
    WeekWorkLog convert(WeekWorkLogJpa task);

    @Mapping(source = "taskId", target = "id.taskId")
    @Mapping(source = "dayIndex", target = "id.dayIndex")
    WeekWorkLogJpa convert(WeekWorkLog task);
}

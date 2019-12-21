package com.metalheart.converter.mapper;

import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RunningListArchivePKToWeekIdMapper {

    RunningListArchivePKToWeekIdMapper INSTANCE = Mappers.getMapper(RunningListArchivePKToWeekIdMapper.class);

    WeekId convert(RunningListArchiveJpaPK task);

    RunningListArchiveJpaPK convert(WeekId task);
}

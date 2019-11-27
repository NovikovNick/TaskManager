package com.metalheart.converter.mapper;

import com.metalheart.model.WeekId;
import com.metalheart.model.rest.request.GetArchiveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GetArchiveRequestToWeekIdMapper {

    GetArchiveRequestToWeekIdMapper INSTANCE = Mappers.getMapper(GetArchiveRequestToWeekIdMapper.class);

    GetArchiveRequest convert(WeekId task);

    WeekId convert(GetArchiveRequest task);
}

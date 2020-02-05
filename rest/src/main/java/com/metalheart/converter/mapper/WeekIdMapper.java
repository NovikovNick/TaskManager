package com.metalheart.converter.mapper;

import com.metalheart.model.WeekId;
import com.metalheart.model.request.ArchiveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeekIdMapper {

    WeekIdMapper INSTANCE = Mappers.getMapper(WeekIdMapper.class);

    WeekId map(ArchiveRequest src);
}

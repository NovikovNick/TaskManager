package com.metalheart.converter;

import com.metalheart.converter.mapper.WeekIdMapper;
import com.metalheart.model.WeekId;
import com.metalheart.model.request.ArchiveRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArchiveRequestToWeekConverter implements Converter<ArchiveRequest, WeekId> {

    @Override
    public WeekId convert(ArchiveRequest source) {
        return WeekIdMapper.INSTANCE.map(source);
    }
}
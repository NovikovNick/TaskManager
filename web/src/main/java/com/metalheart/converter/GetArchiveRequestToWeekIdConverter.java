package com.metalheart.converter;

import com.metalheart.converter.mapper.GetArchiveRequestToWeekIdMapper;
import com.metalheart.model.WeekId;
import com.metalheart.model.request.GetArchiveRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GetArchiveRequestToWeekIdConverter implements Converter<GetArchiveRequest, WeekId> {

    @Override
    public WeekId convert(GetArchiveRequest source) {
        return GetArchiveRequestToWeekIdMapper.INSTANCE.convert(source);
    }
}
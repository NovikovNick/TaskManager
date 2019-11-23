package com.metalheart.converter;

import com.metalheart.model.WeekId;
import com.metalheart.model.rest.request.GetArchiveRequest;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GetArchiveRequestToWeekIdConverter implements Converter<GetArchiveRequest, WeekId> {

    @Autowired
    private Mapper mapper;

    @Override
    public WeekId convert(GetArchiveRequest source) {
        return mapper.map(source, WeekId.class);
    }
}
package com.metalheart.converter;

import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchivePK;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListArchivePKToWeekIdConverter implements Converter<RunningListArchivePK, WeekId> {

    @Autowired
    private Mapper mapper;

    @Override
    public WeekId convert(RunningListArchivePK source) {
        return mapper.map(source, WeekId.class);
    }
}

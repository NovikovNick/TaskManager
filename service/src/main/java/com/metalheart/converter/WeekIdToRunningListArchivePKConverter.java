package com.metalheart.converter;

import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchivePK;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeekIdToRunningListArchivePKConverter implements Converter<WeekId, RunningListArchivePK> {

    @Autowired
    private Mapper mapper;

    @Override
    public RunningListArchivePK convert(WeekId source) {
        return mapper.map(source, RunningListArchivePK.class);
    }
}

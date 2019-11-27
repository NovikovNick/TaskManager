package com.metalheart.converter;

import com.metalheart.converter.mapper.RunningListArchivePKToWeekIdMapper;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchivePK;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeekIdToRunningListArchivePKConverter implements Converter<WeekId, RunningListArchivePK> {

    @Override
    public RunningListArchivePK convert(WeekId source) {
        return RunningListArchivePKToWeekIdMapper.INSTANCE.convert(source);
    }
}

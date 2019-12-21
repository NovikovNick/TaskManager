package com.metalheart.converter;

import com.metalheart.converter.mapper.RunningListArchivePKToWeekIdMapper;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListArchivePKToWeekIdConverter implements Converter<RunningListArchiveJpaPK, WeekId> {

    @Override
    public WeekId convert(RunningListArchiveJpaPK source) {
        return RunningListArchivePKToWeekIdMapper.INSTANCE.convert(source);
    }
}

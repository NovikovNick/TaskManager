package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskToTaskRecordMapper;
import com.metalheart.converter.mapper.WeekWorkLogToWeekWorkLogJpaMapper;
import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.TaskJpa;
import com.metalheart.model.jpa.WeekWorkLogJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeekWorkLogToWeekWorkLogJpaConverter implements Converter<WeekWorkLog, WeekWorkLogJpa> {

    @Override
    public WeekWorkLogJpa convert(WeekWorkLog source) {
        return WeekWorkLogToWeekWorkLogJpaMapper.INSTANCE.convert(source);
    }
}
package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskRecordMapper;
import com.metalheart.model.Task;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskRecordToTaskModelConverter implements Converter<TaskRecord, Task> {

    @Override
    public Task convert(TaskRecord source) {
        return TaskModelToTaskRecordMapper.INSTANCE.convert(source);
    }
}
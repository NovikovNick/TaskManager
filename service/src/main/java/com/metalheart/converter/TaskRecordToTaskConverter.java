package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskToTaskRecordMapper;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskRecordToTaskConverter implements Converter<TaskRecord, Task> {

    @Override
    public Task convert(TaskRecord source) {
        return TaskToTaskRecordMapper.INSTANCE.convert(source);
    }
}
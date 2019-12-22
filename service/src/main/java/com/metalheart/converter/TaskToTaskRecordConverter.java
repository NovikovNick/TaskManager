package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskRecordMapper;
import com.metalheart.model.Task;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskRecordConverter implements Converter<Task, TaskRecord> {

    @Override
    public TaskRecord convert(Task source) {
        return TaskModelToTaskRecordMapper.INSTANCE.convert(source);
    }
}
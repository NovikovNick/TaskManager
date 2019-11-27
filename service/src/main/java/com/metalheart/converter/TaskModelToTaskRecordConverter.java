package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskRecordMapper;
import com.metalheart.model.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskModelToTaskRecordConverter implements Converter<TaskModel, TaskRecord> {

    @Override
    public TaskRecord convert(TaskModel source) {
        return TaskModelToTaskRecordMapper.INSTANCE.convert(source);
    }
}
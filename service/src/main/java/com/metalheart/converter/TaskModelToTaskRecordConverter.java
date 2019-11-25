package com.metalheart.converter;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Component
public class TaskModelToTaskRecordConverter implements Converter<TaskModel, TaskRecord> {

    @Override
    public TaskRecord convert(TaskModel source) {
        TaskRecord dest = new TaskRecord();

        dest.setId(source.getId());

        dest.setTitle(source.getTitle());
        dest.setDescription(source.getDescription());
        dest.setPriority(source.getPriority());

        ofNullable(source.getCreatedAt()).ifPresent(f -> dest.setCreatedAt(f.toOffsetDateTime()));
        ofNullable(source.getModifiedAt()).ifPresent(f -> dest.setModifiedAt(f.toOffsetDateTime()));

        return dest;
    }
}
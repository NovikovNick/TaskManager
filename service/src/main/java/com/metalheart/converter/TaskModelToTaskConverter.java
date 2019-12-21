package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskMapper;
import com.metalheart.model.service.TaskModel;
import com.metalheart.model.jpa.TaskJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskModelToTaskConverter implements Converter<TaskModel, TaskJpa> {
    @Override
    public TaskJpa convert(TaskModel source) {
        return TaskModelToTaskMapper.INSTANCE.convert(source);
    }
}
package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskMapper;
import com.metalheart.model.service.TaskModel;
import com.metalheart.model.jpa.TaskJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskModelConverter implements Converter<TaskJpa, TaskModel> {

    @Override
    public TaskModel convert(TaskJpa source) {
        return TaskModelToTaskMapper.INSTANCE.convert(source);
    }
}
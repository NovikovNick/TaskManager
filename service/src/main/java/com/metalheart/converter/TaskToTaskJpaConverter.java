package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskToTaskJpaMapper;
import com.metalheart.model.Task;
import com.metalheart.model.jpa.TaskJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskJpaConverter implements Converter<Task, TaskJpa> {
    @Override
    public TaskJpa convert(Task source) {
        return TaskToTaskJpaMapper.INSTANCE.map(source);
    }
}
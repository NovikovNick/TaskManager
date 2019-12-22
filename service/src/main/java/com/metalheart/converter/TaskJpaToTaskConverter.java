package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskToTaskJpaMapper;
import com.metalheart.model.Task;
import com.metalheart.model.jpa.TaskJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskJpaToTaskConverter implements Converter<TaskJpa, Task> {

    @Override
    public Task convert(TaskJpa source) {
        return TaskToTaskJpaMapper.INSTANCE.map(source);
    }
}
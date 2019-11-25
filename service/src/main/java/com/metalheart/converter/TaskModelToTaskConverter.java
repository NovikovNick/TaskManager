package com.metalheart.converter;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskModelToTaskConverter implements Converter<TaskModel, Task> {

    @Autowired
    private Mapper objectMapper;

    @Override
    public Task convert(TaskModel source) {
        return objectMapper.map(source, Task.class);
    }
}
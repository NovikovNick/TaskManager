package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskMapper;
import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskModelConverter implements Converter<Task, TaskModel> {

    @Override
    public TaskModel convert(Task source) {
        return TaskModelToTaskMapper.INSTANCE.convert(source);
    }
}
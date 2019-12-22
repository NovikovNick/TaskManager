package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.model.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskViewModelToTaskConverter implements Converter<TaskViewModel, Task> {

    @Override
    public Task convert(TaskViewModel source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}
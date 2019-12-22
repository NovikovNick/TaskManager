package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.rest.response.TaskViewModel;
import com.metalheart.model.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskViewModelConverter implements Converter<Task, TaskViewModel> {

    @Override
    public TaskViewModel convert(Task source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}
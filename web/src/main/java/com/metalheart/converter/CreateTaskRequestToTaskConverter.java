package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.Task;
import com.metalheart.model.request.CreateTaskRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskRequestToTaskConverter implements Converter<CreateTaskRequest, Task> {

    @Override
    public Task convert(CreateTaskRequest source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}
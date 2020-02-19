package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.Task;
import com.metalheart.model.request.UpdateTaskRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UpdateTaskRequestToTaskConverter implements Converter<UpdateTaskRequest, Task> {

    @Override
    public Task convert(UpdateTaskRequest source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}
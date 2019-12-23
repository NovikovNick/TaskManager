package com.metalheart.converter.mapper;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.request.CreateTaskRequest;
import com.metalheart.model.request.UpdateTaskRequest;
import com.metalheart.model.rest.response.TagViewModel;
import com.metalheart.model.rest.response.TaskViewModel;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskViewModelToTaskMapper {

    TaskViewModelToTaskMapper INSTANCE = Mappers.getMapper(TaskViewModelToTaskMapper.class);

    Task map(TaskViewModel task);

    TaskViewModel map(Task task);

    Task map(UpdateTaskRequest request);

    @Mapping(source = "taskId", target = "id")
    Task map(CreateTaskRequest task);

    @Mapping(source = "title", target = "text")
    TagViewModel map(Tag task);



    @Mapping(source = "text", target = "title")
    Tag map(TagViewModel task);

    default Integer map(String src) {
        return StringUtils.isNumeric(src) ? Integer.valueOf(src) : 0;
    }

    default String map(Integer src) {
        return Objects.isNull(src) ? "" : src.toString();
    }
}

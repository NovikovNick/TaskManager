package com.metalheart.converter.mapper;

import com.metalheart.model.TaskModel;
import com.metalheart.model.jpa.Tag;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.response.TagViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskModelToTaskMapper {

    TaskModelToTaskMapper INSTANCE = Mappers.getMapper(TaskModelToTaskMapper.class);

    TaskModel convert(Task task);

    Task convert(TaskModel task);

    TagViewModel convert(Tag task);

    Tag convert(TagViewModel task);
}

package com.metalheart.converter.mapper;

import com.metalheart.model.service.TaskModel;
import com.metalheart.model.jpa.Tag;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.response.TagViewModel;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskModelToTaskMapper {

    TaskModelToTaskMapper INSTANCE = Mappers.getMapper(TaskModelToTaskMapper.class);

    TaskModel convert(Task task);

    Task convert(TaskModel task);

    @Mapping(source = "title", target = "text")
    TagViewModel convert(Tag task);

    @Mapping(source = "text", target = "title")
    Tag convert(TagViewModel task);

    default Integer convert(String src) {
        return StringUtils.isNumeric(src) ? Integer.valueOf(src) : 0;
    }

    default String convert(Integer src) {
        return Objects.isNull(src) ? "" : src.toString();
    }
}

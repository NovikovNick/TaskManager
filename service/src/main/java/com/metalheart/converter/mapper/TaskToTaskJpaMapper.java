package com.metalheart.converter.mapper;

import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.jpa.TagJpa;
import com.metalheart.model.jpa.TaskJpa;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskToTaskJpaMapper {

    TaskToTaskJpaMapper INSTANCE = Mappers.getMapper(TaskToTaskJpaMapper.class);

    Task map(TaskJpa src);

    TaskJpa map(Task src);

    Tag map(TagJpa src);

    TagJpa map(Tag src);

    default Integer map(String src) {
        return StringUtils.isNumeric(src) ? Integer.valueOf(src) : 0;
    }

    default String map(Integer src) {
        return Objects.isNull(src) ? "" : src.toString();
    }
}

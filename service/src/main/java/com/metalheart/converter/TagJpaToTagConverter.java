package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskToTaskJpaMapper;
import com.metalheart.model.Tag;
import com.metalheart.model.jpa.TagJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagJpaToTagConverter implements Converter<TagJpa, Tag> {
    @Override
    public Tag convert(TagJpa source) {
        return TaskToTaskJpaMapper.INSTANCE.map(source);
    }
}
package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskModelToTaskMapper;
import com.metalheart.model.jpa.Tag;
import com.metalheart.model.rest.response.TagViewModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagToTagViewModelConverter implements Converter<Tag, TagViewModel> {

    @Override
    public TagViewModel convert(Tag source) {
        return TaskModelToTaskMapper.INSTANCE.convert(source);
    }
}
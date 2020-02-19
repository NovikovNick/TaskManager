package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.Tag;
import com.metalheart.model.response.TagViewModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagViewModelToTagConverter implements Converter<TagViewModel, Tag> {

    @Override
    public Tag convert(TagViewModel source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}

package com.metalheart.service.impl;

import com.metalheart.model.Tag;
import com.metalheart.model.jpa.TagJpa;
import com.metalheart.model.jpa.TaskJpa;
import com.metalheart.repository.inmemory.SelectedTagRepository;
import com.metalheart.repository.jpa.TagJpaRepository;
import com.metalheart.repository.jpa.TaskJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.TagService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

@Component
public class TagServiceImpl implements TagService {

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    @Autowired
    private DateService dateService;

    @Autowired
    private SelectedTagRepository selectedTagRepository;

    @Autowired
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Transactional
    @Override
    public List<Tag> getSelectedTags(Integer userId) {
        return selectedTagRepository.getSelectedTags(userId).stream()
            .map(tagJpaRepository::getOne)
            .map(tag -> conversionService.convert(tag, Tag.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Tag> getTags(Integer userId) {
        return tagJpaRepository.findAllByUserId(userId).stream()
            .map(tag -> conversionService.convert(tag, Tag.class))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addTagToTask(String tagTitle, Integer taskId) {

        TaskJpa task = taskJpaRepository.getOne(taskId);
        TagJpa tag = getTag(task.getUserId(), tagTitle);

        task.getTags().add(tag);
        taskJpaRepository.save(task);
    }

    @Transactional
    @Override
    public void removeTagFromTask(String tagTitle, Integer taskId) {

        TaskJpa task = taskJpaRepository.getOne(taskId);
        TagJpa tag = tagJpaRepository.findTagByUserIdAndTitle(task.getUserId(), tagTitle);

        task.getTags().remove(tag);
        taskJpaRepository.save(task);
    }

    @Override
    public void selectTag(Integer userId, String tag) {
        selectedTagRepository.addSelectedTag(userId, getTag(userId, tag).getId());
    }

    @Override
    public void removeSelectedTag(Integer userId, String tag) {
        if (tagJpaRepository.existsByTitle(tag)) {
            selectedTagRepository.removeSelectedTag(userId, getTag(userId, tag).getId());
        }
    }

    private TagJpa getTag(Integer userId, String tagTitle) {

        if (tagJpaRepository.existsByTitle(tagTitle)) {

            return tagJpaRepository.findTagByUserIdAndTitle(userId, tagTitle);

        } else {

            return tagJpaRepository.save(TagJpa.builder()
                .userId(userId)
                .title(tagTitle)
                .createdAt(dateService.now())
                .build());
        }
    }
}

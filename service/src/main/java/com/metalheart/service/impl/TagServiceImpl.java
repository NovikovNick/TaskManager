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
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private ConversionService conversionService;

    @Transactional
    @Override
    public List<Tag> getSelectedTags() {
        return selectedTagRepository.getSelectedTags().stream()
            .map(tagJpaRepository::getOne)
            .map(tag -> conversionService.convert(tag, Tag.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Tag> getAllTags() {
        return tagJpaRepository.findAll().stream()
            .map(tag -> conversionService.convert(tag, Tag.class))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addTagToTask(String tagTitle, Integer taskId) {

        TagJpa tag = getTag(tagTitle);

        TaskJpa task = taskJpaRepository.getOne(taskId);
        task.getTags().add(tag);
        taskJpaRepository.save(task);
    }

    @Transactional
    @Override
    public void removeTagFromTask(String tagTitle, Integer taskId) {

        TagJpa tag = tagJpaRepository.findTagByTitle(tagTitle);

        TaskJpa task = taskJpaRepository.getOne(taskId);
        task.getTags().remove(tag);
        taskJpaRepository.save(task);
    }

    @Override
    public void selectTag(String tag) {
        selectedTagRepository.addSelectedTag(getTag(tag).getId());
    }

    @Override
    public void removeSelectedTag(String tag) {
        if (tagJpaRepository.existsByTitle(tag)) {
            selectedTagRepository.removeSelectedTag(getTag(tag).getId());
        }
    }

    private TagJpa getTag(String tagTitle) {

        if (tagJpaRepository.existsByTitle(tagTitle)) {

            return tagJpaRepository.findTagByTitle(tagTitle);

        } else {

            return tagJpaRepository.save(TagJpa.builder()
                .title(tagTitle)
                .createdAt(dateService.now())
                .build());
        }
    }
}

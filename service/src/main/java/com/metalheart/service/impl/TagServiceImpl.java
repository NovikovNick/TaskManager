package com.metalheart.service.impl;

import com.metalheart.model.jpa.Tag;
import com.metalheart.model.jpa.Task;
import com.metalheart.model.rest.response.TagViewModel;
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

    @Override
    public List<TagViewModel> getAllTags() {
        return tagJpaRepository.findAll().stream()
            .map(tag -> conversionService.convert(tag, TagViewModel.class))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addTagToTask(String tagTitle, Integer taskId) {

        Tag tag = getTag(tagTitle);

        Task task = taskJpaRepository.getOne(taskId);
        task.getTags().add(tag);
        taskJpaRepository.save(task);
    }

    @Transactional
    @Override
    public void removeTagFromTask(String tagTitle, Integer taskId) {

        Tag tag = tagJpaRepository.findTagByTitle(tagTitle);

        Task task = taskJpaRepository.getOne(taskId);
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


    private Tag getTag(String tagTitle) {
        Tag tag;

        if (tagJpaRepository.existsByTitle(tagTitle)) {

            tag = tagJpaRepository.findTagByTitle(tagTitle);

        } else {

            tag = tagJpaRepository.save(Tag.builder()
                .title(tagTitle)
                .createdAt(dateService.now())
                .build());
        }
        return tag;
    }
}

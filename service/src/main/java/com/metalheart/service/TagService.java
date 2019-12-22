package com.metalheart.service;

import com.metalheart.model.Tag;
import com.metalheart.model.jpa.TagJpa;
import java.util.List;

public interface TagService {

    List<Tag> getSelectedTags();

    List<Tag> getAllTags();

    void addTagToTask(String tag, Integer taskId);

    void removeTagFromTask(String tag, Integer taskId);

    void  selectTag(String tag);

    void removeSelectedTag(String tag);

    TagJpa getTag(String tagTitle);
}

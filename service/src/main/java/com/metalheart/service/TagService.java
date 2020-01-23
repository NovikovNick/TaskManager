package com.metalheart.service;

import com.metalheart.model.Tag;
import java.util.List;

public interface TagService {

    List<Tag> getSelectedTags(Integer userId);

    List<Tag> getTags(Integer userId);

    void addTagToTask(String tag, Integer taskId);

    void removeTagFromTask(String tag, Integer taskId);

    void selectTag(Integer userId, String tag);

    void removeSelectedTag(Integer userId, String tag);
}

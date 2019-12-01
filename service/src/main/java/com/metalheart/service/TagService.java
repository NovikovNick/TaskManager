package com.metalheart.service;

import com.metalheart.model.jpa.Tag;
import com.metalheart.model.rest.response.TagViewModel;
import java.util.List;

public interface TagService {

    List<TagViewModel> getSelectedTags();

    List<TagViewModel> getAllTags();

    void addTagToTask(String tag, Integer taskId);

    void removeTagFromTask(String tag, Integer taskId);

    void  selectTag(String tag);

    void removeSelectedTag(String tag);

    Tag getTag(String tagTitle);
}

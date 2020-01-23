package com.metalheart.repository.inmemory;

import java.util.List;

public interface SelectedTagRepository {


    void addSelectedTag(Integer userId, Integer tagId);

    void removeSelectedTag(Integer userId, Integer tagId);

    List<Integer> getSelectedTags(Integer userId);

    void deleteAll();
}

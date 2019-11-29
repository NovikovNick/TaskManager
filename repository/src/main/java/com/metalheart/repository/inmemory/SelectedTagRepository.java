package com.metalheart.repository.inmemory;

import java.util.List;

public interface SelectedTagRepository {


    void addSelectedTag(Integer tagId);

    void removeSelectedTag(Integer tagId);

    List<Integer> getSelectedTags();

    void deleteAll();
}

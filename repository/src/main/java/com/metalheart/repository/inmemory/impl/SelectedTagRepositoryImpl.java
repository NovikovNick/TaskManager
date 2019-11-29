package com.metalheart.repository.inmemory.impl;

import com.metalheart.repository.inmemory.SelectedTagRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SelectedTagRepositoryImpl implements SelectedTagRepository {

    Set<Integer> tags = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void addSelectedTag(Integer tagId) {
        tags.add(tagId);
    }

    @Override
    public void removeSelectedTag(Integer tagId) {
        tags.remove(tagId);
    }

    @Override
    public List<Integer> getSelectedTags() {
        return new ArrayList<>(tags);
    }

    @Override
    public void deleteAll() {
        tags.clear();
    }
}

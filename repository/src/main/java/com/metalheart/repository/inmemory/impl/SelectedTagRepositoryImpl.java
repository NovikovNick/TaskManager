package com.metalheart.repository.inmemory.impl;

import com.metalheart.repository.inmemory.SelectedTagRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SelectedTagRepositoryImpl implements SelectedTagRepository {

    Map<Integer, Set<Integer>> tagsByUserId = new ConcurrentHashMap<>();

    @Override
    public void addSelectedTag(Integer userId, Integer tagId) {
        getTags(userId).add(tagId);
    }

    @Override
    public void removeSelectedTag(Integer userId, Integer tagId) {
        getTags(userId).remove(tagId);
    }

    @Override
    public List<Integer> getSelectedTags(Integer userId) {
        return new ArrayList<>(getTags(userId));
    }

    private Set<Integer> getTags(Integer userId) {

        if(!tagsByUserId.containsKey(userId)) {
            tagsByUserId.put(userId, new HashSet<>());
        }

        return tagsByUserId.get(userId);
    }

    @Override
    public void deleteAll() {
        tagsByUserId.clear();
    }
}

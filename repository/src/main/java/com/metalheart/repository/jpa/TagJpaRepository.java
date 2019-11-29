package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Integer> {

    boolean existsByTitle(String title);

    Tag findTagByTitle(String title);
}
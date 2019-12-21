package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.TagJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends JpaRepository<TagJpa, Integer> {

    boolean existsByTitle(String title);

    TagJpa findTagByTitle(String title);
}
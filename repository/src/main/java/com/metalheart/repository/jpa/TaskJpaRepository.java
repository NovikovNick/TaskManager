package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.TaskJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskJpa, Integer> {

    List<TaskJpa> findAllByOrderByPriorityAsc();

    @Query("SELECT DISTINCT t from TaskJpa t "
        + " JOIN t.tags tag "
        + " where tag.id in :tagIds "
        + " GROUP BY t.id, t.priority having count(t.priority) = :tagCount"
        + " ORDER BY t.priority ASC")
    List<TaskJpa> findAllByTags(@Param("tagIds") List<Integer> tagIds,
                                @Param("tagCount") Long tagCount);
}
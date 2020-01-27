package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.TaskJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskJpa, Integer> {

    @Query("SELECT DISTINCT t from TaskJpa t "
        + " where t.deleted = FALSE"
        + " AND t.userId = :userId"
        + " ORDER BY t.priority ASC")
    List<TaskJpa> findAllByUserIdOrderByPriorityAsc(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT t from TaskJpa t "
        + " JOIN t.tags tag "
        + " where tag.id in :tagIds "
        + " and t.deleted = FALSE "
        + " GROUP BY t.id, t.priority having count(t.priority) = :tagCount"
        + " ORDER BY t.priority ASC")
    List<TaskJpa> findAllByTags(@Param("tagIds") List<Integer> tagIds,
                                @Param("tagCount") Long tagCount);
    @Modifying
    @Query("UPDATE TaskJpa t "
        + " SET t.deleted = :deleted"
        + " where t.id = :taskId ")
    void setDeleted(@Param("taskId") Integer taskId, @Param("deleted") Boolean deleted);

    @Query("SELECT count(t) from TaskJpa t "
        + " where t.userId = :userId "
        + " and t.deleted = FALSE ")
    Integer getMaxPriority(@Param("userId") Integer userId);
}
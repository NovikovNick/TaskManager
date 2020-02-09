package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.TagJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends JpaRepository<TagJpa, Integer> {

    boolean existsByUserIdAndTitle(Integer userId, String title);

    TagJpa findTagByUserIdAndTitle(Integer userId, String title);

    List<TagJpa> findAllByUserId(Integer userId);

    @Query(value = "SELECT * from tag as t"
        + " where t.user_id = ?"
        + " AND t.deleted = ?", nativeQuery = true)
    List<TagJpa> findAllByUserIdAndDeleted(Integer userId, Boolean deleted);

    @Modifying
    @Query("UPDATE TagJpa t "
        + " SET t.deleted = :deleted"
        + " where t.id = :tagId ")
    void setDeleted(@Param("tagId") Integer tagId, @Param("deleted") Boolean deleted);
}
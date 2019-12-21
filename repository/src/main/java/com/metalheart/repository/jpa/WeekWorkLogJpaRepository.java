package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.WeekWorkLogJpa;
import com.metalheart.model.jpa.WeekWorkLogJpaPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekWorkLogJpaRepository extends JpaRepository<WeekWorkLogJpa, WeekWorkLogJpaPK> {

    @Query("SELECT log FROM WeekWorkLogJpa log WHERE log.id.taskId = :taskId")
    List<WeekWorkLogJpa> findAllByTaskId(@Param("taskId") Integer taskId);
}

package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.WeekWorkLog;
import com.metalheart.model.jpa.WeekWorkLogPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekWorkLogJpaRepository extends JpaRepository<WeekWorkLog, WeekWorkLogPK> {

    @Query("SELECT log FROM WeekWorkLog log WHERE log.id.taskId = :taskId")
    List<WeekWorkLog> findAllByTaskId(@Param("taskId") Integer taskId);
}

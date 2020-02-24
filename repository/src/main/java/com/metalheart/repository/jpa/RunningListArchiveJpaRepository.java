package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningListArchiveJpaRepository extends JpaRepository<RunningListArchiveJpa, RunningListArchiveJpaPK> {

    @Query("SELECT a.id from RunningListArchiveJpa a "
        + " where a.id.userId = :userId ")
    List<RunningListArchiveJpaPK> findAllIdByUserId(@Param("userId") Integer userId);
}
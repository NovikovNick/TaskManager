package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningListArchiveJpaRepository extends JpaRepository<RunningListArchiveJpa, RunningListArchiveJpaPK> {
}
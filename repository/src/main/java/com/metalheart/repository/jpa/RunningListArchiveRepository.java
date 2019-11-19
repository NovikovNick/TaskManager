package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.RunningListArchive;
import com.metalheart.model.jpa.RunningListArchivePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningListArchiveRepository extends JpaRepository<RunningListArchive, RunningListArchivePK> {
}
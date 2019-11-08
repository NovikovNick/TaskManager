package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByOrderByPriorityAsc();
}
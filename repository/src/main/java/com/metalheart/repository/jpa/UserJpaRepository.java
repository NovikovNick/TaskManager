package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.UserJpa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpa, Integer> {

    Optional<UserJpa> findByUsername(String username);

    boolean existsByEmail(String email);
}
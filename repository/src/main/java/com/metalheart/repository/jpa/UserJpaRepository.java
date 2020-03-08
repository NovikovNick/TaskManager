package com.metalheart.repository.jpa;

import com.metalheart.model.jpa.UserJpa;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpa, Integer> {

    Optional<UserJpa> findByUsername(String username);

    Optional<UserJpa> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE UserJpa u "
        + " SET u.password = :password"
        + " where u.id = :userId ")
    void updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    @Modifying
    @Query("UPDATE UserJpa u "
        + " SET u.lastLoginAt = :lastLoginAt"
        + " where u.id = :userId ")
    void updateLastLogin(@Param("userId") Integer userId, @Param("lastLoginAt") ZonedDateTime lastLoginAt);
}
package com.metalheart.model.jpa;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "TagJpa")
@Table(name = "tag")
@SQLDelete(sql = "UPDATE tag SET deleted = true WHERE id = ?")
@Loader(namedQuery = "findTagById")
@NamedQuery(name = "findTagById", query =
    "SELECT t " +
        "FROM TagJpa t " +
        "WHERE " +
        "    t.id = ?1 AND " +
        "    t.deleted = false")
@Where(clause = "deleted = false")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id", "userId", "title"})
public class TagJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private Integer userId;


    @Column(nullable = false)
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column
    private boolean deleted;
}
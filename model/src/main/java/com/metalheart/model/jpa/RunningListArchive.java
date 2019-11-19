package com.metalheart.model.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "running_list_archive")
public class RunningListArchive implements Serializable {

    @EmbeddedId
    private RunningListArchivePK id;

    @Column(name = "archive", nullable = false)
    private String archive;

}

package com.metalheart.model;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Tag implements Cloneable {

    private Integer id;

    private Integer userId;

    private String title;

    private ZonedDateTime createdAt;

    @Override
    public Tag clone() {
        return toBuilder().build();
    }
}
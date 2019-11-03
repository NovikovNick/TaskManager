package com.metalheart.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskViewModel {

    private Integer id;
    private String title;
    private String description;
    private List<String> status;
}

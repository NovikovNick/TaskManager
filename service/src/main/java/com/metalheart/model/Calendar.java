package com.metalheart.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Current day number and list of weed dates
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Calendar {

    private Integer currentDay;
    private List<String> weekDates;
}

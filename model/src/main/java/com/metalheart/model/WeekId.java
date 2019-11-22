package com.metalheart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pair of year and week number
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekId {

    private Integer year;
    private Integer week;
}

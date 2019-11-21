package com.metalheart.model.rest.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarViewModel {

    private Integer currentDay;
    private List<String> weekDates;
}

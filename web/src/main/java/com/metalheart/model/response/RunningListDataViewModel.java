package com.metalheart.model.response;

import com.metalheart.model.WeekId;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunningListDataViewModel {

    private UserViewModel user;

    private RunningListViewModel runningList;

    private List<WeekId> archives;
}

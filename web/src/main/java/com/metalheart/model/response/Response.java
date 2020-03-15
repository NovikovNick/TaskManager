package com.metalheart.model.response;

import com.metalheart.model.WeekId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private Message message;

    private UserViewModel user;

    private RunningListViewModel runningList;

    private List<WeekId> archives;
}

package com.metalheart.service;

import com.metalheart.model.User;
import com.metalheart.model.response.RunningListDataViewModel;

public interface WebService {
    RunningListDataViewModel geRunningListDataViewModel(User user, Integer timezoneOffset);
}

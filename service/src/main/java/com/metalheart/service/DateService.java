package com.metalheart.service;

import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.rest.response.CalendarViewModel;

public interface DateService {

    RunningListArchivePK getCurrentWeekId();

    RunningListArchivePK getNextWeekId(RunningListArchivePK weekId);

    RunningListArchivePK getPreviousWeekId(RunningListArchivePK weekId);

    CalendarViewModel getCalendar();
}

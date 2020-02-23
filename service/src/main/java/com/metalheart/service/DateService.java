package com.metalheart.service;

import com.metalheart.model.Calendar;
import com.metalheart.model.WeekId;
import java.time.ZonedDateTime;

/**
 * Service to provide operations with {@link WeekId} and {@link Calendar} and other related to date and time
 * structures.
 */
public interface DateService {

    /**
     * @return current time
     */
    ZonedDateTime now();

    /**
     *
     * @param timezoneOffset timezone difference between UTC and Local Time from user
     * @return
     */
    WeekId getCurrentWeekId(Integer timezoneOffset);

    /**
     * @return {@link WeekId} for next week
     */
    WeekId getNextWeekId(WeekId weekId);

    /**
     * @return {@link WeekId} for previous week
     */
    WeekId getPreviousWeekId(WeekId weekId);

    /**
     * @param timezoneOffset timezone difference between UTC and Local Time from user
     * @return {@link Calendar} for current week
     */
    Calendar getCalendar(Integer timezoneOffset);

    /**
     * @return {@link Calendar} for specific week
     */
    Calendar getCalendar(WeekId weekId);
}

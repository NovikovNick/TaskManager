package com.metalheart.test.unit.service;


import com.metalheart.model.WeekId;
import com.metalheart.service.impl.DateServiceImpl;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DateServiceTest {

    DateServiceImpl runningListService = new DateServiceImpl();

    @Test
    public void test() {

        ZonedDateTime now = ZonedDateTime.now();

        int hoursInTenYears = 24 * 356;

        for (int i = 0; i < hoursInTenYears; i++) {

            now = now.plusHours(i);

            List<WeekId> ids = new ArrayList<>(hoursInTenYears);
            for (int j = 0; j < 100; j++) {

                WeekId weekId = runningListService.getWeekId(ZonedDateTime.now().plusWeeks(j));

                Assert.assertFalse( weekId + " already exist in " + ids, ids.contains(weekId));

                ids.add(weekId);
            }
        }
    }
}

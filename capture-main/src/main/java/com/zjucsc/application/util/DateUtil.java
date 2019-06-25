package com.zjucsc.application.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static List<Date> getBetweenDates(Date start, Date end,int type) {
        List<Date> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        //Calendar.DAY_OF_YEAR

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            //Calendar.DAY_OF_YEAR
            tempStart.add(type, 1);
        }
        return result;
    }
}

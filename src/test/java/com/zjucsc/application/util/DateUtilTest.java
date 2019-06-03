package com.zjucsc.application.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void getBetweenDates() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        calendar1.add(Calendar.DATE,1);
        Date date = calendar1.getTime();
        System.out.println(DateUtil.getBetweenDates(new Date(),date,Calendar.DATE));
    }
}
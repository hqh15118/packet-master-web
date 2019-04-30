package com.zjucsc.application.tshark.domain.beans;

import lombok.Data;

@Data
public class TimeStamp {
    private int year;
    private int day;
    private int hour;
    private int minutes;
    private int second;
    private int millsecond;
    private int unsecond;
    private int nansecond;

    public TimeStamp(int year, int day, int hour, int minutes, int second, int millsecond, int unsecond, int nansecond) {
        this.year = year;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.second = second;
        this.millsecond = millsecond;
        this.unsecond = unsecond;
        this.nansecond = nansecond;
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "year=" + year +
                ", day=" + day +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", second=" + second +
                ", millsecond=" + millsecond +
                ", unsecond=" + unsecond +
                ", nansecond=" + nansecond +
                '}';
    }
}

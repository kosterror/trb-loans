package ru.hits.trb.trbloans.util;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public static Date getStartNextDate() {
        var currentDate = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateSeveralHoursAgo(int hourCount) {
        var currentDate = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR, hourCount);
        return calendar.getTime();
    }

    public static Date getStartCurrentDay() {
        var currentDate = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}

package com.navi.my.money.util;

import lombok.NonNull;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for providing date time utils.
 */
public class DateTimeUtils {

    /**
     * Method for converting month to date.
     * @param month Month which has to be converted
     * @return Converted date.
     */
    public static Date getTime(@NonNull final String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Month.valueOf(month.toUpperCase()).getValue());
        Date date = calendar.getTime();
        return date;
    }

    /**
     * Method which converts month to next Month date.
     * @param date Month which has to be convereted
     * @return NextMonth date.
     */
    public static Date getTimeWithNextMonth(Date date) {
        int month = date.getMonth()+1;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        Date newDate = calendar.getTime();
        return newDate;
    }


}


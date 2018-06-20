package com.example.astrodashalib.helper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by himanshu on 29/09/17.
 */

public class DateTimeUtil {

    public static long INTERVAL_1DAY = 1000 * 60 * 60 * 24;

    public static long getCurrentTimestampSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static int daysUntilSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK);
    }

}

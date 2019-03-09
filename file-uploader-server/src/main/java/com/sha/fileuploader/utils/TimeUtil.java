package com.sha.fileuploader.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static int extractYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int extractMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int extractDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String convertToDuration(long totalSecs){
        int hours = (int)totalSecs / 3600;
        int minutes = (int)((totalSecs % 3600) / 60);
        int seconds = (int) (totalSecs % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

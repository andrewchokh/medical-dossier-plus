package com.andrewchokh.medicaldossierplus.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static final String PATTERN = "yyyy-MM-dd-HH-mm-ss";

    public static Date getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).getTime();
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat(PATTERN).format(date);
    }

    public static Date stringToDate(String string){
        try {
            return new SimpleDateFormat(PATTERN).parse(string);
        }
        catch (ParseException e) {
            return null;
        }
    }
}

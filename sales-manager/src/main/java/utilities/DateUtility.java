package utilities;

import exceptions.UtilityException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by u624 on 3/31/17.
 */
public class DateUtility {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String TIME_ZONE = "UTC";

    private DateUtility() {
        /* static class */
    }

    public static String formatDate(Date date) {
        return getSimpleDateFormat().format(date);
    }

    public static Date parseDate(String date) {
        try {
            return getSimpleDateFormat().parse(date);
        } catch (ParseException e) {
            throw new UtilityException(e);
        }
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return simpleDateFormat;
    }
}

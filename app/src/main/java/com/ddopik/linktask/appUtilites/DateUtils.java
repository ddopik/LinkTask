package com.ddopik.linktask.appUtilites;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Utility class for performing operations on Date and Calendar instances.
 */
public class DateUtils {

    public static void setCalTimeToZero(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH);
    }


    public String getCurrentDate(String flag) {
        Calendar c = Calendar.getInstance();


        switch (flag) {
            case "Day":
                SimpleDateFormat d = new SimpleDateFormat("dd");
                return d.format(c.getTime());

            case "Day_word":
                SimpleDateFormat d_w = new SimpleDateFormat("EEEE");
                return d_w.format(c.getTime());

            case "Month_word":
                SimpleDateFormat m = new SimpleDateFormat("MMM");
                return m.format(c.getTime());

            case "Month":
                SimpleDateFormat m_w = new SimpleDateFormat("MM");
                return m_w.format(c.getTime());


            case "Year":
                SimpleDateFormat y = new SimpleDateFormat("yyyy");
                return y.format(c.getTime());

            default:
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());
                return formattedDate;
        }


    }
}
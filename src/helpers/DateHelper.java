/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Nesh
 */
public class DateHelper {

    public static String dateToString(Date in, boolean onlyYear) {
        DateFormat df;
        if (onlyYear) {
            df = new SimpleDateFormat("yyyy");
        } else {
            df = new SimpleDateFormat("dd.MM.yyyy");
        }
        return df.format(in);
    }

    public static Date stringToDate(String in, boolean onlyYear) {
        try {
            Date date;

            if (onlyYear) {
                date = new SimpleDateFormat("yyyy").parse(in);
            } else {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(in);
            }
            return date;
        } catch (ParseException ex) {
            System.out.println("NELZE PARSOVAT DATUM");
            return null;
        }
    }

    public static String dateToStringIncludingTime(Date in) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return df.format(in);
    }

    public static String getCurrentDateString(boolean onlyYear) {
        if (onlyYear) {
            return dateToString(new Date(), true);
        } else {
            return dateToString(new Date(), false);
        }
    }

    public static Date getCurrentDate(boolean onlyYear) {
        if (onlyYear) {
            return stringToDate(dateToString(new Date(), true), true);
        } else {
            return stringToDate(dateToString(new Date(), false), false);
        }
    }

    public static boolean compareGE(Date d1, Date d2) {
        return (d1.getTime() >= d2.getTime());
    }

    public static boolean compareEQ(Date d1, Date d2) {
        return (d1.getTime() == d2.getTime());
    }

    public static Date plusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}

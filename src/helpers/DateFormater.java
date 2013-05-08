/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nesh
 */
public class DateFormater {

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
}

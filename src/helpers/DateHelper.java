package helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Třída pro ulehčení práce s daty
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class DateHelper {

    /**
     * Převede datum na text
     *
     * @param in datum
     * @param onlyYear ignorovat datum?
     * @return rok jako stext
     */
    public static String dateToString(Date in, boolean onlyYear) {
        DateFormat df;
        if (onlyYear) {
            df = new SimpleDateFormat("yyyy");
        } else {
            df = new SimpleDateFormat("dd.MM.yyyy");
        }
        return df.format(in);
    }

    /**
     * Převede text na datum
     *
     * @param in text
     * @param onlyYear ignorovat datum?
     * @return rok jako stext
     */
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

    /**
     * Převede text na datum včetně času
     *
     * @param in text
     * @return datum s časem jako stext
     */
    public static String dateToStringIncludingTime(Date in) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return df.format(in);
    }

    /**
     * Vrací současné datum jako text
     *
     * @param onlyYear ignorovat datum?
     * @return současné datum jako text
     */
    public static String getCurrentDateString(boolean onlyYear) {
        if (onlyYear) {
            return dateToString(new Date(), true);
        } else {
            return dateToString(new Date(), false);
        }
    }

    /**
     * Vrací současné datum
     *
     * @param onlyYear ignorovat datum?
     * @return současné datum
     */
    public static Date getCurrentDate(boolean onlyYear) {
        if (onlyYear) {
            return stringToDate(dateToString(new Date(), true), true);
        } else {
            return stringToDate(dateToString(new Date(), false), false);
        }
    }

    /**
     * Provovná zda je datum 1 větší nebo rovno datumu 2
     *
     * @param d1 datum 1
     * @param d2 datum 2
     * @return indokace zda je datum 1 větší nebo rovno
     */
    public static boolean compareGE(Date d1, Date d2) {
        return (d1.getTime() >= d2.getTime());
    }

    /**
     * Provovná zda je datum 1 rovno datumu 2
     *
     * @param d1 datum 1
     * @param d2 datum 2
     * @return indokace zda je datum 1 stejné jako datum 2
     */
    public static boolean compareEQ(Date d1, Date d2) {
        return (d1.getTime() == d2.getTime());
    }

    /**
     * Vrátí původní datum s rozdílem days dnů
     *
     * @param date vstupní datum
     * @param days kolik dní má metoda přičíst
     * @return původní datum s rozdílem days dnů
     */
    public static Date plusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * Vrátí rozdíl ve dnech mezi daty
     *
     * @param d1 vstupní datum 1
     * @param d2 vstupní datum 2
     * @return rozdíl v počtu dnů
     */
    public static int dayDiff(Date d1, Date d2) {
        final long DAY_MILLIS = 1000 * 60 * 60 * 24;
        long day1 = d1.getTime() / DAY_MILLIS;
        long day2 = d2.getTime() / DAY_MILLIS;
        return ((int) (day2 - day1));
    }

    /**
     * Vrátí současné datum všetně času
     *
     * @return současné datum všetně času
     */
    public static String getCurrentDateIncludingTimeString() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        return df.format(new Date());
    }
}

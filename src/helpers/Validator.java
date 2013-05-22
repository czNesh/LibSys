package helpers;

/**
 * Třída pro validaci vstupů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class Validator {

    /**
     * Vrací zda je alespoň jeden z textů vyplněné
     *
     * @param in1 text1
     * @param in2 text2
     * @return indikace zda je alespoň jeden vyplněné
     */
    public static boolean isOneOfTheTwoFilled(String in1, String in2) {
        int successfullCount = 0;

        if (isValidString(in1)) {
            successfullCount++;
        }

        if (isValidString(in2)) {
            successfullCount++;
        }

        return (successfullCount > 0) ? true : false;
    }

    /**
     * Vrací zda jsou oba z textů vyplněné
     *
     * @param in1 text1
     * @param in2 text2
     * @return indikace zda jsou oba z textů vyplněné
     */
    public static boolean isBothOfTheTwoFilled(String in1, String in2) {
        int successfullCount = 0;

        if (isValidString(in1)) {
            successfullCount++;
        }

        if (isValidString(in2)) {
            successfullCount++;
        }

        return (successfullCount == 2) ? true : false;
    }

    /**
     * Kontroluje validnost emailu
     *
     * @param in vstupní text
     * @return zda je email validní
     */
    public static boolean isValidEmail(String in) {
        return in.matches(".+@.+\\.[a-z]+");
    }

    /**
     * Kontroluje zda je text neprázdný
     *
     * @param in vstupní text
     * @return zda je text neprázdný
     */
    public static boolean isValidString(String in) {
        if (in == null) {
            return false;
        }
        return (in.isEmpty()) ? false : true;
    }

    /**
     * Kontroluje zda je text převeditelný na číslo
     *
     * @param in vstupní text
     * @return zda je text převeditelný na číslo
     */
    public static boolean isValidNumber(String in) {
        try {
            Long i = Long.parseLong(in);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Kontroluje zda objekt není hodnota null
     *
     * @param o objekt
     * @return indikace zda objekt není hodnota null
     */
    public static boolean isNotNull(Object o) {
        return (o != null) ? true : false;
    }

    /**
     * Kontroluje zda je text formátu ISBN10
     *
     * @param in text
     * @return indikace zda je text formátu ISBN10
     */
    public static boolean isValidISBN10(String in) {
        if (in.matches("[0-9]{10}$")) {
            System.out.println("a" + in + "a");
            return false;
        }
        return (in.length() == 10) ? true : false;
    }

    /**
     * Kontroluje zda je text formátu ISBN13
     *
     * @param in text
     * @return indikace zda je text formátu ISBN13
     */
    public static boolean isValidISBN13(String in) {
        if (!isValidNumber(in)) {
            return false;
        }
        return (in.length() == 13) ? true : false;
    }

    /**
     * Kontroluje zda je text kladné číslo
     *
     * @param in text
     * @return indikace zda je text kladné číslo
     */
    public static boolean isValidPositiveNumber(String in) {
        try {
            int i = Integer.parseInt(in);
            if (i > 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}

package io;

import java.util.prefs.Preferences;

/**
 * Třída ukládající nastavení na počítač
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class Configuration {

    Preferences prefs;
    private static Configuration instance; // instance této třídy
    //Server
    private static final int DEFAULT_SERVER_PORT = 6789;
    private static final boolean DEFAULT_SERVER_AUTOSTART = false;
    // Knihy
    private static final int DEFAULT_MAX_BOOK_ROWS_COUNT = 30;
    private static final String DEFAULT_BOOK_ORDER_BY = "id";
    private static final String DEFAULT_BOOK_ORDER_TYPE = "DESC";
    private static final boolean DEFAULT_SHOW_TITLE = true;
    private static final boolean DEFAULT_SHOW_AUTHOR = true;
    private static final boolean DEFAULT_SHOW_ISBN10 = false;
    private static final boolean DEFAULT_SHOW_ISBN13 = false;
    private static final boolean DEFAULT_SHOW_COUNT = false;
    private static final boolean DEFAULT_SHOW_LOCATION = false;
    private static final boolean DEFAULT_SHOW_LANGUAGE = false;
    private static final boolean DEFAULT_SHOW_PUBLISHER = false;
    private static final boolean DEFAULT_SHOW_PUBLISHED_YEAR = false;
    private static final boolean DEFAULT_SHOW_PAGE_COUNT = false;
    private static final boolean DEFAULT_REQUIRE_AUTHOR = true;
    private static final boolean DEFAULT_REQUIRE_ISBN10 = true;
    private static final boolean DEFAULT_REQUIRE_ISBN13 = true;
    private static final boolean DEFAULT_REQUIRE_COUNT = true;
    private static final boolean DEFAULT_REQUIRE_LOCATION = true;
    private static final boolean DEFAULT_REQUIRE_LANGUAGE = true;
    private static final boolean DEFAULT_REQUIRE_PUBLISHER = true;
    private static final boolean DEFAULT_REQUIRE_PUBLISHED_YEAR = true;
    private static final boolean DEFAULT_REQUIRE_PAGE_COUNT = true;
    private static final boolean DEFAULT_REQUIRE_SPONSOR = true;
    private static final boolean DEFAULT_REQUIRE_GENRE = true;
    private static final boolean DEFAULT_REQUIRE_ADDED_DATE = true;
    //Uživatelé
    private static final int DEFAULT_MAX_CUSTOMER_ROWS_COUNT = 30;
    private static final String DEFAULT_CUSTOMER_ORDER_BY = "id";
    private static final String DEFAULT_CUSTOMER_ORDER_TYPE = "DESC";
    private static final boolean DEFAULT_CUSTOMER_SHOW_SSN = true;
    private static final boolean DEFAULT_CUSTOMER_SHOW_NAME = true;
    private static final boolean DEFAULT_CUSTOMER_SHOW_EMAIL = true;
    private static final boolean DEFAULT_CUSTOMER_SHOW_PHONE = false;
    private static final boolean DEFAULT_CUSTOMER_SHOW_ADRESS = true;
    private static final boolean DEFAULT_CUSTOMER_SHOW_NOTES = false;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_FNAME = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_LNAME = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_EMAIL = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_PHONE = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_STREET = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_CITY = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_POSTCODE = true;
    private static final boolean DEFAULT_CUSTOMER_REQUIRE_COUNTRY = true;
    //PŮJČKY
    private static final int DEFAULT_MAX_BORROW_ROWS_COUNT = 30;
    private static final String DEFAULT_BORROW_ORDER_BY = "id";
    private static final String DEFAULT_BORROW_ORDER_TYPE = "DESC";
    private static final boolean DEFAULT_BORROW_SHOW_CUSTOMER = true;
    private static final boolean DEFAULT_BORROW_SHOW_LIBRARIAN = true;
    private static final boolean DEFAULT_BORROW_SHOW_FROM = true;
    private static final boolean DEFAULT_BORROW_SHOW_TO = true;
    private static final boolean DEFAULT_BORROW_SHOW_ITEMS = true;
    private static final boolean DEFAULT_BORROW_SHOW_RETURNED = true;
    private static final int DEFAULT_BORROW_DAYS = 7;
    //OZNÁMENÍ
    private static final int DEFAULT_MAX_NOTIFICATION_ROWS_COUNT = 30;
    private static final int DEFAULT_LONG_BORROW_DAYS = 30;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_TYPE = true;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_CUSTOMER = true;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_BORROW_CODE = true;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_ITEM = true;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_FROM_DATE = true;
    private static final boolean DEFAULT_NOTIFICATION_SHOW_TO_DATE = true;
    //ZÁKLADNÍ
    private static final boolean DEFAULT_SHOW_DELETED = false;
    private static final boolean DEFAULT_SKIP_LOGGIN = false;
    private static final String DEFAULT_EMAIL = "mail@mailserver.com";
    private static final String DEFAULT_WORKSPACE = "";

    // SINGLETON
    public static Configuration getInstance() {
        synchronized (Configuration.class) {
            if (instance == null) {
                instance = new Configuration();
            }
        }
        return instance;
    }

    /**
     * Třídní konsruktor
     */
    private Configuration() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    /**
     * Reset na defaultní hodnoty
     */
    public void setDefaultValues() {
        //Server
        prefs.putInt("SERVER_PORT", DEFAULT_SERVER_PORT);
        prefs.putBoolean("SERVER_AUTOSTART", DEFAULT_SERVER_AUTOSTART);
        // Knihy
        prefs.putInt("MAX_BOOK_ROWS_COUNT", DEFAULT_MAX_BOOK_ROWS_COUNT);
        prefs.put("BOOK_ORDER_BY", DEFAULT_BOOK_ORDER_BY);
        prefs.put("BOOK_ORDER_TYPE", DEFAULT_BOOK_ORDER_TYPE);
        prefs.putBoolean("SHOW_TITLE", DEFAULT_SHOW_TITLE);
        prefs.putBoolean("SHOW_AUTHOR", DEFAULT_SHOW_AUTHOR);
        prefs.putBoolean("SHOW_ISBN10", DEFAULT_SHOW_ISBN10);
        prefs.putBoolean("SHOW_ISBN13", DEFAULT_SHOW_ISBN13);
        prefs.putBoolean("SHOW_COUNT", DEFAULT_SHOW_COUNT);
        prefs.putBoolean("SHOW_LOCATION", DEFAULT_SHOW_LOCATION);
        prefs.putBoolean("SHOW_LANGUAGE", DEFAULT_SHOW_LANGUAGE);
        prefs.putBoolean("SHOW_PUBLISHER", DEFAULT_SHOW_PUBLISHER);
        prefs.putBoolean("SHOW_PUBLISHED_YEAR", DEFAULT_SHOW_PUBLISHED_YEAR);
        prefs.putBoolean("SHOW_PAGE_COUNT", DEFAULT_SHOW_PAGE_COUNT);
        prefs.putBoolean("REQUIRE_AUTHOR", DEFAULT_REQUIRE_AUTHOR);
        prefs.putBoolean("REQUIRE_ISBN10", DEFAULT_REQUIRE_ISBN10);
        prefs.putBoolean("REQUIRE_ISBN13", DEFAULT_REQUIRE_ISBN13);
        prefs.putBoolean("REQUIRE_COUNT", DEFAULT_REQUIRE_COUNT);
        prefs.putBoolean("REQUIRE_LOCATION", DEFAULT_REQUIRE_LOCATION);
        prefs.putBoolean("REQUIRE_LANGUAGE", DEFAULT_REQUIRE_LANGUAGE);
        prefs.putBoolean("REQUIRE_PUBLISHER", DEFAULT_REQUIRE_PUBLISHER);
        prefs.putBoolean("REQUIRE_PUBLISHED_YEAR", DEFAULT_REQUIRE_PUBLISHED_YEAR);
        prefs.putBoolean("REQUIRE_PAGE_COUNT", DEFAULT_REQUIRE_PAGE_COUNT);
        prefs.putBoolean("REQUIRE_SPONSOR", DEFAULT_REQUIRE_SPONSOR);
        prefs.putBoolean("REQUIRE_GENRE", DEFAULT_REQUIRE_GENRE);
        prefs.putBoolean("REQUIRE_ADDED_DATE", DEFAULT_REQUIRE_ADDED_DATE);
        //Uživatelé
        prefs.putInt("MAX_CUSTOMER_ROWS_COUNT", DEFAULT_MAX_CUSTOMER_ROWS_COUNT);
        prefs.put("CUSTOMER_ORDER_BY", DEFAULT_CUSTOMER_ORDER_BY);
        prefs.put("CUSTOMER_ORDER_TYPE", DEFAULT_CUSTOMER_ORDER_TYPE);
        prefs.putBoolean("CUSTOMER_SHOW_SSN", DEFAULT_CUSTOMER_SHOW_SSN);
        prefs.putBoolean("CUSTOMER_SHOW_NAME", DEFAULT_CUSTOMER_SHOW_NAME);
        prefs.putBoolean("CUSTOMER_SHOW_EMAIL", DEFAULT_CUSTOMER_SHOW_EMAIL);
        prefs.putBoolean("CUSTOMER_SHOW_PHONE", DEFAULT_CUSTOMER_SHOW_PHONE);
        prefs.putBoolean("CUSTOMER_SHOW_ADRESS", DEFAULT_CUSTOMER_SHOW_ADRESS);
        prefs.putBoolean("CUSTOMER_SHOW_NOTES", DEFAULT_CUSTOMER_SHOW_NOTES);
        prefs.putBoolean("CUSTOMER_REQUIRE_FNAME", DEFAULT_CUSTOMER_REQUIRE_FNAME);
        prefs.putBoolean("CUSTOMER_REQUIRE_LNAME", DEFAULT_CUSTOMER_REQUIRE_LNAME);
        prefs.putBoolean("CUSTOMER_REQUIRE_EMAIL", DEFAULT_CUSTOMER_REQUIRE_EMAIL);
        prefs.putBoolean("CUSTOMER_REQUIRE_PHONE", DEFAULT_CUSTOMER_REQUIRE_PHONE);
        prefs.putBoolean("CUSTOMER_REQUIRE_STREET", DEFAULT_CUSTOMER_REQUIRE_STREET);
        prefs.putBoolean("CUSTOMER_REQUIRE_CITY", DEFAULT_CUSTOMER_REQUIRE_CITY);
        prefs.putBoolean("CUSTOMER_REQUIRE_POSTCODE", DEFAULT_CUSTOMER_REQUIRE_POSTCODE);
        prefs.putBoolean("CUSTOMER_REQUIRE_COUNTRY", DEFAULT_CUSTOMER_REQUIRE_COUNTRY);
        //PŮJČKY
        prefs.putInt("MAX_BORROW_ROWS_COUNT", DEFAULT_MAX_BORROW_ROWS_COUNT);
        prefs.put("BORROW_ORDER_BY", DEFAULT_BORROW_ORDER_BY);
        prefs.put("BORROW_ORDER_TYPE", DEFAULT_BORROW_ORDER_TYPE);
        prefs.putBoolean("BORROW_SHOW_CUSTOMER", DEFAULT_BORROW_SHOW_CUSTOMER);
        prefs.putBoolean("BORROW_SHOW_LIBRARIAN", DEFAULT_BORROW_SHOW_LIBRARIAN);
        prefs.putBoolean("BORROW_SHOW_FROM", DEFAULT_BORROW_SHOW_FROM);
        prefs.putBoolean("BORROW_SHOW_TO", DEFAULT_BORROW_SHOW_TO);
        prefs.putBoolean("BORROW_SHOW_ITEMS", DEFAULT_BORROW_SHOW_ITEMS);
        prefs.putBoolean("BORROW_SHOW_RETURNED", DEFAULT_BORROW_SHOW_RETURNED);
        prefs.putInt("BORROW_DAYS", DEFAULT_BORROW_DAYS);
        //OZNÁMENÍ
        prefs.putInt("MAX_NOTIFICATION_ROWS_COUNT", DEFAULT_MAX_NOTIFICATION_ROWS_COUNT);
        prefs.putInt("LONG_BORROW_DAYS", DEFAULT_LONG_BORROW_DAYS);
        prefs.putBoolean("NOTIFICATION_SHOW_TYPE", DEFAULT_NOTIFICATION_SHOW_TYPE);
        prefs.putBoolean("NOTIFICATION_SHOW_CUSTOMER", DEFAULT_NOTIFICATION_SHOW_CUSTOMER);
        prefs.putBoolean("NOTIFICATION_SHOW_BORROW_CODE", DEFAULT_NOTIFICATION_SHOW_BORROW_CODE);
        prefs.putBoolean("NOTIFICATION_SHOW_ITEM", DEFAULT_NOTIFICATION_SHOW_ITEM);
        prefs.putBoolean("NOTIFICATION_SHOW_FROM_DATE", DEFAULT_NOTIFICATION_SHOW_FROM_DATE);
        prefs.putBoolean("NOTIFICATION_SHOW_TO_DATE", DEFAULT_NOTIFICATION_SHOW_TO_DATE);

        //ZÁKLADNÍ
        prefs.putBoolean("SHOW_DELETED", DEFAULT_SHOW_DELETED);
        prefs.putBoolean("SKIP_LOGGIN", DEFAULT_SKIP_LOGGIN);
        prefs.put("DEFAULT_EMAIL", DEFAULT_EMAIL);
        prefs.put("WORKSPACE", DEFAULT_WORKSPACE);
    }

    /**
     * Vrátí číslo portu serveru
     *
     * @return číslo portu
     */
    public int getServerPort() {
        return prefs.getInt("SERVER_PORT", DEFAULT_SERVER_PORT);
    }

    /**
     * Nastavení LibSys server port
     *
     * @param port číslo portu
     */
    public void setServerPort(int in) {
        prefs.putInt("SERVER_PORT", in);
    }

    /**
     * Vrátí indikaci zda serverběží
     *
     * @return indikace zda server běží
     */
    public boolean isServerAutoStart() {
        return prefs.getBoolean("SERVER_AUTOSTART", DEFAULT_SERVER_AUTOSTART);
    }

    /**
     * Zapne / Vypne server
     *
     * @param in stav
     */
    public void setServerAutoStart(boolean in) {
        prefs.putBoolean("SERVER_AUTOSTART", in);
    }

    /**
     * Obecná nastavení Vypnutí loginu / Zobrazovat smazané položky / defaultní
     * email / Workspace
     */
    public boolean isSkipLogging() {
        return prefs.getBoolean("SKIP_LOGGING", DEFAULT_SKIP_LOGGIN);
    }

    public void setSkipLogging(boolean in) {
        prefs.putBoolean("SKIP_LOGGING", in);
    }

    public boolean isDeletedItemVisible() {
        return prefs.getBoolean("SHOW_DELETED", DEFAULT_SHOW_DELETED);
    }

    public void setDeletedItemVisible(boolean in) {
        prefs.putBoolean("SHOW_DELETED", in);
    }

    public String getDefaultEmail() {
        return prefs.get("DEFAULT_EMAIL", DEFAULT_EMAIL);
    }

    public void setDefaultEmail(String in) {
        prefs.put("DEFAULT_EMAIL", in);
    }

    public void setWorkcpace(String in) {
        prefs.put("WORKSPACE", in);
    }

    public String getWorkspace() {
        return prefs.get("WORKSPACE", DEFAULT_WORKSPACE);
    }

    /*
     * Nastavení pro knihy
     * zobrazování položek / povinné položky / počet řádků / řazení
     */
    public int getMaxBookRowsCount() {
        return prefs.getInt("MAX_BOOK_ROWS", DEFAULT_MAX_BOOK_ROWS_COUNT);
    }

    public void setMaxBookRowsCount(int in) {
        prefs.putInt("MAX_BOOK_ROWS", in);
    }

    public String getBookOrderBy() {
        return prefs.get("BOOK_ORDER_BY", DEFAULT_BOOK_ORDER_BY);
    }

    public void setBookOrderBy(String in) {
        prefs.put("BOOK_ORDER_BY", in);
    }

    public void setBookOrderType(String in) {
        prefs.put("BOOK_ORDER_TYPE", in);
    }

    public String getBookOrderType() {
        return prefs.get("BOOK_ORDER_TYPE", DEFAULT_BOOK_ORDER_TYPE);
    }

    public void setShowAuthor(boolean in) {
        prefs.putBoolean("SHOW_AUTHOR", in);
    }

    public boolean isShowTitle() {
        return prefs.getBoolean("SHOW_TITLE", DEFAULT_SHOW_TITLE);
    }

    public void setShowTitle(boolean in) {
        prefs.putBoolean("SHOW_TITLE", in);
    }

    public boolean isShowAuthor() {
        return prefs.getBoolean("SHOW_AUTHOR", DEFAULT_SHOW_AUTHOR);
    }

    public void setShowISBN10(boolean in) {
        prefs.putBoolean("SHOW_ISBN10", in);
    }

    public boolean isShowISBN10() {
        return prefs.getBoolean("SHOW_ISBN10", DEFAULT_SHOW_ISBN10);
    }

    public void setShowISBN13(boolean in) {
        prefs.putBoolean("SHOW_ISBN13", in);
    }

    public boolean isShowISBN13() {
        return prefs.getBoolean("SHOW_ISBN13", DEFAULT_SHOW_ISBN13);
    }

    public void setShowCount(boolean in) {
        prefs.putBoolean("SHOW_COUNT", in);
    }

    public boolean isShowCount() {
        return prefs.getBoolean("SHOW_COUNT", DEFAULT_SHOW_COUNT);
    }

    public void setShowLocation(boolean in) {
        prefs.putBoolean("SHOW_LOCATION", in);
    }

    public boolean isShowLocation() {
        return prefs.getBoolean("SHOW_LOCATION", DEFAULT_SHOW_LOCATION);
    }

    public void setShowLanguage(boolean in) {
        prefs.putBoolean("SHOW_LANGUAGE", in);
    }

    public boolean isShowLanguage() {
        return prefs.getBoolean("SHOW_LANGUAGE", DEFAULT_SHOW_LANGUAGE);
    }

    public void setShowPublisher(boolean in) {
        prefs.putBoolean("SHOW_PUBLISHER", in);
    }

    public boolean isShowPublisher() {
        return prefs.getBoolean("SHOW_PUBLISHER", DEFAULT_SHOW_PUBLISHER);
    }

    public void setShowPublishedYear(boolean in) {
        prefs.putBoolean("SHOW_PUBLISHED_YEAR", in);
    }

    public boolean isShowPublishedYear() {
        return prefs.getBoolean("SHOW_PUBLISHED_YEAR", DEFAULT_SHOW_PUBLISHED_YEAR);
    }

    public void setShowPageCount(boolean in) {
        prefs.putBoolean("SHOW_PAGE_COUNT", in);
    }

    public boolean isShowPageCount() {
        return prefs.getBoolean("SHOW_PAGE_COUNT", DEFAULT_SHOW_PAGE_COUNT);
    }

    public void setRequireAuthor(boolean in) {
        prefs.putBoolean("REQUIRE_AUTHOR", in);
    }

    public boolean isRequireAuthor() {
        return prefs.getBoolean("REQUIRE_AUTHOR", DEFAULT_REQUIRE_AUTHOR);
    }

    public void setRequireISBN10(boolean in) {
        prefs.putBoolean("REQUIRE_ISBN10", in);
    }

    public boolean isRequireISBN10() {
        return prefs.getBoolean("REQUIRE_ISBN10", DEFAULT_REQUIRE_ISBN10);
    }

    public void setRequireISBN13(boolean in) {
        prefs.putBoolean("REQUIRE_ISBN13", in);
    }

    public boolean isRequireISBN13() {
        return prefs.getBoolean("REQUIRE_ISBN13", DEFAULT_REQUIRE_ISBN13);
    }

    public void setRequireCount(boolean in) {
        prefs.putBoolean("REQUIRE_COUNT", in);
    }

    public boolean isRequireCount() {
        return prefs.getBoolean("REQUIRE_COUNT", DEFAULT_REQUIRE_COUNT);
    }

    public void setRequireLocation(boolean in) {
        prefs.putBoolean("REQUIRE_LOCATION", in);
    }

    public boolean isRequireLocation() {
        return prefs.getBoolean("REQUIRE_LOCATION", DEFAULT_REQUIRE_LOCATION);
    }

    public void setRequireLanguage(boolean in) {
        prefs.putBoolean("REQUIRE_LANGUAGE", in);
    }

    public boolean isRequireLanguage() {
        return prefs.getBoolean("REQUIRE_LANGUAGE", DEFAULT_REQUIRE_LANGUAGE);
    }

    public void setRequirePublisher(boolean in) {
        prefs.putBoolean("REQUIRE_PUBLISHER", in);
    }

    public boolean isRequirePublisher() {
        return prefs.getBoolean("REQUIRE_PUBLISHER", DEFAULT_REQUIRE_PUBLISHER);
    }

    public void setRequirePublishedYear(boolean in) {
        prefs.putBoolean("REQUIRE_PUBLISHED_YEAR", in);
    }

    public boolean isRequirePublishedYear() {
        return prefs.getBoolean("REQUIRE_PUBLISHED_YEAR", DEFAULT_REQUIRE_PUBLISHED_YEAR);
    }

    public void setRequirePageCount(boolean in) {
        prefs.putBoolean("REQUIRE_PAGE_COUNT", in);
    }

    public boolean isRequirePageCount() {
        return prefs.getBoolean("REQUIRE_PAGE_COUNT", DEFAULT_REQUIRE_PAGE_COUNT);
    }

    public void setRequireSponsor(boolean in) {
        prefs.putBoolean("REQUIRE_SPONSOR", in);
    }

    public boolean isRequireSponsor() {
        return prefs.getBoolean("REQUIRE_SPONSOR", DEFAULT_REQUIRE_SPONSOR);
    }

    public void setRequireAddedDate(boolean in) {
        prefs.putBoolean("REQUIRE_ADDED_DATE", in);
    }

    public boolean isRequireAddedDate() {
        return prefs.getBoolean("REQUIRE_ADDED_DATE", DEFAULT_REQUIRE_ADDED_DATE);
    }

    public void setRequireGenre(boolean in) {
        prefs.putBoolean("REQUIRE_GENRE", in);
    }

    public boolean isRequireGenre() {
        return prefs.getBoolean("REQUIRE_GENRE", DEFAULT_REQUIRE_GENRE);
    }

    /*
     * Nastavení pro zákazníky
     * zobrazování položek / povinné položky / počet řádků / řazení
     */
    public int getMaxCustomerRowsCount() {
        return prefs.getInt("MAX_CUSTOMER_ROWS", DEFAULT_MAX_CUSTOMER_ROWS_COUNT);
    }

    public void setMaxCustomerRowsCount(int in) {
        prefs.putInt("MAX_CUSTOMER_ROWS", in);
    }

    public String getCustomerOrderBy() {
        return prefs.get("CUSTOMER_ORDER_BY", DEFAULT_CUSTOMER_ORDER_BY);
    }

    public void setCustomerOrderBy(String in) {
        prefs.put("CUSTOMER_ORDER_BY", in);
    }

    public String getCustomerOrderType() {
        return prefs.get("CUSTOMER_ORDER_TYPE", DEFAULT_CUSTOMER_ORDER_TYPE);
    }

    public void setCustomerOrderType(String in) {
        prefs.put("CUSTOMER_ORDER_TYPE", in);
    }

    public boolean isCustomerShowSSN() {
        return prefs.getBoolean("SHOW_CUSTOMER_SSN", DEFAULT_CUSTOMER_SHOW_SSN);
    }

    public void setCustomerShowSSN(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_SSN", in);
    }

    public boolean isCustomerShowName() {
        return prefs.getBoolean("SHOW_CUSTOMER_NAME", DEFAULT_CUSTOMER_SHOW_NAME);
    }

    public void setCustomerShowName(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_NAME", in);
    }

    public boolean isCustomerShowEmail() {
        return prefs.getBoolean("SHOW_CUSTOMER_EMAIL", DEFAULT_CUSTOMER_SHOW_EMAIL);
    }

    public void setCustomerShowEmail(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_EMAIL", in);
    }

    public void setCustomerShowPhone(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_PHONE", in);
    }

    public boolean isCustomerShowPhone() {
        return prefs.getBoolean("SHOW_CUSTOMER_PHONE", DEFAULT_CUSTOMER_SHOW_PHONE);
    }

    public void setCustomerShowAdress(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_ADRESS", in);
    }

    public boolean isCustomerShowAdress() {
        return prefs.getBoolean("SHOW_CUSTOMER_ADRESS", DEFAULT_CUSTOMER_SHOW_ADRESS);
    }

    public void setCustomerShowNotes(boolean in) {
        prefs.putBoolean("SHOW_CUSTOMER_NOTES", in);
    }

    public boolean isCustomerShowNotes() {
        return prefs.getBoolean("SHOW_CUSTOMER_NOTES", DEFAULT_CUSTOMER_SHOW_NOTES);
    }

    public void setCustomerRequireFName(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_FNAME", in);
    }

    public boolean isCustomerRequireFName() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_FNAME", DEFAULT_CUSTOMER_REQUIRE_FNAME);
    }

    public void setCustomerRequireLName(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_LNAME", in);
    }

    public boolean isCustomerRequireLName() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_LNAME", DEFAULT_CUSTOMER_REQUIRE_LNAME);
    }

    public void setCustomerRequirePhone(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_PHONE", in);
    }

    public boolean isCustomerRequirePhone() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_PHONE", DEFAULT_CUSTOMER_REQUIRE_PHONE);
    }

    public void setCustomerRequireEmail(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_EMAIL", in);
    }

    public boolean isCustomerRequireEmail() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_EMAIL", DEFAULT_CUSTOMER_REQUIRE_EMAIL);
    }

    public void setCustomerRequirePostcode(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_POSTCODE", in);
    }

    public boolean isCustomerRequirePostcode() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_POSTCODE", DEFAULT_CUSTOMER_REQUIRE_POSTCODE);
    }

    public void setCustomerRequireCity(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_CITY", in);
    }

    public boolean isCustomerRequireCity() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_CITY", DEFAULT_CUSTOMER_REQUIRE_CITY);
    }

    public void setCustomerRequireStreet(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_STREET", in);
    }

    public boolean isCustomerRequireStreet() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_STREET", DEFAULT_CUSTOMER_REQUIRE_STREET);
    }

    public void setCustomerRequireCountry(boolean in) {
        prefs.putBoolean("REQUIRE_CUSTOMER_COUNTRY", in);
    }

    public boolean isCustomerRequireCountry() {
        return prefs.getBoolean("REQUIRE_CUSTOMER_COUNTRY", DEFAULT_CUSTOMER_REQUIRE_COUNTRY);
    }
    /*
     * Nastavení pro půjčky
     * zobrazování položek / povinné položky / počet řádků / řazení
     */

    public int getMaxBorrowRowsCount() {
        return prefs.getInt("MAX_BORROW_ROWS", DEFAULT_MAX_BORROW_ROWS_COUNT);
    }

    public void setMaxBorrowRowsCount(int in) {
        prefs.putInt("MAX_BORROW_ROWS", in);
    }

    public String getBorrowOrderBy() {
        return prefs.get("BORROW_ORDER_BY", DEFAULT_BORROW_ORDER_BY);
    }

    public void setBorrowOrderBy(String in) {
        prefs.put("BORROW_ORDER_BY", in);
    }

    public String getBorrowOrderType() {
        return prefs.get("BORROW_ORDER_TYPE", DEFAULT_BORROW_ORDER_TYPE);
    }

    public void setBorrowOrderType(String in) {
        prefs.put("BORROW_ORDER_TYPE", in);
    }

    public boolean isBorrowShowCustomer() {
        return prefs.getBoolean("SHOW_BORROW_CUSTOMER", DEFAULT_BORROW_SHOW_CUSTOMER);
    }

    public void setBorrowShowCustomer(boolean in) {
        prefs.putBoolean("SHOW_BORROW_CUSTOMER", in);
    }

    public boolean isBorrowShowLibrarian() {
        return prefs.getBoolean("SHOW_BORROW_LIBRARIAN", DEFAULT_BORROW_SHOW_LIBRARIAN);
    }

    public void setBorrowShowLibrarian(boolean in) {
        prefs.putBoolean("SHOW_BORROW_LIBRARIAN", in);
    }

    public boolean isBorrowShowFrom() {
        return prefs.getBoolean("SHOW_BORROW_FROM", DEFAULT_BORROW_SHOW_FROM);
    }

    public void setBorrowShowFrom(boolean in) {
        prefs.putBoolean("SHOW_BORROW_FROM", in);
    }

    public void setBorrowShowTo(boolean in) {
        prefs.putBoolean("SHOW_BORROW_TO", in);
    }

    public boolean isBorrowShowTo() {
        return prefs.getBoolean("SHOW_BORROW_TO", DEFAULT_BORROW_SHOW_TO);
    }

    public void setBorrowShowItems(boolean in) {
        prefs.putBoolean("SHOW_BORROW_ITEMS", in);
    }

    public boolean isBorrowShowItems() {
        return prefs.getBoolean("SHOW_BORROW_ITEMS", DEFAULT_BORROW_SHOW_ITEMS);
    }

    public void setBorrowShowReturned(boolean in) {
        prefs.putBoolean("SHOW_BORROW_RETURNED", in);
    }

    public boolean isBorrowShowReturned() {
        return prefs.getBoolean("SHOW_BORROW_RETURNED", DEFAULT_BORROW_SHOW_RETURNED);
    }

    public void setBorrowDays(int in) {
        prefs.putInt("BORROW_DAYS", in);
    }

    public int getBorrowDays() {
        return prefs.getInt("BORROW_DAYS", DEFAULT_BORROW_DAYS);
    }


    /*
     * Nastavení pro oznámení
     * zobrazování položek / povinné položky / počet řádků / řazení
     */
    public int getMaxNotificationRowsCount() {
        return prefs.getInt("MAX_NOTIFICATION_ROWS", DEFAULT_MAX_NOTIFICATION_ROWS_COUNT);
    }

    public void setMaxNotificationRowsCount(int in) {
        prefs.putInt("MAX_NOTIFICATION_ROWS", in);
    }

    public int getLongBorrowDays() {
        return prefs.getInt("LONG_BORROW_DAYS", DEFAULT_LONG_BORROW_DAYS);
    }

    public void setLongBorrowDays(int in) {
        prefs.putInt("LONG_BORROW_DAYS", in);
    }

    public boolean isNotificationShowType() {
        return prefs.getBoolean("NOTIFICATION_SHOW_TYPE", DEFAULT_NOTIFICATION_SHOW_TYPE);

    }

    public void setNotificationShowType(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_TYPE", in);
    }

    public boolean isNotificationShowCustomer() {
        return prefs.getBoolean("NOTIFICATION_SHOW_CUSTOMER", DEFAULT_NOTIFICATION_SHOW_CUSTOMER);

    }

    public void setNotificationShowCustomer(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_CUSTOMER", in);
    }

    public boolean isNotificationShowBorrowCode() {
        return prefs.getBoolean("NOTIFICATION_SHOW_BORROW_CODE", DEFAULT_NOTIFICATION_SHOW_BORROW_CODE);

    }

    public void setNotificationShowBorrowCode(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_BORROW_CODE", in);
    }

    public boolean isNotificationsShowItem() {
        return prefs.getBoolean("NOTIFICATION_SHOW_ITEM", DEFAULT_NOTIFICATION_SHOW_ITEM);

    }

    public void setNotificationsShowItem(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_ITEM", in);
    }

    public boolean isNotificationShowFrom() {
        return prefs.getBoolean("NOTIFICATION_SHOW_FROM_DATE", DEFAULT_NOTIFICATION_SHOW_FROM_DATE);

    }

    public void setNotificationShowFrom(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_FROM_DATE", in);
    }

    public boolean isNotificationShowTo() {
        return prefs.getBoolean("NOTIFICATION_SHOW_TO_DATE", DEFAULT_NOTIFICATION_SHOW_TO_DATE);

    }

    public void setNotificationShowTo(boolean in) {
        prefs.putBoolean("NOTIFICATION_SHOW_TO_DATE", in);
    }
}

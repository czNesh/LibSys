/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.prefs.Preferences;

/**
 *
 * @author Nesh
 */
public class Configuration {

    Preferences prefs;
    private static Configuration instance;
    // Knihy
    private static final int DEFAULT_MAX_BOOK_ROWS_COUNT = 30;
    private static final String DEFAULT_BOOK_ORDER_BY = "id";
    private static final String DEFAULT_BOOK_ORDER_TYPE = "ASC";
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
    private static final boolean DEFAULT_REQUIRE_AUTHOR = false;
    private static final boolean DEFAULT_REQUIRE_ISBN10 = false;
    private static final boolean DEFAULT_REQUIRE_ISBN13 = false;
    private static final boolean DEFAULT_REQUIRE_COUNT = false;
    private static final boolean DEFAULT_REQUIRE_LOCATION = false;
    private static final boolean DEFAULT_REQUIRE_LANGUAGE = false;
    private static final boolean DEFAULT_REQUIRE_PUBLISHER = false;
    private static final boolean DEFAULT_REQUIRE_PUBLISHED_YEAR = false;
    private static final boolean DEFAULT_REQUIRE_PAGE_COUNT = false;
    private static final boolean DEFAULT_REQUIRE_SPONSOR = false;
    private static final boolean DEFAULT_REQUIRE_GENRE = false;
    private static final boolean DEFAULT_REQUIRE_ADDED_DATE = false;
    
    //Uživatelé
    private static final int DEFAULT_MAX_BORROW_ROWS_COUNT = 30;
    private static final int DEFAULT_MAX_CUSTOMER_ROWS_COUNT = 30;
    private static final int DEFAULT_MAX_NOTIFICATION_ROWS_COUNT = 30;
    private static final boolean DEFAULT_SHOW_DELETED_ITEMS = false;
    private static final boolean DEFAULT_SKIP_LOGGIN = false;
    private static final String DEFAULT_EMAIL = "hejhape1@ssps.cz";
    private static final String DEFAULT_WORKSPACE = "";

    public static Configuration getInstance() {
        synchronized (Configuration.class) {
            if (instance == null) {
                instance = new Configuration();
            }
        }
        return instance;
    }

    private Configuration() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    // Viditelnost smazaných položek
    public boolean isDeletedItemVisible() {
        return prefs.getBoolean("SHOW_DELETED", DEFAULT_SHOW_DELETED_ITEMS);
    }

    public void setDeletedItemVisible(boolean in) {
        prefs.putBoolean("SHOW_DELETED", in);
    }

    // Přihlašování - on/off
    public boolean isSkipLogging() {
        return prefs.getBoolean("SKIP_LOGGING", DEFAULT_SKIP_LOGGIN);
    }

    public void setSkipLogging(boolean in) {
        prefs.putBoolean("SKIP_LOGGING", in);
    }

    // Počet řádků v tabulce - Knihy
    public int getMaxBookRowsCount() {
        return prefs.getInt("MAX_BOOK_ROWS", DEFAULT_MAX_BOOK_ROWS_COUNT);
    }

    public void setMaxBookRowsCount(int in) {
        prefs.putInt("MAX_BOOK_ROWS", in);
    }

    // Počet řádků v tabulce - Zákazníci
    public int getMaxCustomerRowsCount() {
        return prefs.getInt("MAX_CUSTOMER_ROWS", DEFAULT_MAX_CUSTOMER_ROWS_COUNT);
    }

    public void setMaxCustomerRowsCount(int in) {
        prefs.putInt("MAX_CUSTOMER_ROWS", in);
    }

    // Počet řádků v tabulce - Půjčky
    public int getMaxBorrowRowsCount() {
        return prefs.getInt("MAX_BORROW_ROWS", DEFAULT_MAX_BORROW_ROWS_COUNT);
    }

    public void setMaxBorrowRowsCount(int in) {
        prefs.putInt("MAX_BORROW_ROWS", in);
    }

    // Počet řádků v tabulce - Oznámení
    public int getMaxNotificationRowsCount() {
        return prefs.getInt("MAX_NOTIFICATION_ROWS", DEFAULT_MAX_NOTIFICATION_ROWS_COUNT);
    }

    public void setMaxNotificationRowsCount(int in) {
        prefs.putInt("MAX_NOTIFICATION_ROWS", in);
    }

    // Defaultní email
    public String getDefaultEmail() {
        return prefs.get("DEFAULT_EMAIL", DEFAULT_EMAIL);
    }

    public void setDefaultEmail(String in) {
        prefs.put("DEFAULT_EMAIL", in);
    }

    public void setDefaultValues() {
        prefs.putInt("MAX_BOOK_ROWS", DEFAULT_MAX_BOOK_ROWS_COUNT);
        prefs.putInt("MAX_BORROW_ROWS", DEFAULT_MAX_BORROW_ROWS_COUNT);
        prefs.putInt("MAX_CUSTOMER_ROWS", DEFAULT_MAX_CUSTOMER_ROWS_COUNT);
        prefs.putInt("MAX_NOTIFICATION_ROWS", DEFAULT_MAX_NOTIFICATION_ROWS_COUNT);
        prefs.putBoolean("SHOW_DELETED", DEFAULT_SHOW_DELETED_ITEMS);
        prefs.putBoolean("SKIP_LOGGING", DEFAULT_SKIP_LOGGIN);

        prefs.put("BOOK_ORDER_BY", DEFAULT_BOOK_ORDER_BY);
    }

    public String getBookOrderBy() {
        return prefs.get("BOOK_ORDER_BY", DEFAULT_BOOK_ORDER_BY);
    }

    public void setBookOrderBy(String in) {
        prefs.put("BOOK_ORDER_BY", in);
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

    public void setBookOrderType(String in) {
        prefs.put("BOOK_ORDER_TYPE", in);
    }

    public String getBookOrderType() {
        return prefs.get("BOOK_ORDER_TYPE", DEFAULT_BOOK_ORDER_TYPE);
    }

    public void setWorkcpace(String in){
        prefs.put("WORKSPACE", in);
    }
    
    public String getWorkspace() {
        return prefs.get("WORKSPACE", DEFAULT_WORKSPACE);
    }
    
    
}

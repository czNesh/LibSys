package remote;

//import books
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeListener;
import models.entity.Author;
import models.entity.Book;

/**
 *
 * @author Administrator
 */
public class GoogleBooksSearch {

    private StringBuilder query;
    private int status;
    private String textStatus;
    private List<ChangeListener> listeners = new ArrayList<>();
    private ArrayList<Book> results = new ArrayList<>();
    URLConnection conn;

    public GoogleBooksSearch(String query) {
        this.query = new StringBuilder(query);
    }

    public GoogleBooksSearch() {
        this.query = new StringBuilder();
    }

    public void search() {
        results.clear();
        stateChange(0, "ready");
        connectServer();
        searchBooks();
        query = new StringBuilder();
    }

    public void setAutor(String author) {
        if (author.isEmpty()) {
            return;
        }
        query.append(("+inauthor:" + author).replaceAll(" ", "%20"));
    }

    public void setISBN(String isbn) {
        if (isbn.isEmpty()) {
            return;
        }
        query.append(("+isbn:" + isbn).replaceAll(" ", "%20"));
    }

    public void setTitle(String title) {
        if (title.isEmpty()) {
            return;
        }
        query.append(("+intitle:" + title).replaceAll(" ", "%20"));
    }

    public void connectServer() {
        stateChange(10, "Connecting");
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=" + query;
            System.out.println(urlString);
            URL url = new URL(urlString);
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
        } catch (IOException ex) {
            stateChange(0, "Disconnected");
        }
    }

    private void searchBooks() {
        stateChange(30, "Searching");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in;



            /*
             * 
             * PARSING BOOKS FROM GOOGLE
             * 
             */

            // VYTVORI POTREBNE PROMENE
            Book tempBook = null;
            List<Author> tempAuthors = null;
            boolean readAuthors = false;
            boolean readISBN = false;
            String isbnType = "";

            // CTENI
            while ((in = br.readLine()) != null) {

                in = clearSpaces(in);

                if (in.isEmpty()) {
                    continue;
                }

                in = in.replaceFirst("\"", "");

                // NOVA KNIHA
                if (in.startsWith("id\":")) {

                    if (tempBook == null) {
                        tempBook = new Book();
                        tempAuthors = new ArrayList();
                    } else {
                        results.add(tempBook);
                        tempBook = new Book();
                        tempAuthors = new ArrayList();
                    }
                    continue;
                }

                if (tempBook != null) {

                    // ziska autory
                    if (readAuthors) {
                        // HLIDA KONEC CTENI AUTORU
                        if (in.startsWith("],")) {
                            readAuthors = false;
                            tempBook.setMainAuthor(tempAuthors.get(0));
                            tempBook.setAuthors(tempAuthors);
                            continue;
                        }

                        in = in.replaceAll(",", "");

                        // VYTVORI POTREBNE PROMENE
                        Author tempAuthor = new Author();
                        String fname = "";
                        String lname;

                        String[] foo = in.split(" ");

                        for (int i = 0; i < foo.length - 1; i++) {
                            fname += foo[i];
                        }
                        lname = foo[foo.length - 1];
                        lname = lname.substring(0, lname.length() - 1);

                        tempAuthor.setFirstName(fname);
                        tempAuthor.setLastName(lname);
                        tempAuthors.add(tempAuthor);
                    }

                    // ZISKA ISBN (oboje)
                    if (readISBN) {
                        // HLIDA KONEC CTENI AUTORU
                        if (in.startsWith("],")) {
                            readAuthors = false;
                            continue;
                        }

                        // NASTAVI TYP ISBN
                        if (in.startsWith("type\":")) {
                            isbnType = in.substring(8, in.length() - 2);
                            continue;
                        }

                        if (in.startsWith("identifier\":")) {
                            in = in.substring(14, in.length() - 1);
                            if (isbnType.equals("ISBN_10")) {
                                tempBook.setISBN10(in);
                            }

                            if (isbnType.equals("ISBN_13")) {
                                tempBook.setISBN13(in);
                            }
                        }
                    }

                    // ZISKAT NAZEV
                    if (in.startsWith("title\":")) {
                        tempBook.setTitle(in.substring(9, in.length() - 2));
                        continue;
                    }

                    // ZAPNOUT CTENI ATUORU
                    if (in.startsWith("authors\":")) {
                        readAuthors = true;
                        continue;
                    }

                    // ZISKAT VYDAVATELE
                    if (in.startsWith("publisher\":")) {
                        tempBook.setPublisher(in.substring(13, in.length() - 2));
                        continue;
                    }

                    // ZISKAT ROK VYDANI
                    if (in.startsWith("publishedDate\":")) {
                        in = in.substring(17, in.length() - 2);
                        try {
                            Date d = new Date(Integer.parseInt(in)-1900, 1, 1);
                            tempBook.setPublishedYear(d);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        continue;
                    }

                    //ZAPNOUT CTENI ISBN 
                    if (in.startsWith("industryIdentifiers\":")) {
                        readISBN = true;
                        continue;
                    }

                    // ZISKAT POCET STRANEK
                    if (in.startsWith("pageCount\":")) {
                        in = in.substring(12, in.length() - 1);
                        try {
                            tempBook.setPageCount(Integer.parseInt(in));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        continue;
                    }

                    //ZAPNOUT CTENI ISBN 
                    if (in.startsWith("language\":")) {
                        tempBook.setLanguage(in.substring(12, in.length() - 2));
                    }

                }
            }
            if (tempBook != null) {
                results.add(tempBook);
            }

            stateChange(100, "Finished");
        } catch (IOException ex) {
            stateChange(0, "Error");
        }
    }

    public ArrayList<Book> getResults() {
        return results;
    }

    public String clearSpaces(String in) {
        String out = in;
        out = out.replaceAll("\t", "");
        out = out.trim();
        return out;
    }

    public void addChangeListener(ChangeListener e) {
        listeners.add(e);
    }

    public void removeChangeListener(ChangeListener e) {
        listeners.remove(e);
    }

    public void stateChange(int status, String textStatus) {
        this.status = status;
        this.textStatus = textStatus;
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).stateChanged(null);
        }
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the textStatus
     */
    public String getTextStatus() {
        return textStatus;
    }

    public int getResultsCount() {
        return (results == null) ? 0 : results.size();
    }
}
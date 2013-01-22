package services;

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
    private Book temp;
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
        query.append(("isbn:" + isbn).replaceAll(" ", "%20"));
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
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query;
            System.out.println(urlString);
            URL url = new URL(urlString);
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
        } catch (IOException ex) {
            stateChange(0, "Disconnected");
        }
    }

    public void searchBooks() {
        stateChange(30, "Searching");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in;

            while (true) {
                in = br.readLine();
                if (in == null) {
                    break;
                }
                if (in.isEmpty()) {
                    continue;
                }
                in = clearSpaces(in);
                if (in.startsWith("\"title\":")) {
                    temp = new Book();
                    in = in.substring(10, in.length() - 2);
                    temp.setTitle(in);
                    results.add(temp);
                }        
            }
            
            stateChange(100, "Finished");
        } catch (IOException ex) {
            stateChange(0, "Error");
        }
    }

    public ArrayList<Book> getResults() {
        return results;
    }

    public Book getBestResult() {
        return (results.size() > 0) ? results.get(0) : null;
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
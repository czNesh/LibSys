package services;

//import books
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
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
        connectServer();
        stateChange(0, "ready");
        searchBooks();
    }

    public void setAutor(String author) {
        query.append(("+author:" + author));
    }

    public void setISBN(String isbn) {
        query.append(("+isbn:" + isbn));
    }

    public void setTitle(String title) {
        query.append(("+title:" + title));
    }

    public void connectServer() {
        stateChange(10,"Connecting");
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query;
            URL url = new URL(urlString);
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
        } catch (IOException ex) {
            stateChange(0,"Disconnected");
        }
    }

    public void searchBooks() {
        stateChange(30, "Searching");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in;
            while (true) {
                in = br.readLine();
                if (in == null || in.isEmpty()) {
                    break;
                }
                in = clearSpaces(in);


                if (in.startsWith("\"title\":")) {
                    temp = new Book();
                    in = in.substring(10, in.length() - 2);
                    System.out.println(in);
                    temp.setTitle(in);
                    results.add(temp);
                    break;
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
        return results.get(0);
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
}
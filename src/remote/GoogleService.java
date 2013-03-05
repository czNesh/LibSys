package remote;

//import books
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import models.entity.Author;
import models.entity.Book;

/**
 *
 * @author Administrator
 */
public class GoogleService implements Runnable {

    // QUERY
    private StringBuilder query;
    // RESULTS
    private ArrayList<Book> results = new ArrayList<>();
    // THUMBNAIL
    private Image thumbnail;
    // Connection
    URLConnection conn;

    @Override
    public void run() {
        results.clear();
        connectServer();
        searchBooks();
        query = new StringBuilder();
    }
    


    public GoogleService() {
        this.query = new StringBuilder();
    }

    public GoogleService(String query) {
        this.query = new StringBuilder(query);
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
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=" + query;
            System.out.println(urlString);
            URL url = new URL(urlString);
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
        } catch (IOException ex) {
        }
    }

    private void searchBooks() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in;

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
                            Date d = new Date(Integer.parseInt(in) - 1900, 1, 1);
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

        } catch (IOException ex) {
        }
    }

    public String clearSpaces(String in) {
        String out = in;
        out = out.replaceAll("\t", "");
        out = out.trim();
        return out;
    }

    public void downloadThumbnail() {
        try {
            connectServer();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in;
            while ((in = br.readLine()) != null) {
                in = in.trim().replaceAll("\t", "");
                if (in.startsWith("\"thumbnail\":")) {
                    URLConnection connection = new URL(in.substring(14, in.length() - 1).replaceAll("&zoom=1", "")).openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.21; Mac_PowerPC)");
                    thumbnail = ImageIO.read(connection.getInputStream());
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Book> getResults() {
        return results;
    }

    public int getResultsCount() {
        return (results == null) ? 0 : results.size();
    }

    public Image getThumbnail() {
        return thumbnail;
    }
}

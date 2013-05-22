package remote;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Třída - vyhledává náhledy knih
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class GoogleImageDownload extends Thread {

    Image thumbnail;
    URLConnection connection;
    StringBuilder query;
    JLabel target;
    int dimW = -1;
    int dimH = -1;

    public GoogleImageDownload(JLabel target) {
        query = new StringBuilder();
        this.target = target;
    }

    public GoogleImageDownload(JLabel target, int dimW, int dimH) {
        query = new StringBuilder();
        this.target = target;
        this.dimW = dimW;
        this.dimH = dimH;
    }

    @Override
    public void run() {
        if (query.length() == 0) {
            return;
        }
        connectServer();
        downloadThumbnail();
        setImageToTarget();
    }

    public void setISBN(String isbn) {
        if (isbn.isEmpty()) {
            return;
        }
        query.append(("+isbn:" + isbn).replaceAll(" ", "%20"));
    }

    private void connectServer() {
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=" + query;
            URL url = new URL(urlString);
            connection = url.openConnection();
            connection.setConnectTimeout(10000);
        } catch (IOException ex) {
            System.out.println("CHYBA: " + ex);
        }
    }

    private void downloadThumbnail() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String in;
            while ((in = br.readLine()) != null) {
                in = in.trim().replaceAll("\t", "");
                if (in.startsWith("\"thumbnail\":")) {
                    URLConnection c = new URL(in.substring(14, in.length() - 1).replaceAll("&zoom=1", "")).openConnection();
                    c.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.21; Mac_PowerPC)");
                    thumbnail = ImageIO.read(c.getInputStream());
                    return;
                }
            }
        } catch (IOException ex) {
            System.out.println("Chyba: " + ex);
        }
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setImageToTarget() {
        if (thumbnail != null) {
            thumbnail = thumbnail.getScaledInstance(dimW, -1, Image.SCALE_DEFAULT);
            thumbnail = thumbnail.getScaledInstance(-1, dimH, Image.SCALE_DEFAULT);
            target.setIcon(new ImageIcon(thumbnail));
        } else {
            target.setText("Náhled není k dispozici :-(");
        }
    }

    public int getDimW() {
        return dimW;
    }

    public void setDimW(int dimW) {
        this.dimW = dimW;
    }

    public int getDimH() {
        return dimH;
    }

    public void setDimH(int dimH) {
        this.dimH = dimH;
    }
}

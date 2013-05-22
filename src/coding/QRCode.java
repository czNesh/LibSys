package coding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Třída pro generování QR kódů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class QRCode {

    private static final int WIDTH = 300; // Šířka kódu
    private static final int HEIGHT = 300; // Výška kódu

    /**
     * Generuje jeden kód na základě vstupního řetězce
     *
     * @param input vstupní kód který bude přveden
     * @return vygenerovaný obrázek
     */
    public static BufferedImage encode(String input) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix;

        try {
            bitMatrix = writer.encode(input, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generuje kódy na základě vstupního pole řetězců
     *
     * @param input vstupní kódy které budou přvedeny
     * @return vygenerované obrázky
     */
    public static ArrayList<BufferedImage> encode(ArrayList<String> inputs) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix;
        ArrayList<BufferedImage> list = new ArrayList<>();

        try {
            for (String s : inputs) {
                bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
                list.add(MatrixToImageWriter.toBufferedImage(bitMatrix));
            }
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}

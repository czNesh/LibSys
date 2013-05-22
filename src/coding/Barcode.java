package coding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.UPCAWriter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Třída pro generování čárových kódů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class Barcode {

    private static final int WIDTH = 300; // Šířka kódu
    private static final int HEIGHT = 100; // Výška kódu

    /**
     * Generuje jeden kód na základě vstupního řetězce
     *
     * @param input vstupní kód který bude přveden
     * @return vygenerovaný obrázek
     */
    public static BufferedImage encode(String input) {
        UPCAWriter writer = new UPCAWriter();
        BitMatrix bitMatrix;

        try {
            bitMatrix = writer.encode(input + "00", BarcodeFormat.UPC_A, WIDTH, HEIGHT);
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
        UPCAWriter writer = new UPCAWriter();
        BitMatrix bitMatrix;
        ArrayList<BufferedImage> list = new ArrayList<>();

        try {
            for (String s : inputs) {
                bitMatrix = writer.encode(s + "00", BarcodeFormat.UPC_A, WIDTH, HEIGHT);
                list.add(MatrixToImageWriter.toBufferedImage(bitMatrix));
            }
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}

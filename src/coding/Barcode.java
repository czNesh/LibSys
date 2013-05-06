/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.UPCAWriter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Nesh
 */
public class Barcode {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;

    public static BufferedImage encode(String input){
        UPCAWriter writer = new UPCAWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(input+"00", BarcodeFormat.UPC_A, WIDTH, HEIGHT);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<BufferedImage> encode(ArrayList<String> inputs){
        UPCAWriter writer = new UPCAWriter();
        BitMatrix bitMatrix = null;
        ArrayList<BufferedImage> list = new ArrayList<>();
        try {
            for (String s : inputs) {
                bitMatrix = writer.encode(s+"00", BarcodeFormat.UPC_A, WIDTH, HEIGHT);
                list.add(MatrixToImageWriter.toBufferedImage(bitMatrix));
            }
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}

package io;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Třída starající s o soubory
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class FileManager {

    private static FileManager instance; // instance této třídy
    private String workSpace; // workspace

    /**
     * SINGLETON
     *
     * @return instance
     */
    public static FileManager getInstance() {
        synchronized (FileManager.class) {
            if (instance == null) {
                instance = new FileManager();
            }
        }
        return instance;
    }

    private FileManager() {
        // SINGLETON
    }

    /**
     * Uložení obrázku
     *
     * @param name jméno
     * @param in obrázek
     */
    public void saveImage(String name, BufferedImage in) {
        workSpace = Configuration.getInstance().getWorkspace();
        if (workSpace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "CHYBA: Není nastaven workspace", "Nastavit jej můžete v nasavení", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (in == null) {
            return;
        }
        try {
            File outputfile = new File(workSpace + "/" + name + ".png");
            ImageIO.write(in, "png", outputfile);
        } catch (IOException e) {
            ApplicationLog.getInstance().addMessage("Nepodařilo se uložit obrázek");
        }

    }

    /**
     * Otevření obrázku
     *
     * @param name jméno
     */
    public void openImage(String name) {
        workSpace = Configuration.getInstance().getWorkspace();

        try {
            File f = new File(workSpace + "/" + name + ".png");
            if (f.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(f);
                } else {
                    System.out.println("Soubor není podprován");
                }

            }
        } catch (Exception ert) {
        }
    }

    /**
     * Otevření složky
     *
     * @param path cesta
     *
     */
    public void open(String path) {
        workSpace = Configuration.getInstance().getWorkspace();

        try {
            File f = new File(workSpace + "/" + path);
            if (f.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(f);
                } else {
                    System.out.println("Soubor není podprován");
                }

            }
        } catch (Exception ert) {
        }
    }

    /**
     * Vytvoření složky
     * @param folderName jméno složky 
     */
    public void createDir(String folderName) {
        workSpace = Configuration.getInstance().getWorkspace();
        File theDir = new File(workSpace + "/" + folderName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}

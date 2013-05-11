/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Nesh
 */
public class FileManager {

    private static FileManager instance;
    private String workSpace;

    public static FileManager getInstance() {
        synchronized (FileManager.class) {
            if (instance == null) {
                instance = new FileManager();
            }
        }
        return instance;
    }

    private FileManager() {
    }

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

    public void createDir(String folderName) {
        workSpace = Configuration.getInstance().getWorkspace();
        File theDir = new File(workSpace + "/"+folderName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}

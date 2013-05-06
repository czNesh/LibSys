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
        workSpace = Configuration.getInstance().getWorkspace();
    }

    public void saveImage(String name, BufferedImage in) {
        if (workSpace.trim().isEmpty()) {
            ApplicationLog.getInstance().addMessage("Není nastavený workspace programu - nelze provést");
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
        if (workSpace.trim().isEmpty()) {
            ApplicationLog.getInstance().addMessage("Není nastavený workspace programu - nelze provést");
            return;
        }
        try {
            File f = new File(workSpace + "/" + name + ".png");
            if (f.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(f);
                } else {
                    System.out.println("Soubor neexistuje - nelze otevřít");
                }

            }
        } catch (Exception ert) {
            
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import coding.Barcode;
import coding.QRCode;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import helpers.DateFormater;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import models.entity.Author;
import models.entity.Book;

/**
 *
 * @author petr.hejhal
 */
public class PDFPrinter extends PdfPageEventHelper {
    
    private static PDFPrinter instance;
    Book book;
    
    public static PDFPrinter getInstance() {
        synchronized (PDFPrinter.class) {
            if (instance == null) {
                instance = new PDFPrinter();
            }
        }
        return instance;
    }
    
    private PDFPrinter() {
    }
    
    public void printBook(Book book) {
        try {
            this.book = book;
            createPdf("PDF_" + book.getBarcode());
        } catch (IOException | DocumentException ex) {
            System.out.println("VYJIMKA: " + ex);
        }
    }
    
    private void createPdf(String name) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 10, 10, 100, 10);
        PdfWriter.getInstance(document, new FileOutputStream(Configuration.getInstance().getWorkspace() + "\\" + name + ".pdf"));

        // open
        document.open();

        // write

        PdfPTable t = new PdfPTable(2);
        t.setTotalWidth(document.getPageSize().getWidth());
        t.getDefaultCell().setColspan(2);
        t.setWidths(new int[]{350, 1000});
        t.addCell(Image.getInstance(getClass().getResource("../images/header.jpg")));
        t.getDefaultCell().setColspan(1);

        // TITUL
        t.addCell(formatOutput("Kód knihy", 11, true));
        t.addCell(formatOutput(book.getBarcode(), 10, false));

        // TITUL
        t.addCell(formatOutput("Název / Titul", 11, true));
        t.addCell(formatOutput(book.getTitle(), 10, false));

        // AUTORI
        t.addCell(formatOutput("Autor/Autoři", 11, true));
        String temp = "";
        for (Author a : book.getAuthors()) {
            if (temp.isEmpty()) {
                temp += a.toString();
            } else {
                temp += "; " + a.toString();
            }
        }
        t.addCell(formatOutput(temp, 10, false));

        //ISBN 10
        t.addCell(formatOutput("ISBN10", 11, true));
        t.addCell(formatOutput(book.getISBN10(), 10, false));

        //ISBN 13
        t.addCell(formatOutput("ISBN13", 11, true));
        t.addCell(formatOutput(book.getISBN13(), 10, false));

        //Žánry
//        t.addCell(formatOutput("Žánry", 11, true));
//        String temp2 = "";
//        for (Genre g : book.getGenres()) {
//            if (temp2.isEmpty()) {
//                temp2 += g.toString();
//            } else {
//                temp2 += "; " + g.toString();
//            }
//        }
//        t.addCell(formatOutput(temp2, 10,false));

        //Počet stránek
        t.addCell(formatOutput("Počet stránek", 11, true));
        t.addCell(formatOutput(String.valueOf(book.getPageCount()), 10, false));

        //Jazyk
        t.addCell(formatOutput("Jazyk", 11, true));
        t.addCell(formatOutput(book.getLanguage(), 10, false));

        //Vydavatel
        t.addCell(formatOutput("Vydavatel", 11, true));
        t.addCell(formatOutput(book.getPublisher(), 10, false));

        //Vydáno roku
        t.addCell(formatOutput("Vydáno roku", 11, true));
        t.addCell(formatOutput(DateFormater.dateToString(book.getPublishedYear(), true), 10, false));

        //Sponzor
        t.addCell(formatOutput("Sponzor", 11, true));
        t.addCell(formatOutput(book.getSponsor(), 10, false));

        //Zakoupeno dne
        t.addCell(formatOutput("Zakoupeno dne", 11, true));
        t.addCell(formatOutput(DateFormater.dateToString(book.getAddedDate(), false), 10, false));

        //Zakoupeno dne
        t.addCell(formatOutput("Poznámky", 11, true));
        t.addCell(formatOutput(book.getNotes(), 10, false));

        // Vlozit tabulku
        document.add(t);

        // Tabulka pro kódy
        PdfPTable t2 = new PdfPTable(2);
        t2.setTotalWidth(document.getPageSize().getWidth());
        t2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        t2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
       
        java.awt.Image barcode = Barcode.encode(book.getBarcode());
        java.awt.Image qrcode = QRCode.encode(book.getBarcode());
        
        t2.addCell(Image.getInstance(barcode, null));
        t2.addCell(Image.getInstance(qrcode, null));
        document.add(t2);


        // close
        document.close();

        // open
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(Configuration.getInstance().getWorkspace() + "\\" + name + ".pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }
    
    private Phrase formatOutput(String in, int size, boolean bold) throws IOException, DocumentException {
        if (in == null) {
            return new Phrase("");
        }
        
        BaseFont bf;
        if (bold) {
            bf = BaseFont.createFont("timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } else {
            bf = BaseFont.createFont("times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
        Phrase p = new Phrase();
        p.setFont(new Font(bf, size));
        p.add(in);
        return p;
        
        
    }
}

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
import helpers.DateHelper;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import models.entity.Author;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;
import models.entity.Genre;
import services.BorrowService;

/**
 *
 * @author petr.hejhal
 */
public class PDFPrinter extends PdfPageEventHelper {

    private static PDFPrinter instance;

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
            Document document = new Document(PageSize.A4, 10, 10, 100, 10);
            PdfWriter.getInstance(document, new FileOutputStream(Configuration.getInstance().getWorkspace() + "\\PDF_BOOK_" + book.getBarcode() + ".pdf"));

            // open
            document.open();

            // write

            PdfPTable t = new PdfPTable(2);
            t.setTotalWidth(document.getPageSize().getWidth());
            t.getDefaultCell().setColspan(2);
            t.setWidths(new int[]{350, 1000});
            t.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t.addCell(Image.getInstance(getClass().getResource("../images/header.jpg")));
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(formatOutput("Záznam knihy", 12, true));
            t.getDefaultCell().setColspan(1);
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

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
            t.addCell(formatOutput("Žánry", 11, true));
            String temp2 = "";
            for (Genre g : book.getGenres()) {
                if (temp2.isEmpty()) {
                    temp2 += g.getGenre();
                } else {
                    temp2 += "; " + g.toString();
                }
            }
            t.addCell(formatOutput(temp2, 10, false));

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
            t.addCell(formatOutput(DateHelper.dateToString(book.getPublishedYear(), true), 10, false));

            //Sponzor
            t.addCell(formatOutput("Sponzor", 11, true));
            t.addCell(formatOutput(book.getSponsor(), 10, false));

            //Zakoupeno dne
            t.addCell(formatOutput("Zakoupeno dne", 11, true));
            t.addCell(formatOutput(DateHelper.dateToString(book.getAddedDate(), false), 10, false));

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
                    File myFile = new File(Configuration.getInstance().getWorkspace() + "\\" + "PDF_BOOK_" + book.getBarcode() + ".pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        } catch (IOException | DocumentException ex) {
            System.out.println("VYJIMKA: " + ex);
        }
    }

    public void printCustomer(Customer customer) {
        try {
            Document document = new Document(PageSize.A4, 10, 10, 100, 10);
            PdfWriter.getInstance(document, new FileOutputStream(Configuration.getInstance().getWorkspace() + "\\PDF_CUSTOMER_" + customer.getStringSSN() + ".pdf"));

            // open
            document.open();

            // write

            PdfPTable t = new PdfPTable(2);
            t.setTotalWidth(document.getPageSize().getWidth());
            t.getDefaultCell().setColspan(2);
            t.setWidths(new int[]{350, 1000});
            t.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t.addCell(Image.getInstance(getClass().getResource("../images/header.jpg")));
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(formatOutput("Záznam uživatele", 12, true));
            t.getDefaultCell().setColspan(1);
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

            // SSN
            t.addCell(formatOutput("Číslo uživatele", 11, true));
            t.addCell(formatOutput(customer.getStringSSN(), 10, false));

            // JMÉNO

            t.addCell(formatOutput("Jméno", 11, true));
            t.addCell(formatOutput(customer.getFullName(), 10, false));

            //EMAIL
            t.addCell(formatOutput("Email", 11, true));
            t.addCell(formatOutput(customer.getEmail(), 10, false));

            //TELEFON
            t.addCell(formatOutput("Telefon", 11, true));
            t.addCell(formatOutput(customer.getPhone(), 10, false));

            // ADRESA
            t.addCell(formatOutput("Adresa", 11, true));
            t.addCell(formatOutput(customer.getFormatedFullAdress(), 10, false));

            //POZNAMKY
            t.addCell(formatOutput("Poznámky", 11, true));
            t.addCell(formatOutput(customer.getNotes(), 10, false));

            // Vlozit tabulku
            document.add(t);

            // Tabulka pro kódy
            PdfPTable t2 = new PdfPTable(2);
            t2.setTotalWidth(document.getPageSize().getWidth());
            t2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            java.awt.Image barcode = Barcode.encode(customer.getStringSSN());
            java.awt.Image qrcode = QRCode.encode(customer.getStringSSN());

            t2.addCell(Image.getInstance(barcode, null));
            t2.addCell(Image.getInstance(qrcode, null));
            document.add(t2);


            // close
            document.close();

            // open
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(Configuration.getInstance().getWorkspace() + "\\PDF_CUSTOMER_" + customer.getStringSSN() + ".pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        } catch (IOException | DocumentException ex) {
            System.out.println("VYJIMKA: " + ex);
        }
    }

    public void printBorrow(Borrow borrow) {
        try {
            Document document = new Document(PageSize.A4, 10, 10, 100, 10);
            PdfWriter.getInstance(document, new FileOutputStream(Configuration.getInstance().getWorkspace() + "\\PDF_BORROW_" + borrow.getBorrowCode() + ".pdf"));

            // open
            document.open();

            // write

            PdfPTable t = new PdfPTable(2);
            t.setTotalWidth(document.getPageSize().getWidth());
            t.getDefaultCell().setColspan(2);
            t.setWidths(new int[]{350, 1000});
            t.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t.addCell(Image.getInstance(getClass().getResource("../images/header.jpg")));
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(formatOutput("Záznam půjčky", 12, true));
            t.getDefaultCell().setColspan(1);
            t.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

            // CISLO PUJCKY
            t.addCell(formatOutput("Číslo půjčky", 11, true));
            t.addCell(formatOutput(borrow.getBorrowCode(), 10, false));

            // ZAKAZNIK
            t.addCell(formatOutput("Zákazník", 11, true));
            t.addCell(formatOutput(borrow.getCustomer().getFullName(), 10, false));

            //KNIHOVNIK
            t.addCell(formatOutput("Knihovník", 11, true));
            t.addCell(formatOutput(borrow.getLibrarian().toString(), 10, false));

            //OD
            t.addCell(formatOutput("Od", 11, true));
            t.addCell(formatOutput(DateHelper.dateToString(borrow.getFromDate(), false), 10, false));

            // DO
            t.addCell(formatOutput("Do", 11, true));
            t.addCell(formatOutput(DateHelper.dateToString(borrow.getToDate(), false), 10, false));

            //POZNAMKY
            t.addCell(formatOutput("Poznámky", 11, true));
            t.addCell(formatOutput(borrow.getNotes(), 10, false));

            // POLOZKY
            t.addCell(formatOutput("Seznam položek", 11, true));
            String temp2 = "";
            for (Borrow b : BorrowService.getInstance().getBorrows(borrow.getBorrowCode())) {
                String returned = "";

                if (b.getReturned().equals(Byte.valueOf("1"))) {
                    returned = "VRÁCENO";
                } else {
                    returned = "NEVRÁCENO";
                }

                if (temp2.isEmpty()) {
                    temp2 += b.getItem().getTitle() + "(" + b.getItem().getBarcode() + ") - " + returned;
                } else {
                    temp2 += "\n" + b.getItem().getTitle() + "(" + b.getItem().getBarcode() + ") - " + returned;
                }
            }
            t.addCell(formatOutput(temp2, 10, false));

            // Vlozit tabulku
            document.add(t);

            // Tabulka pro kódy
            PdfPTable t2 = new PdfPTable(2);
            t2.setTotalWidth(document.getPageSize().getWidth());
            t2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            java.awt.Image barcode = Barcode.encode(borrow.getBorrowCode());
            java.awt.Image qrcode = QRCode.encode(borrow.getBorrowCode());

            t2.addCell(Image.getInstance(barcode, null));
            t2.addCell(Image.getInstance(qrcode, null));
            document.add(t2);


            // close
            document.close();

            // open
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(Configuration.getInstance().getWorkspace() + "\\PDF_BORROW_" + borrow.getBorrowCode() + ".pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        } catch (IOException | DocumentException ex) {
            System.out.println("VYJIMKA: " + ex);
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

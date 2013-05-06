/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import models.entity.Author;
import models.entity.Book;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import services.AuthorService;
import services.BookService;

/**
 *
 * @author Nesh
 */
public class ImportMARC21 {

    public static void main(String[] args) {

        try {
            String sourcePath = "c:/nk_data.xml";
            XMLReader parser = XMLReaderFactory.createXMLReader();
            InputSource source = new InputSource(sourcePath);
            parser.setContentHandler(new Parse());
            parser.parse(source);
            System.out.println("PARSING STARTED");

        } catch (SAXException | IOException e) {
            System.out.println("CHYBA");
        }
    }
}

class Parse implements ContentHandler {

    Locator locator;
    // init help variables
    Book tempBook;
    BookService bs = BookService.getInstance();
    Author tempAuthor = new Author();
    AuthorService as = AuthorService.getInstance();
    List<Author> tempAuthors = new ArrayList<>();
    Random rand = new Random();
    String tempString;
    // switch
    // Datafield
    boolean dISBN = false;
    boolean dTitle = false;
    boolean dAuthors = false;
    boolean dPageCount = false;
    boolean dLanguage = false;
    boolean dPublishInfo = false;
    // Subfield
    boolean readSubA = false;
    boolean readSubB = false;
    boolean readSubC = false;
    // info variables
    int addedBooks = 0;

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("ADDED BOOKS TOTAL:" + addedBooks);

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes atts) throws SAXException {
        // NEW BOOK
        if (qName.equals("record")) {
            tempBook = new Book();
            return;
        }

        if (dISBN || dTitle || dLanguage || dPageCount || dAuthors) {
            if (qName.equals("subfield") && atts.getValue(0).equals("a")) {
                readSubA = true;
            }
        }

        if (dPublishInfo) {
            if (qName.equals("subfield") && atts.getValue(0).equals("a")) {
                readSubA = true;
                return;
            }
            if (qName.equals("subfield") && atts.getValue(0).equals("b")) {
                readSubB = true;
                return;
            }
            if (qName.equals("subfield") && atts.getValue(0).equals("c")) {
                readSubC = true;
                return;
            }
            dPublishInfo = false;
        }



        // DATA FIELD CHECK
        if (qName.equals("datafield")) {
            // ISBN
            if (atts.getValue(0).equals("020")) {
                dISBN = true;
            }
            // language
            if (atts.getValue(0).equals("041")) {
                dLanguage = true;
            }
            // author
            if (atts.getValue(0).equals("100")) {
                dAuthors = true;
            }
            // title
            if (atts.getValue(0).equals("245")) {
                dTitle = true;
            }
            // publishInfo
            if (atts.getValue(0).equals("260")) {
                dPublishInfo = true;
            }

            //pageCount
            if (atts.getValue(0).equals("300")) {
                dPageCount = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("record")) {
            tempBook.setAddedDate(new Date());
            tempBook.setDeleted(false);
            tempBook.setSponsor("Národní knihovna České republiky");
            tempBook.setNotes("IMPORTOVANO Z DAT NARODNI KNIHOVNY V PRAZE");
            tempBook.setLocation("NK" + String.valueOf(rand.nextInt(20) + 1));

            if (tempBook.getMainAuthor() == null || tempBook.getMainAuthor().toString().isEmpty()) {
                tempAuthor = new Author("", "unknown");
                tempAuthors = new ArrayList<>();
                tempAuthors.add(tempAuthor);

                tempBook.setMainAuthor(tempAuthor);
                tempBook.setAuthors(tempAuthors);
            }

            int count = rand.nextInt(3) + 1; // save 1 - 3 book(s)
            bs.saveBook(tempBook, count);
            tempAuthor = null;
            tempAuthors = null;
            addedBooks++;

            System.out.println(">> PARSING LINE: " + locator.getLineNumber() + " / 1206473 - " + Double.valueOf(locator.getLineNumber() / 1206473.0 * 100) + "%");

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (readSubA) {
            // ISBN
            if (dISBN) {
                tempString = new String(ch, start, 19).replaceAll("[^\\dX]", "");

                if (tempString.length() < 10) {
                    tempBook.setISBN10("unknown");
                    tempBook.setISBN13("unknown");
                } else {
                    if (tempString.length() == 13) {
                        tempBook.setISBN13(tempString);
                        tempBook.setISBN10(tempString.substring(3, 12));
                    } else {
                        tempBook.setISBN10(tempString);
                        tempBook.setISBN13("978" + tempString);
                    }
                }
                dISBN = false;
            }

            // language
            if (dLanguage) {
                tempString = new String(ch, start, length);
                if (tempBook.getLanguage() == null) {
                    tempBook.setLanguage(tempString);
                } else {
                    tempBook.setLanguage(tempBook.getLanguage() + ", " + tempString);
                }
                dLanguage = false;
            }

            //title
            if (dTitle) {
                tempString = new String(ch, start, length).replaceAll("/", "").replaceAll(":", "").trim();
                tempBook.setTitle(tempString);
                dTitle = false;
            }

            //pageCount
            if (dPageCount) {
                tempString = new String(ch, start, length).replaceAll(" ;", "").trim();
                if (tempString.endsWith("s.")) {
                    tempString = tempString.replaceAll("[^\\dX]", "");
                    tempBook.setPageCount(Integer.parseInt(tempString));
                } else {
                    tempBook.setPageCount(-1);
                }

                dPageCount = false;
            }

            //authors
            if (dAuthors) {
                tempString = new String(ch, start, length);
                String[] parts = tempString.split(",");

                tempAuthor = new Author();
                tempAuthors = new ArrayList<>();

                if (parts.length == 1) {
                    tempAuthor.setLastName(parts[0].trim());
                }
                if (parts.length > 1) {
                    tempAuthor.setLastName(parts[0].trim());
                    tempAuthor.setFirstName(parts[1].trim());
                }
                if (parts.length == 0) {
                    dAuthors = false;
                    return;
                }

                tempAuthor = as.getOrCreate(tempAuthor);
                as.save(tempAuthor);
                tempBook.setMainAuthor(tempAuthor);
                tempAuthors.add(tempAuthor);

                tempBook.setAuthors(tempAuthors);

                tempAuthor = null;
                tempAuthors = null;
                dAuthors = false;
            }

            readSubA = false;
        }

        if (readSubB) {
            if (dPublishInfo) {
                tempString = new String(ch, start, length).replaceAll(";", "").trim();

                if (tempString.endsWith(",")) {
                    tempString = tempString.substring(0, tempString.length() - 1);
                }
                tempBook.setPublisher(tempString);
            }
            readSubB = false;
        }

        if (readSubC) {
            if (dPublishInfo) {
                tempString = new String(ch, start, length).replaceAll("[^\\d]", "");
                tempBook.setPublishedYear(new Date(Integer.valueOf(tempString), 1, 1));
            }
            readSubC = false;
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }
}
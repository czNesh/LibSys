/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Nesh
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String barcode;
    private String title;
    private Author mainAuthor;
    private List<Author> authors;
    private String ISBN10;
    private String ISBN13;
    private int pageCount;
    private String language;
    private String publisher;
    private Date publishedYear;
    private List<Genre> genres;
    private String sponsor;
    private Date addedDate;
    private String location;
    private int borrowed;
    private String notes;
    private String volumeCode;
    private boolean deleted;
    private Date inventoriedDate;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the mainAuthor
     */
    public Author getMainAuthor() {
        return mainAuthor;
    }

    /**
     * @param mainAuthor the mainAuthor to set
     */
    public void setMainAuthor(Author mainAuthor) {
        this.mainAuthor = mainAuthor;
    }

    /**
     * @return the authors
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /**
     * @return the ISBN10
     */
    public String getISBN10() {
        return ISBN10;
    }

    /**
     * @param ISBN10 the ISBN10 to set
     */
    public void setISBN10(String ISBN10) {
        this.ISBN10 = ISBN10;
    }

    /**
     * @return the ISBN13
     */
    public String getISBN13() {
        return ISBN13;
    }

    /**
     * @param ISBN13 the ISBN13 to set
     */
    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }

    /**
     * @return the pageCount
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * @param pageCount the pageCount to set
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the publishedYear
     */
    public Date getPublishedYear() {
        return publishedYear;
    }

    /**
     * @param publishedYear the publishedYear to set
     */
    public void setPublishedYear(Date publishedYear) {
        this.publishedYear = publishedYear;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * @return the sponsor
     */
    public String getSponsor() {
        return sponsor;
    }

    /**
     * @param sponsor the sponsor to set
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return the addedDate
     */
    public Date getAddedDate() {
        return addedDate;
    }

    /**
     * @param addedDate the addedDate to set
     */
    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getInventoriedDate() {
        return inventoriedDate;
    }

    public void setInventoriedDate(Date inventoriedDate) {
        this.inventoriedDate = inventoriedDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.title);
        hash = 37 * hash + Objects.hashCode(this.mainAuthor);
        hash = 37 * hash + Objects.hashCode(this.ISBN10);
        hash = 37 * hash + Objects.hashCode(this.ISBN13);
        hash = 37 * hash + Objects.hashCode(this.publisher);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.mainAuthor, other.mainAuthor)) {
            return false;
        }
        if (!Objects.equals(this.ISBN10, other.ISBN10)) {
            return false;
        }
        if (!Objects.equals(this.ISBN13, other.ISBN13)) {
            return false;
        }
        if (!Objects.equals(this.publisher, other.publisher)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * @return the volumeCode
     */
    public String getVolumeCode() {
        return volumeCode;
    }

    /**
     * @param volumeCode the volumeCode to set
     */
    public void setVolumeCode(String volumeCode) {
        this.volumeCode = volumeCode;
    }

    /**
     * @return the borrowed
     */
    public int getBorrowed() {
        return borrowed;
    }

    /**
     * @param borrowed the borrowed to set
     */
    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

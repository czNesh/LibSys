/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.type.DateType;

/**
 *
 * @author Nesh
 */
public class CatalogItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private DateType year;
    private DateType addedDate;
    private String sponsor;
    private String ISN;
    private int minAge;
    private String location;
    private boolean borrowed;
    private Author mainAuthor;
    private Set<Genre> genres = new HashSet<>();
    private Set<Author> authors = new HashSet<>();
    
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
     * @return the year
     */
    public DateType getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(DateType year) {
        this.year = year;
    }

    /**
     * @return the addedDate
     */
    public DateType getAddedDate() {
        return addedDate;
    }

    /**
     * @param addedDate the addedDate to set
     */
    public void setAddedDate(DateType addedDate) {
        this.addedDate = addedDate;
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
     * @return the ISN
     */
    public String getISN() {
        return ISN;
    }

    /**
     * @param ISN the ISN to set
     */
    public void setISN(String ISN) {
        this.ISN = ISN;
    }

    /**
     * @return the minAge
     */
    public int getMinAge() {
        return minAge;
    }

    /**
     * @param minAge the minAge to set
     */
    public void setMinAge(int minAge) {
        this.minAge = minAge;
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
     * @return the borrowed
     */
    public boolean isBorrowed() {
        return borrowed;
    }

    /**
     * @param borrowed the borrowed to set
     */
    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.title);
        hash = 83 * hash + Objects.hashCode(this.ISN);
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
        final CatalogItem other = (CatalogItem) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.ISN, other.ISN)) {
            return false;
        }
        return true;
    }

    /**
     * @return the genres
     */
    public Set<Genre> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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
    public Set<Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
 
}

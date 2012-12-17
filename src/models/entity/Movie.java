/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

import java.io.Serializable;

/**
 *
 * @author Nesh
 */
public class Movie extends CatalogItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private Medium medium;
    private int mediumCount;
    private int movieLength;

    /**
     * @return the medium
     */
    public Medium getMedium() {
        return medium;
    }

    /**
     * @param medium the medium to set
     */
    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    /**
     * @return the mediaCount
     */
    public int getMediumCount() {
        return mediumCount;
    }

    /**
     * @param mediumCount the mediumCount to set
     */
    public void setMediumCount(int mediumCount) {
        this.mediumCount = mediumCount;
    }

    /**
     * @return the movieLength
     */
    public int getMovieLength() {
        return movieLength;
    }

    /**
     * @param movieLength the movieLength to set
     */
    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    @Override
    public String toString() {
        return "Movie(" + getId() + "): " + getTitle();
    }
}

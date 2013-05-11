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
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String genre;
    private boolean deleted;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String toString() {
        return genre;
    }
}

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
    private GenreType genreType;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the genreType
     */
    public GenreType getGenreType() {
        return genreType;
    }

    /**
     * @param genreType the genreType to set
     */
    public void setGenreType(GenreType genreType) {
        this.genreType = genreType;
    }

}

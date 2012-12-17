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
public class Book extends CatalogItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private int pageCount;

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

    @Override
    public String toString() {
        return "Book(" + getId() + "): " + getTitle();
    }
}

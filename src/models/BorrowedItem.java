/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import org.hibernate.type.DateType;

/**
 *
 * @author Nesh
 */
public class BorrowedItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Borrow borrow;
    private CatalogItem item;
    private DateType fromDate;
    private DateType toDate;
    private boolean returned;
    private String description;

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
     * @return the borrow
     */
    public Borrow getBorrow() {
        return borrow;
    }

    /**
     * @param borrow the borrow to set
     */
    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    /**
     * @return the item
     */
    public CatalogItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(CatalogItem item) {
        this.item = item;
    }

    /**
     * @return the fromDate
     */
    public DateType getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(DateType fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public DateType getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(DateType toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the returned
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * @param returned the returned to set
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}

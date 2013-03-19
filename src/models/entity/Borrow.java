/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Nesh
 */
public class Borrow implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private SystemUser librarian;
    private Customer customer;
    private Book item;
    private Date fromDate;
    private Date toDate;
    private int returned;
    private String notes;
    private String borrowCode;

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
     * @return the librarian
     */
    public SystemUser getLibrarian() {
        return librarian;
    }

    /**
     * @param librarian the librarian to set
     */
    
    public void setLibrarian(SystemUser librarian) {
        this.librarian = librarian;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the description
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param description the description to set
     */
    public void setnotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the item
     */
    public Book getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Book item) {
        this.item = item;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the returned
     */
    public int isReturned() {
        return returned;
    }

    /**
     * @param returned the returned to set
     */
    public void setReturned(int returned) {
        this.returned = returned;
    }

    /**
     * @return the borrowCode
     */
    public String getBorrowCode() {
        return borrowCode;
    }

    /**
     * @param borrowCode the borrowCode to set
     */
    public void setBorrowCode(String borrowCode) {
        this.borrowCode = borrowCode;
    }
    
}

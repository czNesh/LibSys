/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nesh
 */
public class Borrow implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private SystemUser librarian;
    private Customer customer;
    private String description;
    private Set<Book> borrowedItems = new HashSet<>();

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
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the borrowedItems
     */
    public Set<Book> getBorrowedItems() {
        return borrowedItems;
    }

    /**
     * @param borrowedItems the borrowedItems to set
     */
    public void setBorrowedItems(Set<Book> borrowedItems) {
        this.borrowedItems = borrowedItems;
    }
}

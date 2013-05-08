/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Nesh
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
    private int SSN;
    private String street;
    private String city;
    private String country;
    private String postcode;
    private String email;
    private String phone;
    private String notes;
    private boolean deleted;
    /*VAZBY*/
    private List<Borrow> borrows;

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
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.email);
        hash = 79 * hash + Objects.hashCode(this.phone);
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
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the UCID
     */
    public int getSSN() {
        return SSN;
    }

    /**
     * @param UCID the UCID to set
     */
    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
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
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        String temp = "";
        if (firstName != null && !firstName.isEmpty()) {
            temp = firstName;
        }
        if (lastName != null && !lastName.isEmpty()) {
            if (temp.isEmpty()) {
                temp = lastName;
            } else {
                temp += " " + lastName;
            }

        }
        String ssnS = String.valueOf(SSN);
        if (ssnS != null && !ssnS.isEmpty()) {
            if (temp.isEmpty()) {
                temp = ssnS;
            } else {
                temp += " " + ssnS;
            }

        }
        return temp;
    }

    public String getFullName() {
        String temp = "";
        if (firstName != null && !firstName.isEmpty()) {
            temp = firstName;
        }
        if (lastName != null && !lastName.isEmpty()) {
            if (temp.isEmpty()) {
                temp = lastName;
            } else {
                temp += " " + lastName;
            }

        }
        return temp;
    }

    public String getFullAdress() {
        String temp = "";
        if (street != null && !street.isEmpty()) {
            temp = street;
        }
        if (city != null && !city.isEmpty()) {
            if (temp.isEmpty()) {
                temp = city;
            } else {
                temp += ", " + city;
            }

        }
        if (country != null && !country.isEmpty()) {
            if (temp.isEmpty()) {
                temp = country;
            } else {
                temp += " - " + country;
            }

        }

        return temp;
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

    public String getStringSSN() {
        return String.valueOf(SSN);
    }

    public String getFormatedFullAdress() {
        String temp = "";

        if (street != null && !street.isEmpty()) {
            temp = street + "\n";
        }

        if (postcode != null && !postcode.isEmpty()) {
            if (temp.isEmpty()) {
                temp = postcode + " - ";
            } else {
                temp += postcode + " - ";
            }

        }

        if (city != null && !city.isEmpty()) {
            if (temp.isEmpty()) {
                temp = city + "\n";
            } else {
                temp += city + "\n";
            }

        } else {
            temp += "\n";
        }

        if (country != null && !country.isEmpty()) {
            if (temp.isEmpty()) {
                temp = country + "\n";
            } else {
                temp += country + "\n";
            }
        }
        return temp;
    }
}

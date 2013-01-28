/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.io.Serializable;
import models.entity.Customer;

/**
 *
 * @author Nesh
 */
public class CustomerDAO extends BaseDAO<Customer> implements Serializable {

    private static CustomerDAO instance;

    public static CustomerDAO getInstance() {
        synchronized (CustomerDAO.class) {
            if (instance == null) {
                instance = new CustomerDAO();
            }
        }
        return instance;
    }

    private CustomerDAO() {
    }

    public void newCustomer(String fname, String lname, String street, String city, String postcode, String country, String email, String phone) {
        Customer c = new Customer();
        c.setFirstName(fname);
        c.setLastName(lname);
        c.setStreet(street);
        c.setCity(city);
        c.setCountry(country);
        c.setPostcode(postcode);
        c.setEmail(email);
        c.setPhone(phone);
        c.setUCID(getFreeSSN());
        
        save(c);
    }

    private int getFreeSSN() {
        while (true) {
            /* GET NUMBER */
            long timeSeed = System.nanoTime();
            double randSeed = Math.random() * 1000;
            long midSeed = (long) (timeSeed * randSeed);
            String s = midSeed + "";
            String subStr = s.substring(0, 9);
            int finalSeed = Integer.parseInt(subStr);
            
            /* CHECK IF FREE */
            getParameters().put("UCID", finalSeed);
            if (getUnique("UCID = :UCID") == null) {
                return finalSeed;
            }
        }
    }
}

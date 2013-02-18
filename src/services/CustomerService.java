/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Customer;

/**
 *
 * @author Nesh
 */
public class CustomerService extends BaseDAO<Customer> implements Serializable {

    private static CustomerService instance;
    private List<Customer> customersList;

    public static CustomerService getInstance() {
        synchronized (CustomerService.class) {
            if (instance == null) {
                instance = new CustomerService();
            } else {
            }
        }
        return instance;
    }

    private CustomerService() {
        customersList = getList();
    }

    private int getFreeSSN() {
        while (true) {
            // vygeneruje SSN
            long timeSeed = System.nanoTime();
            double randSeed = Math.random() * 1000;
            long midSeed = (long) (timeSeed * randSeed);
            String s = "5" + midSeed;
            String subStr = s.substring(0, 9);
            int finalSeed = Integer.parseInt(subStr);

            // volné SSN ?
            getParameters().put("SSN", finalSeed);
            if (getUnique("SSN = :SSN") == null) {
                return finalSeed;
            }
        }
    }

    public void saveCustomer(Customer c) {
        c.setSSN(getFreeSSN());
        customersList.add(c);
        save(c);
    }

    public List<Customer> getCustomers() {
        return customersList;
    }

    public List<Customer> getFilteredList(String ssn, String fname, String lname, String email, String phone) {
        StringBuilder conditionStringBuilder = new StringBuilder();

        if (ssn != null && !ssn.isEmpty()) {
            conditionStringBuilder.append("SSN = :SSN");
            getParameters().put("SSN", ssn);
        }

        if (fname != null && !fname.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("firstName = :firstName");
            getParameters().put("firstName", fname);
        }

        if (lname != null && !lname.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("lastName = :lastName");
            getParameters().put("lastName", lname);
        }

        if (email != null && !email.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("email = :email");
            getParameters().put("email", email);
        }

        if (phone != null && !phone.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("phone = :phone");
            getParameters().put("phone", phone);
        }

        if (conditionStringBuilder.length() > 0) {
            setCondition(conditionStringBuilder.toString());
        }
        return getList();

    }
}

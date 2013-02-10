/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
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
            }else{
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
            String s = midSeed + "";
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
}

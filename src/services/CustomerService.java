/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.RefreshController;
import helpers.DateFormater;
import io.ApplicationLog;
import io.Configuration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.dao.BaseDAO;
import models.entity.Customer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Nesh
 */
public class CustomerService extends BaseDAO<Customer> implements Serializable {

    private static CustomerService instance;

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
        // SINGLETON
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
        c.setDeleted(false);
        create(c);
        RefreshController.getInstance().refreshCustomerTab();
        ApplicationLog.getInstance().addMessage("Zákazník byl úspěšně přidán do systému (" + c.getFullName() + ")");
    }

    public List<Customer> getCustomers() {
        if (!Configuration.getInstance().isDeletedItemVisible()) {
            getParameters().put("false", false);
            setCondition("deleted = :false");
        }
        return getList();
    }

    public List<Customer> getFilteredList(String ssn, String fname, String lname, String email, String phone) {
        StringBuilder conditionStringBuilder = new StringBuilder();
        getParameters().clear();
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

    @Override
    public void delete(Customer c) {
        c.setDeleted(true);
        save(c);
        ApplicationLog.getInstance().addMessage("Smazán uživatel (" + c.getFullName() + ")");
        RefreshController.getInstance().refreshCustomerTab();
    }

    public List<String> criteriaSearch(String in) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Customer.class);
        Disjunction d = Restrictions.disjunction();
        try {
            d.add(Restrictions.eq("SSN", Integer.getInteger(in)));
        } catch (NumberFormatException e) {
            // DO OTHING SKIP
        }
        d.add(Restrictions.eq("postcode", in));
        d.add(Restrictions.ilike("firstName", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("lastName", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("city", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("street", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("country", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("email", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("phone", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("notes", in, MatchMode.ANYWHERE));
        c.add(d);
        c.setProjection(Projections.projectionList().add(Property.forName("id")));
        List<String> result = c.list();

        closeSession();
        return result;
    }

    public Map<String, String> getOrderByMap() {
        Map<String, String> out = new HashMap<>();
        out.put("ID", "id");
        out.put("Přijmení", "lastName");
        out.put("Email", "email");
        out.put("Adresa", "street");
        return out;
    }

    public List<String> extendedCriteriaSearch(String ssn, String fname, String lname, String email, String phone) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Customer.class);

        Disjunction d = Restrictions.disjunction();
        boolean isRestrictionSet = false;
        if (ssn != null && !ssn.isEmpty()) {
            try {
                d.add(Restrictions.eq("SSN", Integer.getInteger(ssn)));
            } catch (NumberFormatException e) {
                // DO OTHING SKIP
            }
            isRestrictionSet = true;
        }
        if (fname != null && !fname.isEmpty()) {
            d.add(Restrictions.ilike("firstName", fname, MatchMode.ANYWHERE));
            isRestrictionSet = true;
        }
        if (lname != null && !lname.isEmpty()) {
            d.add(Restrictions.ilike("lastName", lname, MatchMode.ANYWHERE));
            isRestrictionSet = true;
        }
        if (email != null && !email.isEmpty()) {
            d.add(Restrictions.ilike("email", email, MatchMode.ANYWHERE));
            isRestrictionSet = true;
        }
        if (phone != null && !phone.isEmpty()) {
            d.add(Restrictions.ilike("phone", phone, MatchMode.ANYWHERE));
            isRestrictionSet = true;
        }
        if (!isRestrictionSet) {
            closeSession();
            return new ArrayList<>();
        }
        c.add(d);

        c.setProjection(Projections.projectionList().add(Property.forName("id")));
        List<String> result = c.list();

        closeSession();
        return result;
    }

    public List<Customer> findCustomers(String in) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Customer.class);
        Disjunction d = Restrictions.disjunction();

        d.add(Restrictions.ilike("firstName", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("lastName", in, MatchMode.ANYWHERE));

        c.add(d);

        List<Customer> result = c.list();

        closeSession();
        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.AppController;
import io.ApplicationLog;
import controllers.RefreshController;
import helpers.DateFormater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.dao.BaseDAO;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;
import models.entity.SystemUser;
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
public class BorrowService extends BaseDAO<Borrow> {

    private static BorrowService instance;

    public static BorrowService getInstance() {
        synchronized (BorrowService.class) {
            if (instance == null) {
                instance = new BorrowService();
            }
        }
        return instance;
    }

    private String getFreeBorrowedCode() {
        while (true) {
            // vygeneruje SSN
            long timeSeed = System.nanoTime();
            double randSeed = Math.random() * 1000;
            long midSeed = (long) (timeSeed * randSeed);
            String s = "3" + midSeed;
            String subStr = s.substring(0, 9);
            int finalSeed = Integer.parseInt(subStr);

            // volné SSN ?
            getParameters().clear();
            getParameters().put("borrowCode", finalSeed);
            if (getUnique("borrowCode = :borrowCode") == null) {
                return String.valueOf(finalSeed);
            }
        }
    }

    public void newBorrow(Borrow b, List<Book> booksList) {
        b.setBorrowCode(getFreeBorrowedCode());
        b.setDeleted(false);
        for (Book book : booksList) {
            b.setItem(book);
            b.setLibrarian(AppController.getInstance().getLoggedUser());
            create(b);
        }
        RefreshController.getInstance().refreshBorrowTab();
        ApplicationLog.getInstance().addMessage("Nové vypůjčení uloženo");
    }

    public List<Borrow> getBorrows() {
        setGroupBy("borrowCode");
        return getList();
    }

    public List<Borrow> getFilteredList() {
        return getList();
    }

    @Override
    public void delete(Borrow b) {
        b.setDeleted(true);
        save(b);
        ApplicationLog.getInstance().addMessage("Smazána půjčka (" + b.getBorrowCode() + ")");
        RefreshController.getInstance().refreshCustomerTab();
    }

    public List<Borrow> getBorrowsOfBook(Book b) {
        getParameters().clear();
        getParameters().put("item_id", b.getId());
        setCondition("item_id = :item_id");
        return getList();
    }

    public List<Book> getBooksOfBorrow(String borrowCode) {
        getParameters().clear();
        getParameters().put("borrowCode", borrowCode);
        setCondition("borrowCode = :borrowCode");
        List<Book> list = new ArrayList<>();
        for (Borrow b : getList()) {
            list.add(b.getItem());
        }
        return list;
    }

    public boolean isAllReturned(String borrowCode) {
        openSession();
        int sum;
        try {
            sum = ((Long) getSession().createQuery("SELECT SUM(b.returned) FROM Borrow b WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        } catch (NullPointerException e) {
            return false;
        }

        int count = ((Long) getSession().createQuery("SELECT COUNT(b.id) FROM Borrow b WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        closeSession();

        return (sum == count) ? true : false;
    }

    public void returnBorrows(ArrayList<Borrow> borrowlist) {
    }

    public List<Borrow> getBorrowsOfCustomer(Long customerID) {
        getParameters().put("customer_id", customerID);
        setCondition("customer_id = :customer_id");
        setGroupBy("borrowCode");
        return getList();
    }

    public List<Borrow> activeBorrowsOfCustomer(Customer c) {
        getParameters().put("customer_id", c.getId());
        getParameters().put("returned", false);
        setCondition("customer_id = :customer_id AND returned = :returned");
        return getList();
    }

    public List<String> criteriaSearch(String in) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Customer.class);
        Disjunction d = Restrictions.disjunction();
        d.add(Restrictions.eq("borrowCode", in));
        d.add(Restrictions.in("librarian", SystemUserService.getInstance().findSystemUsers(in)));
        d.add(Restrictions.in("customer", CustomerService.getInstance().findCustomers(in)));
        d.add(Restrictions.in("item", BookService.getInstance().findBooks(in)));
        d.add(Restrictions.eq("fromDate", DateFormater.stringToDate(in, false)));
        d.add(Restrictions.eq("toDate", DateFormater.stringToDate(in, false)));

        if (in.toLowerCase().contains("vracene") || in.toLowerCase().contains("vrácené")) {
            d.add(Restrictions.eq("returned", true));
        }
        if (in.toLowerCase().contains("nevracene") || in.toLowerCase().contains("nevrácené")) {
            d.add(Restrictions.eq("returned", false));
            d.add(Restrictions.isNull("returned"));
        }
        c.add(d);
        c.setProjection(Projections.projectionList().add(Property.forName("id")));
        List<String> result = c.list();

        closeSession();
        return result;
    }

    public Map<String, String> getOrderByMap() {
        Map<String, String> out = new HashMap<>();
        out.put("ID", "id");
        out.put("Zákazník", "cutomer");
        out.put("Vráceno", "returned");
        out.put("Od", "fromDate");
        out.put("Do", "toDate");
        return out;
    }

    public List<String> extendedCriteriaSearch(String borrowCode, String customer, String librarian, String item, String from, String to, int returned) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Customer.class);

        Disjunction d = Restrictions.disjunction();
        boolean isRestrictionSet = false;
        if (borrowCode != null && !borrowCode.isEmpty()) {
            d.add(Restrictions.eq("borrowCode", borrowCode));
            isRestrictionSet = true;
        }

        if (customer != null && !customer.isEmpty()) {
            List<Customer> tempCustomers = CustomerService.getInstance().findCustomers(customer);
            if (tempCustomers != null && !tempCustomers.isEmpty()) {
                d.add(Restrictions.in("customer", tempCustomers));
                isRestrictionSet = true;
            }
        }

        if (librarian != null && !librarian.isEmpty()) {
            List<SystemUser> tempSystemUsers = SystemUserService.getInstance().findSystemUsers(librarian);
            if (tempSystemUsers != null && !tempSystemUsers.isEmpty()) {
                d.add(Restrictions.in("llibrarian", tempSystemUsers));
                isRestrictionSet = true;
            }
        }

        if (item != null && !item.isEmpty()) {
            List<Book> tempBooks = BookService.getInstance().findBooks(item);
            if (tempBooks != null && !tempBooks.isEmpty()) {
                d.add(Restrictions.in("item", tempBooks));
                isRestrictionSet = true;
            }
        }

        if (from != null && !from.isEmpty()) {
            d.add(Restrictions.eq("fromDate", DateFormater.stringToDate(from, true)));
            isRestrictionSet = true;
        }
        if (to != null && !to.isEmpty()) {
            d.add(Restrictions.eq("publishedYear", DateFormater.stringToDate(to, true)));
            isRestrictionSet = true;
        }
        if (!isRestrictionSet) {
            closeSession();
            return new ArrayList<>();
        }

        switch (returned) {
            case -1:
                c.add(Restrictions.eq("returned", false));
                break;
            case 1:
                c.add(Restrictions.eq("returned", true));
                break;
            default:
                // NO FILTER BY RETURNED
                break;
        }

        c.add(d);

        c.setProjection(Projections.projectionList().add(Property.forName("id")));
        List<String> result = c.list();

        closeSession();
        return result;
    }
}

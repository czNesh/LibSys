/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import coding.Barcode;
import controllers.AppController;
import io.ApplicationLog;
import controllers.RefreshController;
import helpers.DateHelper;
import java.util.ArrayList;
import java.util.Date;
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
            book.setBorrowed(1);
            BookService.getInstance().save(book);

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
        getParameters().put("item_id", b.getId());
        setCondition("item_id = :item_id");
        setLimit(20);
        return getList();
    }

    public List<Book> getBooksOfBorrow(String borrowCode) {
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

    public void returnBorrows(List<Borrow> borrowlist) {
        for (Borrow b : borrowlist) {
            b.setDeleted(true);
            b.setReturned(Byte.valueOf("1"));
            Book book = b.getItem();
            book.setBorrowed(0);
            BookService.getInstance().save(book);
            save(b);
        }
    }

    public List<Borrow> getBorrowsOfCustomer(Long customerID) {
        getParameters().put("customer", customerID);
        setCondition("customer = :customer");
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
        Criteria c = s.createCriteria(Borrow.class);
        Disjunction d = Restrictions.disjunction();
        d.add(Restrictions.eq("borrowCode", in));
        d.add(Restrictions.in("librarian", SystemUserService.getInstance().findSystemUsers(in)));
        d.add(Restrictions.in("customer", CustomerService.getInstance().findCustomers(in)));
        d.add(Restrictions.in("item", BookService.getInstance().findBooks(in)));
        d.add(Restrictions.eq("fromDate", DateHelper.stringToDate(in, false)));
        d.add(Restrictions.eq("toDate", DateHelper.stringToDate(in, false)));

        if (in.toLowerCase().equals("vracene") || in.toLowerCase().equals("vrácené")) {
            d.add(Restrictions.eq("returned", Byte.valueOf("1")));
        }
        if (in.toLowerCase().equals("nevracene") || in.toLowerCase().equals("nevrácené")) {
            d.add(Restrictions.eq("returned", Byte.valueOf("0")));
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
        out.put("Od", "fromDate");
        out.put("Do", "toDate");
        out.put("Vráceno", "returned");

        return out;
    }

    public List<String> extendedCriteriaSearch(String borrowCode, String customer, String librarian, String item, String from, String to, int returned) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Borrow.class);

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
                d.add(Restrictions.in("librarian", tempSystemUsers));
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
            d.add(Restrictions.eq("fromDate", DateHelper.stringToDate(from, false)));
            isRestrictionSet = true;
        }
        if (to != null && !to.isEmpty()) {
            d.add(Restrictions.eq("toDate", DateHelper.stringToDate(to, false)));
            isRestrictionSet = true;
        }


        switch (returned) {
            case 1:
                c.add(Restrictions.eq("returned", Byte.valueOf("1")));
                isRestrictionSet = true;
                break;
            case 2:
                c.add(Restrictions.or(Restrictions.eq("returned", Byte.valueOf("0")), Restrictions.isNull("returned")));
                isRestrictionSet = true;
                break;

            default:
                // NO FILTER BY RETURNED
                break;
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

    public int getReturned(String borrowCode) {
        openSession();
        int count = 0;
        try {
            count = ((Long) getSession().createQuery("SELECT SUM(b.returned) FROM Borrow b WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        } catch (NullPointerException e) {
            return 0;
        }

        closeSession();
        return count;
    }

    public int getCount(String borrowCode) {
        openSession();
        int count = ((Long) getSession().createQuery("SELECT COUNT(*) FROM Borrow WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        closeSession();
        return count;
    }

    public List<Borrow> getBorrows(String borrowCode) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Borrow.class);
        c.add(Restrictions.eq("borrowCode", borrowCode));
        List<Borrow> result = c.list();
        closeSession();
        return result;
    }

    public boolean isBookReturned(Book i, String borrowCode) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Borrow.class);
        c.add(Restrictions.eq("borrowCode", borrowCode));
        c.add(Restrictions.eq("item", i));
        c.add(Restrictions.eq("returned", Byte.valueOf("1")));
        List<Borrow> result = c.list();
        closeSession();
        return result.isEmpty();
    }

    public void returnBorrows(List<Book> tempList, String borrowCode) {
        List<Borrow> borrows = getBorrows(borrowCode);
        for (Borrow b : borrows) {
            if (tempList.contains(b.getItem())) {
                b.setReturned(Byte.valueOf("1"));
                Book book = b.getItem();
                book.setBorrowed(0);
                BookService.getInstance().save(book);
                save(b);
            }
        }
    }

    public void notReturnBorrows(List<Book> tempList, String borrowCode) {
        List<Borrow> borrows = getBorrows(borrowCode);
        for (Borrow b : borrows) {
            if (tempList.contains(b.getItem())) {
                b.setReturned(Byte.valueOf("0"));
                Book book = b.getItem();
                book.setBorrowed(1);
                BookService.getInstance().save(book);
                save(b);
            }
        }
    }

    public List<Borrow> getBorrows(List<Book> books, String borrowCode) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Borrow.class);
        c.add(Restrictions.eq("borrowCode", borrowCode));
        c.add(Restrictions.in("item", books));
        List<Borrow> result = c.list();
        closeSession();
        return result;
    }
    
    public void splitBorrows(Date from,Date to,List<Borrow> tempBorrow){
        String newBorrowCode = getFreeBorrowedCode();
        for(Borrow b : tempBorrow){
            b.setFromDate(from);
            b.setToDate(to);
            b.setBorrowCode(newBorrowCode);
            save(b);
        }
    }
}

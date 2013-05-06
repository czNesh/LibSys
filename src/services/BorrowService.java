/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.AppController;
import io.ApplicationLog;
import controllers.RefreshController;
import java.util.ArrayList;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;

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

    public boolean isAllReturned(String borrowCode){
        openSession();
        int sum = ((Long) getSession().createQuery("SELECT SUM(b.returned) FROM Borrow b WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        int count = ((Long) getSession().createQuery("SELECT COUNT(b.id) FROM Borrow b WHERE borrowCode = :borrowCode").setParameter("borrowCode", borrowCode).uniqueResult()).intValue();
        closeSession();
        
        return (sum == count)? true : false;
    }
    
    public void returnBorrows(ArrayList<Borrow> borrowlist){
        
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
    
}

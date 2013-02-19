/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import models.entity.Borrow;

/**
 *
 * @author Nesh
 */
public class BorrowService {

    private static BorrowService instance;
    private List<Borrow> borrows;

    public static BorrowService getInstance() {
        synchronized (BorrowService.class) {
            if (instance == null) {
                instance = new BorrowService();
            }
        }
        return instance;
    }
}

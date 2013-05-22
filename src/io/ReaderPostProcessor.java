package io;

import controllers.BookDetailController;
import controllers.BorrowDetailController;
import controllers.CustomerDetailController;
import java.awt.KeyEventPostProcessor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;
import services.BookService;
import services.BorrowService;
import services.CustomerService;

/**
 *
 * @author Nesh
 */
public class ReaderPostProcessor implements KeyEventPostProcessor {

    private StringBuilder input = new StringBuilder();
    List<Character> allowedNums, allowedChars;

    public ReaderPostProcessor() {
        allowedNums = new ArrayList<>();
        allowedNums.add('0');
        allowedNums.add('1');
        allowedNums.add('2');
        allowedNums.add('3');
        allowedNums.add('4');
        allowedNums.add('5');
        allowedNums.add('6');
        allowedNums.add('7');
        allowedNums.add('8');
        allowedNums.add('9');
        allowedChars = new ArrayList<>();
        allowedChars.add('é');
        allowedChars.add('+');
        allowedChars.add('ě');
        allowedChars.add('š');
        allowedChars.add('č');
        allowedChars.add('ř');
        allowedChars.add('ž');
        allowedChars.add('ý');
        allowedChars.add('á');
        allowedChars.add('í');
    }

    @Override
    public boolean postProcessKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_RELEASED) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                selectAction();
            }

            // DELETE IN AFTER 500 ms
            new DeleteCode().start();

            if (allowedNums.contains(e.getKeyChar())) {
                input.append(e.getKeyChar());
            }
            if (allowedChars.contains(e.getKeyChar())) {
                input.append(allowedChars.indexOf(e.getKeyChar()));
            }

            return true;
        }

        return false;
    }

    /**
     * 3 - pujcka 5 - kniha 7 - zakaznik
     */
    private void selectAction() {

        String code = input.toString().trim();
        System.out.println(input.length());
        if (input.length() < 9) {
            return;
        }

        // Půjčka
        if (code.startsWith("3")) {
            List<Borrow> borrows = BorrowService.getInstance().getBorrows(code);
            if (borrows != null && !borrows.isEmpty()) {
                ApplicationLog.getInstance().addMessage("Čtečka: Půjčka rozpoznána - " + borrows.get(0).getCustomer().getFullName() + " (" + borrows.get(0).getBorrowCode() + ")");
            }
            BorrowDetailController brwdc = new BorrowDetailController(borrows.get(0));
            brwdc.showView();
        }

        // Kniha
        if (code.startsWith("5")) {
            Book b = BookService.getInstance().getBookWithCode(code);
            if (b != null) {
                ApplicationLog.getInstance().addMessage("Čtečka: Kniha rozpoznána - " + b.getTitle() + " (" + b.getBarcode() + ")");
            }
            BookDetailController bdc = new BookDetailController(b);
            bdc.showView();
        }

        //Zákazník
        if (code.startsWith("7")) {
            Customer c = CustomerService.getInstance().getCustomerWithCode(code);
            if (c != null) {
                ApplicationLog.getInstance().addMessage("Čtečka: Zákazník rozpoznán - " + c.getFullName() + " (" + c.getSSN() + ")");
            }
            CustomerDetailController cdc = new CustomerDetailController(c);
            cdc.showView();
        }
    }

    private class DeleteCode extends Thread {

        @Override
        public void run() {
            try {
                sleep(500);
                input = new StringBuilder();

            } catch (InterruptedException ex) {
                System.err.println("CHYBA READER:" + ex);
            }
        }
    }
}

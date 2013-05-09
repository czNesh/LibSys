/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import views.BorrowSplitDialog;
import views.DatePicker;

/**
 *
 * @author Nesh
 */
public class BorrowSplitController extends BaseController {

    BorrowSplitDialog dialog;
    private boolean datesSet;
    private Date from, to;

    public BorrowSplitController() {
        dialog = new BorrowSplitDialog(null, true);
        datesSet = false;
        initListeners();
    }

    private void initListeners() {
        BorrowSplitActionListener a = new BorrowSplitActionListener();
        dialog.getBTNok().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNfrom().addActionListener(a);
        dialog.getBTNto().addActionListener(a);
    }

    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    boolean isDatesSet() {
        return datesSet;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public void setFrom(Date from) {
        this.from = from;
        dialog.getINPfrom().setText(DateHelper.dateToString(from, false));
    }

    public void setTo(Date to) {
        dialog.getINPto().setText(DateHelper.dateToString(to, false));
        this.to = to;
    }

    private class BorrowSplitActionListener implements ActionListener {

        public BorrowSplitActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "ok":
                    StringBuilder validationLog = new StringBuilder();
                    from = DateHelper.stringToDate(dialog.getINPfrom().getText(), false);
                    to = DateHelper.stringToDate(dialog.getINPto().getText(), false);

                    if (from == null) {
                        validationLog.append("Datum (od) nemá správný tvar\n");
                    }
                    if (to == null) {
                        validationLog.append("Datum (do) nemá správný tvar\n");
                    }

                    if (!DateHelper.compareGE(to, from)) {
                        validationLog.append("Vypůjčka musí být minimálně na jeden den\n");
                    }
                    if (validationLog.length() > 0) {
                        JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
                    } else {
                        datesSet = true;
                        dispose();
                    }
                    break;
                case "cancel":
                    dispose();
                    break;
                case "dateFrom":
                    DatePicker dp = new DatePicker(null, true);
                    dp.setLocationRelativeTo(null);
                    dp.setVisible(true);

                    if (dp.getDate() != null) {
                        Date d = dp.getDate();
                        dialog.getINPfrom().setText(DateHelper.dateToString(d, false));
                    }
                    break;
                case "dateTo":
                    DatePicker dp2 = new DatePicker(null, true);
                    dp2.setLocationRelativeTo(null);
                    dp2.setVisible(true);

                    if (dp2.getDate() != null) {
                        Date d2 = dp2.getDate();
                        dialog.getINPto().setText(DateHelper.dateToString(d2, false));
                    }
                    break;
            }
        }
    }
}

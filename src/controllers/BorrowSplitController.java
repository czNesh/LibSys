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
 * Třída (controller) starající se o prozdělení výpůjček
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class BorrowSplitController extends BaseController {

    BorrowSplitDialog dialog; // připojený pohled
    private boolean datesSet; // indikace zda jsou datumy nastaveny
    private Date from, to; // Datumy od a do

    /**
     * Třídní konstruktor
     */
    public BorrowSplitController() {
        dialog = new BorrowSplitDialog(null, true);
        datesSet = false;
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        BorrowSplitActionListener a = new BorrowSplitActionListener();
        dialog.getBTNok().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNfrom().addActionListener(a);
        dialog.getBTNto().addActionListener(a);
    }

    /**
     * Vrací zda jsou nastavené datumy
     *
     * @return indikace zda jsou data nastaveny
     */
    boolean isDatesSet() {
        return datesSet;
    }

    /**
     * Vrací datum od kdy je půjčka aktivní
     *
     * @return datum od
     */
    public Date getFrom() {
        return from;
    }

    /**
     * Vrací datum do kdy je půjčka
     *
     * @return datum do
     */
    public Date getTo() {
        return to;
    }

    /**
     * Nastaví datum od kdy je půjčka aktivní
     *
     * @param from datum od
     */
    public void setFrom(Date from) {
        this.from = from;
        dialog.getINPfrom().setText(DateHelper.dateToString(from, false));
    }

    /**
     * Nastaví datum do kdy je půjčka aktivní
     *
     * @param to datum do
     */
    public void setTo(Date to) {
        dialog.getINPto().setText(DateHelper.dateToString(to, false));
        this.to = to;
    }

    /**
     * Změní termín půjček
     */
    private void confirmTermChange() {
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
    }

    /**
     * Nataví datum od pomocí DatePickeru
     */
    private void setDateFrom() {
        DatePicker dp = new DatePicker(null, true);
        dp.setLocationRelativeTo(null);
        dp.setVisible(true);

        if (dp.getDate() != null) {
            Date d = dp.getDate();
            dialog.getINPfrom().setText(DateHelper.dateToString(d, false));
        }
    }

    /**
     * Nastaví datum do pomocí DatePickeru
     */
    private void setDateTo() {
        DatePicker dp2 = new DatePicker(null, true);
        dp2.setLocationRelativeTo(null);
        dp2.setVisible(true);

        if (dp2.getDate() != null) {
            Date d2 = dp2.getDate();
            dialog.getINPto().setText(DateHelper.dateToString(d2, false));
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class BorrowSplitActionListener implements ActionListener {

        public BorrowSplitActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "ok":
                    confirmTermChange();
                    break;
                case "cancel":
                    dispose();
                    break;
                case "dateFrom":
                    setDateFrom();
                    break;
                case "dateTo":
                    setDateTo();
                    break;
            }
        }
    }
}

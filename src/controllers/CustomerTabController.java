package controllers;

import coding.Barcode;
import coding.QRCode;
import helpers.DateHelper;
import io.Configuration;
import io.FileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.CustomerTableModel;
import models.entity.Customer;
import services.BorrowService;
import services.CustomerService;
import views.CustomerFilterDialog;
import views.CustomerSearchDialog;
import views.MainView;

/**
 * Třída (controller) starající se o záložku zákazníku na hlavním pohledu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class CustomerTabController {

    MainView mainView; // hlavní pohled
    CustomerTableModel tableModel; // tabulka zákazníků
    CustomerFilterDialog filter; // filtr zobrazení
    CustomerSearchDialog csd; // dialog pro vyhledávání
    // ŘAZENÍ
    Map<String, String> orderType = CustomerService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = CustomerService.getInstance().getOrderByMap();

    /**
     * Třídní konstruktor
     *
     * @param mainView hlavní pohled
     */
    CustomerTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        //TableModel
        tableModel = new CustomerTableModel();
        mainView.getCustomerTable().setModel(tableModel);

        //FilterDialog
        filter = new CustomerFilterDialog(null, true);
        setFilterData();

        // Customer Search Dialog
        csd = new CustomerSearchDialog(mainView, true);

        // INIT LISTENERS
        initListeners();

        //updateView
        updateView();
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        // MouseListener
        CustomerTabMouseListener m = new CustomerTabMouseListener();
        mainView.getCustomerTable().addMouseListener(m);

        //ActionListener
        CustomerTabActionListener a = new CustomerTabActionListener();
        mainView.getCustomerTableNextButton().addActionListener(a);
        mainView.getCustomerTablePrevButton().addActionListener(a);
        mainView.getCustomerFilterButton().addActionListener(a);
        mainView.getBTNsearch().addActionListener(a);
        mainView.getBTNstopCustomerSearch().addActionListener(a);
        mainView.getBarcodeButton().addActionListener(a);
        mainView.getQrcodeButton().addActionListener(a);

        //KeyListener
        CustomerTabKeyListener k = new CustomerTabKeyListener();
        mainView.getCustomerTableInputNumber().addKeyListener(k);
        mainView.getCustomerFilterInput().addKeyListener(k);
        mainView.getCustomerTable().addKeyListener(k);

        //FILTER listener 
        filter.getBTNok().addActionListener(a);

        //SEARCH listener
        csd.getBTNsearch().addActionListener(a);
        csd.getBTNcloseSearchDialog().addActionListener(a);
        csd.getBTNreset().addActionListener(a);
    }

    /**
     * update dat pohledu
     */
    public void updateView() {
        // UPDATE DATA
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getCustomerTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getCustomerTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            mainView.getCustomerTablePrevButton().setEnabled(false);
        } else {
            mainView.getCustomerTablePrevButton().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            mainView.getCustomerTableNextButton().setEnabled(false);
        } else {
            mainView.getCustomerTableNextButton().setEnabled(true);
        }
    }

    /**
     * Nastavení filtru zobrazení
     */
    private void setFilterData() {
        filter.getINPssn().setSelected(Configuration.getInstance().isCustomerShowSSN());
        filter.getINPname().setSelected(Configuration.getInstance().isCustomerShowName());
        filter.getINPemail().setSelected(Configuration.getInstance().isCustomerShowEmail());
        filter.getINPadress().setSelected(Configuration.getInstance().isCustomerShowAdress());
        filter.getINPphone().setSelected(Configuration.getInstance().isCustomerShowPhone());
        filter.getINPnotes().setSelected(Configuration.getInstance().isCustomerShowNotes());

        filter.getINPcustomerMaxRowsCount().setValue((int) Configuration.getInstance().getMaxCustomerRowsCount());

        for (Map.Entry<String, String> entry : orderType.entrySet()) {
            filter.getINPorderType().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getCustomerOrderType())) {
                filter.getINPorderType().setSelectedItem(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : orderBy.entrySet()) {
            filter.getINPorderBy().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getCustomerOrderBy())) {
                filter.getINPorderBy().setSelectedItem(entry.getKey());
            }
        }

        updateView();
    }

    /**
     * zobrazí filtr zobrazení
     */
    private void showFilter() {
        filter.setLocationRelativeTo(null);
        filter.setVisible(true);
    }

    /**
     * Nastaví filtr zobreazení
     */
    private void setFilter() {
        Configuration.getInstance().setCustomerShowSSN(filter.getINPssn().isSelected());
        Configuration.getInstance().setCustomerShowName(filter.getINPname().isSelected());
        Configuration.getInstance().setCustomerShowEmail(filter.getINPemail().isSelected());
        Configuration.getInstance().setCustomerShowPhone(filter.getINPphone().isSelected());
        Configuration.getInstance().setCustomerShowAdress(filter.getINPadress().isSelected());
        Configuration.getInstance().setCustomerShowNotes(filter.getINPnotes().isSelected());

        Configuration.getInstance().setCustomerOrderBy(orderBy.get((String) filter.getINPorderBy().getSelectedItem()));
        Configuration.getInstance().setCustomerOrderType(orderType.get((String) filter.getINPorderType().getSelectedItem()));
        Configuration.getInstance().setMaxCustomerRowsCount((int) filter.getINPcustomerMaxRowsCount().getValue());
        updateView();
        filter.setVisible(false);
    }

    /**
     * Zobrazí vyhledávání
     */
    private void showSearch() {
        if (mainView.getTabPanel().getSelectedIndex() != 1) {
            return;
        }
        csd.setLocationRelativeTo(null);
        csd.setVisible(true);
    }

    /**
     * Smaže vstupy vyhledávání
     */
    private void resetSearch() {
        csd.getInputSSN().setText("");
        csd.getInputFName().setText("");
        csd.getInputLName().setText("");
        csd.getInputPhone().setText("");
        csd.getInputEmail().setText("");
    }

    /**
     * Zastaví vyhledávání
     */
    private void stopSearch() {
        csd.getInputSSN().setText("");
        csd.getInputFName().setText("");
        csd.getInputLName().setText("");
        csd.getInputPhone().setText("");
        csd.getInputEmail().setText("");
        tableModel.stopSearch();
        updateView();
        mainView.getBTNstopCustomerSearch().setVisible(false);
    }

    /**
     * Vyhledá zákaznáky
     */
    private void search() {
        tableModel.search(
                csd.getInputSSN().getText().trim(),
                csd.getInputFName().getText().trim(),
                csd.getInputLName().getText().trim(),
                csd.getInputEmail().getText().trim(),
                csd.getInputPhone().getText().trim());
        updateView();
        csd.setVisible(false);

        mainView.getBTNstopCustomerSearch().setVisible(true);

    }

    /**
     * Vygeneruje čárové kódy
     */
    private void generateBarcode() {
        if (mainView.getTabPanel().getSelectedIndex() != 1) {
            return;
        }
        if (mainView.getCustomerTable().getSelectedRowCount() > 0) {
            List<Customer> customers = tableModel.getCustomers(mainView.getCustomerTable().getSelectedRows());
            String folderName = "ba-" + DateHelper.getCurrentDateIncludingTimeString();
            FileManager.getInstance().createDir(folderName);

            for (Customer c : customers) {
                FileManager.getInstance().saveImage(folderName + "/" + c.getStringSSN(), Barcode.encode(c.getStringSSN()));
            }
            FileManager.getInstance().open(folderName + "/");
        } else {
            JOptionPane.showMessageDialog(mainView, "Nejprve vyberte položky z tabulky", "Chyba", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Vygeneruje QR kódy
     */
    private void generateQRCode() {
        if (mainView.getTabPanel().getSelectedIndex() != 1) {
            return;
        }
        if (mainView.getCustomerTable().getSelectedRowCount() > 0) {
            List<Customer> customers = tableModel.getCustomers(mainView.getCustomerTable().getSelectedRows());
            String folderName = "qr-" + DateHelper.getCurrentDateIncludingTimeString();
            FileManager.getInstance().createDir(folderName);

            for (Customer c : customers) {
                FileManager.getInstance().saveImage(folderName + "/" + c.getStringSSN(), QRCode.encode(c.getStringSSN()));
            }
            FileManager.getInstance().open(folderName + "/");
        } else {
            JOptionPane.showMessageDialog(mainView, "Nejprve vyberte položky z tabulky", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Třída zodpovídající za stisk klávesy z odposlouchávaných komponent
     * pohledu
     */
    private class CustomerTabKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "customerFilter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getCustomerFilterInput().getText().trim().isEmpty()) {
                            tableModel.applyFilter("");
                        } else {
                            tableModel.applyFilter(mainView.getCustomerFilterInput().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "customerPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getCustomerTableInputNumber().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
                case "customerTable":
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        if (mainView.getCustomerTable().getSelectedRows().length == 0) {
                            return;
                        }
                        int isSure = JOptionPane.showInternalConfirmDialog(mainView.getContentPane(), "Opravdu chcete smazat vybrané položky?", "Opravdu smazat?", JOptionPane.OK_CANCEL_OPTION);
                        if (isSure == JOptionPane.OK_OPTION) {
                            for (Customer c : tableModel.getCustomers(mainView.getCustomerTable().getSelectedRows())) {
                                if (BorrowService.getInstance().activeBorrowsOfCustomer(c).size() > 0) {
                                    JOptionPane.showMessageDialog(mainView, "Uživatel má aktivní výpůjčky", "Nelze smazat", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    CustomerService.getInstance().delete(c);
                                }
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class CustomerTabActionListener implements ActionListener {

        public CustomerTabActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "filter":
                    showFilter();
                    break;
                case "filterConfirmed":
                    setFilter();
                    break;
                case "searchDialog":
                    showSearch();
                    break;
                case "closeSearchDialog":
                    csd.setVisible(false);
                    break;
                case "resetSearchDialog":
                    resetSearch();
                    break;
                case "stopSearch":
                    stopSearch();
                    break;
                case "search":
                    search();
                    break;
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "prevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
                case "barcode":
                    generateBarcode();
                    break;
                case "qrcode":
                    generateQRCode();
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     */
    private class CustomerTabMouseListener implements MouseListener {

        public CustomerTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Customer temp = tableModel.getCustomer(mainView.getCustomerTable().getSelectedRow());
                CustomerDetailController c = new CustomerDetailController(temp);
                c.showView();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}

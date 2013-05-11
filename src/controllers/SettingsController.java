/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import models.SystemUserTableModel;
import models.entity.Genre;
import models.entity.SystemUser;
import services.GenreService;
import services.SystemUserService;
import views.SettingsDialog;

/**
 *
 * @author Nesh
 */
public class SettingsController extends BaseController {

    SettingsDialog dialog;
    Configuration c;
    DefaultListModel<Genre> genreListModel;
    SystemUserTableModel tableModel;

    //
    public SettingsController() {
        c = Configuration.getInstance();
        dialog = new SettingsDialog(null, true);

        if (!AppController.getInstance().getLoggedUser().isMaster()) {
            dialog.getCMPtabbedPanel().remove(5);
            dialog.repaint();
        }

        tableModel = new SystemUserTableModel();
        dialog.getTABsystemUsers().setModel(tableModel);

        initListeners();
        updateView();
    }

    private void initListeners() {
        // Action Listener        
        SettingsActionListener a = new SettingsActionListener();
        dialog.getBTNsave().addActionListener(a);
        dialog.getBTNdefault().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNworkspace().addActionListener(a);
        dialog.getBTNaddSystemUser().addActionListener(a);
        dialog.getBTNaddGenre().addActionListener(a);
        dialog.getBTNremoveGenre().addActionListener(a);

        // KeyListener
        SettingKeyListener k = new SettingKeyListener();
        dialog.getINPgenre().addKeyListener(k);

        // MouseListener
        SettingMouseListener m = new SettingMouseListener();
        dialog.getTABsystemUsers().addMouseListener(m);
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

    private void updateView() {
        // KNIHY
        dialog.getINPauthor().setSelected(c.isRequireAuthor());
        dialog.getINPcount().setSelected(c.isRequireCount());
        dialog.getINPisbn10().setSelected(c.isRequireISBN10());
        dialog.getINPisbn13().setSelected(c.isRequireISBN13());
        dialog.getINPlanguage().setSelected(c.isRequireLanguage());
        dialog.getINPlocation().setSelected(c.isRequireLocation());
        dialog.getINPpageCount().setSelected(c.isRequirePageCount());
        dialog.getINPpublishedYear().setSelected(c.isRequirePublishedYear());
        dialog.getINPpublisher().setSelected(c.isRequirePublisher());
        dialog.getINPaddedDate().setSelected(c.isRequireAddedDate());
        dialog.getINPgenre().setSelected(c.isRequireGenre());
        dialog.getINPsponsor().setSelected(c.isRequireSponsor());

        // Žánry
        genreListModel = new DefaultListModel();
        for (Genre g : GenreService.getInstance().getGenres()) {
            genreListModel.addElement(g);
        }

        dialog.getINPgenres().setModel(genreListModel);

        // ZÁKAZNÍCI
        dialog.getINPcustomerFirstName().setSelected(c.isCustomerRequireFName());
        dialog.getINPcustomerLastName().setSelected(c.isCustomerRequireLName());
        dialog.getINPcustomerEmail().setSelected(c.isCustomerRequireEmail());
        dialog.getINPcustomerPhone().setSelected(c.isCustomerRequirePhone());
        dialog.getINPcustomerStreet().setSelected(c.isCustomerRequireStreet());
        dialog.getINPcustomerCity().setSelected(c.isCustomerRequireCity());
        dialog.getINPcustomerCountry().setSelected(c.isCustomerRequireCountry());
        dialog.getINPcustomerPostcode().setSelected(c.isCustomerRequirePostcode());

        //VÝPŮJČKY
        dialog.getINPborrowDays().setValue(c.getBorrowDays());

        // LibSys Server
        dialog.getINPportNumber().setValue(c.getServerPort());
        dialog.getINPautoStartServer().setSelected(c.isServerAutoStart());

        //oznámení 
        dialog.getINPlongBorrowDays().setValue(c.getLongBorrowDays());

        // základní
        dialog.getINPshowDeletedItems().setSelected(c.isDeletedItemVisible());
        dialog.getINPdefaultEmail().setText(c.getDefaultEmail());
        dialog.getINPskipLogging().setSelected(c.isSkipLogging());
        dialog.getINPworkspace().setText(c.getWorkspace());

        // Uživatelé
        tableModel.updateData();
        tableModel.fireTableDataChanged();

    }

    private void saveSettings() {
        // WORKSPACE
        if (dialog.getINPworkspace().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nemáte zvolen žádný pracovní prostor", "- Funkce generování kódu a tisku do PDF budou nefunkční", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setWorkcpace(dialog.getINPworkspace().getText());
        }

        //SKIP LOGGING
        if (SystemUserService.getInstance().isOnlyDefault() && !dialog.getINPskipLogging().isSelected()) {
            JOptionPane.showMessageDialog(dialog, "- V aplikaci chybí uživatelé", "Nelze zapnout přihlašování", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setSkipLogging(dialog.getINPskipLogging().isSelected());
        }

        // SHOW DELETED
        c.setDeletedItemVisible(dialog.getINPshowDeletedItems().isSelected());

        // KNIHY
        c.setRequireAuthor(dialog.getINPauthor().isSelected());
        c.setRequireCount(dialog.getINPcount().isSelected());
        c.setRequireISBN10(dialog.getINPisbn10().isSelected());
        c.setRequireISBN13(dialog.getINPisbn13().isSelected());
        c.setRequireLanguage(dialog.getINPlanguage().isSelected());
        c.setRequireLocation(dialog.getINPlocation().isSelected());
        c.setRequirePageCount(dialog.getINPpageCount().isSelected());
        c.setRequirePublishedYear(dialog.getINPpublishedYear().isSelected());
        c.setRequirePublisher(dialog.getINPpublisher().isSelected());
        c.setRequireAddedDate(dialog.getINPaddedDate().isSelected());
        c.setRequireSponsor(dialog.getINPsponsor().isSelected());
        c.setRequireGenre(dialog.getINPgenre().isSelected());

        //ZÁKAZNÍCI
        c.setCustomerRequireFName(dialog.getINPcustomerFirstName().isSelected());
        c.setCustomerRequireLName(dialog.getINPcustomerLastName().isSelected());
        c.setCustomerRequireEmail(dialog.getINPcustomerEmail().isSelected());
        c.setCustomerRequirePhone(dialog.getINPcustomerPhone().isSelected());
        c.setCustomerRequireStreet(dialog.getINPcustomerStreet().isSelected());
        c.setCustomerRequireCity(dialog.getINPcustomerCity().isSelected());
        c.setCustomerRequireCountry(dialog.getINPcustomerCountry().isSelected());
        c.setCustomerRequirePostcode(dialog.getINPcustomerPostcode().isSelected());

        //VÝPŮJČKY
        c.setBorrowDays((int) dialog.getINPborrowDays().getValue());

        //OZNÁMENÍ
        c.setLongBorrowDays((int) dialog.getINPlongBorrowDays().getValue());

        // LIBSYS SERVER
        c.setServerPort((int) dialog.getINPportNumber().getValue());
        c.setServerAutoStart(dialog.getINPautoStartServer().isSelected());

        RefreshController.getInstance().refreshAll();
        dispose();
    }

    private void restoreDefaultSettings() {
        int result = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Chcete opravdu načíst defaultní nastavení??", "Opravdu provést?", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            c.setDefaultValues();
            updateView();
        }
    }

    private void addSystemUser() {
        NewSystemUserController nsuc = new NewSystemUserController();
        nsuc.showView();

        if (nsuc.isSuccess()) {
            updateView();
        }
    }

    private class SettingMouseListener implements MouseListener {

        public SettingMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                SystemUser u = tableModel.getSystemUser(dialog.getTABsystemUsers().getSelectedRow());
                SystemUserDetailController sudc = new SystemUserDetailController(u);
                sudc.showView();
                updateView();
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

    private class SettingKeyListener implements KeyListener {

        public SettingKeyListener() {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "genreList":
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        List<Genre> selectedGenres = (List<Genre>) dialog.getINPgenres().getSelectedValuesList();
                        for (Genre g : selectedGenres) {
                            GenreService.getInstance().deleteGenre(g);
                        }
                    }
                    updateView();
                    break;
            }
        }
    }

    private class SettingsActionListener implements ActionListener {

        public SettingsActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "save":
                    saveSettings();
                    break;
                case "cancel":
                    dispose();
                    break;
                case "default":
                    restoreDefaultSettings();
                    break;
                case "workspace":

                    JFileChooser f = new JFileChooser(".");
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    f.setAcceptAllFileFilterUsed(false);
                    f.setDialogTitle("Zvolte složku");
                    if (f.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                        dialog.getINPworkspace().setText(f.getCurrentDirectory().getAbsolutePath() + "\\" + f.getSelectedFile().getName());
                    }

                    break;
                case "addGenre":
                    String s = JOptionPane.showInternalInputDialog(dialog.getContentPane(), "Žánr", "Vyplňte žánr", JOptionPane.QUESTION_MESSAGE);
                    if (s == null || s.isEmpty()) {
                        return;
                    }
                    Genre g = new Genre();
                    g.setGenre(s);
                    GenreService.getInstance().saveGenre(g);
                    updateView();
                    break;
                case "removeGenre":
                    List<Genre> selectedGenres = (List<Genre>) dialog.getINPgenres().getSelectedValuesList();
                    for (Genre g2 : selectedGenres) {
                        GenreService.getInstance().deleteGenre(g2);
                    }
                    updateView();
                    break;
                case "addSystemUser":
                    addSystemUser();
                    break;

            }
        }
    }
}

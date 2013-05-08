/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import services.SystemUserService;
import views.SettingsDialog;

/**
 *
 * @author Nesh
 */
public class SettingsController extends BaseController {

    SettingsDialog dialog;
    Configuration c;

    public SettingsController() {
        c = Configuration.getInstance();
        dialog = new SettingsDialog(null, true);

        if (!AppController.getInstance().getLoggedUser().isMaster()) {
            dialog.getCMPtabbedPanel().remove(5);
            dialog.repaint();
        }

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

        // ZÁKAZNÍCI
        dialog.getINPcustomerFirstName().setSelected(c.isCustomerRequireFName());
        dialog.getINPcustomerLastName().setSelected(c.isCustomerRequireLName());
        dialog.getINPcustomerEmail().setSelected(c.isCustomerRequireEmail());
        dialog.getINPcustomerPhone().setSelected(c.isCustomerRequirePhone());
        dialog.getINPcustomerStreet().setSelected(c.isCustomerRequireStreet());
        dialog.getINPcustomerCity().setSelected(c.isCustomerRequireCity());
        dialog.getINPcustomerCountry().setSelected(c.isCustomerRequireCountry());
        dialog.getINPcustomerPostcode().setSelected(c.isCustomerRequirePostcode());

        // LibSys Server
        dialog.getINPportNumber().setValue(Configuration.getInstance().getServerPort());
        dialog.getINPautoStartServer().setSelected(Configuration.getInstance().isServerAutoStart());

        dialog.getINPmaxNotificationRows().setValue(c.getMaxNotificationRowsCount());
        dialog.getINPshowDeletedItems().setSelected(c.isDeletedItemVisible());
        dialog.getINPdefaultEmail().setText(c.getDefaultEmail());
        dialog.getINPskipLogging().setSelected(c.isSkipLogging());
        dialog.getINPworkspace().setText(c.getWorkspace());
    }

    private void saveSettings() {
        // WORKSPACE
        if (dialog.getINPworkspace().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nemáte zvolen žádný pracovní prostor", "- Funkce generování kódu a tisku do PDF budou nefunkční", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setWorkcpace(dialog.getINPworkspace().getText());
        }
        if (SystemUserService.getInstance().isOnlyDefault() && !dialog.getINPskipLogging().isSelected()) {
            JOptionPane.showMessageDialog(dialog, "- V aplikaci chybí uživatelé", "Nelze zapnout přihlašování", JOptionPane.ERROR_MESSAGE);
        } else {
            c.setSkipLogging(dialog.getINPskipLogging().isSelected());
        }
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

        // LIBSYS SERVER
        c.setServerPort((int) dialog.getINPportNumber().getValue());
        c.setServerAutoStart(dialog.getINPautoStartServer().isSelected());

        c.setDeletedItemVisible(dialog.getINPshowDeletedItems().isSelected());

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

            }
        }
    }
}

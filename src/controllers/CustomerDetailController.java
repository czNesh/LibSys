/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import coding.Barcode;
import coding.QRCode;
import io.ApplicationLog;
import io.Configuration;
import io.FileManager;
import io.PDFPrinter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.BorrowTableModel;
import models.entity.Borrow;
import models.entity.Customer;
import services.BorrowService;
import services.CustomerService;
import views.CustomerDetailDialog;

/**
 *
 * @author petr.hejhal
 */
public class CustomerDetailController extends BaseController {

    private CustomerDetailDialog dialog;
    BorrowTableModel tableModel;
    private boolean editMode;
    Customer customer;

    public CustomerDetailController(Customer customer) {
        dialog = new CustomerDetailDialog(null, true);
        this.customer = customer;
        editMode = false;
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfCustomer(customer.getId()));
        String[] countries = {"< Neuvedeno >", "Afghanistan - AF", "Aland Islands - AX", "Albania - AL", "Algeria - DZ", "American Samoa - AS", "Andorra - AD", "Angola - AO", "Anguilla - AI", "Antarctica - AQ", "Antigua And Barbuda - AG", "Argentina - AR", "Armenia - AM", "Aruba - AW", "Australia - AU", "Austria - AT", "Azerbaijan - AZ", "Bahamas - BS", "Bahrain - BH", "Bangladesh - BD", "Barbados - BB", "Belarus - BY", "Belgium - BE", "Belize - BZ", "Benin - BJ", "Bermuda - BM", "Bhutan - BT", "Bolivia - BO", "Bosnia And Herzegovina - BA", "Botswana - BW", "Bouvet Island - BV", "Brazil - BR", "British Indian Ocean Territory - IO", "Brunei Darussalam - BN", "Bulgaria - BG", "Burkina Faso - BF", "Burundi - BI", "Cambodia - KH", "Cameroon - CM", "Canada - CA", "Cape Verde - CV", "Cayman Islands - KY", "Central African Republic - CF", "Chad - TD", "Chile - CL", "China - CN", "Christmas Island - CX", "Cocos (Keeling) Islands - CC", "Colombia - CO", "Comoros - KM", "Congo - CG", "Congo, The Democratic Republic Of The - CD", "Cook Islands - CK", "Costa Rica - CR", "Cote D'Ivoire - CI", "Croatia - HR", "Cuba - CU", "Cyprus - CY", "Czech Republic - CZ", "Denmark - DK", "Djibouti - DJ", "Dominica - DM", "Dominican Republic - DO", "Ecuador - EC", "Egypt - EG", "El Salvador - SV", "Equatorial Guinea - GQ", "Eritrea - ER", "Estonia - EE", "Ethiopia - ET", "Falkland Islands (Malvinas) - FK", "Faroe Islands - FO", "Fiji - FJ", "Finland - FI", "France - FR", "French Guiana - GF", "French Polynesia - PF", "French Southern Territories - TF", "Gabon - GA", "Gambia - GM", "Georgia - GE", "Germany - DE", "Ghana - GH", "Gibraltar - GI", "Greece - GR", "Greenland - GL", "Grenada - GD", "Guadeloupe - GP", "Guam - GU", "Guatemala - GT", "Guernsey -  GG", "Guinea - GN", "Guinea-Bissau - GW", "Guyana - GY", "Haiti - HT", "Heard Island And Mcdonald Islands - HM", "Holy See (Vatican City State) - VA", "Honduras - HN", "Hong Kong - HK", "Hungary - HU", "Iceland - IS", "India - IN", "Indonesia - ID", "Iran, Islamic Republic Of - IR", "Iraq - IQ", "Ireland - IE", "Isle Of Man - IM", "Israel - IL", "Italy - IT", "Jamaica - JM", "Japan - JP", "Jersey - JE", "Jordan - JO", "Kazakhstan - KZ", "Kenya - KE", "Kiribati - KI", "Korea, Democratic People'S Republic Of - KP", "Korea, Republic Of - KR", "Kuwait - KW", "Kyrgyzstan - KG", "Lao People'S Democratic Republic - LA", "Latvia - LV", "Lebanon - LB", "Lesotho - LS", "Liberia - LR", "Libyan Arab Jamahiriya - LY", "Liechtenstein - LI", "Lithuania - LT", "Luxembourg - LU", "Macao - MO", "Macedonia, The Former Yugoslav Republic Of - MK", "Madagascar - MG", "Malawi - MW", "Malaysia - MY", "Maldives - MV", "Mali - ML", "Malta - MT", "Marshall Islands - MH", "Martinique - MQ", "Mauritania - MR", "Mauritius - MU", "Mayotte - YT", "Mexico - MX", "Micronesia, Federated States Of - FM", "Moldova, Republic Of - MD", "Monaco - MC", "Mongolia - MN", "Montserrat - MS", "Morocco - MA", "Mozambique - MZ", "Myanmar - MM", "Namibia - NA", "Nauru - NR", "Nepal - NP", "Netherlands - NL", "Netherlands Antilles - AN", "New Caledonia - NC", "New Zealand - NZ", "Nicaragua - NI", "Niger - NE", "Nigeria - NG", "Niue - NU", "Norfolk Island - NF", "Northern Mariana Islands - MP", "Norway - NO", "Oman - OM", "Pakistan - PK", "Palau - PW", "Palestinian Territory, Occupied - PS", "Panama - PA", "Papua New Guinea - PG", "Paraguay - PY", "Peru - PE", "Philippines - PH", "Pitcairn - PN", "Poland - PL", "Portugal - PT", "Puerto Rico - PR", "Qatar - QA", "Reunion - RE", "Romania - RO", "Russian Federation - RU", "Rwanda - RW", "Saint Helena - SH", "Saint Kitts And Nevis - KN", "Saint Lucia - LC", "Saint Pierre And Miquelon - PM", "Saint Vincent And The Grenadines - VC", "Samoa - WS", "San Marino - SM", "Sao Tome And Principe - ST", "Saudi Arabia - SA", "Senegal - SN", "Serbia And Montenegro - CS", "Seychelles - SC", "Sierra Leone - SL", "Singapore - SG", "Slovakia - SK", "Slovenia - SI", "Solomon Islands - SB", "Somalia - SO", "South Africa - ZA", "South Georgia And The South Sandwich Islands - GS", "Spain - ES", "Sri Lanka - LK", "Sudan - SD", "Suriname - SR", "Svalbard And Jan Mayen - SJ", "Swaziland - SZ", "Sweden - SE", "Switzerland - CH", "Syrian Arab Republic - SY", "Taiwan, Province Of China - TW", "Tajikistan - TJ", "Tanzania, United Republic Of - TZ", "Thailand - TH", "Timor-Leste - TL", "Togo - TG", "Tokelau - TK", "Tonga - TO", "Trinidad And Tobago - TT", "Tunisia - TN", "Turkey - TR", "Turkmenistan - TM", "Turks And Caicos Islands - TC", "Tuvalu - TV", "Uganda - UG", "Ukraine - UA", "United Arab Emirates - AE", "United Kingdom - GB", "United States - US", "United States Minor Outlying Islands - UM", "Uruguay - UY", "Uzbekistan - UZ", "Vanuatu - VU", "Venezuela - VE", "Viet Nam - VN", "Virgin Islands, British - VG", "Virgin Islands, U.S. - VI", "Wallis And Futuna - WF", "Western Sahara - EH", "Yemen - YE", "Zambia - ZM", "Zimbabwe - ZW"};
        dialog.getINPcountry().setModel(new javax.swing.DefaultComboBoxModel(countries));
        initListeners();
        showData();
    }

    private void showData() {
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfCustomer(customer.getId()));
        dialog.getTABborrows().setModel(tableModel);
        tableModel.fireTableDataChanged();

        dialog.getINPssn().setText(String.valueOf(customer.getSSN()));
        dialog.getINPfirstName().setText(customer.getFirstName());
        dialog.getINPlastName().setText(customer.getLastName());
        dialog.getINPstreet().setText(customer.getStreet());
        dialog.getINPcity().setText(customer.getCity());
        dialog.getINPpostcode().setText(customer.getPostcode());
        dialog.getINPemail().setText(customer.getEmail());
        dialog.getINPphone().setText(customer.getPhone());
        dialog.getINPnotes().setText(customer.getNotes());
        dialog.getINPcountry().setSelectedItem(customer.getCountry());

        if (customer.isDeleted()) {
            dialog.getBTNrenew().setVisible(true);
            dialog.getBTNdelete().setVisible(false);
        }

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

    private void initListeners() {
        CustomerDetailActionListener a = new CustomerDetailActionListener();
        dialog.getBTNclose().addActionListener(a);
        dialog.getBTNedit().addActionListener(a);
        dialog.getBTNprint().addActionListener(a);
        dialog.getBTNbarcode().addActionListener(a);
        dialog.getBTNqrcode().addActionListener(a);
        dialog.getBTNdelete().addActionListener(a);
        dialog.getBTNrenew().addActionListener(a);

        CustomerDetailMouseListener m = new CustomerDetailMouseListener();
        dialog.getTABborrows().addMouseListener(m);
    }

    private class CustomerDetailActionListener implements ActionListener {

        public CustomerDetailActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "edit":
                    switchEditMode();
                    break;
                case "close":
                    dialog.dispose();
                    dialog = null;
                    break;
                case "saveButton":
                    saveCustomer();
                    break;
                case "delete":
                    deleteAction();
                    break;
                case "print":
                    PDFPrinter.getInstance().printCustomer(customer);
                    break;

                case "renew":
                    renew();
                    break;
                case "qrcode":
                    BufferedImage qrcode = QRCode.encode(customer.getStringSSN());
                    FileManager.getInstance().saveImage("qr_book_" + customer.getStringSSN(), qrcode);
                    FileManager.getInstance().openImage("qr_book_" + customer.getStringSSN());
                    break;
                case "barcode":
                    BufferedImage barcode = Barcode.encode(customer.getStringSSN());
                    FileManager.getInstance().saveImage("ba_book_" + customer.getStringSSN(), barcode);
                    FileManager.getInstance().openImage("ba_book_" + customer.getStringSSN());
                    break;
                default:
                    break;
            }

        }

        private void switchEditMode() {
            if (editMode) {
                dialog.getBTNedit().setName("editButton");
                dialog.getBTNedit().setText("Upravit");
                dialog.getINPnotes().setBackground(new Color(240, 240, 240));


            } else {
                dialog.getBTNedit().setName("saveButton");
                dialog.getBTNedit().setText("Uložit");
                dialog.getINPnotes().setBackground(Color.white);
            }

            dialog.getINPfirstName().setEditable(!editMode);
            dialog.getINPlastName().setEditable(!editMode);
            dialog.getINPemail().setEditable(!editMode);
            dialog.getINPphone().setEditable(!editMode);
            dialog.getINPstreet().setEditable(!editMode);
            dialog.getINPcity().setEditable(!editMode);
            dialog.getINPcountry().setEditable(!editMode);
            dialog.getINPpostcode().setEditable(!editMode);
            dialog.getINPnotes().setEditable(!editMode);

            editMode = !editMode;

            dialog.revalidate();
            dialog.repaint();
        }

        private void saveCustomer() {
            /* Získání dat */
            String fname = dialog.getINPfirstName().getText().trim();
            String lname = dialog.getINPlastName().getText().trim();
            String street = dialog.getINPstreet().getText().trim();
            String city = dialog.getINPcity().getText().trim();
            String postcode = dialog.getINPpostcode().getText().trim();
            String country = dialog.getINPcountry().getSelectedItem().toString().trim();
            String email = dialog.getINPemail().getText().trim();
            String phone = dialog.getINPphone().getText().trim();
            String notes = dialog.getINPnotes().getText().trim();

            /* Validace */
            StringBuilder validationLog = new StringBuilder();
            Configuration cfg = Configuration.getInstance();

            // Kontrola jména
            if (fname.isEmpty()) {
                if (cfg.isCustomerRequireFName()) {
                    validationLog.append("Zadejte jméno\n");
                }
            } else {
                customer.setFirstName(fname);
            }

            // Kontrola přijmení
            if (lname.isEmpty()) {
                if (cfg.isCustomerRequireLName()) {
                    validationLog.append("Zadejte přijmení\n");
                }
            } else {
                customer.setLastName(lname);
            }

            // Aspon jedna cast jmena zadana
            if ((customer.getFirstName() == null || customer.getFirstName().isEmpty()) && (customer.getLastName() == null || customer.getLastName().isEmpty())) {
                validationLog.append("Alespoň jeden z údajů jméno/přijmení je povinný\n");
            }

            // Kontrola emailu
            if (email.isEmpty()) {
                if (cfg.isCustomerRequireEmail()) {
                    validationLog.append("Email je povinná položka\n");
                }
            } else {
                if (!email.matches(".+@.+\\.[a-z]+")) {
                    validationLog.append("Neplatný email\n");
                } else {
                    customer.setEmail(email);
                }
            }

            if (street.isEmpty()) {
                if (cfg.isCustomerRequireStreet()) {
                    validationLog.append("Vyplňte ulici\n");
                }
            } else {
                customer.setStreet(street);
            }

            if (city.isEmpty()) {
                if (cfg.isCustomerRequireCity()) {
                    validationLog.append("Vyplňte město\n");
                }
            } else {
                customer.setCity(city);
            }

            if (country.isEmpty() || country.equals("< Neuvedeno >")) {
                if (cfg.isCustomerRequireCity()) {
                    validationLog.append("Vyplňte zemi\n");
                }
            } else {
                customer.setCountry(country);
            }

            if (postcode.isEmpty()) {
                if (cfg.isCustomerRequireCity()) {
                    validationLog.append("Vyplňte PSČ\n");
                }
            } else {
                customer.setPostcode(postcode);
            }

            if (phone.isEmpty()) {
                if (cfg.isCustomerRequireCity()) {
                    validationLog.append("Vyplňte telefon\n");
                }
            } else {
                customer.setPhone(phone);

            }

            customer.setNotes(notes);
            // Validní? uložení : chyba
            if (validationLog.length() == 0) {
                CustomerService.getInstance().save(customer);
                RefreshController.getInstance().refreshCustomerTab();
                ApplicationLog.getInstance().addMessage("Změny uloženy - " + customer.getFullName() + " (" + customer.getStringSSN() + ")");
                switchEditMode();
            } else {
                JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);

            }
        }

        private void deleteAction() {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete smazat uživatele " + customer.getStringSSN() + "?", "Opravdu smazat?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                if (BorrowService.getInstance().activeBorrowsOfCustomer(customer).size() > 0) {
                    JOptionPane.showMessageDialog(dialog, "Uživatel má aktivní výpůjčky", "Nelze smazat", JOptionPane.ERROR_MESSAGE);
                } else {
                    CustomerService.getInstance().delete(customer);
                    RefreshController.getInstance().refreshCustomerTab();
                    dispose();
                }
            }
        }

        private void renew() {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete obnovit uživatele " + customer.getStringSSN() + "?", "Opravdu obnovit?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                customer.setDeleted(false);
                CustomerService.getInstance().save(customer);
                ApplicationLog.getInstance().addMessage("Zákazník " + customer.getFullName() + " (" + customer.getStringSSN() + ") byla obnovena");
                RefreshController.getInstance().refreshCustomerTab();
                dialog.getBTNrenew().setVisible(false);
                dialog.getBTNdelete().setVisible(true);
            }
        }
    }

    private class CustomerDetailMouseListener implements MouseListener {

        public CustomerDetailMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(dialog.getTABborrows().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
                
                showData();
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

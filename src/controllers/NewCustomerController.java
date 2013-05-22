package controllers;

import helpers.Validator;
import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import models.entity.Customer;
import services.CustomerService;
import views.NewCustomerDialog;

/**
 * Třída (controller) starající se o vložení nového uživatele
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
class NewCustomerController extends BaseController {

    private NewCustomerDialog view; // připojený pohled

    /**
     * Konstruktor třídy
     *
     * @param parent hlavní pohled
     */
    public NewCustomerController(JFrame parent) {
        view = new NewCustomerDialog(parent, true);
        setCountries();
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        view.dispose();
        view = null;
    }

    /**
     * Připravý seznam zemí
     */
    private void setCountries() {
        String[] countries = {"< Neuvedeno >", "Afghanistan - AF", "Aland Islands - AX", "Albania - AL", "Algeria - DZ", "American Samoa - AS", "Andorra - AD", "Angola - AO", "Anguilla - AI", "Antarctica - AQ", "Antigua And Barbuda - AG", "Argentina - AR", "Armenia - AM", "Aruba - AW", "Australia - AU", "Austria - AT", "Azerbaijan - AZ", "Bahamas - BS", "Bahrain - BH", "Bangladesh - BD", "Barbados - BB", "Belarus - BY", "Belgium - BE", "Belize - BZ", "Benin - BJ", "Bermuda - BM", "Bhutan - BT", "Bolivia - BO", "Bosnia And Herzegovina - BA", "Botswana - BW", "Bouvet Island - BV", "Brazil - BR", "British Indian Ocean Territory - IO", "Brunei Darussalam - BN", "Bulgaria - BG", "Burkina Faso - BF", "Burundi - BI", "Cambodia - KH", "Cameroon - CM", "Canada - CA", "Cape Verde - CV", "Cayman Islands - KY", "Central African Republic - CF", "Chad - TD", "Chile - CL", "China - CN", "Christmas Island - CX", "Cocos (Keeling) Islands - CC", "Colombia - CO", "Comoros - KM", "Congo - CG", "Congo, The Democratic Republic Of The - CD", "Cook Islands - CK", "Costa Rica - CR", "Cote D'Ivoire - CI", "Croatia - HR", "Cuba - CU", "Cyprus - CY", "Czech Republic - CZ", "Denmark - DK", "Djibouti - DJ", "Dominica - DM", "Dominican Republic - DO", "Ecuador - EC", "Egypt - EG", "El Salvador - SV", "Equatorial Guinea - GQ", "Eritrea - ER", "Estonia - EE", "Ethiopia - ET", "Falkland Islands (Malvinas) - FK", "Faroe Islands - FO", "Fiji - FJ", "Finland - FI", "France - FR", "French Guiana - GF", "French Polynesia - PF", "French Southern Territories - TF", "Gabon - GA", "Gambia - GM", "Georgia - GE", "Germany - DE", "Ghana - GH", "Gibraltar - GI", "Greece - GR", "Greenland - GL", "Grenada - GD", "Guadeloupe - GP", "Guam - GU", "Guatemala - GT", "Guernsey -  GG", "Guinea - GN", "Guinea-Bissau - GW", "Guyana - GY", "Haiti - HT", "Heard Island And Mcdonald Islands - HM", "Holy See (Vatican City State) - VA", "Honduras - HN", "Hong Kong - HK", "Hungary - HU", "Iceland - IS", "India - IN", "Indonesia - ID", "Iran, Islamic Republic Of - IR", "Iraq - IQ", "Ireland - IE", "Isle Of Man - IM", "Israel - IL", "Italy - IT", "Jamaica - JM", "Japan - JP", "Jersey - JE", "Jordan - JO", "Kazakhstan - KZ", "Kenya - KE", "Kiribati - KI", "Korea, Democratic People'S Republic Of - KP", "Korea, Republic Of - KR", "Kuwait - KW", "Kyrgyzstan - KG", "Lao People'S Democratic Republic - LA", "Latvia - LV", "Lebanon - LB", "Lesotho - LS", "Liberia - LR", "Libyan Arab Jamahiriya - LY", "Liechtenstein - LI", "Lithuania - LT", "Luxembourg - LU", "Macao - MO", "Macedonia, The Former Yugoslav Republic Of - MK", "Madagascar - MG", "Malawi - MW", "Malaysia - MY", "Maldives - MV", "Mali - ML", "Malta - MT", "Marshall Islands - MH", "Martinique - MQ", "Mauritania - MR", "Mauritius - MU", "Mayotte - YT", "Mexico - MX", "Micronesia, Federated States Of - FM", "Moldova, Republic Of - MD", "Monaco - MC", "Mongolia - MN", "Montserrat - MS", "Morocco - MA", "Mozambique - MZ", "Myanmar - MM", "Namibia - NA", "Nauru - NR", "Nepal - NP", "Netherlands - NL", "Netherlands Antilles - AN", "New Caledonia - NC", "New Zealand - NZ", "Nicaragua - NI", "Niger - NE", "Nigeria - NG", "Niue - NU", "Norfolk Island - NF", "Northern Mariana Islands - MP", "Norway - NO", "Oman - OM", "Pakistan - PK", "Palau - PW", "Palestinian Territory, Occupied - PS", "Panama - PA", "Papua New Guinea - PG", "Paraguay - PY", "Peru - PE", "Philippines - PH", "Pitcairn - PN", "Poland - PL", "Portugal - PT", "Puerto Rico - PR", "Qatar - QA", "Reunion - RE", "Romania - RO", "Russian Federation - RU", "Rwanda - RW", "Saint Helena - SH", "Saint Kitts And Nevis - KN", "Saint Lucia - LC", "Saint Pierre And Miquelon - PM", "Saint Vincent And The Grenadines - VC", "Samoa - WS", "San Marino - SM", "Sao Tome And Principe - ST", "Saudi Arabia - SA", "Senegal - SN", "Serbia And Montenegro - CS", "Seychelles - SC", "Sierra Leone - SL", "Singapore - SG", "Slovakia - SK", "Slovenia - SI", "Solomon Islands - SB", "Somalia - SO", "South Africa - ZA", "South Georgia And The South Sandwich Islands - GS", "Spain - ES", "Sri Lanka - LK", "Sudan - SD", "Suriname - SR", "Svalbard And Jan Mayen - SJ", "Swaziland - SZ", "Sweden - SE", "Switzerland - CH", "Syrian Arab Republic - SY", "Taiwan, Province Of China - TW", "Tajikistan - TJ", "Tanzania, United Republic Of - TZ", "Thailand - TH", "Timor-Leste - TL", "Togo - TG", "Tokelau - TK", "Tonga - TO", "Trinidad And Tobago - TT", "Tunisia - TN", "Turkey - TR", "Turkmenistan - TM", "Turks And Caicos Islands - TC", "Tuvalu - TV", "Uganda - UG", "Ukraine - UA", "United Arab Emirates - AE", "United Kingdom - GB", "United States - US", "United States Minor Outlying Islands - UM", "Uruguay - UY", "Uzbekistan - UZ", "Vanuatu - VU", "Venezuela - VE", "Viet Nam - VN", "Virgin Islands, British - VG", "Virgin Islands, U.S. - VI", "Wallis And Futuna - WF", "Western Sahara - EH", "Yemen - YE", "Zambia - ZM", "Zimbabwe - ZW"};
        view.getInputCountry().setModel(new javax.swing.DefaultComboBoxModel(countries));
        view.getInputCountry().setSelectedIndex(58);
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        // Action Listener
        NewCustomerActionListener a = new NewCustomerActionListener();
        view.getSaveButton().addActionListener(a);
        view.getCancelButton().addActionListener(a);
    }

    /**
     * uloží zákazníka
     */
    private void saveCustomer() {
        /* Získání dat */
        String fname = view.getInputFirstName().getText().trim();
        String lname = view.getInputLastName().getText().trim();
        String street = view.getInputStreet().getText().trim();
        String city = view.getInputCity().getText().trim();
        String postcode = view.getInputPostcode().getText().trim();
        String country = view.getInputCountry().getSelectedItem().toString().trim();
        String email = view.getInputEmail().getText().trim();
        String phone = view.getInputPhone().getText().trim();
        String notes = view.getInputNotes().getText().trim();

        /* Validace */
        StringBuilder validationLog = new StringBuilder();
        Configuration cfg = Configuration.getInstance();

        Customer c = new Customer();

        // Validace jména
        if (Validator.isValidString(fname)) {
            c.setFirstName(fname);
        } else {
            if (cfg.isCustomerRequireFName()) {
                validationLog.append("Zadejte jméno\n");
            }
        }

        // Validace přijmení
        if (Validator.isValidString(lname)) {
            c.setLastName(lname);
        } else {
            if (cfg.isCustomerRequireLName()) {
                validationLog.append("Zadejte přijmení\n");
            }
        }

        // Validace jména a přijmení (alespoň jedno)
        if (!Validator.isOneOfTheTwoFilled(fname, lname)) {
            validationLog.append("Alespoň jeden z údajů jméno/přijmení je povinný\n");
        }

        // Validace emailu
        if (Validator.isValidString(email)) {
            if (Validator.isValidEmail(email)) {
                c.setEmail(email);
            } else {
                validationLog.append("Neplatný email\n");
            }
        } else {
            if (cfg.isCustomerRequireEmail()) {
                validationLog.append("Email je povinná položka\n");
            }
        }

        // Validace ulice
        if (Validator.isValidString(street)) {
            c.setStreet(street);
        } else {
            if (cfg.isCustomerRequireStreet()) {
                validationLog.append("Vyplňte ulici\n");
            }
        }

        // Validace města
        if (Validator.isValidString(city)) {
            c.setCity(city);
        } else {
            if (cfg.isCustomerRequireCity()) {
                validationLog.append("Vyplňte město\n");
            }
        }

        // Validace země
        if (country.isEmpty() || country.equals("< Neuvedeno >")) {
            if (cfg.isCustomerRequireCity()) {
                validationLog.append("Vyplňte zemi\n");
            }
        } else {
            c.setCountry(country);
        }

        // Validace poštovního kódu
        if (Validator.isValidString(postcode)) {
            c.setPostcode(postcode);
        } else {
            if (cfg.isCustomerRequireCity()) {
                validationLog.append("Vyplňte PSČ\n");
            }
        }

        if (Validator.isValidString(phone)) {
            c.setPhone(phone);
        } else {
            if (cfg.isCustomerRequireCity()) {
                validationLog.append("Vyplňte telefon\n");
            }
        }

        // nepoviné položky
        c.setNotes(notes);

        // Validní? uložení : chyba
        if (validationLog.length() > 0) {
            JOptionPane.showMessageDialog(view, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
        } else {
            CustomerService.getInstance().saveCustomer(c);
            dispose();
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class NewCustomerActionListener implements ActionListener {

        public NewCustomerActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "save":
                    saveCustomer();
                    break;
                case "cancel":
                    dispose();
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }
}

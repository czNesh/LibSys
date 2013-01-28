/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import models.dao.CustomerDAO;
import models.entity.Customer;
import views.NewCustomerDialog;

/**
 *
 * @author Nesh
 */
class NewCustomerController extends BaseController {

    private NewCustomerDialog view;

    public NewCustomerController(JFrame parent) {
        view = new NewCustomerDialog(parent, false);
        setCountries();
        initListeners();
    }

    @Override
    void showView() {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    @Override
    void dispose() {
        view.dispose();
        view = null;
    }

    private void setCountries() {
        String[] countries = {"Afghanistan - AF", "Aland Islands - AX", "Albania - AL", "Algeria - DZ", "American Samoa - AS", "Andorra - AD", "Angola - AO", "Anguilla - AI", "Antarctica - AQ", "Antigua And Barbuda - AG", "Argentina - AR", "Armenia - AM", "Aruba - AW", "Australia - AU", "Austria - AT", "Azerbaijan - AZ", "Bahamas - BS", "Bahrain - BH", "Bangladesh - BD", "Barbados - BB", "Belarus - BY", "Belgium - BE", "Belize - BZ", "Benin - BJ", "Bermuda - BM", "Bhutan - BT", "Bolivia - BO", "Bosnia And Herzegovina - BA", "Botswana - BW", "Bouvet Island - BV", "Brazil - BR", "British Indian Ocean Territory - IO", "Brunei Darussalam - BN", "Bulgaria - BG", "Burkina Faso - BF", "Burundi - BI", "Cambodia - KH", "Cameroon - CM", "Canada - CA", "Cape Verde - CV", "Cayman Islands - KY", "Central African Republic - CF", "Chad - TD", "Chile - CL", "China - CN", "Christmas Island - CX", "Cocos (Keeling) Islands - CC", "Colombia - CO", "Comoros - KM", "Congo - CG", "Congo, The Democratic Republic Of The - CD", "Cook Islands - CK", "Costa Rica - CR", "Cote D'Ivoire - CI", "Croatia - HR", "Cuba - CU", "Cyprus - CY", "Czech Republic - CZ", "Denmark - DK", "Djibouti - DJ", "Dominica - DM", "Dominican Republic - DO", "Ecuador - EC", "Egypt - EG", "El Salvador - SV", "Equatorial Guinea - GQ", "Eritrea - ER", "Estonia - EE", "Ethiopia - ET", "Falkland Islands (Malvinas) - FK", "Faroe Islands - FO", "Fiji - FJ", "Finland - FI", "France - FR", "French Guiana - GF", "French Polynesia - PF", "French Southern Territories - TF", "Gabon - GA", "Gambia - GM", "Georgia - GE", "Germany - DE", "Ghana - GH", "Gibraltar - GI", "Greece - GR", "Greenland - GL", "Grenada - GD", "Guadeloupe - GP", "Guam - GU", "Guatemala - GT", "Guernsey -  GG", "Guinea - GN", "Guinea-Bissau - GW", "Guyana - GY", "Haiti - HT", "Heard Island And Mcdonald Islands - HM", "Holy See (Vatican City State) - VA", "Honduras - HN", "Hong Kong - HK", "Hungary - HU", "Iceland - IS", "India - IN", "Indonesia - ID", "Iran, Islamic Republic Of - IR", "Iraq - IQ", "Ireland - IE", "Isle Of Man - IM", "Israel - IL", "Italy - IT", "Jamaica - JM", "Japan - JP", "Jersey - JE", "Jordan - JO", "Kazakhstan - KZ", "Kenya - KE", "Kiribati - KI", "Korea, Democratic People'S Republic Of - KP", "Korea, Republic Of - KR", "Kuwait - KW", "Kyrgyzstan - KG", "Lao People'S Democratic Republic - LA", "Latvia - LV", "Lebanon - LB", "Lesotho - LS", "Liberia - LR", "Libyan Arab Jamahiriya - LY", "Liechtenstein - LI", "Lithuania - LT", "Luxembourg - LU", "Macao - MO", "Macedonia, The Former Yugoslav Republic Of - MK", "Madagascar - MG", "Malawi - MW", "Malaysia - MY", "Maldives - MV", "Mali - ML", "Malta - MT", "Marshall Islands - MH", "Martinique - MQ", "Mauritania - MR", "Mauritius - MU", "Mayotte - YT", "Mexico - MX", "Micronesia, Federated States Of - FM", "Moldova, Republic Of - MD", "Monaco - MC", "Mongolia - MN", "Montserrat - MS", "Morocco - MA", "Mozambique - MZ", "Myanmar - MM", "Namibia - NA", "Nauru - NR", "Nepal - NP", "Netherlands - NL", "Netherlands Antilles - AN", "New Caledonia - NC", "New Zealand - NZ", "Nicaragua - NI", "Niger - NE", "Nigeria - NG", "Niue - NU", "Norfolk Island - NF", "Northern Mariana Islands - MP", "Norway - NO", "Oman - OM", "Pakistan - PK", "Palau - PW", "Palestinian Territory, Occupied - PS", "Panama - PA", "Papua New Guinea - PG", "Paraguay - PY", "Peru - PE", "Philippines - PH", "Pitcairn - PN", "Poland - PL", "Portugal - PT", "Puerto Rico - PR", "Qatar - QA", "Reunion - RE", "Romania - RO", "Russian Federation - RU", "Rwanda - RW", "Saint Helena - SH", "Saint Kitts And Nevis - KN", "Saint Lucia - LC", "Saint Pierre And Miquelon - PM", "Saint Vincent And The Grenadines - VC", "Samoa - WS", "San Marino - SM", "Sao Tome And Principe - ST", "Saudi Arabia - SA", "Senegal - SN", "Serbia And Montenegro - CS", "Seychelles - SC", "Sierra Leone - SL", "Singapore - SG", "Slovakia - SK", "Slovenia - SI", "Solomon Islands - SB", "Somalia - SO", "South Africa - ZA", "South Georgia And The South Sandwich Islands - GS", "Spain - ES", "Sri Lanka - LK", "Sudan - SD", "Suriname - SR", "Svalbard And Jan Mayen - SJ", "Swaziland - SZ", "Sweden - SE", "Switzerland - CH", "Syrian Arab Republic - SY", "Taiwan, Province Of China - TW", "Tajikistan - TJ", "Tanzania, United Republic Of - TZ", "Thailand - TH", "Timor-Leste - TL", "Togo - TG", "Tokelau - TK", "Tonga - TO", "Trinidad And Tobago - TT", "Tunisia - TN", "Turkey - TR", "Turkmenistan - TM", "Turks And Caicos Islands - TC", "Tuvalu - TV", "Uganda - UG", "Ukraine - UA", "United Arab Emirates - AE", "United Kingdom - GB", "United States - US", "United States Minor Outlying Islands - UM", "Uruguay - UY", "Uzbekistan - UZ", "Vanuatu - VU", "Venezuela - VE", "Viet Nam - VN", "Virgin Islands, British - VG", "Virgin Islands, U.S. - VI", "Wallis And Futuna - WF", "Western Sahara - EH", "Yemen - YE", "Zambia - ZM", "Zimbabwe - ZW"};
        view.getInputCountry().setModel(new javax.swing.DefaultComboBoxModel(countries));
        view.getInputCountry().setSelectedIndex(57);
    }

    private void initListeners() {
        view.getSaveButton().addActionListener(new SaveButtonListener());
        view.getCancelButton().addActionListener(new CloseButtonListener());
    }

    private class CloseButtonListener implements ActionListener {

        public CloseButtonListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    private class SaveButtonListener implements ActionListener {

        public SaveButtonListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /* GET INPUTS */
            String name = view.getInputName().getText().trim();
            String street = view.getInputStreet().getText().trim();
            String city = view.getInputCity().getText().trim();
            String postcode = view.getInputPostcode().getText().trim();
            String country = view.getInputCountry().getSelectedItem().toString().trim();
            String email = view.getInputEmail().getText().trim();
            String phone = view.getInputPhone().getText().trim();

            /* VALIDATION */
            boolean isCorrectInput = true;
            String errors = "";


            if (name.isEmpty() || !name.contains(" ") || name.length() < 5) {
                isCorrectInput = false;
                errors += "CHYBA: Vložte jméno ve tvaru: jméno[mezera]přijmení";
            }

            String[] temp = name.split(" ");
            String lname = temp[temp.length-1];
            String fname = "";
            for (int i = 0; i < temp.length - 1; i++) {
                fname += temp[i];
            }


            /* SAVE CUSTOMER */
            if (isCorrectInput) {
                CustomerDAO.getInstance().newCustomer(fname, lname, street, city, postcode, country, email, phone);
                dispose();
            }
        }
    }
}

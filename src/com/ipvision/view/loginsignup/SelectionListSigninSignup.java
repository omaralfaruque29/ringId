/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.GetImages;
import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.SeparatorPanel;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Sirat Samyoun
 */
public class SelectionListSigninSignup extends JPanel {

    public static int TYPE_SIGN_IN = 12;
    public static int TYPE_SIGN_UP = 13;
    public static int TYPE_RECOVER_PASS = 14;
    public JPanel seperator, ePanel;
    public JLabel imageLabel;
    public JButton actionPopupButton;
    public SignInSignupCustomPopup countryFlagName_popup;
    private SelectionListSigninSignup instance;
    public JTextField countryCodeTextField, ringIDorMobileorEmailTextField;
    private int number_field_width = 100;
    private UiMethods extra_methods = new UiMethods();
    private static String FORMAT_PNG = ".png";
    public String ENTER_RINGID = "ringID";
    public String ENTER_MOBILE = "Verified Mobile Number";
    public String ENTER_EMAIL = "Verified E-mail";
    public String default_input_text = ENTER_RINGID;
    public static String TXT_RINGID = "ringID";
    public static String CODE_RINGID = "+878";
    public static String TXT_EMAIL = "E-mail";
    public static String CODE_EMAIL = TXT_EMAIL;
    public static String TXT_PHN = "Mobile Number";
    private static String TXT_ENTER = "Enter ";
    public int type;
    public int type_int = MyFnFSettings.VALUE_LOGIN_USER_TYPE;

    public SelectionListSigninSignup(int type) {
        this.instance = this;
        this.type = type;
        if (type == TYPE_SIGN_UP) {
            ENTER_MOBILE = "Mobile Number";
            ENTER_EMAIL = "E-mail";
            default_input_text = ENTER_EMAIL;
            type_int = SettingsConstants.EMAIL_LOGIN;
        } else if (type == TYPE_RECOVER_PASS) {
            type_int = SettingsConstants.RINGID_LOGIN;
        }
        setOpaque(false);
        setLayout(new BorderLayout());
        init();
    }

    public void init() {
        JPanel countryMobileEmail = new RoundedPanel();
        countryMobileEmail.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));;
        countryMobileEmail.setOpaque(false);

        ePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ePanel.setOpaque(false);
        countryMobileEmail.add(ePanel);
        add(countryMobileEmail, BorderLayout.CENTER);

        imageLabel = new JLabel();
        ePanel.add(imageLabel);

        countryFlagName_popup = new SignInSignupCustomPopup(instance);
        countryCodeTextField = DesignClasses.makeTextFieldLimitedTextSize("", 65, 30, MaxLengths.COUNTRY_CODE + 5);
        countryCodeTextField.setOpaque(false);
        countryCodeTextField.setPreferredSize(new Dimension(43, 30));
        countryCodeTextField.setEditable(false);
        countryCodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        countryCodeTextField.setBorder(null);
        countryCodeTextField.addMouseListener(new ContextMenuMouseListener());
        ePanel.add(countryCodeTextField);

        actionPopupButton = new JButton();
        actionPopupButton.setFocusPainted(false);
        actionPopupButton.setContentAreaFilled(false);
        actionPopupButton.setBorder(null);
        actionPopupButton.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
        ePanel.add(actionPopupButton);
        actionPopupButton.addActionListener(actionListener);

        seperator = new SeparatorPanel(1, 35, 6);
        ePanel.add(seperator);

        ringIDorMobileorEmailTextField = DesignClasses.makeTextFieldLimitedTextSize(default_input_text, number_field_width, 30, MaxLengths.EMAIL);
        ringIDorMobileorEmailTextField.setPreferredSize(new Dimension(number_field_width + 60, 30));
        ringIDorMobileorEmailTextField.addMouseListener(new ContextMenuMouseListener());
        ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
        ringIDorMobileorEmailTextField.setToolTipText(TXT_ENTER + default_input_text);
        ringIDorMobileorEmailTextField.setOpaque(false);
        ringIDorMobileorEmailTextField.setBorder(null);
        countryMobileEmail.add(ringIDorMobileorEmailTextField);

        if (type == TYPE_SIGN_UP) {
            countryCodeTextField.setText(CODE_EMAIL);
            imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + TXT_EMAIL + FORMAT_PNG));
        } else {
            countryCodeTextField.setText(CODE_RINGID);
            imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + TXT_RINGID + FORMAT_PNG));
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == actionPopupButton) {
                if (!countryFlagName_popup.isVisible()) {
                    countryFlagName_popup.setVisible(actionPopupButton, 31, -10);
                } else {
                    countryFlagName_popup.hideThis();
                }
            }
        }
    };

    public String getRingIDMobileMail() {
        if (type_int == SettingsConstants.MOBILE_LOGIN) {
            return countryCodeTextField.getText().trim() + ringIDorMobileorEmailTextField.getText().trim();
        } else {
            return ringIDorMobileorEmailTextField.getText().trim();
        }
    }

    private void resetErrorFields() {
        if (type == TYPE_SIGN_UP) {
            LoginUI.getLoginUI().emailVarificationPanel.errorTextArea.setText("");
            LoginUI.getLoginUI().emailVarificationPanel.errorLabel.setIcon(null);
        } else if (type == TYPE_SIGN_IN) {
            LoginUI.getLoginUI().mainLoginPanel.errorTextArea.setText("");
            LoginUI.getLoginUI().mainLoginPanel.errorLabel.setIcon(null);
        } else if (type == TYPE_RECOVER_PASS) {
            LoginUI.getLoginUI().recoverPasswordPanel.errorTextArea.setText("");
            LoginUI.getLoginUI().recoverPasswordPanel.errorLabel.setIcon(null);
        }
    }

    public void setCodeImage() {
        if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.RINGID_LOGIN) {
            imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + TXT_RINGID + FORMAT_PNG));
        } else if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.EMAIL_LOGIN) {
            imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + TXT_EMAIL + FORMAT_PNG));
        } else if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.MOBILE_LOGIN) {
            String countryName = HelperMethods.get_country_name_from_contry_code(MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
            imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + countryName + FORMAT_PNG));
        }
    }

    public void action_mouse_clicked(SignInSignupCustomPopup.CustomMenu menu) {
        resetErrorFields();
        if (menu != null) {
            countryCodeTextField.setText(HelperMethods.get_country_code_from_contry_name(menu.text));
            imageLabel.setIcon(DesignClasses.return_image(menu.imageUrl));
            if (menu.text.equalsIgnoreCase(TXT_RINGID)) {
                type_int = SettingsConstants.RINGID_LOGIN;
                default_input_text = ENTER_RINGID;
                //ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
                //ringIDorMobileorEmailTextField.setText(ENTER_RINGID);
                ringIDorMobileorEmailTextField.setToolTipText(TXT_ENTER + ENTER_RINGID);

            } else if (menu.text.equalsIgnoreCase(TXT_EMAIL)) {
                type_int = SettingsConstants.EMAIL_LOGIN;
                default_input_text = ENTER_EMAIL;
                //ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
                //ringIDorMobileorEmailTextField.setText(ENTER_EMAIL);
                ringIDorMobileorEmailTextField.setToolTipText(TXT_ENTER + ENTER_EMAIL);
            } else {
                type_int = SettingsConstants.MOBILE_LOGIN;
                default_input_text = ENTER_MOBILE;
                // ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
                // ringIDorMobileorEmailTextField.setText(ENTER_MOBILE);
                ringIDorMobileorEmailTextField.setToolTipText(TXT_ENTER + ENTER_MOBILE);
            }
            if (ringIDorMobileorEmailTextField.getForeground() == DefaultSettings.disable_font_color) {
                ringIDorMobileorEmailTextField.setText(default_input_text);
            }
        }
        countryFlagName_popup.hideThis();
        ringIDorMobileorEmailTextField.grabFocus();
    }
}

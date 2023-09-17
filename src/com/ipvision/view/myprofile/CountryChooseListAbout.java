/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.JCustomMenuPopup;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Sirat Samyoun
 */
public class CountryChooseListAbout extends JPanel implements FocusListener {

    public JLabel imageLabel;
    private JButton actionPopupButton;
    private JCustomMenuPopup countryFlagName_popup;
    public JTextField countryCodeTextField, ringIDorMobileorEmailTextField;
    private int number_field_width = 100;
    private UiMethods extra_methods = new UiMethods();
    private static String ENTER_NUMBER = "Add a Number";
    private String default_input_text = ENTER_NUMBER;
    private String s = "";
    private String lastCountryName = "";

    public CountryChooseListAbout() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(190, 20));
        init();
    }

    public void enablePanel() {
        actionPopupButton.setEnabled(true);
        ringIDorMobileorEmailTextField.setEnabled(true);
    }

    public void disablePanel() {
        actionPopupButton.setEnabled(false);
        ringIDorMobileorEmailTextField.setEnabled(false);
    }

    public String getTextMobileNo() {
        return ringIDorMobileorEmailTextField.getText().trim();
    }

    public String getTextMobileDC() {
        return countryCodeTextField.getText().trim();
    }

    public String getTextMobileNowithDC() {
        return countryCodeTextField.getText().trim() + "-" + ringIDorMobileorEmailTextField.getText().trim();
    }

    private void init() {
        JPanel content = new JPanel(new BorderLayout(2, 0));
        add(content, BorderLayout.CENTER);
        content.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        content.setBackground(Color.white);
        JPanel ePanel = new JPanel(new BorderLayout());
        ePanel.setOpaque(false);
        content.add(ePanel, BorderLayout.CENTER);

        imageLabel = new JLabel();
        imageLabel.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + "Bangladesh.png"));
        ePanel.add(imageLabel, BorderLayout.WEST);

        countryFlagName_popup = new JCustomMenuPopup(menuListener, keyListener, JCustomMenuPopup.TYPE_SIGN_IN);
        buildAllMenuItem();
        actionPopupButton = new JButton();
        actionPopupButton.setFocusPainted(false);
        actionPopupButton.setContentAreaFilled(false);
        actionPopupButton.setBorder(null);
        actionPopupButton.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
        actionPopupButton.addActionListener(actionListener);
        ePanel.add(actionPopupButton, BorderLayout.CENTER);

        countryCodeTextField = DesignClasses.makeTextFieldLimitedTextSize("+880", 50, 18, MaxLengths.COUNTRY_CODE + 5);
        countryCodeTextField.setOpaque(false);
        countryCodeTextField.setPreferredSize(new Dimension(35, 20));
        countryCodeTextField.setEditable(false);
        countryCodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        countryCodeTextField.setBorder(null);
        countryCodeTextField.addMouseListener(new ContextMenuMouseListener());
        ePanel.add(countryCodeTextField, BorderLayout.EAST);

        ringIDorMobileorEmailTextField = DesignClasses.makeTextFieldLimitedTextSize(default_input_text, 60, 18, MaxLengths.PHONE_NUMBER);
        ringIDorMobileorEmailTextField.setPreferredSize(new Dimension(117, 20));
        ringIDorMobileorEmailTextField.addMouseListener(new ContextMenuMouseListener());
        ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
        ringIDorMobileorEmailTextField.setToolTipText(default_input_text);
        ringIDorMobileorEmailTextField.setOpaque(false);
        ringIDorMobileorEmailTextField.setBorder(null);
        ringIDorMobileorEmailTextField.addFocusListener(this);
        content.add(ringIDorMobileorEmailTextField, BorderLayout.EAST);
    }

    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                JCustomMenuPopup.CustomMenu menu = countryFlagName_popup.getSelectedMenu();
                action_mouse_clicked(menu);
                selectMenu(null);
            } else {
                char key = (char) e.getKeyCode();
                selectTypedItem("" + key);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    };

    private void selectTypedItem(String str) {
        int maxLength = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        if (s.equals(str)) {
            int indx = getIndexinArray(lastCountryName) + 1;
            if (indx > 0 && indx < maxLength && MyFnFSettings.COUNTRY_MOBILE_CODE[indx][0].startsWith(s)) {
                String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[indx][0];
                lastCountryName = countryName;
                selectMenu(countryName);
                return;
            } else {
                s = str;
                for (int i = 0; i < maxLength; i++) {
                    String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
                    if (countryName.startsWith(s)) {
                        lastCountryName = countryName;
                        selectMenu(countryName);
                        return;
                    }
                }
            }
        } else {
            s = str;
            for (int i = 0; i < maxLength; i++) {
                String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
                if (countryName.startsWith(s)) {
                    lastCountryName = countryName;
                    selectMenu(countryName);
                    return;
                }
            }
        }

    }

    private void action_mouse_clicked(JCustomMenuPopup.CustomMenu menu) {
        if (menu != null) {
            countryCodeTextField.setText(HelperMethods.get_country_code_from_contry_name(menu.text));
            imageLabel.setIcon(DesignClasses.return_image(menu.imageUrl));
            //ringIDorMobileorEmailTextField.setForeground(DefaultSettings.disable_font_color);
            //ringIDorMobileorEmailTextField.setText(default_input_text);
            ringIDorMobileorEmailTextField.setToolTipText(default_input_text);
        }
        countryFlagName_popup.hideThis();
        ringIDorMobileorEmailTextField.grabFocus();
    }

    public void selectMenu(String text) {
        countryFlagName_popup.customMenuSelected = null;
        if (text != null) {
            for (Component menu : countryFlagName_popup.content.getComponents()) {
                JCustomMenuPopup.CustomMenu customMenu = (JCustomMenuPopup.CustomMenu) menu;
                if (customMenu.getName().equalsIgnoreCase(text)) {
                    countryFlagName_popup.customMenuSelected = customMenu;
                } else {
                    customMenu.setMouseExited();
                }
            }
            countryFlagName_popup.customMenuSelected.setMouseEntered();
            countryFlagName_popup.scrollPanel.getVerticalScrollBar().setValue(0);
            int downValue = -1 * (this.getY() - countryFlagName_popup.customMenuSelected.getY());
            // if (selectionList.type == SelectionListSigninSignup.TYPE_SIGN_UP) {
            countryFlagName_popup.scrollPanel.getVerticalScrollBar().setValue(downValue - 130);
            // } else {
            //countryFlagName_popup.scrollPanel.getVerticalScrollBar().setValue(downValue + 250);
            // }
        } else {
            for (Component menu : countryFlagName_popup.content.getComponents()) {
                JCustomMenuPopup.CustomMenu customMenu = (JCustomMenuPopup.CustomMenu) menu;
                customMenu.setMouseExited();
            }
        }
    }

    private int getIndexinArray(String p) {
        int length = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        for (int i = 0; i < length; i++) {
            String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
            if (countryName.equals(p)) {
                return i;
            }
        }
        return -1;
    }

    private void buildAllMenuItem() {
        int length = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        for (int i = 2; i < length; i++) {
            String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
            String imageSource = GetImages.FLAGS_ROOT_FOLDER + countryName + ".png";
            countryFlagName_popup.addMenu(countryName, imageSource);
        }
    }

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            action_mouse_clicked(menu);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }

    };
    ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == actionPopupButton) {
                if (!countryFlagName_popup.isVisible()) {
                    countryFlagName_popup.setVisible(actionPopupButton, 40, -15);
                } else {
                    countryFlagName_popup.hideThis();
                }
            }
        }
    };

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == ringIDorMobileorEmailTextField) {
            if (ringIDorMobileorEmailTextField.getText().equals(default_input_text) || ringIDorMobileorEmailTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, false);
            } else if (ringIDorMobileorEmailTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, true);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == ringIDorMobileorEmailTextField) {
            extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, true);
        }
    }
}

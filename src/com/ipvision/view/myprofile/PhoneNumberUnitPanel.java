/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.service.AddSecondaryMailOrNumber;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.EmailOrMobileVerificationDialog;
import java.awt.Color;

/**
 *
 * @author Sirat Samyoun
 */
public class PhoneNumberUnitPanel extends JPanel {

    public String value;
    public int isVerified;
    private String title;
    private JPanel namePanel, contanier;
    private JPanel colonPanel;
    private JPanel valuePanel;
    private JPanel editTextPanel;
    private JPanel msgShow;
    //public JTextField valuebox;
    public JLabel pleasW;
    JPanel buttonsPanel;
    JPanel p1, p2;
    public JButton okButton;
    public JButton closeButton;
    public int type_int;
    public boolean labelMode;
    public JLabel valuelabel;
    PhoneNumberUnitPanel phoneNumberUnitPanel;
    public CountryChooseListAbout countryChooseList;

    public PhoneNumberUnitPanel(int type_int,
            boolean isOkayed,
            String value,
            int isVerified) {
        this.type_int = type_int;
        this.value = value;
        this.isVerified = isVerified;
        this.labelMode = isOkayed;
        init();
    }

    private boolean isSameasPrimary(String str) {
        String tmp = MyFnFSettings.userProfile.getMobilePhone();
        return str.equals(tmp);
    }

    private boolean isSameasSecondary(String str) {
        if (MyFnFSettings.userProfile.getAdditionalInfo() != null && !MyFnFSettings.userProfile.getAdditionalInfo().isEmpty()) {
            for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                if (str.equals(ad.getNameOfEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void init() {
        this.phoneNumberUnitPanel = this;
        title = "";
        if (type_int == 1) {
            title = "Secondary Number";
        }
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel sizePanel1 = new JPanel();
        sizePanel1.setOpaque(false);
        sizePanel1.setPreferredSize(new Dimension(600, 1));
        this.add(sizePanel1, BorderLayout.NORTH);

        contanier = new JPanel();
        contanier.setBackground(Color.WHITE);
        contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
        contanier.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(contanier, BorderLayout.CENTER);

        JLabel name = DesignClasses.makeJLabel_normal(title, 13, DefaultSettings.BLACK_FONT);
        namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        namePanel.setOpaque(false);//Background(Color.red); 
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = DesignClasses.makeJLabel_normal(":", 14, DefaultSettings.BLACK_FONT);
        colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        colonPanel.setOpaque(false);
        if (type_int == 1) {
            colonPanel.add(colon, BorderLayout.CENTER);
        }
        valuelabel = DesignClasses.makeJLabel_normal(value, 14, DefaultSettings.BLACK_FONT);
        countryChooseList = new CountryChooseListAbout();
        //valuebox = DesignClasses.makeTextFieldLimitedTextSize(value, 100, 20, MaxLengths.PHONE_NUMBER);
        valuePanel = new JPanel(new BorderLayout());
        valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        valuePanel.setOpaque(false);
        if (labelMode) {
            valuePanel.add(valuelabel, BorderLayout.CENTER);
        } else {
            valuePanel.add(countryChooseList, BorderLayout.CENTER);
        }
        msgShow = new JPanel(new BorderLayout());
        msgShow.setPreferredSize(new Dimension(100, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        msgShow.setOpaque(false);
        pleasW = DesignClasses.makeJLabel_normal("", 10, DefaultSettings.errorLabelColor);
        if (labelMode) {
            if (isVerified == 1) {
                pleasW.setText("");
                pleasW.setCursor(null);
                pleasW.addMouseListener(null);
            } else if (isVerified == 0) {
                pleasW.setText("Not Verified");
                pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pleasW.addMouseListener(mouseListener);
            }
        }
        msgShow.add(pleasW, BorderLayout.CENTER);
        editTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        editTextPanel.setOpaque(false);
        editTextPanel.setPreferredSize(new Dimension(133, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        p1 = new JPanel(new BorderLayout());
        p2 = new JPanel(new BorderLayout());
        p1.setOpaque(false);
        p2.setOpaque(false);
        p1.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        p2.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        editTextPanel.add(p1);
        editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
        editTextPanel.add(p2);
        okButton = DesignClasses.createImageButton(GetImages.OK_MINI, GetImages.OK_MINI_H, "Save");
        closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Close");
        okButton.addMouseListener(mouseListener);
        closeButton.addMouseListener(mouseListener);
        if (!labelMode) {
            p1.add(okButton, BorderLayout.CENTER);
            p2.add(closeButton, BorderLayout.CENTER);
        }

        JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameHolder.add(namePanel);
        JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colonHolder.add(colonPanel);
        JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        msgShowHolder.add(msgShow);
        JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        privacyandEditHolder.add(editTextPanel);

        nameHolder.setOpaque(false);
        colonHolder.setOpaque(false);
        msgShowHolder.setOpaque(false);
        privacyandEditHolder.setOpaque(false);

        contanier.add(nameHolder);
        contanier.add(colonHolder);
        contanier.add(valuePanel);
        contanier.add(msgShowHolder);
        contanier.add(privacyandEditHolder);
        contanier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                contanier.setBackground(RingColorCode.SEARCH_PANEL_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contanier.setBackground(Color.WHITE);
            }
        });
    }

    public void action_cancel_button() {
        MyProfilePhoneNumberPanel emp = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getPhoneNumberPanel();
        if (emp != null) {
            emp.remove(emp.getComponentCount() - 1);
            emp.con.gridy--;
            emp.countSec--;
            emp.revalidate();
            emp.repaint();
        }
    }

    public void action_save_secondary_phone() {
        pleasW.setText("Not Verified");
        pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isVerified == 0) {
            pleasW.removeMouseListener(mouseListener);
        }
        pleasW.addMouseListener(mouseListener);
        p1.removeAll();
        p2.removeAll();
        p1.revalidate();
        p1.repaint();
        p2.revalidate();
        p2.repaint();
        valuePanel.removeAll();
        valuelabel = DesignClasses.makeJLabel_normal(countryChooseList.getTextMobileNowithDC(), 14, DefaultSettings.BLACK_FONT);
        valuePanel.add(valuelabel, BorderLayout.CENTER);
        valuePanel.revalidate();
        valuePanel.repaint();
    }
    public MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == pleasW) {
                String str = value;
                if (value == null || value.trim().length() < 1) {
                    str = countryChooseList.getTextMobileNowithDC();
                }
                new EmailOrMobileVerificationDialog(str, phoneNumberUnitPanel);
            } else if (e.getSource() == closeButton) {
                pleasW.setText("");
                labelMode = true;
                action_cancel_button();
            } else if (e.getSource() == okButton) {
                String val = countryChooseList.getTextMobileNowithDC();//valuebox.getText().trim();
                String[] parts = val.split("-");
                if (HelperMethods.isValidNumber(parts[1])) {
                    if (!isSameasPrimary(val) && !isSameasSecondary(val)) {
                        labelMode = true;
                        new AddSecondaryMailOrNumber(val, phoneNumberUnitPanel).start();
                    } else {
                        pleasW.setText("Number already exists");
                    }
                } else {
                    pleasW.setText("Invalid Number");
                }
            }
        }
    };
}

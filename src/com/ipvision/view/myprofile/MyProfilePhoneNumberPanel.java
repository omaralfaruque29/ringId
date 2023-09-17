/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.AppConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.service.ChangeMobileNumber;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.EmailOrMobileVerificationDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Sirat Samyoun
 */
public class MyProfilePhoneNumberPanel extends JPanel {

    public GridBagConstraints con;
    private final JButton addButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add a Phone Number");
    private PhoneNumberUnitPanel secondary;
    private PhoneNumberUnitPanel last = null;
    private PhoneNumberUnitPanel addedPanel;
    private JPanel topPanel, primaryPanel, valuePanel,contanier;
    private int secLen = 0;
    public int countSec = 0;
    public String primaryMblwithDC = "";
    public int isPrimaryVerified;
    public JLabel pleasW, editThis, valueLabel;
    public JButton okButton, closeButton, privacyButton;
    JPanel p1, p2;
    //public JTextField valuebox;
    public CountryChooseListAbout countryChooseList;
    MyProfilePhoneNumberPanel myProfilePhoneNumberPanel;
    public MyProfileVisibilities phonevisibility;
    public String prev_pleasW;

    private int numberSecNumbers() {
        if (MyFnFSettings.userProfile.getAdditionalInfo() != null && !MyFnFSettings.userProfile.getAdditionalInfo().isEmpty()) {
            int count = 0;
            for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                if (!HelperMethods.isValidEmail(ad.getNameOfEmail())) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }

    private boolean isSameasPrimary(String str) {
        String tmp = MyFnFSettings.userProfile.getMobilePhoneDialingCode() + "-" + MyFnFSettings.userProfile.getMobilePhone();
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

    public void checkAddButtonVisibility() {
        if (numberSecNumbers() <= 2) {
            addButton.setVisible(true);
        } else {
            addButton.setVisible(false);
        }
    }

    public MyProfilePhoneNumberPanel() {
        myProfilePhoneNumberPanel = this;
        this.phonevisibility = new MyProfileVisibilities(AppConstants.CONS_MOBILE_PHONE);
        setOpaque(false);
        setVisible(true);
        setLayout(new GridBagLayout());
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(600, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(590, 30));
        //titlePanel.setBackground(Color.BLUE);
        titlePanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        titlePanel.add(DesignClasses.makeJLabel_normal("Phone Number", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        titlePanel.add(addButton, BorderLayout.EAST);
        topPanel.add(Box.createRigidArea(new Dimension(9, 0)));
        topPanel.add(titlePanel);
        add(topPanel, con);
        con.gridy++;
        isPrimaryVerified = -1;
        if (MyFnFSettings.userProfile.getMobilePhone() != null
                && MyFnFSettings.userProfile.getMobilePhone().trim().length() > 0
                && MyFnFSettings.userProfile.getMobilePhoneDialingCode() != null
                && MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim().length() > 0
                && MyFnFSettings.userProfile.getIsMobileNumberVerified() != null) {
            primaryMblwithDC = MyFnFSettings.userProfile.getMobilePhoneDialingCode() + "-" + MyFnFSettings.userProfile.getMobilePhone();
            isPrimaryVerified = MyFnFSettings.userProfile.getIsMobileNumberVerified();
        }
        last = null;
        add(getPrimaryPanel(), con);
        con.gridy++;

        if (isPrimaryVerified >= 0 && primaryMblwithDC.trim().length() > 0) {
            secLen = numberSecNumbers();
            countSec = secLen;
            if (secLen <= 2) {
                addButton.setVisible(true);
            } else {
                addButton.setVisible(false);
            }
            if (secLen > 0) {
                int i = 1;
                for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                    if (!HelperMethods.isValidEmail(ad.getNameOfEmail())) {
                        secondary = new PhoneNumberUnitPanel(i++, true, ad.getNameOfEmail(), ad.getIsVerified());
                        last = secondary;
                        add(secondary, con);
                        con.gridy++;
                    }
                }
            }
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    secLen = numberSecNumbers();
                    if (countSec <= 2) {
                        if (last == null || last.labelMode) {
                            if (secLen == 0) {
                                addedPanel = new PhoneNumberUnitPanel(1, false, "", 0);
                            } else {
                                addedPanel = new PhoneNumberUnitPanel(2, false, "", 0);
                            }
                            countSec++;
                            last = addedPanel;
                            add(addedPanel, con);
                            con.gridy++;
                            addedPanel.revalidate();
                            addedPanel.repaint();
                        }
                    }
                }
            });
        } else {
            addButton.setVisible(false);
        }
    }

    public JPanel getPrimaryPanel() {
        primaryPanel = new JPanel();
        primaryPanel.setLayout(new BorderLayout());
        primaryPanel.setOpaque(false);

        JPanel sizePanel1 = new JPanel();
        sizePanel1.setOpaque(false);
        sizePanel1.setPreferredSize(new Dimension(600, 1));
        primaryPanel.add(sizePanel1, BorderLayout.NORTH);

        contanier = new JPanel();
        contanier.setBackground(Color.WHITE);
        contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
        contanier.add(Box.createRigidArea(new Dimension(5, 0)));
        primaryPanel.add(contanier, BorderLayout.CENTER);

        JLabel name = DesignClasses.makeJLabel_normal("Primary Number", 13, DefaultSettings.BLACK_FONT);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        namePanel.setOpaque(false);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = DesignClasses.makeJLabel_normal(":", 14, DefaultSettings.BLACK_FONT);
        JPanel colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        colonPanel.setOpaque(false);
        colonPanel.add(colon, BorderLayout.CENTER);

        valueLabel = DesignClasses.makeJLabel_normal(primaryMblwithDC, 14, DefaultSettings.BLACK_FONT);
        valuePanel = new JPanel(new BorderLayout());
        valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        valuePanel.setOpaque(false);
        valuePanel.add(valueLabel, BorderLayout.CENTER);

        JPanel msgShow = new JPanel(new BorderLayout());
        msgShow.setPreferredSize(new Dimension(100, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        msgShow.setOpaque(false);

        String privacy = StaticFields.PRIVACY_PUBLIC;
        String buttonPrivacy = GetImages._PUBLIC;
        if (MyFnFSettings.userProfile.getMobilePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
            privacy = StaticFields.PRIVACY_ONLY_FRIEND;
            buttonPrivacy = GetImages._ONLY_FRIEND;
        } else if (MyFnFSettings.userProfile.getMobilePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
            privacy = StaticFields.PRIVACY_ONLY_ME;
            buttonPrivacy = GetImages._ONLY_ME;
        }
        pleasW = DesignClasses.makeJLabel_normal("", 10, DefaultSettings.errorLabelColor);
        if (isPrimaryVerified == 0) {
            pleasW.setText("Not Verified");
            pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pleasW.addMouseListener(mouseListener);
        }
        msgShow.add(pleasW, BorderLayout.CENTER);

        privacyButton = DesignClasses.createImageButton(buttonPrivacy, buttonPrivacy, privacy);//BasicArrowButton.SOUTH);
        privacyButton.addMouseListener(mouseListener);
        editThis = DesignClasses.create_image_label_with_preferredSize(10, 10, GetImages.PEN);
        editThis.setToolTipText("Edit Primary Number");
        editThis.addMouseListener(mouseListener);
        okButton = DesignClasses.createImageButton(GetImages.OK_MINI, GetImages.OK_MINI_H, "Save");
        okButton.addMouseListener(mouseListener);
        closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Close");
        closeButton.addMouseListener(mouseListener);

        JPanel editTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        editTextPanel.setOpaque(false);
        editTextPanel.setPreferredSize(new Dimension(133, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        p1 = new JPanel(new BorderLayout());
        p2 = new JPanel(new BorderLayout());
        p1.setOpaque(false);
        p2.setOpaque(false);
        p1.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        p2.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        p1.add(privacyButton, BorderLayout.CENTER);
        p2.add(editThis, BorderLayout.CENTER);
        editTextPanel.add(p1);
        editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
        editTextPanel.add(p2);

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
                if (editThis != null) {
                    editThis.setVisible(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contanier.setBackground(Color.WHITE);
                if (editThis != null) {
                    editThis.setVisible(false);
                }
            }
        });
        if (primaryMblwithDC.trim().length() < 1) {
            privacyButton.setVisible(false);
        }
        return primaryPanel;
    }

    public MouseAdapter mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == pleasW) {
                new EmailOrMobileVerificationDialog(primaryMblwithDC, myProfilePhoneNumberPanel);
            } else if (e.getSource() == editThis) {
                valuePanel.removeAll();
                //valuebox = DesignClasses.makeTextFieldLimitedTextSize(primaryMblwithDC, 100, 20, MaxLengths.EMAIL);
                // if (ringIDMobileMailChooseList1 == null) {
                countryChooseList = new CountryChooseListAbout();
                if (MyFnFSettings.userProfile.getMobilePhone() != null
                        && MyFnFSettings.userProfile.getMobilePhone().trim().length() > 0
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode() != null
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim().length() > 0) {
                    countryChooseList.countryCodeTextField.setText(MyFnFSettings.userProfile.getMobilePhoneDialingCode());
                    countryChooseList.ringIDorMobileorEmailTextField.setText(MyFnFSettings.userProfile.getMobilePhone());
                    String countryName = HelperMethods.get_country_name_from_contry_code(MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim());
                    String imageSource = GetImages.FLAGS_ROOT_FOLDER + countryName + ".png";
                    countryChooseList.imageLabel.setIcon(DesignClasses.return_image(imageSource));
                    countryChooseList.ringIDorMobileorEmailTextField.setForeground(null);
                }
                //}MyFnFSettings.userProfile.getMobilePhoneDialingCode()
                valuePanel.add(countryChooseList, BorderLayout.CENTER);
                valuePanel.revalidate();
                valuePanel.repaint();
                prev_pleasW = pleasW.getText();
                pleasW.setText("");
                //pleasW.setVisible(false);
                p1.removeAll();
                p2.removeAll();
                p1.add(okButton, BorderLayout.CENTER);
                p2.add(closeButton, BorderLayout.CENTER);
                p1.revalidate();
                p1.repaint();
                p2.revalidate();
                p2.repaint();
            } else if (e.getSource() == closeButton) {
                valuePanel.removeAll();
                JLabel valuelabel = DesignClasses.makeJLabel_normal(primaryMblwithDC, 14, DefaultSettings.BLACK_FONT);
                valuePanel.add(valuelabel, BorderLayout.CENTER);
                valuePanel.revalidate();
                valuePanel.repaint();
                pleasW.setText(prev_pleasW);
                p1.removeAll();
                p2.removeAll();
                p1.add(privacyButton, BorderLayout.CENTER);
                p2.add(editThis, BorderLayout.CENTER);
                p1.revalidate();
                p1.repaint();
                p2.revalidate();
                p2.repaint();
            } else if (e.getSource() == okButton) {
                String val = countryChooseList.getTextMobileNowithDC();//valuebox.getText().trim();
                String[] parts = val.split("-");
                if (HelperMethods.isValidNumber(parts[1])) {
                    if (!isSameasPrimary(val) && !isSameasSecondary(val)) {
                        new ChangeMobileNumber(parts[1], parts[0], myProfilePhoneNumberPanel).start();
                    } else {
                        //prev_pleasW = pleasW.getText();
                        pleasW.setText("Number exists!");
                    }
                } else {
                    pleasW.setText("Invalid number!");
                }
            } else if (e.getSource() == privacyButton) {
                if (InternetUnavailablityCheck.isInternetAvailable) {
                    phonevisibility.show(privacyButton, privacyButton.getX(), privacyButton.getY() + 10);
                }
            }
//            if (value.trim().length() < 1 && privacyButton != null) {
//                privacyButton.setVisible(false);
//            }
        }

    };

    public void action_save_primary_number() {
        pleasW.setText("Not Verified");
        pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPrimaryVerified == 0) {
            pleasW.removeMouseListener(mouseListener);
        }
        pleasW.addMouseListener(mouseListener);
        p1.removeAll();
        p2.removeAll();
        p1.add(privacyButton, BorderLayout.CENTER);
        p2.add(editThis, BorderLayout.CENTER);
        p1.revalidate();
        p1.repaint();
        p2.revalidate();
        p2.repaint();
        valuePanel.removeAll();
        valueLabel = DesignClasses.makeJLabel_normal(countryChooseList.getTextMobileNowithDC(), 14, DefaultSettings.BLACK_FONT);//valuebox.getText().trim()
        valuePanel.add(valueLabel, BorderLayout.CENTER);
        valuePanel.revalidate();
        valuePanel.repaint();
    }
}

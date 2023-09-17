/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.AddSecondaryMailOrNumber;
import com.ipvision.service.ChangeEmail;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.EmailOrMobileVerificationDialog;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import java.awt.Color;

/**
 *
 * @author Sirat Samyoun
 */
public class CreateEmailPanel extends JPanel implements FocusListener {

    public String value;
    public int isVerified;
    private int fieldCons;
    private String title;
    private JPanel namePanel;
    private JPanel colonPanel;
    private JPanel valuePanel;
    private JPanel editTextPanel;
    private JPanel msgShow;
    public JTextField valuebox;
    private UiMethods extra_methods = new UiMethods();
    private String DEFAULT_TXT = "Add a Mail";
    public JLabel pleasW;
    JPanel buttonsPanel, contanier;
    JPanel p1, p2;
    public JButton okButton;
    public JButton closeButton;
    JLabel editThis;
    public int type_int;
    public boolean isOkayed;
    // MyProfileEmailPanel emailPanel;
    public JButton privacyButton;
    public MyProfileVisibilities emailvisibility;
    // private CreateEmailPanel instance;
    public JLabel valuelabel1;

    public CreateEmailPanel(int type_int,
            boolean isOkayed,
            String value,
            int isVerified) {
        this.type_int = type_int;
        this.value = value;
        this.isVerified = isVerified;
        this.fieldCons = AppConstants.CONS_EMAIL;
        this.isOkayed = isOkayed;
        this.emailvisibility = new MyProfileVisibilities(AppConstants.CONS_EMAIL);
        //  this.instance = this;
        init();
    }

    public boolean isSameasPrimary(String mail) {
        return mail.equals(MyFnFSettings.userProfile.getEmail());
    }

    public boolean isSameasSecondary(String mail) {
        if (MyFnFSettings.userProfile.getAdditionalInfo() != null) {
            int secLen = MyFnFSettings.userProfile.getAdditionalInfo().size();
            for (int i = 0; i < secLen; i++) {
                String secMail = MyFnFSettings.userProfile.getAdditionalInfo().get(i).getNameOfEmail();
                if (mail.equals(secMail)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void init() {
        if (type_int == 0) {
            title = "Primary mail";
        } else if (type_int == 1) {
            title = "Secondary mail";
        } else {
            title = "";
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
        name.addFocusListener(this);
        namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        namePanel.setOpaque(false);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = DesignClasses.makeJLabel_normal(":", 14, DefaultSettings.BLACK_FONT);
        colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        colonPanel.setOpaque(false);
        if (type_int == 0 || type_int == 1) {
            colonPanel.add(colon, BorderLayout.CENTER);
        }
        JLabel valuelabel = DesignClasses.makeJLabel_normal(value, 14, DefaultSettings.BLACK_FONT);
        valuebox = DesignClasses.makeTextFieldLimitedTextSize(value, 100, 20, MaxLengths.EMAIL);
        if (value.equals(DEFAULT_TXT)) {
            valuebox.setForeground(DefaultSettings.disable_font_color);
        }
        valuebox.addFocusListener(this);
        valuePanel = new JPanel(new BorderLayout());
        valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        valuePanel.setOpaque(false);
        if (isOkayed) {
            valuePanel.add(valuelabel, BorderLayout.CENTER);
        } else {
            valuePanel.add(valuebox, BorderLayout.CENTER);
        }
        msgShow = new JPanel(new BorderLayout());
        msgShow.setPreferredSize(new Dimension(100, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        msgShow.setOpaque(false);

        String privacy = StaticFields.PRIVACY_PUBLIC;
        String buttonPrivacy = GetImages._PUBLIC;
        if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
            privacy = StaticFields.PRIVACY_ONLY_FRIEND;
            buttonPrivacy = GetImages._ONLY_FRIEND;
        } else if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
            privacy = StaticFields.PRIVACY_ONLY_ME;
            buttonPrivacy = GetImages._ONLY_ME;
        }

        pleasW = DesignClasses.makeJLabel_normal("", 10, DefaultSettings.errorLabelColor);
        if (isOkayed) {
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
        privacyButton = DesignClasses.createImageButton(buttonPrivacy, buttonPrivacy, privacy);//BasicArrowButton.SOUTH);
        if (type_int == 0) {
            p1.add(privacyButton, BorderLayout.CENTER);
            editTextPanel.add(p1);
            editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
        }
        privacyButton.addMouseListener(mouseListener);
        editThis = DesignClasses.create_image_label_with_preferredSize(10, 10, GetImages.PEN);
        editThis.setToolTipText("Edit Primary Mail");
        okButton = DesignClasses.createImageButton(GetImages.OK_MINI, GetImages.OK_MINI_H, "Save");
        closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Close");
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);

        if (type_int > 0) {
            p1.add(okButton, BorderLayout.CENTER);
            p2.add(closeButton, BorderLayout.CENTER);
        }
        if (isOkayed) {
            if (type_int == 0) {
                if (InternetUnavailablityCheck.isInternetAvailable) {
                    //   editTextPanel.add(editThis, BorderLayout.CENTER);
                    p2.add(editThis, BorderLayout.CENTER);
                    editTextPanel.add(p2);
                }
            }
        } else {
            editTextPanel.add(p1);
            editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
            editTextPanel.add(p2);
        }
        okButton.addMouseListener(mouseListener);
        closeButton.addMouseListener(mouseListener);
        editThis.addMouseListener(mouseListener);

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
                if (type_int == 0 && editThis != null) {
                    editThis.setVisible(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contanier.setBackground(Color.WHITE);
                if (type_int == 0 && editThis != null) {
                    editThis.setVisible(false);
                }
            }
        });
        if (value.trim().length() < 1 && privacyButton != null) {
            privacyButton.setVisible(false);
        }
    }

    public void focusGained(FocusEvent e) {
        if (e.getSource() == valuebox) {
            if (valuebox.getText().equals(DEFAULT_TXT) || valuebox.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(valuebox, DEFAULT_TXT, false);
            } else if (valuebox.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(valuebox, DEFAULT_TXT, true);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    private CreateEmailPanel getCreateEmailPanel() {
        return this;
    }

    public void action_cancel_Button() {
        MyProfileEmailPanel emp = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEmailPanel();
        if (emp != null) {
            emp.remove(emp.getComponentCount() - 1);
            emp.con.gridy--;
            emp.countSec--;
            emp.revalidate();
            emp.repaint();
        }
    }
    public MouseAdapter mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == pleasW) {
                if (type_int == 0) {
                    new EmailOrMobileVerificationDialog(MyFnFSettings.userProfile.getEmail(), getCreateEmailPanel());
                } else {
                    new EmailOrMobileVerificationDialog(value, getCreateEmailPanel());
                }
            } else if (e.getSource() == editThis) {
                isOkayed = false;
                valuePanel.removeAll();
                valuebox.setText(MyFnFSettings.userProfile.getEmail());
                valuePanel.add(valuebox, BorderLayout.CENTER);
                valuePanel.revalidate();
                valuePanel.repaint();
                pleasW.setText("");
                editTextPanel.removeAll();
                p1.removeAll();
                p2.removeAll();
                p1.add(okButton, BorderLayout.CENTER);
                p2.add(closeButton, BorderLayout.CENTER);
                editTextPanel.add(p1);
                editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
                editTextPanel.add(p2);
                p1.revalidate();
                p1.repaint();
                p2.revalidate();
                p2.repaint();
                editTextPanel.revalidate();
                editTextPanel.repaint();
            } else if (e.getSource() == closeButton) {
                pleasW.setText("");
                isOkayed = true;
                if (type_int == 0) {
                    if (isVerified == 0) {
                        pleasW.setText("Not Verified");
                    }
                    editTextPanel.removeAll();
                    p1.removeAll();
                    p2.removeAll();
                    p1.add(privacyButton, BorderLayout.CENTER);
                    p2.add(editThis, BorderLayout.CENTER);
                    editTextPanel.add(p1);
                    editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
                    editTextPanel.add(p2);
                    p1.revalidate();
                    p1.repaint();
                    p2.revalidate();
                    p2.repaint();
                    editTextPanel.revalidate();
                    editTextPanel.repaint();
                    valuePanel.removeAll();
                    JLabel valuelabel1;
                    valuelabel1 = DesignClasses.makeJLabel_normal(MyFnFSettings.userProfile.getEmail(), 14, DefaultSettings.BLACK_FONT);
                    valuePanel.add(valuelabel1, BorderLayout.CENTER);
                    valuePanel.revalidate();
                    valuePanel.repaint();
                } else {
                    action_cancel_Button();
                }
            } else if (e.getSource() == okButton) {
                String val = valuebox.getText();
                if (HelperMethods.isValidEmail(val)) {
                    if (!isSameasPrimary(val) && !isSameasSecondary(val)) {
                        isOkayed = true;
                        // editTextPanel.removeAll();
                        editTextPanel.removeAll();
                        p1.removeAll();
                        p2.removeAll();
                        if (type_int == 0) {
                            p1.add(privacyButton, BorderLayout.CENTER);
                            p2.add(editThis, BorderLayout.CENTER);
                            editTextPanel.add(p1);
                            editTextPanel.add(Box.createRigidArea(new Dimension(51, 1)));
                            editTextPanel.add(p2);
                        }
                        p1.revalidate();
                        p1.repaint();
                        p2.revalidate();
                        p2.repaint();
                        editTextPanel.revalidate();
                        editTextPanel.repaint();
                        valuePanel.removeAll();
                        valuelabel1 = DesignClasses.makeJLabel_normal(val, 14, DefaultSettings.BLACK_FONT);
                        valuePanel.add(valuelabel1, BorderLayout.CENTER);
                        if (type_int == 0) {
                            new ChangeEmail(val, okButton, getCreateEmailPanel()).start();
                        } else {
                            //new AddMoreMails(val, okButton, valuelabel1, getCreateEmailPanel()).start();
                            new AddSecondaryMailOrNumber(val, getCreateEmailPanel()).start();
                        }
                        valuePanel.revalidate();
                        valuePanel.repaint();
                    } else {
                        pleasW.setText("mail already exists");
                    }
                } else {
                    pleasW.setText("Invalid mail");
                }
            } else if (e.getSource() == privacyButton) {
                if (InternetUnavailablityCheck.isInternetAvailable) {
                    emailvisibility.show(privacyButton, privacyButton.getX(), privacyButton.getY() + 10);
                    editTextPanel.revalidate();
                    editTextPanel.repaint();
                }
            }
            if (value.trim().length() < 1 && privacyButton != null) {
                privacyButton.setVisible(false);
            }
        }

    };
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.singinsignup.VerifyPrimaryEmail;
import com.ipvision.service.singinsignup.VerifyPrimaryMobileNumber;
import com.ipvision.service.singinsignup.VerifySecondaryMailOrNumber;
import com.ipvision.view.myprofile.CreateEmailPanel;
import com.ipvision.view.myprofile.MyProfilePhoneNumberPanel;
import com.ipvision.view.myprofile.PhoneNumberUnitPanel;

/**
 *
 * @author Sirat Samyoun
 */
public class EmailOrMobileVerificationDialog extends JOptionPanelBasicsShadow implements ActionListener, FocusListener {

    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private BufferedImage TXT_ICON = DesignClasses.return_buffer_image(GetImages.LOGIN_TEXTBOX);
    private BufferedImage TXT_ICON_H = DesignClasses.return_buffer_image(GetImages.LOGIN_TEXTBOX_H);
    private BufferedImage CODE_ICON = DesignClasses.return_buffer_image(GetImages.CODE_TEXTBOX);
    private BufferedImage CODE_ICON_H = DesignClasses.return_buffer_image(GetImages.CODE_TEXTBOX_H);
    private String DEFAULT_TXT = "E-mail";
    final String DEFAULT_CODE = "Code";
    private UiMethods extra_methods = new UiMethods();
    public Object actionSource;
    private JPanel emailSendPanel;
    private JPanel buttonPanel;
    private ImagePane mailPanel;
    private ImagePane codePanel;
    private JButton sendButton;
    private JButton cancelButton;
    private JButton okButton;
    private JTextField txtEmail;
    private JTextField txtCode;
    private String mailOrMobile;// = "E-mail";
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private CreateEmailPanel createEmailAboutPanelPrivacy = null;
    private PhoneNumberUnitPanel phoneNumberUnitPanel = null;
    private MyProfilePhoneNumberPanel myProfilePhoneNumberPanel = null;

    public EmailOrMobileVerificationDialog(String mailOrMobile, CreateEmailPanel createEmailAboutPanelPrivacy) {
        DEFAULT_TXT = "Email Verification";
        setTitle(DEFAULT_TXT);
        this.mailOrMobile = mailOrMobile;
        this.createEmailAboutPanelPrivacy = createEmailAboutPanelPrivacy;
        setMinimumSize(new Dimension(width + 20, height + 40));
        componentContent.setBorder(new EmptyBorder(padding + 20, padding, padding, padding));
        initContents();
    }

    public EmailOrMobileVerificationDialog(String mailOrMobile, PhoneNumberUnitPanel phoneNumberUnitPanel) {
        DEFAULT_TXT = "Mobile Verification";
        setTitle(DEFAULT_TXT);
        this.mailOrMobile = mailOrMobile;
        this.phoneNumberUnitPanel = phoneNumberUnitPanel;
        setMinimumSize(new Dimension(width + 20, height + 40));
        componentContent.setBorder(new EmptyBorder(padding + 20, padding, padding, padding));
        initContents();
    }

    public EmailOrMobileVerificationDialog(String mailOrMobile, MyProfilePhoneNumberPanel myProfilePhoneNumberPanel) {
        DEFAULT_TXT = "Mobile Verification";
        setTitle(DEFAULT_TXT);
        this.mailOrMobile = mailOrMobile;
        this.myProfilePhoneNumberPanel = myProfilePhoneNumberPanel;
        setMinimumSize(new Dimension(width + 20, height + 40));
        componentContent.setBorder(new EmptyBorder(padding + 20, padding, padding, padding));
        initContents();
    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 20, 2, 2);
        JPanel errorPanel = new JPanel(new BorderLayout(5, 0));
        errorPanel.setOpaque(false);
        content.add(errorPanel, c);
        c.gridy += 1;

        errorLabel = DesignClasses.makeAncorLabel("", 0, 12);
        errorTextArea = new JTextArea();
        errorTextArea.setEnabled(false);
        errorTextArea.setColumns(27);
        errorTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setBorder(null);
        errorTextArea.setOpaque(false);
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.add(errorTextArea, BorderLayout.CENTER);

        emailSendPanel = new JPanel(new BorderLayout(5, 0));
        emailSendPanel.setOpaque(false);
        mailPanel = new ImagePane();
        mailPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mailPanel.setPreferredSize(new Dimension(175, 25));
//        if (mailOrMobile.equals("E-mail")) {
//            if (TXT_ICON != null) {
//                mailPanel.setImage(TXT_ICON);
//                TXT_ICON.flush();
//            }
//        } else {
        if (TXT_ICON_H != null) {
            mailPanel.setImage(TXT_ICON_H);
            TXT_ICON_H.flush();
        }
//        }
        sendButton = new JButton();
        sendButton = new RoundedCornerButton("Send", "Send");
        sendButton.addActionListener(this);
        emailSendPanel.add(mailPanel, BorderLayout.WEST);
        emailSendPanel.add(sendButton, BorderLayout.EAST);
        content.add(emailSendPanel, c);
        c.gridy += 1;
        codePanel = new ImagePane();
        codePanel.setLayout(new BorderLayout());
        codePanel.setPreferredSize(new Dimension(50, 5));
        codePanel.setOpaque(false);
        if (CODE_ICON != null) {
            codePanel.setImage(CODE_ICON);
            CODE_ICON.flush();
        }
        content.add(codePanel, c);
        c.gridy += 1;

        content.add(Box.createRigidArea(new Dimension(0, 5)), c);
        c.gridy++;
        buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setOpaque(false);
        okButton = new RoundedCornerButton("Ok", "Ok");
        okButton.addActionListener(this);
        cancelButton = new RoundedCornerButton("Cancel", "Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(Box.createRigidArea(new Dimension(8, 0)), BorderLayout.WEST);
        buttonPanel.add(okButton, BorderLayout.CENTER);
        buttonPanel.add(cancelButton, BorderLayout.EAST);
        content.add(buttonPanel, c);

        txtEmail = DesignClasses.makeTextFieldLimitedTextSize(mailOrMobile, 170, 25, MaxLengths.EMAIL);
        txtEmail.setToolTipText(DEFAULT_TXT);
        txtEmail.setEditable(false);
        txtEmail.setOpaque(false);
        txtEmail.setBorder(null);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(txtEmail.getBorder(), BorderFactory.createEmptyBorder(5, 7, 3, 5)));
        txtEmail.addFocusListener(this);
        mailPanel.add(txtEmail);

        txtCode = DesignClasses.makeTextFieldLimitedTextSize(DEFAULT_CODE, 150, 25, MaxLengths.CODE);
        txtCode.setToolTipText(DEFAULT_CODE);
        txtCode.setForeground(DefaultSettings.disable_font_color);
        txtCode.setOpaque(false);
        txtCode.setBorder(null);
        txtCode.setBorder(BorderFactory.createCompoundBorder(txtCode.getBorder(), BorderFactory.createEmptyBorder(5, 7, 3, 5)));
        txtCode.addFocusListener(this);
        codePanel.add(txtCode);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            if (createEmailAboutPanelPrivacy != null) {
                if (MyFnFSettings.userProfile.getEmail().equalsIgnoreCase(txtEmail.getText())) {
                    new VerifyPrimaryEmail(txtEmail.getText(), null, errorLabel, errorTextArea, sendButton, okButton, cancelButton).start();
                } else {
                    new VerifySecondaryMailOrNumber(txtEmail.getText(), errorLabel, errorTextArea, sendButton, okButton, cancelButton).start();
                }
            } else if (myProfilePhoneNumberPanel != null) {
                String[] parts = txtEmail.getText().split("-");
                new VerifyPrimaryMobileNumber(parts[1], parts[0], null, errorLabel, errorTextArea, sendButton, okButton, cancelButton).start();
            } else if (phoneNumberUnitPanel != null) { //send secNum
                String ml = txtEmail.getText();
                String mdc = "+880";
                if (txtEmail.getText().contains("-")) {
                    String[] parts = txtEmail.getText().split("-");
                    ml = parts[1];
                    mdc = parts[0];
                }
                new VerifySecondaryMailOrNumber(ml, mdc, null, errorLabel, errorTextArea, sendButton, okButton, cancelButton, null).start();
            }
        } else if (e.getSource() == okButton) {
            if (txtCode.getForeground() != DefaultSettings.disable_font_color && !txtCode.getText().equals(DEFAULT_CODE) && txtCode.getText().trim().length() > 0) {
                if (createEmailAboutPanelPrivacy != null) {
                    if (MyFnFSettings.userProfile.getEmail().equalsIgnoreCase(txtEmail.getText())) {
                        new VerifyPrimaryEmail(txtEmail.getText(), txtCode.getText().trim(), errorLabel, errorTextArea, sendButton, okButton, cancelButton, createEmailAboutPanelPrivacy).start();
                    } else {
                        new VerifySecondaryMailOrNumber(txtEmail.getText(), txtCode.getText().trim(), errorLabel, errorTextArea, sendButton, okButton, cancelButton, createEmailAboutPanelPrivacy).start();
                    }
                } else if (myProfilePhoneNumberPanel != null) {
                    String[] parts = txtEmail.getText().split("-");
                    new VerifyPrimaryMobileNumber(parts[1], parts[0], txtCode.getText().trim(), errorLabel, errorTextArea, sendButton, okButton, cancelButton, myProfilePhoneNumberPanel).start();
                } else if (phoneNumberUnitPanel != null) { //ok secNum
                    String ml = txtEmail.getText();
                    String mdc = "+880";
                    if (txtEmail.getText().contains("-")) {
                        String[] parts = txtEmail.getText().split("-");
                        ml = parts[1];
                        mdc = parts[0];
                    }
                    new VerifySecondaryMailOrNumber(ml, mdc, txtCode.getText().trim(), errorLabel, errorTextArea, sendButton, okButton, cancelButton, phoneNumberUnitPanel).start();
                }
            } else {
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText("Must provide valid code");
            }
        } else if (e.getSource() == cancelButton) {
            disposeThis();
        }
    }

    private void disposeThis() {
        this.dispose();
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    public void focusGained(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == txtEmail) {
            if (TXT_ICON_H != null) {
                mailPanel.setImage(TXT_ICON_H);
                TXT_ICON_H.flush();
            }
        } else if (actionSource == txtCode) {
            if (txtCode.getText().equals(DEFAULT_CODE) || txtCode.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, false);
            } else if (txtCode.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, true);
            }
            if (CODE_ICON_H != null) {
                codePanel.setImage(CODE_ICON_H);
                CODE_ICON_H.flush();
            }
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == txtEmail) {
            if (TXT_ICON != null) {
                mailPanel.setImage(TXT_ICON);
                TXT_ICON.flush();
            }
        } else if (actionSource == txtCode) {
            extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, true);
            if (TXT_ICON != null) {
                codePanel.setImage(CODE_ICON);
                TXT_ICON.flush();
            }
        }
    }

}

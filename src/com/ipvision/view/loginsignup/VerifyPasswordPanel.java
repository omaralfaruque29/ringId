/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.singinsignup.SendRecoveredPasscode;
import com.ipvision.service.singinsignup.VerifyRecoveredPasscode;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Sirat Samyoun
 */
public class VerifyPasswordPanel extends ImagePane implements KeyListener, FocusListener {

    final String DEFAULT_VERIFICATION_STRING = "ringID/Phone/email";
    final String DEFAULT_CODE = "Code";
    private UiMethods extra_methods = new UiMethods();
    public Object actionSource;
    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel buttonPanel;
    private JPanel emailPhonePanel;
    private JPanel codePanel;
    private JButton sendButton;
    private JButton okButton;
    private JButton cancelButton;
    public SelectionListSigninSignup selectionList;
    private JTextField codeTextField;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private int number_field_width = 100;

    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private String recoverBy = "";
    public static String getResponseUserId;

    public VerifyPasswordPanel(String str) {
        this.recoverBy = str;
        if (BG_IMAGE != null) {
            this.setImage(BG_IMAGE);
            BG_IMAGE.flush();
        }
        setLayout(new BorderLayout());
        setOpaque(false);

        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(350, 0));
        containerPanel.setOpaque(false);
        add(containerPanel, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(200, 0, 0, 0));
        containerPanel.add(topPanel, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 70, 7));
        titlePanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Recover Password");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        titlePanel.add(titleLabel);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setOpaque(false);
        //errorPanel.setBackground(Color.blue);
        errorPanel.setBorder(new EmptyBorder(80, 0, 0, 0));
        topPanel.add(errorPanel, BorderLayout.SOUTH);

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

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        initContents();

    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 0, 0);

        emailPhonePanel = new JPanel(new BorderLayout());
        emailPhonePanel.setOpaque(false);
        emailPhonePanel.setPreferredSize(new Dimension(265, 35));
        mainPanel.add(getWrapperPanel(emailPhonePanel), c);

        selectionList = new SelectionListSigninSignup(SelectionListSigninSignup.TYPE_SIGN_UP);
        selectionList.ringIDorMobileorEmailTextField.setText(recoverBy);
        selectionList.ringIDorMobileorEmailTextField.setEditable(false);
        selectionList.ringIDorMobileorEmailTextField.setForeground(null);
        selectionList.actionPopupButton.setEnabled(false);
        if (!HelperMethods.isValidEmail(recoverBy)) {
            selectionList.countryCodeTextField.setText("Phone");
            selectionList.imageLabel.setIcon(null);
//            selectionList.seperator.setVisible(false);
//            selectionList.ePanel.removeAll();
//            selectionList.ePanel.add(Box.createHorizontalStrut(20));
//            selectionList.ePanel.revalidate();
//            selectionList.ePanel.repaint();
        }
        selectionList.actionPopupButton.removeActionListener(selectionList.actionListener);
        emailPhonePanel.add(selectionList);

        c.gridy += 1;
        sendButton = new RoundedButton("Send", "Send");
        sendButton.addActionListener(actionListener);
        mainPanel.add(getWrapperButton(sendButton), c);

        c.gridy += 1;
        c.insets = new Insets(10, 0, 0, 0);
        codePanel = new JPanel(new BorderLayout());
        codePanel.setOpaque(false);
        codePanel.setPreferredSize(new Dimension(265, 35));
        mainPanel.add(getWrapperPanel(codePanel), c);

        JPanel code = new RoundedPanel();
        code.setOpaque(false);
        codeTextField = DesignClasses.makeTextFieldLimitedTextSize(DEFAULT_CODE, 5, 30, 5);
        codeTextField.setPreferredSize(new Dimension(60, 30));
        codeTextField.addMouseListener(new ContextMenuMouseListener());
        codeTextField.setForeground(DefaultSettings.disable_font_color);
        codeTextField.setToolTipText(DEFAULT_CODE);
        codeTextField.addKeyListener(this);
        codeTextField.addFocusListener(this);
        codeTextField.setOpaque(false);
        codeTextField.setBorder(null);
        code.add(codeTextField);

        codePanel.add(code);

        c.gridy += 1;
        c.insets = new Insets(5, 0, 0, 0);

        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(265, 25));
        buttonPanel.setOpaque(false);

        okButton = new RoundedButton("Ok", "Ok");
        okButton.addActionListener(actionListener);
        buttonPanel.add(okButton, BorderLayout.WEST);

        cancelButton = new RoundedButton("Cancel", "Cancel");
        cancelButton.addActionListener(actionListener);
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        mainPanel.add(getWrapperPanel(buttonPanel), c);

        c.gridy += 1;
        mainPanel.add(Box.createVerticalStrut(180), c);

    }

    private JPanel getWrapperButton(JButton panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 25));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                new SendRecoveredPasscode(recoverBy, errorLabel, errorTextArea, sendButton, okButton, cancelButton).start();
            } else if (e.getSource() == okButton) {
                if (codeTextField.getForeground() != DefaultSettings.disable_font_color && !codeTextField.getText().equals(DEFAULT_CODE) && codeTextField.getText().trim().length() > 0 && getResponseUserId != null && getResponseUserId.length() > 0) {
                    new VerifyRecoveredPasscode(getResponseUserId, codeTextField.getText(), errorLabel, errorTextArea, sendButton, okButton, cancelButton).start();
                } else {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Must provide valid code");
                }
            } else if (e.getSource() == cancelButton) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().RECOVER_PASSWORD_UI);
            }
        }
    };

    @Override
    public void keyTyped(KeyEvent event) {
        actionSource = event.getSource();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        actionSource = event.getSource();
        if (actionSource == codeTextField) {
            if ((int) event.getKeyCode() == KeyEvent.VK_ENTER) {
                okButton.doClick();
            }
        }
//        int key = (int) event.getKeyCode();
//        if (key == KeyEvent.VK_ENTER) {
//            okButton.doClick();
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
        actionSource = e.getSource();
        /*  if (actionSource == txtPhnorMail) {
         if (txtPhnorMail.getText().equals(DEFAULT_VERIFICATION_STRING) || txtPhnorMail.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(txtPhnorMail, DEFAULT_VERIFICATION_STRING, false);
         } else if (txtPhnorMail.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(txtPhnorMail, DEFAULT_VERIFICATION_STRING, true);
         }

         } else */
        if (actionSource == codeTextField) {
            if (codeTextField.getText().equals(DEFAULT_CODE) || codeTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(codeTextField, DEFAULT_CODE, false);
            } else if (codeTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(codeTextField, DEFAULT_CODE, true);
            }

        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        /*if (actionSource == txtPhnorMail) {
         extra_methods.set_reset_defalut_text(txtPhnorMail, DEFAULT_VERIFICATION_STRING, true);

         } else*/ if (actionSource == codeTextField) {
            extra_methods.set_reset_defalut_text(codeTextField, DEFAULT_CODE, true);
        }
    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 35));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

}

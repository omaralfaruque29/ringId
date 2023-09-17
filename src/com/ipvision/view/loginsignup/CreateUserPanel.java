/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.ipvision.service.singinsignup.SignUpRequest;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Shahadat
 */
public class CreateUserPanel extends ImagePane implements KeyListener, FocusListener {

    private String DEFAULT_NAME = "Enter Name";
    private String DEFAULT_PASSWORD = "Password";
    private UiMethods extra_methods = new UiMethods();
    public Object actionSource;
    public Object focusedSource;
    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel buttonPanel;
    private JPanel wrapperPasswordPanel;
    private JPanel wrapperRePasswordPanel;
    private JPanel namePanel;
    private JPanel passwordPanel;
    private JPanel rePasswordPanel;
    private JButton backButton;
    private JButton doneButton;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JTextField txtName;
    private JPasswordField txtPassword;
    private JPasswordField reTxtPassword;
    private JLabel warningLabel = DesignClasses.create_imageJlabel(GetImages._WARNING);
    private JLabel reWarningLabel = DesignClasses.create_imageJlabel(GetImages._WARNING);
    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private JLabel ringIdLabel;
    private String default_input_text = "Enter E-mail";
    private int number_field_width = 100;
    private int selectedValue = 0;

    private String mbl;
    private String mblDc;
    private String email;
    private String code;

    public void setLoginType(int type) {
        this.selectedValue = type;
    }

    public void setMobileNum(String mbl, String mblDc) {
        this.mbl = mbl;
        this.mblDc = mblDc;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerificationCode(String code) {
        this.code = code;
    }

    public CreateUserPanel() {
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

        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setOpaque(false);
        //topPanel.setBackground(Color.yellow);
        containerPanel.add(topPanel, BorderLayout.NORTH);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        idPanel.setBorder(new EmptyBorder(160, 0, 0, 0));
        idPanel.setOpaque(false);
        topPanel.add(idPanel, BorderLayout.NORTH);

        String str = "Your ringID is :  " + HelperMethods.getRingID(MyFnFSettings.VALUE_NEW_USER_NAME);
        ringIdLabel = new JLabel(str);
        ringIdLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        idPanel.add(ringIdLabel);

        JPanel signUpPanel = new JPanel();
        signUpPanel.setOpaque(false);
        signUpPanel.setBorder(new EmptyBorder(80, 0, 0, 80));
        JLabel signUpLbl = new JLabel("Sign Up");
        signUpLbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        signUpLbl.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        signUpPanel.add(signUpLbl);

        topPanel.add(signUpPanel, BorderLayout.CENTER);

        JPanel errorPanel = new JPanel(new BorderLayout(0, 0));
        errorPanel.setBorder(new EmptyBorder(80, 0, 0, 0));
        errorPanel.setOpaque(false);
        //errorPanel.setBackground(Color.blue);
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
        //mainPanel.setBackground(Color.red);
        mainPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        initContents();
    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);

        namePanel = new RoundedPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setPreferredSize(new Dimension(265, 35));
        namePanel.setOpaque(false);
        mainPanel.add(getWrapperPanel(namePanel), c);

        txtName = DesignClasses.makeTextFieldLimitedTextSize(DEFAULT_NAME, number_field_width, 30, MaxLengths.NAME);
        txtName.setPreferredSize(new Dimension(number_field_width + 60, 30));
        txtName.addMouseListener(new ContextMenuMouseListener());
        txtName.setToolTipText(DEFAULT_NAME);
        txtName.setForeground(DefaultSettings.disable_font_color);
        txtName.setOpaque(false);
        txtName.setBorder(null);
        txtName.setBorder(BorderFactory.createCompoundBorder(txtName.getBorder(), BorderFactory.createEmptyBorder(7, 7, 0, 7)));
        txtName.addFocusListener(this);
        txtName.addKeyListener(this);
        namePanel.add(txtName);

        c.gridy += 1;
        c.insets = new Insets(10, 0, 0, 0);

        wrapperPasswordPanel = new JPanel(new BorderLayout());
        wrapperPasswordPanel.setOpaque(false);
        passwordPanel = new RoundedPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordPanel.setPreferredSize(new Dimension(265, 35));
        passwordPanel.setOpaque(false);
        wrapperPasswordPanel.add(passwordPanel, BorderLayout.WEST);

        txtPassword = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, number_field_width, 30, MaxLengths.PASSWORD, true);
        txtPassword.setPreferredSize(new Dimension(number_field_width + 60, 30));
        txtPassword.addMouseListener(new ContextMenuMouseListener());
        txtPassword.setForeground(DefaultSettings.disable_font_color);
        txtPassword.setToolTipText("Enter " + StaticFields.PASSWORD_TEXT);
        txtPassword.setOpaque(false);
        txtPassword.setBorder(null);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(txtPassword.getBorder(), BorderFactory.createEmptyBorder(7, 7, 0, 7)));
        txtPassword.addFocusListener(this);
        txtPassword.addKeyListener(this);
        passwordPanel.add(txtPassword);

        warningLabel.setText("Caps Lock on");
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            warningLabel.setVisible(true);
        } else {
            warningLabel.setVisible(false);
        }
        wrapperPasswordPanel.add(warningLabel, BorderLayout.EAST);
        mainPanel.add(getWrapperPanel(wrapperPasswordPanel), c);

        c.gridy += 1;
        c.insets = new Insets(10, 0, 0, 0);

        wrapperRePasswordPanel = new JPanel(new BorderLayout());
        wrapperRePasswordPanel.setOpaque(false);
        rePasswordPanel = new RoundedPanel();
        rePasswordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rePasswordPanel.setPreferredSize(new Dimension(265, 35));
        rePasswordPanel.setOpaque(false);
        wrapperRePasswordPanel.add(rePasswordPanel, BorderLayout.WEST);

        reTxtPassword = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, number_field_width, 30, MaxLengths.PASSWORD, true);
        reTxtPassword.addMouseListener(new ContextMenuMouseListener());
        reTxtPassword.setForeground(DefaultSettings.disable_font_color);
        reTxtPassword.setToolTipText("Retype " + StaticFields.PASSWORD_TEXT);
        reTxtPassword.setOpaque(false);
        reTxtPassword.setBorder(null);
        reTxtPassword.setBorder(BorderFactory.createCompoundBorder(reTxtPassword.getBorder(), BorderFactory.createEmptyBorder(7, 7, 0, 7)));
        reTxtPassword.addFocusListener(this);
        reTxtPassword.addKeyListener(this);
        rePasswordPanel.add(reTxtPassword);

        reWarningLabel.setText("Caps Lock is on");
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            reWarningLabel.setVisible(true);
        } else {
            reWarningLabel.setVisible(false);
        }
        wrapperRePasswordPanel.add(reWarningLabel, BorderLayout.EAST);
        mainPanel.add(getWrapperPanel(wrapperRePasswordPanel), c);

        c.gridy += 1;
        c.insets = new Insets(10, 0, 70, 0);
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(265, 25));
        doneButton = new JButton();
        backButton = new JButton();
        doneButton = new RoundedButton("Done", "Done");
        doneButton.addActionListener(actionListener);
        backButton = new RoundedButton("Back", "Back");
        backButton.addActionListener(actionListener);
        buttonPanel.add(doneButton, BorderLayout.WEST);
        buttonPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(getWrapperPanel(buttonPanel), c);

        c.gridy += 1;
        c.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Box.createVerticalStrut(50), c);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            /* if (e.getSource() == sendButton) {
             if ((ringIDorMobileorEmailTextField.getForeground() != DefaultSettings.disable_font_color) && (ringIDorMobileorEmailTextField.getText().trim().length() > 0)) {
             if (selectedValue == SettingsConstants.EMAIL_LOGIN) {
             if (HelperMethods.isValidEmail(ringIDorMobileorEmailTextField.getText())) {
             new SendEmailPasscode(ringIDorMobileorEmailTextField.getText(), errorLabel, errorTextArea, sendButton, doneButton, backButton).start();
             } else {
             errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
             errorTextArea.setForeground(DefaultSettings.errorLabelColor);
             errorTextArea.setText("Must provide valid E-mail address");
             }
             } else if (selectedValue == SettingsConstants.MOBILE_LOGIN) {
             if (HelperMethods.isValidNumber(ringIDorMobileorEmailTextField.getText())) {
             new SendMobileNumberPasscode(ringIDorMobileorEmailTextField.getText(), countryCodeTextField.getText(), errorLabel, errorTextArea, sendButton, doneButton, backButton).start();
             } else {
             errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
             errorTextArea.setForeground(DefaultSettings.errorLabelColor);
             errorTextArea.setText("Must provide valid number");
             }
             }
             } else {
             errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
             errorTextArea.setForeground(DefaultSettings.errorLabelColor);
             if (selectedValue == SettingsConstants.EMAIL_LOGIN) {
             errorTextArea.setText("Must provide a E-mail address");
             } else if (selectedValue == SettingsConstants.MOBILE_LOGIN) {
             errorTextArea.setText("Must provide a mobile number");
             }
             }
             } else*/ if (e.getSource() == doneButton) {
                /*if (ringIDorMobileorEmailTextField.getForeground() == DefaultSettings.disable_font_color || ringIDorMobileorEmailTextField.getText().trim().length() < 1) {
                 errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                 errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                 if (selectedValue == SettingsConstants.EMAIL_LOGIN) {
                 errorTextArea.setText("Must provide a valid E-mail address");
                 } else if (selectedValue == SettingsConstants.MOBILE_LOGIN) {
                 errorTextArea.setText("Must provide a valid mobile number");
                 }
                 } else*/
                if ((txtName.getForeground() == DefaultSettings.disable_font_color && txtName.getText().equals(DEFAULT_NAME)) || txtName.getText().trim().length() < 1) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Must provide Username");
                } else if (txtPassword.getForeground() == DefaultSettings.disable_font_color || reTxtPassword.getForeground() == DefaultSettings.disable_font_color || txtPassword.getText().length() < 1 || reTxtPassword.getText().length() < 1) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Must fill out password fields");
                } else if (!reTxtPassword.getText().equals(txtPassword.getText())) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Passwords don't match");
                } else {
                    String msg = HelperMethods.password_check(txtPassword.getText());
                    if (msg.length() == 0) {
                        if (selectedValue == SettingsConstants.EMAIL_LOGIN && email != null && email.length() > 0) {
                            if (code != null && code.length() > 0) {
                                new SignUpRequest(email, code, txtName.getText(), txtPassword.getText(), errorLabel, errorTextArea, doneButton, backButton, ringIdLabel).start();
                            } else {
                                new SignUpRequest(email, null, txtName.getText(), txtPassword.getText(), errorLabel, errorTextArea, doneButton, backButton, ringIdLabel).start();
                            }
                        } else if (selectedValue == SettingsConstants.MOBILE_LOGIN && mbl != null && mblDc.length() > 0) {
                            if (code != null && code.length() > 0) {
                                new SignUpRequest(mbl, mblDc, code, txtName.getText(), txtPassword.getText(), errorLabel, errorTextArea, doneButton, backButton, ringIdLabel).start();
                            } else {
                                new SignUpRequest(mbl, mblDc, null, txtName.getText(), txtPassword.getText(), errorLabel, errorTextArea, doneButton, backButton, ringIdLabel).start();
                            }
                        }
                    } else {
                        errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                        errorTextArea.setText(" "+msg);
                    }
                }
            } else if (e.getSource() == backButton) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.EMAIL_MOBILE_VARIFICATION_UI);
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
        int key = (int) event.getKeyCode();

        if (txtPassword.getForeground() != DefaultSettings.disable_font_color) {

            if (key == KeyEvent.VK_CAPS_LOCK) {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                    warningLabel.setVisible(true);
                } else {
                    warningLabel.setVisible(false);
                }
            }

        }

        if (key == KeyEvent.VK_ENTER) {
            doneButton.doClick();
        }

        if (reTxtPassword.getForeground() != DefaultSettings.disable_font_color) {
            if (key == KeyEvent.VK_CAPS_LOCK) {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                    reWarningLabel.setVisible(true);
                } else {
                    reWarningLabel.setVisible(false);
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
        actionSource = e.getSource();
        /*if (actionSource == ringIDorMobileorEmailTextField) {
         if (ringIDorMobileorEmailTextField.getText().equals(default_input_text) || ringIDorMobileorEmailTextField.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, false);
         } else if (ringIDorMobileorEmailTextField.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, true);
         }
         } else if (actionSource == txtCode) {
         if (txtCode.getText().equals(DEFAULT_CODE) || txtCode.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, false);
         } else if (txtCode.getText().length() < 1) {
         extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, true);
         }

         } else */
        if (actionSource == txtName) {
            if (txtName.getText().equals(DEFAULT_NAME) || txtName.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtName, DEFAULT_NAME, false);
            } else if (txtName.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtName, DEFAULT_NAME, true);
            }

        } else if (actionSource == txtPassword) {
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                warningLabel.setVisible(true);
            } else {
                warningLabel.setVisible(false);
            }

            if (txtPassword.getText().equals(DEFAULT_PASSWORD) || txtPassword.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtPassword, DEFAULT_PASSWORD, false);
            } else if (txtPassword.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(txtPassword, DEFAULT_PASSWORD, true);
            }

        } else if (actionSource == reTxtPassword) {
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                reWarningLabel.setVisible(true);
            } else {
                reWarningLabel.setVisible(false);
            }

            if (reTxtPassword.getText().equals(DEFAULT_PASSWORD) || reTxtPassword.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(reTxtPassword, DEFAULT_PASSWORD, false);
            } else if (reTxtPassword.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(reTxtPassword, DEFAULT_PASSWORD, true);
            }
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        /*if (actionSource == ringIDorMobileorEmailTextField) {
         extra_methods.set_reset_defalut_text(ringIDorMobileorEmailTextField, default_input_text, true);
         } else if (actionSource == txtCode) {
         extra_methods.set_reset_defalut_text(txtCode, DEFAULT_CODE, true);
         } else */
        if (actionSource == txtName) {
            extra_methods.set_reset_defalut_text(txtName, DEFAULT_NAME, true);
        } else if (actionSource == txtPassword) {
            extra_methods.set_reset_defalut_text(txtPassword, DEFAULT_PASSWORD, true);
        } else if (actionSource == reTxtPassword) {
            extra_methods.set_reset_defalut_text(reTxtPassword, DEFAULT_PASSWORD, true);
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

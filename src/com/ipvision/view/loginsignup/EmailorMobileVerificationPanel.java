/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.JsonFields;
import com.ipvision.model.dao.RingUserSettingsDAO;
import com.ipvision.service.utility.HelperMethods;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.ipvision.service.SendEmailPasscode;
import com.ipvision.service.SendMobileNumberPasscode;
import com.ipvision.service.singinsignup.VerifyPrimaryEmail;
import com.ipvision.service.singinsignup.VerifyPrimaryMobileNumber;
import com.ipvision.view.SplashScreenAfterLogin;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Shahadat
 */
public class EmailorMobileVerificationPanel extends ImagePane implements KeyListener, FocusListener {

    private UiMethods extra_methods = new UiMethods();
    public Object actionSource;
    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel emailPhonePanel;
    private JPanel codePanel;
    private JButton sendButton;
    private JButton cancelButton;
    private JButton nextButton;
    public JLabel errorLabel;
    public JTextArea errorTextArea;
    private JTextField codeTextField;
    private static String ENTER_CODE = "Code";
    private static String ERR_MAIL_MOB = " Must provide Email / Phone number";
    private static String ERR_CODE = " Must provide the valid code";
    private static String ERR_MAIL = " Must provide a valid Email";
    private static String ERR_MOB = " Must provide a valid Phone number";
    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private JsonFields json_fields;
    public SelectionListSigninSignup selectionList;
    private JPanel buttonPanel;
    private JButton skipButton;
    private JButton doneButton;
    private JTextArea area1, area2;
    private JLabel signUpLbl;
    private boolean isSignUp;

    public void set_Is_Sign_Up(boolean isSignUp) {
        this.isSignUp = isSignUp;
    }

    public void setJson_fields(JsonFields json_fields) {
        this.json_fields = json_fields;
    }

    public void setEmail() {
        if (json_fields.getEmail() != null && json_fields.getEmail().length() > 0) {
            selectionList.countryCodeTextField.setText(selectionList.TXT_EMAIL);
            selectionList.ringIDorMobileorEmailTextField.setText(json_fields.getEmail());
            String imageSource = GetImages.FLAGS_ROOT_FOLDER + "E-mail.png";
            selectionList.imageLabel.setIcon(DesignClasses.return_image(imageSource));
            selectionList.ringIDorMobileorEmailTextField.setEditable(false);
            selectionList.ringIDorMobileorEmailTextField.setForeground(null);
            selectionList.ringIDorMobileorEmailTextField.removeActionListener(actionListener);
            selectionList.type_int = SettingsConstants.EMAIL_LOGIN;
            area1.setText("Verify your E-mail Address!");//You can login using ringID,verified E-mail/Mobile number
            selectionList.actionPopupButton.setEnabled(false);
            signUpLbl.setVisible(false);
            addSikpDoneButtons();
        }
    }

    public void setMobile() {
        if (json_fields.getMobilePhone() != null && json_fields.getMobilePhone().length() > 0) {
            selectionList.countryCodeTextField.setText(json_fields.getMobilePhoneDialingCode());
            selectionList.ringIDorMobileorEmailTextField.setText(json_fields.getMobilePhone());
            String countryName = HelperMethods.get_country_name_from_contry_code(json_fields.getMobilePhoneDialingCode().trim());
            String imageSource = GetImages.FLAGS_ROOT_FOLDER + countryName + ".png";
            selectionList.imageLabel.setIcon(DesignClasses.return_image(imageSource));
            selectionList.ringIDorMobileorEmailTextField.setEditable(false);
            selectionList.ringIDorMobileorEmailTextField.setForeground(null);
            selectionList.actionPopupButton.removeActionListener(selectionList.actionListener);
            selectionList.type_int = SettingsConstants.MOBILE_LOGIN;
            area1.setText("Verify your Mobile number!");// You can login using ringID,verified E-mail/Mobile number
            //area2.setText("");
            signUpLbl.setVisible(false);
            selectionList.actionPopupButton.setEnabled(false);
            addSikpDoneButtons();
        }
    }

    private void addSikpDoneButtons() {
        buttonPanel.removeAll();
        skipButton = new RoundedButton("Skip", "Skip");
        skipButton.addActionListener(actionListener);
        buttonPanel.add(skipButton, BorderLayout.WEST);

        doneButton = new RoundedButton("Done", "Done");
        doneButton.addActionListener(actionListener);
        buttonPanel.add(doneButton, BorderLayout.EAST);
        buttonPanel.revalidate();
    }

    public EmailorMobileVerificationPanel() {
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
        //topPanel.setBackground(Color.red);
        topPanel.setBorder(new EmptyBorder(140, 0, 0, 0));
        containerPanel.add(topPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        topPanel.add(infoPanel, BorderLayout.NORTH);
        area1 = DesignClasses. createJTextAreaForSignup("Create your ringID account by your existing E-mail or Mobile number", 14);
        infoPanel.add(area1); 
        infoPanel.add(Box.createVerticalStrut(10));
        area2 = DesignClasses. createJTextAreaForSignup("It is suggested to verify your associated E-mail or Mobile number to recover your account", 12);
        infoPanel.add(area2); 

        JPanel signUpPanel = new JPanel();
        signUpPanel.setOpaque(false);
        signUpPanel.setBorder(new EmptyBorder(20, 0, 0, 60));
        signUpLbl = new JLabel("Sign Up");
        signUpLbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        signUpLbl.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        signUpPanel.add(signUpLbl);

        topPanel.add(signUpPanel, BorderLayout.CENTER);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setOpaque(false);
        //errorPanel.setBackground(Color.blue);
        errorPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
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
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);
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
        selectionList.ringIDorMobileorEmailTextField.addKeyListener(this);
        selectionList.ringIDorMobileorEmailTextField.addFocusListener(this);
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
        codeTextField = DesignClasses.makeTextFieldLimitedTextSize(ENTER_CODE, 5, 30, 5);
        codeTextField.setPreferredSize(new Dimension(60, 30));
        codeTextField.addMouseListener(new ContextMenuMouseListener());
        codeTextField.setForeground(DefaultSettings.disable_font_color);
        codeTextField.setToolTipText(ENTER_CODE);
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

        nextButton = new RoundedButton("Next", "Next");
        nextButton.addActionListener(actionListener);
        buttonPanel.add(nextButton, BorderLayout.WEST);

        cancelButton = new RoundedButton("Cancel", "Cancel");
        cancelButton.addActionListener(actionListener);
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        mainPanel.add(getWrapperPanel(buttonPanel), c);

        c.gridy += 1;
        mainPanel.add(Box.createVerticalStrut(150), c);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            errorLabel.setIcon(null);
            errorTextArea.setText("");
            if (e.getSource() == sendButton) {
                if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN) {
                    if (json_fields != null) {
                        new VerifyPrimaryEmail(selectionList.ringIDorMobileorEmailTextField.getText(), null, errorLabel, errorTextArea, sendButton, nextButton, cancelButton).start();
                    } else {
                        new SendEmailPasscode(selectionList.ringIDorMobileorEmailTextField.getText(), errorLabel, errorTextArea, sendButton, nextButton, cancelButton).start();
                    }
                } else if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN) {
                    if (json_fields != null) {
                        new VerifyPrimaryMobileNumber(selectionList.ringIDorMobileorEmailTextField.getText(), selectionList.countryCodeTextField.getText(), null, errorLabel, errorTextArea, sendButton, nextButton, cancelButton).start();
                    } else {
                        new SendMobileNumberPasscode(selectionList.ringIDorMobileorEmailTextField.getText(), selectionList.countryCodeTextField.getText(), errorLabel, errorTextArea, sendButton, nextButton, cancelButton).start();
                    }
                }
            } else if (e.getSource() == doneButton) {
                if (codeTextField.getText().trim().length() > 0 && !codeTextField.getText().trim().equals(ENTER_CODE)) {
                    if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN) {
                        new VerifyPrimaryEmail(selectionList.ringIDorMobileorEmailTextField.getText(), codeTextField.getText().trim(), errorLabel, errorTextArea, sendButton, doneButton, skipButton).start();
                    } else if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN) {
                        new VerifyPrimaryMobileNumber(selectionList.ringIDorMobileorEmailTextField.getText(), selectionList.countryCodeTextField.getText(), codeTextField.getText().trim(), errorLabel, errorTextArea, sendButton, doneButton, skipButton).start();
                    }
                } else {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_CODE);
                }
            } else if (e.getSource() == skipButton) {
                if (json_fields != null) {
                    if ((json_fields.getProfileImage() == null || json_fields.getProfileImage().trim().length() <= 0) && RingUserSettingsDAO.getLastLoggedInTime() <= 0) {
                        LoginUI.getLoginUI().loadMainContent(LoginUI.PROFILE_IMAGE_UI);
                        LoginUI.getLoginUI().profileImagePanel.setJson_fields(json_fields);
                    } else {
                        if (isSignUp) {
                            LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().SUCCESS_UI);
                            LoginUI.getLoginUI().successPanel.setValues(json_fields);
                        }
                        if (LoginUI.getLoginUI() != null) {
                            LoginUI.getLoginUI().disposeLoginUI();
                            LoginUI.loginUI = null;
                        }
                        SplashScreenAfterLogin sp = new SplashScreenAfterLogin(json_fields);
                        sp.setVisible(true);

                    }
                }
            } else if (e.getSource() == nextButton) {
                if (selectionList.ringIDorMobileorEmailTextField.getForeground() == DefaultSettings.disable_font_color || selectionList.ringIDorMobileorEmailTextField.getText().trim().length() < 1) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_MAIL_MOB);
                } else if (codeTextField.getForeground() != DefaultSettings.disable_font_color && codeTextField.getText().trim().length() > 0 && !HelperMethods.isValidNumber(codeTextField.getText().trim())) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_CODE);
                } else {
                    if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN && !HelperMethods.isValidNumber(selectionList.ringIDorMobileorEmailTextField.getText().trim())) {
                        errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                        errorTextArea.setText(ERR_MOB);
                    } else if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN && !HelperMethods.isValidEmail(selectionList.ringIDorMobileorEmailTextField.getText().trim())) {
                        errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                        errorTextArea.setText(ERR_MAIL);
                    } else {
                        LoginUI.getLoginUI().loadMainContent(LoginUI.CREATE_USER_UI);
                        LoginUI.getLoginUI().createUserPanel.setLoginType(selectionList.type_int);
                        LoginUI.getLoginUI().createUserPanel.setVerificationCode(codeTextField.getText());
                        if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN) {
                            LoginUI.getLoginUI().createUserPanel.setMobileNum(selectionList.ringIDorMobileorEmailTextField.getText(), selectionList.countryCodeTextField.getText());
                        } else if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN) {
                            LoginUI.getLoginUI().createUserPanel.setEmail(selectionList.ringIDorMobileorEmailTextField.getText());
                        }
                    }
                }
            } else if (e.getSource() == cancelButton) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.MAIN_LOGIN_UI);
            }
            selectionList.countryFlagName_popup.hideThis();
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
        if (key == KeyEvent.VK_ENTER) {
            if (json_fields != null) {
                skipButton.doClick();
            } else {
                nextButton.doClick();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == selectionList.ringIDorMobileorEmailTextField) {
            if (selectionList.ringIDorMobileorEmailTextField.getText().equals(selectionList.default_input_text) || selectionList.ringIDorMobileorEmailTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(selectionList.ringIDorMobileorEmailTextField, selectionList.default_input_text, false);
            } else if (selectionList.ringIDorMobileorEmailTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(selectionList.ringIDorMobileorEmailTextField, selectionList.default_input_text, true);
            }
        } else if (actionSource == codeTextField) {
            if (codeTextField.getText().equals(ENTER_CODE) || codeTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(codeTextField, ENTER_CODE, false);
            } else if (codeTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(codeTextField, ENTER_CODE, true);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == selectionList.ringIDorMobileorEmailTextField) {
            extra_methods.set_reset_defalut_text(selectionList.ringIDorMobileorEmailTextField, selectionList.default_input_text, true);
        } else if (actionSource == codeTextField) {
            extra_methods.set_reset_defalut_text(codeTextField, ENTER_CODE, true);
        }

    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 35));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    private JPanel getWrapperButton(JButton panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 25));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }
}

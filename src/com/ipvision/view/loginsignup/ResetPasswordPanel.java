/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.MaxLengths;
import com.ipvision.view.utility.DefaultSettings;
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
import com.ipvision.service.singinsignup.ResetPassword;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Sirat Samyoun
 */
public class ResetPasswordPanel extends ImagePane implements KeyListener, FocusListener {

    private UiMethods extra_methods = new UiMethods();
    public Object actionSource;
    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private int number_field_width = 100;
    private JPanel wrapperPasswordPanel;
    private JPanel wrapperRePasswordPanel;
    private JPanel passwordPanel;
    private JPanel rePasswordPanel;
    private JPasswordField passwordTxtField;
    private JPasswordField rePasswordTxtField;
    private JLabel warningLabel = DesignClasses.create_imageJlabel(GetImages._WARNING);
    private JLabel reWarningLabel = DesignClasses.create_imageJlabel(GetImages._WARNING);
    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private String str = "";
    final String DEFAULT_PASSWORD = "Password";

    public ResetPasswordPanel() {
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

        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        titlePanel.add(titleLabel);

        JPanel errorPanel = new JPanel(new BorderLayout(5, 0));
        errorPanel.setBorder(new EmptyBorder(100, 0, 0, 0));
        errorPanel.setOpaque(false);
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

        c.insets = new Insets(5, 0, 5, 0);

        wrapperPasswordPanel = new JPanel(new BorderLayout());
        wrapperPasswordPanel.setOpaque(false);
        wrapperPasswordPanel.setPreferredSize(new Dimension(265, 35));
        mainPanel.add(getWrapperPanel(wrapperPasswordPanel), c);

        passwordPanel = new RoundedPanel();
        passwordPanel.setOpaque(false);

        passwordTxtField = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, number_field_width, 30, MaxLengths.PASSWORD, false);
        passwordTxtField.setPreferredSize(new Dimension(number_field_width + 60, 30));
        passwordTxtField.addMouseListener(new ContextMenuMouseListener());
        passwordTxtField.setForeground(DefaultSettings.disable_font_color);
        passwordTxtField.setToolTipText("Enter " + StaticFields.PASSWORD_TEXT);
        passwordTxtField.setOpaque(false);
        passwordTxtField.setBorder(BorderFactory.createCompoundBorder(passwordTxtField.getBorder(), BorderFactory.createEmptyBorder(5, 7, 3, 5)));
        passwordTxtField.addFocusListener(this);
        passwordTxtField.addKeyListener(this);
        passwordPanel.add(passwordTxtField);

        wrapperPasswordPanel.add(passwordPanel, BorderLayout.WEST);

        warningLabel.setText("Caps Lock on");
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            warningLabel.setVisible(true);
        } else {
            warningLabel.setVisible(false);
        }
        wrapperPasswordPanel.add(warningLabel, BorderLayout.EAST);

        c.gridy += 1;
        c.insets = new Insets(5, 0, 5, 0);

        wrapperRePasswordPanel = new JPanel(new BorderLayout());
        wrapperRePasswordPanel.setOpaque(false);
        wrapperRePasswordPanel.setPreferredSize(new Dimension(265, 35));
        mainPanel.add(getWrapperPanel(wrapperRePasswordPanel), c);

        rePasswordPanel = new RoundedPanel();
        rePasswordPanel.setOpaque(false);

        rePasswordTxtField = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, number_field_width, 30, MaxLengths.PASSWORD, true);
        rePasswordTxtField.setPreferredSize(new Dimension(number_field_width + 60, 30));
        rePasswordTxtField.addMouseListener(new ContextMenuMouseListener());
        rePasswordTxtField.setForeground(DefaultSettings.disable_font_color);
        rePasswordTxtField.setToolTipText("Retype " + StaticFields.PASSWORD_TEXT);
        rePasswordTxtField.setOpaque(false);
        rePasswordTxtField.setBorder(null);
        rePasswordTxtField.setBorder(BorderFactory.createCompoundBorder(rePasswordTxtField.getBorder(), BorderFactory.createEmptyBorder(5, 7, 3, 5)));
        rePasswordTxtField.addFocusListener(this);
        rePasswordTxtField.addKeyListener(this);
        rePasswordPanel.add(rePasswordTxtField);

        wrapperRePasswordPanel.add(rePasswordPanel, BorderLayout.WEST);

        reWarningLabel.setText("Caps Lock on");
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            reWarningLabel.setVisible(true);
        } else {
            reWarningLabel.setVisible(false);
        }

        wrapperRePasswordPanel.add(reWarningLabel, BorderLayout.EAST);

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
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) {
                if (passwordTxtField.getForeground() == DefaultSettings.disable_font_color || rePasswordTxtField.getForeground() == DefaultSettings.disable_font_color || passwordTxtField.getText().length() < 1 || rePasswordTxtField.getText().length() < 1) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Must fill out password fields");
                } else if (!rePasswordTxtField.getText().equals(passwordTxtField.getText())) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(" Passwords don't match");
                } else {
                    String msg = HelperMethods.password_check(passwordTxtField.getText());
                    if (msg.length() == 0) {
                        if (VerifyPasswordPanel.getResponseUserId != null && VerifyPasswordPanel.getResponseUserId.length() > 0) {
                            new ResetPassword(VerifyPasswordPanel.getResponseUserId, passwordTxtField.getText(), errorLabel, errorTextArea, okButton, cancelButton).start();
                        }
                    } else {
                        errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                        errorTextArea.setText(msg);
                    }
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
        int key = (int) event.getKeyCode();

        if (passwordTxtField.getForeground() != DefaultSettings.disable_font_color) {
            if (key == KeyEvent.VK_CAPS_LOCK) {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                    warningLabel.setVisible(true);
                } else {
                    warningLabel.setVisible(false);
                }
            }
        }
        if (key == KeyEvent.VK_ENTER) {
            okButton.doClick();
        }
        if (rePasswordTxtField.getForeground() != DefaultSettings.disable_font_color) {
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
        if (actionSource == passwordTxtField) {
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                warningLabel.setVisible(true);
            } else {
                warningLabel.setVisible(false);
            }

            if (passwordTxtField.getText().equals(DEFAULT_PASSWORD) || passwordTxtField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(passwordTxtField, DEFAULT_PASSWORD, false);
            } else if (passwordTxtField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(passwordTxtField, DEFAULT_PASSWORD, true);
            }
        } else if (actionSource == rePasswordTxtField) {
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                reWarningLabel.setVisible(true);
            } else {
                reWarningLabel.setVisible(false);
            }

            if (rePasswordTxtField.getText().equals(DEFAULT_PASSWORD) || rePasswordTxtField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(rePasswordTxtField, DEFAULT_PASSWORD, false);
            } else if (rePasswordTxtField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(rePasswordTxtField, DEFAULT_PASSWORD, true);
            }
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == passwordTxtField) {
            extra_methods.set_reset_defalut_text(passwordTxtField, DEFAULT_PASSWORD, true);
        } else if (actionSource == rePasswordTxtField) {
            extra_methods.set_reset_defalut_text(rePasswordTxtField, DEFAULT_PASSWORD, true);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import com.ipvision.service.singinsignup.ForgetPassowordGetPorts;
import com.ipvision.service.singinsignup.SignInRequest;
import com.ipvision.service.singinsignup.RingIdRequest;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import utils.DocumentSizeFilter;
import javax.swing.BorderFactory;

/**
 *
 * @author Shahadat
 */
public class MainLoginPanel extends ImagePane implements KeyListener, FocusListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MainLoginPanel.class);
    private static MainLoginPanel mainLoginPanel;
    public Object actionSource;
    public Object focusedSource;
    private JPanel containerPanel;
    private JPanel topMainPanel;
    private JPanel bottomMainPanel;
    public JPasswordField passTextField;
    public JButton btnSignIn;
    private JLabel lblCreateNew;
    public JLabel errorLabel;
    public JTextArea errorTextArea;
    private JPanel ringIdPanel;
    private JPanel passwordPanel;
    private JLabel warningLabel;
    private JCheckBox savePassCheckbox;
    private UiMethods extra_methods = new UiMethods();
    private String DEFAULT_PASSWORD = "Password";
    private BufferedImage BG_IMAGE;
    private JLabel forgetPass;
    private static String ERROR_MSG_EMAIL = "Please enter valid email address";
    private static String ERROR_MSG_MOBILE = "Please enter valid mobile number";
    private int number_field_width = 100;
    public SelectionListSigninSignup selectionList;

    public static MainLoginPanel getInstance() {
        return mainLoginPanel;
    }

    private void setMainloginPanel() {
        mainLoginPanel = this;
    }

    public MainLoginPanel() {
        setMainloginPanel();
        try {
            forgetPass = DesignClasses.makeAncorLabelDefaultColor("Forgot Password?", 0, 12);
            forgetPass.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            forgetPass.setForeground(RingColorCode.RING_THEME_COLOR);
            BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
            warningLabel = DesignClasses.create_imageJlabel(GetImages._WARNING);
        } catch (Exception e) {
            log.error("error while loading default image");
        }

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

        JPanel errorPanel = new JPanel(new BorderLayout(0, 0));
        errorPanel.setBorder(new EmptyBorder(150, 0, 0, 0));
        errorPanel.setOpaque(false);
        //errorPanel.setBackground(Color.red);
        containerPanel.add(errorPanel, BorderLayout.NORTH);

        errorLabel = DesignClasses.makeAncorLabel("", 0, 12);
        errorTextArea = new JTextArea("");
        errorTextArea.setEnabled(false);
        errorTextArea.setColumns(27);
        errorTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setBorder(null);
        errorTextArea.setOpaque(false);
        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(300));
        errorTextArea.setDocument(doc);
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.add(errorTextArea, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        topMainPanel = new JPanel(new GridBagLayout());
        topMainPanel.setOpaque(false);
        mainPanel.add(topMainPanel, BorderLayout.NORTH);

        bottomMainPanel = new JPanel(new GridBagLayout());
        bottomMainPanel.setOpaque(false);
        mainPanel.add(bottomMainPanel, BorderLayout.SOUTH);
        initContents();

        if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.RINGID_LOGIN) {
            selectionList.countryCodeTextField.setText(selectionList.CODE_RINGID);
        } else if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.EMAIL_LOGIN) {
            selectionList.countryCodeTextField.setText(selectionList.CODE_EMAIL);
        } else if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.MOBILE_LOGIN) {
            selectionList.countryCodeTextField.setText(MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
        }

        if (MyFnFSettings.VALUE_LOGIN_USER_NAME != null && MyFnFSettings.VALUE_LOGIN_USER_NAME.trim().length() > 0) {
            selectionList.ringIDorMobileorEmailTextField.setText(MyFnFSettings.VALUE_LOGIN_USER_NAME);
            selectionList.ringIDorMobileorEmailTextField.setForeground(Color.BLACK);
        }
        selectionList.setCodeImage();
        if (MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 1 || MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 1) {
            if (MyFnFSettings.VALUE_LOGIN_USER_PASSWORD != null && MyFnFSettings.VALUE_LOGIN_USER_PASSWORD.trim().length() > 0) {
                passTextField.setText(MyFnFSettings.VALUE_LOGIN_USER_PASSWORD);
                passTextField.setForeground(Color.BLACK);
            }
        }
        savePassCheckbox.setSelected(MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 1);

    }

    private void initContents() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 0, 0);

        ringIdPanel = new JPanel(new BorderLayout());
        ringIdPanel.setBackground(Color.WHITE);
        ringIdPanel.setPreferredSize(new Dimension(265, 35));
        topMainPanel.add(getWrapperPanel(ringIdPanel), c);
        c.gridy += 1;
        c.insets = new Insets(10, 0, 0, 0);
        JPanel wrapperPasswordPanel = new JPanel(new BorderLayout());
        wrapperPasswordPanel.setOpaque(false);
        passwordPanel = new RoundedPanel();
        passwordPanel.setLayout(new BorderLayout()); //ImagePane();
        passwordPanel.setOpaque(false);
        passwordPanel.setPreferredSize(new Dimension(265, 35));
        wrapperPasswordPanel.add(passwordPanel, BorderLayout.WEST);
        warningLabel.setText("Caps Lock on");
        if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
            warningLabel.setVisible(true);
        } else {
            warningLabel.setVisible(false);
        }
        wrapperPasswordPanel.add(warningLabel, BorderLayout.EAST);
        topMainPanel.add(getWrapperPanel(wrapperPasswordPanel), c);

        c.gridy++;
        c.insets = new Insets(5, 0, 0, 0);

        String msg = "Save Password";
        savePassCheckbox = new JCheckBox("" + msg);
        savePassCheckbox.setForeground(RingColorCode.RING_THEME_COLOR);
        savePassCheckbox.setOpaque(false);
        savePassCheckbox.setFont(new Font(msg, Font.PLAIN, 12));
        savePassCheckbox.setIcon(DesignClasses.return_image(GetImages.CHECK_BOX));
        savePassCheckbox.setSelectedIcon(DesignClasses.return_image(GetImages.CHECK_BOX_SELECTED));

        topMainPanel.add(savePassCheckbox, c);
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);

        topMainPanel.add(forgetPass, c);
        forgetPass.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgetPassowordGetPorts(errorLabel, errorTextArea, btnSignIn, lblCreateNew).start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }
        });

        c.gridy += 1;
        c.insets = new Insets(20, 0, 110, 0);

        JPanel buttonWrapperPanel = new JPanel(new BorderLayout());
        buttonWrapperPanel.setPreferredSize(new Dimension(230, 22));
        buttonWrapperPanel.setOpaque(false);
        btnSignIn = new RoundedButton("Sign In", "Sign In");//new RoundedCornerButton("Sign in", "Sign in");
        btnSignIn.addActionListener(actionListener);
        btnSignIn.addKeyListener(this);
        buttonWrapperPanel.add(btnSignIn, BorderLayout.WEST);
        topMainPanel.add(getWrapperPanel(buttonWrapperPanel), c);

        GridBagConstraints bC = new GridBagConstraints();
        bC.gridx = 0;
        bC.gridy = 0;
        bC.anchor = GridBagConstraints.WEST;
        bC.insets = new Insets(5, 0, 0, 0);

        JLabel lblDontHaveAccount = new JLabel("Don't have a ringID?");
        lblDontHaveAccount.setForeground(DefaultSettings.APP_DEFAULT_FONT_COLOR);
        lblDontHaveAccount.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        bottomMainPanel.add(getWrapperPanel(lblDontHaveAccount), bC);

        bC.gridy += 1;
        bC.insets = new Insets(0, 0, 40, 0);

        lblCreateNew = DesignClasses.makeAncorLabelDefaultColor("Sign-up now", 1, 13);
        lblCreateNew.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        lblCreateNew.setForeground(RingColorCode.RING_THEME_COLOR);
        lblCreateNew.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                new RingIdRequest(errorLabel, errorTextArea, btnSignIn, lblCreateNew).start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }

        });
        bottomMainPanel.add(getWrapperPanel(lblCreateNew), bC);

        selectionList = new SelectionListSigninSignup(SelectionListSigninSignup.TYPE_SIGN_IN);
        selectionList.ringIDorMobileorEmailTextField.addKeyListener(this);
        selectionList.ringIDorMobileorEmailTextField.addFocusListener(this);
        ringIdPanel.add(selectionList);

        passTextField = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, number_field_width, 30, MaxLengths.PASSWORD, false);
        passTextField.setPreferredSize(new Dimension(number_field_width + 60, 30));
        passTextField.addMouseListener(new ContextMenuMouseListener());
        passTextField.setForeground(DefaultSettings.disable_font_color);
        passTextField.setToolTipText("Enter " + StaticFields.PASSWORD_TEXT);
        passTextField.setOpaque(false);
        passTextField.setBorder(BorderFactory.createCompoundBorder(passTextField.getBorder(), BorderFactory.createEmptyBorder(5, 7, 3, 5)));
        passTextField.addFocusListener(this);
        passTextField.addKeyListener(this);
        passwordPanel.add(passTextField);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);
    }

    ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            actionSource = e.getSource();
            if (actionSource == btnSignIn) {
                if (savePassCheckbox.isSelected()) {
                    MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 1;
                } else {
                    MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 0;
                    MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 0;
                }
                MyFnFSettings.VALUE_NEW_USER_NAME = "";
                errorLabel.setText("");
                errorLabel.setIcon(null);
                if (selectionList.ringIDorMobileorEmailTextField.getText().equals(selectionList.default_input_text) || selectionList.ringIDorMobileorEmailTextField.getText().trim().length() < 1) {
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    String id = selectionList.TXT_RINGID;
                    if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN) {
                        id = selectionList.TXT_PHN;
                    } else if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN) {
                        id = selectionList.TXT_EMAIL;
                    }
                    errorTextArea.setText(id + " required");
                } else if (passTextField.getText().equals(DEFAULT_PASSWORD) || passTextField.getText().trim().length() < 1) {
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(StaticFields.PASSWORD_TEXT + " required");
                } else if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN && !HelperMethods.isValidEmail(selectionList.ringIDorMobileorEmailTextField.getText())) {
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERROR_MSG_EMAIL);
                } else if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN && !HelperMethods.check_mobile_number(selectionList.ringIDorMobileorEmailTextField.getText())) {
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERROR_MSG_MOBILE);
                } else {
                    if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN) {
                        MyFnFSettings.VALUE_MOBILE_DIALING_CODE = selectionList.countryCodeTextField.getText().trim();
                        (new SignInRequest(errorLabel, errorTextArea, selectionList.ringIDorMobileorEmailTextField.getText().trim(), passTextField.getText(), btnSignIn, lblCreateNew, null, selectionList.type_int, selectionList.countryCodeTextField.getText().trim(), false)).start();
                    } else {
                        MyFnFSettings.VALUE_MOBILE_DIALING_CODE = null;
                        (new SignInRequest(errorLabel, errorTextArea, selectionList.ringIDorMobileorEmailTextField.getText().trim(), passTextField.getText(), btnSignIn, lblCreateNew, null, selectionList.type_int, false)).start();
                    }
                }
            } /*else if (actionSource == lblCreateNew) {
             new RingIdRequest(errorLabel, errorTextArea, btnSignIn, lblCreateNew).start();
             }*/

        }
    };

    @Override
    public void keyTyped(KeyEvent event) {
        actionSource = event.getSource();
        if (actionSource == passTextField) {
            //savePassCheckbox.setVisible(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        actionSource = event.getSource();
        int key = (int) event.getKeyCode();

        if (passTextField.getForeground() != DefaultSettings.disable_font_color) {

            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                warningLabel.setVisible(true);
            } else {
                warningLabel.setVisible(false);
            }
        }
        if (key == KeyEvent.VK_ENTER) {
            if (!btnSignIn.isEnabled()) {
                errorLabel.setText(" ");
                errorLabel.setIcon(null);
            } else {
                btnSignIn.doClick();
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
        } else if (actionSource == passTextField) {
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                warningLabel.setVisible(true);
            } else {
                warningLabel.setVisible(false);
            }

            if (passTextField.getText().equals(DEFAULT_PASSWORD) || passTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(passTextField, DEFAULT_PASSWORD, false);
            } else if (passTextField.getText().length() < 1) {
                extra_methods.set_reset_defalut_text(passTextField, DEFAULT_PASSWORD, true);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == selectionList.ringIDorMobileorEmailTextField) {
            extra_methods.set_reset_defalut_text(selectionList.ringIDorMobileorEmailTextField, selectionList.default_input_text, true);
        } else if (actionSource == passTextField) {
            extra_methods.set_reset_defalut_text(passTextField, DEFAULT_PASSWORD, true);
        }
    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 35));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    private JPanel getWrapperPanel(JLabel label) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 25));
        wrapper.add(label);
        wrapper.setOpaque(false);
        return wrapper;
    }
}

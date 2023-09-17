/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.SettingsConstants;
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
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.RecoverySuggestionRequest;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.ImagePane;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

/**
 *
 * @author Sirat Samyoun
 */
public class RecoverPasswordPanel extends ImagePane implements ActionListener, KeyListener, FocusListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RecoverPasswordPanel.class);
    private static RecoverPasswordPanel recoverPasswordPanel;
    public SelectionListSigninSignup selectionList;
    public Object actionSource;
    public Object focusedSource;
    private JPanel containerPanel;
    private JPanel mainPanel;
    private UiMethods extra_methods = new UiMethods();
    public JPanel getSuggestionsPanel, errorPanel;
    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
    private JButton getSuggestionButton, cancelButton;
    public JLabel errorLabel;
    public JTextArea errorTextArea;
    private static String ERR_RING_MAIL_MOB = "Please Enter a valid RingID/verified email or number";
    private static String ERR_RING = " Must provide a valid ringiD";
    private static String ERR_MAIL = " Must provide a valid Email";
    private static String ERR_MOB = " Must provide a valid Phone number";

    public static RecoverPasswordPanel getInstance() {
        return recoverPasswordPanel;
    }

    public RecoverPasswordPanel() {
        recoverPasswordPanel = this;
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
        topPanel.setBorder(new EmptyBorder(150, 0, 0, 0));
        containerPanel.add(topPanel, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 70, 7));
        titlePanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Recover Password");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        titlePanel.add(titleLabel);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        //mainPanel.setBackground(Color.blue);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        getSuggestionsPanel = new JPanel(new BorderLayout());
        getSuggestionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        getSuggestionsPanel.setPreferredSize(new Dimension(0, 350));
        getSuggestionsPanel.setOpaque(false);
        //getSuggestionsPanel.setBackground(Color.yellow);
        containerPanel.add(getSuggestionsPanel, BorderLayout.SOUTH);
        initContents();
    }

    public static JTextArea createJTextAreaForSignIn(String text, int font_size) {
        JTextArea row2 = new JTextArea(text);
        try {
            row2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font_size));
        } catch (Exception e) {
        }
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        row2.setEditable(false);
        row2.setColumns(28);
        /*Border border = new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);//BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
         row2.setBorder(BorderFactory.createCompoundBorder(border,
         BorderFactory.createEmptyBorder(5, 5, 5, 5)));*/
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
        }
        return row2;
    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 0, 5, 0);
        c.anchor = GridBagConstraints.WEST;

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        mainPanel.add(infoPanel, c);

        infoPanel.add(createJTextAreaForSignIn("Please enter your ringID or verified E-mail or verified Phone number to recover your password", 12));

        errorPanel = new JPanel(new BorderLayout());
        errorPanel.setPreferredSize(new Dimension(337, 14));
        errorPanel.setOpaque(false);
        errorPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        infoPanel.add(errorPanel);
        errorLabel = DesignClasses.makeAncorLabel("", 0, 12);
        errorTextArea = new JTextArea("");
        errorTextArea.setEnabled(false);
        errorTextArea.setColumns(27);
        errorTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setBorder(null);
        errorTextArea.setOpaque(false);
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.add(errorTextArea, BorderLayout.CENTER);

        // mainPanel.add(getInstPanel(), c);
        c.gridy++;
        selectionList = new SelectionListSigninSignup(SelectionListSigninSignup.TYPE_RECOVER_PASS);
        selectionList.ringIDorMobileorEmailTextField.addKeyListener(this);
        selectionList.ringIDorMobileorEmailTextField.addFocusListener(this);
        mainPanel.add(selectionList, c);
        c.gridy++;
        getSuggestionButton = new RoundedButton("Get Suggestions", "Get Suggestions");
        getSuggestionButton.addActionListener(this);
        cancelButton = new RoundedButton("Cancel", "Cancel");
        cancelButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel(new BorderLayout(0, 0));
        buttonsPanel.setPreferredSize(new Dimension(265, 25));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(getSuggestionButton, BorderLayout.WEST);
        buttonsPanel.add(cancelButton, BorderLayout.EAST);
        mainPanel.add(getWrapperPanel(buttonsPanel), c);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        errorLabel.setIcon(null);
        errorTextArea.setText("");
        if (e.getSource() == getSuggestionButton) {
            if (selectionList.ringIDorMobileorEmailTextField.getText().trim().length() > 0
                    && !selectionList.ringIDorMobileorEmailTextField.getText().trim().equals(selectionList.default_input_text)) {
                if (selectionList.type_int == SettingsConstants.RINGID_LOGIN
                        && !HelperMethods.isValidNumber(selectionList.ringIDorMobileorEmailTextField.getText())) {
                    getSuggestionsPanel.removeAll();
                    getSuggestionsPanel.revalidate();
                    getSuggestionsPanel.repaint();
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_RING);
                } else if (selectionList.type_int == SettingsConstants.EMAIL_LOGIN
                        && !HelperMethods.isValidEmail(selectionList.ringIDorMobileorEmailTextField.getText())) {
                    getSuggestionsPanel.removeAll();
                    getSuggestionsPanel.revalidate();
                    getSuggestionsPanel.repaint();
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_MAIL);
                } else if (selectionList.type_int == SettingsConstants.MOBILE_LOGIN
                        && !HelperMethods.check_mobile_number(selectionList.ringIDorMobileorEmailTextField.getText())) {
                    getSuggestionsPanel.removeAll();
                    getSuggestionsPanel.revalidate();
                    getSuggestionsPanel.repaint();
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText(ERR_MOB);
                } else {
                    new RecoverySuggestionRequest(selectionList.getRingIDMobileMail(), getSuggestionsPanel, getSuggestionButton, cancelButton).start();
                }
            } else {
                getSuggestionsPanel.removeAll();
                getSuggestionsPanel.revalidate();
                getSuggestionsPanel.repaint();
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText(ERR_RING_MAIL_MOB);
            }
        } else if (e.getSource() == cancelButton) {
            LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().MAIN_LOGIN_UI);
        }
    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 35));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    /*private JPanel getInstPanel() {
     JPanel instPanel = new JPanel();
     instPanel.setOpaque(false);
     //instPanel.setBackground(Color.YELLOW);
     instPanel.setPreferredSize(new Dimension(350, 60));
     instPanel.setLayout(new BoxLayout(instPanel, BoxLayout.Y_AXIS));

     JPanel text1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     text1Panel.setOpaque(false);
     JLabel text1Label = new JLabel("Please enter your ringID");
     text1Label.setForeground(new Color(0x686868));
     text1Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
     text1Panel.add(text1Label);

     JPanel text2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     text2Panel.setOpaque(false);
     JLabel text2Label = new JLabel("Or,");
     text2Label.setForeground(new Color(0x686868));
     text2Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
     text2Panel.add(text2Label);

     JPanel text3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     text3Panel.setOpaque(false);
     JLabel text3Label = new JLabel("Verified Email/ Phone number associated with your ringID");
     text3Label.setForeground(new Color(0x686868));
     text3Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
     text3Panel.add(text3Label);

     instPanel.add(text1Panel);
     instPanel.add(text2Panel);
     instPanel.add(text3Panel);
     return instPanel;
     }*/
    @Override
    public void keyTyped(KeyEvent event) {
        actionSource = event.getSource();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        actionSource = event.getSource();
        int key = (int) event.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            getSuggestionButton.doClick();
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
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        actionSource = e.getSource();
        if (actionSource == selectionList.ringIDorMobileorEmailTextField) {
            extra_methods.set_reset_defalut_text(selectionList.ringIDorMobileorEmailTextField, selectionList.default_input_text, true);
        }
    }
}

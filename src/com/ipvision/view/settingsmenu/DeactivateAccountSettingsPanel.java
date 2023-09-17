/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
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
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.DeactivateAccount;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Sirat Samyoun
 */
public class DeactivateAccountSettingsPanel extends JPanel implements FocusListener, ActionListener {

    private JButton btnOk;
    private JButton btnCancel;
    private JPanel mainContent;
    private GridBagConstraints con;
    private JPasswordField password;
    private UiMethods extra_methods = new UiMethods();
    final String DEFAULT_PASSWORD = "Password";
    private JLabel errorLabel;
    private JTextArea errorTextArea;

    public DeactivateAccountSettingsPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(5, 5));
        headerPanel.setPreferredSize(new Dimension(0, 30));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        add(headerPanel, BorderLayout.NORTH);

        JLabel myNameLabel = DesignClasses.makeLableBold1("Deactivate Account", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);

//        mainContent = new JPanel();
//        mainContent.setBorder(new EmptyBorder(7, 7, 7, 7));
//        mainContent.setOpaque(true);
//        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent = new JPanel(new GridBagLayout());
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(3, 5, 3, 2);
        con.anchor = GridBagConstraints.WEST;
        mainContent.setOpaque(false);

        bodyPanel.add(mainContent, BorderLayout.NORTH);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 35));
        add(footerPanel, BorderLayout.SOUTH);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonContainer.setOpaque(false);

        btnOk = new RoundedCornerButton("Deactivate", "Deactivate my Account");
        //DesignClasses.createImageButton(GetImages.SAVE_CHANGES, GetImages.SAVE_CHANGES_H, "Save");
        btnOk.addActionListener(this);

        btnCancel = new RoundedCornerButton("Cancel", "Cancel");
//DesignClasses.createImageButton(GetImages.RESET_CHANGES, GetImages.RESET_CHANGES_H, "Reset");
        btnCancel.addActionListener(this);

//        if (MyFnFUtilities.isInternetAvailable) {
//            buttonContainer.add(btnOk);
//            buttonContainer.add(btnCancel);
//        }
        footerPanel.add(buttonContainer, BorderLayout.CENTER);
        buildDeactivateSettings();
    }

    private void buildDeactivateSettings() {

        mainContent.add(Box.createRigidArea(new Dimension(0, 10)), con);
        con.gridy++;

        JLabel titleLabel = DesignClasses.makeLableBold1("Attention!", 18);
        mainContent.add(titleLabel, con);
        con.gridy++;

        JLabel label1 = DesignClasses.makeJLabelFullName("Are you sure you want to close your ringID account?", 13);
        mainContent.add(label1, con);
        con.gridy++;

        JLabel label2 = DesignClasses.makeJLabelFullName("Choosing Close Account will remove all your data from ringID & deactivate ringID from all of your devices", 13);
        mainContent.add(label2, con);
        con.gridy++;

        mainContent.add(Box.createRigidArea(new Dimension(0, 5)), con);
        con.gridy++;

        JLabel passLabel = DesignClasses.makeJLabelFullName("Please enter your password to deactivate your account ", 13);
        mainContent.add(passLabel, con);
        con.gridy++;

        JPanel passwordPanel = new JPanel();
        passwordPanel.setOpaque(false);
        password = DesignClasses.makeJpasswordField(DEFAULT_PASSWORD, 165, 25, MaxLengths.PASSWORD, true);
        password.setForeground(DefaultSettings.disable_font_color);
        password.setToolTipText("Enter " + DEFAULT_PASSWORD);
        password.addFocusListener(this);
        passwordPanel.add(password);
        JPanel errorPanel = new JPanel(new BorderLayout(0, 0));
        errorPanel.setOpaque(false);
        errorLabel = DesignClasses.makeAncorLabel("", 0, 12);
        errorTextArea = new JTextArea();
        //errorPanel.setPreferredSize(new Dimension(50, 0));
        //   errorLabel.setPreferredSize(new Dimension(22, 33));
        errorTextArea.setEnabled(false);
        errorTextArea.setColumns(27);
        errorTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setBorder(null);
        errorTextArea.setOpaque(false);
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.setOpaque(false);
        errorPanel.add(errorTextArea, BorderLayout.CENTER);
        passwordPanel.add(errorPanel);
        mainContent.add(passwordPanel, con);
        con.gridy++;

        mainContent.add(Box.createRigidArea(new Dimension(0, 10)), con);
        con.gridy++;

        JPanel buttonsPanel = new JPanel(new BorderLayout());//new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnOk, BorderLayout.WEST);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.CENTER);
        buttonsPanel.add(btnCancel, BorderLayout.EAST);
        mainContent.add(buttonsPanel, con);
        con.gridy++;
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)), con);
        con.gridy++;

    }

    @Override
    public void focusGained(FocusEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (password.getText().equals(DEFAULT_PASSWORD) || password.getText().length() < 1) {
            extra_methods.set_reset_defalut_text(password, DEFAULT_PASSWORD, false);
        } else if (password.getText().length() < 1) {
            extra_methods.set_reset_defalut_text(password, DEFAULT_PASSWORD, true);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        extra_methods.set_reset_defalut_text(password, DEFAULT_PASSWORD, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == btnOk) {
            if (password.getForeground() == DefaultSettings.disable_font_color) {
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText("Please enter your password");
            } else {
                if (!password.getText().equals(MyFnFSettings.VALUE_LOGIN_USER_PASSWORD)) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                    errorTextArea.setText("Must enter your correct password");
                } else {
                    //System.out.println("sdQAVJ");
                    new DeactivateAccount(errorLabel, errorTextArea, btnOk, btnCancel).start();
                }
            }
        } else if (e.getSource() == btnCancel) {
            // System.out.println("dasfs");
            // extra_methods.set_reset_defalut_text(password, DEFAULT_PASSWORD, true);
            //GuiRingID.getInstance().getTopMenuBar().setRingIDSettingsDialog(null);   
            password.setForeground(DefaultSettings.disable_font_color);
            password.setToolTipText("Enter " + DEFAULT_PASSWORD);
            password.setText(DEFAULT_PASSWORD);
            errorTextArea.setText("");
            errorTextArea.setForeground(null);
            errorLabel.setIcon(null);
           // GuiRingID.getInstance().getTopMenuBar().setRingIDSettingsDialog(null);
            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
        }
    }
}

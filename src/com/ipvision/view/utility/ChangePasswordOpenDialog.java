/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.JOptionPanelBasicsShadow;
import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserLogInInfo;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Sirat Samyoun
 */
public class ChangePasswordOpenDialog extends JOptionPanelBasicsShadow implements ActionListener {

    JLabel error_text_label;
    //JLabel old_label;
    JPasswordField old_password;
    //  JPanel oldPassPanel;
//    JLabel new_label;
    JPasswordField new_password;
    JPasswordField confirm_new_password;
    //  JPanel newPassPanel;
    JButton ok_button;
    JButton cancel_button;
    //JPanel buttonPanel;

    public ChangePasswordOpenDialog() {
        setTitle("Change Password");
        setMinimumSize(new Dimension(width + 20, height + 40));
        componentContent.setBorder(new EmptyBorder(padding + 20, padding, padding, padding));
    }

    public void init() {

        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.ipady = 5;
        con.gridy = 0;

//        final JDialog dialog = create_joption_panel(300, 160, "Change Password", null);
        //      JPanel panel = new JPanel();
        error_text_label = DesignClasses.makeJLabelFullName(" ");
        error_text_label.setFont(new Font("Arial", Font.PLAIN, 13));
        //   error_text_label.setPreferredSize(new Dimension(250, 25));
        error_text_label.setForeground(DefaultSettings.errorLabelColor);
        content.add(error_text_label, con);
        con.gridy++;

        JLabel old_label = DesignClasses.makeJLabelFullName("Old Password");
        old_label.setFont(new Font("Arial", Font.PLAIN, 13));
        old_label.setPreferredSize(new Dimension(140, 25));
        old_password = DesignClasses.makeJpasswordField("", DefaultSettings.textBoxWidth, DefaultSettings.texBoxHeight, MaxLengths.PASSWORD, true);
        JPanel oldPassPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        oldPassPanel.setOpaque(false);
        oldPassPanel.add(old_label, BorderLayout.WEST);
        oldPassPanel.add(old_password, BorderLayout.CENTER);
        content.add(oldPassPanel, con);
        con.gridy++;

        JLabel new_label = DesignClasses.makeJLabelFullName("New Password");
        new_label.setFont(new Font("Arial", Font.PLAIN, 13));
        new_label.setPreferredSize(new Dimension(140, 25));
        new_password = DesignClasses.makeJpasswordField("", DefaultSettings.textBoxWidth, DefaultSettings.texBoxHeight, MaxLengths.PASSWORD, true);
        new_password.setText(null);
        new_password.setBackground(Color.WHITE);
        JPanel newPassPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        newPassPanel.setOpaque(false);
        newPassPanel.add(new_label);
        newPassPanel.add(new_password);
        content.add(newPassPanel, con);
        con.gridy++;

        JLabel confirm_new_label = DesignClasses.makeJLabelFullName("Confirm New Password");
        confirm_new_label.setFont(new Font("Arial", Font.PLAIN, 13));
        confirm_new_label.setPreferredSize(new Dimension(140, 25));
        confirm_new_password = DesignClasses.makeJpasswordField("", DefaultSettings.textBoxWidth, DefaultSettings.texBoxHeight, MaxLengths.PASSWORD, true);
        confirm_new_password.setText(null);
        confirm_new_password.setBackground(Color.WHITE);
        JPanel confirmNewPassPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        confirmNewPassPanel.setOpaque(false);
        confirmNewPassPanel.add(confirm_new_label, BorderLayout.WEST);
        confirmNewPassPanel.add(confirm_new_password, BorderLayout.CENTER);
        content.add(confirmNewPassPanel, con);
        con.gridy++;

        ok_button = new RoundedCornerButton("Ok", "Ok");
        ok_button.setFocusPainted(false);
        cancel_button = new RoundedCornerButton("Cancel", "Cancel");// DesignClasses.createImageButton(GetImages.BUTTON_CANCEL, GetImages.BUTTON_CANCEL_H, "Cancel");
        cancel_button.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 7));
        buttonPanel.setOpaque(false);
        buttonPanel.add(ok_button);
        buttonPanel.add(cancel_button);
        content.add(buttonPanel, con);
        con.gridy++;

        cancel_button.addActionListener(this);

        ok_button.addActionListener(this);
//        getContentPane().add(panel);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String notify_msg = "";
        int success = 0;
        if (e.getSource() == ok_button) {
            if (old_password.getText().length() > 0 && new_password.getText().length() > 0 && confirm_new_password.getText().length() > 0) {
                if (new_password.getText().equals(confirm_new_password.getText())) {
                    if (MyFnFSettings.VALUE_LOGIN_USER_PASSWORD.equals(old_password.getText())) {
                        if (!old_password.getText().equals(new_password.getText())) {
                            String msg = HelperMethods.password_check(new_password.getText());
                            if (msg.length() == 0) {
                                String pak_id = SendToServer.getRanDomPacketID();
                                JsonFields jF = new JsonFields();
                                jF.setAction(AppConstants.TYPE_CHANGE_PASSWORD);
                                jF.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                                jF.setPacketId(pak_id);
                                jF.setOldPass(old_password.getText());
                                jF.setNewPass(new_password.getText());
                                SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                                disposeThis();
                                for (int i = 1; i <= 3; i++) {
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception ex) {
                                    }
                                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().isEmpty()
                                            || MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id) == null) {
                                        SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                                    } else {
                                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id);
                                        if (js.getSuccess()) {
                                            MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = new_password.getText();
                                            notify_msg = "";
                                            success = 1;

                                            UserLogInInfo passwordInfo = new UserLogInInfo();
                                            passwordInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_PASSWORD);
                                            String newPass =HelperMethods.encryptPassword(new_password.getText());
//                                            String password = new_password.getText();
//                                            if (password != null && password.length() > 0) {
//                                                for (int j = 0; j < password.length(); j++) {
//                                                    int val = (int) password.charAt(j);
//                                                    if (val < 2 || val > 126) {
//                                                        newPass += password.charAt(j);
//                                                    } else if (val % 2 == 0) {
//                                                        newPass += (char) (password.charAt(j) - 1);
//                                                    } else {
//                                                        newPass += (char) (password.charAt(j) + 1);
//                                                    }
//                                                }
//                                            }
                                            passwordInfo.setValue(newPass);  //String.valueOf(passTextField.getPassword())  
                                            List<UserLogInInfo> userLogInInfoList = new ArrayList<UserLogInInfo>();
                                            userLogInInfoList.add(passwordInfo);
                                            (new InsertRingLoginSetting(userLogInInfoList)).start();
                                        } else {
                                            notify_msg = "Password change failed!";
                                        }
                                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pak_id);
                                    }
                                }
                            } else {
                                notify_msg = msg;
                            }
                        } else {
                            notify_msg = "Same password used";
                        }
                    } else {
                        notify_msg = "Wrong Old Password";
                    }
                } else {
                    notify_msg = "New Passwords doesn't match";
                }
            } else {
                notify_msg = "Please fill out all password fields";
            }
            if (success == 1) {
                error_text_label.setText(notify_msg);

                disposeThis();
            } else {
                error_text_label.setText(notify_msg);
            }
        } else if (e.getSource() == cancel_button) {
            disposeThis();
        }
    }

    private void disposeThis() {
        this.setVisible(false);
        this.dispose();
        System.gc();
        Runtime.getRuntime().gc();
    }

//    public static void main(String[] args) {
//        ChangePasswordOpenDialog changePasswordOpenDialog = new ChangePasswordOpenDialog();
//        changePasswordOpenDialog.setVisible(true);
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.view.loginsignup.LoginUI;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class ResetPassword extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ResetPassword.class);
    private String userId;
    private String newPass;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton doneButton;
    private JButton cancelButton;

    public ResetPassword(String userId, String newPass, JLabel errorLabel, JTextArea errorTextArea, JButton doneButton, JButton cancelButton) {
        this.userId = userId;
        this.newPass = newPass;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.doneButton = doneButton;
        this.cancelButton = cancelButton;
    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (doneButton != null) {
                    doneButton.setEnabled(false);
                }
                if (cancelButton != null) {
                    cancelButton.setEnabled(false);
                }
                if (errorLabel != null) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                }
                if (errorTextArea != null) {
                    errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                    errorTextArea.setText(" Please wait...");
                }
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_RESET_PASSWORD);
                js_fields.setUserIdentity(userId);
                js_fields.setNewPass(newPass);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        this.doneButton.setEnabled(true);
                        this.cancelButton.setEnabled(true);
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            if (LoginUI.getLoginUI().mainLoginPanel != null) {
                                LoginUI.getLoginUI().mainLoginPanel.selectionList.ringIDorMobileorEmailTextField.setText(userId);
                                LoginUI.getLoginUI().mainLoginPanel.passTextField.setText(newPass);
                                LoginUI.getLoginUI().mainLoginPanel.selectionList.type_int = SettingsConstants.RINGID_LOGIN;
                                LoginUI.getLoginUI().mainLoginPanel.btnSignIn.setEnabled(true);
                                LoginUI.getLoginUI().mainLoginPanel.btnSignIn.doClick();
                            }
//                            LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().SUCCESS_UI);
//                            LoginUI.getLoginUI().verifyinRecoverPasswordPanel.selectionList.type_int;
//                            LoginUI.getLoginUI().verifyinRecoverPasswordPanel.selectionList.countryCodeTextField.getText();
//                            LoginUI.getLoginUI().verifyinRecoverPasswordPanel.selectionList.ringIDorMobileorEmailTextField.getText();
//                            LoginUI.getLoginUI().successPanel.setValuesFromResetPassowrdUI(js_fields, i);
                        } else {
                            errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                            errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                            errorTextArea.setText(" " + feedBackFields.getMessage());
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in ResetPassword class ==>" + e.getMessage());
                this.doneButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText("Failed! To reset password");
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
            this.doneButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
        }
    }
}

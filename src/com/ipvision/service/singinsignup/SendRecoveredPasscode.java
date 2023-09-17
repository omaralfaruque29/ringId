/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.view.loginsignup.VerifyPasswordPanel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class SendRecoveredPasscode extends Thread {

    private String recoverBy;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton sendButton;
    private JButton doneButton;
    private JButton cancelButton;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendRecoveredPasscode.class);

    public SendRecoveredPasscode(String email,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton sendButton,
            JButton doneButton,
            JButton cancelButton) {
        this.recoverBy = email;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.sendButton = sendButton;
        this.doneButton = doneButton;
        this.cancelButton = cancelButton;
    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (sendButton != null) {
                    sendButton.setEnabled(false);
                }
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
                js_fields.setAction(AppConstants.TYPE_SEND_RECOVERED_PASSCODE);
                js_fields.setRecoveryBy(recoverBy);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        this.doneButton.setEnabled(true);
                        this.sendButton.setEnabled(true);
                        this.cancelButton.setEnabled(true);
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            VerifyPasswordPanel.getResponseUserId = feedBackFields.getUserIdentity();
                            errorLabel.setIcon(null);
                            errorTextArea.setText(feedBackFields.getMessage());
                        } else {
                            VerifyPasswordPanel.getResponseUserId = null;
                            errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                            errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                            errorTextArea.setText(feedBackFields.getMessage());
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in SendRecoveredPasscode class ==>" + e.getMessage());
                this.doneButton.setEnabled(true);
                this.sendButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                VerifyPasswordPanel.getResponseUserId = null;
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText(" Failed! To send recovered passcode");
                log.error("Failed! To send recovered passcode" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

}

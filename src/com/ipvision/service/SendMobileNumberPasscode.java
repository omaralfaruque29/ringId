/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class SendMobileNumberPasscode extends Thread {

    private boolean success = false;
    private String mobileNumber, mobileCode;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton sendButton;
    private JButton doneButton;
    private JButton cancelButton;
    private boolean ver_msg_show = false;
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendMobileNumberPasscode.class);

    public SendMobileNumberPasscode(String mobileNumber, String mobileCode, JLabel errorLabel, JTextArea errorTextArea, JButton sendButton, JButton doneButton, JButton cancelButton) {
        this.mobileNumber = mobileNumber;
        this.mobileCode = mobileCode;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.sendButton = sendButton;
        this.doneButton = doneButton;
        this.cancelButton = cancelButton;
        setName(this.getClass().getSimpleName());
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
                js_fields.setAction(AppConstants.TYPE_SEND_MOBILE_PASSCODE);
                // js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.VALUE_NEW_USER_NAME);
                js_fields.setMobilePhone(mobileNumber);
                js_fields.setMobilePhoneDialingCode(mobileCode);
                js_fields.setDeviceUniqueId(HelperMethods.getPCMacAddress());

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
                            this.errorLabel.setIcon(null);
                            this.errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                            this.errorTextArea.setText(feedBackFields.getMessage());
                            // new SignInRequest(errorLabel, errorTextArea, MyFnFSettings.LOGIN_USER_ID, password, doneButton, sendButton, cancelButton).start();
                        } else {
                            log.error(AppConstants.TYPE_SEND_MOBILE_PASSCODE + " response not in correct format==>" + feedBackFields.getMessage());
                            this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                            this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                            this.errorTextArea.setText(feedBackFields.getMessage());
                        }
                        break;
                    }
                }
                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
            } catch (InterruptedException | HeadlessException ex) {
                log.error("Exception in SendMobileNumberPasscode class ==>" + ex.getMessage());
                this.doneButton.setEnabled(true);
                this.sendButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText(" Failed! Try again");
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

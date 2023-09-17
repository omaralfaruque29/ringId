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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class DeactivateAccount extends Thread {

    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton okButton;
    private JButton cancelButton;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeactivateAccount.class);

    public DeactivateAccount(
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton okButton,
            JButton cancelButton) {
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.okButton = okButton;
        this.cancelButton = cancelButton;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                // System.out.println("FMAAQAVJ");
                if (okButton != null) {
                    okButton.setEnabled(false);
                }
                if (cancelButton != null) {
                    cancelButton.setEnabled(false);
                }
                if (errorLabel != null) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                }
                if (errorTextArea != null) {
                    errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                    errorTextArea.setText("Please wait...");
                }
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_DEACTIVATE_ACCOUNT);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                //  System.out.println("FMAAQAVJ");
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        this.okButton.setEnabled(true);
                        this.cancelButton.setEnabled(true);
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            errorLabel.setIcon(null);
                            errorTextArea.setText("");
                            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().setVisible(false);
                            GuiRingID.getInstance().getTopMenuBar().setRingIDSettingsDialog(null);
                            //   LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().RESET_PASSWORD_UI);

                        } else {
                            errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                            errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                            errorTextArea.setText(feedBackFields.getMessage());
                        }
                        break;
                    }
                }

            } catch (Exception e) {
              //  e.printStackTrace();
                this.okButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                errorTextArea.setText("Failed! To send recovered passcode");
                log.error("Failed! To send recovered passcode" + e.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

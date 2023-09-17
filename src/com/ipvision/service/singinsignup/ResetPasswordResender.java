/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ResetPasswordResender extends Thread {

    private JLabel loadingOrtimePanel;
    private JButton loginbutton;
    private String secrekeKey;
    private String newpassword;

    public ResetPasswordResender(JLabel loadingOrtimePanel, String secretKey, String newpassword, JButton loginbutton) {
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.secrekeKey = secretKey;
        this.newpassword = newpassword;
        this.loginbutton = loginbutton;

    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            try {

                this.loginbutton.setEnabled(false);
                this.loadingOrtimePanel.setIcon(null);
                this.loadingOrtimePanel.setText("");
                JsonFields singInpacked = new JsonFields();
                singInpacked.setAction(AppConstants.TYPE_PASSWORD_RESET);
                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSecretCode(this.secrekeKey);
                singInpacked.setNewPass(this.newpassword);

                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                this.loadingOrtimePanel.setText("Please wait...");
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        this.loginbutton.setEnabled(true);
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.OK_MINI));
                            this.loadingOrtimePanel.setText("Your password changed succefully, login now");
                            //   HelperMethods.show_long_notification_msg("You have received a secret code in your email address. please check and reset your password");

                        } else {
                            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
                            this.loadingOrtimePanel.setForeground(DefaultSettings.errorLabelColor);
                            this.loadingOrtimePanel.setText(fields.getMessage());

                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }

                }

                this.loginbutton.setEnabled(true);
                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
                this.loadingOrtimePanel.setForeground(DefaultSettings.errorLabelColor);
                this.loadingOrtimePanel.setText("Failed! " + NotificationMessages.NETWORK_PROBLEM);
            } catch (Exception ex) {
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

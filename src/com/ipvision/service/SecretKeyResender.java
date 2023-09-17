/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class SecretKeyResender extends Thread {

    private JLabel loadingOrtimePanel;
    private String userid;// groupid;
    private JButton loginbutton;

    public SecretKeyResender(JLabel loadingOrtimePanel, String userid, JButton loginbutton) {
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.userid = userid;
        this.loginbutton = loginbutton;
        setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {

                this.loginbutton.setEnabled(false);
                this.loadingOrtimePanel.setIcon(null);
                this.loadingOrtimePanel.setText("");
                JsonFields singInpacked = new JsonFields();
                singInpacked.setAction(AppConstants.TYPE_PASSWORD_RECOVERY);
                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setUserIdentity(userid);
                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                this.loadingOrtimePanel.setText("Please wait...");
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        this.loginbutton.setEnabled(true);
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.OK_MINI));
                            this.loadingOrtimePanel.setText("Please check your email");
                            //  HelperMethods.show_long_notification_msg("You have received a secret code in your email address. please check and reset your password");
                            //JOptionPane.showConfirmDialog(null, "You have received a secret code in your email address. please check and reset your password", StaticFields.APP_NAME, JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                            HelperMethods.showWarningDialogMessage("You have received a secret code in your email address. please check and reset your password");
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

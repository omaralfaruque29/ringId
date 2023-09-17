/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
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
public class InviteMobileFriends extends Thread {

    private JLabel loadingLable;
    private JButton sendbutton;
    private String mobileNumbers;

    public InviteMobileFriends(JLabel loadingLable, JButton sendbutton, String mobileNumbers) {
        this.loadingLable = loadingLable;
        this.sendbutton = sendbutton;
        this.mobileNumbers = mobileNumbers;
        setName(this.getClass().getSimpleName());
    }

    private void setMessage(int a) {
        String string;
        switch (a) {
            case 1:
                string = ".";
                break;
            case 2:
                string = "..";
                break;
            case 3:
                string = "...";
                break;
            case 4:
                string = "....";
                break;
            default:
                string = "";

        }
        this.loadingLable.setText("Please wait" + string);

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                this.sendbutton.setEnabled(false);
                this.loadingLable.setForeground(DefaultSettings.blue_bar_background);
                String pak_id = System.currentTimeMillis() + MyFnFSettings.LOGIN_SESSIONID;
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ADD_ADDRESS_BOOK_FRIEND);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pak_id);
                js_fields.setSearchParam(this.mobileNumbers);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(50);
                for (int i = 1; i <= 3; i++) {
                    setMessage(i);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().isEmpty() || MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id);
                        if (js.getSuccess()) {
                            this.loadingLable.setText("Successfully added the following friends");
                        } else {
                            this.loadingLable.setForeground(DefaultSettings.errorLabelColor);
                            this.loadingLable.setText("No contacts added !!");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pak_id);
                        return;
                    }
                    Thread.sleep(500);
                }
                this.loadingLable.setForeground(DefaultSettings.errorLabelColor);
                this.sendbutton.setEnabled(true);
                this.loadingLable.setText("Failed! " + NotificationMessages.NETWORK_PROBLEM);
            } catch (Exception e) {
                this.loadingLable.setForeground(DefaultSettings.errorLabelColor);
                this.sendbutton.setEnabled(true);
                this.loadingLable.setText("Failed !");
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

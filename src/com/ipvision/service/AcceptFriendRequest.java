/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Faiz Ahmed
 */
public class AcceptFriendRequest extends Thread {
//

    private String userId;
    private JLabel textLabel;
    private JButton button;
    private String text;
    private Icon icon;
    private int contactType;
    private List<JButton> tobeDisabledList;

    public AcceptFriendRequest(String userid, JLabel textLabel, int contactType) {
        this.userId = userid;
        this.textLabel = textLabel;
        if (textLabel.getText() != null && textLabel.getText().length() > 0) {
            this.text = textLabel.getText();
        }
        this.contactType = contactType;
    }

    public AcceptFriendRequest(String userid, JButton button, int contactType) {
        this.userId = userid;
        this.button = button;
        this.contactType = contactType;
    }

    public AcceptFriendRequest(String userid, List<JButton> tobeDisabledList, int contactType) {
        this.userId = userid;
        this.button = button;
        this.tobeDisabledList = tobeDisabledList;
        this.contactType = contactType;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (text != null) {
                    textLabel.setText("");
                }
                if (button != null) {
                    button.setEnabled(false);
                }
                if (tobeDisabledList != null && !tobeDisabledList.isEmpty()) {
                    for (JButton btn : tobeDisabledList) {
                        btn.setEnabled(false);
                    }
                }
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ACCEPT_FRIEND);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setContactType(contactType);
                js_fields.setUserIdentity(userId);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (tobeDisabledList != null && !tobeDisabledList.isEmpty()) {
                            for (JButton btn : tobeDisabledList) {
                                btn.setEnabled(true);
                            }
                        }
                        break;
                    }
                }

                if (button != null) {
                    button.setEnabled(true);
                }
                if (text != null && textLabel != null) {
                    textLabel.setText(text);
                }

            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

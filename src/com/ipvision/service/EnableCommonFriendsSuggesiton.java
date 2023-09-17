/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.AppConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Wasif Islam
 */
public class EnableCommonFriendsSuggesiton extends Thread {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EnableCommonFriendsSuggesiton.class);
    private int on = 1;

    public EnableCommonFriendsSuggesiton() {
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields jsonMaker = new JsonFields();
                jsonMaker.setAction(AppConstants.TYPE_UPDATE_TOGGLE_SETTINGS);
                jsonMaker.setSn(StatusConstants.COMMON_FRIENDS_SUGGESTION);
                jsonMaker.setSv(on);
                jsonMaker.setPacketId(pakId);
                jsonMaker.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                SendToServer.sendPacketAsString(jsonMaker, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(150);

                for (int i = 1; i <= 5; i++) {
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(jsonMaker, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields != null && fields.getSuccess()) {
                            MyFnFSettings.userProfile.setCommonFriendsSuggestion(on);
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                log.error("Exception in EnableCommonFriendsSuggesiton ==>" + e.getMessage());
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

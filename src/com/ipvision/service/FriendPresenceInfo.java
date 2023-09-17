/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class FriendPresenceInfo extends Thread {

    private final String friendIdentity;

    public FriendPresenceInfo(String friendIdentity) {
        this.friendIdentity = friendIdentity;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {

            try {
                JsonFields defautlRequest = new JsonFields();
                defautlRequest.setAction(AppConstants.TYPE_FRIEND_PRESENCE_INFO);
                String pakId = SendToServer.getRanDomPacketID();
                defautlRequest.setPacketId(pakId);
                defautlRequest.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                defautlRequest.setFriendIdentity(friendIdentity);

                SendToServer.sendPacketAsString(defautlRequest, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 4; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(defautlRequest, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }

                }

            } catch (Exception e) {
            }
        }
    }
}

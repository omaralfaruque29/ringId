/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class MigrationFriendRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MigrationFriendRequest.class);

    public MigrationFriendRequest() {
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            if (MyFnFSettings.userProfile.getCommonFriendsSuggestion() > 0 || MyFnFSettings.userProfile.getContactListSuggestion() > 0) {
                try {
                    JsonFields packet = new JsonFields();
                    String pakId = SendToServer.getRanDomPacketID();
                    packet.setPacketId(pakId);
                    packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    packet.setAction(AppConstants.TYPE_FRIEND_MIGRATION);

                    SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                    Thread.sleep(25);

                    for (int i = 0; i <= 10; i++) {
                        Thread.sleep(1000);
                        if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                            SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                        } else {
                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                            return;
                        }
                    }

                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("Error in MigrationFriendRequest ==>" + e.getMessage());
                }
            }
        }
    }
}

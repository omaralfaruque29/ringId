/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class ChangePresence extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangePresence.class);
    private int presence = 0;

    public ChangePresence(int presence) {
        this.presence = presence;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields packet = new JsonFields();
                packet.setAction(AppConstants.TYPE_ONILE_OFFLINE_STATUS);
                packet.setPacketId(pakId);
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                packet.setLastStatus(presence);
                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            MyFnFSettings.USER_PREV_STATUS = MyFnFSettings.userProfile.getPresence();
                            
                            if (fields.getLastStatus() != null) {
                                MyFnFSettings.userProfile.setPresence(fields.getLastStatus());
                            }
                            
                            GuiRingID.getInstance().getTopMenuBar().changeMenuBarStatus();
                            GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
                            
                            if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_ONLINE) {
                                RecentChatCallActivityDAO.fetchAndResendPendingChat();
                                ChatService.sendOfflineChatRequest();
                            }
                        } else {
                            HelperMethods.showPlaneDialogMessage(fields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in ChangePresence ==>" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

}

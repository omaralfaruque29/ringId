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
public class NotificationRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NotificationRequest.class);
    private long max_ut = 0;
    private short scl = 0;

    public NotificationRequest(long max_ut, short scl) {
        this.max_ut = max_ut;
        this.scl = scl;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                log.warn("Sending Notification Request ...");
                JsonFields notificationRequest = new JsonFields();
                notificationRequest.setAction(AppConstants.TYPE_MY_NOTIFICATIONS);
                String pakId = SendToServer.getRanDomPacketID();
                notificationRequest.setPacketId(pakId);
                notificationRequest.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                if (max_ut > 0) {
                    notificationRequest.setScl(scl);
                    notificationRequest.setUt(max_ut);
                }
                SendToServer.sendPacketAsString(notificationRequest, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(notificationRequest, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }

            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in NotificationRequest ==>" + e.getMessage());
            }
        }
    }
}

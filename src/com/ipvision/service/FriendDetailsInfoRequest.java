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
 * @author Sirat Samyoun
 */
public class FriendDetailsInfoRequest extends Thread {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FriendDetailsInfoRequest.class);

    private String uId;

    public FriendDetailsInfoRequest(String uId) {
        this.uId = uId;
         setName(this.getClass().getSimpleName());
    }
    /*  "sId": "14230470926772000001015",
     "pckId": "8IZcHu9Z2000001015",
     "actn": 95,
     "utId": 23*/

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_USER_DETAILS);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setUserIdentity(uId);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);

                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
            } catch (Exception ex) {
              //  ex.printStackTrace();
                log.error("failed to list FriendCityMarriageAbout" + ex.getMessage());

            }
        }
    }
}

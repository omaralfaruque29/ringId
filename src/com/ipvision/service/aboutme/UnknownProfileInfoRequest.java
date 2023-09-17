/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.aboutme;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class UnknownProfileInfoRequest extends Thread {

    private UserBasicInfo basicInfo;
    private String uId;

    public UnknownProfileInfoRequest(UserBasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public UnknownProfileInfoRequest(String uId) {
        this.uId = uId;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_UNKNWON_PROFILE_INFO);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                if (basicInfo != null) {
                    js_fields.setUserTabelId(basicInfo.getUserTableId());
                } else {
                    js_fields.setUserIdentity(uId);
                }

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

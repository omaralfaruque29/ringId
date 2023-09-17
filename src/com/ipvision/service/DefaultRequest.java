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
import com.ipvision.constants.SettingsConstants;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.service.auth.AuthMsgParser;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class DefaultRequest extends Thread {

    //private boolean isFirstTime = false;
    private int requestType;

    public DefaultRequest(int requestType) {
        this.requestType = requestType;
    }

    /*public DefaultRequest(int requestType, boolean isFirstTime) {
     this.requestType = requestType;
     this.isFirstTime = isFirstTime;
     }*/
    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {

                if (DBConstants.conn != null) {
                    JsonFields defautlRequest = new JsonFields();
                    defautlRequest.setAction(this.requestType);
                    String pakId = SendToServer.getRanDomPacketID();
                    //     String pakId = System.currentTimeMillis() + MyFnFSettings.LOGIN_USER_ID + requestType;
                    defautlRequest.setPacketId(pakId);
                    defautlRequest.setSessionId(MyFnFSettings.LOGIN_SESSIONID);

                    if (requestType == AppConstants.TYPE_CONTACT_LIST) {
                        AuthMsgParser.contactListSeqCount = 0;
                        defautlRequest.setUt(SettingsConstants.FNF_USERINFO_UT);
                        defautlRequest.setTut(SettingsConstants.FNF_GROUP_UT);
                        defautlRequest.setCut(SettingsConstants.FNF_CONTACT_UT);
                    } else if (requestType == AppConstants.TYPE_CIRCLE_LIST) {
                        defautlRequest.setUt(SettingsConstants.FNF_CIRCLE_UT);
                        defautlRequest.setGpmut(SettingsConstants.FNF_CIRCLE_MEMBER_UT);
                    } else if (requestType == AppConstants.TYPE_CALL_LOG) {
                        defautlRequest.setUt(SettingsConstants.FNF_CALL_LOG_UT);
                    }

                    SendToServer.sendPacketAsString(defautlRequest, ServerAndPortSettings.REQUEST_PORT);
                    Thread.sleep(25);

                    for (int i = 1; i <= 20; i++) {
                        Thread.sleep(400 + (i * 100));
                        if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                            SendToServer.sendPacketAsString(defautlRequest, ServerAndPortSettings.REQUEST_PORT);
                        } else {
                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                            /*if(this.requestType == AppConstants.TYPE_YOU_MAY_KNOW_LIST && isFirstTime) {
                             GuiRingID.getInstance().getMainFriendListContainer().getFriendListPanel().startPeopleYouMayKnow();
                             }*/
                            return;
                        }

                    }
                }

            } catch (Exception e) {
            }
        }

    }
}

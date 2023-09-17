/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class ChangeSuggestionSettings extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeSuggestionSettings.class);
    private int sn, sv;

    public ChangeSuggestionSettings(int sn, int sv) {
        this.sn = sn;
        this.sv = sv;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields jsonMaker = new JsonFields();
                jsonMaker.setAction(AppConstants.TYPE_UPDATE_TOGGLE_SETTINGS);
                jsonMaker.setSn(sn);
                jsonMaker.setSv(sv);
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
                            if (sn == StatusConstants.COMMON_FRIENDS_SUGGESTION) {
                                MyFnFSettings.userProfile.setCommonFriendsSuggestion(sv);
                            } else if (sn == StatusConstants.CONTACT_LIST_SUGGESTION) {
                                MyFnFSettings.userProfile.setContactListSuggestion(sv);
                            }
                        } else if (fields.getSuccess()!=null && !fields.getSuccess()) {
                            if (sn == StatusConstants.COMMON_FRIENDS_SUGGESTION) {
                                if (sv == 1) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSettingsPanel().commonFriendsCheckBox.setSelected(false);
                                } else if (sv == 0) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSettingsPanel().commonFriendsCheckBox.setSelected(true);
                                }
                            } else if (sn == StatusConstants.CONTACT_LIST_SUGGESTION) {
                                if (sv == 1) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSettingsPanel().contactListCheckBox.setSelected(false);
                                } else if (sv == 0) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSettingsPanel().contactListCheckBox.setSelected(true);
                                }
                            }

                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                    Thread.sleep(600);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                log.error("Exception in Changing SuggestionSettings ==>" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

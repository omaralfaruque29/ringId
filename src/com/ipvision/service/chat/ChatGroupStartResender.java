/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import java.util.Objects;
import com.ipvision.model.dao.DeleteFromGroupListTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.service.utility.OnGroupResponseListener;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ChatGroupStartResender extends Thread {
    /*
     "uId":"anwar",
     "actn":188,
     "sId":"1397628903092anwar",
     "pckId":"r87cPvQhanwar",
     "grpId":669
     */

    private boolean showWarning = true;
    private Long groupId;
    private OnGroupResponseListener listener;

    public ChatGroupStartResender(Long groupId, boolean showWarning) {
        this.groupId = groupId;
        this.showWarning = showWarning;
        setName(this.getClass().getSimpleName());
    }

    public ChatGroupStartResender(Long groupId, boolean showWarning, OnGroupResponseListener listener) {
        this.groupId = groupId;
        this.showWarning = showWarning;
        this.listener = listener;
        setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                pakToSend.setAction(AppConstants.TYPE_START_GROUP_CHAT);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setPacketId(pakId);
                pakToSend.setGroupId(groupId);
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.CHAT_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(1500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.CHAT_PORT);
                    } else {

                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (!fields.getSuccess()) {

                            GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                            if (groupInfo == null || !Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())) {
                                if (showWarning) {
                                    HelperMethods.showWarningDialogMessage("You are not a member of this group!");
                                }
                                new DeleteFromGroupListTable(groupId).start();
                                MyfnfHashMaps.getInstance().getGroup_hash_map().remove(groupId);
                                ChatService.unregisterGroupChat(groupId, ChatConstants.PRESENCE_ONLINE);
                                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                                if (groupPanel != null
                                        && groupPanel.isDisplayable()
                                        && GuiRingID.getInstance() != null
                                        && GuiRingID.getInstance().getMainRight() != null) {
                                    GuiRingID.getInstance().getMainRight().showGroupViewPanel();
                                }
                                MyfnfHashMaps.getInstance().getGroupPanelMap().remove(groupId);

                                if (GuiRingID.getInstance() != null
                                        && GuiRingID.getInstance().getMainLeftContainer() != null
                                        && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
                                    GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
                                }
                                if (GuiRingID.getInstance() != null
                                        && GuiRingID.getInstance().getMainButtonsPanel() != null) {
                                    GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");
                                }

                            }

                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

                if (listener != null) {
                    listener.onResponse(groupId);
                }
            } catch (Exception e) {
            }
        }
    }
}

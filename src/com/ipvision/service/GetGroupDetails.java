/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.dao.DeleteFromGroupListTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.service.utility.OnGroupResponseListener;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class GetGroupDetails extends Thread {
//

    private boolean isOfflineRequest = false;
    private Long groupId;
    private OnGroupResponseListener listener;

    public GetGroupDetails(Long groupId) {
        this.groupId = groupId;
        setName(this.getClass().getSimpleName());

    }

    public GetGroupDetails(Long groupId, boolean isOfflineRequest) {
        this.groupId = groupId;
        this.isOfflineRequest = isOfflineRequest;
        setName(this.getClass().getSimpleName());
    }

    public GetGroupDetails(Long groupId, boolean isOfflineRequest, OnGroupResponseListener listener) {
        this.groupId = groupId;
        this.isOfflineRequest = isOfflineRequest;
        this.listener = listener;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                //Sending data for action 241-->{"pckId":"14213238393882000000006","actn":241,"tid":3,"sId":"14213236831272000000006"}
                JsonFields singInpacked = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                singInpacked.setAction(AppConstants.TYPE_GROUP_DETAILS);
                singInpacked.setGroupId(groupId);
                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (!fields.getSuccess() && isOfflineRequest) {
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
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

                if (listener != null) {
                    listener.onResponse(groupId);
                }

            } catch (Exception ex) {
            }
        }
    }
}

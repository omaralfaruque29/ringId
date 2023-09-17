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
import com.ipvision.model.dao.DeleteFromGroupListTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class DeleteGroup extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteGroup.class);
    private Long groupId;

    public DeleteGroup(long groupId) {
        this.groupId = groupId;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields packet = new JsonFields();
                packet.setAction(AppConstants.TYPE_REMOVE_GROUP);
                packet.setPacketId(pakId);
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                packet.setGroupId(groupId);
                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            deleteInfo();
                        } else {
                            if (!check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
                                deleteInfo();
                            } else {
                                HelperMethods.showPlaneDialogMessage(fields.getMessage());
                            }
                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Exception in DeletingGroup ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

    public boolean check_already_in_group(String user_id) {
        GroupInfoDTO groupInfoDTO = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
        if (groupInfoDTO != null && groupInfoDTO.getMemberMap() != null && groupInfoDTO.getMemberMap().get(user_id) != null) {
            return true;
        }
        return false;
    }

    private void deleteInfo() {
        new DeleteFromGroupListTable(groupId).start();
//      new AddRemoveGroupChatMember(groupId, userTableId, false).start();
        ChatService.sendGroupDelete(groupId);

        MyfnfHashMaps.getInstance().getGroup_hash_map().remove(groupId);
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainRight() != null) {
            GuiRingID.getInstance().getMainRight().showGroupViewPanel();
        }
        MyfnfHashMaps.getInstance().getGroupPanelMap().remove(groupId);

        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
        }
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtonsPanel() != null) {
            GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");
        }
    }
}

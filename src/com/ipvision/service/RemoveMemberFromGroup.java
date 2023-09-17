/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.ActivityConstants;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.dao.DeleteFromGroupListTable;
import com.ipvision.model.dao.DeleteFromGroupMemberListTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author shahadat
 */
public class RemoveMemberFromGroup extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RemoveMemberFromGroup.class);
    private Long groupId;
    private String userIdentity;
    private GroupPanel.ContactPanel contactPanel;

    public RemoveMemberFromGroup(Long groupId, String userIdentity, GroupPanel.ContactPanel contactPanel) {
        this.groupId = groupId;
        this.userIdentity = userIdentity;
        this.contactPanel = contactPanel;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);

                JsonFields packet = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                packet.setPacketId(pakId);
                packet.setGroupId(groupId);
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                if (groupInfo != null
                        && Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())
                        && userIdentity.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    packet.setFriendIdentity(null);
                } else {
                    packet.setFriendIdentity(userIdentity);
                }
                packet.setAction(AppConstants.TYPE_REMOVE_GROUP_CHAT_MEMBER);

                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.CHAT_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.CHAT_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            if (userIdentity.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {

                                if (groupInfo != null
                                        && Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())
                                        && groupInfo.getMemberMap() != null
                                        && groupInfo.getMemberMap().size() > 1) {

                                    new DeleteFromGroupMemberListTable(groupId, userIdentity).start();
                                    ChatService.sendGroupMemberRemove(groupId, userIdentity);

                                    groupInfo.getMemberMap().remove(userIdentity);
                                    ChatService.unregisterGroupChat(groupId, ChatConstants.PRESENCE_ONLINE);

                                    GroupPanel gPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                                    if (gPanel != null) {
                                        gPanel.setGroupMemberCount();
                                        gPanel.buildPopUpMenu();
                                        gPanel.buildChatWritingArea();
                                    }

                                    if (GuiRingID.getInstance() != null
                                            && GuiRingID.getInstance().getMainRight() != null
                                            && GuiRingID.getInstance().getMainRight().getGroupMainView() != null) {
                                        GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                                    }

                                    List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                    ActivityDTO activityDTO = new ActivityDTO();
                                    activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                                    activityDTO.setMessageType(ActivityConstants.MSG_GROUP_LEAVE);
                                    activityDTO.setGroupId(groupId);
                                    activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                    activityDTO.setPacketID(pakId);
                                    activityDTOs.add(activityDTO);
                                    new InsertIntoRingActivityTable(activityDTOs).start();

                                } else {
                                    new DeleteFromGroupListTable(groupId).start();
                                    ChatService.sendGroupMemberRemove(groupId, userIdentity);
                                    MyfnfHashMaps.getInstance().getGroup_hash_map().remove(groupId);
                                    MyfnfHashMaps.getInstance().getGroupPanelMap().remove(groupId);
                                    GuiRingID.getInstance().getMainRight().showGroupViewPanel();
                                    ChatService.unregisterGroupChat(groupId, ChatConstants.PRESENCE_ONLINE);

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

                            } else {

                                new DeleteFromGroupMemberListTable(groupId, userIdentity).start();
                                ChatService.sendGroupMemberRemove(groupId, userIdentity);

                                if (groupInfo != null && groupInfo.getMemberMap() != null) {
                                    groupInfo.getMemberMap().remove(userIdentity);
                                    if (groupInfo.getMemberMap().size() == 1 || !groupInfo.getMemberMap().containsKey(MyFnFSettings.LOGIN_USER_ID)) {
                                        ChatService.unregisterGroupChat(groupId, ChatConstants.PRESENCE_ONLINE);
                                    }
                                }

                                GroupPanel gPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                                if (gPanel != null) {
                                    gPanel.setGroupMemberCount();
                                }

                                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                                activityDTO.setMessageType(ActivityConstants.MSG_GROUP_REMOVE_MEMBER);
                                activityDTO.setGroupId(groupId);
                                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                activityDTO.setPacketID(pakId);
                                activityDTO.setMembers(new ConcurrentHashMap<String, String>());
                                activityDTO.getMembers().put(userIdentity, contactPanel != null ? contactPanel.member.getFullName() : userIdentity);
                                activityDTOs.add(activityDTO);
                                new InsertIntoRingActivityTable(activityDTOs).start();
                            }
                        } else {
                            if (userIdentity.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                HelperMethods.showWarningDialogMessage(fields.getMessage());
                            } else {
                                HelperMethods.showWarningDialogMessage(fields.getMessage());
                                GroupPanel gPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                                if (gPanel != null && contactPanel != null) {
                                    gPanel.membersPanel.add(contactPanel);
                                    gPanel.membersPanel.revalidate();
                                    gPanel.membersPanel.repaint();
                                }
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in RemoveMemberFromGroup ==>" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

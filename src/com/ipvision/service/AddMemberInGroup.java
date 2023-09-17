/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.service.ChatService;
import com.ipv.chat.storers.ChatStorer;
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
import com.ipvision.model.dao.InsertIntoGroupMemberTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class AddMemberInGroup extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddMemberInGroup.class);
    private GroupPanel.ContactPanel contactPanel;
    private GroupMemberDTO member;
    private String groupName;

    public AddMemberInGroup(String groupName, GroupMemberDTO member, GroupPanel.ContactPanel contactPanel) {
        this.member = member;
        this.groupName = groupName;
        this.contactPanel = contactPanel;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(member.getGroupId());

                JsonFields packet = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                packet.setPacketId(pakId);
                packet.setGroupId(member.getGroupId());
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                if (groupInfo != null
                        && Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())
                        && member.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    packet.setFriendIdentity(null);
                } else {
                    packet.setFriendIdentity(member.getUserIdentity());
                }
                packet.setAction(AppConstants.TYPE_ADD_GROUP_CHAT_MEMBER);
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(member.getGroupId() + "");
                if (serverLocation != null) {
                    packet.setChatServerIp(serverLocation.getChatServerIP());
                    packet.setChatRegistrationPort(serverLocation.getChatRegisterPort());
                }

                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.CHAT_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.CHAT_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {

                            if (groupInfo != null && groupInfo.getMemberMap() != null) {
                                groupInfo.getMemberMap().put(member.getUserIdentity(), member);
                            } else if (groupInfo != null) {
                                groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                                groupInfo.getMemberMap().put(member.getUserIdentity(), member);
                            }

                            GroupPanel gPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(member.getGroupId());
                            if (gPanel != null) {
                                if (member.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                    gPanel.buildPopUpMenu();
                                    gPanel.buildChatWritingArea();
                                } else {
                                    gPanel.setGroupMemberCount();
                                }
                            }

                            if (GuiRingID.getInstance() != null
                                    && GuiRingID.getInstance().getMainRight() != null
                                    && GuiRingID.getInstance().getMainRight().getGroupMainView() != null) {
                                GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                            }

                            List<GroupMemberDTO> members = new ArrayList<GroupMemberDTO>();
                            members.add(member);
                            new InsertIntoGroupMemberTable(members).start();

                            if (member.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                ChatHelpers.startGroupChat(member.getGroupId(), true);

                                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setMessageType(ActivityConstants.MSG_GROUP_JOIN);
                                activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                                activityDTO.setGroupId(member.getGroupId());
                                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                activityDTO.setPacketID(pakId);
                                activityDTOs.add(activityDTO);
                                new InsertIntoRingActivityTable(activityDTOs).start();
                            } else {
                                List<String> list = new ArrayList<String>();
                                list.add(member.getUserIdentity());
                                ChatService.sendGroupMemberList(member.getGroupId(), list, null);
                            }

                        } else {
                            GroupPanel gPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(member.getGroupId());
                            if (gPanel != null && contactPanel != null) {
                                gPanel.membersPanel.add(contactPanel);
                                gPanel.membersPanel.revalidate();
                                gPanel.membersPanel.repaint();
                            }
                        }
                        break;
                    }
                }
                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);

            } catch (Exception e) {
              //  e.printStackTrace();
             log.error("Error in AddMemberInGroup class ==>" + e.getMessage());   
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

}

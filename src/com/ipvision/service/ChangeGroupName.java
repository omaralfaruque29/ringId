/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.ActivityConstants;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.dao.InsertIntoGroupListTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class ChangeGroupName extends Thread {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeGroupName.class);
    private Long groupId;
    private String newGroupName;
    private String prevGroupName;

    public ChangeGroupName(Long groupId, String newGroupName) {
        this.groupId = groupId;
        this.newGroupName = newGroupName;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainRight() != null
                        && GuiRingID.getInstance().getMainRight().getGroupPanel() != null) {
                    GroupPanel groupPanel = GuiRingID.getInstance().getMainRight().getGroupPanel();
                    groupPanel.groupNameTextField.setEditable(false);

                    String pakId = SendToServer.getRanDomPacketID();
                    JsonFields packet = new JsonFields();
                    packet.setAction(AppConstants.TYPE_UPDATE_TAG_NAME);
                    packet.setPacketId(pakId);
                    packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    packet.setGroupId(groupId);
                    packet.setNewGroupName(newGroupName);
                    SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                    Thread.sleep(25);

                    for (int i = 1; i <= 5; i++) {
                        Thread.sleep(500);
                        if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                            SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                        } else {
                            FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                            if (fields.getSuccess()) {

                                List<GroupInfoDTO> groupList = new ArrayList<GroupInfoDTO>();
                                GroupInfoDTO dto = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                                prevGroupName = dto.getGroupName();
                                dto.setGroupName(newGroupName);
                                groupList.add(dto);
                                new InsertIntoGroupListTable(groupList).start();

                                if (GuiRingID.getInstance() != null
                                        && GuiRingID.getInstance().getMainRight() != null
                                        && GuiRingID.getInstance().getMainRight().getGroupMainView() != null) {
                                    GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                                }

                                groupPanel.groupNameTextField.setEditable(true);
                                groupPanel.groupName = newGroupName;
                                groupPanel.groupNameLabel.setText(newGroupName);
                                groupPanel.groupNameTextField.setVisible(false);
                                groupPanel.groupNameLabel.setVisible(true);
                                groupPanel.actionPopUpButton.setVisible(true);
                                groupPanel.buttonsPanel.setVisible(false);

                                ChatHistoryContainer.changeFullName(groupId + "");

                                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                                activityDTO.setMessageType(ActivityConstants.MSG_GROUP_EDIT_NAME);
                                activityDTO.setGroupId(groupId);
                                activityDTO.setPrevGroupName(prevGroupName);
                                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                activityDTO.setPacketID(pakId);
                                activityDTOs.add(activityDTO);
                                new InsertIntoRingActivityTable(activityDTOs).start();

                            } else {
                                HelperMethods.showPlaneDialogMessage(fields.getMessage());
                            }

                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                            break;
                        }

                    }
                } else {
                    HelperMethods.showPlaneDialogMessage("Can not change Group name");
                }

            } catch (Exception e) {
                //e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.ActivityConstants;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.model.dao.InsertIntoGroupListTable;
import com.ipvision.model.dao.InsertIntoGroupMemberTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Shahadat Hossain
 */
public class CreateGroupRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateGroupRequest.class);
    private JLabel loadingLable;
    private JButton sendbutton;
    private JButton resetbutton;
    private String groupName;
    private ArrayList<Long> frdids;

    public CreateGroupRequest(JLabel loadingLable, JButton sendbutton, JButton resetbutton, String groupName) {
        this.loadingLable = loadingLable;
        this.sendbutton = sendbutton;
        this.resetbutton = resetbutton;
        this.groupName = groupName;
        this.frdids = new ArrayList<>();
    }

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null & GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
//        return GuiRingID.getInstance().getMainRight() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    private void setMessage(int a) {
        String string;
        switch (a) {
            case 1:
                string = ".";
                break;
            case 2:
                string = "..";
                break;
            case 3:
                string = "...";
                break;
            case 4:
                string = "....";
                break;
            case 5:
                string = ".....";
                break;
            default:
                string = "";

        }
        this.loadingLable.setText("Please wait" + string);

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                this.sendbutton.setEnabled(false);
                this.resetbutton.setEnabled(false);
                this.loadingLable.setForeground(DefaultSettings.blue_bar_background);

                for (GroupMemberDTO member : MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().values()) {
                    this.frdids.add(member.getUserTableId());
                }

                JsonFields packet = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                packet.setPacketId(pakId);
                packet.setGroupName(groupName);
                packet.setFriendIdList(frdids);
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                packet.setAction(AppConstants.TYPE_UPDATE_FRIEND_GROUP);
                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    setMessage(i);
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (js.getSuccess()) {

                            List<GroupInfoDTO> groupList = new ArrayList<GroupInfoDTO>();
                            List<GroupMemberDTO> memberList = new ArrayList<GroupMemberDTO>();

                            GroupInfoDTO groupInfo = new GroupInfoDTO();
                            groupInfo.setOwnerUserTableId(MyFnFSettings.userProfile.getUserTableId());
                            groupInfo.setGroupId(js.getGroupId());
                            groupInfo.setGroupName(groupName);
                            groupInfo.setGroupUt(js.getTut());
                            groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                            groupList.add(groupInfo);
                            MyfnfHashMaps.getInstance().getGroup_hash_map().put(js.getGroupId(), groupInfo);

                            for (GroupMemberDTO member : MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().values()) {
                                groupInfo.getMemberMap().put(member.getUserIdentity(), member);
                                member.setGroupId(js.getGroupId());
                                memberList.add(member);
                            }

                            GroupMemberDTO member = new GroupMemberDTO();
                            member.setGroupId(js.getGroupId());
                            member.setFullName(MyFnFSettings.userProfile.getFullName());
                            member.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                            memberList.add(member);
                            groupInfo.getMemberMap().put(member.getUserIdentity(), member);

                            new InsertIntoGroupListTable(groupList).start();
                            new InsertIntoGroupMemberTable(memberList).start();

                            this.loadingLable.setText(js.getMessage());
                            if (getMainRight().getGroupMainView() != null) {
                                getMainRight().getGroupMainView().buildGroupList();
                                getMainRight().showGroupPanel(js.getGroupId());
                            }

                            List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                            ActivityDTO activityDTO = new ActivityDTO();
                            activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                            activityDTO.setMessageType(ActivityConstants.MSG_GROUP_CREATE);
                            activityDTO.setGroupId(js.getGroupId());
                            activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                            activityDTO.setPacketID(pakId);
                            activityDTOs.add(activityDTO);
                            Thread.sleep(300);
                            new InsertIntoRingActivityTable(activityDTOs).start();

                            this.resetbutton.setEnabled(true);
                            this.resetbutton.doClick();
                        } else {
                            this.loadingLable.setForeground(Color.RED);
                            this.loadingLable.setText(js.getMessage());
                            this.loadingLable.setText("Server Response Failed! " + NotificationMessages.CAN_NOT_CREATE_GROUP+"Click Reset Button and Create Again");
                        }
                        MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().clear();

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }
                this.loadingLable.setForeground(Color.RED);
               
                this.sendbutton.setEnabled(true);
                this.resetbutton.setEnabled(true);
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in CreatingGroupRequest ==>" + ex.getMessage());
                this.loadingLable.setForeground(Color.RED);
                this.loadingLable.setText("Failed! " + NotificationMessages.CAN_NOT_CREATE_GROUP);
                this.sendbutton.setEnabled(true);
                this.resetbutton.setEnabled(true);
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
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
import javax.swing.JButton;
import com.ipvision.model.dao.InsertIntoContactListTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class AcceptFriendAccess extends Thread {
//

    private String userIdentity;
    private long userTableId;
    private int contactType;
    private int prevContactType;
    private boolean isAccepted;
    private JButton button, btn1, btn2;

    public AcceptFriendAccess(String userIdentity, long userTableId, int contactType, boolean isAccepted, JButton button) {
        this.userIdentity = userIdentity;
        this.userTableId = userTableId;
        this.contactType = contactType;
        this.isAccepted = isAccepted;
        this.button = button;
    }

    public AcceptFriendAccess(String userIdentity, long userTableId, int contactType, boolean isAccepted, JButton btn1, JButton btn2) {
        this.userIdentity = userIdentity;
        this.userTableId = userTableId;
        this.contactType = contactType;
        this.isAccepted = isAccepted;
        this.btn1 = btn1;
        this.btn2 = btn2;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (button != null) {
                    button.setEnabled(false);
                }
                if (btn1 != null) {
                    btn1.setEnabled(false);
                }
                if (btn2 != null) {
                    btn2.setEnabled(false);
                }
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_ACCEPT_FRIEND_ACCESS);
                pakToSend.setContactType(contactType);
                pakToSend.setUserTabelId(userTableId);
                pakToSend.setIsAccepted(isAccepted);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(userIdentity);
                            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
                            UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);

                            if (basicInfo != null) {
                                prevContactType = basicInfo.getContactType();
                                if (isAccepted) {
                                    basicInfo.setContactType(contactType);
                                }
                                basicInfo.setNewContactType(0);
                                basicInfo.setIsChangeRequester(Boolean.FALSE);

                                List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                                list.add(basicInfo);
                                new InsertIntoContactListTable(list).start();

                                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setActivityType(ActivityConstants.ACTIVITY_ACCESS_REQUEST);
                                activityDTO.setMessageType(isAccepted ? ActivityConstants.MSG_ACCEPTED_ACCESS_CHANGE_REQUEST : ActivityConstants.MSG_REJECTED_ACCESS_CHANGE_REQUEST);
                                activityDTO.setFriendIdentity(basicInfo.getUserIdentity());
                                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                activityDTO.setFromContactType(prevContactType);
                                activityDTO.setToContactType(contactType);
                                activityDTO.setPacketID(pakId);
                                activityDTOs.add(activityDTO);
                                new InsertIntoRingActivityTable(activityDTOs).start();
                            }

                            if (friendProfile != null) {
                                friendProfile.setFriendProfileInfo(userIdentity);
                                friendProfile.refreshMyFriendPanel();
                            }

                            if (friendChatCallPanel != null) {
                                friendChatCallPanel.setFriendProfileInfo(userIdentity);
                                friendChatCallPanel.refreshMyFriendChatCallPanelAll();
                            }
                            if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                                if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().change_access(userIdentity);
                                }
                                if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                        && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().change_access(userIdentity);
                                }
                            }
                            AllFriendList.changeAccessControl(userIdentity);
                            //  AllFriendList.changeSelectedFriend(userIdentity);
                            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                            /*GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().peopleYouMayKnowWrapperPanel.revalidate();
                             GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().inviteListWrapperPanel.revalidate();*/
                            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().friendListWrapperPanel.revalidate();
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (btn1 != null) {
                            btn1.setEnabled(true);
                        }
                        if (btn2 != null) {
                            btn2.setEnabled(true);
                        }
                        break;
                    }

                }
            } catch (Exception ex) {

            } finally {
                if (button != null) {
                    button.setEnabled(true);
                }
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

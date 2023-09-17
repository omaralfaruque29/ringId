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
import com.ipvision.model.constants.StatusConstants;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.dao.InsertIntoContactListTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import javax.swing.JButton;

/**
 *
 * @author Faiz Ahmed
 */
public class ChangeFriendAccess extends Thread {
//

    private String userIdentity;
    private long userTableId;
    private int contactType;
    private int prevContactType;
    private JButton btn1, btn2, btn3;

    public ChangeFriendAccess(String userIdentity, long userTableId, int contactType) {
        this.userIdentity = userIdentity;
        this.userTableId = userTableId;
        this.contactType = contactType;
    }

    public ChangeFriendAccess(String userIdentity, JButton btn1, JButton btn2, JButton btn3, long userTableId, int contactType) {
        this.userIdentity = userIdentity;
        this.userTableId = userTableId;
        this.contactType = contactType;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (btn1 != null) {
                    btn1.setEnabled(false);
                }
                if (btn2 != null) {
                    btn2.setEnabled(false);
                }
                if (btn3 != null) {
                    btn3.setEnabled(false);
                }
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_CHANGE_FRIEND_ACCESS);
                pakToSend.setContactType(contactType);
                pakToSend.setUserTabelId(userTableId);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
                            if (basicInfo != null) {
                                prevContactType = basicInfo.getContactType();
                                boolean isDownGradeChange = contactType == StatusConstants.ACCESS_CHAT_CALL && basicInfo.getContactType() == StatusConstants.ACCESS_FULL;
                                if (isDownGradeChange) {
                                    basicInfo.setContactType(contactType);
                                    basicInfo.setNewContactType(0);
                                    basicInfo.setIsChangeRequester(Boolean.FALSE);
                                } else {
                                    basicInfo.setNewContactType(contactType);
                                    basicInfo.setIsChangeRequester(Boolean.TRUE);
                                    /*GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                                     if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                                     if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                                     GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().change_access(userIdentity);
                                     }
                                     if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                     && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                                     GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().change_access(userIdentity);
                                     }
                                     }*/
                                }

                                List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                                list.add(basicInfo);
                                new InsertIntoContactListTable(list).start();

                                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setActivityType(ActivityConstants.ACTIVITY_ACCESS_REQUEST);
                                activityDTO.setMessageType(isDownGradeChange ? ActivityConstants.MSG_DOWNGRADE_ACCESS_CHANGE : ActivityConstants.MSG_PENDING_ACCESS_CHANGE_REQUEST);
                                activityDTO.setFriendIdentity(basicInfo.getUserIdentity());
                                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                                activityDTO.setFromContactType(prevContactType);
                                activityDTO.setToContactType(contactType);
                                activityDTO.setPacketID(pakId);
                                activityDTOs.add(activityDTO);
                                new InsertIntoRingActivityTable(activityDTOs).start();
                            }

                            MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(userIdentity);
                            if (friendProfile != null) {
                                friendProfile.setFriendProfileInfo(userIdentity);
                                friendProfile.refreshMyFriendPanel();
                            }

                            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
                            if (friendChatCallPanel != null) {
                                friendChatCallPanel.setFriendProfileInfo(userIdentity);
                                friendChatCallPanel.refreshMyFriendChatCallPanelAccess();
                            }
                            /*if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                             && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                             GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().add_contents();
                             }
                             if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                             GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().add_contents();
                             }*/
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
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (btn1 != null) {
                            btn1.setEnabled(true);
                        }
                        if (btn2 != null) {
                            btn2.setEnabled(true);
                        }
                        if (btn3 != null) {
                            btn3.setEnabled(true);
                        }
                        break;
                    }

                }
            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

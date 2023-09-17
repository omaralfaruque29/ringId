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
import com.ipvision.model.dao.InsertIntoContactListTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ActivityDTO;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class BlockUnblockFriend extends Thread {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BlockUnblockFriend.class);
    private String uid;
    private Long utid;
    private int block;

    public BlockUnblockFriend(String uid, Long utid, int block) {
        this.uid = uid;
        this.utid = utid;
        this.block = block;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_BLOCK_UNBLOCK_FRIEND);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserTabelId(utid);
                js_fields.setBlocked(block);
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (js.getSuccess()) {
                            UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(uid);
                            basicInfo.setBlocked(block);
                            MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(uid);
                            if (friendProfile != null) {
                                friendProfile.setFriendProfileInfo(uid);
                                friendProfile.buildPopUpMenu();
                                friendProfile.buildRightTablPanel();
                            }
                            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(uid);
                            if (friendChatCallPanel != null) {
                                friendChatCallPanel.setFriendProfileInfo(uid);
                                friendChatCallPanel.refreshMyFriendChatCallPanelAccess();
                            }

                            List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
                            contactList.add(basicInfo);
                            new InsertIntoContactListTable(contactList).start();

                            AllFriendList.changeFriendShipStatus(uid);
                            if (GuiRingID.getInstance() != null
                                    && GuiRingID.getInstance().getMainLeftContainer() != null
                                    && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null
                                    && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel() != null) {
                                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().showHideFriendList();
                            }
                            /* if (AcceptedFriendList.getInstance().friendSearchPanel != null) {
                             AcceptedFriendList.getInstance().friendSearchPanel.showHideFriendList();
                             }*/

                            //  AcceptedFriendList.getInstance().showHideFriendList();
                            List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                            ActivityDTO activityDTO = new ActivityDTO();
                            activityDTO.setActivityType(ActivityConstants.ACTIVITY_BLOCK_FRIEND);
                            activityDTO.setMessageType(block == 1 ? ActivityConstants.MSG_BLOCK_FRIEND : ActivityConstants.MSG_UNBLOCK_FRIEND);
                            activityDTO.setFriendIdentity(uid);
                            activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                            activityDTO.setPacketID(pakId);
                            activityDTOs.add(activityDTO);
                            new InsertIntoRingActivityTable(activityDTOs).start();
                        } else {
                            HelperMethods.showConfirmationDialogMessage("Can not block/ unblock the user right now");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

            } catch (Exception ex) {
                //ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

}

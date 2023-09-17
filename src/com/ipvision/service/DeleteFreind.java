/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.dao.DeleteFromContactListTable;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author Faiz Ahmed
 */
public class DeleteFreind extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteFreind.class);
    private String userid;
    private List<JButton> tobeDisabledList;

    public DeleteFreind(String userid) {
        this.userid = userid;
    }

    public DeleteFreind(String userid, List<JButton> tobeDisabledList) {
        this.userid = userid;
        this.tobeDisabledList = tobeDisabledList;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (tobeDisabledList != null && !tobeDisabledList.isEmpty()) {
                    for (JButton btn : tobeDisabledList) {
                        btn.setEnabled(false);
                    }
                }
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_DELETE_FRIEND);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(this.userid);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        try {
                            FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                            if (fields.getSuccess()) {
                                try {
                                    UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(fields.getUserIdentity());
                                    FriendList.getInstance().getFriend_hash_map().remove(fields.getUserIdentity());
                                    (new DeleteFromContactListTable(fields.getUserIdentity())).start();

                                    MyfnfHashMaps.getInstance().getSingleFriendPanel().remove(fields.getUserIdentity());
                                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();

                                    if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                                        if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                                            GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(fields.getUserIdentity());
                                        }
                                        if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                                && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                                            GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(fields.getUserIdentity());
                                        }
                                    }
                                    //AcceptedFriendList.getInstance().buildFriendList();

                                    if (MyFriendProfile.getSeleected_user_identiy() != null
                                            && MyFriendProfile.getSeleected_user_identiy().equals(fields.getUserIdentity())
                                            && friendInfo != null) {
                                        GuiRingID.getInstance().showUnknownProfile(friendInfo);
                                    }

                                    MyfnfHashMaps.getInstance().getMyFriendProfiles().remove(fields.getUserIdentity());
                                    MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().remove(fields.getUserIdentity());

                                    // GuiRingID.getInstance().getMainButtonsPanel().addIncomingRequestNotification();
                                } catch (Exception e) {
                                 //   e.printStackTrace();
                                log.error("Error in Deleting Freind ==>" + e.getMessage());
                                }

                            } else {
                                //JOptionPane.showConfirmDialog(null, fields.getMessage(), "Delete friend", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                                HelperMethods.showPlaneDialogMessage(fields.getMessage());
                            }

                        } catch (Exception e) {
                            //e.printStackTrace();
                        log.error("Exception in DeleteFreind class ==>" + e.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (tobeDisabledList != null && !tobeDisabledList.isEmpty()) {
                            for (JButton btn : tobeDisabledList) {
                                btn.setEnabled(true);
                            }
                        }
                        return;
                    }

                }

            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

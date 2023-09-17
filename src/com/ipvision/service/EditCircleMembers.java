/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.CreateCircle;
import com.ipvision.model.NewCircleFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.dao.UpdateCircleMember;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class EditCircleMembers extends Thread {

    //  private JMenuItem adminMemberLabel;
    private final JLabel adminTextLabel;
    private final long circleId;
    private final String friendId;
    private final boolean isAdmin;

    public EditCircleMembers(JLabel adminTextLabel, long circleId, String friendId, boolean isAdmin) {
        // this.adminMemberLabel = adminMemberLabel;
        this.adminTextLabel = adminTextLabel;
        this.circleId = circleId;
        this.friendId = friendId;
        this.isAdmin = isAdmin;
        setName(this.getClass().getSimpleName());
    }

    private MainRightDetailsView getMainRightDetailsView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
        // return GuiRingID.getInstance().getMainRight();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                CreateCircle grpClass = new CreateCircle();
                NewCircleFields grpMapp = new NewCircleFields();
                grpMapp.setUserIdentity(this.friendId);
                grpMapp.setAdmin(this.isAdmin);
                grpClass.getCircleMembers().add(grpMapp);

                String pakId = SendToServer.getRanDomPacketID();
                grpClass.setAction(AppConstants.TYPE_EDIT_CIRCLE_MEMBER);
                grpClass.setPacketId(pakId);
                grpClass.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                grpClass.setCircleId(circleId);
                SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
                            if (this.isAdmin == Boolean.TRUE) {
                                // adminMemberLabel.setText(CircleProfile.MAKE_ADMIN);
                                adminTextLabel.setText(CircleProfile.ONLY_MEMBER);
                            } else {
                                // adminMemberLabel.setText(CircleProfile.REMOVE_FROM_ADMIN);
                                adminTextLabel.setText(CircleProfile.ADMIN);
                            }

                            if (MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId) != null) {
                                MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId).get(this.friendId).setAdmin(this.isAdmin);
                            }
                            new UpdateCircleMember(circleId, this.friendId, this.isAdmin).start();
                            getMainRightDetailsView().getCircleProfile().getCircleMembersListPanel().buildMembersList();

                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
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

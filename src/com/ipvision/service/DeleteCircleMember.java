/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.CreateCircle;
import com.ipvision.model.NewCircleFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.dao.DeleteFromCircleMemberListTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class DeleteCircleMember extends Thread {

    private long circleId;
    private String userIDToDelete;

    public DeleteCircleMember(long circleId, String useridToDelete) {
        this.circleId = circleId;
        this.userIDToDelete = useridToDelete;
    }

    private MainRightDetailsView getMainRightDetailsView() {
        if (GuiRingID.getInstance() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
//        return GuiRingID.getInstance().getMainRight();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {

            try {
                //  System.out.println("running....DeleteCircleMember");
                CreateCircle grpClass = new CreateCircle();
                NewCircleFields grpMapp = new NewCircleFields();
                grpMapp.setUserIdentity(this.userIDToDelete);
                grpClass.getCircleMembers().add(grpMapp);
                String pakId = SendToServer.getRanDomPacketID();
                grpClass.setAction(AppConstants.TYPE_REMOVE_CIRCLE_MEMBER);
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
                            if (MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId) != null) {
                                MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId).remove(userIDToDelete);
                            }

                            new DeleteFromCircleMemberListTable(circleId, userIDToDelete).start();

                            if (getMainRightDetailsView().getCircleViewRight() != null) {
                                getMainRightDetailsView().getCircleViewRight().buildCircleCount();
//                                if (getMainRightDetailsView().getCircleViewRight().textMyCircles.getForeground() == DefaultSettings.ORANGE_BACKGROUND_COLOR) {
//                                    getMainRightDetailsView().getCircleViewRight().buildMyCircle();
//                                } 
                                 if (getMainRightDetailsView().getCircleViewRight().textCircleofMe.getForeground() == DefaultSettings.ORANGE_BACKGROUND_COLOR) {
                                    getMainRightDetailsView().getCircleViewRight().buildCircleOfMe();
                                }
                            }
                            getMainRightDetailsView().getCircleProfile().getCircleMembersListPanel().buildMembersList();
                        } else {
                            HelperMethods.showPlaneDialogMessage(fields.getMessage());
                            // JOptionPane.showMessageDialog(null, fields.getMessage(), "Delete group member", JOptionPane.ERROR_MESSAGE);
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

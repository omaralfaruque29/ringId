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
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.dao.DeleteFromCircleListTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class LeaveCircle extends Thread {

    private long circleId;

    public LeaveCircle(long circleId) {
        this.circleId = circleId;
        setName(this.getClass().getSimpleName());
    }

    private MainRightDetailsView getMainRightDetailsView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {

                JsonFields leaveGroup = new JsonFields();
                leaveGroup.setAction(AppConstants.TYPE_LEAVE_CIRCLE);
                //  fields_fetch_group.setType(GroupConstants.TYPE_leave_group);
                String pakId = SendToServer.getRanDomPacketID();
                leaveGroup.setPacketId(pakId);
                leaveGroup.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                leaveGroup.setCircleId(circleId);
                SendToServer.sendPacketAsString(leaveGroup, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(leaveGroup, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        try {
                            FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                            if (fields.getSuccess()) {
                                (new DeleteFromCircleListTable(circleId)).start();
                                MyfnfHashMaps.getInstance().getCircleLists().remove(circleId);
                                MyfnfHashMaps.getInstance().getCircleMembers().remove(circleId);
                                MyfnfHashMaps.getInstance().getCircleProfiles().remove(circleId);

                                if (getMainRightDetailsView().getCircleViewRight() != null) {
                                    getMainRightDetailsView().getCircleViewRight().buildCircleCount();
                                    getMainRightDetailsView().getCircleViewRight().buildCircleOfMe();
                                    GuiRingID.getInstance().loadIntoMainRightContent(getMainRightDetailsView().getCircleViewRight());
                                }
                                // GuiRingID.getInstance().showHome();
                            } else {
                                // JOptionPane.showConfirmDialog(null, fields.getMessage(), "Leave group Member", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                                HelperMethods.showPlaneDialogMessage(fields.getMessage());
                            }

                        } catch (Exception e) {
                            // e.printStackTrace();
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

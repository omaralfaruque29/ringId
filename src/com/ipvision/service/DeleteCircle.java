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
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class DeleteCircle extends Thread {

    private long circleId;

    public DeleteCircle(long circleId) {
        this.circleId = circleId;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields grpClass = new JsonFields();
                grpClass.setAction(AppConstants.TYPE_DELETE_CIRCLE);
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
                            (new DeleteFromCircleListTable(circleId)).start();
                            MyfnfHashMaps.getInstance().getCircleLists().remove(circleId);
                            MyfnfHashMaps.getInstance().getCircleMembers().remove(circleId);
                            MyfnfHashMaps.getInstance().getCircleProfiles().remove(circleId);

                            if (GuiRingID.getInstance() != null
                                    && GuiRingID.getInstance().getMainRight() != null
                                    && GuiRingID.getInstance().getMainRight().getCircleViewRight() != null) {
                                GuiRingID.getInstance().getMainRight().getCircleViewRight().buildCircleCount();
                                GuiRingID.getInstance().getMainRight().getCircleViewRight().buildMyCircle();
                                GuiRingID.getInstance().loadIntoMainRightContent(GuiRingID.getInstance().getMainRight().getCircleViewRight());
                            }
                            ///GuiRingID.getInstance().showHome();

                        } else {
                            HelperMethods.showPlaneDialogMessage(fields.getMessage());
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

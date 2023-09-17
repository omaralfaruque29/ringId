/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class ChangeNotificationStateRequest extends Thread {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeNotificationStateRequest.class);

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"actn":195,"pckId":"1409559768066ring8801717634317","sId":"1409559767716ring8801717634317","ut":1409559768066}
                 */
                JsonFields pakToSend = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();//AppConstants.TYPE_DELETE_MY_NOTIFICATIONS);
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_CHANGE_NOTIFICATION_STATE);
                pakToSend.setUt(AppConstants.NOTIFICATION_MAX_UT);
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        NotificationHistoryDAO.loadMaxTimeFromNotificationHistory();
                        NotificationHistoryDAO.loadMinTimeFromNotificationHistory();
                        break;
                    }
                }
                NotificationHistoryDAO.loadMaxTimeFromNotificationHistory();
                NotificationHistoryDAO.loadMinTimeFromNotificationHistory();

            } catch (Exception ex) {
                log.error("Error in ChangeNotificationStateRequest==>" + ex.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

}

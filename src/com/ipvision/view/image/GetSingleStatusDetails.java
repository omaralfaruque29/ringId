/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Faiz
 */
public class GetSingleStatusDetails extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetSingleStatusDetails.class);
    long nfID;

    public GetSingleStatusDetails(long NfId) {
        this.nfID = NfId;
    }

    @Override
    public void run() {
        try {

            JsonFields pakToSend = new JsonFields();
            String pakId = SendToServer.getRanDomPacketID();
            pakToSend.setPacketId(pakId);
            pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);

            pakToSend.setAction(AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION);
            pakToSend.setNfId(nfID);

            SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
            Thread.sleep(500);

            for (int i = 1; i <= 5; i++) {
                if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                    SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                } else {
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                    break;
                }
            }
            Thread.sleep(500);// to create singleImageDetails object
            if (SingleImgeDetails.getSingleImgeDetails() != null) {
                SingleImgeDetails.getSingleImgeDetails().statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(nfID);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
        }
    }

}

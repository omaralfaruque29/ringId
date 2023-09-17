/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.image.SingeImageCommentDetails;

/**
 *
 * @author Wasif Islam
 */
public class FeedEditImageCommentRequest extends Thread {

    private SingeImageCommentDetails singeImageCommentDetails;
    private JsonFields newComments;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedEditImageCommentRequest.class);

    public FeedEditImageCommentRequest(SingeImageCommentDetails singeImageCommentDetails, JsonFields newComments) {
        this.singeImageCommentDetails = singeImageCommentDetails;
        this.newComments = newComments;
         setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        try {
            /*{"actn":194,"pckId":"1409481389622ring8801717634317","sId":"1409481420638ring8801717634317","cmn":"Random comment 180 by ashraful","cmnId":4,"imgId":295}*/
            JsonFields pakToSend = new JsonFields();

            String pakId = SendToServer.getRanDomPacketID();
            pakToSend.setPacketId(pakId);
            pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
            pakToSend.setAction(AppConstants.TYPE_EDIT_IMAGE_COMMENT);
            pakToSend.setImageId(newComments.getImageId());
            pakToSend.setComment(newComments.getComment());
            pakToSend.setCmnId(newComments.getCmnId());
            SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
            Thread.sleep(25);

            for (int i = 1; i <= 7; i++) {
                Thread.sleep(500);
                if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                    SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                } else {
                    //{"actn":194,"pckId":"ring8801728119927111416466811930","sucs":true,"cmnId":1558,"cmn":"56656533","fn":"Faiz","ln":"Ahmed","imgId":9221}
                    FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                    if (responsefields.getSuccess()) {
                        try {
                            JsonFields updateOldComment = singeImageCommentDetails.addImageCaptionCommentsPanel.getImageComments().get(newComments.getCmnId());
                            if (updateOldComment != null) {
                                updateOldComment.setComment(newComments.getComment());
                            }
                        } catch (Exception e) {
                         //   e.printStackTrace();
                            log.error("Edit image comment ex==>" + e.getMessage());
                        }
                    } else {
                        HelperMethods.showWarningDialogMessage("Can not process this comment right now");
                    }
                    singeImageCommentDetails.changeComment();
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                    return;
                }

            }
            HelperMethods.showWarningDialogMessage("Can not process this comment right now");
        } catch (Exception ex) {
           // ex.printStackTrace();
          log.error("Edit image comment ex==>" + ex.getMessage());
        }
    }
}

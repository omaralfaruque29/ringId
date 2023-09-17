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
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.image.AddImageCaptionCommentsPanel;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.view.image.SingleImageDetailsProfileCover;
import com.ipvision.view.image.SingleImgeDetails;

/**
 *
 * @author Faiz Ahmed
 */
public class NewImageCommentRequest extends Thread {
//

    private JsonFields newComments;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NewImageCommentRequest.class);

    public NewImageCommentRequest(JsonFields newComments) {
        this.newComments = newComments;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"actn":180,"pckId":"1409481139435ring8801717634317","sId":"1409481170524ring8801717634317","cmn":"Random comment 26480.399736449446","imgId":295}
                 */
                JsonFields pakToSend = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_ADD_IMAGE_COMMENT);
                pakToSend.setImageId(newComments.getImageId());
                pakToSend.setComment(newComments.getComment());
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (responsefields.getSuccess()) {
                            try {
                                newComments.setCmnId(responsefields.getCmnId());
                                newComments.setTm(responsefields.getTm());
                                newComments.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                newComments.setTl(0L);
                                newComments.setIl((short) 0);
                                AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
                                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                                    SingleImgeDetails singleImgeDetails = SingleImgeDetails.getSingleImgeDetails();
                                    if (singleImgeDetails.imageDTo.getImageId().longValue() == newComments.getImageId()) {
                                        addImageCaptionCommentsPanel = singleImgeDetails.addImageCaptionCommentsPanel;

                                    }
                                } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                                    if (SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == newComments.getImageId()) {
                                        addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;
                                    }
                                } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                                    SingleImageDetailsProfileCover singleImgeDetails = SingleImageDetailsProfileCover.getSingleImgeDetails();
                                    if (singleImgeDetails.imageDTo.getImageId().longValue() == newComments.getImageId()) {
                                        addImageCaptionCommentsPanel = singleImgeDetails.addImageCaptionCommentsPanel;

                                    }
                                }
                                if (addImageCaptionCommentsPanel != null) {
                                    addImageCaptionCommentsPanel.getImageComments().put(responsefields.getCmnId(), newComments);
                                    addImageCaptionCommentsPanel.changeIComment(true);
                                    addImageCaptionCommentsPanel.addAComment(newComments);
                                    addImageCaptionCommentsPanel.changeImageCommentAllinBar();

                                }
                            } catch (Exception e) {
                                log.error("New Image Comment ex==>" + e.getMessage());
                            }

                        } else {
                            HelperMethods.showWarningDialogMessage(responsefields.getMessage());
                            //JOptionPane.showConfirmDialog(null, responsefields.getMessage(), "Comments failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }

                }
                HelperMethods.showWarningDialogMessage("Can not process this comment right now");
                //JOptionPane.showConfirmDialog(null, "Can not process this comment right now", "Comments failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

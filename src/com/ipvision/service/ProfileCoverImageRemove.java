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
import static com.ipvision.service.utility.SendToServer.sendPacketAsString;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ProfileCoverImageRemove extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileCoverImageRemove.class);
    private String loging_packedId;
    private JsonFields pakTosend;
    private int imageType;

    public ProfileCoverImageRemove(int imageType) {
        this.imageType = imageType;
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
                loging_packedId = SendToServer.getRanDomPacketID();
                if (MyFnFSettings.userProfile != null) {
                    pakTosend = new JsonFields();
                    if (imageType == MyFnFSettings.PROFILE_IMAGE) {
                        pakTosend.setAction(AppConstants.TYPE_REMOVE_PROFILE_IMAGE);
                        pakTosend.setProfileImage(MyFnFSettings.userProfile.getProfileImage());
                    } else if (imageType == MyFnFSettings.COVER_IMAGE) {
                        pakTosend.setAction(AppConstants.TYPE_REMOVE_COVER_IMAGE);
                        pakTosend.setCoverImage(MyFnFSettings.userProfile.getCoverImage());
                    }
                    pakTosend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    pakTosend.setPacketId(loging_packedId);

                    sendPacketAsString(pakTosend, ServerAndPortSettings.UPDATE_PORT);
                    Thread.sleep(125);
                    for (int i = 1; i <= 5; i++) {
                        if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(loging_packedId) == null) {
                            sendPacketAsString(pakTosend, ServerAndPortSettings.UPDATE_PORT);
                        } else {
                            try {
                                FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(loging_packedId);
                                if (fields.getSuccess()) {
                                    if (imageType == MyFnFSettings.PROFILE_IMAGE) {
                                        MyFnFSettings.userProfile.setProfileImageId(0L);
                                        MyFnFSettings.userProfile.setProfileImage(null);
                                    } else if (imageType == MyFnFSettings.COVER_IMAGE) {
                                        MyFnFSettings.userProfile.setCoverImageId(0L);
                                        MyFnFSettings.userProfile.setCoverImage(null);
                                        MyFnFSettings.userProfile.setCoverImageX(0);
                                        MyFnFSettings.userProfile.setCoverImageY(0);
                                    }
                                    getMainRightDetailsView().getMyProfilePanel().changeMyImage();
                                    GuiRingID.getInstance().getMyNamePanel().change_profile_image();
                                }
                            } catch (Exception e) {
                            }

                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(loging_packedId);
                            return;
                        }
                        Thread.sleep(1000);
                    }
                }
                //JOptionPane.showMessageDialog(null, "Failed! " + NotificationMessages.NETWORK_PROBLEM);
                HelperMethods.showPlaneDialogMessage("Failed! " + NotificationMessages.NETWORK_PROBLEM);
            } catch (Exception ex) {
                // ed.printStackTrace();
                log.error("Error in ProfileCoverImageRemove class ==>" + ex.getMessage());
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

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
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.view.myprofile.MyProfilePanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ChangePrivacy extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangePrivacy.class);
    private int changeType;
    private int privacy;

    public ChangePrivacy(int changeType, int privacy) {
        this.changeType = changeType;
        this.privacy = privacy;
    }

    private MyProfilePanel getMyProfilePanel() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel();
        } else {
            return null;
        }

//        return GuiRingID.getInstance().getMainRight().getMyProfilePanel();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields jsonMaker = new JsonFields();
                if (changeType == AppConstants.CONS_EMAIL) {
                    jsonMaker.setEmailPrivacy(privacy);
                } else if (changeType == AppConstants.CONS_MOBILE_PHONE) {
                    jsonMaker.setMobilePhonePrivacy(privacy);
                } else if (changeType == AppConstants.CONS_PROFILE_IMAGE) {
                    jsonMaker.setProfileImagePrivacy(privacy);
                } else if (changeType == AppConstants.CONS_BIRTH_DAY) {
                    jsonMaker.setBirthdayPrivacy(privacy);
                } else if (changeType == AppConstants.CONS_COVER_IMAGE) {
                    jsonMaker.setCoverImagePrivacy(privacy);
                } else {
                    return;
                }
                jsonMaker.setAction(AppConstants.TYPE_PRIVACY_SETTINGS);
                jsonMaker.setNoOfHeaders("1");
                jsonMaker.setPacketId(pakId);
                jsonMaker.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                SendToServer.sendPacketAsString(jsonMaker, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(150);
                for (int i = 1; i <= 5; i++) {
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(jsonMaker, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields != null && fields.getSuccess()) {
                            if (changeType == AppConstants.CONS_EMAIL) {
                                MyFnFSettings.userProfile.getPrivacy()[0] = (short) privacy;
                                MyFnFSettings.userProfile.setEmailPrivacy((short) privacy);
                                getMyProfilePanel().getMyProfileAbout().changEmailPrivacyIcon();
                            } else if (changeType == AppConstants.CONS_MOBILE_PHONE) {
                                MyFnFSettings.userProfile.getPrivacy()[1] = (short) privacy;
                                MyFnFSettings.userProfile.setMobilePrivacy((short) privacy);
                                getMyProfilePanel().getMyProfileAbout().changemobileNumber();
                            } else if (changeType == AppConstants.CONS_PROFILE_IMAGE) {
                                MyFnFSettings.userProfile.getPrivacy()[2] = (short) privacy;
                                MyFnFSettings.userProfile.setProfileImagePrivacy((short) privacy);
//                            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().profilePictureSettingBox.setSelected(false);
//                            if (privacy == AppConstants.PRIVACY_SHORT_PUBLIC) {
//                                GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().profilePictureSettingBox.setText("Make Profile Picture private");
//                            } else if (privacy == AppConstants.PRIVACY_SHORT_ONLY_ME) {
//                                GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().profilePictureSettingBox.setText("Make Profile Picture public");
//                            }
                                //GuiRingID.getInstance().myProfilePanel.changeMyImage();
                            } else if (changeType == AppConstants.CONS_BIRTH_DAY) {
                                MyFnFSettings.userProfile.getPrivacy()[3] = (short) privacy;
                                MyFnFSettings.userProfile.setBirthdayPrivacy((short) privacy);
                                getMyProfilePanel().getMyProfileAbout().changeBirthDay();
                            } else if (changeType == AppConstants.CONS_COVER_IMAGE) {
                                MyFnFSettings.userProfile.getPrivacy()[4] = (short) privacy;
                                MyFnFSettings.userProfile.setCoverImagePrivacy((short) privacy);
//                            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().coverPictureSettingBox.setSelected(false);
//                            if (privacy == AppConstants.PRIVACY_SHORT_PUBLIC) {
//                                GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().coverPictureSettingBox.setText("Make Cover Picture private");
//                            } else if (privacy == AppConstants.PRIVACY_SHORT_ONLY_ME) {
//                                GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().getPrivacySettingPanel().coverPictureSettingBox.setText("Make Cover Picture public");
//                            }

                                //GuiRingID.getInstance().myProfilePanel.changeMyImage();
                            }
                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                    Thread.sleep(600);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                log.error("Exception in Changing Privacy ==>" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

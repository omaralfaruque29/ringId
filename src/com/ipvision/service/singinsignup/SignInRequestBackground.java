/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.utility.SetSocketPorts;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.UserLogInInfo;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.service.KeepAlive;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class SignInRequestBackground extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignInRequestBackground.class);
    private String password;
    private String ringIdOrEmailOrPhone;
    private int type;
    private boolean ver_msg_show = false;

    public SignInRequestBackground() {
        log.debug("SignInRequest in Background ==> START");
        MyFnFSettings.COMMUNICATION_SERVER_STATUS = true;
        this.ringIdOrEmailOrPhone = MyFnFSettings.VALUE_LOGIN_USER_NAME;
        this.type = MyFnFSettings.VALUE_LOGIN_USER_TYPE;
        this.password = MyFnFSettings.VALUE_LOGIN_USER_PASSWORD;
    }

    public void startService() {
        this.start();
    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            SetSocketPorts obj = null;
            if (type == SettingsConstants.MOBILE_LOGIN) {
                obj = new SetSocketPorts(this, ringIdOrEmailOrPhone, MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
            } else {
                obj = new SetSocketPorts(this, ringIdOrEmailOrPhone);
            }
            obj.set_socket_port_with_existing_ringIDorEmailorPhone(type);
            GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
        } else {
            MyFnFSettings.USER_PREV_STATUS = MyFnFSettings.userProfile.getPresence(); 
            MyFnFSettings.userProfile.setPresence(StatusConstants.PRESENCE_OFFLINE);
            GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
            log.error("SignInRequest in Background failed ==> No Available Internet Connection!");
        }
    }

    private void saveLoginInfointoDB(String info) {
        List<UserLogInInfo> userLogInInfoList = new ArrayList<UserLogInInfo>();
        UserLogInInfo userInfo = new UserLogInInfo();
        userInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_INFO);
        userInfo.setValue(info);
        userLogInInfoList.add(userInfo);
        (new InsertRingLoginSetting(userLogInInfoList)).start();
    }

    public void startLoginProcess() {
        try {
            if (ServerAndPortSettings.AUTH_SERVER_IP != null && ServerAndPortSettings.AUTH_SERVER_IP.length() > 0) {
                JsonFields signInpacket = new JsonFields();
                signInpacket.setAction(AppConstants.TYPE_SIGN_IN);
                String pakId = SendToServer.getRanDomPacketID();
                signInpacket.setPacketId(pakId);
                signInpacket.setDeviceUniqueId(HelperMethods.getPCMacAddress());
                if (type == SettingsConstants.EMAIL_LOGIN) {
                    signInpacket.setLoginType(SettingsConstants.EMAIL_LOGIN);
                    signInpacket.setEmail(ringIdOrEmailOrPhone);
                } else if (type == SettingsConstants.RINGID_LOGIN) {
                    signInpacket.setLoginType(SettingsConstants.RINGID_LOGIN);
                    signInpacket.setUserIdentity(ringIdOrEmailOrPhone);
                } else if (type == SettingsConstants.MOBILE_LOGIN) {
                    signInpacket.setLoginType(SettingsConstants.MOBILE_LOGIN);
                    signInpacket.setMobilePhone(ringIdOrEmailOrPhone);
                    signInpacket.setMobilePhoneDialingCode(MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
                }
                signInpacket.setPassword(password);
                signInpacket.setVersion(MyFnFSettings.VERSION_PC);
                signInpacket.setDevice(AppConstants.PLATFORM_DESKTOP);
                if (InternetUnavailablityCheck.isInternetAvailable) {
                    SendToServer.sendPacketAsString(signInpacket, ServerAndPortSettings.AUTHENTICATION_PORT);
                } else {
                    log.error("SignInRequest in Background failed ==> No Available Internet Connection!");
                    return;
                }
                for (int i = 1; i <= 3600; i++) {
                    Thread.sleep(500);

                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        if (i % 10 == 0) {
                            if (InternetUnavailablityCheck.isInternetAvailable) {
                                SendToServer.sendPacketAsString(signInpacket, ServerAndPortSettings.AUTHENTICATION_PORT);
                            } else {
                                log.error("SignInRequest in Background failed ==> No Available Internet Connection!");
                                return;
                            }
                        }
                    } else {
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            JsonFields fields = HelperMethods.getJsonFields(feedBackFields.getMessage());
                            MyFnFSettings.userProfile = new UserBasicInfo();
                            if (fields.getUserIdentity() != null) {
                                MyFnFSettings.userProfile.setUserIdentity(fields.getUserIdentity());
                                MyFnFSettings.LOGIN_USER_ID = fields.getUserIdentity();
                            }
                            MyFnFSettings.LOGIN_SESSIONID = fields.getSessionId();
                            if (fields.getUserTableId() != null) {
                                MyFnFSettings.LOGIN_USER_TABLE_ID = fields.getUserTableId();
                            }
                            if (fields.getFullName() != null) {
                                MyFnFSettings.userProfile.setFullName(fields.getFullName());
                            }
                            if (fields.getIsEmailVerified() != null) {
                                MyFnFSettings.userProfile.setIsEmailVerified(fields.getIsEmailVerified());
                            }
                            if (fields.getIsMobileNumberVerified() != null) {
                                MyFnFSettings.userProfile.setIsMobileNumberVerified(fields.getIsMobileNumberVerified());
                            }
                            if (fields.getAdditionalInfo() != null) {
                                MyFnFSettings.userProfile.setAdditionalInfo(fields.getAdditionalInfo());
                            }
                            MyFnFSettings.FRIEND_LIST_LOADED = false;
                            MyFnFSettings.userProfile.setPresence(fields.getLastStatus());
                            MyFnFSettings.userProfile.setPhoneNumberSuggestion(fields.getPhoneNumberSuggestion());
                            MyFnFSettings.userProfile.setContactListSuggestion(fields.getContactListSuggestion());
                            MyFnFSettings.userProfile.setCommonFriendsSuggestion(fields.getCommonFriendsSuggestion());
                            saveLoginInfointoDB(feedBackFields.getMessage());
                            HelperMethods.bindMyProfileObject(fields, true);
                            HelperMethods.startVoiceAndChatSockets();
                            new KeepAlive().start();
                            GuiRingID.getInstance().sendDefaultRequestToServer();
                            GuiRingID.getInstance().getMyNamePanel().changeStatusIcon();
                            if (isFriendListExist()) {
                                // GuiRingID.getInstance().getMainLeftContainer().getFriendListPanel().repaint();
                                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().repaint();
                            }
                        } else {
                            JsonFields fields2 = HelperMethods.getJsonFields(feedBackFields.getMessage());
                            if (fields2 != null) {
                                MyFnFSettings.LOGIN_SESSIONID = null;
                                if (MyFnFSettings.userProfile != null) {
                                    MyFnFSettings.userProfile = null;
                                }
                                if (fields2.getVersionMessage() != null && fields2.getVersionMessage().length() > 0 && ver_msg_show == false) {
                                    ver_msg_show = true;
                                    HelperMethods.showWarningDialogMessage(fields2.getVersionMessage());
                                }
                                if (fields2.getDownloadMandatory() != null && fields2.getDownloadMandatory()) {
                                    System.exit(0);
                                } else {
                                    GuiRingID.getInstance().signoutActions(false);
                                }
                            } else {
                                log.error("SignInRequest in Background failed ==> " + AppConstants.TYPE_SIGN_IN + " response not in correct format==>" + feedBackFields.getMessage());
                                GuiRingID.getInstance().signoutActions(false);
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
            } else {
                MyFnFSettings.COMMUNICATION_SERVER_STATUS = false;
                log.error("SignInRequest in Background failed ==> Communicatin server not responding");
            }
        } catch (InterruptedException | HeadlessException ex) {
            log.error("Exception in here ==>" + ex.getMessage());
        }
    }

    private boolean isFriendListExist() {
        return GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                //  && GuiRingID.getInstance().getMainLeftContainer().getFriendListPanel() != null;
                && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null;
    }

}

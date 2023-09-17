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
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.UpdateMyProfileFields;
import com.ipvision.view.myprofile.MyProfilePanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ChangeMyProfile extends Thread {

    private JLabel loadingLabel;
    private String value;
    private int indexNumber;
    private JsonFields jF;
    private UpdateMyProfileFields uF;
    private JButton okButton;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeMyProfile.class);

    private void setMessage(int a) {
        String string;
        switch (a) {
            case 1:
                string = ".";
                break;
            case 2:
                string = "..";
                break;
            case 3:
                string = "...";
                break;
            case 4:
                string = "....";
                break;
            case 5:
                string = "......";
                break;
            default:
                string = "";

        }
        this.loadingLabel.setText("Saving " + string);

    }

    public ChangeMyProfile(JLabel loadJLabel, String change, int indexNumber1, JButton okButton1) {
        this.loadingLabel = loadJLabel;
        this.value = change;
        this.indexNumber = indexNumber1;
        this.okButton = okButton1;
    }

    private MyProfilePanel getMyProfilePanel() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pak_id = SendToServer.getRanDomPacketID();
                if (indexNumber == AppConstants.CONS_MARRIAGE_DAY) {
                    uF = new UpdateMyProfileFields();
                    uF.setAction(AppConstants.TYPE_USER_PROFILE);
                    uF.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    uF.setPacketId(pak_id);
                    uF.setNoOfHeaders("1");
                    uF.setMarriageDay(value);
                    SendToServer.sendPacketAsString(uF, ServerAndPortSettings.UPDATE_PORT);
                } else {
                    jF = new JsonFields();
                    jF.setAction(AppConstants.TYPE_USER_PROFILE);
                    jF.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    jF.setPacketId(pak_id);
                    jF.setNoOfHeaders("1");
                    if (indexNumber == AppConstants.CONS_FULL_NAME) {
                        jF.setFullName(value);
                    } /*else if (indexNumber == AppConstants.CONS_LAST_NAME) {
                     jF.setLastName(value);
                     } */ else if (indexNumber == AppConstants.CONS_COUNTRY) {
                        jF.setCountry(value);
                    } else if (indexNumber == AppConstants.CONS_CALLING_CODE) {
                        jF.setMobilePhoneDialingCode(value);
                    } else if (indexNumber == AppConstants.CONS_MOBILE_PHONE) {
                        jF.setMobilePhone(value);
                    } else if (indexNumber == AppConstants.CONS_EMAIL) {
                        jF.setEmail(this.value);
                    } else if (indexNumber == AppConstants.CONS_GENDER) {
                        jF.setGender(value);
                    } else if (indexNumber == AppConstants.CONS_BIRTH_DAY) {
                        jF.setBirthday(value);
                    } else if (indexNumber == AppConstants.CONS_CURRENT_CITY) {
                        jF.setCurrentCity(value);
                    } else if (indexNumber == AppConstants.CONS_HOME_CITY) {
                        jF.setHomeCity(value);
                    } else if (indexNumber == AppConstants.CONS_ABOUT_ME) {
                        jF.setAboutMe(value);
                    }
                    SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                }
                //   Thread.sleep(100);
                for (int i = 1; i <= 5; i++) {
                    setMessage(i);
                    Thread.sleep(500);
                    //  MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().isEmpty() || MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id) == null) {
                        if (indexNumber == AppConstants.CONS_MARRIAGE_DAY) {
                            SendToServer.sendPacketAsString(uF, ServerAndPortSettings.UPDATE_PORT);
                        } else {
                            SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                        }

                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id);
                        if (js.getSuccess()) {
                            if (indexNumber == AppConstants.CONS_FULL_NAME) {
                                MyFnFSettings.userProfile.setFullName(this.value);
                                GuiRingID.getInstance().getMyNamePanel().change_myFullNameRingID();
                                getMyProfilePanel().change_myFullName();
                                getMyProfilePanel().getMyProfileAbout().changeFullName();
                            } /*else if (indexNumber == AppConstants.CONS_LAST_NAME) {
                             MyFnFSettings.userProfile.setLastName(this.value);
                             GuiRingID.getInstance().getMyNameAndNotificationPanel().change_myFullNameRingID();
                             getMyProfilePanel().change_myFullNameRingID();
                             getMyProfilePanel().changeLastname();
                             }*/ else if (indexNumber == AppConstants.CONS_COUNTRY) {
                                MyFnFSettings.userProfile.setCountry(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeCountry();
                            } else if (indexNumber == AppConstants.CONS_CALLING_CODE) {
                                MyFnFSettings.userProfile.setMobilePhoneDialingCode(this.value);
                                getMyProfilePanel().getMyProfileAbout().changemobileNumber();
                            } else if (indexNumber == AppConstants.CONS_MOBILE_PHONE) {
                                MyFnFSettings.userProfile.setMobilePhone(this.value);
                                getMyProfilePanel().getMyProfileAbout().changemobileNumber();
                            } else if (indexNumber == AppConstants.CONS_EMAIL) {
                                MyFnFSettings.userProfile.setEmail(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeEmail();
                            } else if (indexNumber == AppConstants.CONS_GENDER) {
                                MyFnFSettings.userProfile.setGender(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeGender();
                            } else if (indexNumber == AppConstants.CONS_CURRENT_CITY) {
                                MyFnFSettings.userProfile.setCurrentCity(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeCurrentCity();
                            } else if (indexNumber == AppConstants.CONS_HOME_CITY) {
                                MyFnFSettings.userProfile.setHomeCity(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeHomeCity();
                            } else if (indexNumber == AppConstants.CONS_MARRIAGE_DAY) {
                                MyFnFSettings.userProfile.setMarriageDay(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeMarriageDay();
                            } else if (indexNumber == AppConstants.CONS_BIRTH_DAY) {
                                MyFnFSettings.userProfile.setBirthday(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeBirthDay();
                            } else if (indexNumber == AppConstants.CONS_ABOUT_ME) {
                                MyFnFSettings.userProfile.setAboutMe(this.value);
                                getMyProfilePanel().getMyProfileAbout().changeAboutMe();
                            }
                        } else {
                            loadingLabel.setText("Failed!!!");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pak_id);
                        loadingLabel.setText("");
                        return;
                    }
                }
                loadingLabel.setText("Failed!!!");
                this.okButton.setEnabled(true);
            } catch (Exception e) {
                log.error("Failed! to change profile" + e.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

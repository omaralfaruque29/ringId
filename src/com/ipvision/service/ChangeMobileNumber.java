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
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.MyNamePanel;
import com.ipvision.view.myprofile.MyProfilePanel;
import com.ipvision.view.myprofile.MyProfilePhoneNumberPanel;

/**
 *
 * @author Sirat Samyoun
 */
public class ChangeMobileNumber extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeMobileNumber.class);
    private String mbl, mblDc;
    private MyProfilePhoneNumberPanel myProfilePhoneNumberPanel;

    public ChangeMobileNumber(String mbl, String mblDc, MyProfilePhoneNumberPanel myProfilePhoneNumberPanel) {
        this.mbl = mbl;
        this.mblDc = mblDc;
        this.myProfilePhoneNumberPanel = myProfilePhoneNumberPanel;
        if (myProfilePhoneNumberPanel != null) {
            myProfilePhoneNumberPanel.okButton.setEnabled(false);
            myProfilePhoneNumberPanel.closeButton.setEnabled(false);
            myProfilePhoneNumberPanel.countryChooseList.disablePanel();//valuebox.setEnabled(false);
        }
    }

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
        if (myProfilePhoneNumberPanel != null) {
            myProfilePhoneNumberPanel.pleasW.setText("Saving " + string);
        }
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
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ACTION_VERIFY_MY_NUMBER);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                js_fields.setMobilePhone(mbl);
                js_fields.setMobilePhoneDialingCode(mblDc);
                //               js_fields.setMobilePhone("");
//                js_fields.setMobilePhoneDialingCode("");

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);

                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
                    setMessage(i);
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            MyFnFSettings.userProfile.setIsMobileNumberVerified(0);
                            MyFnFSettings.userProfile.setMobilePhone(this.mbl);
                            MyFnFSettings.userProfile.setMobilePhoneDialingCode(this.mblDc);
                            MyNamePanel.getInstance().change_myFullNameRingID();
                           // if (MyNamePanel.mobileNoLabel != null) {
                                //MyNamePanel.mobileNoLabel.setText(MyFnFSettings.userProfile.getMobilePhoneDialingCode() + " " + MyFnFSettings.userProfile.getMobilePhone());
                           // } //
                            getMyProfilePanel().getMyProfileAbout().changemobileNumber();
                            if (myProfilePhoneNumberPanel != null) {
                                myProfilePhoneNumberPanel.action_save_primary_number();
                                myProfilePhoneNumberPanel.okButton.setEnabled(true);
                                myProfilePhoneNumberPanel.closeButton.setEnabled(true);
                                myProfilePhoneNumberPanel.countryChooseList.enablePanel();//valuebox.setEnabled(true);
                                myProfilePhoneNumberPanel.privacyButton.setVisible(true);
                            }
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        } else {
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
                log.error("Error in ChangeMobileNumber class ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

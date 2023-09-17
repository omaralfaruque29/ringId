/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.service.utility.SetSocketPorts;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserLogInInfo;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class SignUpRequest extends Thread {

    private boolean success = false;
    private String email = null, mbl = null, mblDc = null;
    private String code;
    private String userName;
    private String password;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton doneButton;
    private JButton cancelButton;
    private boolean ver_msg_show = false;
    private JLabel ringIdLabel;
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignUpRequest.class);
    private int type_int;

    public SignUpRequest(String email,
            String code,
            String userName,
            String password,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton doneButton,
            JButton cancelButton,
            JLabel ringIdLabel) {
        this.email = email;
        this.code = code;
        this.userName = userName;
        this.password = password;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.doneButton = doneButton;
        this.cancelButton = cancelButton;
        this.ringIdLabel = ringIdLabel;
        this.type_int = SettingsConstants.EMAIL_LOGIN;
        setName(this.getClass().getName());
    }

    public SignUpRequest(String mbl, String mblDc,
            String code,
            String userName,
            String password,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton doneButton,
            JButton cancelButton,
            JLabel ringIdLabel) {
        this.mbl = mbl;
        this.mblDc = mblDc;
        this.code = code;
        this.userName = userName;
        this.password = password;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.doneButton = doneButton;
        this.cancelButton = cancelButton;
        this.ringIdLabel = ringIdLabel;
        this.type_int = SettingsConstants.MOBILE_LOGIN;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (doneButton != null) {
                    doneButton.setEnabled(false);
                }
                if (cancelButton != null) {
                    cancelButton.setEnabled(false);
                }
                if (errorLabel != null) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                    //errorLabel.setVisible(true);
                }
                if (errorTextArea != null) {
                    errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                    errorTextArea.setText(" Please wait...");
                }

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ADD_PROFILE_DETAILS);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.VALUE_NEW_USER_NAME);
                js_fields.setName(userName);
                js_fields.setPassword(password);
                js_fields.setIspc(0);
                if (type_int == SettingsConstants.EMAIL_LOGIN) {
                    js_fields.setEmail(email);
                    js_fields.setEmailVerificationCode(code);
                } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
                    js_fields.setMobilePhone(mbl);
                    js_fields.setMobilePhoneDialingCode(mblDc);
                    js_fields.setVerificationCode(code);
                }
                js_fields.setDeviceUniqueId(HelperMethods.getPCMacAddress());
                js_fields.setVersion(MyFnFSettings.VERSION_PC);
                js_fields.setDevice(AppConstants.PLATFORM_DESKTOP);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        this.doneButton.setEnabled(true);
                        this.cancelButton.setEnabled(true);
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            deleteNewUserIDfromDB();
                            MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 1;
                            MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 1;
                            MyFnFSettings.VALUE_LOGIN_AUTO_START = 1;
                            MyFnFSettings.VALUE_LOGIN_USER_INFO = null;
                            if (type_int == SettingsConstants.EMAIL_LOGIN) {
                                new SignInRequest(errorLabel, errorTextArea, email, password, doneButton, null, cancelButton, SettingsConstants.EMAIL_LOGIN, true).start();
                            } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
                                new SignInRequest(errorLabel, errorTextArea, mbl, password, doneButton, null, cancelButton, SettingsConstants.MOBILE_LOGIN, mblDc, true).start();
                            }

                        } else {
                            log.error(AppConstants.TYPE_ADD_PROFILE_DETAILS + " response not in correct format==>" + feedBackFields.getMessage());
                            this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                            this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);

                            if (feedBackFields.getUf() == 0) {
                                this.errorTextArea.setText(" " + MyFnFSettings.VALUE_NEW_USER_NAME + ": " + feedBackFields.getMessage());
                                deleteNewUserIDfromDB();
                                SetSocketPorts obj = new SetSocketPorts(this, ringIdLabel);
                                obj.set_socket_ports_with_new_ringID();
                            } else {
                                this.errorTextArea.setText(" " + feedBackFields.getMessage());
                            }

                        }

                    }
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                    return;

                }
            } catch (InterruptedException | HeadlessException ex) {
                log.error("Exception in here ==>" + ex.getMessage());
                this.doneButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText(" Failed! Internal server error. Please Try again");
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }

    public void deleteNewUserIDfromDB() {
        List<UserLogInInfo> newUserInfoList = new ArrayList<UserLogInInfo>();

        UserLogInInfo nameInfo = new UserLogInInfo();
        nameInfo.setKey(MyFnFSettings.KEY_NEW_USER_NAME);
        nameInfo.setStatus(StatusConstants.STATUS_DELETED);
        newUserInfoList.add(nameInfo);
        // MyFnFSettings.VALUE_NEW_USER_NAME = "";
        (new InsertRingLoginSetting(newUserInfoList)).start();
    }
}

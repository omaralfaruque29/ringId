/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.view.loginsignup.LoginUI;
import com.ipvision.view.SplashScreenAfterLogin;
import com.ipvision.service.utility.SetSocketPorts;
import com.ipvision.model.dao.RingUserSettingsDAO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.UserLogInInfo;
import com.ipvision.service.KeepAlive;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Faiz Ahmed
 */
public class SignInRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignInRequest.class);
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private String password;
    private JButton button1;
    private JLabel button2;
    private JButton button3;
    private boolean ver_msg_show = false;
    private String ringId;
    private SetSocketPorts obj;
    private String mblDc = null, email = null, mbl = null;
    private int type_int = 0;
    private JsonFields fields;
    private boolean isSignUp;

    public SignInRequest(JLabel loadingOrtimePanel,
            JTextArea errorTextArea,
            String ringId,
            String password,
            JButton loginbutton,
            JLabel createNewButton,
            JButton cancelButton,
            int type_int,
            boolean isSignUp) {
        MyFnFSettings.COMMUNICATION_SERVER_STATUS = true;
        this.errorLabel = loadingOrtimePanel;
        this.errorTextArea = errorTextArea;
        this.ringId = ringId;
        this.password = password;
        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = password;
        this.button1 = loginbutton;
        this.button2 = createNewButton;
        this.button3 = cancelButton;
        this.type_int = type_int;
        this.isSignUp = isSignUp;
        setName(this.getClass().getName());
    }

    public SignInRequest(JLabel loadingOrtimePanel,
            JTextArea errorTextArea,
            String ringId,
            String password,
            JButton loginbutton,
            JLabel createNewButton,
            JButton cancelButton,
            int type_int, String mblDc,
            boolean isSignUp) {
        MyFnFSettings.COMMUNICATION_SERVER_STATUS = true;
        this.errorLabel = loadingOrtimePanel;
        this.errorTextArea = errorTextArea;
        this.ringId = ringId;
        this.password = password;
        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = password;
        this.button1 = loginbutton;
        this.button2 = createNewButton;
        this.button3 = cancelButton;
        this.type_int = type_int;
        this.mblDc = mblDc;
        MyFnFSettings.VALUE_MOBILE_DIALING_CODE = mblDc;
        this.isSignUp = isSignUp;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        // MyFnFSettings.VALUE_LOGIN_USER_TYPE = type_int;
        if (button1 != null) {
            this.button1.setEnabled(false);
        }
        if (button2 != null) {
            this.button2.setEnabled(false);
        }
        if (this.button3 != null) {
            this.button3.setEnabled(false);
        }
        this.errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
        this.errorTextArea.setForeground(DefaultSettings.blue_bar_background);
        this.errorTextArea.setText(" Please wait...");
        //set_socket_port();
//        if (pattern.matcher(ringId).matches()) {
//            isMail = true;
//        }
        if (type_int == SettingsConstants.MOBILE_LOGIN) {
            obj = new SetSocketPorts(this, ringId, mblDc);
        } else {
            obj = new SetSocketPorts(this, ringId);
        }
        obj.set_socket_port_with_existing_ringIDorEmailorPhone(type_int);

    }

    public void startLoginProcess() {
        try {
            if (ServerAndPortSettings.AUTH_SERVER_IP != null && ServerAndPortSettings.AUTH_SERVER_IP.length() > 0) {
                JsonFields signInpacket = new JsonFields();
                signInpacket.setAction(AppConstants.TYPE_SIGN_IN);
                String pakId = SendToServer.getRanDomPacketID();
                signInpacket.setPacketId(pakId);
                signInpacket.setDeviceUniqueId(HelperMethods.getPCMacAddress());
                //signInpacket.setUserIdentity(MyFnFSettings.VALUE_NEW_USER_NAME);
                if (MyFnFSettings.VALUE_NEW_USER_NAME != null && MyFnFSettings.VALUE_NEW_USER_NAME.trim().length() > 0) {
                    signInpacket.setUserIdentity(MyFnFSettings.VALUE_NEW_USER_NAME);
                    signInpacket.setLoginType(SettingsConstants.RINGID_LOGIN);
                } else {
                    if (type_int == SettingsConstants.EMAIL_LOGIN) {
                        signInpacket.setLoginType(SettingsConstants.EMAIL_LOGIN);
                        signInpacket.setEmail(ringId);
                    } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
                        signInpacket.setLoginType(SettingsConstants.MOBILE_LOGIN);
                        signInpacket.setMobilePhone(ringId);
                        signInpacket.setMobilePhoneDialingCode(mblDc);
                    } else {
                        signInpacket.setLoginType(SettingsConstants.RINGID_LOGIN);
                        signInpacket.setUserIdentity(ringId);
                    }
                }
                signInpacket.setPassword(password);
                signInpacket.setVersion(MyFnFSettings.VERSION_PC);
                signInpacket.setDevice(AppConstants.PLATFORM_DESKTOP);
                SendToServer.sendPacketAsString(signInpacket, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 60; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        if (i % 10 == 0) {
                            SendToServer.sendPacketAsString(signInpacket, ServerAndPortSettings.AUTHENTICATION_PORT);
                        }
                    } else {
                        if (button1 != null) {
                            this.button1.setEnabled(true);
                        }
                        if (button2 != null) {
                            this.button2.setEnabled(true);
                        }
                        if (this.button3 != null) {
                            this.button3.setEnabled(true);
                        }

                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            fields = HelperMethods.getJsonFields(feedBackFields.getMessage());
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
                            /*if (fields.getLastName() != null) {
                             MyFnFSettings.userProfile.setLastName(fields.getLastName());
                             }*/
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

                            if (type_int == SettingsConstants.EMAIL_LOGIN) {
                                if (fields.getIsEmailVerified() != null && fields.getIsEmailVerified() == 1) {
                                    MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.EMAIL_LOGIN;
                                    MyFnFSettings.VALUE_LOGIN_USER_NAME = ringId;
                                    action_verifed();
                                } else {
                                    MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.RINGID_LOGIN;
                                    MyFnFSettings.VALUE_LOGIN_USER_NAME = fields.getUserIdentity();
                                    action_email_unverifed();
                                }
                            } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
                                if (fields.getIsMobileNumberVerified() != null && fields.getIsMobileNumberVerified() == 1) {
                                    MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.MOBILE_LOGIN;
                                    MyFnFSettings.VALUE_LOGIN_USER_NAME = ringId;
                                    action_verifed();
                                } else {
                                    MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.RINGID_LOGIN;
                                    MyFnFSettings.VALUE_LOGIN_USER_NAME = fields.getUserIdentity();
                                    action_mobile_unverifed();
                                }
                            } else {
                                MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.RINGID_LOGIN;
                                MyFnFSettings.VALUE_LOGIN_USER_NAME = fields.getUserIdentity();
                                if (fields.getIsMobileNumberVerified() != null && fields.getIsMobileNumberVerified() == 0
                                        && fields.getMobilePhone() != null && fields.getMobilePhone().trim().length() > 0
                                        && fields.getMobilePhoneDialingCode() != null && fields.getMobilePhoneDialingCode().trim().length() > 0) {
                                    action_mobile_unverifed();
                                } else if (fields.getIsEmailVerified() != null && fields.getIsEmailVerified() == 0
                                        && fields.getEmail() != null && fields.getEmail().trim().length() > 0) {
                                    action_email_unverifed();
                                } else {
                                    action_verifed();
                                }
                            }
                            saveLoginInfointoDB(feedBackFields.getMessage());
                            this.errorLabel.setIcon(null);
                            this.errorTextArea.setText("");
                            new KeepAlive().start();
                        } else {
                            JsonFields fields2 = HelperMethods.getJsonFields(feedBackFields.getMessage());
                            if (fields2 != null) {
                                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                                this.errorTextArea.setText(" " + fields2.getMessage());
                                MyFnFSettings.LOGIN_SESSIONID = null;
                                if (MyFnFSettings.userProfile != null) {
                                    MyFnFSettings.userProfile = null;
                                }
                                if (fields2.getVersionMessage() != null && fields2.getVersionMessage().length() > 0 && ver_msg_show == false) {
                                    ver_msg_show = true;
                                    HelperMethods.showWarningDialogMessage(fields2.getVersionMessage());
                                    //JOptionPane.showConfirmDialog(null, fields2.getVersionMessage(), StaticFields.APP_NAME, JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                                }
                                if (fields2.getDownloadMandatory() != null && fields2.getDownloadMandatory()) {
                                    System.exit(0);
                                }
                            } else {
                                log.error(AppConstants.TYPE_SIGN_IN + " response not in correct format==>" + feedBackFields.getMessage());
                                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                                this.errorTextArea.setText(" Failed! Internal server error. Please Try again");
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                if (button1 != null) {
                    this.button1.setEnabled(true);
                }
                if (button2 != null) {
                    this.button2.setEnabled(true);
                }
                if (this.button3 != null) {
                    this.button3.setEnabled(true);
                }

                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText(" Failed! " + NotificationMessages.NETWORK_PROBLEM);
            } else {
                MyFnFSettings.COMMUNICATION_SERVER_STATUS = false;
                log.error("Communicatin server not responding");
                if (button1 != null) {
                    this.button1.setEnabled(true);
                }
                if (button2 != null) {
                    this.button2.setEnabled(true);
                }
                if (this.button3 != null) {
                    this.button3.setEnabled(true);
                }
                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText(" Failed! Try again");
            }
        } catch (InterruptedException | HeadlessException ex) {
            log.error("Exception in here ==>" + ex.getMessage());
            if (button1 != null) {
                this.button1.setEnabled(true);
            }
            if (button2 != null) {
                this.button2.setEnabled(true);
            }
            if (this.button3 != null) {
                this.button3.setEnabled(true);
            }
            this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
            this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
            this.errorTextArea.setText(" Failed! Try again");
        }
    }

    private void action_verifed() {
        if (isSignUp) {
            LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().SUCCESS_UI);
            LoginUI.getLoginUI().successPanel.setValues(fields);
            /*fields.setPassword(password);
             fields.setUserIdentity(ringId);
             if (type_int == SettingsConstants.EMAIL_LOGIN) {
             fields.setLoginType(SettingsConstants.EMAIL_LOGIN);
             fields.setEmail(ringId);
             } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
             fields.setLoginType(SettingsConstants.MOBILE_LOGIN);
             fields.setMobilePhone(ringId);
             fields.setMobilePhoneDialingCode(mblDc);
             } else {
             fields.setLoginType(SettingsConstants.RINGID_LOGIN);
             }*/

        } else {
            if ((fields.getProfileImage() == null || fields.getProfileImage().trim().length() <= 0) && RingUserSettingsDAO.getLastLoggedInTime() <= 0) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.PROFILE_IMAGE_UI);
                LoginUI.getLoginUI().profileImagePanel.setJson_fields(fields);
            } else {
                if (LoginUI.getLoginUI() != null) {
                    LoginUI.getLoginUI().disposeLoginUI();
                    LoginUI.loginUI = null;
                }
                SplashScreenAfterLogin sp = new SplashScreenAfterLogin(fields);
                sp.setVisible(true);
            }
        }
    }

    private void action_mobile_unverifed() {
        LoginUI.getLoginUI().loadMainContent(LoginUI.EMAIL_MOBILE_VARIFICATION_UI);
        LoginUI.getLoginUI().emailVarificationPanel.set_Is_Sign_Up(isSignUp);
        LoginUI.getLoginUI().emailVarificationPanel.setJson_fields(fields);
        LoginUI.getLoginUI().emailVarificationPanel.setMobile();
        HelperMethods.showWarningDialogMessage("Your Mobile Number is not verified! You can login using verified Mobile Number!");
    }

    private void action_email_unverifed() {
        LoginUI.getLoginUI().loadMainContent(LoginUI.EMAIL_MOBILE_VARIFICATION_UI);
        LoginUI.getLoginUI().emailVarificationPanel.set_Is_Sign_Up(isSignUp);
        LoginUI.getLoginUI().emailVarificationPanel.setJson_fields(fields);
        LoginUI.getLoginUI().emailVarificationPanel.setEmail();
        HelperMethods.showWarningDialogMessage("Your E-mail address is not verified! You can login using verified email!");
    }

    public void saveLoginInfointoDB(String info) {
        List<UserLogInInfo> userLogInInfoList = new ArrayList<UserLogInInfo>();
        UserLogInInfo u1 = new UserLogInInfo();
        u1.setKey(MyFnFSettings.KEY_LOGIN_USER_TYPE);
        u1.setValue(MyFnFSettings.VALUE_LOGIN_USER_TYPE + "");
        userLogInInfoList.add(u1);
        UserLogInInfo u2 = new UserLogInInfo();
        u2.setKey(MyFnFSettings.KEY_LOGIN_USER_NAME);
        u2.setValue(MyFnFSettings.VALUE_LOGIN_USER_NAME);
        userLogInInfoList.add(u2);
//        UserLogInInfo nameInfo = new UserLogInInfo();
//        nameInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_NAME);
//        nameInfo.setValue(ringId);
//        userLogInInfoList.add(nameInfo);
//
//        UserLogInInfo typeInfo = new UserLogInInfo();
//        typeInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_TYPE);
//        typeInfo.setValue(MyFnFSettings.VALUE_LOGIN_USER_TYPE + "");
//        userLogInInfoList.add(typeInfo);

        //   if (MyFnFSettings.VALUE_MOBILE_DIALING_CODE != null) {
        UserLogInInfo mblDcInfo = new UserLogInInfo();
        mblDcInfo.setKey(MyFnFSettings.KEY_MOBILE_DIALING_CODE);
        mblDcInfo.setValue(MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
        userLogInInfoList.add(mblDcInfo);
        // }
        UserLogInInfo passwordInfo = new UserLogInInfo();
        passwordInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_PASSWORD);
        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = password;
        String newPass = HelperMethods.encryptPassword(password);
        passwordInfo.setValue(newPass);  //String.valueOf(passTextField.getPassword())                
        userLogInInfoList.add(passwordInfo);

        UserLogInInfo userInfo = new UserLogInInfo();
        userInfo.setKey(MyFnFSettings.KEY_LOGIN_USER_INFO);
        userInfo.setValue(info);
        userLogInInfoList.add(userInfo);

        UserLogInInfo savePassInfo = new UserLogInInfo();
        savePassInfo.setKey(MyFnFSettings.KEY_LOGIN_SAVE_PASSWORD);
        savePassInfo.setValue(MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD + "");
        userLogInInfoList.add(savePassInfo);

        if (!ringId.equals(MyFnFSettings.PREVIOUS_USER_NAME)) {
            UserLogInInfo autostartInfo = new UserLogInInfo();
            autostartInfo.setKey(MyFnFSettings.KEY_LOGIN_AUTO_START);
            autostartInfo.setValue("1");
            userLogInInfoList.add(autostartInfo);

            UserLogInInfo autologinInfo = new UserLogInInfo();
            autologinInfo.setKey(MyFnFSettings.KEY_LOGIN_AUTO_SIGNIN);
            userLogInInfoList.add(autologinInfo);
            if (MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 1) {
                autologinInfo.setValue("1");
            } else {
                autologinInfo.setValue("0");
            }
        } else {
            UserLogInInfo autologinInfo = new UserLogInInfo();
            autologinInfo.setKey(MyFnFSettings.KEY_LOGIN_AUTO_SIGNIN);
            autologinInfo.setValue(MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN + "");
            userLogInInfoList.add(autologinInfo);
        }

        (new InsertRingLoginSetting(userLogInInfoList)).start();
    }
}

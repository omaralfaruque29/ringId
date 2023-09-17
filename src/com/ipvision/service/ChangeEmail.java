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
import java.awt.Cursor;
import javax.swing.JButton;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.view.myprofile.CreateEmailPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.MyNamePanel;

/**
 *
 * @author Dell
 */
public class ChangeEmail extends Thread {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BlockUnblockFriend.class);
    private String email;
    private JButton okButton;
    private String previous_str;
    private CreateEmailPanel createEmailAboutPanelPrivacy;

    public ChangeEmail(String email, JButton okButton1, CreateEmailPanel createEmailAboutPanelPrivacy) {
        this.email = email;
        this.okButton = okButton1;
        this.createEmailAboutPanelPrivacy = createEmailAboutPanelPrivacy;
        if (createEmailAboutPanelPrivacy != null) {
            this.previous_str = createEmailAboutPanelPrivacy.pleasW.getText();
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
        if (createEmailAboutPanelPrivacy != null) {
            createEmailAboutPanelPrivacy.pleasW.setText("Saving " + string);
        }

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_ADD_VERIFY_EMAIL);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                js_fields.setEmail(email);

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
                            MyFnFSettings.userProfile.setIsEmailVerified(0);
                            MyFnFSettings.userProfile.setEmail(this.email);
                            MyNamePanel.getInstance().change_myFullNameRingID();
//                            if (MyNamePanel.emailLabel != null) {
//                                MyNamePanel.emailLabel.setText(MyFnFSettings.userProfile.getEmail());
//                            }
                            GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().changeEmail();
                            if (createEmailAboutPanelPrivacy != null) {
                                createEmailAboutPanelPrivacy.valuelabel1.setText(this.email);
                                createEmailAboutPanelPrivacy.pleasW.setText("Not Verified");
                                createEmailAboutPanelPrivacy.isVerified = 0;
                                if (createEmailAboutPanelPrivacy.pleasW.getMouseListeners() == null || createEmailAboutPanelPrivacy.pleasW.getMouseListeners().length == 0) {
                                    createEmailAboutPanelPrivacy.pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                    createEmailAboutPanelPrivacy.pleasW.addMouseListener(createEmailAboutPanelPrivacy.mouseListener);
                                }
                            }
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                            if (createEmailAboutPanelPrivacy != null) {
                                createEmailAboutPanelPrivacy.privacyButton.setVisible(true);
                            }
                        } else {
                            //GuiRingID.getInstance().myProfilePanel.changEmail();
                            if (createEmailAboutPanelPrivacy != null) {
                                createEmailAboutPanelPrivacy.valuelabel1.setText(MyFnFSettings.userProfile.getEmail());
                                createEmailAboutPanelPrivacy.pleasW.setText(previous_str);
                            }
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                //loadingLabel.setText("Failed!!!");
                if (okButton != null) {
                    this.okButton.setEnabled(true);
                }
            } catch (Exception ex) {
                //  ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

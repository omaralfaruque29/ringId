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
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.view.myprofile.CreateEmailPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.myprofile.PhoneNumberUnitPanel;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Sirat Samyoun
 */
public class VerifySecondaryMailOrNumber extends Thread {

    private boolean success = false;
    private String email;
    private String code = null;
    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton sendButton;
    private JButton doneButton;
    private JButton cancelOrSkipButton;
    private CreateEmailPanel createEmailAboutPanelPrivacy = null;
    private PhoneNumberUnitPanel phoneNumberUnitPanel = null;
    String mblDc = null;
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VerifyPrimaryEmail.class);

    public VerifySecondaryMailOrNumber(String email,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton sendButton,
            JButton doneButton,
            JButton cancelButton) {
        this.email = email;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.sendButton = sendButton;
        this.doneButton = doneButton;
        this.cancelOrSkipButton = cancelButton;
        //this.createNew = createNew;
    }

    public VerifySecondaryMailOrNumber(String email,
            String code,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton sendButton,
            JButton doneButton,
            JButton cancelButton,
            CreateEmailPanel createEmailAboutPanelPrivacy) {
        this.email = email;
        this.code = code;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.sendButton = sendButton;
        this.doneButton = doneButton;
        this.cancelOrSkipButton = cancelButton;
        this.createEmailAboutPanelPrivacy = createEmailAboutPanelPrivacy;
    }

    public VerifySecondaryMailOrNumber(String email,
            String mblDc,
            String code,
            JLabel errorLabel,
            JTextArea errorTextArea,
            JButton sendButton,
            JButton doneButton,
            JButton cancelButton,
            PhoneNumberUnitPanel phoneNumberUnitPanel) {
        this.email = email;
        this.mblDc = mblDc;
        this.code = code;
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.sendButton = sendButton;
        this.doneButton = doneButton;
        this.cancelOrSkipButton = cancelButton;
        this.phoneNumberUnitPanel = phoneNumberUnitPanel;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {

                if (sendButton != null) {
                    sendButton.setEnabled(false);
                }
                if (doneButton != null) {
                    doneButton.setEnabled(false);
                }
                if (cancelOrSkipButton != null) {
                    cancelOrSkipButton.setEnabled(false);
                }
                if (errorLabel != null) {
                    errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                }
                if (errorTextArea != null) {
                    errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                    errorTextArea.setText("Please wait...");
                }

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_VERIFY_NUMBER_OR_EMAIL);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                js_fields.setAdditionalMblOrEml(email);
                if (mblDc != null) {
                    js_fields.setMobilePhoneDialingCode(mblDc);
                }
                if (code != null) {
                    js_fields.setVerificationCode(code);
                }

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        this.doneButton.setEnabled(true);
                        this.sendButton.setEnabled(true);
                        this.cancelOrSkipButton.setEnabled(true);
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            if (createEmailAboutPanelPrivacy != null) {
                                createEmailAboutPanelPrivacy.pleasW.setText("");
                                createEmailAboutPanelPrivacy.pleasW.setCursor(null);
                                createEmailAboutPanelPrivacy.pleasW.removeMouseListener(createEmailAboutPanelPrivacy.mouseListener);
                                createEmailAboutPanelPrivacy.isVerified = 1;
                            } else if (phoneNumberUnitPanel != null) {
                                phoneNumberUnitPanel.pleasW.setText("");
                                phoneNumberUnitPanel.pleasW.setCursor(null);
                                phoneNumberUnitPanel.pleasW.removeMouseListener(phoneNumberUnitPanel.mouseListener);
                                phoneNumberUnitPanel.isVerified = 1;
                            }
                            for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                                if (email.equals(ad.getNameOfEmail())) {
                                    ad.setIsVerified(1);
                                    break;
                                }
                            }
                            this.errorLabel.setIcon(null);
                            this.errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                            this.errorTextArea.setText(feedBackFields.getMessage());
                            if (code != null && code.length() > 0) {
                                cancelOrSkipButton.doClick();
                            }
                        } else {
                            if (code != null && code.length() > 0) {
                                this.errorLabel.setIcon(null);
                                this.errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                                this.errorTextArea.setText("");
                                HelperMethods.showConfirmationDialogMessage(feedBackFields.getMessage() + "\nDo you want to verify your E-mail again?");
                                if (JOptionPanelBasics.YES_NO) {
                                    return;
                                } else {
                                    cancelOrSkipButton.doClick();
                                }
                            } else {
                                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                                this.errorTextArea.setText(feedBackFields.getMessage());
                            }
                            if (sendButton != null) {
                                sendButton.setEnabled(true);
                            }
                            if (doneButton != null) {
                                doneButton.setEnabled(true);
                            }
                            if (cancelOrSkipButton != null) {
                                cancelOrSkipButton.setEnabled(true);
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                    //break;
                }
              //  MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                //  return;

            } catch (InterruptedException | HeadlessException ex) {
                log.error("ex" + ex.getMessage());
                this.doneButton.setEnabled(true);
                this.sendButton.setEnabled(true);
                this.cancelOrSkipButton.setEnabled(true);
                this.errorLabel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText("Failed! Try again");
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

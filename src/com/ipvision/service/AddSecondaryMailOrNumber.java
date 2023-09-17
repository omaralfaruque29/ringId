/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

/**
 *
 * @author Sirat Samyoun
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.view.myprofile.CreateEmailPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.EmailOrMobileVerificationDialog;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.myprofile.PhoneNumberUnitPanel;

public class AddSecondaryMailOrNumber extends Thread {

    private String mailOrNumber;
    private CreateEmailPanel createEmailAboutPanelPrivacy;
    private PhoneNumberUnitPanel phoneNumberUnitPanel;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddSecondaryMailOrNumber.class);

    public AddSecondaryMailOrNumber(String mailOrNumber, CreateEmailPanel createEmailAboutPanelPrivacy) {
        this.mailOrNumber = mailOrNumber;
        this.createEmailAboutPanelPrivacy = createEmailAboutPanelPrivacy;
    }

    public AddSecondaryMailOrNumber(String mailOrNumber, PhoneNumberUnitPanel phoneNumberUnitPanel) {
        this.mailOrNumber = mailOrNumber;
        this.phoneNumberUnitPanel = phoneNumberUnitPanel;
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
        if (phoneNumberUnitPanel != null) {
            phoneNumberUnitPanel.pleasW.setText("Saving " + string);
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (createEmailAboutPanelPrivacy != null) {
                    createEmailAboutPanelPrivacy.valuebox.setEnabled(false);
                    createEmailAboutPanelPrivacy.okButton.setEnabled(false);
                    createEmailAboutPanelPrivacy.closeButton.setEnabled(false);
                }
                if (phoneNumberUnitPanel != null) {
                    phoneNumberUnitPanel.countryChooseList.disablePanel();
                    phoneNumberUnitPanel.okButton.setEnabled(false);
                    phoneNumberUnitPanel.closeButton.setEnabled(false);
                }
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_VERIFY_NUMBER_OR_EMAIL);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                if (phoneNumberUnitPanel != null) {
                    String[] parts = mailOrNumber.split("-");
                    js_fields.setAdditionalMblOrEml(parts[1]);
                    js_fields.setMobilePhoneDialingCode(parts[0]);
                } else {
                    js_fields.setAdditionalMblOrEml(mailOrNumber);
                }
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
                            AdditionalInfoDTO add = new AdditionalInfoDTO();
                            add.setIsVerified(0);
                            add.setNameOfEmail(mailOrNumber);
                            if (MyFnFSettings.userProfile.getAdditionalInfo() != null) {
                                MyFnFSettings.userProfile.getAdditionalInfo().add(add);
                            } else {
                                List<AdditionalInfoDTO> adInfo = new ArrayList<>();
                                adInfo.add(add);
                                MyFnFSettings.userProfile.setAdditionalInfo(adInfo);
                            }
                            if (createEmailAboutPanelPrivacy != null) {
                                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEmailPanel().checkAddButtonVisibility();
                                createEmailAboutPanelPrivacy.valuebox.setEnabled(true);
                                createEmailAboutPanelPrivacy.okButton.setEnabled(true);
                                createEmailAboutPanelPrivacy.closeButton.setEnabled(true);
                                createEmailAboutPanelPrivacy.valuelabel1.setText(this.mailOrNumber);
                                createEmailAboutPanelPrivacy.pleasW.setText("Not Verified");
                                createEmailAboutPanelPrivacy.pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                createEmailAboutPanelPrivacy.pleasW.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        new EmailOrMobileVerificationDialog(mailOrNumber, createEmailAboutPanelPrivacy);
                                    }
                                });
                            }
                            if (phoneNumberUnitPanel != null) {
                                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getPhoneNumberPanel().checkAddButtonVisibility();
                                phoneNumberUnitPanel.action_save_secondary_phone();
                                phoneNumberUnitPanel.countryChooseList.enablePanel();
                                phoneNumberUnitPanel.okButton.setEnabled(true);
                                phoneNumberUnitPanel.closeButton.setEnabled(true);
                            }
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        } else {
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                            if (createEmailAboutPanelPrivacy != null) {
                                createEmailAboutPanelPrivacy.action_cancel_Button();
                            }
                            if (phoneNumberUnitPanel != null) {
                                phoneNumberUnitPanel.action_cancel_button();
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("failed to add" + ex.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }

    }
}

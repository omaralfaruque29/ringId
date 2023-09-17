/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

/**
 *
 * @author Sirat
 */
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

public class AddMoreMailsVerify extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddMoreMailsVerify.class);
    private String email;
    private JButton okButton;
    private JLabel valueLbl;
    private String code;

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }

//        return GuiRingID.getInstance().getMainRight() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    public AddMoreMailsVerify(String email, JButton okButton1, JLabel valueLbl, String code) {
        this.email = email;
        this.okButton = okButton1;
        this.valueLbl = valueLbl;
        this.code = code;
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
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setAction(AppConstants.TYPE_VERIFY_NUMBER_OR_EMAIL);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js_fields.setPacketId(pakId);
                js_fields.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                js_fields.setAdditionalMblOrEml(email);
                if (code != null) {
                    js_fields.setVerificationCode(code);
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
                            MyFnFSettings.userProfile.setIsEmailVerified(0);
                            AdditionalInfoDTO add = new AdditionalInfoDTO();
                            add.setIsVerified(0);
                            add.setNameOfEmail(email);
                            MyFnFSettings.userProfile.getAdditionalInfo().add(add);
                            //GuiRingID.getInstance().myProfilePanel.changEmail();
                            this.valueLbl.setText(this.email);
//                        createEmailAboutPanelPrivacy.pleasW.setText("Not Verified");
//                        createEmailAboutPanelPrivacy.pleasW.setCursor(new Cursor(Cursor.HAND_CURSOR));
//                        createEmailAboutPanelPrivacy.pleasW.addMouseListener(new MouseAdapter() {
//                            @Override
//                            public void mouseClicked(MouseEvent e) {
//                                new EmailVerificationDialog(email, createEmailAboutPanelPrivacy);
//                            }
//                        });
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        } else {
//                        createEmailAboutPanelPrivacy.pleasW.setText(previous_str);
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                            getMainRight().getMyProfilePanel().getMyProfileAbout().email.remove(getMainRight().getMyProfilePanel().getMyProfileAbout().email.getComponentCount() - 1);
                            getMainRight().getMyProfilePanel().getMyProfileAbout().gbc.gridy--;
                            getMainRight().getMyProfilePanel().getMyProfileAbout().email.revalidate();;
                            getMainRight().getMyProfilePanel().getMyProfileAbout().email.repaint();
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        //loadingLabel.setText("Not Verified");
                        return;
                    }
                }
                //loadingLabel.setText("Failed!!!");
                this.okButton.setEnabled(true);
            } catch (Exception ex) {
                //ex.printStackTrace();
                log.error("Error in AddMoreMailsVerify class ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

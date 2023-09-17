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
import javax.swing.JLabel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class ChangeWhatInMind extends Thread {

    private JLabel loadingLabel;
    private String text;
    private JsonFields jF;

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

    public ChangeWhatInMind(JLabel loadJLabel, String change, int field) {
        this.loadingLabel = loadJLabel;
        this.text = change;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                //  System.out.println("running....ChangeWhatInMind");
                String pak_id = SendToServer.getRanDomPacketID();
                jF = new JsonFields();
                jF.setAction(AppConstants.TYPE_WHAT_IS_IN_UR_MIND);
                jF.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                jF.setPacketId(pak_id);
                jF.setWhatisInYourMind(this.text);
                SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                for (int i = 1; i <= 5; i++) {
                    setMessage(i);
                    Thread.sleep(500);
                    /*
                     if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().isEmpty() || MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id) == null) {
                     SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);

                     } else {
                     JsonFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id);
                     if (js.getSuccess()) {
                     */
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().isEmpty() || MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id) == null) {
                        SendToServer.sendPacketAsString(jF, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields js = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pak_id);
                        if (js.getSuccess()) {
                            MyFnFSettings.userProfile.setWhatisInYourMind(this.text);
                        // MyProfileDetails.getMyProfileDetails().changWhatinMind();
                            //GuiRingID.getInstance().myProfilePanel.changWhatinMind();
                        } else {
                            loadingLabel.setText("Failed!!!" + js.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pak_id);
                        loadingLabel.setText("");
                        return;
                    }
                }
                loadingLabel.setText("Failed!!!");
            } catch (Exception e) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.view.loginsignup.SuggestionPanel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Wasif Islam
 */
public class RecoverySuggestionRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RecoverySuggestionRequest.class);
    private String ringId_phone_mail;
    private JButton sgtnButton;
    private JButton cancelButton;
    private JPanel sgtnPanel;
    private JLabel loading = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);

    public RecoverySuggestionRequest(String ringId_phone_mail, JPanel sgtnPanel, JButton sgtnButton, JButton cancelButton) {
        this.ringId_phone_mail = ringId_phone_mail;
        this.sgtnPanel = sgtnPanel;
        this.sgtnButton = sgtnButton;
        this.cancelButton = cancelButton;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (this.sgtnButton != null) {
                    this.sgtnButton.setEnabled(false);
                }
                if (this.cancelButton != null) {
                    this.cancelButton.setEnabled(false);
                }
                if (this.sgtnPanel != null) {
                    this.sgtnPanel.removeAll();
                    this.sgtnPanel.add(loading);
                    this.sgtnPanel.revalidate();
                    this.sgtnPanel.repaint();
                }

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_RECOVERY_SUGGESTION);
                js_fields.setRecoveryBy(this.ringId_phone_mail);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(300);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.AUTHENTICATION_PORT);
                    } else {
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            if (MyfnfHashMaps.getInstance().getTempRecoverySuggestionsMap().get(pakId) != null) {
                                this.sgtnPanel.removeAll();
                                JPanel panel = new SuggestionPanel(MyfnfHashMaps.getInstance().getTempRecoverySuggestionsMap().get(pakId));
                                this.sgtnPanel.add(panel);
                                this.sgtnPanel.revalidate();
                                this.sgtnPanel.repaint();
                                MyfnfHashMaps.getInstance().getTempRecoverySuggestionsMap().clear();
                            }
                        } else {
                            this.sgtnPanel.removeAll();
                            this.sgtnPanel.revalidate();
                            this.sgtnPanel.repaint();
                            HelperMethods.showWarningDialogMessage(feedBackFields.getMessage());
                        }
                        break;
                    }
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                }
                if (sgtnButton != null) {
                    sgtnButton.setEnabled(true);
                }
                if (cancelButton != null) {
                    cancelButton.setEnabled(true);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                log.error("Exception in RecoverySuggestionRequest ==>" + e.getMessage());
                this.sgtnButton.setEnabled(true);
                this.cancelButton.setEnabled(true);
                HelperMethods.showWarningDialogMessage("Failed!");
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
            this.sgtnButton.setEnabled(true);
            this.cancelButton.setEnabled(true);
        }
    }
}

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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class DeleteWorkInfo extends Thread {

    private JLabel errorLabel;
    private Long workInfoID;
    private Map<Long, WorkInfoDTO> map;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteWorkInfo.class);

    public DeleteWorkInfo(Long workInfoID, JLabel errorLabel) {
        this.errorLabel = errorLabel;
        this.workInfoID = workInfoID;
        this.map = new HashMap<>();
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_REMOVE_WORK);
                js_fields.setId(workInfoID.toString());
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);

                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
//                setMessage(i);
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (feedBackFields.getSuccess()) {
                            map = MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
                            for (long key : map.keySet()) {
                                WorkInfoDTO w = map.get(key);
                                if (w.getWorkInfoID() == workInfoID) {
                                    map.remove(workInfoID);
                                    break;
                                }
                            }

                            /*for (WorkInfoDTO w : MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID)) {
                             if (w.getWorkInfoID() == workInfoID) {
                             MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID).remove(w);
                             break;
                             }
                             }*/
                            JPanel workDetailsPanel = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getWorkPanel().getWorkDetailsPanel();
                            for (int j = 0; j < workDetailsPanel.getComponentCount(); j++) {
                                if (workDetailsPanel.getComponent(j).getName() != null && workDetailsPanel.getComponent(j).getName().equalsIgnoreCase(workInfoID.toString())) {
                                    workDetailsPanel.remove(j);
                                    break;
                                }
                            }
                            if (workDetailsPanel.getComponentCount() > 0) {
                                JPanel jp = (JPanel) workDetailsPanel.getComponent(workDetailsPanel.getComponentCount() - 1);
                                jp.setBorder(null);
                            }
                            workDetailsPanel.revalidate();
                            workDetailsPanel.repaint();
                        } else {
//                        errorLabel.setText("Sorry! Failed to delete work");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                //           errorLabel.setText("Sorry! Failed to delete work");

            } catch (Exception ex) {
                //  ex.printStackTrace();
//            errorLabel.setText("Sorry! Failed to add work");
                log.error("failed to add works" + ex.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

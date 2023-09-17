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
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class DeleteEducationInfo extends Thread {

    private JLabel errorLabel;
    private Long educationInfoID;
    private JPanel educationDetailsPanel;
    private boolean isSchool;
    private Map<Long, EducationInfoDTO> map;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteEducationInfo.class);

    public DeleteEducationInfo(Long educationInfoID, JLabel errorLabel) {
        this.errorLabel = errorLabel;
        this.educationInfoID = educationInfoID;
        this.map = new HashMap<>();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_REMOVE_EDUCATION);
                js_fields.setId(educationInfoID.toString());
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
                            map = MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
                            for (long key : map.keySet()) {
                                EducationInfoDTO e = map.get(key);
                                if (e.getEducationId().equals(educationInfoID)) {
                                    isSchool = e.getIsSchool();
                                    map.remove(educationInfoID);
                                    break;
                                }
                            }
                            if (isSchool) {
                                educationDetailsPanel = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().getSchoolDetailsPanel();
                            } else {
                                educationDetailsPanel = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().getClgDetailsPanel();
                            }

                            for (int j = 0; j < educationDetailsPanel.getComponentCount(); j++) {
                                if (educationDetailsPanel.getComponent(j).getName() != null && educationDetailsPanel.getComponent(j).getName().equalsIgnoreCase(educationInfoID.toString())) {
                                    educationDetailsPanel.remove(j);
                                    break;
                                }
                            }
                            if (!isSchool) {
                                if (educationDetailsPanel.getComponentCount() > 0) {
                                    JPanel jp = (JPanel) educationDetailsPanel.getComponent(educationDetailsPanel.getComponentCount() - 1);
                                    jp.setBorder(null);
                                } else if (educationDetailsPanel.getComponentCount() == 0) {
                                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().topPanel3.setBorder(null);
                                }
                            }
                            educationDetailsPanel.revalidate();
                            educationDetailsPanel.repaint();
                        } else {
//                        errorLabel.setText("Sorry! Failed to delete work");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                //           errorLabel.setText("Sorry! Failed to delete work");

            } catch (Exception ex) {
                //   ex.printStackTrace();
//            errorLabel.setText("Sorry! Failed to add work");
                log.error("failed to add works" + ex.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

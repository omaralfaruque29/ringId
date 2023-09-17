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
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class DeleteSkillInfo extends Thread {

    private JLabel errorLabel;
    private Long skillInfoID;
    private Map<Long, SkillInfoDTO> map;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteSkillInfo.class);

    public DeleteSkillInfo(Long skillInfoID, JLabel errorLabel) {
        this.errorLabel = errorLabel;
        this.skillInfoID = skillInfoID;
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
                js_fields.setAction(AppConstants.TYPE_REMOVE_SKILL);
                js_fields.setId(skillInfoID.toString());
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
                            map = MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
                            for (long key : map.keySet()) {
                                SkillInfoDTO s = map.get(key);
                                if (s.getSkillInfoID() == skillInfoID) {
                                    map.remove(skillInfoID);
                                    break;
                                }
                            }
                            JPanel skillDetailsPanel = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getSkillPanel().getSkillDetailsPanel();
                            for (int j = 0; j < skillDetailsPanel.getComponentCount(); j++) {
                                if (skillDetailsPanel.getComponent(j).getName() != null && skillDetailsPanel.getComponent(j).getName().equalsIgnoreCase(skillInfoID.toString())) {
                                    skillDetailsPanel.remove(j);
                                    break;
                                }
                            }
                            if (skillDetailsPanel.getComponentCount() > 0) {
                                JPanel jp = (JPanel) skillDetailsPanel.getComponent(skillDetailsPanel.getComponentCount() - 1);
                                jp.setBorder(null);
                            }
                            skillDetailsPanel.revalidate();
                            skillDetailsPanel.repaint();
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
                log.error("failed to add skills" + ex.getMessage());

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

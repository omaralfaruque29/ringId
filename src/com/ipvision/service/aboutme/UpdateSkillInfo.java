/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.aboutme;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.view.myprofile.SkillSinglePanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class UpdateSkillInfo extends Thread {

    private String skl;
    private String desc;
    private JLabel errorLabel;
    private Long skillInfoID;
    private SkillSinglePanel instance;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SkillSinglePanel.class);
    private Map<Long, SkillInfoDTO> map;

    public UpdateSkillInfo(SkillSinglePanel instance) {
        this.instance = instance;
        this.skl = instance.skillField.getText();
        this.desc = instance.descArea.getText();
        this.errorLabel = instance.errorLabel;
        this.skillInfoID = instance.skillInfoID;
        this.map = new HashMap<>();
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
        errorLabel.setText("Saving " + string);
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                instance.skillField.setEditable(false);
                instance.descArea.setEditable(false);
                instance.saveButton.setEnabled(false);
                instance.cancelButton.setEnabled(false);
                SkillInfoDTO skill = new SkillInfoDTO();
                skill.setSkillInfoID(skillInfoID);
                skill.setSkill(skl);
                skill.setDescription(desc);

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_UPDATE_SKILL);
                js_fields.setSkillInfo(skill);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);

                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.UPDATE_PORT);

                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
                    setMessage(i);
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
                                    s.setSkill(skl);
                                    s.setDescription(desc);
                                    break;
                                }
                            }
                            instance.setVariables(skill.getSkillInfoID());
                            instance.showUpdatedInfo();
                        } else {
                            errorLabel.setText("Sorry! Failed to add skills");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        instance.skillField.setEditable(false);
                        instance.descArea.setEditable(false);
                        instance.saveButton.setEnabled(false);
                        instance.cancelButton.setEnabled(false);
                        return;
                    }
                }
                errorLabel.setText("Sorry! Failed to add skills");

            } catch (Exception ex) {
               // ex.printStackTrace();
                errorLabel.setText("Sorry! Failed to add skills");
                log.error("failed to add skills" + ex.getMessage());
                instance.skillField.setEditable(false);
                instance.descArea.setEditable(false);
                instance.saveButton.setEnabled(false);
                instance.cancelButton.setEnabled(false);

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

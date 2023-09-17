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
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.view.myprofile.EducationSinglePanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class UpdateEducationInfo extends Thread {

    private EducationSinglePanel instance;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateEducationInfo.class);
    private Map<Long, EducationInfoDTO> map;

    public UpdateEducationInfo(EducationSinglePanel instance) {
        this.instance = instance;
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
        instance.errorLabel.setText("Saving " + string);

    }

    public void disableInputControls() {
        instance.institutionTextField.setEditable(false);
        instance.currentlyStudying.setEnabled(false);
        instance.datePanel1.disableBoxes();
        if (!instance.currentlyStudying.isSelected()) {
            instance.datePanel2.disableBoxes();
        }
        instance.graduated.setEnabled(false);
        instance.descArea.setEditable(false);
        if (!instance.isSchool) {
            instance.concentrationTextField.setEditable(false);
            instance.attendedClgRadioButton.setEnabled(false);
            instance.attendedGradSchlRadioButton.setEnabled(false);
        }
        instance.saveButton.setEnabled(false);
        instance.cancelButton.setEnabled(false);
    }

    public void enableInputControls() {
        instance.institutionTextField.setEditable(true);
        instance.currentlyStudying.setEnabled(true);
        instance.datePanel1.enableBoxes();
        if (!instance.currentlyStudying.isSelected()) {
            instance.datePanel2.enableBoxes();
        }
        instance.graduated.setEnabled(true);
        instance.descArea.setEditable(true);
        if (!instance.isSchool) {
            instance.concentrationTextField.setEditable(true);
            instance.attendedClgRadioButton.setEnabled(true);
            instance.attendedGradSchlRadioButton.setEnabled(true);
        }
        instance.saveButton.setEnabled(true);
        instance.cancelButton.setEnabled(true);
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                disableInputControls();

                EducationInfoDTO education = new EducationInfoDTO();
                education.setEducationId(instance.educationInfoID);
                education.setSchool(instance.institutionTextField.getText());
                education.setDescription(instance.descArea.getText());
                education.setFromTime(instance.datePanel1.getDateinMillisecs());
                if (instance.currentlyStudying.isSelected()) {
                    education.setToTime(0L);
                } else {
                    education.setToTime(instance.datePanel2.getDateinMillisecs());
                }
                education.setGraduated(instance.graduated.isSelected());
                if (instance.isSchool) {
                    education.setIsSchool(true);
                    education.setAttendedFor(1);
                    education.setConcentration("");
                } else {
                    education.setIsSchool(false);
                    if (instance.attendedClgRadioButton.isSelected()) {
                        education.setAttendedFor(1);
                    } else {
                        education.setAttendedFor(2);
                        if (instance.degreeTextField.getText().trim().length() > 0) {
                            education.setDegree(instance.degreeTextField.getText());
                        }
                    }
                    education.setConcentration(instance.concentrationTextField.getText());
                }

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_UPDATE_EDUCATION);
                js_fields.setEducationInfo(education);
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
                            map = MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
                            for (long key : map.keySet()) {
                                EducationInfoDTO e = map.get(key);
                                if (e.getEducationId() == instance.educationInfoID) {
                                    e.setSchool(instance.institutionTextField.getText());
                                    e.setDescription(instance.descArea.getText());
                                    e.setFromTime(instance.datePanel1.getDateinMillisecs());
                                    if (instance.currentlyStudying.isSelected()) {
                                        e.setToTime(0L);
                                    } else {
                                        e.setToTime(instance.datePanel2.getDateinMillisecs());
                                    }
                                    e.setGraduated(instance.graduated.isSelected());
                                    if (instance.isSchool) {
                                        e.setIsSchool(true);
                                        e.setAttendedFor(1);
                                        e.setConcentration("");
                                    } else {
                                        e.setIsSchool(false);
                                        if (instance.attendedClgRadioButton.isSelected()) {
                                            e.setAttendedFor(1);
                                        } else {
                                            e.setAttendedFor(2);
                                        }
                                        e.setConcentration(instance.concentrationTextField.getText());
                                    }
                                    break;
                                }
                            }

                            instance.setVariables(education.getEducationId());
                            instance.showUpdatedInfo();
                        } else {
                            instance.errorLabel.setText("Sorry! Failed to update edu");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        enableInputControls();
                        return;
                    }
                }
                instance.errorLabel.setText("Sorry! Failed to update edu");

            } catch (Exception ex) {
              //  ex.printStackTrace();
                instance.errorLabel.setText("Sorry! Failed to update edu");
                log.error("failed to update edu" + ex.getMessage());
                enableInputControls();

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

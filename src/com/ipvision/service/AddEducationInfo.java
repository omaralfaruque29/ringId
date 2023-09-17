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
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.view.myprofile.CreateEducationSinglePanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class AddEducationInfo extends Thread {

    private CreateEducationSinglePanel instance;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddEducationInfo.class);

    public AddEducationInfo(CreateEducationSinglePanel instance) {
        this.instance = instance;
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
        if (instance.type_int == 1) {
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
        if (instance.type_int == 1) {
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
                education.setSchool(instance.institutionTextField.getText());
                education.setDescription(instance.descArea.getText());
                education.setFromTime(instance.datePanel1.getDateinMillisecs());
                if (instance.currentlyStudying.isSelected()) {
                    education.setToTime(0L);
                } else {
                    education.setToTime(instance.datePanel2.getDateinMillisecs());
                }
                education.setGraduated(instance.graduated.isSelected());
                if (instance.type_int == 0) {
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
                js_fields.setAction(AppConstants.TYPE_ADD_EDUCATION);
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
                        if (!feedBackFields.getSuccess()) {
                            instance.errorLabel.setText("Sorry! Failed to add work");
                            break;
                        } else {
                            break;
                        }
                    }
                }
                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                enableInputControls();

            } catch (Exception ex) {
               // ex.printStackTrace();
                instance.errorLabel.setText("Sorry! Failed to add Education");
                log.error("failed to add Education" + ex.getMessage());
                enableInputControls();

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

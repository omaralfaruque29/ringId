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
import javax.swing.JLabel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.view.myprofile.CreateWorkSinglePanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.GuiRingID;

/**
 *
 * @author Sirat Samyoun
 */
public class AddWorkInfo extends Thread {

    private String cnm;
    private String pstn;
    private String ct;
    private String desc;
    private long ft;
    private long tt;
    private JLabel errorLabel;
    private CreateWorkSinglePanel instance;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddWorkInfo.class);

    public AddWorkInfo(CreateWorkSinglePanel instance) {
        this.instance = instance;
        this.cnm = instance.companyField.getText();
        this.pstn = instance.positionField.getText();
        this.ct = instance.cityField.getText();
        this.desc = instance.descArea.getText();
        this.ft = instance.datePanel1.getDateinMillisecs();
        if (instance.isCurrentWork) {
            this.tt = 0;
        } else {
            this.tt = instance.datePanel2.getDateinMillisecs();
        }
        this.errorLabel = instance.errorLabel;
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
                instance.companyField.setEditable(false);
                instance.positionField.setEditable(false);
                instance.cityField.setEditable(false);
                instance.descArea.setEditable(false);
                instance.saveButton.setEnabled(false);
                instance.cancelButton.setEnabled(false);
                instance.currentlyWorking.setEnabled(false);
                WorkInfoDTO work = new WorkInfoDTO();
                work.setCompanyName(cnm);
                work.setDesignation(pstn);
                work.setCity(ct);
                work.setDescription(desc);
                work.setFromtime(ft);
                work.setTotime(tt);

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_ADD_WORK);
                js_fields.setWorkInfo(work);
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
                            errorLabel.setText("Sorry! Failed to add work");
                            break;
                        } else {
                            // if (instance == GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().createWorkSinglePanelInstance) {
//                        if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID) != null && !MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID).isEmpty()) {
//                            MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID).add(work);
//                        } else {
//                            List<WorkInfoDTO> workList = new ArrayList<WorkInfoDTO>();
//                            workList.add(work);
//                            MyfnfHashMaps.getInstance().getWorkInfoMap().put(MyFnFSettings.LOGIN_USER_ID, workList);
//                        }
                            break;
                        }
                    }
                }
                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                instance.companyField.setEditable(true);
                instance.positionField.setEditable(true);
                instance.cityField.setEditable(true);
                instance.descArea.setEditable(true);
                instance.saveButton.setEnabled(true);
                instance.cancelButton.setEnabled(true);
                instance.currentlyWorking.setEnabled(true);
                // errorLabel.setText("Sorry! Failed to add work");

            } catch (Exception ex) {
                //  ex.printStackTrace();
                errorLabel.setText("Sorry! Failed to add work");
                log.error("failed to add works" + ex.getMessage());
                instance.companyField.setEditable(true);
                instance.positionField.setEditable(true);
                instance.cityField.setEditable(true);
                instance.descArea.setEditable(true);
                instance.saveButton.setEnabled(true);
                instance.cancelButton.setEnabled(true);
                instance.currentlyWorking.setEnabled(true);

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

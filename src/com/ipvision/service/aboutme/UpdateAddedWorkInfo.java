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
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.view.myprofile.CreateWorkSinglePanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class UpdateAddedWorkInfo extends Thread {

    private String cnm;
    private String pstn;
    private String ct;
    private String desc;
    private long ft;
    private long tt;
    private JLabel errorLabel;
    private Long workInfoID;
    private CreateWorkSinglePanel instance;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateAddedWorkInfo.class);
    private Map<Long, WorkInfoDTO> map;

    public UpdateAddedWorkInfo(CreateWorkSinglePanel instance) {
        this.instance = instance;
        this.cnm = instance.companyField.getText();
        this.pstn = instance.positionField.getText();
        this.ct = instance.cityField.getText();
        this.desc = instance.descArea.getText();
        this.ft = instance.datePanel1.getDateinMillisecs();
        this.tt = instance.datePanel2.getDateinMillisecs();
        this.errorLabel = instance.errorLabel;
        this.workInfoID = instance.workInfoID;
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
                instance.companyField.setEditable(false);
                instance.positionField.setEditable(false);
                instance.cityField.setEditable(false);
                instance.descArea.setEditable(false);
                instance.saveButton.setEnabled(false);
                instance.cancelButton.setEnabled(false);
                instance.currentlyWorking.setEnabled(false);
                WorkInfoDTO work = new WorkInfoDTO();
                work.setWorkInfoID(workInfoID);
                work.setCompanyName(cnm);
                work.setDesignation(pstn);
                work.setCity(ct);
                work.setDescription(desc);
                work.setFromtime(ft);
                work.setTotime(tt);

                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_UPDATE_WORK);
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
                        if (feedBackFields.getSuccess()) {
                            map = MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
                            for (long key : map.keySet()) {
                                WorkInfoDTO w = map.get(key);
                                if (w.getWorkInfoID() == workInfoID) {
                                //MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID).remove(w);
                                    //MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_ID).add(work);
                                    w.setCompanyName(cnm);
                                    w.setDesignation(pstn);
                                    w.setCity(ct);
                                    w.setDescription(desc);
                                    w.setFromtime(ft);
                                    w.setTotime(tt);
                                    break;
                                }
                            }
                            instance.setVariables(work.getWorkInfoID());
                            instance.showUpdatedInfo();

                        } else {
                            errorLabel.setText("Sorry! Failed to add work");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        instance.companyField.setEditable(true);
                        instance.positionField.setEditable(true);
                        instance.cityField.setEditable(true);
                        instance.descArea.setEditable(true);
                        instance.saveButton.setEnabled(true);
                        instance.cancelButton.setEnabled(true);
                        instance.currentlyWorking.setEnabled(true);
                        return;
                    }
                }
                errorLabel.setText("Sorry! Failed to add work");

            } catch (Exception ex) {
               // ex.printStackTrace();
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

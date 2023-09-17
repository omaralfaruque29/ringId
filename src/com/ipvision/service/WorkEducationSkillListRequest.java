/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class WorkEducationSkillListRequest extends Thread {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WorkEducationSkillListRequest.class);

    private Long utid;

    public WorkEducationSkillListRequest() {
        setName(this.getClass().getSimpleName());
    }

    public WorkEducationSkillListRequest(Long utid) {
        setName(this.getClass().getSimpleName());
        this.utid = utid;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields js_fields = new JsonFields();
                js_fields.setPacketId(pakId);
                js_fields.setAction(AppConstants.TYPE_LIST_WORKS_EDUCATIONS_SKILLS);
                js_fields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                if (utid != null) {
                    js_fields.setUserTabelId(utid);
                }
                SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);

                Thread.sleep(25);
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js_fields, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        //{"pckId":"14210411753202000000005","actn":230,"sId":"14210411630372000000005"}
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }

            } catch (Exception ex) {
                //ex.printStackTrace();
                //   errorLabel.setText("Sorry! Failed to add work");
                log.error("failed to list work-educaiton-skill" + ex.getMessage());

            }
        }
    }
}

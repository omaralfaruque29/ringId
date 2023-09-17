/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StaticFields;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.dao.DatabaseActivityDAO;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Shahadat
 */
public class LoadUserBasicInformation {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadUserBasicInformation.class);
    private JsonFields fields;

    public LoadUserBasicInformation(JsonFields fields) {
        this.fields = fields;
    }

    public void loadInformation() {
        try {
            Statement stmt = null;
            try {
                if (DBConstants.conn != null) {
                    stmt = DBConstants.conn.createStatement();
                    DatabaseActivityDAO.getInstance().createFriendChatTable(stmt);
                    DatabaseActivityDAO.getInstance().fetchRingUserSettings(stmt);
                    DatabaseActivityDAO.getInstance().fetchContractList(stmt);
                    DatabaseActivityDAO.getInstance().fetchGroupList(stmt);
                    DatabaseActivityDAO.getInstance().fetchRingMarketStickerCategoriesList(stmt);
                    DatabaseActivityDAO.getInstance().fetchCircleList(stmt);
                    NotificationHistoryDAO.loadMaxTimeFromNotificationHistory();
                    NotificationHistoryDAO.loadMinTimeFromNotificationHistory();
                } else {
                    log.error(StaticFields.APP_NAME + " has stopped working. please try again");
                    HelperMethods.showWarningDialogMessage(StaticFields.APP_NAME + " has stopped working. please try again ");
                    System.exit(0);
                }
            } catch (SQLException ex) {
                
                log.error("Error occured during Loading UserBasicInformation from Database. Exception :: " + ex.getMessage());
            } finally {
                try {
                    stmt.close();
                } catch (Exception e) {
                    log.error("Error occured during Loading UserBasicInformation from Database. Exception :: " + e.getMessage());
                }
            }
        } catch (Exception e) {
           // e.printStackTrace();
            log.error("System error ==>" + e.getMessage());
        }
    }

}

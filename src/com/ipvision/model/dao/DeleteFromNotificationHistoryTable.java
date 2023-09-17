/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author raj
 */
public class DeleteFromNotificationHistoryTable extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteFromNotificationHistoryTable.class);
    private String id;

    public DeleteFromNotificationHistoryTable(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM " + DBConstants.TABLE_RING_NOTIFICATION_HISTORY
                            + " WHERE "
                            + DBConstants.ID + "='" + id + "' AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    stmt.executeUpdate(query);

                } catch (Exception e) {
                   // e.printStackTrace();
                log.error("Error in TABLE_RING_NOTIFICATION_HISTORY ==>" + e.getMessage());    
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                       // ex.printStackTrace();
                        log.error("SQLException in DeleteFromNotificationHistoryTable while deleting notification ==>" + ex.getMessage()); 
                    }
                }
            }

        } catch (SQLException ex) {
        }
    }

}

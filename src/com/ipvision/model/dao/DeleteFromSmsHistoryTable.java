/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author Shahadat Hossain
 */
public class DeleteFromSmsHistoryTable extends Thread {

    private Set<Long> ids;

    public DeleteFromSmsHistoryTable(Set<Long> ids) {
        this.ids = ids;
    }

    @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    for (Long id : ids) {
                        String query = "DELETE FROM  " + DBConstants.TABLE_RING_SMS_HISTORY + " WHERE " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND " + DBConstants.ID + " = " + id;
                        stmt.executeUpdate(query);
                    }
                } catch (Exception e) {
                    
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (SQLException ex) {
        }
    }
}

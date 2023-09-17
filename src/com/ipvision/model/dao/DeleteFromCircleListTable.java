/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author Shahadat Hossain
 */
public class DeleteFromCircleListTable extends Thread {

    private long circleId;

    public DeleteFromCircleListTable(long circleId) {
        this.circleId = circleId;
    }

    @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_LIST
                            + " WHERE "
                            + DBConstants.CIRCLE_ID + "=" + circleId + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                            + " WHERE " + DBConstants.CIRCLE_ID + "=" + circleId + " AND " 
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    stmt.executeUpdate(query);
                    stmt.executeUpdate(query1);
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

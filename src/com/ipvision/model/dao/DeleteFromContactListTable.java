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
public class DeleteFromContactListTable extends Thread {

    private String uesrId;

    public DeleteFromContactListTable(String userId) {
        this.uesrId = userId;
    }

    @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_CONTACT_LIST
                            + " WHERE "
                            + DBConstants.USER_IDENTITY + "='" + uesrId + "' AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID 
                            + " WHERE "
                            + DBConstants.FRIEND_IDENTITY + "='" + uesrId + "'";
                    
                    String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_CALL_LOG 
                            + " WHERE "
                            + DBConstants.FRIEND_IDENTITY + "='" + uesrId + "' AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    String query3 = "DELETE FROM  " + DBConstants.TABLE_RING_ACTIVITY
                            + " WHERE "
                            + DBConstants.FRIEND_IDENTITY + "='" + uesrId + "' AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    stmt.executeUpdate(query);
                    stmt.executeUpdate(query1);
                    stmt.executeUpdate(query2);
                    stmt.executeUpdate(query3);
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

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
 * @author Wasif Islam
 */
public class DeleteFromGroupListTable extends Thread {

    private long groupId;

    public DeleteFromGroupListTable(long groupId) {
        this.groupId = groupId;
    }
    
        @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_LIST 
                            + " WHERE " 
                            + DBConstants.GROUP_ID + "=" + groupId + " AND " 
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST 
                            + " WHERE " 
                            + DBConstants.GROUP_ID + "=" + groupId + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                            + " WHERE " 
                            + DBConstants.GROUP_ID + "=" + groupId + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    
                    String query3 = "DELETE FROM  " + DBConstants.TABLE_RING_ACTIVITY
                            + " WHERE "
                            + DBConstants.GROUP_ID + "=" + groupId + " AND "
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

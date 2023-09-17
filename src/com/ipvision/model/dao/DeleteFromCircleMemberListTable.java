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
 * @author Faiz Ahmed
 */
public class DeleteFromCircleMemberListTable extends Thread {

    private long circleId;
    private String userId;

    public DeleteFromCircleMemberListTable(long circleId, String userId) {
        this.circleId = circleId;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "";
                    if(userId == null || userId.trim().equalsIgnoreCase("")){
                        query = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST + " WHERE " + DBConstants.CIRCLE_ID + "=" + circleId 
                                + " AND " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    }else{
                        query = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST + " WHERE " + DBConstants.CIRCLE_ID + "=" + circleId 
                                + " AND " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'" 
                                + " AND " + DBConstants.USER_IDENTITY + "='" + userId + "'";
                    }
                    stmt.executeUpdate(query);
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
            //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

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
public class DeleteFromGroupMemberListTable extends Thread {
    private long groupId;
    private String uId;
    
    public DeleteFromGroupMemberListTable(long groupId, String uId){
        this.groupId = groupId;
        this.uId = uId;
    }
    
            @Override
    public void run() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST 
                            + " WHERE " 
                            + DBConstants.GROUP_ID + "=" + groupId + " AND "
                            + DBConstants.USER_IDENTITY + " = '" + uId + "' AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    stmt.executeUpdate(query);
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

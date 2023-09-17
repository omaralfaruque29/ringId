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
public class UpdateCircleMember extends Thread {

    private long cirlceId;
    private String uesrid;
    private boolean admin;

    public UpdateCircleMember(long circleId, String userId, boolean admin) {
        this.cirlceId = circleId;
        this.uesrid = userId;
        this.admin = admin;
    }

    @Override
    public void run() {
        if (DBConstants.conn != null) {
            try {

                Statement stmt = null;
                try {
                    stmt = DBConstants.conn.createStatement();
                    String sql = "UPDATE " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                            + " SET "
                            + DBConstants.ADMIN + " =" + this.admin + ""
                            + " WHERE "
                            + DBConstants.CIRCLE_ID + " = " + cirlceId + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                            + DBConstants.USER_IDENTITY + " = '" + uesrid + "'";

                    //  System.out.println("sql==>" + sql);
                    stmt.executeUpdate(sql);

                } catch (SQLException sqlExcept) {
                    //   System.out.println("sqlExcept.getSQLState()==>" + sqlExcept.getSQLState());
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException ex) {
                        //    Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (Exception e) {
            }

        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;

/**
 * @author Shahadat Hossain
 */
public class RingUserSettingsDAO {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RingUserSettingsDAO.class);

    public static long getLastLoggedInTime() {
        Statement stmt = null;
        long lastLoginTime = 0;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                try {
                    String query = "SELECT " + DBConstants.LAST_LOGIN_TIME + " FROM  " + DBConstants.TABLE_RING_USER_SETTINGS
                            + " WHERE " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);

                    if (results.next()) {
                        lastLoginTime = results.getLong(1);
                    }

                } catch (SQLException ex) {
                    // ex.printStackTrace();
                    log.error("Error in RingUserSettingsDAO class while selecting LAST_LOGIN_TIME ==>" + ex.getMessage());
                }

            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
            }
        }

        return lastLoginTime;

    }
}

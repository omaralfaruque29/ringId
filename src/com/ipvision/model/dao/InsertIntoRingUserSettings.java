/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoRingUserSettings extends Thread {

    @Override
    public void run() {
        insertValue();
    }

    synchronized public void insertValue() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:" + DBConstants.DB_NAME + ";create=true";

        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();

            String chat_bg = ((SettingsConstants.FNF_CHAT_BG != null && SettingsConstants.FNF_CHAT_BG.length() > 0) ? SettingsConstants.FNF_CHAT_BG : SettingsConstants.FNF_DEAFULT_CHAT_BG);

            try {
                String sql = "INSERT INTO " + DBConstants.TABLE_RING_USER_SETTINGS
                        + "	(" + DBConstants.LOGIN_USER_ID + "," + DBConstants.USERINFO_UT + "," + DBConstants.GROUP_UT + "," + DBConstants.CONTACT_UT + "," + DBConstants.CIRCLE_UT + "," + DBConstants.CIRCLE_MEMBER_UT + "," + DBConstants.CALL_LOG_UT + "," + DBConstants.SMS_LOG_UT + "," + DBConstants.LAST_LOGIN_TIME + "," + DBConstants.CHAT_BG + ")\n"
                        + "VALUES "
                        + "	("
                        + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                        + " " + SettingsConstants.FNF_USERINFO_UT + ", "
                        + " " + SettingsConstants.FNF_GROUP_UT + ", "
                        + " " + SettingsConstants.FNF_CONTACT_UT + ", "
                        + " " + SettingsConstants.FNF_CIRCLE_UT + ", "
                        + " " + SettingsConstants.FNF_CIRCLE_MEMBER_UT + ", "
                        + " " + SettingsConstants.FNF_CALL_LOG_UT + ", "
                        + " " + SettingsConstants.FNF_SMS_LOG_UT + ", "
                        + " " + SettingsConstants.FNF_LAST_LOGIN_TIME + ", "
                        + " '" + chat_bg + "' "
                        + ")";
                    stmt.execute(sql);
            } catch (SQLException sqlExcept) {
                if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                    String sql = "UPDATE " + DBConstants.TABLE_RING_USER_SETTINGS
                            + " SET "
                            + DBConstants.USERINFO_UT + " =" + SettingsConstants.FNF_USERINFO_UT + ","
                            + DBConstants.GROUP_UT + " =" + SettingsConstants.FNF_GROUP_UT + ","
                            + DBConstants.CONTACT_UT + " =" + SettingsConstants.FNF_CONTACT_UT + ","
                            + DBConstants.CIRCLE_UT + " =" + SettingsConstants.FNF_CIRCLE_UT + ","
                            + DBConstants.CIRCLE_MEMBER_UT + " =" + SettingsConstants.FNF_CIRCLE_MEMBER_UT + ","
                            + DBConstants.CALL_LOG_UT + " =" + SettingsConstants.FNF_CALL_LOG_UT + ","
                            + DBConstants.SMS_LOG_UT + " =" + SettingsConstants.FNF_SMS_LOG_UT + ","
                            + DBConstants.LAST_LOGIN_TIME + " =" + SettingsConstants.FNF_LAST_LOGIN_TIME + ","
                            + DBConstants.CHAT_BG + " ='" + chat_bg + "'"
                            + " WHERE "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                        stmt.executeUpdate(sql);
                } else {
                    throw new Exception();
                }
            }

        } catch (Exception e) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if(conn!=null){
                conn.close();}
            } catch (SQLException ex) {
            }
        }
    }
}

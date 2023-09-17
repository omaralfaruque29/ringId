/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserLogInInfo;

/**
 *
 * @author raj
 */
public class InsertRingLoginSetting extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertRingLoginSetting.class);
    private List<UserLogInInfo> userLogInInfoList;

    public InsertRingLoginSetting(List<UserLogInInfo> userLogInInfoList) {
        this.userLogInInfoList = userLogInInfoList;
    }

    @Override
    public void run() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:" + DBConstants.DB_NAME + ";create=true";

        Connection conn = null;
        Statement stmt = null;

        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            conn.setAutoCommit(false);

            for (UserLogInInfo entry : userLogInInfoList) {

                if (entry.getStatus() == StatusConstants.STATUS_DELETED) {
                    try {
                        String sql1 = "DELETE FROM " + DBConstants.TABLE_RING_LOGIN_SETTINGS
                                + " WHERE " + DBConstants.LOGIN_KEY + " = '" + entry.getKey() + "'";
                        stmt.execute(sql1);
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                        log.error("Error in TABLE_RING_LOGIN_SETTINGS while deleting ==>" + ex.getMessage());
                    }
                } else {

                    try {
                        String sql1 = "INSERT INTO " + DBConstants.TABLE_RING_LOGIN_SETTINGS
                                + " ( " + DBConstants.LOGIN_KEY + "," + DBConstants.LOGIN_VALUE + ")"
                                + " VALUES"
                                + " ("
                                + "'" + entry.getKey() + "',"
                                + "'" + entry.getValue() + "'"
                                + ")";
                        stmt.execute(sql1);
                    } catch (SQLException SqlExcept) {
                        if (SqlExcept.getSQLState().equalsIgnoreCase("23505")) {
                            String sql2 = "UPDATE " + DBConstants.TABLE_RING_LOGIN_SETTINGS
                                    + " SET "
                                    + DBConstants.LOGIN_VALUE + "='" + entry.getValue() + "'"
                                    + " WHERE "
                                    + DBConstants.LOGIN_KEY + " = '" + entry.getKey() + "'";

                            stmt.execute(sql2);
                        } else {
                            SqlExcept.printStackTrace();
                            throw new Exception();
                        }
                    }
                }
            }

            conn.commit();
            setLoginSettingInfo();
        } catch (Exception e) {
            try {
                if(conn!=null){
                conn.rollback();}
            } catch (Exception ex) {
            }
        } finally {
            try {
                if(stmt!=null){
                stmt.close();}
                if(conn!=null){
                conn.close();}
            } catch (SQLException ex) {
            }
        }
    }

    private void setLoginSettingInfo() {

        for (UserLogInInfo entry : userLogInInfoList) {
            if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_NAME)) {
                MyFnFSettings.VALUE_LOGIN_USER_NAME = entry.getValue();
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_TYPE)) {
                int int_value = 0;
                try {
                    int_value = Integer.parseInt(entry.getValue());
                } catch (Exception e) {
                }
                MyFnFSettings.VALUE_LOGIN_USER_TYPE = int_value;
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_PASSWORD)) {
                String pass = entry.getValue();
                String newPass = HelperMethods.encryptPassword(pass);
                MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = newPass;
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_SAVE_PASSWORD)) {
                int int_value = 0;
                try {
                    int_value = Integer.parseInt(entry.getValue());
                } catch (Exception e) {
                }
                MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = int_value;
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_AUTO_START)) {
                int int_value = 0;
                try {
                    int_value = Integer.parseInt(entry.getValue());
                } catch (Exception e) {
                }
                MyFnFSettings.VALUE_LOGIN_AUTO_START = int_value;
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_AUTO_SIGNIN)) {
                int int_value = 0;
                try {
                    int_value = Integer.parseInt(entry.getValue());
                } catch (Exception e) {
                }
                MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = int_value;
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_INFO)) {
                JsonFields fields = null;
                try {
                    fields = HelperMethods.getJsonFields(entry.getValue());
                } catch (Exception ex) {
                    fields = null;
                }
                if (fields != null) {
                    MyFnFSettings.VALUE_LOGIN_USER_INFO = fields;
                }
            } else if (entry.getKey().equalsIgnoreCase(MyFnFSettings.KEY_MOBILE_DIALING_CODE)) {
                MyFnFSettings.VALUE_MOBILE_DIALING_CODE = entry.getValue();
            }
        }
    }

}

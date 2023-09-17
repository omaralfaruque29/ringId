/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.constants.StatusConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.SingleMemberInCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoCircleMemberTable extends Thread {

    private List<SingleMemberInCircleDto> memberList;

    public InsertIntoCircleMemberTable(List<SingleMemberInCircleDto> memberList) {
        this.memberList = memberList;
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

            for (SingleMemberInCircleDto members : memberList) {

                if (members.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                            + " WHERE "
                            + DBConstants.CIRCLE_ID + "=" + members.getCircleId() + " AND "
                            + DBConstants.USER_IDENTITY + "='" + members.getUserIdentity() + "'" + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    if (stmt != null) {
                        stmt.executeUpdate(query);
                    }
                } else {
                    String sql = null;
                    try {

                        sql = "INSERT INTO " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                                + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.CIRCLE_ID + "," + DBConstants.ADMIN + "," + DBConstants.USER_IDENTITY + "," + DBConstants.FULL_NAME + "," + DBConstants.GENDER + "," + DBConstants.MOBILE_PHONE + " ," + DBConstants.UPDATE_TIME + ")"
                                + "VALUES"
                                + "("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + members.getCircleId() + ","
                                + members.isAdmin() + ","
                                + "'" + members.getUserIdentity() + "',"
                                + "'" + members.getFullName() + "',"
                                //+ "'" + members.getLastName() + "',"
                                + "'" + members.getGender() + "',"
                                + "'" + members.getMobilePhone() + "',"
                                + members.getUt() + ""
                                + ")";
                        if (stmt != null) {
                            stmt.execute(sql);
                        }
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            sql = "UPDATE " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                                    + " SET "
                                    + DBConstants.ADMIN + " =" + members.isAdmin() + ","
                                    + DBConstants.FULL_NAME + " ='" + members.getFullName().trim() + "',"
                                    // + DBConstants.LAST_NAME + " ='" + members.getLastName().trim() + "',"
                                    + DBConstants.GENDER + " ='" + members.getGender() + "',"
                                    + DBConstants.MOBILE_PHONE + " = '" + members.getMobilePhone() + "',"
                                    + DBConstants.UPDATE_TIME + " =" + members.getUt() + ""
                                    + " WHERE "
                                    + DBConstants.CIRCLE_ID + " = " + members.getCircleId() + " AND "
                                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                    + DBConstants.USER_IDENTITY + " = '" + members.getUserIdentity() + "'";

                            if (stmt != null) {
                                stmt.executeUpdate(sql);
                            }
                        } else {
                            throw new Exception();
                        }
                    }
                }
            }

            for (SingleMemberInCircleDto entity : memberList) {
                if (entity.getUt() > SettingsConstants.FNF_CIRCLE_MEMBER_UT) {
                    SettingsConstants.FNF_CIRCLE_MEMBER_UT = entity.getUt();
                }
            }
            new InsertIntoRingUserSettings().start();
            
                conn.commit();
        } catch (Exception e) {
            try {
                if(conn!=null){
                conn.rollback();}
            } catch (Exception ex) {

            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {

            }
        }

    }
}

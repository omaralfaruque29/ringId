/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.model.constants.StatusConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.GroupMemberDTO;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoGroupMemberTable extends Thread {

    private List<GroupMemberDTO> memberList;

    public InsertIntoGroupMemberTable(List<GroupMemberDTO> memberList) {
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

            for (GroupMemberDTO members : memberList) {

                if (members.getIntegerStatus() != null && members.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST + " WHERE " + DBConstants.GROUP_ID + "=" + members.getGroupId()
                            + " AND " + DBConstants.USER_IDENTITY + "='" + members.getUserIdentity() + "'"
                            + " AND " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    stmt.executeUpdate(query);
                } else {
                    String sql = null;
                    try {

                        sql = "INSERT INTO " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST
                                + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.GROUP_ID + "," + DBConstants.USER_IDENTITY + "," + DBConstants.FULL_NAME + ")"
                                + "VALUES"
                                + "("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + members.getGroupId() + ","
                                + "'" + members.getUserIdentity() + "',"
                                + "'" + members.getFullName() + "'"
                                + ")";
                        stmt.execute(sql);
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            sql = "UPDATE " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST
                                    + " SET "
                                    + DBConstants.FULL_NAME + " ='" + members.getFullName().trim() + "'"
                                    + " WHERE "
                                    + DBConstants.GROUP_ID + " = " + members.getGroupId() + " AND "
                                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                    + DBConstants.USER_IDENTITY + " = '" + members.getUserIdentity() + "'";

                            stmt.executeUpdate(sql);
                        } else {
                            throw new Exception();
                        }
                    }
                }
            }

            conn.commit();
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {

            }
        } finally {
            try {
                if (stmt != null && conn != null) {
                    stmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {

            }
        }

    }
}

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
import com.ipvision.model.GroupInfoDTO;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoGroupListTable extends Thread {

    private List<GroupInfoDTO> groupList;

    public InsertIntoGroupListTable(List<GroupInfoDTO> groupList) {
        this.groupList = groupList;
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

            for (GroupInfoDTO group : groupList) {

                if (group.getIntegerStatus() != null && group.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_LIST
                            + " WHERE "
                            + DBConstants.GROUP_ID + "=" + group.getGroupId() + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST
                            + " WHERE "
                            + DBConstants.GROUP_ID + "=" + group.getGroupId() + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    stmt.executeUpdate(query1);
                    stmt.executeUpdate(query2);
                } else {
                    try {
                        if (group.getGroupUt() == null) {
                            group.setGroupUt(0L);
                        }
                        String sql = "INSERT INTO " + DBConstants.TABLE_RING_GROUP_LIST
                                + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.GROUP_ID + "," + DBConstants.GROUP_NAME + "," + DBConstants.GROUP_OWNER_USER_TABLE_ID + "," + DBConstants.GROUP_UT + ")"
                                + "VALUES"
                                + "("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + group.getGroupId() + ","
                                + "'" + group.getGroupName() + "',"
                                + group.getOwnerUserTableId() + ","
                                + "" + group.getGroupUt() + ""
                                + ")";
                        stmt.execute(sql);
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_GROUP_LIST
                                    + " SET "
                                    + DBConstants.GROUP_NAME + " ='" + group.getGroupName() + "',";
                            if (group.getOwnerUserTableId() != null) {
                                sql += DBConstants.GROUP_OWNER_USER_TABLE_ID + " =" + group.getOwnerUserTableId() + ",";
                            }
                            sql += DBConstants.GROUP_UT + " =" + group.getGroupUt() + ""
                                    + " WHERE "
                                    + DBConstants.GROUP_ID + " = " + group.getGroupId() + " AND "
                                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                            stmt.execute(sql);
                        } else {
                            throw new Exception();
                        }
                    }
                }
            }

            for (GroupInfoDTO entity : groupList) {
                if (entity.getGroupUt() != null && entity.getGroupUt() > SettingsConstants.FNF_GROUP_UT) {
                    SettingsConstants.FNF_GROUP_UT = entity.getGroupUt();
                }
            }
            (new InsertIntoRingUserSettings()).start();

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

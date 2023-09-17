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
import com.ipvision.model.SingleCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoCircleListTable extends Thread {

    private List<SingleCircleDto> circleList;

    public InsertIntoCircleListTable(List<SingleCircleDto> circleList) {
        this.circleList = circleList;
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

            for (SingleCircleDto circle : circleList) {

                if (circle.getIntegerStatus() != null && circle.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_LIST
                            + " WHERE "
                            + DBConstants.CIRCLE_ID + "=" + circle.getCircleId() + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                            + " WHERE "
                            + DBConstants.CIRCLE_ID + "=" + circle.getCircleId() + " AND "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    if (stmt != null) {
                        stmt.executeUpdate(query1);
                        stmt.executeUpdate(query2);
                    }
                } else {
                    try {
                        if (circle.getUt() == null) {
                            circle.setUt(0L);
                        }
                        String sql = "INSERT INTO " + DBConstants.TABLE_RING_CIRCLE_LIST
                                + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.CIRCLE_ID + "," + DBConstants.CIRCLE_NAME + "," + DBConstants.SUPER_ADMIN + "," + DBConstants.UPDATE_TIME + ")"
                                + "VALUES"
                                + "("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + circle.getCircleId() + ","
                                + "'" + circle.getCircleName() + "',"
                                + "'" + circle.getSuperAdmin() + "',"
                                + circle.getUt() + ""
                                + ")";
                        if (stmt != null) {
                            stmt.execute(sql);
                        }
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_CIRCLE_LIST
                                    + " SET "
                                    + DBConstants.CIRCLE_NAME + " ='" + circle.getCircleName() + "',"
                                    + DBConstants.SUPER_ADMIN + " ='" + circle.getSuperAdmin() + "',"
                                    + DBConstants.UPDATE_TIME + " =" + circle.getUt() + ""
                                    + " WHERE "
                                    + DBConstants.CIRCLE_ID + " = " + circle.getCircleId() + " AND "
                                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                            if (stmt != null) {
                                stmt.execute(sql);
                            }
                        } else {
                            throw new Exception();
                        }
                    }
                }
            }

            for (SingleCircleDto entity : circleList) {
                if (entity.getUt() != null && entity.getUt() > SettingsConstants.FNF_CIRCLE_UT) {
                    SettingsConstants.FNF_CIRCLE_UT = entity.getUt();
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

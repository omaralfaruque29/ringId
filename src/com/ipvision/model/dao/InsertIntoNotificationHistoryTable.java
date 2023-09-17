/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;
import com.ipvision.constants.MyFnFSettings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Wasif Islam
 */
public class InsertIntoNotificationHistoryTable extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertIntoNotificationHistoryTable.class);
    private List<JsonFields> notificationList;

    public InsertIntoNotificationHistoryTable(List<JsonFields> notificationList) {
        this.notificationList = notificationList;
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

            for (JsonFields entity : notificationList) {
                try {
                    String sql = "INSERT INTO " + DBConstants.TABLE_RING_NOTIFICATION_HISTORY
                            + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.ID + "," + DBConstants.NOTIFICATION_FRIENDID + "," + DBConstants.NOTIFICATION_UT + "," + DBConstants.NOTIFICATION_N_TYPE + "," + DBConstants.NOTIFICATION_ACTIVITY_ID + "," + DBConstants.NOTIFICATION_NEWSFEED_ID + "," + DBConstants.NOTIFICATION_IMAGE_ID + "," + DBConstants.NOTIFICATION_COMMENT_ID + "," + DBConstants.NOTIFICATION_MESSAGE_TYPE + "," + DBConstants.NOTIFICATION_FRIEND_NAME + "," + DBConstants.NOTIFICATION_NUMBER_OF_LIKECOMMENT + "," + DBConstants.NOTIFICATION_IS_READ + ")"
                            + "VALUES"
                            + "("
                            + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                            + "'" + entity.getId() + "',"
                            + "'" + entity.getFriendIdentity() + "',"
                            + entity.getUt() + ","
                            + entity.getNt() + ","
                            + entity.getAcId() + ","
                            + entity.getNfId() + ","
                            + entity.getImageId() + ","
                            + entity.getCmnId() + ","
                            + entity.getMt() + ","
                            + "'" + entity.getFndN() + "',"
                            + entity.getLoc() + ","
                            + entity.getIsRead()
                            + ")";
                    stmt.execute(sql);
                } catch (SQLException sqlExcept) {
                    //
                    if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                        String sql = "UPDATE " + DBConstants.TABLE_RING_NOTIFICATION_HISTORY
                                + " SET "
                                + DBConstants.NOTIFICATION_FRIENDID + " = '" + entity.getFriendIdentity() + "',"
                                + DBConstants.NOTIFICATION_UT + " = " + entity.getUt() + ","
                                + DBConstants.NOTIFICATION_N_TYPE + " = " + entity.getNt() + ","
                                + DBConstants.NOTIFICATION_ACTIVITY_ID + " = " + entity.getAcId() + ","
                                + DBConstants.NOTIFICATION_NEWSFEED_ID + " = " + entity.getNfId() + ","
                                + DBConstants.NOTIFICATION_IMAGE_ID + " = " + entity.getImageId() + ","
                                + DBConstants.NOTIFICATION_COMMENT_ID + " = " + entity.getCmnId() + ","
                                + DBConstants.NOTIFICATION_MESSAGE_TYPE + " = " + entity.getMt() + ","
                                + DBConstants.NOTIFICATION_FRIEND_NAME + " ='" + entity.getFndN() + "',"
                                + DBConstants.NOTIFICATION_NUMBER_OF_LIKECOMMENT + " = " + entity.getLoc() + ","
                                + DBConstants.NOTIFICATION_IS_READ + " = " + entity.getIsRead()
                                + " WHERE "
                                + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + DBConstants.ID + " = '" + entity.getId() + "'";
                        stmt.execute(sql);
                    } else {
                        throw new Exception();
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
            //e.printStackTrace();
       log.error("Error in here ==>" + e.getMessage());
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

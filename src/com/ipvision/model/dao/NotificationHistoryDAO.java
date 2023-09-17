/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Shahadat Hossain
 */
public class NotificationHistoryDAO {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NotificationHistoryDAO.class);

    public static List<JsonFields> loadNotificationHistoryList(int start, int limit) {
        List<JsonFields> notificationList = new ArrayList<JsonFields>();
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                // MyfnfHashMaps.getInstance().getNotificationsMap().clear();
                try {
                    String query = "SELECT * FROM "
                            + DBConstants.TABLE_RING_NOTIFICATION_HISTORY + " "
                            + "WHERE "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                            + "ORDER BY "
                            + DBConstants.NOTIFICATION_UT + " DESC"
                            + (limit > 0 ? (" OFFSET " + start + " ROWS FETCH FIRST " + limit + " ROWS ONLY") : "");

                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            JsonFields entity = new JsonFields();
                            entity.setId(results.getString(DBConstants.ID));
                            entity.setFriendIdentity(results.getString(DBConstants.NOTIFICATION_FRIENDID));
                            entity.setUt(results.getLong(DBConstants.NOTIFICATION_UT));
                            entity.setNt(results.getInt(DBConstants.NOTIFICATION_N_TYPE));
                            entity.setAcId(results.getLong(DBConstants.NOTIFICATION_ACTIVITY_ID));
                            entity.setNfId(results.getLong(DBConstants.NOTIFICATION_NEWSFEED_ID));
                            entity.setImageId(results.getLong(DBConstants.NOTIFICATION_IMAGE_ID));
                            entity.setCmnId(results.getString(DBConstants.NOTIFICATION_COMMENT_ID));
                            entity.setMt(results.getInt(DBConstants.NOTIFICATION_MESSAGE_TYPE));
                            entity.setFndN(results.getString(DBConstants.NOTIFICATION_FRIEND_NAME));
                            entity.setLoc(results.getLong(DBConstants.NOTIFICATION_NUMBER_OF_LIKECOMMENT));
                            entity.setIsRead(results.getBoolean(DBConstants.NOTIFICATION_IS_READ));

                            notificationList.add(entity);
                            if (!MyfnfHashMaps.getInstance().getNotificationsMap().containsKey(entity.getId())) {
                                MyfnfHashMaps.getInstance().getNotificationsMap().put(entity.getId(), entity);
                            }

                        }
                    }

                } finally {
                    loadMaxTimeFromNotificationHistory();
                    loadMinTimeFromNotificationHistory();
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                    }
                }
            }

        } catch (SQLException ex) {
        }

        return notificationList;
    }

    public static void loadMaxTimeFromNotificationHistory() {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT MAX(" + DBConstants.NOTIFICATION_UT + ") AS TEMP FROM "
                            + DBConstants.TABLE_RING_NOTIFICATION_HISTORY + " "
                            + "WHERE "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' ";

                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            AppConstants.NOTIFICATION_MAX_UT = results.getLong("TEMP");
                        }
                    }

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

    public static void loadMinTimeFromNotificationHistory() {

        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT MIN(" + DBConstants.NOTIFICATION_UT + ") AS TEMP FROM "
                            + DBConstants.TABLE_RING_NOTIFICATION_HISTORY + " "
                            + "WHERE "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' ";

                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            AppConstants.NOTIFICATION_MIN_UT = results.getLong("TEMP");
                        }
                    }

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

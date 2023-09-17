/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import com.ipvision.model.constants.DBConstants;

/**
 *
 * @author Sirat Samyoun
 */
public class DeleteChatCallHistoryDAO {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteChatCallHistoryDAO.class);
    public SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    private long daysInSecond = 86400000;

    public static final int TYPE_FRIEND_CHAT = 1;
    public static final int TYPE_GROUP_CHAT = 2;
    public static final int TYPE_CALL_LOG = 3;
    public static final int TYPE_FRIEND_ACTIVITY = 4;
    public static final int TYPE_GROUP_ACTIVITY = 5;
    public static final int TYPE_SCROLL_UP = 1;
    public static final int TYPE_SCROLL_DOWN = 2;

//    public static List<RecentDTO> deleteChatCallHistory(Long groupId, String friendId, long time, Integer limit) {
//        List<RecentDTO> recentList = new ArrayList<RecentDTO>();
//        return recentList;
//    }
    public static void deleteChatCallHistory(long time) {

        Statement stmt = null;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                try {
                    String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID
                            + " WHERE "
                            + DBConstants.MESSAGE_DATE + " < " + time + "";

                    String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_CALL_LOG
                            + " WHERE "
                            + DBConstants.CL_CALLING_TIME + " < " + time + " AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    String query3 = "DELETE FROM  " + DBConstants.TABLE_RING_ACTIVITY
                            + " WHERE "
                            + DBConstants.UPDATE_TIME + " < " + time + " AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    String query4 = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                            + " WHERE "
                            + DBConstants.MESSAGE_DATE + " < " + time + " AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    stmt.executeUpdate(query1);
                    stmt.executeUpdate(query2);
                    stmt.executeUpdate(query3);
                    stmt.executeUpdate(query4);
                } catch (Exception exx) {
                   // exx.printStackTrace();
                log.error("Error in deleteChatCallHistory ==>" + exx.getMessage());    
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

    }

}

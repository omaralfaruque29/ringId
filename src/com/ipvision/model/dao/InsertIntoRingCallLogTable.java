/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.call.CallLog;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.leftdata.CallHistoryContainer;
import com.ipvision.view.leftdata.SingleCallHistoryPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.FriendList;

/**
 *
 * @author Shahadat Hossain
 */
public class InsertIntoRingCallLogTable extends Thread {

    private List<CallLog> callLogList;
    private boolean isDisplayInUI;

    public InsertIntoRingCallLogTable(List<CallLog> callLogList, boolean isDisplayInUI) {
        this.callLogList = callLogList;
        this.isDisplayInUI = isDisplayInUI;
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

            for (CallLog callLog : callLogList) {
                String friendIdentity = callLog.getFriendIdentity() != null ? callLog.getFriendIdentity() : "";
                if (FriendList.getInstance().getFriend_hash_map().containsKey(friendIdentity)) {
                    try {
                        String sql = "INSERT INTO " + DBConstants.TABLE_RING_CALL_LOG
                                + " (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.ID + "," + DBConstants.FRIEND_IDENTITY + "," + DBConstants.CL_CALL_TYPE + "," + DBConstants.CL_CALL_DURATION + "," + DBConstants.CL_CALLING_TIME + ")"
                                + "VALUES"
                                + "("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + "'" + callLog.getCallID() + "',"
                                + "'" + friendIdentity + "',"
                                + callLog.getCallType() + ","
                                + callLog.getCallDuration() + ","
                                + callLog.getCallingTime() + ""
                                + ")";
                        stmt.execute(sql);
                    } catch (SQLException sqlExcept) {

                        if (sqlExcept.getSQLState().equals("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_CALL_LOG
                                    + " SET "
                                    + DBConstants.CL_CALL_DURATION + " =" + callLog.getCallDuration() + "";

                            if (callLog.getCallType() != null) {
                                sql += DBConstants.CL_CALL_TYPE + " =" + callLog.getCallType() + ",";
                            }

                            sql += DBConstants.CL_CALLING_TIME + " =" + callLog.getCallingTime() + ""
                                    + " WHERE "
                                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                    + DBConstants.FRIEND_IDENTITY + " = '" + friendIdentity + "' AND "
                                    + DBConstants.ID + " = '" + callLog.getCallID() + "'";
                            stmt.execute(sql);
                        } else {
                            throw new Exception();
                        }
                    }
                }
            }

            for (CallLog entity : callLogList) {
                if (entity.getUpdateTime() != null && entity.getUpdateTime() > SettingsConstants.FNF_CALL_LOG_UT) {
                    SettingsConstants.FNF_CALL_LOG_UT = entity.getUpdateTime();
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

        isDisplayInUI();
    }

    private void isDisplayInUI() {
        if (isDisplayInUI) {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            String url = "jdbc:derby:" + DBConstants.DB_NAME + ";create=true";
            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url);
                stmt = conn.createStatement();

                for (CallLog log : callLogList) {
                    CallLog entity = null;

                    if (log.getFriendIdentity() != null
                            && log.getFriendIdentity().length() > 0) {

                        try {
                            String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2} AND {3} = {4} ORDER BY {5} ASC",
                                    DBConstants.TABLE_RING_CALL_LOG,
                                    DBConstants.ID,
                                    "'" + log.getCallID() + "'",
                                    DBConstants.LOGIN_USER_ID,
                                    "'" + MyFnFSettings.LOGIN_USER_ID + "'",
                                    DBConstants.CL_CALLING_TIME);
                            ResultSet results = stmt.executeQuery(query);

                            if (results.next()) {
                                entity = new CallLog();
                                entity.setCallID(results.getString(DBConstants.ID));
                                entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
                                entity.setCallType(results.getInt(DBConstants.CL_CALL_TYPE));
                                entity.setCallDuration(results.getLong(DBConstants.CL_CALL_DURATION));
                                entity.setCallingTime(results.getLong(DBConstants.CL_CALLING_TIME));
                            }

                        } catch (SQLException ex) {
                        }

                        if (entity != null) {
                            RecentDTO recentDTO = new RecentDTO();
                            recentDTO.setTime(entity.getCallingTime());
                            recentDTO.setContactId(entity.getFriendIdentity());
                            recentDTO.setContactType(RecentContactDAO.TYPE_FRIEND);
                            recentDTO.setType(RecentChatCallActivityDAO.TYPE_CALL_LOG);
                            recentDTO.setCallLog(entity);

                            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(log.getFriendIdentity());
                            if (friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                                friendChatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(entity);
                            } else {
                                RecentDTO prevRecentDTO = ChatHashMap.getInstance().getCallUnreadHistories().get(recentDTO.getContactId());
                                if (prevRecentDTO == null) {
                                    ChatHashMap.getInstance().getCallUnreadHistories().put(recentDTO.getContactId(), recentDTO);
                                    GuiRingID.getInstance().getMainButtonsPanel().addCallNotification(recentDTO);
                                } else {
                                    if (recentDTO.getTime() >= prevRecentDTO.getTime()) {
                                        ChatHashMap.getInstance().getCallUnreadHistories().put(recentDTO.getContactId(), recentDTO);
                                        GuiRingID.getInstance().getMainButtonsPanel().addCallNotification(recentDTO);
                                    } else {
                                        recentDTO = prevRecentDTO;
                                    }
                                }
                            }

                            CallHistoryContainer.changeStatus(recentDTO);
                        }
                    }
                }

            } catch (Exception ex) {
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.call.CallLog;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHelpers;

/**
 *
 * @author Shahadat Hossain
 */
public class RecentChatCallActivityDAO {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RecentChatCallActivityDAO.class);
    private static long daysInSecond = 86400000;

    public static final int TYPE_FRIEND_CHAT = 1;
    public static final int TYPE_GROUP_CHAT = 2;
    public static final int TYPE_CALL_LOG = 3;
    public static final int TYPE_FRIEND_ACTIVITY = 4;
    public static final int TYPE_GROUP_ACTIVITY = 5;
    public static final int TYPE_SCROLL_UP = 1;
    public static final int TYPE_SCROLL_DOWN = 2;

    public static List<RecentDTO> loadRecentChatCallActivityListByDays(Long groupId, String friendId, long time, Integer limit) {
        List<RecentDTO> recentList = new ArrayList<RecentDTO>();

        if (limit != null && limit <= 0) {
            return recentList;
        }

        Statement stmt = null;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                if (friendId != null && friendId.length() > 0) {
                    String query = "";
                    try {
                        query = "SELECT * FROM "
                                + "    ( "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            " + DBConstants.MSG_SENDER + ", "
                                + "            " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.MESSAGE_DATE + ", "
                                + "            " + DBConstants.SYSTEM_DATE + ", "
                                + "            " + DBConstants.TIME_OUT + ", "
                                + "            " + DBConstants.PACKET_TYPE + ", "
                                + "            " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            0 AS " + DBConstants.CL_CALL_DURATION + ", "
                                + "            0 AS " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            0 AS " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_FRIEND_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID + " "
                                + "        WHERE "
                                + "        " + DBConstants.MESSAGE_DATE + " > " + time + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "        UNION "
                                + "        SELECT "
                                + "            0 AS " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            '' AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.CL_CALLING_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            '' AS " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.CL_CALL_TYPE + " AS " + DBConstants.MESSAGE_TYPE + ", "
                                + "            " + DBConstants.CL_CALL_DURATION + ", "
                                + "            0 AS " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            0 AS " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_CALL_LOG + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_CALL_LOG + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.CL_CALLING_TIME + " > " + time + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "        UNION "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            " + DBConstants.ACTIVITY_BY + " AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            0 AS " + DBConstants.CL_CALL_DURATION + ", "
                                + "            " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_FRIEND_ACTIVITY + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_ACTIVITY + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.UPDATE_TIME + " > " + time + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "    ) AS vTable "
                                + "    ORDER BY vTable." + DBConstants.MESSAGE_DATE + " DESC "
                                + "   " + (limit != null ? " OFFSET 0 ROWS FETCH FIRST " + limit + " ROWS ONLY" : "");

                        ResultSet results = stmt.executeQuery(query);
                        while (results.next()) {
                            int tableType = results.getInt(DBConstants.TABLE_TYPE);
                            if (tableType == TYPE_FRIEND_CHAT) {
                                recentList.add(makeFriendChatObject(results));
                            } else if (tableType == TYPE_CALL_LOG) {
                                recentList.add(makeCallLogObject(results));
                            } else if (tableType == TYPE_FRIEND_ACTIVITY) {
                                recentList.add(makeFriendActivityObject(results));
                            }
                        }

                    } catch (SQLException ex) {
                        log.error(query);
                      //  ex.printStackTrace();
                    }

                }

                if (groupId != null && groupId > 0) {
                    String query = "";
                    try {
                        query = "SELECT * FROM "
                                + "    ( "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.GROUP_ID + ", "
                                + "            " + DBConstants.MSG_SENDER + ", "
                                + "            " + DBConstants.FULL_NAME + ", "
                                + "            " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.MESSAGE_DATE + ", "
                                + "            " + DBConstants.SYSTEM_DATE + ", "
                                + "            " + DBConstants.TIME_OUT + ", "
                                + "            " + DBConstants.PACKET_TYPE + ", "
                                + "            " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            '' AS " + DBConstants.MEMBER_LIST + ", "
                                + "            '' AS " + DBConstants.PREVIOUS_GROUP_NAME + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_GROUP_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.MESSAGE_DATE + " > " + time + " AND "
                                + "        " + DBConstants.GROUP_ID + " = " + groupId + " "
                                + "        UNION "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.GROUP_ID + ", "
                                + "            " + DBConstants.ACTIVITY_BY + " AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.FULL_NAME + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            " + DBConstants.MEMBER_LIST + ", "
                                + "            " + DBConstants.PREVIOUS_GROUP_NAME + ", "
                                + "            " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_GROUP_ACTIVITY + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_ACTIVITY + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.UPDATE_TIME + " > " + time + " AND "
                                + "        " + DBConstants.GROUP_ID + " = " + groupId + " "
                                + "    ) AS vTable "
                                + "    ORDER BY vTable." + DBConstants.MESSAGE_DATE + " DESC "
                                + "    " + (limit != null ? " OFFSET 0 ROWS FETCH FIRST " + limit + " ROWS ONLY" : "");

                        ResultSet results = stmt.executeQuery(query);
                        while (results.next()) {
                            int tableType = results.getInt(DBConstants.TABLE_TYPE);
                            if (tableType == TYPE_GROUP_CHAT) {
                                recentList.add(makeGroupChatObject(results));
                            } else if (tableType == TYPE_GROUP_ACTIVITY) {
                                recentList.add(makeGroupActivityObject(results));
                            }
                        }

                    } catch (SQLException ex) {
                        log.error(query);
                    }
                }

            }
        } catch (SQLException ex) {

        } finally {
            try {
                if(stmt!=null){
                stmt.close();}
            } catch (SQLException ex) {
            }
        }

        return recentList;
    }

    public static List<RecentDTO> loadRecentChatCallActivityListByScroll(Long groupId, String friendId, long time, Integer limit, int scrollType) {
        List<RecentDTO> recentList = new ArrayList<RecentDTO>();

        if (limit != null && limit <= 0) {
            return recentList;
        }

        Date currDate = new Date();
        currDate.setHours(0);
        currDate.setMinutes(0);
        currDate.setSeconds(0);
        long minTime = currDate.getTime() - (RecentContactDAO._90_DAYS * daysInSecond);

        Statement stmt = null;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                if (friendId != null && friendId.length() > 0) {
                    String query = "";
                    try {
                        query = "SELECT * FROM "
                                + "    ( "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            " + DBConstants.MSG_SENDER + ", "
                                + "            " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.MESSAGE_DATE + ", "
                                + "            " + DBConstants.SYSTEM_DATE + ", "
                                + "            " + DBConstants.TIME_OUT + ", "
                                + "            " + DBConstants.PACKET_TYPE + ", "
                                + "            " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            0 AS " + DBConstants.CL_CALL_DURATION + ", "
                                + "            0 AS " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            0 AS " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_FRIEND_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID + " "
                                + "        WHERE "
                                + "        " + DBConstants.MESSAGE_DATE + " >= " + minTime + " AND "
                                + "        " + (scrollType == TYPE_SCROLL_UP ? (DBConstants.MESSAGE_DATE + " < " + time) : (DBConstants.MESSAGE_DATE + " > " + time)) + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "        UNION "
                                + "        SELECT "
                                + "            0 AS " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            '' AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.CL_CALLING_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            '' AS " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.CL_CALL_TYPE + " AS " + DBConstants.MESSAGE_TYPE + ", "
                                + "            " + DBConstants.CL_CALL_DURATION + ", "
                                + "            0 AS " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            0 AS " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_CALL_LOG + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_CALL_LOG + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.CL_CALLING_TIME + " >= " + minTime + " AND "
                                + "        " + (scrollType == TYPE_SCROLL_UP ? (DBConstants.CL_CALLING_TIME + " < " + time) : (DBConstants.CL_CALLING_TIME + " > " + time)) + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "        UNION "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.FRIEND_IDENTITY + ", "
                                + "            " + DBConstants.ACTIVITY_BY + " AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            0 AS " + DBConstants.CL_CALL_DURATION + ", "
                                + "            " + DBConstants.FROM_CONTACT_TYPE + ", "
                                + "            " + DBConstants.TO_CONTACT_TYPE + ", "
                                + "            " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_FRIEND_ACTIVITY + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_ACTIVITY + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.UPDATE_TIME + " >= " + minTime + " AND "
                                + "        " + (scrollType == TYPE_SCROLL_UP ? (DBConstants.UPDATE_TIME + " < " + time) : (DBConstants.UPDATE_TIME + " > " + time)) + " AND "
                                + "        " + DBConstants.FRIEND_IDENTITY + " = '" + friendId + "' "
                                + "    ) AS vTable "
                                + "    ORDER BY vTable." + DBConstants.MESSAGE_DATE + " " + (scrollType == TYPE_SCROLL_UP ? "DESC" : "ASC") + " "
                                + "    " + (limit != null ? " OFFSET 0 ROWS FETCH FIRST " + limit + " ROWS ONLY" : "");

                        ResultSet results = stmt.executeQuery(query);
                        while (results.next()) {
                            int tableType = results.getInt(DBConstants.TABLE_TYPE);
                            if (tableType == TYPE_FRIEND_CHAT) {
                                recentList.add(makeFriendChatObject(results));
                            } else if (tableType == TYPE_CALL_LOG) {
                                recentList.add(makeCallLogObject(results));
                            } else if (tableType == TYPE_FRIEND_ACTIVITY) {
                                recentList.add(makeFriendActivityObject(results));
                            }
                        }

                    } catch (SQLException ex) {
                        log.error(query);
                       // ex.printStackTrace();
                    }

                }

                if (groupId != null && groupId > 0) {
                    String query = "";
                    try {
                        query = "SELECT * FROM "
                                + "    ( "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.GROUP_ID + ", "
                                + "            " + DBConstants.MSG_SENDER + ", "
                                + "            " + DBConstants.FULL_NAME + ", "
                                + "            " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.MESSAGE_DATE + ", "
                                + "            " + DBConstants.SYSTEM_DATE + ", "
                                + "            " + DBConstants.TIME_OUT + ", "
                                + "            " + DBConstants.PACKET_TYPE + ", "
                                + "            " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            '' AS " + DBConstants.MEMBER_LIST + ", "
                                + "            '' AS " + DBConstants.PREVIOUS_GROUP_NAME + ", "
                                + "            FALSE AS " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_GROUP_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.MESSAGE_DATE + " >= " + minTime + " AND "
                                + "        " + (scrollType == TYPE_SCROLL_UP ? (DBConstants.MESSAGE_DATE + " < " + time) : (DBConstants.MESSAGE_DATE + " > " + time)) + " AND "
                                + "        " + DBConstants.GROUP_ID + " = " + groupId + " "
                                + "        UNION "
                                + "        SELECT "
                                + "            " + DBConstants.ID + ", "
                                + "            " + DBConstants.GROUP_ID + ", "
                                + "            " + DBConstants.ACTIVITY_BY + " AS " + DBConstants.MSG_SENDER + ", "
                                + "            '' AS " + DBConstants.FULL_NAME + ", "
                                + "            '' AS " + DBConstants.MESSAGE + ", "
                                + "            " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
                                + "            0 AS " + DBConstants.SYSTEM_DATE + ", "
                                + "            0 AS " + DBConstants.TIME_OUT + ", "
                                + "            0 AS " + DBConstants.PACKET_TYPE + ", "
                                + "            0 AS " + DBConstants.STATUS + ", "
                                + "            " + DBConstants.PACKET_ID + ", "
                                + "            " + DBConstants.ACTIVITY_TYPE + ", "
                                + "            " + DBConstants.MESSAGE_TYPE + ", "
                                + "            " + DBConstants.MEMBER_LIST + ", "
                                + "            " + DBConstants.PREVIOUS_GROUP_NAME + ", "
                                + "            " + DBConstants.IS_PROCESSED + ", "
                                + "            " + TYPE_GROUP_ACTIVITY + " AS " + DBConstants.TABLE_TYPE + " "
                                + "        FROM "
                                + "        " + DBConstants.TABLE_RING_ACTIVITY + " "
                                + "        WHERE "
                                + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + "        " + DBConstants.UPDATE_TIME + " >= " + minTime + " AND "
                                + "        " + (scrollType == TYPE_SCROLL_UP ? (DBConstants.UPDATE_TIME + " < " + time) : (DBConstants.UPDATE_TIME + " > " + time)) + " AND "
                                + "        " + DBConstants.GROUP_ID + " = " + groupId + " "
                                + "    ) AS vTable "
                                + "    ORDER BY vTable." + DBConstants.MESSAGE_DATE + " " + (scrollType == TYPE_SCROLL_UP ? "DESC" : "ASC") + " "
                                + "    " + (limit != null ? " OFFSET 0 ROWS FETCH FIRST " + limit + " ROWS ONLY" : "");

                        ResultSet results = stmt.executeQuery(query);
                        while (results.next()) {
                            int tableType = results.getInt(DBConstants.TABLE_TYPE);
                            if (tableType == TYPE_GROUP_CHAT) {
                                recentList.add(makeGroupChatObject(results));
                            } else if (tableType == TYPE_GROUP_ACTIVITY) {
                                recentList.add(makeGroupActivityObject(results));
                            }
                        }

                    } catch (SQLException ex) {
                        log.error(query);
                       // ex.printStackTrace();
                    }

                }
            }
        } catch (Exception ex) {

        } finally {
            try {
                if(stmt!=null){
                stmt.close();}
            } catch (SQLException ex) {
            }
        }

        return recentList;
    }

    private static RecentDTO makeFriendChatObject(ResultSet results) throws SQLException {
        MessageDTO entity = new MessageDTO();
        entity.setMessageId(results.getLong(DBConstants.ID));
        entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
        entity.setUserIdentity(results.getString(DBConstants.MSG_SENDER));
        entity.setMessage(results.getString(DBConstants.MESSAGE));
        entity.setMessageDate(results.getLong(DBConstants.MESSAGE_DATE));
        entity.setSystemDate(results.getLong(DBConstants.SYSTEM_DATE));
        entity.setPacketType(results.getInt(DBConstants.PACKET_TYPE));
        entity.setStatus(results.getInt(DBConstants.STATUS));
        entity.setTimeout(results.getInt(DBConstants.TIME_OUT));
        entity.setPacketID(results.getString(DBConstants.PACKET_ID));
        entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));

        RecentDTO recent = new RecentDTO();
        recent.setType(RecentChatCallActivityDAO.TYPE_FRIEND_CHAT);
        recent.setContactId(entity.getFriendIdentity());
        recent.setTime(entity.getMessageDate());
        recent.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recent.getTime())));
        recent.setMessageDTO(entity);
        return recent;
    }

    private static RecentDTO makeCallLogObject(ResultSet results) throws SQLException {
        CallLog entity = new CallLog();
        entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
        entity.setCallType(results.getInt(DBConstants.MESSAGE_TYPE));
        entity.setCallDuration(results.getLong(DBConstants.CL_CALL_DURATION));
        entity.setCallingTime(results.getLong(DBConstants.MESSAGE_DATE));

        RecentDTO recent = new RecentDTO();
        recent.setType(RecentChatCallActivityDAO.TYPE_CALL_LOG);
        recent.setContactId(entity.getFriendIdentity());
        recent.setTime(entity.getCallingTime());
        recent.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recent.getTime())));
        recent.setCallLog(entity);
        return recent;
    }

    private static RecentDTO makeFriendActivityObject(ResultSet results) throws SQLException {
        ActivityDTO entity = new ActivityDTO();
        entity.setId(results.getLong(DBConstants.ID));
        entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
        entity.setActivityBy(results.getString(DBConstants.MSG_SENDER));
        entity.setUpdateTime(results.getLong(DBConstants.MESSAGE_DATE));
        entity.setActivityType(results.getInt(DBConstants.ACTIVITY_TYPE));
        entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));
        entity.setFromContactType(results.getInt(DBConstants.FROM_CONTACT_TYPE));
        entity.setToContactType(results.getInt(DBConstants.TO_CONTACT_TYPE));
        entity.setIsProcessed(results.getBoolean(DBConstants.IS_PROCESSED));
        entity.setPacketID(results.getString(DBConstants.PACKET_ID));

        RecentDTO recent = new RecentDTO();
        recent.setType(RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY);
        recent.setContactId(entity.getFriendIdentity());
        recent.setTime(entity.getUpdateTime());
        recent.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recent.getTime())));
        recent.setActivityDTO(entity);
        return recent;
    }

    private static RecentDTO makeGroupChatObject(ResultSet results) throws SQLException {
        MessageDTO entity = new MessageDTO();
        entity.setMessageId(results.getLong(DBConstants.ID));
        entity.setGroupId(results.getLong(DBConstants.GROUP_ID));
        entity.setFullName(results.getString(DBConstants.FULL_NAME));
        entity.setUserIdentity(results.getString(DBConstants.MSG_SENDER));
        entity.setMessage(results.getString(DBConstants.MESSAGE));
        entity.setMessageDate(results.getLong(DBConstants.MESSAGE_DATE));
        entity.setSystemDate(results.getLong(DBConstants.SYSTEM_DATE));
        entity.setPacketType(results.getInt(DBConstants.PACKET_TYPE));
        entity.setStatus(results.getInt(DBConstants.STATUS));
        entity.setTimeout(results.getInt(DBConstants.TIME_OUT));
        entity.setPacketID(results.getString(DBConstants.PACKET_ID));
        entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));

        RecentDTO recent = new RecentDTO();
        recent.setType(RecentChatCallActivityDAO.TYPE_GROUP_CHAT);
        recent.setContactId(entity.getGroupId() + "");
        recent.setTime(entity.getMessageDate());
        recent.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recent.getTime())));
        recent.setMessageDTO(entity);
        return recent;
    }

    private static RecentDTO makeGroupActivityObject(ResultSet results) throws SQLException {
        ActivityDTO entity = new ActivityDTO();
        entity.setId(results.getLong(DBConstants.ID));
        entity.setGroupId(results.getLong(DBConstants.GROUP_ID));
        entity.setActivityBy(results.getString(DBConstants.MSG_SENDER));
        entity.setUpdateTime(results.getLong(DBConstants.MESSAGE_DATE));
        entity.setActivityType(results.getInt(DBConstants.ACTIVITY_TYPE));
        entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));
        entity.setIsProcessed(results.getBoolean(DBConstants.IS_PROCESSED));
        entity.setPacketID(results.getString(DBConstants.PACKET_ID));
        entity.setPrevGroupName(results.getString(DBConstants.PREVIOUS_GROUP_NAME));
        String members = results.getString(DBConstants.MEMBER_LIST);
        if (members != null && members.trim().length() > 0) {
            entity.setMembers(HelperMethods.mapActivityGroupMembers(members));
        }

        RecentDTO recent = new RecentDTO();
        recent.setType(RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY);
        recent.setContactId(entity.getGroupId() + "");
        recent.setTime(entity.getUpdateTime());
        recent.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recent.getTime())));
        recent.setActivityDTO(entity);
        return recent;
    }

    public static void fetchAndResendPendingChat() {
        Map<String, List<MessageBaseDTO>> pendingFriendChat = new ConcurrentHashMap<String, List<MessageBaseDTO>>();
        Map<Long, List<MessageBaseDTO>> pendingGroupChat = new ConcurrentHashMap<Long, List<MessageBaseDTO>>();

        Statement stmt = null;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                String query1 = "";
                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID).toUpperCase(), null);
                    if (!tablesw.isClosed() && tablesw.next()) {
                        query1 = "SELECT * "
                                + "FROM "
                                + "" + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID + " "
                                + "WHERE "
                                + DBConstants.MESSAGE_DATE + " > " + 0 + " AND "
                                + DBConstants.STATUS + " = " + ChatConstants.CHAT_FRIEND_PENDING + " AND "
                                + DBConstants.MSG_SENDER + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                                + "ORDER BY " + DBConstants.MESSAGE_DATE;

                        ResultSet results = stmt.executeQuery(query1);
                        while (results.next()) {
                            String friendIdentity = results.getString(DBConstants.FRIEND_IDENTITY);
                            List<MessageBaseDTO> list = pendingFriendChat.get(friendIdentity);
                            if (list == null) {
                                list = new ArrayList<MessageBaseDTO>();
                                pendingFriendChat.put(friendIdentity, list);
                                ChatHelpers.startFriendChat(friendIdentity);
                            }

                            MessageBaseDTO entity = new MessageBaseDTO();
                            entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
                            entity.setUserIdentity(results.getString(DBConstants.MSG_SENDER));
                            entity.setMessage(results.getString(DBConstants.MESSAGE));
                            entity.setMessageDate(results.getLong(DBConstants.MESSAGE_DATE));
                            entity.setPacketType(results.getInt(DBConstants.PACKET_TYPE));
                            entity.setTimeout(results.getInt(DBConstants.TIME_OUT));
                            entity.setPacketID(results.getString(DBConstants.PACKET_ID));
                            entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));
                            list.add(entity);
                        }
                    }
                } catch (SQLException ex) {
                    log.error(query1);
                   // ex.printStackTrace();
                }

                String query2 = "";
                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_GROUP_CHAT_TABLE).toUpperCase(), null);
                    if (!tablesw.isClosed() && tablesw.next()) {
                        query2 = "SELECT * "
                                + "FROM "
                                + "" + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " "
                                + "WHERE "
                                + DBConstants.MESSAGE_DATE + " > " + 0 + " AND "
                                + DBConstants.STATUS + " = " + ChatConstants.CHAT_GROUP_PENDING + " AND "
                                + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                + DBConstants.MSG_SENDER + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                                + "ORDER BY " + DBConstants.MESSAGE_DATE;

                        ResultSet results = stmt.executeQuery(query2);
                        while (results.next()) {
                            long groupId = results.getLong(DBConstants.GROUP_ID);
                            List<MessageBaseDTO> list = pendingGroupChat.get(groupId);
                            if (list == null) {
                                list = new ArrayList<MessageBaseDTO>();
                                pendingGroupChat.put(groupId, list);
                                ChatHelpers.startGroupChat(groupId, false);
                            }

                            MessageBaseDTO entity = new MessageBaseDTO();
                            entity.setGroupId(results.getLong(DBConstants.GROUP_ID));
                            entity.setFullName(results.getString(DBConstants.FULL_NAME));
                            entity.setUserIdentity(results.getString(DBConstants.MSG_SENDER));
                            entity.setMessage(results.getString(DBConstants.MESSAGE));
                            entity.setMessageDate(results.getLong(DBConstants.MESSAGE_DATE));
                            entity.setPacketType(results.getInt(DBConstants.PACKET_TYPE));
                            entity.setTimeout(results.getInt(DBConstants.TIME_OUT));
                            entity.setPacketID(results.getString(DBConstants.PACKET_ID));
                            entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));
                            list.add(entity);
                        }
                    }
                } catch (SQLException ex) {
                    log.error(query2);
                  //  ex.printStackTrace();
                }

            }
        } catch (SQLException ex) {

        } finally {
            try {
                if(stmt!=null){
                stmt.close();}
            } catch (SQLException ex) {
            }
        }

        for (Entry<String, List<MessageBaseDTO>> entrySet : pendingFriendChat.entrySet()) {
            List<MessageBaseDTO> list = entrySet.getValue();
            if (list.size() > 0) {
                ChatService.sendFriendChatInBackground(entrySet.getKey(), list);
            }
        }

        for (Entry<Long, List<MessageBaseDTO>> entrySet : pendingGroupChat.entrySet()) {
            List<MessageBaseDTO> list = entrySet.getValue();
            if (list.size() > 0) {
                ChatService.sendGroupChatInBackground(entrySet.getKey(), list);
            }
        }
    }

}

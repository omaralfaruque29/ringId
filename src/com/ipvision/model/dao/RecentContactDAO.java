/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.model.UserBasicInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.call.CallLog;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class RecentContactDAO {

    private static SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public static final long MILISECONDS_IN_DAY = 86400000;

    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_GROUP = 2;

    public static final int _TODAY = 0;
    public static final int _YESTERDAY = 1;
    public static final int _7_DAYS = 6;
    public static final int _30_DAYS = 29;
    public static final int _90_DAYS = 89;

        /*    public static Map<Integer, List<RecentDTO>> loadRecentAllContactList() {
    
     Date currDate = new Date();
     currDate.setHours(0);
     currDate.setMinutes(0);
     currDate.setSeconds(0);
     long currTime = currDate.getTime();
     long minTime = (currTime - _90_DAYS * MILISECONDS_IN_DAY);
     Statement stmt = null;
    
     Map<Integer, List<RecentDTO>> tempMap = new ConcurrentHashMap<Integer, List<RecentDTO>>();
     List<RecentDTO> tempList = new ArrayList<RecentDTO>();
    
     List<RecentDTO> conYesterday = new ArrayList<RecentDTO>();
     tempMap.put(_YESTERDAY, conYesterday);
     List<RecentDTO> con7days = new ArrayList<RecentDTO>();
     tempMap.put(_7_DAYS, con7days);
     List<RecentDTO> con30days = new ArrayList<RecentDTO>();
     tempMap.put(_30_DAYS, con30days);
     List<RecentDTO> con90days = new ArrayList<RecentDTO>();
     tempMap.put(_90_DAYS, con90days);
    
     try {
     if (DBConstants.conn != null) {
     stmt = DBConstants.conn.createStatement();
     try {
     String query = "SELECT "
     + "        TABLE_2.* "
     + "    FROM "
     + "    ( "
     + "         SELECT "
     + "              " + DBConstants.FRIEND_IDENTITY + ", "
     + "              MAX(" + DBConstants.MESSAGE_DATE + ") AS " + DBConstants.MESSAGE_DATE + " "
     + "         FROM "
     + "         ( "
     + "              SELECT "
     + "                   (CASE WHEN " + DBConstants.GROUP_ID + " IS NOT NULL AND " + DBConstants.GROUP_ID + " > 0 THEN CAST(CAST(" + DBConstants.GROUP_ID + " AS CHAR(20)) AS VARCHAR(20)) ELSE " + DBConstants.FRIEND_IDENTITY + " END) AS " + DBConstants.FRIEND_IDENTITY + ", "
     + "                   " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + " "
     + "              FROM " + DBConstants.TABLE_RING_ACTIVITY + " "
     + "              WHERE "
     + "              " + DBConstants.UPDATE_TIME + " >= " + minTime + " AND "
     + "              " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
     + "          ) AS tTABLE "
     + "          GROUP BY " + DBConstants.FRIEND_IDENTITY + " "
     + "     ) AS TABLE_1 "
     + "     LEFT JOIN "
     + "     ( "
     + "        SELECT "
     + "             (CASE WHEN " + DBConstants.GROUP_ID + " IS NOT NULL AND " + DBConstants.GROUP_ID + " > 0 THEN CAST(CAST(" + DBConstants.GROUP_ID + " AS CHAR(20)) AS VARCHAR(20)) ELSE " + DBConstants.FRIEND_IDENTITY + " END) AS " + DBConstants.FRIEND_IDENTITY + ", "
     + "             " + DBConstants.UPDATE_TIME + " AS " + DBConstants.MESSAGE_DATE + ", "
     + "             '' AS " + DBConstants.MESSAGE + ", "
     + "             " + DBConstants.ACTIVITY_BY + " AS " + DBConstants.MSG_SENDER + ", "
     + "             " + DBConstants.ACTIVITY_TYPE + ", "
     + "             " + DBConstants.MESSAGE_TYPE + " AS " + DBConstants.MESSAGE_TYPE + ", "
     + "             (CASE WHEN " + DBConstants.GROUP_ID + " IS NOT NULL AND " + DBConstants.GROUP_ID + " > 0 THEN " + RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY + " ELSE " + RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY + " END) AS " + DBConstants.TABLE_TYPE + " "
     + "        FROM " + DBConstants.TABLE_RING_ACTIVITY + " "
     + "        WHERE "
     + "        " + DBConstants.UPDATE_TIME + " >= " + minTime + " AND "
     + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
     + "    ) AS TABLE_2 ON TABLE_2." + DBConstants.FRIEND_IDENTITY + " = TABLE_1." + DBConstants.FRIEND_IDENTITY + " AND TABLE_2." + DBConstants.MESSAGE_DATE + " = TABLE_1." + DBConstants.MESSAGE_DATE + " "
     + "    ORDER BY TABLE_1." + DBConstants.MESSAGE_DATE + " DESC";
     ResultSet results = stmt.executeQuery(query);
    
     while (results.next()) {
    
     String friendOrGroupName = results.getString(DBConstants.FRIEND_IDENTITY).trim();
     long messsageDate = results.getLong(DBConstants.MESSAGE_DATE);
     String messsage = results.getString(DBConstants.MESSAGE);
     String msgSender = results.getString(DBConstants.MSG_SENDER);
     int type = results.getInt(DBConstants.ACTIVITY_TYPE);
     int messageType = results.getInt(DBConstants.MESSAGE_TYPE);
     int tableType = results.getInt(DBConstants.TABLE_TYPE);
    
     RecentDTO entity = new RecentDTO();
     entity.setContactId(friendOrGroupName);
     entity.setTime(messsageDate);
    
     if (tableType == RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY) {
     entity.setType(RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY);
     entity.setContactType(TYPE_GROUP);
     ActivityDTO activityDTO = new ActivityDTO();
     activityDTO.setGroupId(Long.parseLong(friendOrGroupName));
     activityDTO.setActivityType(type);
     activityDTO.setMessageType(messageType);
     activityDTO.setActivityBy(msgSender);
     entity.setActivityDTO(activityDTO);
     tempList.add(entity);
     } else {
     UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(friendOrGroupName);
     if (friendInfo != null) {
     if (tableType == RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY) {
     entity.setType(RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY);
     entity.setContactType(TYPE_FRIEND);
     ActivityDTO activityDTO = new ActivityDTO();
     activityDTO.setFriendIdentity(friendOrGroupName);
     activityDTO.setActivityType(type);
     activityDTO.setMessageType(messageType);
     activityDTO.setActivityBy(msgSender);
     entity.setActivityDTO(activityDTO);
     tempList.add(entity);
     }
     }
     }
     }
    
     } catch (SQLException ex) {
     }
     }
     } catch (Exception ex) {
    
     } finally {
     try {
     if (stmt != null) {
     stmt.close();
     }
     } catch (SQLException ex) {
     }
     }
    
     for (RecentDTO entity : tempList) {
     if (entity.getTime() > (currTime - _YESTERDAY * MILISECONDS_IN_DAY)) {
     tempMap.get(_YESTERDAY).add(entity);
     } else if (entity.getTime() > (currTime - _7_DAYS * MILISECONDS_IN_DAY)) {
     tempMap.get(_7_DAYS).add(entity);
     } else if (entity.getTime() > (currTime - _30_DAYS * MILISECONDS_IN_DAY)) {
     tempMap.get(_30_DAYS).add(entity);
     } else {
     tempMap.get(_90_DAYS).add(entity);
     }
     }
    
     return tempMap;
    
     }*/
    public static List<RecentDTO> loadRecentChatContactList() {

        Date currDate = new Date();
        currDate.setHours(0);
        currDate.setMinutes(0);
        currDate.setSeconds(0);
        long currTime = currDate.getTime();
        long minTime = (currTime - _90_DAYS * MILISECONDS_IN_DAY);
        Statement stmt = null;

        List<RecentDTO> tempMap = new ArrayList<RecentDTO>();

        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT "
                            + "        TABLE_2.* "
                            + "    FROM "
                            + "    ( "
                            + "         SELECT "
                            + "              " + DBConstants.FRIEND_IDENTITY + ", "
                            + "              MAX(" + DBConstants.MESSAGE_DATE + ") AS " + DBConstants.MESSAGE_DATE + " "
                            + "         FROM "
                            + "         ( "
                            + "              SELECT "
                            + "                   " + DBConstants.FRIEND_IDENTITY + ", "
                            + "                   " + DBConstants.MESSAGE_DATE + " "
                            + "              FROM "
                            + "              " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID + " "
                            + "              WHERE " + DBConstants.MESSAGE_DATE + " >= " + minTime + " "
                            + "              UNION "
                            + "              SELECT "
                            + "                   CAST(CAST(" + DBConstants.GROUP_ID + " AS CHAR(20)) AS VARCHAR(20)) AS " + DBConstants.FRIEND_IDENTITY + ", "
                            + "                   " + DBConstants.MESSAGE_DATE + " "
                            + "              FROM " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " "
                            + "              WHERE "
                            + "              " + DBConstants.MESSAGE_DATE + " >= " + minTime + " AND "
                            + "              " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                            + "          ) AS tTABLE "
                            + "          GROUP BY " + DBConstants.FRIEND_IDENTITY + " "
                            + "     ) AS TABLE_1 "
                            + "     LEFT JOIN "
                            + "     ( "
                            + "         SELECT "
                            + "             " + DBConstants.FRIEND_IDENTITY + ", "
                            + "             " + DBConstants.MESSAGE_DATE + ", "
                            + "             " + DBConstants.MESSAGE + ", "
                            + "             " + DBConstants.MSG_SENDER + ", "
                            + "             0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                            + "             " + DBConstants.MESSAGE_TYPE + ", "
                            + "             " + RecentChatCallActivityDAO.TYPE_FRIEND_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                            + "        FROM " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID + " "
                            + "        WHERE "
                            + "        " + DBConstants.MESSAGE_DATE + " >= " + minTime + " "
                            + "        UNION "
                            + "        SELECT "
                            + "             CAST(CAST(" + DBConstants.GROUP_ID + " AS CHAR(20)) AS VARCHAR(20)) AS " + DBConstants.FRIEND_IDENTITY + ", "
                            + "             " + DBConstants.MESSAGE_DATE + ", "
                            + "             " + DBConstants.MESSAGE + ", "
                            + "             " + DBConstants.MSG_SENDER + ", "
                            + "             0 AS " + DBConstants.ACTIVITY_TYPE + ", "
                            + "             " + DBConstants.MESSAGE_TYPE + ", "
                            + "             " + RecentChatCallActivityDAO.TYPE_GROUP_CHAT + " AS " + DBConstants.TABLE_TYPE + " "
                            + "        FROM " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " "
                            + "        WHERE "
                            + "        " + DBConstants.MESSAGE_DATE + " >= " + minTime + " AND "
                            + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                            + "    ) AS TABLE_2 ON TABLE_2." + DBConstants.FRIEND_IDENTITY + " = TABLE_1." + DBConstants.FRIEND_IDENTITY + " AND TABLE_2." + DBConstants.MESSAGE_DATE + " = TABLE_1." + DBConstants.MESSAGE_DATE + " "
                            + "    ORDER BY TABLE_1." + DBConstants.MESSAGE_DATE + " DESC";
                    ResultSet results = stmt.executeQuery(query);

                    while (results.next()) {

                        String friendOrGroupName = results.getString(DBConstants.FRIEND_IDENTITY).trim();
                        long messsageDate = results.getLong(DBConstants.MESSAGE_DATE);
                        String messsage = results.getString(DBConstants.MESSAGE);
                        String msgSender = results.getString(DBConstants.MSG_SENDER);
                        int type = results.getInt(DBConstants.ACTIVITY_TYPE);
                        int messageType = results.getInt(DBConstants.MESSAGE_TYPE);
                        int tableType = results.getInt(DBConstants.TABLE_TYPE);

                        RecentDTO entity = new RecentDTO();
                        entity.setContactId(friendOrGroupName);
                        entity.setTime(messsageDate);

                        if (tableType == RecentChatCallActivityDAO.TYPE_GROUP_CHAT) {
                            entity.setType(RecentChatCallActivityDAO.TYPE_GROUP_CHAT);
                            entity.setContactType(TYPE_GROUP);
                            MessageDTO messageDTO = new MessageDTO();
                            messageDTO.setGroupId(Long.parseLong(friendOrGroupName));
                            messageDTO.setMessage(messsage);
                            messageDTO.setUserIdentity(msgSender);
                            messageDTO.setMessageType(messageType);
                            entity.setMessageDTO(messageDTO);
                            tempMap.add(entity);
                        } else {
                            UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(friendOrGroupName);
                            if (friendInfo != null) {
                                if (tableType == RecentChatCallActivityDAO.TYPE_FRIEND_CHAT) {
                                    entity.setType(RecentChatCallActivityDAO.TYPE_FRIEND_CHAT);
                                    entity.setContactType(TYPE_FRIEND);
                                    MessageDTO messageDTO = new MessageDTO();
                                    messageDTO.setMessage(messsage);
                                    messageDTO.setFriendIdentity(friendOrGroupName);
                                    messageDTO.setUserIdentity(msgSender);
                                    messageDTO.setMessageType(messageType);
                                    entity.setMessageDTO(messageDTO);
                                    tempMap.add(entity);
                                }
                            }
                        }
                    }

                } catch (SQLException ex) {
                }
            }
        } catch (Exception ex) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
            }
        }

        return tempMap;
    }

    public static void deletechatHistoryFromDB(List<RecentDTO> deleteMsgID) {

        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    for (RecentDTO ins : deleteMsgID) {
                        String query = "";
                        if (ins.getMessageDTO().getGroupId() != null) {
                            query = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                                    + " WHERE "
                                    + DBConstants.GROUP_ID + "=" + ins.getMessageDTO().getGroupId() + "";
                        } else {
                            query = "DELETE FROM  " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID
                                    + " WHERE "
                                    + DBConstants.FRIEND_IDENTITY + "='" + ins.getContactId() + "'";
                        }
                        //String query = "DELETE FROM  " + DBConstants.TABLE_RING_CONTACT_LIST + " WHERE " + DBConstants.ID + "= '" + ins + "'";
                        stmt.executeUpdate(query);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } catch (SQLException ex) {
            //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteRecentActivityFromDB(List<RecentDTO> recentList) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    for (RecentDTO recent : recentList) {
                        String query = "";
                        if (recent.getActivityDTO().getGroupId() != null && recent.getActivityDTO().getGroupId() > 0) {
                            query = "DELETE FROM  " + DBConstants.TABLE_RING_ACTIVITY
                                    + " WHERE "
                                    + DBConstants.GROUP_ID + "=" + recent.getActivityDTO().getGroupId() + "";
                        } else {
                            query = "DELETE FROM  " + DBConstants.TABLE_RING_ACTIVITY
                                    + " WHERE "
                                    + DBConstants.FRIEND_IDENTITY + "='" + recent.getActivityDTO().getFriendIdentity() + "'";
                        }
                        //String query = "DELETE FROM  " + DBConstants.TABLE_RING_CONTACT_LIST + " WHERE " + DBConstants.ID + "= '" + ins + "'";
                        stmt.executeUpdate(query);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
            //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteCallHistoryFromDB(List<String> callIds) {

        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    for (String ins : callIds) {
                        String query = "DELETE FROM  " + DBConstants.TABLE_RING_CALL_LOG + " WHERE " + DBConstants.ID + "= '" + ins + "'";
                        stmt.executeUpdate(query);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } catch (SQLException ex) {
            //  Logger.getLogger(GetFriendListFromTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<RecentDTO> loadRecentCallContactList() {

        Date currDate = new Date();
        currDate.setHours(0);
        currDate.setMinutes(0);
        currDate.setSeconds(0);
        long currTime = currDate.getTime();
        long minTime = (currTime - _90_DAYS * MILISECONDS_IN_DAY);
        Statement stmt = null;

        List<RecentDTO> tempMap = new ArrayList<RecentDTO>();

        RecentDTO entity = null;
        Map<String, Integer> sequenceMap = new ConcurrentHashMap<String, Integer>();

        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();
                try {
                    String query = "   SELECT "
                            + "             " + DBConstants.FRIEND_IDENTITY + ", "
                            + "             " + DBConstants.ID + ", "
                            + "             " + DBConstants.CL_CALLING_TIME + ", "
                            + "             " + DBConstants.CL_CALL_TYPE + ", "
                            + "             " + DBConstants.CL_CALL_DURATION + " "
                            + "        FROM " + DBConstants.TABLE_RING_CALL_LOG + " "
                            + "        WHERE "
                            + "        " + DBConstants.CL_CALLING_TIME + " >= " + minTime + " AND "
                            + "        " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' "
                            + "        ORDER BY " + DBConstants.CL_CALLING_TIME + " DESC ";
                    ResultSet results = stmt.executeQuery(query);

                    while (results.next()) {
                        String friendIdentity = results.getString(DBConstants.FRIEND_IDENTITY).trim();
                        int type = results.getInt(DBConstants.CL_CALL_TYPE);
                        String callID = results.getString(DBConstants.ID);
                        //System.out.println("jhkv");
                        if (entity == null || !entity.getCallLog().getFriendIdentity().equalsIgnoreCase(friendIdentity) || entity.getCallLog().getCallType() != type) {
                            UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(friendIdentity);
                            if (friendInfo != null) {
                                long messsageDate = results.getLong(DBConstants.CL_CALLING_TIME);
                                long callDuration = results.getLong(DBConstants.CL_CALL_DURATION);

                                entity = new RecentDTO();
                                entity.setContactId(friendIdentity);
                                entity.setTime(messsageDate);
                                entity.setType(RecentChatCallActivityDAO.TYPE_CALL_LOG);
                                entity.setContactType(TYPE_FRIEND);
                                entity.setCallCount(1);
                                Integer sequence = sequenceMap.get(type + "_" + friendIdentity);
                                if (sequence == null) {
                                    sequence = 1;
                                    sequenceMap.put(type + "_" + friendIdentity, sequence);
                                } else {
                                    sequence += 1;
                                    sequenceMap.put(type + "_" + friendIdentity, sequence);
                                }
                                entity.setSequence(sequence);
                                CallLog callLog = new CallLog();
                                callLog.setFriendIdentity(friendIdentity);
                                callLog.setCallType(type);
                                callLog.setCallDuration(callDuration);
                                callLog.setCallingTime(messsageDate);
                                entity.setCallLog(callLog);
                                ArrayList<String> callIds = new ArrayList<>();
                                callIds.add(callID);
                                entity.setCallIds(callIds);
                                tempMap.add(entity);
                            }
                        } else {
                            entity.setCallCount(entity.getCallCount() + 1);
                            entity.getCallIds().add(callID);
                        }
                        //  System.out.println("sfcas");
                    }
                } catch (SQLException ex) {
                }
            }
        } catch (Exception ex) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
            }
        }

        return tempMap;
    }

}

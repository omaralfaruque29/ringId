/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author Shahadat
 */
public class GetSingleMessageDAO {

    private static SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    public static MessageDTO getMessageDtoByPacketId(String friendId, Long groupId, String packetId) {
        Statement stmt = null;
        MessageDTO entity = null;

        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                if (friendId != null && friendId.trim().length() > 0) {
                    try {

                        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2} AND {3} = {4} ORDER BY {5} DESC",
                                DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID,
                                DBConstants.PACKET_ID,
                                "'" + packetId + "'",
                                DBConstants.FRIEND_IDENTITY,
                                "'" + friendId + "'",
                                DBConstants.MESSAGE_DATE).replace(",", "");
                        ResultSet results = stmt.executeQuery(query);

                        if (results.next()) {
                            entity = new MessageDTO();
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
                        }

                    } catch (SQLException ex) {
                    }
                }

                if (groupId != null && groupId > 0) {
                    try {

                        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2} AND {3} = {4} AND {5} = {6} ORDER BY {7} DESC",
                                DBConstants.TABLE_RING_GROUP_CHAT_TABLE,
                                DBConstants.PACKET_ID,
                                "'" + packetId + "'",
                                DBConstants.GROUP_ID,
                                groupId,
                                DBConstants.LOGIN_USER_ID,
                                "'" + MyFnFSettings.LOGIN_USER_ID + "'",
                                DBConstants.MESSAGE_DATE).replace(",", "");
                        ResultSet results = stmt.executeQuery(query);

                        if (results.next()) {
                            entity = new MessageDTO();
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
                        }

                    } catch (SQLException ex) {
                    }
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

        return entity;
    }

}

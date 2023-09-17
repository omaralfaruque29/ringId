/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipvision.constants.MyFnFSettings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author shahadat
 */
public class BrokenChatDAO {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BrokenChatDAO.class);

    public synchronized static MessageDTO getMergedBorkenChat(MessageBaseDTO messageBaseDTO) {
        MessageDTO messageDTO = null;
        List<MessageDTO> list = new ArrayList<MessageDTO>();
        Statement stmt = null;

        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();
                try {
                    String packetID = messageBaseDTO.getPacketID() != null ? messageBaseDTO.getPacketID() : "";
                    String friendId = messageBaseDTO.getFriendIdentity() != null ? messageBaseDTO.getFriendIdentity() : "";
                    String contactId = messageBaseDTO.getGroupId() != null && messageBaseDTO.getGroupId() > 0 ? (messageBaseDTO.getGroupId() + "") : messageBaseDTO.getFriendIdentity();
                    int packetType = messageBaseDTO.getPacketType();
                    String message = messageBaseDTO.getMessage() != null ? messageBaseDTO.getMessage().replace("'", "''") : "";
                    String fullName = messageBaseDTO.getFullName() != null ? messageBaseDTO.getFullName().replace("'", "''") : "";

                    String query = "INSERT INTO " + DBConstants.TABLE_RING_BROKEN_CHAT
                            + "	("
                            + DBConstants.LOGIN_USER_ID + ","
                            + DBConstants.CONTACT_ID + ","
                            + DBConstants.FRIEND_IDENTITY + ","
                            + DBConstants.GROUP_ID + ","
                            + DBConstants.USER_IDENTITY + ","
                            + DBConstants.FULL_NAME + ","
                            + DBConstants.MESSAGE + ","
                            + DBConstants.MESSAGE_DATE + ","
                            + DBConstants.TIME_OUT + ","
                            + DBConstants.PACKET_TYPE + ","
                            + DBConstants.PACKET_ID + ","
                            + DBConstants.NUMBER_OF_PACKET + ","
                            + DBConstants.SEQUENCE_NO + ","
                            + DBConstants.MESSAGE_TYPE + ")"
                            + " VALUES "
                            + "	("
                            + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                            + "'" + contactId + "',"
                            + "'" + friendId + "',"
                            + "" + messageBaseDTO.getGroupId() + ","
                            + "'" + messageBaseDTO.getUserIdentity() + "',"
                            + "'" + fullName + "',"
                            + "'" + message + "',"
                            + " " + messageBaseDTO.getMessageDate() + ", "
                            + " " + messageBaseDTO.getTimeout() + ", "
                            + " " + messageBaseDTO.getPacketType() + ", "
                            + "'" + packetID + "',"
                            + " " + messageBaseDTO.getNumberOfPacket() + ", "
                            + " " + messageBaseDTO.getSequenceNumber() + ", "
                            + " " + messageBaseDTO.getMessageType()
                            + ")";

                    try {
                            stmt.execute(query);
                    } catch (SQLException ex) {
                        // ex.printStackTrace();
                    }

                    String query1 = "SELECT "
                            + "     M.* "
                            + " FROM " + DBConstants.TABLE_RING_BROKEN_CHAT + " AS M "
                            + " WHERE "
                            + "     M." + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                            + "     M." + DBConstants.CONTACT_ID + " = '" + contactId + "' AND "
                            + "     M." + DBConstants.PACKET_ID + " = '" + packetID + "' AND "
                            + "     M." + DBConstants.PACKET_TYPE + " = " + packetType + " AND "
                            + "     (M." + DBConstants.NUMBER_OF_PACKET + " * (M." + DBConstants.NUMBER_OF_PACKET + " + 1) / 2) = "
                            + "     (SELECT SUM(" + DBConstants.SEQUENCE_NO + ") FROM " + DBConstants.TABLE_RING_BROKEN_CHAT + " AS T "
                            + "             WHERE "
                            + "             T." + DBConstants.LOGIN_USER_ID + " = M." + DBConstants.LOGIN_USER_ID + " AND "
                            + "             T." + DBConstants.CONTACT_ID + " = M." + DBConstants.CONTACT_ID + " AND "
                            + "             T." + DBConstants.PACKET_TYPE + " = M." + DBConstants.PACKET_TYPE + " AND "
                            + "             T." + DBConstants.PACKET_ID + " = M." + DBConstants.PACKET_ID + " "
                            + "      GROUP BY T." + DBConstants.PACKET_ID + ") "
                            + " ORDER BY M." + DBConstants.SEQUENCE_NO + "";
                    ResultSet results = stmt.executeQuery(query1);

                    while (results.next()) {
                        MessageDTO entity = new MessageDTO();
                        entity.setGroupId(results.getLong(DBConstants.GROUP_ID));
                        entity.setFriendIdentity(results.getString(DBConstants.FRIEND_IDENTITY));
                        entity.setUserIdentity(results.getString(DBConstants.USER_IDENTITY));
                        entity.setMessage(results.getString(DBConstants.MESSAGE));
                        entity.setMessageDate(results.getLong(DBConstants.MESSAGE_DATE));
                        entity.setFullName(results.getString(DBConstants.FULL_NAME));
                        entity.setPacketType(results.getInt(DBConstants.PACKET_TYPE));
                        entity.setTimeout(results.getInt(DBConstants.TIME_OUT));
                        entity.setPacketID(results.getString(DBConstants.PACKET_ID));
                        entity.setMessageType(results.getInt(DBConstants.MESSAGE_TYPE));
                        entity.setNumberOfPacket(results.getInt(DBConstants.NUMBER_OF_PACKET));
                        entity.setSequenceNumber(results.getInt(DBConstants.SEQUENCE_NO));
                        list.add(entity);
                    }

                    if (list.size() > 0) {
                        String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_BROKEN_CHAT
                                + " WHERE "
                                + DBConstants.CONTACT_ID + " = '" + contactId + "' AND "
                                + DBConstants.PACKET_ID + " = '" + packetID + "' AND "
                                + DBConstants.PACKET_TYPE + " = " + packetType + " AND "
                                + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                            stmt.executeUpdate(query2);
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                log.error("SQLException in BrokenChatDAO class ==>" + ex.getMessage());
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

        if (list.size() > 0) {
            messageDTO = list.get(0);
            String msg = "";
            for (MessageDTO msgDTO : list) {
                msg += msgDTO.getMessage();
            }
            int status = messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0 ? ChatConstants.CHAT_GROUP_DELIVERED : ChatConstants.CHAT_FRIEND_DELIVERED;
            messageDTO.setStatus(status);
            messageDTO.setSystemDate(messageDTO.getMessageDate());
            messageDTO.setMessage(msg);
        }

        return messageDTO;
    }

}

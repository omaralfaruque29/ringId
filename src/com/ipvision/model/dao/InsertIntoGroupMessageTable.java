/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipv.chat.ChatConstants;
import com.ipvision.constants.MyFnFSettings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author Faiz Ahmed
 */
public class InsertIntoGroupMessageTable extends Thread {

    private List<MessageDTO> messageDTOs;

    public InsertIntoGroupMessageTable(List<MessageDTO> messageDTOs) {
        this.messageDTOs = messageDTOs;
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

            for (MessageDTO messageDTO : messageDTOs) {
                String packetId = (messageDTO.getPacketID() != null ? messageDTO.getPacketID() : "");

                if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_MESSAGE_DELETED) {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " WHERE "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                            + DBConstants.GROUP_ID + "=" + messageDTO.getGroupId() + " AND "
                            + DBConstants.PACKET_ID + "='" + packetId + "'";
                    stmt.executeUpdate(query);
                } else {
                    String fullName = messageDTO.getFullName() != null ? messageDTO.getFullName().replace("'", "''") : "";
                    String message = messageDTO.getMessage() != null ? messageDTO.getMessage().replace("'", "''") : "";
                    try {
                        String sql = "INSERT INTO " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                                + "	(" + DBConstants.LOGIN_USER_ID + "," + DBConstants.GROUP_ID + "," + DBConstants.MSG_SENDER + "," + DBConstants.FULL_NAME + "," + DBConstants.MESSAGE + "," + DBConstants.MESSAGE_DATE + "," + DBConstants.SYSTEM_DATE + "," + DBConstants.TIME_OUT + "," + DBConstants.PACKET_TYPE + "," + DBConstants.STATUS + "," + DBConstants.PACKET_ID + "," + DBConstants.MESSAGE_TYPE + ")"
                                + " VALUES "
                                + "	("
                                + "'" + MyFnFSettings.LOGIN_USER_ID + "',"
                                + "" + messageDTO.getGroupId() + ","
                                + "'" + messageDTO.getUserIdentity() + "',"
                                + "'" + fullName + "',"
                                + "'" + message + "',"
                                + " " + messageDTO.getMessageDate() + ", "
                                + " " + messageDTO.getSystemDate() + ", "
                                + " " + messageDTO.getTimeout() + ", "
                                + " " + messageDTO.getPacketType() + ", "
                                + " " + messageDTO.getStatus() + ", "
                                + "'" + packetId + "',"
                                + " " + messageDTO.getMessageType()
                                + ")";
                        stmt.execute(sql);
                    } catch (SQLException sqlExcept) {
                        if (sqlExcept.getSQLState().equalsIgnoreCase("23505")) {//duplicate
                            String sql = "UPDATE " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                                    + " SET "
                                    + DBConstants.MESSAGE + " ='" + message + "', "
                                    + DBConstants.MESSAGE_DATE + " =" + messageDTO.getMessageDate() + ", "
                                    + DBConstants.PACKET_TYPE + " =" + messageDTO.getPacketType() + ", "
                                    + DBConstants.STATUS + " =" + messageDTO.getStatus() + ", "
                                    + DBConstants.TIME_OUT + " =" + messageDTO.getTimeout() + ", "
                                    + DBConstants.MESSAGE_TYPE + " =" + messageDTO.getMessageType() + ", "
                                    + DBConstants.PACKET_ID + " ='" + (messageDTO.getPacketID() != null ? messageDTO.getPacketID() : "") + "' "
                                    + " WHERE "
                                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                                    + DBConstants.GROUP_ID + "=" + messageDTO.getGroupId() + " AND "
                                    + DBConstants.PACKET_ID + " ='" + packetId + "' ";
                            stmt.executeUpdate(sql);
                        } else {
                            throw new Exception();
                        }
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

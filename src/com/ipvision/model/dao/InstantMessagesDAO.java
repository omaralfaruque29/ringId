/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.InstantMessageDTO;

/**
 *
 * @author Sirat Samyoun
 */
public class InstantMessagesDAO {

    public static List<InstantMessageDTO> getInstantMessagesFromDB() {
        Statement stmt = null;
        List<InstantMessageDTO> instantMessages = new ArrayList<>();
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_INSTANT_MESSAGES;
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            InstantMessageDTO instantMessageDTO = new InstantMessageDTO();
                            String insMsg = results.getString(DBConstants.INS_MSG);
                            int insType = results.getInt(DBConstants.INS_MSG_TYPE);
                            instantMessageDTO.setInstantMessage(insMsg);
                            instantMessageDTO.setMsgType(insType);
                            instantMessages.add(instantMessageDTO);
                        }
                    }
                } catch (SQLException ex) {
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
        return instantMessages;
    }

    public static void deleteInstantMessagesFromDB(List<InstantMessageDTO> insMsgs) {

        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    for (InstantMessageDTO ins : insMsgs) {
                        String query = "DELETE FROM  " + DBConstants.TABLE_RING_INSTANT_MESSAGES + " WHERE " + DBConstants.INS_MSG + "= '" + ins.getInstantMessage()
                                + "' AND " + DBConstants.INS_MSG_TYPE + "=" + ins.getMsgType() + "";
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

    public static void deleteSingleInstantMessageFromDB(InstantMessageDTO insMsg) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "DELETE FROM  " + DBConstants.TABLE_RING_INSTANT_MESSAGES + " WHERE " + DBConstants.INS_MSG + "= '" + insMsg.getInstantMessage()
                            + "' AND " + DBConstants.INS_MSG_TYPE + "=" + insMsg.getMsgType() + "";
                    stmt.executeUpdate(query);
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

    public static void insertSingleInstantMessagetoDB(InstantMessageDTO insMsg) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String str = "INSERT INTO " + DBConstants.TABLE_RING_INSTANT_MESSAGES
                            + " (" + DBConstants.INS_MSG + "," + DBConstants.INS_MSG_TYPE + ")"
                            + " VALUES "
                            + " ("
                            + "'" + insMsg.getInstantMessage() + "',"
                            + "" + insMsg.getMsgType() + ""
                            + ")";
                    stmt.execute(str);
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
}

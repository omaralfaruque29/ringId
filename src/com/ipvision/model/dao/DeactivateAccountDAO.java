/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.ipvision.model.constants.DBConstants;

/**
 * @author Shahadat Hossain
 */
public class DeactivateAccountDAO {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeactivateAccountDAO.class);

    public static void trancatUserInfo() {
        Statement stmt = null;
        try {
            if (DBConstants.conn != null) {
                stmt = DBConstants.conn.createStatement();

                DeactivateAccountDAO.deleteFriendChatTable(stmt);
                DeactivateAccountDAO.deleteRingCallLog(stmt);
                DeactivateAccountDAO.deleteRingContactList(stmt);
                DeactivateAccountDAO.deleteRingCircleList(stmt);
                DeactivateAccountDAO.deleteRingGroupList(stmt);
                DeactivateAccountDAO.deleteRingMailMessage(stmt);
                DeactivateAccountDAO.deleteRingMarketStickerCategory(stmt);
                DeactivateAccountDAO.deleteRingMarketSticker(stmt);
                DeactivateAccountDAO.deleteRingNotificationHistory(stmt);
                DeactivateAccountDAO.deleteRingSmsHistory(stmt);
                DeactivateAccountDAO.deleteRingGroupChat(stmt);
                DeactivateAccountDAO.deleteRingUserSettings(stmt);
                DeactivateAccountDAO.deleteRingActivity(stmt);
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

    private static void deleteFriendChatTable(Statement stmt) {
        log.info("Deleting FriendChat Info .............");
        try {
            String query = "DROP TABLE " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID;
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting FriendChat Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingCallLog(Statement stmt) {
        log.info("Deleting RingCallLog Info .............");
        try {
            String query = "DELETE FROM  " + DBConstants.TABLE_RING_CALL_LOG + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingCallLog Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingContactList(Statement stmt) {
        log.info("Deleting ContactList Info .............");
        try {
            String query = "DELETE FROM  " + DBConstants.TABLE_RING_CONTACT_LIST + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting ContactList Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingCircleList(Statement stmt) {
        log.info("Deleting RingCircleList Info .............");
        try {
            if (stmt != null) {
                String circleIds = "0";

                String query = "SELECT * FROM  " + DBConstants.TABLE_RING_CIRCLE_LIST + " WHERE " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";

                ResultSet results = stmt.executeQuery(query);
                if (!results.isClosed()) {
                    while (results.next()) {
                        circleIds += "," + results.getLong(2) + "";
                    }
                }

                String query1 = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_LIST + " WHERE " + DBConstants.CIRCLE_ID + " IN (" + circleIds + ") AND "
                        + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                stmt.executeUpdate(query1);

                String query2 = "DELETE FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST + " WHERE " + DBConstants.CIRCLE_ID + " IN (" + circleIds + ") AND "
                        + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                stmt.executeUpdate(query2);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingCircleList Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingGroupList(Statement stmt) {
        log.info("Deleting RingGroupList Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_GROUP_LIST + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingGroupList Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingMailMessage(Statement stmt) {
        log.info("Deleting RingMailMessage Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_MAIL_MESSAGE + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingMailMessage Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingMarketStickerCategory(Statement stmt) {
        log.info("Deleting RingMarketStickerCategory Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingMarketStickerCategory Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingMarketSticker(Statement stmt) {
        log.info("Deleting RingMarketSticker Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_MARKET_STICKER + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingMarketSticker Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingNotificationHistory(Statement stmt) {
        log.info("Deleting RingNotificationHistory Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_NOTIFICATION_HISTORY + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingNotificationHistory Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingSmsHistory(Statement stmt) {
        log.info("Deleting RingSmsHistory Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_SMS_HISTORY + " WHERE "
                    + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingSmsHistory Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingGroupChat(Statement stmt) {
        log.info("Deleting RingGroupChat Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE + " WHERE "
                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingGroupChat Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingUserSettings(Statement stmt) {
        log.info("Deleting RingUserSettings Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_USER_SETTINGS + " WHERE "
                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingUserSettings Info. Exception :: " + e.getMessage());
        }
    }

    private static void deleteRingActivity(Statement stmt) {
        log.info("Deleting RingActivity Info .............");
        try {
            String query = "DELETE FROM " + DBConstants.TABLE_RING_ACTIVITY + " WHERE "
                    + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
            if (stmt != null) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error("Error occured during deleting RingActivity Info. Exception :: " + e.getMessage());
        }
    }

}

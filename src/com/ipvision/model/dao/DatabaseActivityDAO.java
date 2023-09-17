/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.dao;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.constants.StatusConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.constants.DBConstants;
import com.ipvision.model.EmotionDtos;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerImagesDTO;
import com.ipvision.model.stores.StickerStorer;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.HashMapsBeforeLogin;
import com.ipvision.model.stores.MyfnfHashMaps;
import java.sql.Connection;

/**
 *
 * @author shahadat
 */
public class DatabaseActivityDAO {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DatabaseActivityDAO.class);
    private static DatabaseActivityDAO instance;

    public static DatabaseActivityDAO getInstance() {
        if (instance == null) {
            instance = new DatabaseActivityDAO();
        }
        return instance;
    }

    /*
     CREATE DATABASE
     */
    public void createDatabaseConnection(boolean showMessage) {

        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            String url = "jdbc:derby:" + DBConstants.DB_NAME + ";create=true";
            Class.forName(driver);
            if (DBConstants.conn == null) {
                try {
                    DBConstants.conn = DriverManager.getConnection(url);
                    // DBConstants.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    DBConstants.dbm = DBConstants.conn.getMetaData();
                } catch (SQLException ex) {
                    log.error("Error occured during creating database table. Exception :: " + ex.getMessage());
                    if (ex.getSQLState().equals("XJ040")) {
                        if (showMessage) {
                            HelperMethods.showPlaneDialogMessage("Another version of " + StaticFields.APP_NAME + " already runnig. ");
                        }
                        System.exit(0);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
        }
    }


    /*
     CREATE TABLE
     */
    public void createRingLoginSettingTable(Statement statement) {
        log.info("Creating RingLoginSetting Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tblLogin = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_LOGIN_SETTINGS).toUpperCase(), null);
                    if (!tblLogin.isClosed() && !tblLogin.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_LOGIN_SETTINGS
                                + " ("
                                + DBConstants.LOGIN_KEY + " VARCHAR(50) NOT NULL,"
                                + DBConstants.LOGIN_VALUE + " VARCHAR(10000),"
                                + " CONSTRAINT RING_LOGIN_SETTINGS_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_KEY + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating RingLoginSetting Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating RingLoginSetting Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating RingLoginSetting Table. Exception :: " + ex.getMessage());
        }
    }

    public void createEmotionsTable(Statement statement) {
        log.info("Creating Emotions Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_EMOTICONS).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_EMOTICONS
                                + "("
                                + DBConstants.ID + " BIGINT,"
                                + DBConstants.EMO_NAME + " VARCHAR(50),"
                                + DBConstants.EMO_SYMBOL + " VARCHAR(50),"
                                + DBConstants.EMO_URL + " VARCHAR(50),"
                                + DBConstants.EMO_TYPE + " SMALLINT,"
                                + " CONSTRAINT unique_emoticons" + System.currentTimeMillis() + 1000 + " UNIQUE (" + DBConstants.EMO_SYMBOL + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();

                        BufferedReader in = new BufferedReader(new FileReader(MyFnFSettings.SCRIPT_FOLDER + File.separator + MyFnFSettings.EMOTICONS_INSERT_FILE));
                        String script = "";
                        while (in.ready()) {
                            script += in.readLine();
                        }
                        in.close();

                        if (script.length() > 0) {
                            stmt.execute(script);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating Emotions Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating Emotions Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating Emotions Table. Exception :: " + ex.getMessage());
        }
    }

    public void createInstantMessagesTable(Statement statement) {
        log.info("Creating Instant Messages Table .............");
        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }
                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_INSTANT_MESSAGES).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {
                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_INSTANT_MESSAGES
                                + "("
                                + DBConstants.INS_MSG + " VARCHAR(100),"
                                + DBConstants.INS_MSG_TYPE + " SMALLINT,"
                                + " CONSTRAINT unique_instant_messages" + System.currentTimeMillis() + 1000 + " UNIQUE (" + DBConstants.INS_MSG + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();

                        BufferedReader in = new BufferedReader(new FileReader(MyFnFSettings.SCRIPT_FOLDER + File.separator + MyFnFSettings.INSTANT_MESSAGES_INSERT_FILE));
                        String script = "";
                        while (in.ready()) {
                            script += in.readLine();
                        }
                        in.close();

                        if (script.length() > 0) {
                            stmt.execute(script);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating Instant Messages Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating Instant Messages Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating Instant Messages Table. Exception :: " + ex.getMessage());
        }
    }

    public void createRingMarketStickerCategoriesTable(Statement statement) {
        log.info("Creating StickerMarketCategories Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY
                                + "("
                                + DBConstants.STK_CATEGORY_ID + " INTEGER,"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(20),"
                                + DBConstants.STK_CATEGORY_NAME + " VARCHAR(150),"
                                + DBConstants.STK_COLLECTION_ID + " INTEGER,"
                                + DBConstants.STK_CATEGORY_BANNER_IMAGE + " VARCHAR(100),"
                                + DBConstants.STK_CATEGORY_RANK + " INTEGER,"
                                + DBConstants.STK_CATEGORY_DETAIL_IMAGE + " VARCHAR(100),"
                                + DBConstants.STK_CATEGORY_IS_NEW + " BOOLEAN,"
                                + DBConstants.STK_CATEGORY_ICON + " VARCHAR(100),"
                                + DBConstants.STK_CATEGORY_PRIZE + " FLOAT,"
                                + DBConstants.STK_CATEGORY_IS_FREE + " BOOLEAN,"
                                + DBConstants.STK_CATEGORY_DESCRIPTION + " VARCHAR(1000),"
                                + DBConstants.STK_CATEGORY_IS_DOWNLOADED + " BOOLEAN,"
                                + " CONSTRAINT unique_StickerMarketCategories" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.STK_CATEGORY_ID + ", " + DBConstants.LOGIN_USER_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating StickerMarketCategories Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating StickerMarketCategories Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating StickerMarketCategories Table. Exception :: " + ex.getMessage());
        }
    }

    public void createRingMarketStickerTable(Statement statement) {
        log.info("Creating RingMarketSticker Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_MARKET_STICKER).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_MARKET_STICKER
                                + "("
                                + DBConstants.STK_IMAGE_ID + " INTEGER,"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(20),"
                                + DBConstants.STK_IMAGE_URL + " VARCHAR(50),"
                                + DBConstants.STK_CATEGORY_ID + " INTEGER,"
                                + " CONSTRAINT unique_RingMarketSticker" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.STK_IMAGE_ID + ", " + DBConstants.STK_CATEGORY_ID + ", " + DBConstants.LOGIN_USER_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating RingMarketSticker Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating RingMarketSticker Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating RingMarketSticker Table. Exception :: " + ex.getMessage());
        }
    }

    public void createRingUserSettings(Statement statement) {
        log.info("Creating RingUserSettings Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablets = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_USER_SETTINGS).toUpperCase(), null);
                    if (!tablets.isClosed() && !tablets.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_USER_SETTINGS
                                + "("
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25) NOT NULL,"
                                + DBConstants.USERINFO_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.GROUP_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.CONTACT_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.CIRCLE_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.CIRCLE_MEMBER_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.CALL_LOG_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.SMS_LOG_UT + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.LAST_LOGIN_TIME + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.CHAT_BG + " VARCHAR(100),"
                                + " CONSTRAINT ring_settings" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating RingUserSettings Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating RingUserSettings Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating RingUserSettings Table. Exception :: " + ex.getMessage());
        }
    }

    public void createGroupListTable(Statement statement) {
        log.info("Creating createGroupList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, DBConstants.TABLE_RING_GROUP_LIST.toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_GROUP_LIST
                                + "  (" + ""
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(20) NOT NULL,"
                                + DBConstants.GROUP_ID + "  BIGINT NOT NULL,"
                                + DBConstants.GROUP_NAME + "  VARCHAR(200) NOT NULL,"
                                + DBConstants.GROUP_UT + "  BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.GROUP_OWNER_USER_TABLE_ID + "  BIGINT,"
                                + "CONSTRAINT groupList_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.GROUP_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating createGroupList Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating createGroupList Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating createGroupList Table. Exception :: " + ex.getMessage());
        }
    }

    public void createGroupMemberListTable(Statement statement) {
        log.info("Creating GroupMemberList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, DBConstants.TABLE_RING_GROUP_MEMBERS_LIST.toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST
                                + "  (" + ""
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(20) NOT NULL,"
                                + DBConstants.GROUP_ID + "  BIGINT NOT NULL,"
                                + DBConstants.USER_IDENTITY + " VARCHAR(20),"
                                //+ DBConstants.USER_TABLE_ID + "  BIGINT,"
                                + DBConstants.FULL_NAME + " VARCHAR(300),"
                                + "CONSTRAINT groupList_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + "," + DBConstants.GROUP_ID + "," + DBConstants.USER_IDENTITY + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating GroupMemberList Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating GroupMemberList Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating GroupMemberList Table. Exception :: " + ex.getMessage());
        }
    }

    public void createContractListTable(Statement statement) {
        log.info("Creating ContractList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, DBConstants.TABLE_RING_CONTACT_LIST.toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_CONTACT_LIST
                                + "  (" + ""
                                + DBConstants.LOGIN_USER_ID + "    VARCHAR(20),"
                                + DBConstants.USER_IDENTITY + "    VARCHAR(20),"
                                + DBConstants.USER_TABLE_ID + "    BIGINT,"
                                + DBConstants.FULL_NAME + "    VARCHAR(200),"
                                + DBConstants.EMAIL + "    VARCHAR(200),"
                                + DBConstants.GENDER + "    VARCHAR(50),"
                                + DBConstants.MOBILE_PHONE + "    VARCHAR(20),"
                                + DBConstants.COUNTRY + "    VARCHAR(50),"
                                + DBConstants.BIRTHDAY + "    VARCHAR(50),"
                                + DBConstants.WHAT_IS_IN_YOUR_MIND + "    VARCHAR(500),"
                                + DBConstants.FRIENDSHIP_STATUS + "    SMALLINT,"
                                + DBConstants.MOBILE_PHONE_DIALING_CODE + "    VARCHAR(10),"
                                + DBConstants.PROFILE_IMAGE + "   VARCHAR(250),"
                                + DBConstants.PROFILE_IMAGE_ID + "   BIGINT,"
                                + DBConstants.COVER_IMAGE + "   VARCHAR(250),"
                                + DBConstants.COVER_IMAGE_ID + "    BIGINT,"
                                + DBConstants.COVER_IMAGE_X + "    BIGINT,"
                                + DBConstants.COVER_IMAGE_Y + "    BIGINT,"
                                + DBConstants.RING_EMAIL + "    VARCHAR(200),"
                                + DBConstants.EMAIL_PRIVACY + "    SMALLINT,"
                                + DBConstants.MOBILE_PRIVACY + "    SMALLINT,"
                                + DBConstants.PROFILE_IMAGE_PRIVACY + "    SMALLINT,"
                                + DBConstants.BIRTHDAY_PRIVACY + "    SMALLINT,"
                                + DBConstants.COVER_IMAGE_PRIVACY + "    SMALLINT,"
                                + DBConstants.DEVICE_TOKEN + "    VARCHAR(1100),"
                                + DBConstants.CONTACT_TYPE + "  SMALLINT NOT NULL DEFAULT 0,"
                                + DBConstants.NEW_CONTACT_TYPE + "  SMALLINT NOT NULL DEFAULT 0,"
                                + DBConstants.IS_CHANGE_REQUESTER + " BOOLEAN,"
                                + DBConstants.IS_BLOCKED + "  SMALLINT NOT NULL DEFAULT 0,"
                                + DBConstants.INCOMING_NOTIFICATION + " BOOLEAN,"
                                + DBConstants.UPDATE_TIME + "  BIGINT NOT NULL DEFAULT 0,"
                                + "CONSTRAINT ContractList" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.USER_IDENTITY + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating ContractList Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating ContractList Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating ContractList Table. Exception :: " + ex.getMessage());
        }
    }

    public void createCircleListTable(Statement statement) {
        log.info("Creating CircleList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_CIRCLE_LIST).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_CIRCLE_LIST
                                + "("
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25),"
                                + DBConstants.CIRCLE_ID + " BIGINT,"
                                + DBConstants.CIRCLE_NAME + " VARCHAR(100),"
                                + DBConstants.SUPER_ADMIN + " VARCHAR(25),"
                                + DBConstants.UPDATE_TIME + " BIGINT NOT NULL DEFAULT 0,"
                                + " CONSTRAINT circlelist" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.CIRCLE_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating CircleList Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating CircleList Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating CircleList Table. Exception :: " + ex.getMessage());
        }
    }

    public void createCircleMemberListTable(Statement statement) {
        log.info("Creating CircleMemberList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                                + "("
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25),"
                                + DBConstants.CIRCLE_ID + " BIGINT,"
                                + DBConstants.ADMIN + " BOOLEAN,"
                                + DBConstants.USER_IDENTITY + " VARCHAR(25),"
                                + DBConstants.FULL_NAME + " VARCHAR(50),"
                                // + DBConstants.LAST_NAME + " VARCHAR(50),"
                                + DBConstants.GENDER + " VARCHAR(25),"
                                + DBConstants.MOBILE_PHONE + " VARCHAR(25),"
                                + DBConstants.UPDATE_TIME + " BIGINT NOT NULL DEFAULT 0,"
                                + " CONSTRAINT unique_circleMembers" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.CIRCLE_ID + ", " + DBConstants.USER_IDENTITY + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating CircleMemberList Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating CircleMemberList Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating CircleMemberList Table. Exception :: " + ex.getMessage());
        }
    }

    public void createRingCallLogTable(Statement statement) {
        log.info("Creating RingCallLog Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_CALL_LOG).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_CALL_LOG
                                + "("
                                + DBConstants.ID + " VARCHAR(35),"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(20),"
                                + DBConstants.FRIEND_IDENTITY + " VARCHAR(20),"
                                + DBConstants.CL_CALL_TYPE + " SMALLINT,"
                                + DBConstants.CL_CALL_DURATION + " BIGINT,"
                                + DBConstants.CL_CALLING_TIME + " BIGINT,"
                                + "CONSTRAINT unique_calllog" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.FRIEND_IDENTITY + ", " + DBConstants.ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating RingCallLog Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating RingCallLog Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating RingCallLog Table. Exception :: " + ex.getMessage());
        }
    }

    public void createSmsHistoryTable(Statement statement) {
        log.info("Creating SmsHistory Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_SMS_HISTORY).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_SMS_HISTORY
                                + "("
                                + DBConstants.ID + " BIGINT,"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25),"
                                + DBConstants.SMS_SENDER + " VARCHAR(25),"
                                + DBConstants.SMS_RECEIVER + " VARCHAR(25),"
                                + DBConstants.SMS_MESSAGE + " VARCHAR(500),"
                                + DBConstants.SMS_TIME + " VARCHAR(50),"
                                + DBConstants.SMS_STATUS + " SMALLINT,"
                                + DBConstants.SMS_DESTINATION + " VARCHAR(25),"
                                + " CONSTRAINT unique_smshistory" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.SMS_TIME + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating SmsHistory Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating SmsHistory Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating SmsHistory Table. Exception :: " + ex.getMessage());
        }
    }

    public void createGroupChatTable(Statement statement) {
        log.info("Creating GroupChatTable Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_GROUP_CHAT_TABLE).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_GROUP_CHAT_TABLE
                                + "  ("
                                + ""
                                + DBConstants.ID + "    BIGINT NOT NULL "
                                + "               PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                                + "                (START WITH 1, INCREMENT BY 1),"
                                + DBConstants.LOGIN_USER_ID + "  VARCHAR(20),"
                                + DBConstants.GROUP_ID + "  BIGINT NOT NULL,"
                                + DBConstants.MSG_SENDER + "  VARCHAR(20),"
                                + DBConstants.FULL_NAME + "  VARCHAR(150),"
                                + DBConstants.MESSAGE + "   VARCHAR(10000),"
                                + DBConstants.MESSAGE_DATE + "  BIGINT,"
                                + DBConstants.SYSTEM_DATE + "  BIGINT,"
                                + DBConstants.TIME_OUT + "  INTEGER,"
                                + DBConstants.PACKET_TYPE + "  INTEGER,"
                                + DBConstants.STATUS + "  INTEGER,"
                                + DBConstants.PACKET_ID + "   VARCHAR(64),"
                                + DBConstants.MESSAGE_TYPE + "  INTEGER,"
                                + "CONSTRAINT unique_group_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.GROUP_ID + ", " + DBConstants.PACKET_ID + "))";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating GroupChatTable Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating GroupChatTable Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating GroupChatTable Table. Exception :: " + ex.getMessage());
        }
    }

    public void createRingActivityTable(Statement statement) {
        log.info("Creating RingActivity Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablets = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_ACTIVITY).toUpperCase(), null);
                    if (!tablets.isClosed() && !tablets.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_ACTIVITY
                                + "("
                                + DBConstants.ID + "    BIGINT NOT NULL "
                                + "               PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                                + "                (START WITH 1, INCREMENT BY 1),"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25) NOT NULL,"
                                + DBConstants.FRIEND_IDENTITY + " VARCHAR(25),"
                                + DBConstants.GROUP_ID + " BIGINT,"
                                + DBConstants.ACTIVITY_TYPE + " SMALLINT,"
                                + DBConstants.MESSAGE_TYPE + " SMALLINT,"
                                + DBConstants.ACTIVITY_BY + " VARCHAR(25),"
                                + DBConstants.UPDATE_TIME + " BIGINT NOT NULL DEFAULT 0,"
                                + DBConstants.MEMBER_LIST + " VARCHAR(1024),"
                                + DBConstants.FROM_CONTACT_TYPE + " SMALLINT,"
                                + DBConstants.TO_CONTACT_TYPE + " SMALLINT,"
                                + DBConstants.PREVIOUS_GROUP_NAME + "  VARCHAR(200),"
                                + DBConstants.PACKET_ID + "   VARCHAR(64),"
                                + DBConstants.IS_PROCESSED + " BOOLEAN NOT NULL DEFAULT FALSE,"
                                + "CONSTRAINT unique_activity_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.PACKET_ID + "))";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating RingActivity Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating RingActivity Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating RingActivity Table. Exception :: " + ex.getMessage());
        }
    }

    public void createNotificationHistoryTable(Statement statement) {
        log.info("Creating NotificationHistory Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_NOTIFICATION_HISTORY).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_NOTIFICATION_HISTORY
                                + "("
                                + DBConstants.ID + " VARCHAR(25),"
                                + DBConstants.LOGIN_USER_ID + " VARCHAR(25),"
                                + DBConstants.NOTIFICATION_FRIENDID + " VARCHAR(25),"
                                + DBConstants.NOTIFICATION_UT + " BIGINT,"
                                + DBConstants.NOTIFICATION_N_TYPE + " SMALLINT,"
                                + DBConstants.NOTIFICATION_ACTIVITY_ID + " BIGINT,"
                                + DBConstants.NOTIFICATION_NEWSFEED_ID + " BIGINT,"
                                + DBConstants.NOTIFICATION_IMAGE_ID + " BIGINT,"
                                + DBConstants.NOTIFICATION_COMMENT_ID + " BIGINT,"
                                + DBConstants.NOTIFICATION_MESSAGE_TYPE + " SMALLINT,"
                                + DBConstants.NOTIFICATION_FRIEND_NAME + " VARCHAR(500),"
                                + DBConstants.NOTIFICATION_NUMBER_OF_LIKECOMMENT + " BIGINT,"
                                + DBConstants.NOTIFICATION_IS_READ + " BOOLEAN,"
                                + " CONSTRAINT unique_notificationhistory" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.ID + ", " + DBConstants.LOGIN_USER_ID + ")"
                                + ")";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating NotificationHistory Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating NotificationHistory Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating NotificationHistory Table. Exception :: " + ex.getMessage());
        }
    }

    public void createFriendChatTable(Statement statement) {
        log.info("Creating FriendChat Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_FRIEND_CHAT + MyFnFSettings.LOGIN_USER_ID
                                + " ("
                                + DBConstants.ID + "  BIGINT NOT NULL "
                                + "                PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                                + "                (START WITH 1, INCREMENT BY 1),"
                                + DBConstants.FRIEND_IDENTITY + "   VARCHAR(20),"
                                + DBConstants.MSG_SENDER + "  VARCHAR(20),"
                                + DBConstants.MESSAGE + "   VARCHAR(10000),"
                                + DBConstants.MESSAGE_DATE + "  BIGINT,"
                                + DBConstants.SYSTEM_DATE + "  BIGINT,"
                                + DBConstants.TIME_OUT + "  INTEGER,"
                                + DBConstants.PACKET_TYPE + "  INTEGER,"
                                + DBConstants.STATUS + "  INTEGER,"
                                + DBConstants.PACKET_ID + "   VARCHAR(64),"
                                + DBConstants.MESSAGE_TYPE + "  INTEGER,"
                                + "CONSTRAINT unique_fnd_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.FRIEND_IDENTITY + ", " + DBConstants.PACKET_ID + "))";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }
                } catch (Exception e) {
                    log.error("Error occured during Creating FriendChat Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating FriendChat Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating FriendChat Table. Exception :: " + ex.getMessage());
        }
    }

    public void createBrokenChatTable(Statement statement) {
        log.info("Creating BrokenChat Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_BROKEN_CHAT).toUpperCase(), null);
                    if (!tablesw.isClosed() && !tablesw.next()) {

                        String sqlCreate = "CREATE TABLE " + DBConstants.TABLE_RING_BROKEN_CHAT
                                + " ("
                                + DBConstants.LOGIN_USER_ID + "  VARCHAR(20) NOT NULL,"
                                + DBConstants.CONTACT_ID + "   VARCHAR(20) NOT NULL,"
                                + DBConstants.FRIEND_IDENTITY + "   VARCHAR(20),"
                                + DBConstants.GROUP_ID + "  BIGINT,"
                                + DBConstants.USER_IDENTITY + "  VARCHAR(20),"
                                + DBConstants.FULL_NAME + "  VARCHAR(150),"
                                + DBConstants.MESSAGE + "   VARCHAR(512),"
                                + DBConstants.MESSAGE_DATE + "  BIGINT,"
                                + DBConstants.TIME_OUT + "  INTEGER,"
                                + DBConstants.PACKET_TYPE + "  INTEGER NOT NULL,"
                                + DBConstants.PACKET_ID + "   VARCHAR(64) NOT NULL,"
                                + DBConstants.NUMBER_OF_PACKET + "  INTEGER,"
                                + DBConstants.SEQUENCE_NO + "  INTEGER NOT NULL,"
                                + DBConstants.MESSAGE_TYPE + "  INTEGER,"
                                + "CONSTRAINT broken_chat_" + System.currentTimeMillis() + " UNIQUE (" + DBConstants.LOGIN_USER_ID + ", " + DBConstants.CONTACT_ID + ", " + DBConstants.PACKET_ID + ", " + DBConstants.SEQUENCE_NO + ", " + DBConstants.PACKET_TYPE + "))";
                        stmt.execute(sqlCreate);
                        DBConstants.conn.commit();
                    }

                } catch (Exception e) {
                    log.error("Error occured during Creating BrokenChatTable Table. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during Creating BrokenChatTable Table. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during Creating BrokenChatTable Table. Exception :: " + ex.getMessage());
        }
    }

    /*
     FETCH FROM DATABASE
     */
    public void fetchRingSignInSettingsBeforeLogin(Statement statement) {
        log.info("Fetching from RingLoginSettingTable Table .............");

        MyFnFSettings.VALUE_NEW_USER_NAME = "";
        MyFnFSettings.PREVIOUS_USER_NAME = "";
        MyFnFSettings.VALUE_LOGIN_USER_NAME = "";
        MyFnFSettings.VALUE_LOGIN_USER_TYPE = SettingsConstants.RINGID_LOGIN;
        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = "";
        MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 1;
        MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 1;
        MyFnFSettings.VALUE_LOGIN_AUTO_START = 1;
        MyFnFSettings.VALUE_LOGIN_USER_INFO = null;

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    ResultSet tablesw = DBConstants.dbm.getTables(null, null, (DBConstants.TABLE_RING_LOGIN_SETTINGS).toUpperCase(), null);
                    if (!tablesw.isClosed() && tablesw.next()) {

                        String query = "SELECT * FROM  " + DBConstants.TABLE_RING_LOGIN_SETTINGS;
                        ResultSet results = stmt.executeQuery(query);
                        if (!results.isClosed()) {
                            while (results.next()) {
                                String key = results.getString(DBConstants.LOGIN_KEY);
                                String value = results.getString(DBConstants.LOGIN_VALUE);
                                if (key != null && value != null) {
                                    if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_AUTO_START)) {
                                        int int_value = 1;
                                        try {
                                            int_value = Integer.parseInt(value);
                                        } catch (Exception e) {
                                        }
                                        MyFnFSettings.VALUE_LOGIN_AUTO_START = int_value;
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_NAME)) {
                                        MyFnFSettings.PREVIOUS_USER_NAME = value;
                                        MyFnFSettings.VALUE_LOGIN_USER_NAME = value;

                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_TYPE)) {
                                        int int_value = SettingsConstants.RINGID_LOGIN;
                                        try {
                                            int_value = Integer.parseInt(value);
                                        } catch (Exception e) {
                                        }
                                        MyFnFSettings.VALUE_LOGIN_USER_TYPE = int_value;
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_PASSWORD)) {
                                        String pass = value;
                                        String newPass = HelperMethods.encryptPassword(pass);
                                        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = newPass;
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_SAVE_PASSWORD)) {
                                        int int_value = 0;
                                        try {
                                            int_value = Integer.parseInt(value);
                                        } catch (Exception e) {
                                        }
                                        MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = int_value;
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_AUTO_SIGNIN)) {
                                        int int_value = 0;
                                        try {
                                            int_value = Integer.parseInt(value);
                                        } catch (Exception e) {
                                        }
                                        MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = int_value;
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_LOGIN_USER_INFO)) {
                                        JsonFields fields = null;
                                        try {
                                            fields = HelperMethods.getJsonFields(value);
                                        } catch (Exception ex) {
                                            fields = null;
                                        }
                                        if (fields != null) {
                                            MyFnFSettings.VALUE_LOGIN_USER_INFO = fields;
                                        }
                                    } else if (key.equalsIgnoreCase(MyFnFSettings.KEY_MOBILE_DIALING_CODE)) {
                                        MyFnFSettings.VALUE_MOBILE_DIALING_CODE = value;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching RingLoginSetting from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching RingLoginSetting from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching RingLoginSetting from DB. Exception :: " + ex.getMessage());
        }

    }

    public void fetchEmotions(Statement statement) {
        log.info("Fetching from Emotions Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                XMLConstants.EMOTION_REGULAR_EXPRESSION = XMLConstants.REGX_FOR_URL + "|";
                List<EmotionDtos> empList = new ArrayList<EmotionDtos>();

                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_EMOTICONS;
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            EmotionDtos groupMsg = new EmotionDtos();
                            groupMsg.setId(results.getInt(DBConstants.ID));
                            groupMsg.setName(results.getString(DBConstants.EMO_NAME));
                            groupMsg.setSymbol(results.getString(DBConstants.EMO_SYMBOL).replace("<", "&lt;"));
                            groupMsg.setUrl(results.getString(DBConstants.EMO_URL));
                            groupMsg.setType(results.getInt(DBConstants.EMO_TYPE));
                            empList.add(groupMsg);
                            HashMapsBeforeLogin.getInstance().getEmotionHashmap().put(groupMsg.getSymbol(), groupMsg);
                            
                            String sym = groupMsg.getSymbol();
                            sym = sym.replace("|", "\\|");
                            sym = sym.replace("(", "\\(");
                            sym = sym.replace(")", "\\)");
                            sym = sym.replace("*", "\\*");
                            sym = sym.replace("$", "\\$");
                            sym = sym.replace("/", "\\/");
                            sym = sym.replace("^", "\\^");
                            
                            if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null && XMLConstants.EMOTION_REGULAR_EXPRESSION.length() > 0) {
                                XMLConstants.EMOTION_REGULAR_EXPRESSION = XMLConstants.EMOTION_REGULAR_EXPRESSION + sym + "|";
                            } else {
                                XMLConstants.EMOTION_REGULAR_EXPRESSION = sym + "|";
                            }

                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching Emotions from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching Emotions from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching Emotions from DB. Exception :: " + ex.getMessage());
        }

    }

    /*    public void fetchInstantMessages(Statement statement) {
     log.info("Fetching from Instant Messages Table .............");
     try {
     if (DBConstants.conn != null) {
     Statement stmt = statement;
     if (statement == null) {
     stmt = DBConstants.conn.createStatement();
     }
     try {
     String query = "SELECT * FROM  " + DBConstants.TABLE_RING_INSTANT_MESSAGES;
     ResultSet results = stmt.executeQuery(query);
     if (!results.isClosed()) {
     while (results.next()) {
     String insMsg = results.getString(DBConstants.INS_MSG);
     int insType = results.getInt(DBConstants.INS_MSG_TYPE);
     HashMapsBeforeLogin.getInstance().getInstantMessages().add(insMsg);
     }
     }
     } catch (Exception e) {
     log.error("Error occured during fetching Instant Messages from DB. Exception :: " + e.getMessage());
     } finally {
     if (statement == null) {
     try {
     stmt.close();
     } catch (SQLException ex) {
     log.error("Error occured during fetching Instant Messages from DB. Exception :: " + ex.getMessage());
     }
     }
     }
     }
     } catch (SQLException ex) {
     log.error("Error occured during fetching Instant Messages from DB. Exception :: " + ex.getMessage());
     }
    
     }*/
    public void fetchContractList(Statement statement) {
        log.info("Fetching from Contract List Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    String query = "SELECT * FROM " + DBConstants.TABLE_RING_CONTACT_LIST + " WHERE " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            UserBasicInfo entity = new UserBasicInfo();
                            entity.setUserIdentity(results.getString(DBConstants.USER_IDENTITY));
                            entity.setUserTableId(results.getLong(DBConstants.USER_TABLE_ID));
                            entity.setFullName(results.getString(DBConstants.FULL_NAME));
                            entity.setEmail(results.getString(DBConstants.EMAIL));
                            entity.setGender(results.getString(DBConstants.GENDER));
                            entity.setMobilePhone(results.getString(DBConstants.MOBILE_PHONE));
                            entity.setCountry(results.getString(DBConstants.COUNTRY));
                            entity.setBirthday(results.getString(DBConstants.BIRTHDAY));
                            entity.setPresence(StatusConstants.PRESENCE_OFFLINE);
                            entity.setWhatisInYourMind(results.getString(DBConstants.WHAT_IS_IN_YOUR_MIND));
                            entity.setFriendShipStatus(results.getInt(DBConstants.FRIENDSHIP_STATUS));
                            entity.setMobilePhoneDialingCode(results.getString(DBConstants.MOBILE_PHONE_DIALING_CODE));
                            entity.setProfileImage(results.getString(DBConstants.PROFILE_IMAGE));
                            entity.setProfileImageId(results.getLong(DBConstants.PROFILE_IMAGE_ID));
                            entity.setCoverImage(results.getString(DBConstants.COVER_IMAGE));
                            entity.setCoverImageId(results.getLong(DBConstants.COVER_IMAGE_ID));
                            entity.setCoverImageX(results.getInt(DBConstants.COVER_IMAGE_X));
                            entity.setCoverImageY(results.getInt(DBConstants.COVER_IMAGE_Y));
                            entity.setRingEmail(results.getString(DBConstants.RING_EMAIL));
                            entity.setEmailPrivacy(results.getShort(DBConstants.EMAIL_PRIVACY));
                            entity.setMobilePrivacy(results.getShort(DBConstants.MOBILE_PRIVACY));
                            entity.setProfileImagePrivacy(results.getShort(DBConstants.PROFILE_IMAGE_PRIVACY));
                            entity.setBirthdayPrivacy(results.getShort(DBConstants.BIRTHDAY_PRIVACY));
                            entity.setCoverImagePrivacy(results.getShort(DBConstants.COVER_IMAGE_PRIVACY));
                            short[] pvc = new short[5];
                            pvc[0] = entity.getEmailPrivacy();
                            pvc[1] = entity.getMobilePrivacy();
                            pvc[2] = entity.getProfileImagePrivacy();
                            pvc[3] = entity.getBirthdayPrivacy();
                            pvc[4] = entity.getCoverImagePrivacy();
                            entity.setPrivacy(pvc);
                            entity.setDeviceToken(results.getString(DBConstants.DEVICE_TOKEN));
                            entity.setContactType(results.getInt(DBConstants.CONTACT_TYPE));
                            entity.setNewContactType(results.getInt(DBConstants.NEW_CONTACT_TYPE));
                            entity.setIsChangeRequester(results.getBoolean(DBConstants.IS_CHANGE_REQUESTER));
                            entity.setBlocked(results.getInt(DBConstants.IS_BLOCKED));
                            entity.setIncomingNotification(results.getBoolean(DBConstants.INCOMING_NOTIFICATION));
                            entity.setUt(results.getLong(DBConstants.UPDATE_TIME));
                            entity.setFullName(HelperMethods.getUserFullName(entity));
                            FriendList.getInstance().getFriend_hash_map().put(entity.getUserIdentity(), entity);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching Contract List from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching Contract List from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching Contract List from DB. Exception :: " + ex.getMessage());
        }
    }

    public void fetchRingUserSettings(Statement statement) {
        log.info("Fetching from RingUserSettings Table .............");
        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                SettingsConstants.FNF_USERINFO_UT = -1;
                SettingsConstants.FNF_GROUP_UT = -1;
                SettingsConstants.FNF_CONTACT_UT = -1;
                SettingsConstants.FNF_CIRCLE_UT = -1;
                SettingsConstants.FNF_CIRCLE_MEMBER_UT = -1;
                SettingsConstants.FNF_CALL_LOG_UT = -1;
                SettingsConstants.FNF_SMS_LOG_UT = -1;
                SettingsConstants.FNF_CHAT_BG = SettingsConstants.FNF_DEAFULT_CHAT_BG;

                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_USER_SETTINGS + " WHERE " + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {

                        while (results.next()) {
                            if (results.getLong(DBConstants.USERINFO_UT) > SettingsConstants.FNF_USERINFO_UT) {
                                SettingsConstants.FNF_USERINFO_UT = results.getLong(DBConstants.USERINFO_UT);
                            }
                            if (results.getLong(DBConstants.GROUP_UT) > SettingsConstants.FNF_GROUP_UT) {
                                SettingsConstants.FNF_GROUP_UT = results.getLong(DBConstants.GROUP_UT);
                            }
                            if (results.getLong(DBConstants.CONTACT_UT) > SettingsConstants.FNF_CONTACT_UT) {
                                SettingsConstants.FNF_CONTACT_UT = results.getLong(DBConstants.CONTACT_UT);
                            }
                            if (results.getLong(DBConstants.CIRCLE_UT) > SettingsConstants.FNF_CIRCLE_UT) {
                                SettingsConstants.FNF_CIRCLE_UT = results.getLong(DBConstants.CIRCLE_UT);
                            }
                            if (results.getLong(DBConstants.CIRCLE_MEMBER_UT) > SettingsConstants.FNF_CIRCLE_MEMBER_UT) {
                                SettingsConstants.FNF_CIRCLE_MEMBER_UT = results.getLong(DBConstants.CIRCLE_MEMBER_UT);
                            }
                            if (results.getLong(DBConstants.CALL_LOG_UT) > SettingsConstants.FNF_CALL_LOG_UT) {
                                SettingsConstants.FNF_CALL_LOG_UT = results.getLong(DBConstants.CALL_LOG_UT);
                            }
                            if (results.getLong(DBConstants.SMS_LOG_UT) > SettingsConstants.FNF_SMS_LOG_UT) {
                                SettingsConstants.FNF_SMS_LOG_UT = results.getLong(DBConstants.SMS_LOG_UT);
                            }
                            String temp = results.getString(DBConstants.CHAT_BG);
                            if (temp != null && temp.length() > 0) {
                                SettingsConstants.FNF_CHAT_BG = results.getString(DBConstants.CHAT_BG);
                            }
                        }
                    }

                    SettingsConstants.FNF_LAST_LOGIN_TIME = System.currentTimeMillis();
                    new InsertIntoRingUserSettings().start();

                } catch (Exception e) {
                    log.error("Error occured during fetching RingUserSettings from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching RingUserSettings from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching RingUserSettings from DB. Exception :: " + ex.getMessage());
        }
    }

    public void fetchCircleList(Statement statement) {
        log.info("Fetching from CircleListTable & CircleMemberList Table .............");

        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_CIRCLE_LIST + " WHERE " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            SingleCircleDto entity = new SingleCircleDto();
                            entity.setCircleId(results.getLong(DBConstants.CIRCLE_ID));
                            entity.setCircleName(results.getString(DBConstants.CIRCLE_NAME));
                            entity.setSuperAdmin(results.getString(DBConstants.SUPER_ADMIN));
                            entity.setUt(results.getLong(DBConstants.UPDATE_TIME));
                            MyfnfHashMaps.getInstance().getCircleLists().put(entity.getCircleId(), entity);
                            fetchCircleMemberList(results.getLong(DBConstants.CIRCLE_ID));
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching CircleListTable & CircleMemberList from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching CircleListTable & CircleMemberList from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching CircleListTable & CircleMemberList from DB. Exception :: " + ex.getMessage());
        }

    }

    public void fetchCircleMemberList(long circleId) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_CIRCLE_MEMBERS_LIST
                            + " WHERE "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                            + DBConstants.CIRCLE_ID + "=" + circleId;
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            SingleMemberInCircleDto entity = new SingleMemberInCircleDto();
                            entity.setCircleId(results.getLong(DBConstants.CIRCLE_ID));
                            entity.setAdmin(results.getBoolean(DBConstants.ADMIN));
                            entity.setUserIdentity(results.getString(DBConstants.USER_IDENTITY));
                            entity.setFullName(results.getString(DBConstants.FULL_NAME));
                            entity.setGender(results.getString(DBConstants.GENDER));
                            entity.setMobilePhone(results.getString(DBConstants.MOBILE_PHONE));
                            if (MyfnfHashMaps.getInstance().getCircleMembers().get(entity.getCircleId()) == null) {
                                Map<String, SingleMemberInCircleDto> singleGroup = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                                singleGroup.put(entity.getUserIdentity(), entity);
                                MyfnfHashMaps.getInstance().getCircleMembers().put(entity.getCircleId(), singleGroup);
                            } else {
                                MyfnfHashMaps.getInstance().getCircleMembers().get(entity.getCircleId()).put(entity.getUserIdentity(), entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + e.getMessage());
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException ex) {
                        log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + ex.getMessage());
        }

    }

    public void fetchGroupList(Statement statement) {
        log.info("Fetching from GroupListTable & GroupMemberList Table .............");
        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_GROUP_LIST + " WHERE " + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            GroupInfoDTO entity = new GroupInfoDTO();
                            entity.setGroupId(results.getLong(DBConstants.GROUP_ID));
                            entity.setGroupName(results.getString(DBConstants.GROUP_NAME));
                            entity.setGroupUt(results.getLong(DBConstants.GROUP_UT));
                            entity.setOwnerUserTableId(results.getLong(DBConstants.GROUP_OWNER_USER_TABLE_ID));
                            MyfnfHashMaps.getInstance().getGroup_hash_map().put(entity.getGroupId(), entity);
                            entity.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                            fetchGroupMemberList(entity);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching GroupListTable & GroupMemberList from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching GroupListTable & GroupMemberList from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching GroupListTable & GroupMemberList from DB. Exception :: " + ex.getMessage());
        }

    }

    public void fetchGroupMemberList(GroupInfoDTO entity) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_GROUP_MEMBERS_LIST
                            + " WHERE "
                            + DBConstants.LOGIN_USER_ID + "='" + MyFnFSettings.LOGIN_USER_ID + "' AND "
                            + DBConstants.GROUP_ID + "=" + entity.getGroupId();
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            GroupMemberDTO member = new GroupMemberDTO();
                            member.setGroupId(results.getLong(DBConstants.GROUP_ID));
                            member.setUserIdentity(results.getString(DBConstants.USER_IDENTITY));
                            member.setFullName(results.getString(DBConstants.FULL_NAME));
                            entity.getMemberMap().put(member.getUserIdentity(), member);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + e.getMessage());
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException ex) {
                        log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching CircleMemberList from DB. Exception :: " + ex.getMessage());
        }

    }

    public void fetchRingMarketStickerCategoriesList(Statement statement) {
        log.info("Fetching from StickerCategories & Stickers List Table .............");
        try {
            if (DBConstants.conn != null) {
                Statement stmt = statement;
                if (statement == null) {
                    stmt = DBConstants.conn.createStatement();
                }

                try {
                    String query = "SELECT * FROM " + DBConstants.TABLE_RING_MARKET_STICKER_CATEGORY + " WHERE "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";
                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            StickerCategoryDTO entity = new StickerCategoryDTO();
                            entity.setStickerCategoryId(results.getInt(DBConstants.STK_CATEGORY_ID));
                            entity.setStickerCategoryName(results.getString(DBConstants.STK_CATEGORY_NAME));
                            entity.setStickerCollectionId(results.getInt(DBConstants.STK_COLLECTION_ID));
                            entity.setCategoryBannerImage(results.getString(DBConstants.STK_CATEGORY_BANNER_IMAGE));
                            entity.setRank(results.getInt(DBConstants.STK_CATEGORY_RANK));
                            entity.setDetailImage(results.getString(DBConstants.STK_CATEGORY_DETAIL_IMAGE));
                            entity.setCategoryNew(results.getBoolean(DBConstants.STK_CATEGORY_IS_NEW));
                            entity.setIcon(results.getString(DBConstants.STK_CATEGORY_ICON));
                            entity.setPrize(results.getFloat(DBConstants.STK_CATEGORY_PRIZE));
                            entity.setFree(results.getBoolean(DBConstants.STK_CATEGORY_IS_FREE));
                            entity.setDescription(results.getString(DBConstants.STK_CATEGORY_DESCRIPTION));
                            entity.setDownloaded(results.getBoolean(DBConstants.STK_CATEGORY_IS_DOWNLOADED));

                            if (StickerStorer.getInstance().getCategoriesMap().get(entity.getStickerCategoryId()) == null) {
                                StickerStorer.getInstance().getCategoriesMap().put(entity.getStickerCategoryId(), entity);
                            }

                            fetchRingMarketStickersList(entity.getStickerCategoryId());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching StickerCategories & Stickers List from DB. Exception :: " + e.getMessage());
                } finally {
                    if (statement == null) {
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            log.error("Error occured during fetching StickerCategories & Stickers List from DB. Exception :: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching StickerCategories & Stickers List from DB. Exception :: " + ex.getMessage());
        }
    }

    public void fetchRingMarketStickersList(int categoryId) {
        try {
            if (DBConstants.conn != null) {
                Statement stmt = DBConstants.conn.createStatement();
                try {
                    if (StickerStorer.getInstance().getImagesMap().get(categoryId) == null) {
                        StickerStorer.getInstance().getImagesMap().put(categoryId, new ConcurrentHashMap<Integer, StickerImagesDTO>());
                    }

                    String query = "SELECT * FROM  " + DBConstants.TABLE_RING_MARKET_STICKER + " WHERE "
                            + DBConstants.STK_CATEGORY_ID + "=" + categoryId + " AND "
                            + DBConstants.LOGIN_USER_ID + " = '" + MyFnFSettings.LOGIN_USER_ID + "'";

                    ResultSet results = stmt.executeQuery(query);
                    if (!results.isClosed()) {
                        while (results.next()) {
                            StickerImagesDTO entity = new StickerImagesDTO();
                            entity.setStickerCategoryId(results.getInt(DBConstants.STK_CATEGORY_ID));
                            entity.setImageId(results.getInt(DBConstants.STK_IMAGE_ID));
                            entity.setImageUrl(results.getString(DBConstants.STK_IMAGE_URL));

                            if (StickerStorer.getInstance().getImagesMap().get(entity.getStickerCategoryId()).get(entity.getImageId()) == null) {
                                StickerStorer.getInstance().getImagesMap().get(entity.getStickerCategoryId()).put(entity.getImageId(), entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occured during fetching RingMarketStickersList from DB. Exception :: " + e.getMessage());
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException ex) {
                        log.error("Error occured during fetching RingMarketStickersList from DB. Exception :: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error occured during fetching RingMarketStickersList from DB. Exception :: " + ex.getMessage());
        }
    }

}

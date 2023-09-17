/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.constants;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 *
 * @author Faiz Ahmed
 */
public class DBConstants {

    public static Connection conn = null;
    public static DatabaseMetaData dbm = null;
    public static String DB_NAME = "db";
    /**/
    public static String ID = "ID";
    public static String MSG_SENDER = "MSG_SENDER";
    public static String FULL_NAME = "FULL_NAME";
    public static String FRIEND_IDENTITY = "FRIEND_IDENTITY";
    public static String MESSAGE = "MESSAGE";
    public static String MESSAGE_DATE = "MESSAGE_DATE";
    public static String SYSTEM_DATE = "SYSTEM_DATE";
    public static String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static String TABLE_TYPE = "TABLE_TYPE";
    public static String STATUS = "STATUS";
    public static String PACKET_TYPE = "PACKET_TYPE";
    public static String TIME_OUT = "TIME_OUT";
    public static String PACKET_ID = "PACKET_ID";
    public static String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static String MEMBER_LIST = "MEMBER_LIST";
    public static String IS_PROCESSED = "IS_PROCESSED";
    public static String FROM_CONTACT_TYPE = "FROM_CONTACT_TYPE";
    public static String TO_CONTACT_TYPE = "TO_CONTACT_TYPE";
    public static String ACTIVITY_BY = "ACTIVITY_BY";
    public static String PREVIOUS_GROUP_NAME = "PREVIOUS_GROUP_NAME";
    public static String SEQUENCE_NO = "SEQUENCE_NO";
    public static String NUMBER_OF_PACKET = "NUMBER_OF_PACKET";
    public static String CONTACT_ID = "CONTACT_ID";

    /*tables start*/
    public static final String TABLE_RING_LOGIN_SETTINGS = "RING_LOGIN_SETTINGS";
    public static final String LOGIN_KEY = "LOGIN_KEY";
    public static final String LOGIN_VALUE = "LOGIN_VALUE";

    public static final String TABLE_RING_FRIEND_CHAT = "RING_FRIEND_CHAT_";
    public static final String TABLE_RING_GROUP_CHAT_TABLE = "RING_GROUP_CHAT";
    public static final String TABLE_RING_CIRCLE_MEMBERS_LIST = "RING_CIRCLE_MEMBERS_LIST";
    public static final String TABLE_RING_CIRCLE_LIST = "RING_CIRCLE_LIST";
    public static final String TABLE_RING_CONTACT_LIST = "RING_CONTACT_LIST";
    public static final String TABLE_RING_USER_SETTINGS = "RING_USER_SETTINGS";
    public static final String TABLE_RING_CALL_LOG = "RING_CALL_LOG";
    public static final String TABLE_RING_EMOTICONS = "RING_EMOTICONS";
    public static final String TABLE_RING_INSTANT_MESSAGES = "RING_INSTANT_MESSAGES";
    public static final String TABLE_RING_SMS_HISTORY = "RING_SMS_HISTORY";
    public static final String TABLE_RING_NOTIFICATION_HISTORY = "RING_NOTIFICATION_HISTORY";
    public static final String TABLE_RING_MAIL_MESSAGE = "RING_MAIL_MESSAGE";
    public static final String TABLE_RING_MARKET_STICKER_CATEGORY = "RING_MARKET_STICKER_CATEGORY";
    public static final String TABLE_RING_MARKET_STICKER = "RING_MARKET_STICKER";
    public static final String TABLE_RING_GROUP_LIST = "RING_GROUP_LIST";
    public static final String TABLE_RING_GROUP_MEMBERS_LIST = "RING_GROUP_MEMBERS_LIST";
    public static final String TABLE_RING_ACTIVITY = "RING_ACTIVITY";
    public static final String TABLE_RING_BROKEN_CHAT = "RING_BROKEN_CHAT";
    /*tables end*/

    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String USER_IDENTITY = "USER_IDENTITY";//            VARCHAR     20      true                          NO               
    //public static final String FIRST_NAME = "FIRST_NAME";//                  VARCHAR     100                                   YES              
    //public static final String LAST_NAME = "LAST_NAME";//                  VARCHAR     50                                    YES              
    public static final String EMAIL = "EMAIL";//                    VARCHAR     50                                    YES              
    public static final String GENDER = "GENDER";//                 VARCHAR     50                                    YES              
    public static final String MOBILE_PHONE = "MOBILE_PHONE";//               VARCHAR     20                                    YES              
    public static final String COUNTRY = "COUNTRY";//               VARCHAR     50                                    YES              
    public static final String BIRTHDAY = "BIRTHDAY";//                VARCHAR     50                                    YES              
    public static final String PRESENCE = "PRESENCE";//                SMALLINT    5                                     YES              
    public static final String WHAT_IS_IN_YOUR_MIND = "WHAT_IS_IN_YOUR_MIND";//        VARCHAR     400                                   YES              
    public static final String FRIENDSHIP_STATUS = "FRIENDSHIP_STATUS";//         SMALLINT    5                                     YES              
    public static final String MOBILE_PHONE_DIALING_CODE = "MOBILE_PHONE_DIALING_CODE";//    VARCHAR     10                                    YES              
    public static final String RING_EMAIL = "RING_EMAIL";//             VARCHAR     50                                    YES              
    public static final String UPDATE_TIME = "UPDATE_TIME";//              BIGINT      19                                    NO     
    public static final String PROFILE_IMAGE = "PROFILE_IMAGE";//       VARCHAR     200                                   YES 
    public static final String PROFILE_IMAGE_ID = "PROFILE_IMAGE_ID";
    public static final String COVER_IMAGE = "COVER_IMAGE";//
    public static final String COVER_IMAGE_ID = "COVER_IMAGE_ID";
    public static final String COVER_IMAGE_X = "COVER_IMAGE_X";
    public static final String COVER_IMAGE_Y = "COVER_IMAGE_Y";
    public static final String EMAIL_PRIVACY = "EMAIL_PRIVACY";
    public static final String MOBILE_PRIVACY = "MOBILE_PRIVACY";
    public static final String PROFILE_IMAGE_PRIVACY = "PROFILE_IMAGE_PRIVACY";
    public static final String COVER_IMAGE_PRIVACY = "COVER_IMAGE_PRIVACY";
    public static final String BIRTHDAY_PRIVACY = "BIRTHDAY_PRIVACY";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String CONTACT_TYPE = "CONTACT_TYPE";
    public static final String NEW_CONTACT_TYPE = "NEW_CONTACT_TYPE";
    public static final String IS_CHANGE_REQUESTER = "IS_CHANGE_REQUESTER";
    public static final String IS_BLOCKED = "IS_BLOCKED";
    public static final String INCOMING_NOTIFICATION = "INCOMING_NOTIFICATION";

    /*table fields*/
    public static String USER_TABLE_ID = "USER_TABLE_ID";
    public static String CIRCLE_ID = "CIRCLE_ID";
    public static String GROUP_NAME = "GROUP_NAME";
    public static String GROUP_UT = "GROUP_UT";
    public static String GROUP_ID = "GROUP_ID";
    public static String GROUP_OWNER_USER_TABLE_ID = "GROUP_OWNER_USER_TABLE_ID";
    public static String ADMIN = "ADMIN";
    public static String CIRCLE_NAME = "CIRCLE_NAME";
    public static String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String USERINFO_UT = "USERINFO_UT";
    public static final String CONTACT_UT = "CONTACT_UT";
    public static final String CIRCLE_UT = "CIRCLE_UT";
    public static final String CIRCLE_MEMBER_UT = "CIRCLE_MEMBER_UT";
    public static final String CALL_LOG_UT = "CALL_LOG_UT";
    public static final String SMS_LOG_UT = "SMS_LOG_UT";
    public static final String LAST_LOGIN_TIME = "LAST_LOGIN_TIME";
    public static final String CHAT_BG = "CHAT_BG";

    public static final String CL_CALL_TYPE = "CALL_TYPE";
    public static final String CL_CALL_DURATION = "CALL_DURATION";
    public static final String CL_CALLING_TIME = "CALLING_TIME";

    public static final String SMS_SENDER = "SENDER";
    public static final String SMS_RECEIVER = "RECEIVER";
    public static final String SMS_MESSAGE = "MESSAGE";
    public static final String SMS_STATUS = "STATUS";
    public static final String SMS_TIME = "TIME";
    public static final String SMS_DESTINATION = "DESTINATION";

    public static final String NOTIFICATION_FRIENDID = "FRIENDID";
    public static final String NOTIFICATION_UT = "UT";
    public static final String NOTIFICATION_N_TYPE = "N_TYPE";
    public static final String NOTIFICATION_ACTIVITY_ID = "ACTIVITY_ID";
    public static final String NOTIFICATION_NEWSFEED_ID = "NEWSFEED_ID";
    public static final String NOTIFICATION_IMAGE_ID = "IMAGE_ID";
    public static final String NOTIFICATION_COMMENT_ID = "COMMENT_ID";
    public static final String NOTIFICATION_MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String NOTIFICATION_FRIEND_NAME = "FRIEND_NAME";
    public static final String NOTIFICATION_IS_READ = "IS_READ";
    public static final String NOTIFICATION_NUMBER_OF_LIKECOMMENT = "NUMBER_OF_LIKECOMMENT";

    public static final String ML_MESSAGE_ID = "MESSAGE_ID";
    public static final String ML_UID = "UID";
    public static final String ML_FOLDER_NAME = "FOLDER_NAME";
    public static final String ML_SUBJECT = "SUBJECT";
    public static final String ML_RCPT_FROM = "RCPT_FROM";
    public static final String ML_RCPT_TO = "RCPT_TO";
    public static final String ML_RCPT_CC = "RCPT_CC";
    public static final String ML_RCPT_BCC = "RCPT_BCC";
    public static final String ML_IN_REPLY_TO = "IN_REPLY_TO";
    public static final String ML_RECEIVE_DATE = "RECEIVE_DATE";
    public static final String ML_SENT_DATE = "SENT_DATE";
    public static final String ML_PLAIN_TEXT = "PLAIN_TEXT";
    public static final String ML_HTML_TEXT = "HTML_TEXT";
    public static final String ML_CONTENT_TYPE = "CONTENT_TYPE";
    public static final String ML_SEEN = "SEEN";

    public static String EMO_NAME = "NAME";
    public static String EMO_SYMBOL = "SYMBOL";
    public static String EMO_URL = "URL";
    public static String EMO_TYPE = "TYPE";

    public static String INS_MSG = "INSTANT_MESSAGE";
    public static String INS_MSG_TYPE = "MESSAGE_TYPE";
    public static String STK_CATEGORY_ID = "CATEGORY_ID";
    public static String STK_CATEGORY_NAME = "CATEGORY_NAME";
    public static String STK_COLLECTION_ID = "COLLECTION_ID";
    public static String STK_CATEGORY_BANNER_IMAGE = "CATEGORY_BANNER_IMAGE";
    public static String STK_CATEGORY_DETAIL_IMAGE = "CATEGORY_DETAIL_IMAGE";
    public static String STK_CATEGORY_RANK = "CATEGORY_RANK";
    public static String STK_CATEGORY_IS_NEW = "CATEGORY_IS_NEW";
    public static String STK_CATEGORY_ICON = "CATEGORY_ICON";
    public static String STK_CATEGORY_PRIZE = "CATEGORY_PRIZE";
    public static String STK_CATEGORY_IS_FREE = "CATEGORY_IS_FREE";
    public static String STK_CATEGORY_IS_DOWNLOADED = "CATEGORY_IS_DOWNLOADED";
    public static String STK_CATEGORY_DESCRIPTION = "CATEGORY_DESCRIPTION";
    public static String STK_IMAGE_ID = "IMAGE_ID";
    public static String STK_IMAGE_URL = "IMAGE_URL";

}

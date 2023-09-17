
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.constants;

/**
 *
 * @author faizahmed
 */
public class AppConstants {

    public static int CLIENT_DATA_SIZE = 100;
    public static final int HEADER_SIZE = 12;
    public static final int DATA_SIZE = 500;
    public static final int DATA_OFFSET = 1;

    public static final int CONS_FULL_NAME = 1;
    //public static final int CONS_LAST_NAME = 2;
    public static final int CONS_GENDER = 3;
    public static final int CONS_COUNTRY = 4;
    public static final int CONS_MOBILE_PHONE = 5;
    public static final int CONS_EMAIL = 6;
    public static final int ACTION_ADD_NUMBER_OR_EMAIL = 214;
    public static final int CONS_PRESENCE = 7;
    public static final int CONS_WHAT_IS_IN_UR_MIND = 8;
    public static final int CONS_FRIENDSHIP_STATUS = 9;
    public static final int CONS_CALLING_CODE = 10;
    public static final int CONS_PREV_PASS = 11;
    public static final int CONS_NEW_PASS = 12;
    public static final int CONS_PROFILE_IMAGE = 13;
    public static final int CONS_BIRTH_DAY = 14;
    public static final int CONS_COVER_IMAGE = 15;
    public static final int CONS_PRIVACY = 16;
    /**/
    public static final int CONS_CURRENT_CITY = 18;
    public static final int CONS_HOME_CITY = 19;
    public static final int CONS_MARRIAGE_DAY = 20;
    public static final int CONS_ABOUT_ME = 21;
    //-------------------------------------- changes made by reefat[13.03.2014] (start) --------------------------------------
    //--------------- revised call type
    public static final int TYPE_CONFIRMATION = 200;
    /*
     * Client<<------->>Server[<100]
     */
    public static final int NEWS_FEED_TYPE_IMAGE = 1;
    public static final int NEWS_FEED_TYPE_STATUS = 2;
    public static final int NEWS_FEED_TYPE_MULTIPLE_IMAGE = 3;
    public static final int NEWS_FEED_TYPE_ALBUM = 4;
    public static final int TYPE_INVALID_LOGIN_SESSION = 19;
    public static final int TYPE_SIGN_IN = 20; // "signin";    
    public static final int TYPE_SIGN_UP = 21; // "signup";
    public static final int TYPE_SIGN_OUT = 22; //"signout";    
    public static final int TYPE_CONTACT_LIST = 23; // "contactList";
    public static final int TYPE_USER_ID_AVAILABLE = 24; // "userIdentityAvailable";
    public static final int TYPE_USER_PROFILE = 25; // "user_profile";    
    public static final int TYPE_KEEP_ALIVE = 26; // "keepAlive";
    public static final int TYPE_WHAT_IS_IN_UR_MIND = 31; // "whatisInYourMind";
    public static final int TYPE_CONTACT_SEARCH = 34; // "contactSearch";
    public static final int TYPE_CALL_LOG = 35; // "callLog";
    public static final int TYPE_PROFILE_IMAGE_TEXT = 37; // "profileImage";
    public static final int TYPE_ADD_ADDRESSBOOK_FRIEND = 38; // "addAddressBookFriend";
    public static final int TYPE_PASSWORD_RECOVERY = 39; // "passwordRecovery";
    public static final int TYPE_PASSWORD_RESET = 40; // "passwordReset";
    public static final int TYPE_EMOTICONS_ALL = 41; // "emoticons_all";
    public static final int TYPE_EMOTICONS = 42; // "emoticons";
    public static final int TYPE_REMOVE_PROFILE_IMAGE = 43; // "removeProfileImage";
    public static final int TYPE_BALANCE = 44; // "balance";
    public static final int TYPE_CREATE_CIRCLE = 50;//"create_group";
    public static final int TYPE_NEW_CIRCLE = 51;//"new_group";
    public static final int TYPE_LEAVE_CIRCLE = 53;// "leave_group";
    public static final int TYPE_SEND_SMS = 61;//”send_sms”
    public static final int TYPE_UPDATE_IMAGE_FROM_WEB = 63;//”update_image_from_web” 
    public static final int TYPE_CIRCLE_LIST = 70;
    public static final int TYPE_ADDRESS_BOOK = 71;
    public static final int TYPE_PRIVACY_SETTINGS = 74;   //"privacy_settings"
    public static final int TYPE_MULTIPLE_SESSION_WARNING = 75;
    public static final int TYPE_SESSION_VALIDATION = 76;
    public static final int TYPE_SERVER_REPORT = 77;
    public static final int TYPE_PRESENCE = 78;
    /* SMS related contants */
    public static final int TYPE_DELETE_SMS = 79;
    public static final int TYPE_SMS_LIST = 80;
    public static final int TYPE_FRIEND_SMS = 81;
    public static final int TYPE_CIRCLE_SMS = 82;
    public final static int TYPE_KNOW_SMS_STATUS = 83;
    /*
     * 81,82 and 83 has bben defined for SMS action types
     * Please do not use them here
     * public final static int TYPE_FRIEND_SMS = 81;
     * public final static int TYPE_CIRCLE_SMS = 82;
     * public final static int TYPE_KNOW_SMS_STATUS = 83;
     * 
     */
    public static final int TYPE_COMMENTS_FOR_STATUS = 84;
    public static final int TYPE_UPLOAD_ALBUM_IMAGE = 85;
    public static final int TYPE_STICKERS = 86;
    public static final int TYPE_TYPE_STICKERS_WITH_CATAGORY = 87;
    public static final int TYPE_NEWS_FEED = 88;
    public static final int TYPE_COMMENTS_FOR_IMAGE = 89;
    public static final int TYPE_UPDATE_UPLOAD_ALBUM_IMAGE = 90;
    public static final int TYPE_STICKERS_BACKGROUND_IMAGES = 91;
    public static final int TYPE_LIKES_FOR_STATUS = 92;
    public static final int TYPE_LIKES_FOR_IMAGE = 93;
    public static final int TYPE_MY_BOOK = 94;
    public static final int TYPE_USER_DETAILS = 95;
    public static final int TYPE_MY_ALBUMS = 96;
    public static final int TYPE_MY_ALBUM_IMAGES = 97;
    public static final int TYPE_FRIENDS_PRESENCE_LIST = 98;
    public static final int TYPE_CIRCLE_MEMBERS_LIST = 99;
    public static final int TYPE_SEND_MOBILE_PASSCODE = 100;
    public static final int TYPE_REMOVE_COVER_IMAGE = 104;
    public static final int TYPE_UPDATE_FRIEND_GROUP = 105;
    public static final int TYPE_YOU_MAY_KNOW_LIST = 106;
    //public static final int TYPE_FRIEND_ALL_DETAILS = 107;
    public static final int TYPE_FRIEND_CONTACT_LIST = 107;
    public static final int TYPE_FRIEND_ALBUMS = 108;
    public static final int TYPE_FRIEND_ALBUM_IMAGES = 109;
    public static final int TYPE_FRIEND_NEWSFEED = 110;
    public static final int TYPE_MY_NOTIFICATIONS = 111;
    public static final int TYPE_DELETE_MY_NOTIFICATIONS = 112;
    public static final int TYPE_SINGLE_NOTIFICATION = 113;
    public static final int TYPE_SINGLE_STATUS_NOTIFICATION = 114;
    /**/
    public static final int TYPE_LIST_COMMENTS_OF_COMMENT = 115;
    public static final int TYPE_LIST_LIKES_OF_COMMENT = 116;
    public static final int TYPE_MULTIPLE_IMAGE_POST = 117;
    public static final int TYPE_UPDATE_TAG_NAME = 119;
    /*
     * Client<<----->>Server<<----->>Client [>100 and <200]     
     */
    public static final int TYPE_IMAGE_DETAILS = 121;
    public static final int TYPE_ADD_COMMENT_ON_COMMENT = 122;
    public static final int TYPE_LIKE_COMMENT = 123;
    public static final int TYPE_REMOVE_COMMENT_ON_COMMENT = 124;
    public static final int TYPE_UNLIKE_COMMENT = 125;
    public static final int TYPE_ADD_PROFILE_DETAILS = 126;
    /**/
    public static final int TYPE_ADD_FRIEND = 127; //"add_friend";
    public static final int TYPE_DELETE_FRIEND = 128; // "delete_friend";
    public static final int TYPE_ACCEPT_FRIEND = 129; //"accept_friend";
    public static final int TYPE_CHANGE_PASSWORD = 130; // "change_password";
    public static final int TYPE_CALL_START = 132; // "call_start";
    public static final int TYPE_CALL_END = 133; // "call_end";
    public static final int TYPE_CALL_LOG_SINGLE_FRIEND = 136; // "callLogSingleFriend";
    public static final int TYPE_DELETE_CIRCLE = 152;// "delete_group";
    public static final int TYPE_REMOVE_CIRCLE_MEMBER = 154;  //"remove_group_member"; 
    public static final int TYPE_ADD_CIRCLE_MEMBER = 156;// "add_group_member";
    public static final int TYPE_EDIT_CIRCLE_MEMBER = 158;//  "edit_group_member";
    public static final int TYPE_ADD_ADDRESS_BOOK_FRIEND = 172;
    public static final int TYPE_ADD_MULTIPLE_FRIEND = 173; //"add_multiple_friend";
    public static final int TYPE_SEND_REGISTER = 174; // "want to start a call";
    public static final int TYPE_DELETE_ALBUM_IMAGE = 176;
    public static final int TYPE_ADD_STATUS = 177;
    public static final int TYPE_EDIT_STATUS = 178;
    public static final int TYPE_DELETE_STATUS = 179;
    public static final int TYPE_ADD_IMAGE_COMMENT = 180;
    public static final int TYPE_ADD_STATUS_COMMENT = 181;
    public static final int TYPE_DELETE_IMAGE_COMMENT = 182;
    public static final int TYPE_DELETE_STATUS_COMMENT = 183;
    public static final int TYPE_LIKE_STATUS = 184;
    public static final int TYPE_LIKE_UNLIKE_IMAGE = 185;
    public static final int TYPE_UNLIKE_STATUS = 186;
    public static final int TYPE_UNLIKE_IMAGE = 187;
    public static final int TYPE_EDIT_STATUS_COMMENT = 189;
    public static final int TYPE_ONILE_OFFLINE_STATUS = 190;
    public static final int TYPE_EDIT_IMAGE_COMMENT = 194;
    public static final int TYPE_CHANGE_NOTIFICATION_STATE = 195;
    public static final int TYPE_IMAGE_COMMENT_LIKES = 196;
    public static final int TYPE_LIKE_UNLIKE_IMAGE_COMMENT = 197;
    public static final int TYPE_CIRCLE_NEWSFEED = 198;
    public static final int TYPE_FRIEND_PRESENCE_INFO = 199;
    public static final int TYPE_DEACTIVATE_ACCOUNT = 202;
    public static final int TYPE_UNKNWON_PROFILE_INFO = 204;
    public static final int TYPE_ACTION_VERIFY_MY_NUMBER = 212;
    public static final int TYPE_VERIFY_NUMBER_OR_EMAIL = 215;
    public static final int TYPE_UPDATE_TOGGLE_SETTINGS = 216;
    public static final int TYPE_SEND_RECOVERED_PASSCODE = 217;
    public static final int TYPE_VERIFY_RECOVERED_PASSCODE = 218;
    public static final int TYPE_RESET_PASSWORD = 219;
    public static final int TYPE_SEND_EMAIL_PASSCODE = 220;
    public static final int TYPE_ADD_VERIFY_EMAIL = 221;
    public static final int TYPE_RECOVERY_SUGGESTION = 222;
    public static final int TYPE_GROUP_LIST = 225;
    public static final int TYPE_REMOVE_GROUP = 226;
    public static final int TYPE_ADD_WORK = 227;
    public static final int TYPE_UPDATE_WORK = 228;
    public static final int TYPE_REMOVE_WORK = 229;
    public static final int TYPE_LIST_WORKS_EDUCATIONS_SKILLS = 230;
    public static final int TYPE_ADD_EDUCATION = 231;
    public static final int TYPE_UPDATE_EDUCATION = 232;
    public static final int TYPE_REMOVE_EDUCATION = 233;
    public static final int TYPE_LIST_WORK = 234;
    public static final int TYPE_LIST_EDUCATION = 235;
    public static final int TYPE_LIST_SKILL = 236;
    public static final int TYPE_ADD_SKILL = 237;
    public static final int TYPE_UPDATE_SKILL = 238;
    public static final int TYPE_REMOVE_SKILL = 239;
    public static final int TYPE_ADD_GROUP_CHAT_MEMBER = 240;
    public static final int TYPE_GROUP_DETAILS = 241;
    public static final int TYPE_REMOVE_GROUP_CHAT_MEMBER = 242;
    public static final int TYPE_BLOCK_UNBLOCK_FRIEND = 243;
    public static final int TYPE_CHANGE_FRIEND_ACCESS = 244;
    public static final int TYPE_ACCEPT_FRIEND_ACCESS = 245;
    public static final int TYPE_FRIEND_MIGRATION = 247;

    /*
     * Client<<----->>Server<<----->>Client [>100 and <200]     
     */
    public static final int TYPE_UPDATE_COMMENT_ON_COMMENT = 322;
    public static final int TYPE_UPDATE_LIKE_COMMENT = 323;
    public static final int TYPE_UPDATE_REMOVE_COMMENT_ON_COMMENT = 324;
    public static final int TYPE_UPDATE_UNLIKE_COMMENT = 325;
    public static final int TYPE_UPDATE_ADD_FRIEND = 327; //"add_friend";    
    public static final int TYPE_UPDATE_DELETE_FRIEND = 328; // "delete_friend";    
    public static final int TYPE_UPDATE_ACCEPT_FRIEND = 329; //"accept_friend";
    public static final int TYPE_UPDATE_CHANGE_PASSWORD = 330; // "change_password";
    public static final int TYPE_UPDATE_CALL_START = 332; // "call_start";
    public static final int TYPE_UPDATE_CALL_END = 333; // "call_end";
    public static final int TYPE_UPDATE_CALL_LOG_SINGLE_FRIEND = 336; // "callLogSingleFriend";
    public static final int TYPE_UPDATE_DELETE_CIRCLE = 352;// "delete_group";
    public static final int TYPE_UPDATE_REMOVE_CIRCLE_MEMBER = 354;  //"remove_group_member"; 
    public static final int TYPE_UPDATE_ADD_CIRCLE_MEMBER = 356;// "add_group_member";
    public static final int TYPE_UPDATE_EDIT_CIRCLE_MEMBER = 358;//  "edit_group_member";
    public static final int TYPE_UPDATE_ADD_ADDRESS_BOOK_FRIEND = 372;
    public static final int TYPE_UPDATE_ADD_MULTIPLE_FRIEND = 373; //"add_multiple_friend";
    public static final int TYPE_UPDATE_SEND_REGISTER = 374; // "want to start a call";
    public static final int TYPE_UPDATE_START_CHAT = 375; // "want to start a call";
    public static final int TYPE_UPDATE_DELETE_ALBUM_IMAGE = 376;
    public static final int TYPE_UPDATE_ADD_STATUS = 377;
    public static final int TYPE_UPDATE_EDIT_STATUS = 378;
    public static final int TYPE_UPDATE_DELETE_STATUS = 379;
    public static final int TYPE_UPDATE_ADD_IMAGE_COMMENT = 380;
    public static final int TYPE_UPDATE_ADD_STATUS_COMMENT = 381;
    public static final int TYPE_UPDATE_DELETE_IMAGE_COMMENT = 382;
    public static final int TYPE_UPDATE_DELETE_STATUS_COMMENT = 383;
    public static final int TYPE_UPDATE_LIKE_STATUS = 384;
    public static final int TYPE_UPDATE_LIKE_UNLIKE_IMAGE = 385;
    public static final int TYPE_UPDATE_UNLIKE_STATUS = 386;
    public static final int TYPE_UPDATE_EDIT_STATUS_COMMENT = 389;
    public static final int TYPE_UPDATE_EDIT_IMAGE_COMMENT = 394;
    public static final int TYPE_UPDATE_LIKE_UNLIKE_IMAGE_COMMENT = 397;
    public static final int TYPE_UPDATE_DEACTIVATE_ACCOUNT = 402;
    public static final int TYPE_UPDATE_ADD_GROUP_CHAT_MEMBER = 440;
    public static final int TYPE_UPDATE_REMOVE_GROUP_CHAT_MEMBER = 442;
    public static final int TYPE_UPDATE_CHANGE_FRIEND_ACCESS = 444;
    public static final int TYPE_UPDATE_ACCEPT_FRIEND_ACCESS = 445;
    public static final int TYPE_UPDATE_FRIEND_MIGRATION = 447;

    //others constants
    //  public static String CALLEE_FRIEND_ID = "CALLEE_FRIEND_ID";
    public static String TERMINATION_RULE = "IP PORT";
    //    /*PRIVACE*/
    public static final short PRIVACY_SHORT_PUBLIC = 1;
    public static final short PRIVACY_SHORT_ONLY_FRIEND = 2;
    public static final short PRIVACY_SHORT_ONLY_ME = 3;
    /**/
    public final static int STATUS_IN_PROGRESS = 201;
    public final static int STATUS_SENT = 202;
    public final static int STATUS_NOT_DELEVERED = 203;
    //public final static int STATUS_NOT_ENOUGH_BALANCE = 204;
    /* SMS History */
    public final static int HISTORY_STATUS_IN_PROGRESS = 0;
    public final static int HISTORY_STATUS_FAILED = 1;
    public final static int HISTORY_STATUS_SENT = 2;
    public final static int HISTORY_STATUS_DELETED = 3;
    /*chat*/
    public static final int TYPE_START_FRIEND_CHAT = 175; // "want to start friend chat";
    public static final int TYPE_START_GROUP_CHAT = 188; // "want to start group chat";
    /*notification max ut*/
    public static long NOTIFICATION_MAX_UT = 0;
    public static long NOTIFICATION_MIN_UT = 0;
    public static long NOTIFICATION_LAST_MAX_UT = 0;

    //Device Platform Constants
    public static final int PLATFORM_DESKTOP = 1;
    public static final int PLATFORM_ANDEOID = 2;
    public static final int PLATFORM_IPHONE = 3;
    public static final int PLATFORM_WINDOWS = 4;
    public static final int PLATFORM_WEB = 5;

    /*friend feed activitys*/
    public static final short NEWSFEED_LIKED = 1;
    public static final short NEWSFEED_COMMENTED = 2;
    public static final short NEWSFEED_SHARED = 3;

    /* TIME INTERVALS */
    public static long TEN_MINUTES = 600000; // 5 minutes
    public static long FIVE_MINUTES = 300000; // 5 minutes
    

}

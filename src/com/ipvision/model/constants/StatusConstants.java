/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.constants;

/**
 *
 * @author FaizAhmed
 */
public class StatusConstants {

    public static final Integer PRESENCE_OFFLINE = 1;
    public static final Integer PRESENCE_ONLINE = 2;
    public static final Integer PRESENCE_AWAY = 3;
    public static final Integer PRESENCE_DO_NOT_DISTURB = 4;
    // public static final int PRESENCE_INVISIBLE = 3;
    /**/
    public static final Integer FRIENDSHIP_STATUS_ACCEPTED = 1;
    public static final Integer FRIENDSHIP_STATUS_INCOMING = 2;
    public static final Integer FRIENDSHIP_STATUS_PENDING = 3;
    public static final short STATUS_DELETED = 1;

    // Contct Access
    public static final int ACCESS_CHAT_CALL = 1;
    public static final int ACCESS_FULL = 2;

    //Notification Type
    public static final int NOTIFICATION_CONTACT = 1;
    public static final int NOTIFICATION_BOOK = 2;
    public static final int NOTIFICATION_SMS = 3;
    public static final int NOTIFICATION_CIRCLE = 4;
    public static final int NOTIFICATION_PROFILE = 5;

    //Notification Message Type
    public static final int MESSAGE_UPDATE_PROFILE_IMAGE = 1;// Example: FrinedName(fndN) updated his profile photo.
    public static final int MESSAGE_UPDATE_COVER_IMAGE = 2; //Example: FrinedName(fndN) updated his cover photo.
    public static final int MESSAGE_ADD_FRIEND = 3; //Example: FrinedName(fndN) wants to be friends with you.
    public static final int MESSAGE_ACCEPT_FRIEND = 4; //Example: FrinedName(fndN) has accepted your friend request.
    public static final int MESSAGE_ADD_CIRCLE_MEMBER = 5; //Example: FrinedName(fndN) added you in groupName(Using groupId need to find groupName).
    public static final int MESSAGE_ADD_STATUS_COMMENT = 6; //Example: FriendName(fndN) commented on your status. or Example: FriendName(fndN) & previousFriendName Commented on your status.
    public static final int MESSAGE_LIKE_STATUS = 7; //Example: FriendName(fndN) liked your status. or Example: FriendName(fndN) & previousFriendName liked your status.
    public static final int MESSAGE_LIKE_COMMENT = 8; //
    public static final int MESSAGE_ADD_COMMENT_ON_COMMENT = 9;
    public static final int MESSAGE_SHARE_STATUS = 10;
    public static final int MESSAGE_LIKE_IMAGE = 11; //
    public static final int MESSAGE_IMAGE_COMMENT = 12;
    public static final int MESSAGE_LIKE_IMAGE_COMMENT = 13;
    public static final int MESSAGE_ACCEPT_FRIEND_ACCESS = 15;
    public static final int MESSAGE_UPGRADE_FRIEND_ACCESS = 14;
    public static final int MESSAGE_DOWNGRADE_FRIEND_ACCESS = 16;
    public static final int MESSAGE_NOW_FRIEND = 17;

    //Search category
    public static final int SEARCH_BY_ALL = 0;
    public static final int SEARCH_BY_NAME = 1;
    public static final int SEARCH_BY_PHONE = 2;
    public static final int SEARCH_BY_EMAIL = 3;
    public static final int SEARCH_BY_RINGID = 4;
    public static final int SEARCH_BY_LOCATION = 5;

    public static final int WEB_LOGIN_ENABLED = 1;
    public static final int PC_LOGIN_ENABLED = 2;
    public static final int COMMON_FRIENDS_SUGGESTION = 3;
    public static final int PHONE_NUMBER_SUGGESTION = 4;
    public static final int CONTACT_LIST_SUGGESTION = 5;
}

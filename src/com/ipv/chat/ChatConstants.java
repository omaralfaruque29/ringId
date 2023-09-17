/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatConstants {

    //Client Registration Constants
    public static final int CHAT_FRIEND_REGISTER = 1;
    public static final int CHAT_FRIEND_UNREGISTER = 2;
    public static final int CHAT_FRIEND_REGISTER_CONFIRMATION = 3;
    public static final int CHAT_GROUP_UNREGISTER = 4;

    //Friend to Friend Chat Constants
    public static final int CHAT_FRIEND = 5;
    public static final int CHAT_FRIEND_DELIVERED = 6;
    public static final int CHAT_FRIEND_SEEN = 7;
    public static final int CHAT_FRIEND_SENT = 8;
    public static final int CHAT_FRIEND_TYPING = 9;
    public static final int CHAT_FRIEND_IDEL = 10;

    //Group Chat Constants
    public static final int CHAT_GROUP_REGISTER = 12;
    public static final int CHAT_GROUP_REGISTER_CONFIRMATION = 13;
    public static final int CHAT_GROUP = 14;
    public static final int CHAT_GROUP_DELIVERED = 15;
    public static final int CHAT_GROUP_SEEN = 16;
    public static final int CHAT_GROUP_SENT = 17;
    public static final int CHAT_GROUP_TYPING = 18;
    public static final int CHAT_GROUP_IDEL = 19;

    //Offline Message
    public static final int CHAT_FRIEND_OFFLINE = 20;
    public static final int CHAT_GROUP_OFFLINE = 21;
    public static final int CHAT_OFFLINE_REQUEST = 22;
    public static final int CHAT_OFFLINE_REQUEST_CONFIRMATION = 23;

    //Chat Delete Constants
    public static final int CHAT_FRIEND_MESSAGE_DELETED = 24;
    public static final int CHAT_FRIEND_MESSAGE_DELETE_CONFIRMATION = 25;
    public static final int CHAT_GROUP_MESSAGE_DELETED = 26;
    public static final int CHAT_GROUP_MESSAGE_DELETE_CONFIRMATION = 27;

    public static final int CHAT_FRIEND_INFORMATION = 28;
    public static final int CHAT_FRIEND_INFORMATION_CONFIRMATION = 29;

    //Group Chat
    public static final int CHAT_GROUP_MEMBERS_SEND = 34;
    public static final int CHAT_GROUP_MEMBERS_SEND_CONFIRMATION = 35;
    public static final int CHAT_GROUP_MEMBERS_GET = 36;
    public static final int CHAT_GROUP_MEMBER_REMOVE_OR_LEAVE = 37;
    public static final int CHAT_GROUP_MEMBER_REMOVE_OR_LEAVE_CONFIRMATION = 38;
    public static final int CHAT_GROUP_DELETE = 39;
    public static final int CHAT_GROUP_DELETE_CONFIRMATION = 40;
    public static final int CHAT_FRIEND_MULTIPLE_MESSAGE_DELETE = 41;
    public static final int CHAT_GROUP_MULTIPLE_MESSAGE_DELETE = 42;
    public static final int CHAT_FRIEND_CHAT_EDIT = 43;
    public static final int CHAT_GROUP_CHAT_EDIT = 44;
    public static final int CHAT_FRIEND_MULTIPLE_MESSAGE = 45;
    public static final int CHAT_FRIEND_BROCKEN_MESSAGE = 46;
    public static final int CHAT_GROUP_BROCKEN_MESSAGE = 47;
    public static final int CHAT_FRIEND_CHAT_EDIT_BROCKEN_MESSAGE = 48;
    public static final int CHAT_GROUP_CHAT_EDIT_BROCKEN_MESSAGE = 49;

    public static final int CHAT_FRIEND_FILE_STREAM_REQUEST = 50;
    public static final int CHAT_FRIEND_FILE_STREAM_REQUEST_CONFIRMATION = 51;
    public static final int CHAT_FRIEND_FILE_STREAM_SEND = 52;
    public static final int CHAT_FRIEND_FILE_STREAM_SEND_CONFIRMATION = 53;
    
    public static final int CHAT_GROUP_FILE_STREAM_REQUEST = 54;
    public static final int CHAT_GROUP_FILE_STREAM_REQUEST_CONFIRMATION = 55;
    public static final int CHAT_GROUP_FILE_STREAM_SEND = 56;
    public static final int CHAT_GROUP_FILE_STREAM_SEND_CONFIRMATION = 57;

    //User Online Status Constants
    public static final int PRESENCE_ONLINE = 2;
    public static final int PRESENCE_AWAY = 3;
    public static final int PRESENCE_OFFLINE = 1;
    public static final int PRESENCE_DO_NOT_DISTURB = 4;
    public static final int NUMBER_OF_RESEND = 5;
    public static final int NUMBER_OF_ADDITIONAL_RESEND = 4;
    public static final int UNIT_OF_DELAY = 250;
    public static final int DELAY_OF_RESEND = 3000;
    public static final int REGISTRATION_HOLD_DURATION = 180000;

    public static final int TYPE_DELETE_MSG = 0;
    public static final int TYPE_BLANK_MSG = 1;
    public static final int TYPE_PLAIN_MSG = 2;
    public static final int TYPE_EMOTICON_MSG = 3;
    public static final int TYPE_LARGE_EMOTICON_MSG = 4;
    public static final int TYPE_TEXT_EMOTICON_MSG = 5;
    public static final int TYPE_DOWNLOADED_STICKER_MSG = 6;
    public static final int TYPE_IMAGE_FILE_DIRECTORY = 7;
    public static final int TYPE_AUDIO_FILE = 8;
    public static final int TYPE_VIDEO_FILE = 9;
    public static final int TYPE_IMAGE_FILE_RING_CAMERA = 10;
    public static final int TYPE_STREAM_FILE = 11;

    public static final int CHAT_GROUP_PENDING = 0; //Custom
    public static final int CHAT_FRIEND_PENDING = 0; //Custom

    //Device Platform Constants
    public static final int PLATFORM_DESKTOP = 1;
    public static final int PLATFORM_ANDEOID = 2;
    public static final int PLATFORM_IPHONE = 3;
    public static final int PLATFORM_WINDOWS = 4;
    public static final int PLATFORM_WEB = 5;
    
    public static final String BROKEN_PACKET_SEPARATOR = "_";
}

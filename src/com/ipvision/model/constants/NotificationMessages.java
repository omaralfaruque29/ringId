/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.constants;

public class NotificationMessages {

    private static final String are_you_sure = "Are you sure? ";
    public static final String COM_PORT_PROBLEM = "Please check your network status and try again";
    public static final String NETWORK_PROBLEM = "Please check your network status";
    public static final String SIGN_OUT_NOTIFICAITON = are_you_sure + "you want to sign out of " + StaticFields.APP_NAME + "?";
    public static final String EXIT_NOTIFICAITON = are_you_sure + " you want to exit?";
    public static final String CANCEL_NOTIFICAITON = are_you_sure + "you want to cancel?";
    public static final String REJECT_NOTIFICAITON = are_you_sure + "you want to reject?";
    public static final String REMOVE_NOTIFICAITON = are_you_sure + "you want to remove?";
    public static final String DELETE_NOTIFICAITON = are_you_sure + "you want to delete?";
    public static final String LEAVE_NOTIFICAITON = are_you_sure + "you want to leave?";
    public static final String CALL_TERMINATE = "Previous call will be terminated?";
    public static final String ERROR_IN_UPLOAD_NOTIFICAITON = "Cannot upload image";
    public static final String NO_LONGER_FRIEND_NOTIFICAITON = "Sorry! This friend is no longer in your contacts";
    public static final String NO_LONGER_IN_GROUP_NOTIFICAITON = "Sorry! No longer in this group";
    public static final String CALL_LIMIT_NOTIFICATION = "You have reached your call limit for today";
    public static final String NO_FRIEND = "No friend";
    public static final String NOT_SLECTE_FRIEND = "Friend has not been selected";
    public static final String OFFLINE_NOTIFICATION = "You are currently offline";
    public static final String CIRCLE_CREATE_SUCCESS = "Circle created successfully";
    public static final String CIRCLE_EDIT_SUCCESS = "Circle edited successfully";
    public static final String DELETE_SUCCESS = " Deleted successfully";
    public static final String INVALID_CIRCLE_NAME = "Please enter valid circle name";
    public static final String NO_CIRCLE_MEMBER = "You need members in this circle";
    public static final String CAN_NOT_UPLOAD_IMAGE = "Cannot upload image";
    public static final String CAN_NOT_UPLOAD_FILE = "Cannot upload file!";
    public static final String CAN_NOT_FOUND_IMAGE = "Image not found.";
    public static final String CAN_NOT_FOUND_FILE = "File not found.";
    public static final String NEW_MEMBER_ADDED = "New members added successfully";
    public static final String CIRCLE_EDITED_SUCCESSFULLY = "Circle has been edited successfully";
    public static final String CAN_NOT_CREATE_CIRCLE = "Cannot create circle";
    public static final String CAN_NOT_CREATE_GROUP = "Cannot create group";
    public static final String INCOMING_REQUEST = "Incoming request";
    public static final String PENDING_REQUEST = "Pending request";
    /*SIGN UP REQUEST*/
    public static final String ATLEAST_4_CHARACTER_NEEDED = " Name must be at least 4 character long";
    public static final String START_WITH_LETTER = "Name must start with a letter";
    public static final String CAN_CONTAINS_ONLY = " Password can only contain alphabets and underscore ";
    public static final String FIRST_NAMME = "Please enter first name";
    public static final String LAST_NAME = "Please enter last name";
    public static final String MUST_HAVE_6_CHAR = " must be of at least 6 character";
    public static final String COMMA_NOT_ALLOWED = "Comma(,) is not allowed ";
    public static final String CAN_NOT_CANTAIN = " can't contain ";
    public static final String PASSWORD_MISSMATCH = "Password does not match";
    public static final String VALID_EMAIL = "Please enter a valid email address";
    public static final String VALID_PHONE = "Please enter your phone number";
    public static final String CHOOSE_CALLING_CODE = "Please specify calling code";
    public static final String CHOOSE_COUNTRY = "Please select a country";
    public static final String CHOOSE_GENDER = "Please select your gender";
    public static final String DOWNLOAD_NEW_VERSION = "Please download the new version of "
            + StaticFields.APP_NAME + "";
    public static final String MAX_STATUS_LENGTH_OVER = "Number of characters should be less than 400.";

    /**/
    /*NOTIFICATION MESSAGE*/
    public static final String LIKES_STRING = "like";
    public static final String MSG_UPDATE_PROFILE_IMAGE = "updated profile photo";
    public static final String MSG_UPDATE_COVER_IMAGE = "updated cover photo";
    public static final String MSG_ADD_FRIEND = "wants to be your friend";
    public static final String MSG_ACCEPT_FRIEND = " accepted your friend request";
    public static final String MSG_ADD_CIRCLE_MEMBER = "added you in a Circle";
    public static final String MSG_ADD_STATUS_COMMENT = "commented on your post";
    public static final String MSG_LIKE_STATUS = LIKES_STRING + "d your post";
    public static final String MSG_LIKE_COMMENT = LIKES_STRING + "d your comment";
    public static final String MSG_ADD_COMMENT_ON_COMMENT = "commented on a post where you have commented";
    public static final String MSG_SHARE_STATUS = "shared your post";
    public static final String MSG_LIKE_IMAGE = LIKES_STRING + "d your photo";
    public static final String MSG_IMAGE_COMMENT = "commented on your photo";
    public static final String MSG_LIKE_IMAGE_COMMENT = LIKES_STRING + "d your comment in a photo";
    public static final String MSG_ACCEPT_FRIEND_ACCESS = "accepted your friend access";
    public static final String MSG_UPGRADE_FRIEND_ACCESS = "upgraded friend access";
    public static final String MSG_DOWNGRADE_FRIEND_ACCESS = "downgraded friend access";
    public static final String MSG_NOW_FRIEND = "added you as a friend";
    /**/
    public static final String VIDEO_CALL_NOTIFICATION = "Video Calling is currently not supported on Desktop. This feature will be available soon.";
    public static final String ROUTE_CALL_NOTIFICATION = "Making free calls to landlines and mobile numbers is currently not supported on Desktop. This feature will be available soon.";
    public static final String SMS_NOTIFICATION = "Sending free SMS is currently not supported on Desktop. This feature will be available soon.";
    public static final String RING_MAIL_NOTIFICATION = "Secure Ring Mail is currently not supported on Desktop. This feature will be available soon.";
    public static final String FRIEND_ACCESS_NOTIFICAITON = "You have a request of changing friend information access permission to '{0}'. " + are_you_sure + "you want to accept?";
    public static final String INTERNET_UNAVAILABLE = "Internet unavailable";
}

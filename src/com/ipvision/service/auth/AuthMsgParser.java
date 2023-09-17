/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.SignalGenerator;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.ChatRegistrationDTO;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.ActivityConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.AppConstants;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import com.ipvision.service.uploaddownload.ImageDownloader;
import java.io.File;
import java.text.SimpleDateFormat;
import com.ipvision.model.call.CallLog;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.CircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.FriendlistMapping;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SearchFreindlListMapping;
import com.ipvision.service.utility.SendToServer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.dao.DeleteFromCircleListTable;
import com.ipvision.model.dao.DeleteFromCircleMemberListTable;
import com.ipvision.model.dao.InsertIntoCircleListTable;
import com.ipvision.model.dao.InsertIntoCircleMemberTable;
import com.ipvision.model.AlbumImageMap;
import com.ipvision.model.AlbumMap;
import com.ipvision.model.CommentsMap;
import com.ipvision.model.LikesMap;
import com.ipvision.model.NewsFeedMap;
import com.ipvision.model.dao.DeleteFromContactListTable;
import com.ipvision.model.dao.DeleteFromGroupListTable;
import com.ipvision.model.dao.DeleteFromGroupMemberListTable;
import com.ipvision.model.dao.InsertIntoContactListTable;
import com.ipvision.model.dao.InsertIntoRingCallLogTable;
import com.ipvision.model.dao.InsertIntoNotificationHistoryTable;
import com.ipvision.model.dao.InsertIntoGroupListTable;
import com.ipvision.model.dao.InsertIntoGroupMemberTable;
import com.ipvision.model.dao.InsertIntoRingActivityTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.CircleListMap;
import com.ipvision.model.CircleMembersMap;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.ShareFeedResponse;
import com.ipvision.model.GroupListMapping;
import com.ipvision.view.MenuBarTop;
import com.ipvision.service.GetGroupDetails;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.feed.ChooseFromAlbum;
import com.ipvision.view.feed.ImagesInAlbumFolder;
import com.ipvision.view.feed.ImagesInAlbumFolderCircle;
import com.ipvision.view.feed.ImagesInAlbumFolderFriend;
import com.ipvision.view.feed.LikePopupSingleFeed;
import com.ipvision.view.feed.LikesInCommentsJdialog;
import com.ipvision.view.friendprofile.MyFriendAlbumPanel;
import com.ipvision.view.friendprofile.MyFriendNewsFeedPanel;
import com.ipvision.model.call.CallLogList;
import com.ipvision.view.feed.SingleCommentView;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.feed.SingleStatusPanelInNotification;
import com.ipvision.view.feed.SingleStatusPanelInShareFeed;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.ChatIntiatorDto;
import com.ipvision.model.PressenceDto;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.view.myprofile.ProfileCoverChangePanel;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.call.CallUtils;
import com.ipvision.model.call.CallerDto;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.dao.DeactivateAccountDAO;
import com.ipvision.view.circle.CircleViewRight;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.EducationListDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.SkillListDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.UserDetailsInfoDTO;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.model.WorkListDTO;
import com.ipvision.model.MigrationFriendlistMapping;
import com.ipvision.view.myprofile.CreateEducationSinglePanel;
import com.ipvision.view.myprofile.CreateSkillSinglePanel;
import com.ipvision.view.myprofile.CreateWorkSinglePanel;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.service.utility.SortedArrayList;
import com.ipvision.view.image.AddImageCaptionCommentsPanel;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.image.ImageLikesPopup;
import com.ipvision.view.image.LikesInImageCommentsPopup;
import com.ipvision.view.image.SingeImageCommentDetails;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.view.image.SingleImageDetailsProfileCover;
import com.ipvision.view.image.SingleImgeDetails;
import com.ipvision.view.myprofile.Photos;
import com.ipvision.view.utility.FeedUtils;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import org.omg.CORBA.portable.UnknownException;

/**
 *
 * @author user
 */
public class AuthMsgParser {

    public static int contactListSeqCount = 0;
    //public static int friendContactListSeqCount = 0;
    private static int peopleyouMayKnowcount = 0;
    private static int workListSequenceCount = 0;
    private static int educationListSequenceCount = 0;
    private static int skillListSequenceCount = 0;

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AuthMsgParser.class);
    DownLoaderHelps dHelp = new DownLoaderHelps();
    String response;
    private FeedBackFields js;

    public AuthMsgParser(String response) {
        this.response = response != null ? response.trim() : "";
        js = HelperMethods.getFeedBackDto(this.response);
    }

    public void process() {
        try {
            log.info("Received from Auth ==> " + response);
            if (js != null) {
                if (js.getPacketId() != null && js.getPacketId().length() > 0) {
                    if (js.getAction() != AppConstants.TYPE_SEND_REGISTER
                            || js.getAction() != AppConstants.TYPE_SIGN_IN
                            || js.getAction() != AppConstants.TYPE_ADD_STATUS
                            || js.getAction() != AppConstants.TYPE_EDIT_STATUS
                            || js.getAction() != AppConstants.TYPE_EDIT_STATUS_COMMENT) {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    }
                }
                if (js.getAction() != null && js.getPacketIdFromServer() != null && js.getPacketIdFromServer().length() > 0) {
                    SendToServer.sendConfirmationWhenPacketIdFromServer(js.getAction(), js.getPacketIdFromServer(), ServerAndPortSettings.CONFIRMATION_PORT);
                }
                this.processResponse();
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Auth parse==>" + e.getMessage() + " message==>" + response);
        }

    }

    // *************************************************************************************************
    // Response Handler Methods
    // *************************************************************************************************
    private void processResponse() {
        if (js.getAction() != null) {
            int action = js.getAction();

            switch (action) {
                case AppConstants.TYPE_INVALID_LOGIN_SESSION: //19
                    this.processInvalidLoginSession();
                    break;

                case AppConstants.TYPE_SIGN_IN: //20
                    //if (MyFnFSettings.userProfile == null) {
                    //if (js.getSuccess()) {
                    js.setMessage(response);
                    // }
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    //}
                    break;

//            case AppConstants.TYPE_SIGN_UP: //21
//                break;
                case AppConstants.TYPE_SIGN_OUT: //22
                    this.processSignOut();
                    break;

                case AppConstants.TYPE_CONTACT_LIST: //23
                    this.processContactList();
                    break;

//            case AppConstants.TYPE_USER_ID_AVAILABLE: //24
////                if (SignUP.getSignUP() != null && SignUP.getSignUP().isVisible()) {
////                    SignUP.getSignUP().addUseridMessage(js);
////                }
//                break;
                case AppConstants.TYPE_USER_PROFILE: //25
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;

                case AppConstants.TYPE_WHAT_IS_IN_UR_MIND: //31
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;

                case AppConstants.TYPE_CONTACT_SEARCH: //34
                    this.processContactSearch();
                    break;
                case AppConstants.TYPE_CALL_LOG: //35
                    this.processCallLog();
                    break;

                case AppConstants.TYPE_PASSWORD_RECOVERY: //39
                    break;

                case AppConstants.TYPE_PASSWORD_RESET: //40
                    break;

                case AppConstants.TYPE_REMOVE_PROFILE_IMAGE: //43
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;

                case AppConstants.TYPE_BALANCE: //44
                    MyFnFSettings.userProfile.setBalance(js.getBalance());
                    break;

                case AppConstants.TYPE_CREATE_CIRCLE: //50
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;

                case AppConstants.TYPE_NEW_CIRCLE: //51
                    this.processNewCircle();
                    break;

                case AppConstants.TYPE_LEAVE_CIRCLE: //53
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;

                case AppConstants.TYPE_CIRCLE_LIST: //70
                    this.processCircleList();
                    break;

//            case AppConstants.TYPE_PRIVACY_SETTINGS: //74
//                // MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
//                break;
                case AppConstants.TYPE_MULTIPLE_SESSION_WARNING: //75
                    this.processMultipleSessionWarning();
                    break;

                case AppConstants.TYPE_PRESENCE: //78
                    this.processPresence();
                    break;
                case AppConstants.TYPE_SMS_LIST: //80
                    //   this.processSmsList();
                    break;
                case AppConstants.TYPE_FRIEND_SMS: //81
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;
                case AppConstants.TYPE_CIRCLE_SMS: //82
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    break;
                case AppConstants.TYPE_COMMENTS_FOR_STATUS: //84
                    this.processCommentsForStatus();
                    break;
                case AppConstants.TYPE_NEWS_FEED: //88
                    this.processNewsFeed();
                    break;
                case AppConstants.TYPE_COMMENTS_FOR_IMAGE: //89
                    this.processCommentsForImage();
                    break;
                case AppConstants.TYPE_LIKES_FOR_STATUS: //92
                    this.processFetchLikesForStatus();
                    break;
                case AppConstants.TYPE_LIKES_FOR_IMAGE: //93
                    this.processFetchLikesForImage();
                    break;
                case AppConstants.TYPE_MY_BOOK: //94
                    this.processMyBook();
                    break;
                case AppConstants.TYPE_USER_DETAILS: //95
                    this.processFriendDetails();
                    break;
                case AppConstants.TYPE_MY_ALBUMS: //96
                    this.processMyAlbums();
                    break;
                case AppConstants.TYPE_MY_ALBUM_IMAGES: //97
                    this.processMyAlbumImages();
                    break;
                case AppConstants.TYPE_FRIENDS_PRESENCE_LIST: //98
                    this.processFriendsPresenceList();
                    break;
                case AppConstants.TYPE_CIRCLE_MEMBERS_LIST: //99
                    this.processCircleMembersList();
                    break;
                case AppConstants.TYPE_YOU_MAY_KNOW_LIST: //106
                    this.processPeopleYouMayKnow();
                    break;
                case AppConstants.TYPE_FRIEND_CONTACT_LIST: //107
                    this.processFriendContactList();
                    break;
                case AppConstants.TYPE_FRIEND_ALBUMS: //108
                    this.processFriendsAlbums();
                    break;
                case AppConstants.TYPE_FRIEND_ALBUM_IMAGES: //109
                    this.processFriendsAlbumImages();
                    break;
                case AppConstants.TYPE_FRIEND_NEWSFEED: //110
                    this.processFriendNewsFeed();
                    break;
                case AppConstants.TYPE_MY_NOTIFICATIONS: //111
                    this.processMyNotifications();
                    break;
                case AppConstants.TYPE_SINGLE_NOTIFICATION: //113
                    this.processSingleNotification();
                    break;
                case AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION: //114
                    this.processSingleStatusNotificationPanel();
                    break;
                case AppConstants.TYPE_LIST_LIKES_OF_COMMENT: //116
                    this.processFetchLikesForAComment();
                    break;
                case AppConstants.TYPE_MULTIPLE_IMAGE_POST: //117
                    this.processTypeMultipleImagePost();
                    break;
                case AppConstants.TYPE_IMAGE_DETAILS: //121
                    this.processTypeImageDetails();
                    break;
                case AppConstants.TYPE_ADD_FRIEND: //127e
                    this.processAddFriend();
                    break;
//            case AppConstants.TYPE_DELETE_FRIEND: //128
//                break;

                case AppConstants.TYPE_ACCEPT_FRIEND: //129
                    this.processAcceptFriend();
                    break;
                case AppConstants.TYPE_DELETE_CIRCLE: //152
                    this.processDeleteCircle();
                    break;

                case AppConstants.TYPE_REMOVE_CIRCLE_MEMBER: //154
                    this.processRemoveCircleMember();
                    break;

//            case AppConstants.TYPE_ADD_CIRCLE_MEMBER: //156
//                break;
//
//            case AppConstants.TYPE_EDIT_CIRCLE_MEMBER: //158
//                break;
//
//            case AppConstants.TYPE_ADD_ADDRESS_BOOK_FRIEND: //172
//                //   this.processAddAddressBookFriend();
//                break;
                case AppConstants.TYPE_SEND_REGISTER: //174
                    if (js.getPacketId() != null && js.getPacketId().length() > 0) {
                        if (js.getSuccess()) {
                            js.setMessage(response);
                        }
                        //  System.out.println("response 174==" + response);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(js.getPacketId(), js);
                    }
                    break;

                case AppConstants.TYPE_START_FRIEND_CHAT: //175
                    this.processStartFriendChat();
                    break;
                case AppConstants.TYPE_ADD_STATUS: //177
                    if (js.getPacketId() != null && js.getPacketId().length() > 0) {
                        if (js.getSuccess()) {
                            js.setMessage(response);
                        }
                        String makRespnse = FeedUtils.makeResponsePacket(js.getPacketId(), AppConstants.TYPE_ADD_STATUS);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(makRespnse, js);
                    }
                    break;
                case AppConstants.TYPE_EDIT_STATUS: //178
                    if (js.getPacketId() != null && js.getPacketId().length() > 0) {
                        if (js.getSuccess()) {
                            js.setMessage(response);
                        }
                        String makResponse = FeedUtils.makeResponsePacket(js.getPacketId(), AppConstants.TYPE_EDIT_STATUS);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(makResponse, js);
                    }
                    break;
                case AppConstants.TYPE_START_GROUP_CHAT: //188
                    this.processStartGroupChat();
                    break;
                case AppConstants.TYPE_EDIT_STATUS_COMMENT: //189
                    if (js.getPacketId() != null && js.getPacketId().length() > 0) {
                        if (js.getSuccess()) {
                            js.setMessage(response);
                        }
                        String makRespnse = FeedUtils.makeResponsePacket(js.getPacketId(), AppConstants.TYPE_EDIT_STATUS_COMMENT);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().put(makRespnse, js);
                    }
                    break;
                case AppConstants.TYPE_IMAGE_COMMENT_LIKES: //196
                    this.processFetchLikesForAnImageComment();
                    break;

                case AppConstants.TYPE_CIRCLE_NEWSFEED: //198
                    this.processCircleNewsFeed();
                    break;

                case AppConstants.TYPE_FRIEND_PRESENCE_INFO: //199
                    this.processFriendPresenceInfo();
                    break;

                case AppConstants.TYPE_CONFIRMATION: //200
                    break;

                case AppConstants.TYPE_UNKNWON_PROFILE_INFO: // 204
                    this.processUnknownProfileInfo();
                    break;
                case AppConstants.TYPE_RECOVERY_SUGGESTION: // 222
                    this.processRecoverySuggestion();
                    break;
                case AppConstants.TYPE_GROUP_LIST: // 225
                    this.processGroupList();
                    break;
                case AppConstants.TYPE_ADD_WORK: // 227;
                    this.processAddWork();
                    break;
                case AppConstants.TYPE_ADD_EDUCATION: // 231;
                    this.processAddEducation();
                    break;
                case AppConstants.TYPE_LIST_WORK: // 234
                    this.processWorkInfoList();
                    break;
                case AppConstants.TYPE_LIST_EDUCATION: //235
                    this.processEducationInfoList();
                    break;

                case AppConstants.TYPE_LIST_SKILL: //236
                    this.processSkillInfoList();
                    break;
                case AppConstants.TYPE_ADD_SKILL: // 237;
                    this.processAddSkill();
                    break;
                case AppConstants.TYPE_GROUP_DETAILS: //241
                    this.processGroupDetails();
                    break;
                case AppConstants.TYPE_FRIEND_MIGRATION: //247
                    this.processMigrationList();
                    break;

                case AppConstants.TYPE_UPDATE_LIKE_COMMENT: //323
                    this.processUpdateLikeComment();
                    break;

                case AppConstants.TYPE_UPDATE_UNLIKE_COMMENT: //323
                    this.processUpdateUnLikeComment();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_FRIEND: //327
                    if (GuiRingID.getInstance() != null) {
                        processAddFriendUpdate();
                    }
                    break;

                case AppConstants.TYPE_UPDATE_DELETE_FRIEND: //328
                    if (GuiRingID.getInstance() != null) {
                        processDeleteFriendUpdate();
                    }
                    break;

                case AppConstants.TYPE_UPDATE_ACCEPT_FRIEND: //329
                    if (GuiRingID.getInstance() != null) {
                        processAcceptFriendUpdate();
                    }
                    break;

//            case AppConstants.TYPE_UPDATE_CHANGE_PASSWORD: //330
//                break;
//
//            case AppConstants.TYPE_UPDATE_CALL_START: //332
//                break;
                case AppConstants.TYPE_UPDATE_CALL_END: //333
                    this.processUpdateCallEnd();
                    break;

                case AppConstants.TYPE_UPDATE_DELETE_CIRCLE: //352
                    this.processUpdateDeleteCircle();
                    break;

                case AppConstants.TYPE_UPDATE_REMOVE_CIRCLE_MEMBER: //354
                    this.processUpdateRemoveCircleMember();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_CIRCLE_MEMBER: //356
                    this.processUpdateAddCircleMember();
                    break;

                case AppConstants.TYPE_UPDATE_EDIT_CIRCLE_MEMBER: //358
                    this.processUpdateEditCircleMember();
                    break;

                case AppConstants.TYPE_UPDATE_SEND_REGISTER: //374
                    this.processUpdateSendRegister();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_STATUS: //377
                    //{"actn":377,"stId":1114,"uId":"miraz","sts":"10","tm":1400634779891,"imageType":2,"sucs":true,"pckFs":7430}
                    //"stId":1121,"uId":"miraz","sts":"pused from server","tm":1400637822326,"imageType":2,"nc":0,"nl":1,"iLike":0,"sucs":true,"fn":"Miraz","ln":"Mahmud"
                    this.processUpdateAddStatus();
                    break;

                case AppConstants.TYPE_UPDATE_EDIT_STATUS: //378
                    //{"actn":378,"nfId":5721,"sts":"asadsd Fsdfdsfsd","sucs":true,"pckFs":15024,"fn":"Shahadat","ln":"Hossain","lctn":""}
                    this.processUpdateEditStatus();
                    break;

                case AppConstants.TYPE_UPDATE_DELETE_STATUS: //379
                    // {"actn":379,"stId":666,"sucs":true,"pckFs":23446,"fn":"Shahadat","ln":"Hossain"}
                    this.processUpdateDeleteStatus();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_IMAGE_COMMENT: //380
                    //{"actn":380,"sucs":true,"tm":1409481171036,"uId":"ring8801717634317","pckFs":776,"cmn":"Random comment 26480.399736449446","fn":"à¦†à¦¶à¦°à¦¾à¦«à§à¦²","ln":"Alom 017","imgId":295}
                    this.processUpdateAddImageComment();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_STATUS_COMMENT: //381
                    //"actn":381,"sucs":true,"stId":1143,"cmnId":1119,"tm":1400717578715,"uId":"miraz","pckFs":2297,"cmn":"454"}
                    //"stId":1121,"uId":"miraz","sts":"pused from server","tm":1400637822326,"imageType":2,"nc":0,"nl":1,"iLike":0,"sucs":true,"fn":"Miraz","ln":"Mahmud"                        
                    this.processUpdateAddStatusComment();
                    break;

                case AppConstants.TYPE_UPDATE_DELETE_IMAGE_COMMENT: //382
                    //{"actn":382,"sucs":true,"cmnId":4,"pckFs":2912,"fn":"à¦†à¦¶à¦°à¦¾à¦«à§à¦²","ln":"Alom 017","imgId":295}
                    this.processUpdateDeleteImageComment();
                    break;

                case AppConstants.TYPE_UPDATE_DELETE_STATUS_COMMENT: //383
                    //{"actn":383,"stId":1174,"cmnId":1149,"pckFs":4259}
                    this.processUpdateDeleteStatusComment();
                    break;

                case AppConstants.TYPE_UPDATE_LIKE_STATUS: //384
                    //{"stId":1111,"uId":"miraz","actn":384,"pckFs":2302,"sucs":true}
                    this.processUpdateLikeStatus();
                    break;

                case AppConstants.TYPE_UPDATE_LIKE_UNLIKE_IMAGE: //385
                    //{"uId":"ring8801717634317","actn":385,"pckFs":2,"sucs":true,"fn":"???????","ln":"Alom 017","imgId":295,"lkd":1}
                    this.processUpdateLikeUnlikeImage();
                    break;

                case AppConstants.TYPE_UPDATE_UNLIKE_STATUS: //386
                    //{"stId":1056,"uId":"miraz","actn":386,"pckFs":6423}
                    this.processUpdateUnlikeStatus();
                    break;

                case AppConstants.TYPE_UPDATE_EDIT_STATUS_COMMENT: //389
                    //{"actn":389,"sucs":true,"stId":613,"cmnId":538,"tm":1404195674207,"uId":"ring8801617634317","pckFs":2486,"cmn":"Random comment 788 by ashraful","fn":"Ashraful","ln":"Alom(016)"}
                    this.processUpdateEditComment();
                    break;

                case AppConstants.TYPE_UPDATE_EDIT_IMAGE_COMMENT: //394
                    //{"actn":394,"sucs":true,"cmnId":4,"uId":"ring8801717634317","pckFs":2442,"cmn":"Random comment 180 by ashraful","fn":"à¦†à¦¶à¦°à¦¾à¦«à§à¦²","ln":"Alom 017","imgId":295}        this.processUpdateEditComment();
                    this.processUpdateEditImageComment();
                    break;

                case AppConstants.TYPE_UPDATE_LIKE_UNLIKE_IMAGE_COMMENT: //397
                    //{"uId":"ring8801717634317","actn":397,"pckFs":6,"sucs":true,"fn":"à¦†à¦¶à¦°à¦¾à¦«à§à¦²","ln":"Alom 017","id":2,"cmnId":2,"tm":1409805476000,"imgId":295,"lkd":1}
                    this.processUpdateLikeUnlikeImageComment();
                    break;

                case AppConstants.TYPE_UPDATE_DEACTIVATE_ACCOUNT: //402
                    this.processUpdateDeactivateAccount();
                    break;

                case AppConstants.TYPE_UPDATE_ADD_GROUP_CHAT_MEMBER: //440
                    this.processUpdateAddGroupChatMember();
                    break;

                case AppConstants.TYPE_UPDATE_REMOVE_GROUP_CHAT_MEMBER: //442
                    this.processUpdateRemoveGroupChatMember();
                    break;

                case AppConstants.TYPE_UPDATE_CHANGE_FRIEND_ACCESS: //444
                    this.processUpdateChangeFriendAccess();
                    break;

                case AppConstants.TYPE_UPDATE_ACCEPT_FRIEND_ACCESS: //445
                    this.processUpdateAcceptFriendAccess();
                    break;
                case AppConstants.TYPE_UPDATE_FRIEND_MIGRATION: //447
                    //this.processUpdateFriendMigration();
                    this.processAddFriendUpdate();
                    break;

                default:
                    break;
            }
        } else {
            log.error("No action field is null");
        }
    }

    private void processPresence() {
        if (js.getPacketIdFromServer() != null && MyfnfHashMaps.getInstance().getAlreadyHaveThisAuthPakId().get(js.getPacketIdFromServer()) == null) {
            MyfnfHashMaps.getInstance().getAlreadyHaveThisAuthPakId().put(js.getPacketIdFromServer(), Boolean.TRUE);
            if (js.getUserIdentity() != null && !js.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                PressenceDto prsc = HelperMethods.getPressenceDto(response);
                if (prsc.getIndexOfHeaders() != null) {
                    int[] indexes;
                    indexes = prsc.getIndexOfHeaders();
                    UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getUserIdentity());
                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(js.getUserIdentity());

                    if (basicInfo != null) {
                        basicInfo.setUt(prsc.getUt());
                        for (int indexNumber : indexes) {
                            if (indexNumber == AppConstants.CONS_FULL_NAME) {
                                basicInfo.setFullName(prsc.getFullName());

                                AllFriendList.changeFullName(js.getUserIdentity());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.buildFullName();
                                    friendProfile.refreshMyFriendInformations();
                                }

                                if (friendChatCallPanel != null) {
                                    friendChatCallPanel.setFriendProfileInfo(js.getUserIdentity());
                                    friendChatCallPanel.refreshMyFriendChatCallPanelFullName();
                                }

                            }/* else if (indexNumber == AppConstants.CONS_LAST_NAME) {
                             basicInfo.setLastName(prsc.getLastName());

                             AcceptedFriendList.changeFullName(js.getUserIdentity());
                             if (friendProfile != null) {
                             friendProfile.setFriendProfileInfo(js.getUserIdentity());
                             friendProfile.buildFullName();
                             friendProfile.refreshMyFriendInformations();
                             }

                             }*/ else if (indexNumber == AppConstants.CONS_GENDER) {
                                basicInfo.setGender(prsc.getGender());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_COUNTRY) {
                                basicInfo.setCountry(prsc.getCountry());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_MOBILE_PHONE) {
                                if (prsc.getMobilePhone() != null && prsc.getMobilePhone().trim().length() > 0) {
                                    basicInfo.setMobilePhone(prsc.getMobilePhone());
                                }

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_EMAIL) {
                                basicInfo.setEmail(prsc.getEmail());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_WHAT_IS_IN_UR_MIND) {
                                basicInfo.setWhatisInYourMind(prsc.getWhatisInYourMind());
                            } else if (indexNumber == AppConstants.CONS_CALLING_CODE) {
                                basicInfo.setMobilePhoneDialingCode(prsc.getMobilePhoneDialingCode());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_PRESENCE) {
                                basicInfo.setPresence(prsc.getPresence());
                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendPanel();
                                }

                                if (friendChatCallPanel != null) {
                                    friendChatCallPanel.setFriendProfileInfo(js.getUserIdentity());
                                    friendChatCallPanel.refreshMyFriendChatCallPanelStatus();
                                }
                            } else if (indexNumber == AppConstants.CONS_FRIENDSHIP_STATUS) {
                                basicInfo.setFriendShipStatus(prsc.getFriendShipStatus());

                                AllFriendList.changeFriendAllInformation(js.getUserIdentity());
                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendPanel();
                                }

                                if (friendChatCallPanel != null) {
                                    friendChatCallPanel.setFriendProfileInfo(js.getUserIdentity());
                                    friendChatCallPanel.refreshMyFriendChatCallPanelAll();
                                }

                            } else if (indexNumber == AppConstants.CONS_PROFILE_IMAGE) {
                                if (basicInfo.getProfileImage() != null
                                        && basicInfo.getProfileImage().trim().length() > 0) {
                                    String previous_image = ImageHelpers.getImageNameFromUrl(basicInfo.getProfileImage());
                                    File f_pre = new File(dHelp.getProfileDestinationFolder() + previous_image);
                                    if (f_pre.exists()) {
                                        f_pre.delete();
                                    }
                                }
                                basicInfo.setProfileImage(prsc.getProfileImage());
                                basicInfo.setProfileImageId(js.getProfileImageId());

                                if (prsc.getProfileImage() != null && prsc.getProfileImage().length() > 0) {
                                    new ImageDownloader(prsc.getProfileImage(), js.getUserIdentity(), ImageDownloader.FRIEND_PROFILE_IMAGE).start();
                                } else {
                                    if (friendProfile != null) {
                                        friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                        friendProfile.buildFrinedImage();
                                        //friendProfile.buildStatusImage();

                                    }

                                    if (friendChatCallPanel != null) {
                                        friendChatCallPanel.setFriendProfileInfo(js.getUserIdentity());
                                        friendChatCallPanel.refreshMyFriendChatCallPanelImage();
                                    }
                                }
                                AllFriendList.changeFriendProfileImage(js.getUserIdentity());

                            } else if (indexNumber == AppConstants.CONS_COVER_IMAGE) {
                                if (basicInfo.getCoverImage() != null
                                        && basicInfo.getCoverImage().trim().length() > 0) {
                                    String previous_image = ImageHelpers.getImageNameFromUrl(basicInfo.getCoverImage());
                                    File f_pre = new File(dHelp.getCoverDestinationFolder() + previous_image);
                                    if (f_pre.exists()) {
                                        f_pre.delete();
                                    }
                                }
                                basicInfo.setCoverImage(prsc.getCoverImage());
                                basicInfo.setCoverImageId(js.getCoverImageId());

                                if (prsc.getCoverImage() != null && prsc.getCoverImage().length() > 0) {
                                    new ImageDownloader(prsc.getCoverImage(), js.getUserIdentity(), ImageDownloader.FRIEND_COVER_IMAGE).start();
                                } else {
                                    if (friendProfile != null) {
                                        friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                        friendProfile.buildFrinedImage();
                                        // friendProfile.buildStatusImage();
                                    }
                                }

                            } else if (indexNumber == AppConstants.CONS_BIRTH_DAY) {
                                basicInfo.setBirthday(prsc.getBirthday());

                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.refreshMyFriendInformations();
                                }
                            } else if (indexNumber == AppConstants.CONS_PRIVACY) {
                                //Privacy
                                basicInfo.setPrivacy(prsc.getPrivacy());

                                if (prsc.getPrivacy() != null && prsc.getPrivacy().length > 0) {
                                    short privacy[] = prsc.getPrivacy();
                                    try {
                                        basicInfo.setEmailPrivacy(privacy[0]);
                                        basicInfo.setMobilePrivacy(privacy[1]);
                                        basicInfo.setProfileImagePrivacy(privacy[2]);
                                        basicInfo.setBirthdayPrivacy(privacy[3]);
                                        basicInfo.setCoverImagePrivacy(privacy[4]);
                                    } catch (Exception e) {
                                    }
                                }

                                AllFriendList.changeFriendAllInformation(js.getUserIdentity());

                                ChatHistoryContainer.changeAllInformation(js.getUserIdentity());
                                if (friendProfile != null) {
                                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                                    friendProfile.buildFrinedImage();
                                    friendProfile.refreshMyFriendInformations();
                                }

                            }
                        }

                        List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
                        contactList.add(basicInfo);
                        new InsertIntoContactListTable(contactList).start();
                    }
                }
            }
        }
    }

    private void processAddFriendUpdate() {
        if (js.getSuccess()) {
            UserBasicInfo json = HelperMethods.getUserBasicInfoDto(response);
            json.setIncomingNotification(Boolean.TRUE);
            FriendList.getInstance().add_single_friend_entry(json);
            if (json.getProfileImage() != null && json.getProfileImage().trim().length() > 0) {
                new ImageDownloader(json.getProfileImage(), json.getUserIdentity(), ImageDownloader.FRIEND_PROFILE_IMAGE).start();
            }
            if (json.getCoverImage() != null && json.getCoverImage().trim().length() > 0) {
                new ImageDownloader(json.getCoverImage(), json.getUserIdentity(), ImageDownloader.FRIEND_COVER_IMAGE).start();
            }
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();

            /*if (MyfnfHashMaps.getInstance().getInviteFriendsContainer() != null 
             && MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(json.getUserIdentity()) != null) {
             MyfnfHashMaps.getInstance().getInviteFriendsContainer().remove(json.getUserIdentity());
             }
             if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
             GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
             }*/
            if (MyfnfHashMaps.getInstance().getInviteFriendsContainer() != null
                    && !MyfnfHashMaps.getInstance().getInviteFriendsContainer().isEmpty()) {
                for (int key : MyfnfHashMaps.getInstance().getInviteFriendsContainer().keySet()) {
                    if (MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key).containsKey(json.getUserIdentity())) {
                        MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key).remove(json.getUserIdentity());
                        if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                            if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
                                GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().remove_content(json.getUserIdentity());
                            }
                        }
                        break;
                    }
                }
            }

            if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {

                /*if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel() != null) {
                 GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();

                 if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null) {
                 if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel() != null) {
                 GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
                 }
                 }*/
                if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                    GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().add_contents();
                }
                if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                        && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().add_contents();
                }
            }
            //AcceptedFriendList.getInstance().buildFriendList();

            /*            if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtonsPanel() != null) {
             GuiRingID.getInstance().getMainButtonsPanel().addIncomingRequestNotification();
             }*/
            if (GuiRingID.getInstance().getMainLeftContainer() != null
                    && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null) {
                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().addIncomingRequestNotification();
            }
            MyfnfHashMaps.getInstance().getMyFriendProfiles().remove(json.getUserIdentity());
            MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().remove(json.getUserIdentity());
            if (GuiRingID.getInstance() != null
                    && GuiRingID.getInstance().getMainRight() != null
                    && GuiRingID.getInstance().getMainRight().getUnknownProfile() != null
                    && GuiRingID.getInstance().getMainRight().getUnknownProfile().isDisplayable()
                    && GuiRingID.getInstance().getMainRight().getUnknownProfile().friendProfileInfo.getUserIdentity().equals(json.getUserIdentity())) {
                GuiRingID.getInstance().showFriendProfile(json.getUserIdentity());
            }

            List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
            ActivityDTO activityDTO = new ActivityDTO();
            activityDTO.setActivityType(ActivityConstants.ACTIVITY_FRIEND_REQUEST);
            activityDTO.setMessageType(ActivityConstants.MSG_INCOMING_FRIEND_REQUEST);
            activityDTO.setFriendIdentity(json.getUserIdentity());
            activityDTO.setActivityBy(json.getUserIdentity());
            activityDTO.setPacketID(js.getPacketId());
            activityDTOs.add(activityDTO);
            new InsertIntoRingActivityTable(activityDTOs).start();
        }
    }

    private void processDeleteFriendUpdate() {
        if (js.getSuccess()) {
            UserBasicInfo frienBasicInfo = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
            FriendList.getInstance().getFriend_hash_map().remove(js.getUserIdentity());
            MyfnfHashMaps.getInstance().getSingleFriendPanel().remove(js.getUserIdentity());
            //MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().remove(js.getUserIdentity());
            (new DeleteFromContactListTable(js.getUserIdentity())).start();
            //AcceptedFriendList.getInstance().buildFriendList();
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();

            if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                    GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(js.getUserIdentity());
                }
                if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                        && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(js.getUserIdentity());
                }
            }
            /*            if (MyFriendProfile.getSeleected_user_identiy() != null
             && MyFriendProfile.getSeleected_user_identiy().equals(js.getUserIdentity())
             && frienBasicInfo != null) {
             GuiRingID.getInstance().showUnknownProfile(frienBasicInfo);
             }*/
            MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getUserIdentity());
            MyFriendChatCallPanel myFriendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(js.getUserIdentity());
            if ((myFriendProfile != null && myFriendProfile.isDisplayable() && myFriendProfile.isVisible())
                    || (myFriendChatCallPanel != null && myFriendChatCallPanel.isDisplayable() && myFriendChatCallPanel.isVisible())) {
                GuiRingID.getInstance().showUnknownProfile(frienBasicInfo);
            }
            MyfnfHashMaps.getInstance().getMyFriendProfiles().remove(js.getUserIdentity());
            MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().remove(js.getUserIdentity());
        }
    }

    private void processAcceptFriendUpdate() {
        try {
            if (js.getSuccess()) {
                UserBasicInfo json = HelperMethods.getUserBasicInfoDto(response);
//                json.setIncomingNotification(false);
                FriendList.getInstance().add_single_friend_entry(json);
                if (json.getProfileImage() != null && json.getProfileImage().trim().length() > 0) {
                    new ImageDownloader(json.getProfileImage(), json.getUserIdentity(), ImageDownloader.FRIEND_PROFILE_IMAGE).start();
                }
                if (json.getCoverImage() != null && json.getCoverImage().trim().length() > 0) {
                    new ImageDownloader(json.getCoverImage(), json.getUserIdentity(), ImageDownloader.FRIEND_COVER_IMAGE).start();
                }
                AllFriendList.changeFriendAllInformation(json.getUserIdentity());
                // AcceptedFriendList.getInstance().buildFriendList();
                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(json.getUserIdentity());
                    }
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                            && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(json.getUserIdentity());
                    }
                }
                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getUserIdentity());
                if (friendProfile != null) {
                    friendProfile.setFriendProfileInfo(json.getUserIdentity());
                    friendProfile.myFriendAboutPanel = null;
                    friendProfile.refreshMyFriendPanel();
                }

                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(js.getUserIdentity());
                if (friendChatCallPanel != null) {
                    friendChatCallPanel.setFriendProfileInfo(json.getUserIdentity());
                    friendChatCallPanel.refreshMyFriendChatCallPanelAll();
                }

                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                ActivityDTO activityDTO = new ActivityDTO();
                activityDTO.setActivityType(ActivityConstants.ACTIVITY_FRIEND_REQUEST);
                activityDTO.setMessageType(ActivityConstants.MSG_ACCEPTED_FRIEND_REQUEST);
                activityDTO.setFriendIdentity(json.getUserIdentity());
                activityDTO.setActivityBy(json.getUserIdentity());
                activityDTO.setPacketID(js.getPacketId());
                activityDTOs.add(activityDTO);
                Thread.sleep(300);
                new InsertIntoRingActivityTable(activityDTOs).start();
            }
        } catch (Exception e) {
        }
    }

    private void processAddFriend() {
        if (js.getSuccess()) {
            UserBasicInfo userBasicInfoAdd = HelperMethods.getUserBasicInfoDto(response);
//            userBasicInfoAdd.setIncomingNotification(false);
            FriendList.getInstance().add_single_friend_entry(userBasicInfoAdd);
            /*if (MyfnfHashMaps.getInstance().getInviteFriendsContainer() != null && MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(js.getUserIdentity()) != null) {
             MyfnfHashMaps.getInstance().getInviteFriendsContainer().remove(js.getUserIdentity());
             }
             GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
             if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
             if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
             GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().add_contents();
             }
             if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
             GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
             }
             if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel() != null) {
             GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
             }
             }*/

            List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
            ActivityDTO activityDTO = new ActivityDTO();
            activityDTO.setActivityType(ActivityConstants.ACTIVITY_FRIEND_REQUEST);
            activityDTO.setMessageType(ActivityConstants.MSG_PENDING_FRIEND_REQUEST);
            activityDTO.setFriendIdentity(js.getUserIdentity());
            activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
            activityDTO.setPacketID(js.getPacketId());
            activityDTOs.add(activityDTO);
            new InsertIntoRingActivityTable(activityDTOs).start();
        }
    }

    private void processAcceptFriend() {
        try {
            if (js.getSuccess()) {
                UserBasicInfo addUserBasicInfo = HelperMethods.getUserBasicInfoDto(response);
//                addUserBasicInfo.setIncomingNotification(false);
                FriendList.getInstance().add_single_friend_entry(addUserBasicInfo);
                if (addUserBasicInfo.getProfileImage() != null && addUserBasicInfo.getProfileImage().trim().length() > 0) {
                    new ImageDownloader(addUserBasicInfo.getProfileImage(), js.getUserIdentity(), ImageDownloader.FRIEND_PROFILE_IMAGE).start();
                }
                if (addUserBasicInfo.getCoverImage() != null && addUserBasicInfo.getCoverImage().trim().length() > 0) {
                    new ImageDownloader(addUserBasicInfo.getCoverImage(), js.getUserIdentity(), ImageDownloader.FRIEND_COVER_IMAGE).start();
                }

                AllFriendList.changeFriendAllInformation(js.getUserIdentity());
                //AcceptedFriendList.getInstance().buildFriendList();
                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(addUserBasicInfo.getUserIdentity());
                    }
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                            && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(addUserBasicInfo.getUserIdentity());
                    }
                }
                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getUserIdentity());
                if (friendProfile != null) {
                    friendProfile.setFriendProfileInfo(js.getUserIdentity());
                    friendProfile.myFriendAboutPanel = null;
                    friendProfile.refreshMyFriendPanel();
                }

                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(js.getUserIdentity());
                if (friendChatCallPanel != null) {
                    friendChatCallPanel.setFriendProfileInfo(js.getUserIdentity());
                    friendChatCallPanel.refreshMyFriendChatCallPanelAll();
                }

//                GuiRingID.getInstance().getMainButtonsPanel().addIncomingRequestNotification();
                List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                ActivityDTO activityDTO = new ActivityDTO();
                activityDTO.setActivityType(ActivityConstants.ACTIVITY_FRIEND_REQUEST);
                activityDTO.setMessageType(ActivityConstants.MSG_ACCEPTED_FRIEND_REQUEST);
                activityDTO.setFriendIdentity(js.getUserIdentity());
                activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                activityDTO.setPacketID(js.getPacketId());
                activityDTOs.add(activityDTO);
                Thread.sleep(300);
                new InsertIntoRingActivityTable(activityDTOs).start();
            } else {
                HelperMethods.showPlaneDialogMessage(js.getMessage());
                // JOptionPane.showMessageDialog(null, js.getMessage(), "Confirm friend", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        }
    }

    private void processUpdateSendRegister() {
        if (js.getSuccess()) {
            try {
                //  System.out.println("response==>" + response);
                CallerDto callingTimeDto = HelperMethods.getCallerDto(response);
                if (callingTimeDto.getCallID() != null) {

                    ConfigFile.USER_ID = MyFnFSettings.LOGIN_USER_ID;
                    if (ConfigFile.CALL_ID == null || (ConfigFile.FRIEND_ID != null && ConfigFile.FRIEND_ID.equals(callingTimeDto.getFriendIdentity()))) {
                        if (MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() || MyfnfHashMaps.getInstance().getCallInitiators().get(callingTimeDto.getCallID()) == null) {
                            CallUtils.addCallinitiators(callingTimeDto);
                        }
                        SignalGenerator.sendRegButtonAction(callingTimeDto.getFriendIdentity(), callingTimeDto.getCallID(), callingTimeDto.getSwitchIp(), callingTimeDto.getSwitchPort());
                        Thread.sleep(100);
                        for (int i = 0; i < 120; i++) {
                            if (MyfnfHashMaps.getInstance().getCallInitiators().get(callingTimeDto.getCallID()) == null) {
                                break;
                            }
                            if (MyfnfHashMaps.getInstance().getCallInitiators().get(callingTimeDto.getCallID()).getVoiceBindingPort() > 0) {
                                break;
                            }
                            if (i % 10 == 0) {
                                SignalGenerator.sendRegButtonAction(callingTimeDto.getFriendIdentity(), callingTimeDto.getCallID(), callingTimeDto.getSwitchIp(), callingTimeDto.getSwitchPort());
                            }
                            Thread.sleep(200);
                        }

                    } else {
                        SignalGenerator.sendCallIn(callingTimeDto.getFriendIdentity(), callingTimeDto.getCallID(), callingTimeDto.getSwitchIp(), callingTimeDto.getSwitchPort());
                    }
                } else {
                }

            } catch (Exception e) {
                log.error("processUpdateSendRegister==>" + e.getMessage());
            }

        } else {
            HelperMethods.showPlaneDialogMessage(js.getMessage());
        }

    }

    private void processUpdateCallEnd() {
        try {
            //        System.out.println("processUpdateCallEnd");
            //      GuiRingID.getInstance().getUserAgent().unregister();
//            if (CallingUA.getStaticObject() != null) {
//                CallingUA.getStaticObject().forceHangUp();
//            }
        } catch (Exception e) {
        }
    }

    private void processCallLog() {
        if (js.getSuccess()) {
            CallLogList dd = HelperMethods.map_call_log(response);
            if (dd != null) {
                List<CallLog> callLogList = new ArrayList<>();
                for (CallLog entity : dd.getCallLog()) {
                    entity.setCallID("CALL_ID_SERVER_" + entity.getCallingTime().toString());
                    callLogList.add(entity);
                }

                new InsertIntoRingCallLogTable(callLogList, false).start();
            }
        }
    }

    private void processFriendPresenceInfo() {
        try {
            JsonFields dto = HelperMethods.getJsonFields(response);
            if (dto != null) {
                UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(dto.getFriendIdentity());
                if (basicInfo != null) {
                    basicInfo.setDevice(dto.getDevice());
                    basicInfo.setPresence(dto.getPresence());
                    basicInfo.setLastOnlineTime(dto.getLastOnlineTime());

                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(dto.getFriendIdentity());
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.setFriendProfileInfo(basicInfo);
                        friendChatCallPanel.refreshMyFriendChatCallPanelStatus();
                    }

                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(dto.getFriendIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(basicInfo);
                        friendProfile.buildStatusImage();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void processMultipleSessionWarning() {
        if (js.getSessionId().equals(MyFnFSettings.LOGIN_SESSIONID)) {
            MyFnFSettings.LOGIN_SESSIONID = null;
            MyFnFSettings.LOGIN_USER_ID = null;
            MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = null;

            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().clearFriendList();
            //AcceptedFriendList.getInstance().clearFriendList();
            if (GuiRingID.getInstance() != null) {
                GuiRingID.getInstance().signoutActions(false);
            }
        }
    }

    private void processInvalidLoginSession() {
        MyFnFSettings.LOGIN_SESSIONID = null;
        MyFnFSettings.LOGIN_USER_ID = null;
        MyFnFSettings.VALUE_LOGIN_USER_PASSWORD = null;

        //AcceptedFriendList.getInstance().clearFriendList();
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().clearFriendList();
            GuiRingID.getInstance().signoutActions(false);
        }
        HelperMethods.showPlaneDialogMessage("Invalid session! " + js.getMessage());
        //JOptionPane.showMessageDialog(null, "Invalid session! " + js.getMessage(), StaticFields.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }

    private void processSignOut() {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().clearFriendList();
            //AcceptedFriendList.getInstance().clearFriendList();
        }
    }

    private void processNewCircle() {
        if (js.getSuccess()) {
            /*
             {"sucs":true,"actn":51,"pckFs":35222,"grpId":206,"gNm":"asd","sAd":"ring8801911722319","groupMembers":[{"uId":"ring8801728119927","admin":false,"psnc":2,"fn":"Faiz","ln":"Ahmed","gr":"Male","mbl":"+8801728119927","grpId":206,"usrTId":9,"tID":1159},{"uId":"ring8801911722319","admin":true,"psnc":2,"fn":"Wasif","ln":"Islam","gr":"Male","mbl":"+8801911722319","grpId":206,"usrTId":65,"tID":1160}]}
             */
            SingleCircleDto singleGroupDto = HelperMethods.getSingleCircleDto(response);
            List<SingleCircleDto> groupList = new ArrayList<SingleCircleDto>();
            groupList.add(singleGroupDto);
            (new InsertIntoCircleListTable(groupList)).start();

            MyfnfHashMaps.getInstance().getCircleLists().put(js.getCircleId(), singleGroupDto);
            CircleMembersMap members = HelperMethods.mapCircleMembersMap(response);
            for (SingleMemberInCircleDto dto : members.getCircleMembers()) {
                if (MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()) == null) {
                    Map<String, SingleMemberInCircleDto> singleGroup = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                    singleGroup.put(dto.getUserIdentity(), dto);
                    MyfnfHashMaps.getInstance().getCircleMembers().put(js.getCircleId(), singleGroup);
                } else {
                    MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()).put(dto.getUserIdentity(), dto);
                }
            }

            (new InsertIntoCircleMemberTable(members.getCircleMembers())).start();
            if (getCircleLeft() != null) {
                getCircleLeft().buildCircleCount();
                /**
                 * *if (singleGroupDto.getSuperAdmin() != null &&
                 * singleGroupDto.getSuperAdmin().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID))
                 * {
                 * GuiRingID.getInstance().circleLeft.onCircleButtonClick(CircleLeftPanel.MY_CIRCLE_GROUP);
                 * } else {
                 * GuiRingID.getInstance().circleLeft.onCircleButtonClick(CircleLeftPanel.CIRCLE_OF_ME_GROUP);
                 * }**
                 */
            }
        }
    }

    private void processDeleteCircle() {
        if (js.getSuccess() && js.getCircleId() != null) {
            MyfnfHashMaps.getInstance().getCircleLists().remove(js.getCircleId());
            MyfnfHashMaps.getInstance().getCircleMembers().remove(js.getCircleId());

            (new DeleteFromCircleListTable(js.getCircleId())).start();

            if (getCircleLeft() != null) {
                getCircleLeft().buildCircleCount();
                getCircleLeft().buildCircleList();
                ///GuiRingID.getInstance().circleLeft.onCircleButtonClick(CircleViewRight.HIDE_ALL_GROUP);
            }

            CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
            if (circleDetails != null && circleDetails.isDisplayable()) {
                ///GuiRingID.getInstance().showHome();
                if (getCircleLeft() != null) {
                    GuiRingID.getInstance().loadIntoMainRightContent(getCircleLeft());
                }
            }
            MyfnfHashMaps.getInstance().getCircleProfiles().remove(js.getCircleId());
        }
    }

    private void processUpdateDeleteCircle() {
        if (js.getSuccess()) {
            try {
                MyfnfHashMaps.getInstance().getCircleLists().remove(js.getCircleId());

                (new DeleteFromCircleListTable(js.getCircleId())).start();

                if (getCircleLeft() != null) {
                    getCircleLeft().buildCircleCount();
                    getCircleLeft().buildCircleList();
                    ///GuiRingID.getInstance().circleLeft.onCircleButtonClick(CircleViewRight.HIDE_ALL_GROUP);
                }

                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                if (circleDetails != null && circleDetails.isDisplayable()) {
                    ///GuiRingID.getInstance().showHome();
                    if (getCircleLeft() != null) {
                        GuiRingID.getInstance().loadIntoMainRightContent(getCircleLeft());
                    }
                }

                MyfnfHashMaps.getInstance().getCircleProfiles().remove(js.getCircleId());
            } catch (Exception e) {
            }
        }
    }

    private void processRemoveCircleMember() {
        if (js.getSuccess() && js.getUserIdentity() != null && js.getUserIdentity().trim().length() > 0) {
            if (MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()) != null) {
                MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()).remove(js.getUserIdentity());
            }
            new DeleteFromCircleMemberListTable(js.getCircleId(), js.getUserIdentity()).start();

            CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
            if (circleDetails != null) {
                circleDetails.getCircleMembersListPanel().buildMembersList();
            }
        }
    }

    private void processUpdateRemoveCircleMember() {
        if (js.getSuccess()) {
            try {
                Long gurop_id = js.getCircleId();
                CircleDto members = HelperMethods.map_into_CircleDto(response);

                for (String member : members.getRemovedMembers()) {
                    if (member.equalsIgnoreCase(MyFnFSettings.userProfile.getUserIdentity())) {
                        MyfnfHashMaps.getInstance().getCircleLists().remove(js.getCircleId());

                        (new DeleteFromCircleListTable(js.getCircleId())).start();

                        if (getCircleLeft() != null) {
                            getCircleLeft().buildCircleCount();
                            ///GuiRingID.getInstance().circleLeft.onCircleButtonClick(CircleViewRight.HIDE_ALL_GROUP);
                            getCircleLeft().buildCircleList();
                        }

                        CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                        if (circleDetails != null && circleDetails.isDisplayable()) {
                            ///GuiRingID.getInstance().showHome();
                            if (getCircleLeft() != null) {
                                GuiRingID.getInstance().loadIntoMainRightContent(getCircleLeft());
                            }
                        }

                        MyfnfHashMaps.getInstance().getCircleProfiles().remove(js.getCircleId());
                        break;
                    } else {
                        if (MyfnfHashMaps.getInstance().getCircleMembers().get(gurop_id) != null) {
                            MyfnfHashMaps.getInstance().getCircleMembers().get(gurop_id).remove(member);
                        }
                        (new DeleteFromCircleMemberListTable(gurop_id, member)).start();
                    }
                }

                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                if (circleDetails != null) {
                    circleDetails.getCircleMembersListPanel().buildMembersList();
                }
            } catch (Exception e) {
            }
        }
    }

    private void processUpdateAddCircleMember() {
        if (js.getSuccess()) {
            try {
                if (MyfnfHashMaps.getInstance().getCircleLists().get(js.getCircleId()) == null) {
                    SingleCircleDto singleGroupDto = HelperMethods.getSingleCircleDto(response);
                    List<SingleCircleDto> groupList = new ArrayList<SingleCircleDto>();
                    groupList.add(singleGroupDto);
                    (new InsertIntoCircleListTable(groupList)).start();

                    MyfnfHashMaps.getInstance().getCircleLists().put(js.getCircleId(), singleGroupDto);
                }

                CircleMembersMap members = HelperMethods.mapCircleMembersMap(response);

                for (SingleMemberInCircleDto dto : members.getCircleMembers()) {
                    if (MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()) == null) {
                        Map<String, SingleMemberInCircleDto> singleGroup = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                        singleGroup.put(dto.getUserIdentity(), dto);
                        MyfnfHashMaps.getInstance().getCircleMembers().put(js.getCircleId(), singleGroup);
                    } else {
                        MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()).put(dto.getUserIdentity(), dto);
                    }

                }

                (new InsertIntoCircleMemberTable(members.getCircleMembers())).start();

                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                if (circleDetails != null) {
                    circleDetails.getCircleMembersListPanel().buildMembersList();
                }
            } catch (Exception e) {
            }
        }
    }

    private void processUpdateEditCircleMember() {
        if (js.getSuccess()) {
            try {
                CircleMembersMap members = HelperMethods.mapCircleMembersMap(response);

                for (SingleMemberInCircleDto dto : members.getCircleMembers()) {
                    if (MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()) != null) {
                        SingleMemberInCircleDto entity = MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()).get(dto.getUserIdentity());
                        if (entity != null) {
                            entity.setAdmin(dto.isAdmin());
                            MyfnfHashMaps.getInstance().getCircleMembers().get(js.getCircleId()).put(dto.getUserIdentity(), entity);

                        }
                    }
                }

                (new InsertIntoCircleMemberTable(members.getCircleMembers())).start();

                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                if (circleDetails != null) {
                    circleDetails.getCircleMembersListPanel().buildMembersList();
                }
            } catch (Exception e) {
            }
        }
    }

    private void processFriendsPresenceList() {
        try {
            if (js.getUserIdentity() != null && js.getUserIdentity().length() > 0) {

                String[] parts = js.getUserIdentity().split(",");
                for (String part : parts) {
                    String[] deviceId = part.split(";");
                    //{"actn":98,"uId":"
                    //5;ring8801670866268;2;null,
                    //2;ring8801736861305;2;null,
                    //1;ring8801918845404;2;null,
                    //4;ring8801911013776;2;null,
                    //3;ring8801751268545;2;1,
                    //3;ring8801714104958;2;1,
                    //3;ring8801919639815;3;95,
                    //3;ring8801919639815;3;96,
                    //2;ring8801756858005;2;140",
                    //"pckFs":31187}
                    if (FriendList.getInstance().getFriend_hash_map().get(deviceId[1]) != null) {
                        FriendList.getInstance().getFriend_hash_map().get(deviceId[1]).setDevice(Integer.parseInt(deviceId[0]));
                        FriendList.getInstance().getFriend_hash_map().get(deviceId[1]).setPresence(Integer.parseInt(deviceId[2]));
                    }
                    Long groupId = null;
                    try {
                        groupId = Long.parseLong(deviceId[3]);
                    } catch (Exception ex) {
                        groupId = null;
                    }
                    if (groupId != null) {
                        Map<String, SingleMemberInCircleDto> dtos = MyfnfHashMaps.getInstance().getCircleMembers().get(groupId);
                        if (dtos != null) {
                            SingleMemberInCircleDto singleMemberInGroupDto = dtos.get(deviceId[1]);
                            if (singleMemberInGroupDto != null) {
                                singleMemberInGroupDto.setPresence(Integer.parseInt(deviceId[2]));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in processFriendsPresenceList ==>" + e.getMessage());
        }

    }

    private void processContactSearch() {
        if (js.getSuccess()) {
            SearchFreindlListMapping searchFreindlListMapping = HelperMethods.tamp_friendlist_mapping(response);

            if (searchFreindlListMapping.getSearchedcontaclist() != null) {
                for (final UserBasicInfo singleProfile : searchFreindlListMapping.getSearchedcontaclist()) {
                    if (MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(searchFreindlListMapping.getSearchCategory()) != null) {
                        MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(searchFreindlListMapping.getSearchCategory()).put(singleProfile.getUserIdentity(), singleProfile);
                    } else {
                        ConcurrentHashMap<String, UserBasicInfo> userMap = new ConcurrentHashMap<String, UserBasicInfo>();
                        userMap.put(singleProfile.getUserIdentity(), singleProfile);
                        MyfnfHashMaps.getInstance().getInviteFriendsContainer().put(searchFreindlListMapping.getSearchCategory(), userMap);
                    }

                }
            }
        }

        /* if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel() != null) {

         GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().addSearchResultInContainer();
         }*/
        if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
            GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
        }
    }

    private void processGroupList() {
        try {
            if (js.getSuccess()) {
                GroupListMapping mapper = HelperMethods.grouplist_mapping(response);
                if (mapper.getGroupList() != null) {
                    new InsertIntoGroupListTable(mapper.getGroupList()).start();
                    for (GroupInfoDTO entity : mapper.getGroupList()) {
                        new GetGroupDetails(entity.getGroupId()).start();
                    }
                }
            }

        } catch (Exception e) {
            //   e.printStackTrace();
            log.error("processGroupList ex-->" + response);
        }
    }

    private void processGroupDetails() {
        try {
            if (js.getSuccess()) {
                JsonFields jsonFields = HelperMethods.getJsonFields(response);

                List<GroupInfoDTO> tempGroups = new ArrayList<GroupInfoDTO>();
                List<GroupMemberDTO> tempGroupMembers = new ArrayList<GroupMemberDTO>();

                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(jsonFields.getGroupId());
                if (groupInfo != null) {
                    if (groupInfo.getMemberMap() == null) {
                        groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                    }
                    groupInfo.setGroupName(jsonFields.getGroupName());
                    groupInfo.setGroupUt(jsonFields.getUt());
                    groupInfo.setOwnerUserTableId(jsonFields.getGroupOwnerUserTableId());

                    for (Entry<String, GroupMemberDTO> entry : groupInfo.getMemberMap().entrySet()) {
                        if (jsonFields.getFriendMap() == null || !jsonFields.getFriendMap().containsKey(entry.getKey())) {
                            entry.getValue().setIntegerStatus(StatusConstants.STATUS_DELETED);
                            tempGroupMembers.add(entry.getValue());
                        }
                    }

                    for (GroupMemberDTO groupMember : tempGroupMembers) {
                        groupInfo.getMemberMap().remove(groupMember.getUserIdentity());
                    }

                    if (jsonFields.getFriendMap() != null && jsonFields.getFriendMap().size() > 0) {
                        for (Entry<String, String> entry : jsonFields.getFriendMap().entrySet()) {
                            GroupMemberDTO memberDTO = groupInfo.getMemberMap().get(entry.getKey());
                            if (memberDTO != null) {
                                memberDTO.setFullName(entry.getValue());
                            } else {
                                memberDTO = new GroupMemberDTO();
                                memberDTO.setGroupId(groupInfo.getGroupId());
                                memberDTO.setUserIdentity(entry.getKey());
                                memberDTO.setFullName(entry.getValue());
                                groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                            }
                            tempGroupMembers.add(memberDTO);
                        }
                    }
                } else {
                    groupInfo = new GroupInfoDTO();
                    groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                    groupInfo.setGroupId(jsonFields.getGroupId());
                    groupInfo.setGroupUt(jsonFields.getUt());
                    groupInfo.setGroupName(jsonFields.getGroupName());
                    groupInfo.setOwnerUserTableId(jsonFields.getGroupOwnerUserTableId());
                    MyfnfHashMaps.getInstance().getGroup_hash_map().put(jsonFields.getGroupId(), groupInfo);

                    if (jsonFields.getFriendMap() != null && jsonFields.getFriendMap().size() > 0) {
                        if (!Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())
                                && !jsonFields.getFriendMap().containsKey(MyFnFSettings.LOGIN_USER_ID)) {
                            MyfnfHashMaps.getInstance().getGroup_hash_map().remove(groupInfo.getGroupId());
                            return;
                        }

                        for (Entry<String, String> entry : jsonFields.getFriendMap().entrySet()) {
                            GroupMemberDTO memberDTO = new GroupMemberDTO();
                            memberDTO.setGroupId(groupInfo.getGroupId());
                            memberDTO.setUserIdentity(entry.getKey());
                            memberDTO.setFullName(entry.getValue());
                            groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                            tempGroupMembers.add(memberDTO);
                        }
                    }
                }
                tempGroups.add(groupInfo);

                new InsertIntoGroupListTable(tempGroups).start();
                new InsertIntoGroupMemberTable(tempGroupMembers).start();

                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(js.getGroupId());
                if (groupPanel != null) {
                    groupPanel.buildMembersPanel();
                    groupPanel.buildPopUpMenu();
                    groupPanel.buildChatWritingArea();
                }

                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainRight() != null
                        && GuiRingID.getInstance().getMainRight().getGroupMainView() != null
                        && GuiRingID.getInstance().getMainRight().getGroupMainView().isDisplayable()) {
                    GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("processGroupDetails ex-->" + response);
        }
    }

    private void processFriendDetails() {
        if (js.getSuccess()) {
            UserDetailsInfoDTO json = HelperMethods.getUserDetailsInfo(response);
            UserBasicInfo friendDetailsinMap = FriendList.getInstance().getFriend_hash_map().get(json.getUserDetails().getUserIdentity());
            if (friendDetailsinMap != null) {
                if (json.getUserDetails().getAboutMe() != null) {
                    friendDetailsinMap.setAboutMe(json.getUserDetails().getAboutMe());

                }
                if (json.getUserDetails().getMarriageDay() != null) {
                    if (json.getUserDetails().getMarriageDay() > 0) {
                        Date d = new Date(json.getUserDetails().getMarriageDay());
                        String strDate = new SimpleDateFormat("MMMM d, yyyy").format(d);
                        friendDetailsinMap.setMarriageDay(strDate);
                    }
                }
                if (json.getUserDetails().getHomeCity() != null) {
                    friendDetailsinMap.setHomeCity(json.getUserDetails().getHomeCity());
                }
                if (json.getUserDetails().getCurrentCity() != null) {
                    friendDetailsinMap.setCurrentCity(json.getUserDetails().getCurrentCity());
                }
                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(json.getUserDetails().getUserIdentity());
                if (friendProfile != null) {
                    friendProfile.myFriendAboutPanel.loadFriendInformation();
                }

            } //else {
            //   FriendList.getInstance().getFriend_hash_map().put(json.getUserIdentity(), json);
            // }
        }
    }
    //public static int k = 0;

    private void processMigrationList() {
        try {
            if (js.getSuccess()) {
                MigrationFriendlistMapping mapper = HelperMethods.migrationlist_mapping(response);
                if (mapper.getMigrationFriendList() != null) {
                    for (final UserBasicInfo entity : mapper.getMigrationFriendList()) {
                        FriendList.getInstance().getFriend_hash_map().put(entity.getUserIdentity(), entity);
                        FriendList.getInstance().getTempContactList().put(entity.getUserIdentity(), entity);
                        /*if (GuiRingID.getInstance() != null
                         && GuiRingID.getInstance().getMainLeftContainer() != null
                         && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null
                         && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel() != null) {
                         GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().addSingleFriendFromServer(entity);
                         }*/
                    }
                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                            && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().add_contents();
                    }
                    List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
                    for (String userId : FriendList.getInstance().getTempContactList().keySet()) {
                        UserBasicInfo entity = FriendList.getInstance().getTempContactList().get(userId);
                        contactList.add(entity);
                    }
                    (new InsertIntoContactListTable(contactList)).start();
                    FriendList.getInstance().getTempContactList().clear();
                }
            }

        } catch (Exception e) {
            log.error("processMigrationList ex-->" + e.getMessage());
            //   e.printStackTrace();
        }
    }

    private void processContactList() {
        try {
            if (js.getSuccess()) {
                contactListSeqCount++;
                FriendlistMapping mapper = HelperMethods.friendlist_mapping(response);
                if (mapper.getContactList() != null) {
                    for (final UserBasicInfo entity : mapper.getContactList()) {
                        //  System.out.println(k + "UB>> " + entity.getUserIdentity());
                        //k++;
                        //   UserBasicInfo prevBasicInfo = FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity());
                        FriendList.getInstance().getTempContactList().put(entity.getUserIdentity(), entity);
                        if (entity.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                            FriendList.getInstance().getFriend_hash_map().remove(entity.getUserIdentity());
                        } else {
                            // entity.setIncomingNotification(prevBasicInfo != null ? prevBasicInfo.getIncomingNotification() : null);
                            boolean isIncomingRequest = (entity.getFriendShipStatus() != null && entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING)
                                    || (entity.getNewContactType() != null && entity.getNewContactType() > 0 && !entity.getIsChangeRequester()) ? Boolean.TRUE : Boolean.FALSE;
                            entity.setIncomingNotification(isIncomingRequest);

                            FriendList.getInstance().getFriend_hash_map().put(entity.getUserIdentity(), entity);
                            if (GuiRingID.getInstance() != null
                                    && GuiRingID.getInstance().getMainLeftContainer() != null
                                    && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null
                                    && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel() != null) {
                                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().addSingleFriendFromServer(entity);
                            }
                        }
                    }
                }
                int totalSeq = 0;
                if (js.getSeq() != null && js.getSeq().contains("/")) {
                    totalSeq = Integer.valueOf(js.getSeq().split("/")[1]);
                }

                log.warn("\nFRIEND_LIST_LOADED == " + MyFnFSettings.FRIEND_LIST_LOADED + ", Parcent == " + ((contactListSeqCount * 100) / totalSeq) + "\n");
                if (!MyFnFSettings.FRIEND_LIST_LOADED && ((contactListSeqCount * 100) / totalSeq) >= 60) {
                    HelperMethods.setFriendListLoaded();
                }

                if (contactListSeqCount >= totalSeq) {
                    if (!MyFnFSettings.FRIEND_LIST_LOADED && ((contactListSeqCount * 100) / totalSeq) >= 60) {
                        HelperMethods.setFriendListLoaded();
                    }

                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                    List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
                    for (String userId : FriendList.getInstance().getTempContactList().keySet()) {
                        UserBasicInfo entity = FriendList.getInstance().getTempContactList().get(userId);
                        contactList.add(entity);
                    }
                    (new InsertIntoContactListTable(contactList)).start();
                    FriendList.getInstance().getTempContactList().clear();

                    if (GuiRingID.getInstance().getMainLeftContainer() != null
                            && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null) {
                        GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().initIncomingRequestNotification();
                    }
                }
            } else {
                //System.err.println("\n\n\n\n\n\nContact List Packet Loaded :: No Contact List Packet Found.\n\n\n\n\n\n");
                HelperMethods.setFriendListLoaded();
                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();

                if (GuiRingID.getInstance().getMainLeftContainer() != null
                        && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().initIncomingRequestNotification();
                }
                if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().add_contents();
                    }
                    if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                            && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                        GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().add_contents();
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    private void processCircleList() {
        if (js.getSuccess()) {
            CircleListMap groupListMap = HelperMethods.getCircleListMap(response);
            if (groupListMap != null && groupListMap.getCircleList().size() > 0) {
                for (SingleCircleDto dto : groupListMap.getCircleList()) {
                    MyfnfHashMaps.getInstance().getTempCircleList().put(dto.getCircleId(), dto);

                    if (dto.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                        MyfnfHashMaps.getInstance().getCircleLists().remove(dto.getCircleId());
                        if (MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()) != null) {
                            MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()).clear();
                        }
                    } else {
                        MyfnfHashMaps.getInstance().getCircleLists().put(dto.getCircleId(), dto);
                    }

                }

                if (js.getTr() != null && MyfnfHashMaps.getInstance().getTempCircleList().size() == js.getTr()) {

                    List<SingleCircleDto> groupList = new ArrayList<SingleCircleDto>();
                    for (Entry entry : MyfnfHashMaps.getInstance().getTempCircleList().entrySet()) {
                        SingleCircleDto dto = (SingleCircleDto) entry.getValue();
                        groupList.add(dto);
                    }

                    (new InsertIntoCircleListTable(groupList)).start();
                    MyfnfHashMaps.getInstance().getTempCircleList().clear();
                }
            }
        }
    }

    private void processCircleMembersList() {
        try {
            CircleMembersMap groupMembers = HelperMethods.mapCircleMembersMap(response);
            if (groupMembers != null && groupMembers.getCircleMembers() != null && js.getSeq() != null) {

                MyfnfHashMaps.getInstance().getTempCircleMembers().put(js.getSeq(), groupMembers);

                for (SingleMemberInCircleDto dto : groupMembers.getCircleMembers()) {

                    if (dto.getIntegerStatus() == StatusConstants.STATUS_DELETED) {
                        if (MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()) != null) {
                            MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()).remove(dto.getUserIdentity());
                        }
                    } else {
                        if (MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()) == null) {
                            Map<String, SingleMemberInCircleDto> singleGroup = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                            singleGroup.put(dto.getUserIdentity(), dto);
                            MyfnfHashMaps.getInstance().getCircleMembers().put(dto.getCircleId(), singleGroup);
                        } else {
                            MyfnfHashMaps.getInstance().getCircleMembers().get(dto.getCircleId()).put(dto.getUserIdentity(), dto);
                        }
                    }
                }

                int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);
                if (!MyfnfHashMaps.getInstance().getTempCircleMembers().isEmpty() && MyfnfHashMaps.getInstance().getTempCircleMembers().size() == seqTotal) {
                    List<SingleMemberInCircleDto> memberList = new ArrayList<SingleMemberInCircleDto>();

                    for (Entry entry : MyfnfHashMaps.getInstance().getTempCircleMembers().entrySet()) {
                        CircleMembersMap dto = (CircleMembersMap) entry.getValue();
                        for (SingleMemberInCircleDto entity : dto.getCircleMembers()) {
                            memberList.add(entity);
                        }
                    }

                    (new InsertIntoCircleMemberTable(memberList)).start();
                    MyfnfHashMaps.getInstance().getTempCircleMembers().clear();
                }
            }
        } catch (Exception ex) {
        }
    }

    private void processPeopleYouMayKnow() {
        try {
            peopleyouMayKnowcount++;
            if (js.getSuccess()) {
                FriendlistMapping tamp_list = HelperMethods.friendlist_mapping(response);
                if (!tamp_list.getContactList().isEmpty()) {
                    for (final UserBasicInfo singleProfile : tamp_list.getContactList()) {
                        FriendList.getInstance().getPeopleYouMayKnow().insertData(singleProfile, System.currentTimeMillis());
//                        if (singleProfile.getProfileImage() != null && singleProfile.getProfileImage().length() > 0) {
//                            new ImageDownloader(singleProfile.getProfileImage(), singleProfile.getUserIdentity(), ImageDownloader.FRIEND_PROFILE_IMAGE).start();
//                        }
//                        if (singleProfile.getCoverImage() != null && singleProfile.getCoverImage().trim().length() > 0) {
//                            new ImageDownloader(singleProfile.getCoverImage(), js.getUserIdentity(), ImageDownloader.FRIEND_COVER_IMAGE).start();
//                        }
                    }
                }

                if (js.getSeq() != null) {
                    String[] d = js.getSeq().split("/");
                    if (d[1] != null) {
                        int seq = Integer.valueOf(d[1]);
                        if (peopleyouMayKnowcount >= seq) {
                            peopleyouMayKnowcount = 0;
                        }
                    }
                }
                if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                        && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel() != null) {
                    GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getSuggestionPanel().add_contents();
                }
            }
        } catch (Exception e) {
        }
    }

    private void processUnknownProfileInfo() {
        if (js.getSuccess()) {
            try {
                UserDetailsInfoDTO temp = HelperMethods.getUserDetailsInfo(response);
                if (temp != null && temp.getUserDetails() != null) {
                    UserBasicInfo basicInfo = new UserBasicInfo();
                    basicInfo.setUserIdentity(temp.getUserDetails().getUserIdentity());
                    basicInfo.setFullName(temp.getUserDetails().getFullName());
                    // basicInfo.setLastName(temp.getUserDetails().getLastName());
                    basicInfo.setCountry(temp.getUserDetails().getCountry());

                    if (js.getUserTableId() != null) {
                        MyfnfHashMaps.getInstance().getUnknowonProfileMap().put(js.getUserTableId() + "", basicInfo);
                    } else {
                        MyfnfHashMaps.getInstance().getUnknowonProfileMap().put(temp.getUserDetails().getUserIdentity(), basicInfo);
                    }
                    //MyfnfHashMaps.getInstance().getUnknowonProfileMap().put(js.getUserTableId(), basicInfo);
                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null
                            && GuiRingID.getInstance().getMainRight().getUnknownProfile() != null && basicInfo.getUserIdentity() != null) {
                        GuiRingID.getInstance().getMainRight().getUnknownProfile().friendProfileInfo = basicInfo;
                        GuiRingID.getInstance().getMainRight().getUnknownProfile().loadFriendInformation();
                    } else {
                        GuiRingID.getInstance().getMainRight().showUnknownProfile(basicInfo);
                    }
                    /*if (GuiRingID.getInstance().getMainRight().getGroupPanel() != null && GuiRingID.getInstance().getMainRight().getGroupPanel().groupMembersListPanel != null && basicInfo.getUserIdentity() != null) {
                     GuiRingID.getInstance().getMainRight().getGroupPanel().groupMembersListPanel.add(GuiRingID.getInstance().getMainRight().getGroupPanel().groupMembersListPanel.addContent(basicInfo));
                     GuiRingID.getInstance().getMainRight().getGroupPanel().groupMembersListPanel.revalidate();
                     }*/
                }
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in processUnknownProfileInfo ==>" + e.getMessage());
            }
        }
    }

    private void processAddEducation() {
        if (js.getSuccess()) {
            EducationInfoDTO education = HelperMethods.getEducationInfo(response);
            CreateEducationSinglePanel instance;
            boolean isClg = false;
            instance = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().getCreateSchoolSinglePanel();
            if (instance == null) {
                instance = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().getCreateClgSinglePanel();
                isClg = true;
            }
            if (instance != null) {
                instance.setName(education.getEducationId().toString());
                education.setSchool(instance.institutionTextField.getText());
                education.setDescription(instance.descArea.getText());
                education.setFromTime(instance.datePanel1.getDateinMillisecs());
                if (instance.currentlyStudying.isSelected()) {
                    education.setToTime(0L);
                } else {
                    education.setToTime(instance.datePanel2.getDateinMillisecs());
                }
                education.setGraduated(instance.graduated.isSelected());
                if (instance.type_int == 0) {
                    education.setIsSchool(true);
                    education.setAttendedFor(1);
                    education.setConcentration("");
                } else {
                    education.setIsSchool(false);
                    if (instance.attendedClgRadioButton.isSelected()) {
                        education.setAttendedFor(1);
                    } else {
                        if (instance.degreeTextField.getText().trim().length() > 0) {
                            education.setDegree(instance.degreeTextField.getText());
                        }
                        education.setAttendedFor(2);
                    }
                    education.setConcentration(instance.concentrationTextField.getText());
                }

                if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                        && !MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
                    MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(education.getEducationId(), education);
                } else {
                    Map<Long, EducationInfoDTO> educationMap = new HashMap<Long, EducationInfoDTO>();
                    educationMap.put(education.getEducationId(), education);
                    MyfnfHashMaps.getInstance().getEducationInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, educationMap);
                }

                instance.setVariables(education.getEducationId());
                instance.showUpdatedInfo();
                if (isClg) {
                    JPanel clgDetailsPanel = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().getClgDetailsPanel();
                    if (clgDetailsPanel.getComponentCount() == 1) {
                        JPanel jp = (JPanel) clgDetailsPanel.getComponent(0);
                        jp.setBorder(null);
                    }
                }
            }
        }
    }

    private void processAddSkill() {
        if (js.getSuccess()) {
            SkillInfoDTO skill = HelperMethods.getSkillInfo(response);
            CreateSkillSinglePanel instance = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().createSkillSinglePanelInstance;
            if (instance != null) {
                skill.setSkill(instance.skillField.getText());
                skill.setDescription(instance.descArea.getText());

                if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                        && !MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
                    MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(skill.getSkillInfoID(), skill);
                } else {
                    Map<Long, SkillInfoDTO> skillMap = new HashMap<>();
                    skillMap.put(skill.getSkillInfoID(), skill);
                    MyfnfHashMaps.getInstance().getSkillInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, skillMap);
                }
//                instance.setVariables(skill.getSkillInfoID());
//                instance.showUpdatedInfo();
                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainRight() != null
                        && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                        && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout() != null) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMySkillLable();
                    if (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getSkillPanel() != null) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getSkillPanel().addNewSkillPanel(skill);
                    }
                }
            }
        }
    }

    private void processAddWork() {
        if (js.getSuccess()) {
            WorkInfoDTO work = HelperMethods.getWorkInfo(response);
            CreateWorkSinglePanel instance = GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().createWorkSinglePanelInstance;
            if (instance != null) {
                work.setCompanyName(instance.companyField.getText());
                work.setDesignation(instance.positionField.getText());
                work.setCity(instance.cityField.getText());
                work.setDescription(instance.descArea.getText());
                work.setFromtime(instance.datePanel1.getDateinMillisecs());
                if (!instance.isCurrentWork) {
                    work.setTotime(instance.datePanel2.getDateinMillisecs());
                } else {
                    work.setTotime(0);
                }
                if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                        && !MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
                    MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(work.getWorkInfoID(), work);
                } else {
                    Map<Long, WorkInfoDTO> workMap = new HashMap<>();
                    workMap.put(work.getWorkInfoID(), work);
                    MyfnfHashMaps.getInstance().getWorkInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, workMap);
                }
                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainRight() != null
                        && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                        && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout() != null) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyWorkLable();
                    if (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getWorkPanel() != null) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getWorkPanel().addNewWorkPanel(work);

//                        instance.setVariables(work.getWorkInfoID());
//                        instance.showUpdatedInfo();
//                        instance.setVisible(true); 
                    }
                }
            }
        }
    }

    private void processEducationInfoList() {
        if (js.getSuccess()) {
            int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);

            EducationListDTO temp = HelperMethods.getEducationListInfo(response);
            if (temp != null && temp.getEducationList() != null) {
                if (js.getUserTableId() != null) {
                    if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(js.getUserTableId()) == null) {
                        Map<Long, EducationInfoDTO> educationMap = new HashMap<Long, EducationInfoDTO>();
                        for (EducationInfoDTO education : temp.getEducationList()) {
                            educationMap.put(education.getEducationId(), education);
                        }
                        MyfnfHashMaps.getInstance().getEducationInfoMap().put(js.getUserTableId(), educationMap);
                    } else {
                        for (EducationInfoDTO entity : temp.getEducationList()) {
                            MyfnfHashMaps.getInstance().getEducationInfoMap().get(js.getUserTableId()).put(entity.getEducationId(), entity);
                        }
                    }
                } else {
                    if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) == null) {
                        Map<Long, EducationInfoDTO> educationMap = new HashMap<Long, EducationInfoDTO>();
                        for (EducationInfoDTO education : temp.getEducationList()) {
                            educationMap.put(education.getEducationId(), education);
                        }
                        MyfnfHashMaps.getInstance().getEducationInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, educationMap);
                        //GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyEducationLable();
                    } else {
                        for (EducationInfoDTO entity : temp.getEducationList()) {
                            MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(entity.getEducationId(), entity);
                        }
                    }
                    //GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyEducationLable();
                }
                educationListSequenceCount++;

                if (js.getUserTableId() != null
                        && educationListSequenceCount == seqTotal) {
                    String friendId = "";
                    for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
                        if (basicInfo.getUserTableId().equals(js.getUserTableId())) {
                            friendId = basicInfo.getUserIdentity();
                            break;
                        }
                    }
                    if (friendId.length() > 0
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel() != null) {
                        educationListSequenceCount = 0;
                        MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel().showPreviouslyAddedEducationPanel(js.getUserTableId(), true, true);
                    }

                } else {

                    if (educationListSequenceCount == seqTotal) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyEducationLable();
                        if (GuiRingID.getInstance() != null
                                && GuiRingID.getInstance().getMainRight() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel() != null) {
                            educationListSequenceCount = 0;
                            GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getEducationPanel().showPreviouslyAddedEducationPanel(MyFnFSettings.LOGIN_USER_TABLE_ID, true, true);
                        }
                    }
                }
            }
        }
    }

    private void processSkillInfoList() {
        if (js.getSuccess()) {
            int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);

            SkillListDTO temp = HelperMethods.getSkillListInfo(response);
            if (temp != null && temp.getSkillList() != null) {
                if (js.getUserTableId() != null) {
                    if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(js.getUserTableId()) == null) {
                        Map<Long, SkillInfoDTO> skillMap = new HashMap<Long, SkillInfoDTO>();
                        for (SkillInfoDTO skill : temp.getSkillList()) {
                            skillMap.put(skill.getSkillInfoID(), skill);
                        }
                        MyfnfHashMaps.getInstance().getSkillInfoMap().put(js.getUserTableId(), skillMap);
                    } else {
                        for (SkillInfoDTO entity : temp.getSkillList()) {
                            MyfnfHashMaps.getInstance().getSkillInfoMap().get(js.getUserTableId()).put(entity.getSkillInfoID(), entity);
                        }
                    }
                } else {
                    if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) == null) {
                        Map<Long, SkillInfoDTO> skillMap = new HashMap<Long, SkillInfoDTO>();
                        for (SkillInfoDTO skill : temp.getSkillList()) {
                            skillMap.put(skill.getSkillInfoID(), skill);
                        }
                        MyfnfHashMaps.getInstance().getSkillInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, skillMap);
                        //  GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMySkillLable();
                    } else {
                        for (SkillInfoDTO entity : temp.getSkillList()) {
                            MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(entity.getSkillInfoID(), entity);
                        }
                    }
//                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMySkillLable();
                }
                skillListSequenceCount++;
                if (js.getUserTableId() != null && skillListSequenceCount == seqTotal) {
                    String friendId = "";
                    for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
                        if (basicInfo.getUserTableId().equals(js.getUserTableId())) {
                            friendId = basicInfo.getUserIdentity();
                            break;
                        }
                    }
                    if (friendId.length() > 0
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel() != null) {
                        skillListSequenceCount = 0;
                        MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel().showPreviouslyAddedSkillPanel(js.getUserTableId());
                    }
                } else {
                    if (skillListSequenceCount == seqTotal) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMySkillLable();
                        if (GuiRingID.getInstance() != null
                                && GuiRingID.getInstance().getMainRight() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getSkillPanel() != null) {
                            skillListSequenceCount = 0;
                            GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getSkillPanel().showPreviouslyAddedSkillPanel(MyFnFSettings.LOGIN_USER_TABLE_ID);
                        }
                    }
                }
            }
        }
    }

    private void processWorkInfoList() {
        if (js.getSuccess()) {
            int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);

            WorkListDTO temp = HelperMethods.getWorkListInfo(response);
            if (temp != null && temp.getWorkList() != null) {
                if (js.getUserTableId() != null) {
                    if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(js.getUserTableId()) == null) {
                        Map<Long, WorkInfoDTO> workMap = new HashMap<Long, WorkInfoDTO>();
                        for (WorkInfoDTO work : temp.getWorkList()) {
                            workMap.put(work.getWorkInfoID(), work);
                        }
                        MyfnfHashMaps.getInstance().getWorkInfoMap().put(js.getUserTableId(), workMap);
                    } else {
                        for (WorkInfoDTO entity : temp.getWorkList()) {
                            MyfnfHashMaps.getInstance().getWorkInfoMap().get(js.getUserTableId()).put(entity.getWorkInfoID(), entity);
                        }
                    }
                } else {
                    if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) == null) {
                        Map<Long, WorkInfoDTO> workMap = new HashMap<Long, WorkInfoDTO>();
                        for (WorkInfoDTO work : temp.getWorkList()) {
                            workMap.put(work.getWorkInfoID(), work);
                        }
                        MyfnfHashMaps.getInstance().getWorkInfoMap().put(MyFnFSettings.LOGIN_USER_TABLE_ID, workMap);
                        //   GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyWorkLable();
                    } else {
                        for (WorkInfoDTO entity : temp.getWorkList()) {
                            MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).put(entity.getWorkInfoID(), entity);
                        }
                    }
                    //GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyWorkLable();
                }

                workListSequenceCount++;
                if (js.getUserTableId() != null && workListSequenceCount == seqTotal) {
                    String friendId = "";
                    for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
                        if (basicInfo.getUserTableId().equals(js.getUserTableId())) {
                            friendId = basicInfo.getUserIdentity();
                            break;
                        }
                    }
                    if (friendId.length() > 0
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null
                            && MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel() != null) {
                        workListSequenceCount = 0;
                        MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendAboutPanel.getWorkandEducationPanel().showPreviouslyAddedWorksPanel(js.getUserTableId());
                    }
                } else {
                    if (workListSequenceCount == seqTotal) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().updateMyWorkLable();
                        if (GuiRingID.getInstance() != null
                                && GuiRingID.getInstance().getMainRight() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout() != null
                                && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getWorkPanel() != null) {
                            workListSequenceCount = 0;
                            GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().getWorkPanel().showPreviouslyAddedWorksPanel(MyFnFSettings.LOGIN_USER_TABLE_ID);
                        }
                    }
                }
            }
        }
    }

    private void processRecoverySuggestion() {
        if (js.getSuccess()) {
            JsonFields js = HelperMethods.getJsonFields(response);
            if (js != null && js.getSuggetions() != null && js.getSuggetions().size() > 0) {
                MyfnfHashMaps.getInstance().getTempRecoverySuggestionsMap().put(js.getPacketId(), js.getSuggetions());
            }
        }
    }

    private void processNewsFeed() {
        if (js.getSuccess()) {
            try {
                NewsFeedMap tamp_list = HelperMethods.mapNewsFeed(response);
                if (tamp_list != null && tamp_list.getNewsFeedList() != null) {
                    for (final NewsFeedWithMultipleImage singleNews : tamp_list.getNewsFeedList()) {
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(singleNews.getNfId()) == null) {
                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(singleNews.getNfId(), singleNews);
                        }
                        NewsFeedMaps.getInstance().getAllNewfeedId().insertDataFromServer(singleNews.getNfId(), singleNews.getTm());
                        if (singleNews.getWhoShare() != null && singleNews.getWhoShare().size() > 0) {
                            for (SingleBookDetails sbd : singleNews.getWhoShare()) {
                                NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), singleNews.getNfId());
                                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(sbd.getNfId()) == null) {
                                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                                }
                                //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                                if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()) != null) {
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()).insertDataFromServer(sbd.getNfId(), sbd.getTm());
                                } else {
                                    SortedArrayList sortedArrayList = new SortedArrayList();
                                    sortedArrayList.insertDataFromServer(sbd.getNfId(), sbd.getTm());
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().put(singleNews.getNfId(), sortedArrayList);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Exception in processNewsFeed ==>" + e.getMessage());
            }
        }
    }

    private void processMyBook() {
        try {
            NewsFeedMap tamp_list = HelperMethods.mapNewsFeed(response);
            if (tamp_list.getSuccess()) {
                if (tamp_list.getNewsFeedList() != null) {
                    for (final NewsFeedWithMultipleImage singleNews : tamp_list.getNewsFeedList()) {
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(singleNews.getNfId()) == null) {
                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(singleNews.getNfId(), singleNews);
                        }
                        NewsFeedMaps.getInstance().getMyNewsFeedId().insertDataFromServer(singleNews.getNfId(), singleNews.getAddedTime());

                        if (singleNews.getWhoShare() != null && singleNews.getWhoShare().size() > 0) {
                            for (SingleBookDetails sbd : singleNews.getWhoShare()) {
                                NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), singleNews.getNfId());
                                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(sbd.getNfId()) == null) {
                                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                                }
                                //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                                if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()) != null) {
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()).insertDataFromServer(sbd.getNfId(), sbd.getAddedTime());
                                } else {
                                    SortedArrayList sortedArrayList = new SortedArrayList();
                                    sortedArrayList.insertDataFromServer(sbd.getNfId(), sbd.getAddedTime());
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().put(singleNews.getNfId(), sortedArrayList);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void processFriendNewsFeed() {

        try {
            NewsFeedMap tamp_list = HelperMethods.mapNewsFeed(response);
            if (tamp_list.getSuccess()) {
                if (tamp_list.getNewsFeedList() != null) {
                    if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(tamp_list.getFriendIdentity()) == null) {
                        NewsFeedMaps.getInstance().getFriendNewsFeedId().put(tamp_list.getFriendIdentity(), new SortedArrayList());
                    }

                    for (final NewsFeedWithMultipleImage singleNews : tamp_list.getNewsFeedList()) {
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(singleNews.getNfId()) == null) {
                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(singleNews.getNfId(), singleNews);
                        }
                        NewsFeedMaps.getInstance().getFriendNewsFeedId().get(tamp_list.getFriendIdentity()).insertDataFromServer(singleNews.getNfId(), singleNews.getAddedTime());

                        if (singleNews.getWhoShare() != null && singleNews.getWhoShare().size() > 0) {
                            for (SingleBookDetails sbd : singleNews.getWhoShare()) {
                                NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), singleNews.getNfId());
                                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(sbd.getNfId()) == null) {
                                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                                }
                                //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                                if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()) != null) {
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()).insertDataFromServer(sbd.getNfId(), sbd.getAddedTime());
                                } else {
                                    SortedArrayList sortedArrayList = new SortedArrayList();
                                    sortedArrayList.insertDataFromServer(sbd.getNfId(), sbd.getAddedTime());
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().put(singleNews.getNfId(), sortedArrayList);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in processFriendNewsFeed ==>" + e.getMessage());
        }
    }

    private void processCircleNewsFeed() {
        try {
            NewsFeedMap tamp_list = HelperMethods.mapNewsFeed(response);
            if (tamp_list.getSuccess()) {
                if (tamp_list.getNewsFeedList() != null) {
                    if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(tamp_list.getCircleId()) == null) {
                        NewsFeedMaps.getInstance().getCircleNewsFeedId().put(tamp_list.getCircleId(), new SortedArrayList());
                    }

                    for (final NewsFeedWithMultipleImage singleNews : tamp_list.getNewsFeedList()) {
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(singleNews.getNfId()) == null) {
                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(singleNews.getNfId(), singleNews);
                        }
                        NewsFeedMaps.getInstance().getCircleNewsFeedId().get(tamp_list.getCircleId()).insertDataFromServer(singleNews.getNfId(), singleNews.getTm());

                        if (singleNews.getWhoShare() != null && singleNews.getWhoShare().size() > 0) {
                            for (SingleBookDetails sbd : singleNews.getWhoShare()) {
                                NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), singleNews.getNfId());
                                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(sbd.getNfId()) == null) {
                                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                                }
                                //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                                if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()) != null) {
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()).insertDataFromServer(sbd.getNfId(), sbd.getTm());
                                } else {
                                    SortedArrayList sortedArrayList = new SortedArrayList();
                                    sortedArrayList.insertDataFromServer(sbd.getNfId(), sbd.getTm());
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().put(singleNews.getNfId(), sortedArrayList);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in processCircleNewsFeed ==>" + e.getMessage());
        }

    }

    private void processTypeMultipleImagePost() {
        try {
            /*
             {"actn":117,"stId":5424,"tm":1408610630785,"imageType":4,"pckId":"ring880191172231991408610638481","sucs":true,"fn":"Wasif","ln":"Islam","imageList":[{"imgId":3572,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/ring88019117223191408610625438/1408610619661.png","cptn":"","ih":155,"iw":300,"imT":1},{"imgId":3573,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/ring88019117223191408610625438/1408610622991.png","cptn":"","ih":300,"iw":300,"imT":1},{"imgId":3574,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/ring88019117223191408610625438/1408610626643.png","cptn":"","ih":300,"iw":300,"imT":1},{"imgId":3575,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/ring88019117223191408610625438/1408610629112.png","cptn":"","ih":183,"iw":300,"imT":1},{"imgId":3576,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/ring88019117223191408610625438/1408610630160.png","cptn":"","ih":225,"iw":300,"imT":1}]}
             {"actn":117,"stId":5434,"sts":"test","tm":1408611897885,"imageType":3,"pckId":"ring8801911722319111408611905581","sucs":true,"fn":"Wasif","ln":"Islam","imageList":[{"imgId":3582,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408611886702.png","cptn":"","ih":155,"iw":300,"imT":1},{"imgId":3583,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408611890082.png","cptn":"","ih":300,"iw":300,"imT":1},{"imgId":3584,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408611894322.png","cptn":"","ih":300,"iw":300,"imT":1},{"imgId":3585,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408611896441.png","cptn":"","ih":183,"iw":300,"imT":1},{"imgId":3586,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408611897301.png","cptn":"","ih":225,"iw":300,"imT":1}]}   * {"imgId":3306,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408517602442.jpg","cptn":"","ih":183,"iw":300,"imT":1}]}
             */
            NewsFeedWithMultipleImage newfFeedWithMultipleImage = HelperMethods.getNewsFeedWithMultipleImageDto(response);
            js.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
            if (newfFeedWithMultipleImage != null && NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()) == null) {
                NewsFeedMaps.getInstance().getAllNewsFeeds().put(js.getNfId(), newfFeedWithMultipleImage);
            }
            NewsFeedMaps.getInstance().getAllNewfeedId().insertData(js.getNfId(), js.getTm());
            NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(js.getNfId(), js.getTm());
            if (js.getFriendIdentity() != null && js.getFriendIdentity().length() > 0) {
                if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(js.getFriendIdentity()) == null) {
                    NewsFeedMaps.getInstance().getFriendNewsFeedId().put(js.getFriendIdentity(), new SortedArrayList());
                }
                NewsFeedMaps.getInstance().getFriendNewsFeedId().get(js.getFriendIdentity()).insertData(js.getNfId(), js.getTm());
            }
            if (js.getCircleId() != null && js.getCircleId() > 0) {
                if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(js.getCircleId()) == null) {
                    NewsFeedMaps.getInstance().getCircleNewsFeedId().put(js.getCircleId(), new SortedArrayList());
                }
                NewsFeedMaps.getInstance().getCircleNewsFeedId().get(js.getCircleId()).insertData(js.getNfId(), js.getTm());
            }

            if (GuiRingID.getInstance() != null
                    && GuiRingID.getInstance().getMainRight() != null
                    && GuiRingID.getInstance().getMainRight().getAllFeedsView() != null
                    && GuiRingID.getInstance().getMainRight().getAllFeedsView().scrollContent.getVerticalScrollBar().getValue() < (GuiRingID.getInstance().getMainRight().getAllFeedsView().postToFeeds.getHeight() + 32)) {
                GuiRingID.getInstance().getMainRight().getAllFeedsView().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()));
            }

            if (getMainRight().getMyProfilePanel() != null && getMainRight().getMyProfilePanel().getMyFeeds() != null
                    && getMainRight().getMyProfilePanel().getMyFeeds().scrollContent.getVerticalScrollBar().getValue() < (getMainRight().getMyProfilePanel().getMyFeeds().postToFeeds.getHeight() + 32)) {
                getMainRight().getMyProfilePanel().getMyFeeds().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()));
            }

            if (js.getFriendIdentity() != null && js.getFriendIdentity().length() > 0) {
                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getFriendIdentity());
                if (friendProfile != null
                        && friendProfile.myFriendNewsFeedPanel != null
                        && friendProfile.myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().getValue() < (friendProfile.myFriendNewsFeedPanel.postToFeeds.getHeight() + 32)) {
                    friendProfile.myFriendNewsFeedPanel.addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()));
                }
            }

            if (js.getCircleId() != null && js.getCircleId() > 0) {
                CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(js.getCircleId());
                if (circleProfile != null
                        && circleProfile.getCircleNewsFeedPanel() != null
                        && circleProfile.getCircleNewsFeedPanel().scrollContent.getVerticalScrollBar().getValue() < (circleProfile.getCircleNewsFeedPanel().postToFeeds.getHeight() + 32)) {
                    circleProfile.getCircleNewsFeedPanel().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()));
                }
            }

        } catch (Exception e) {
            log.error("processTypeMultipleImagePost==>" + response);
        }
    }

    private void processMyAlbums() {
        try {
            if (js.getSuccess()) {
                AlbumMap tamp_list = HelperMethods.mapAlbumMap(response);
                if (tamp_list != null && tamp_list.getAlbumList() != null) {
                    for (final JsonFields singleAlbum : tamp_list.getAlbumList()) {
                        NewsFeedMaps.getInstance().getMyAllbums().put(singleAlbum.getAlbId(), singleAlbum);
                    }
                }
            }
        } catch (Exception e) {
            log.error("processMyAlbums==>" + response);
        }
    }

    private void processMyAlbumImages() {
        AlbumImageMap albumImages = HelperMethods.albumImageMap(response);
        String[] d = null;

        if (albumImages != null && albumImages.getImageList() != null) {
            for (final JsonFields singleNews : albumImages.getImageList()) {
                Map<String, JsonFields> commentsJson;
                if (albumImages.getAlbId() != null) {
                    if (MyfnfHashMaps.getInstance().getAlBumImages().get(albumImages.getAlbId()) == null) {
                        commentsJson = new ConcurrentHashMap<String, JsonFields>();
                        commentsJson.put(singleNews.getIurl(), singleNews);
                        MyfnfHashMaps.getInstance().getAlBumImages().put(albumImages.getAlbId(), commentsJson);
                    } else {
                        MyfnfHashMaps.getInstance().getAlBumImages().get(albumImages.getAlbId()).put(singleNews.getIurl(), singleNews);
                    }
                } else {
                    if (MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.LOGIN_USER_ID) == null) {
                        commentsJson = new ConcurrentHashMap<String, JsonFields>();
                        commentsJson.put(singleNews.getIurl(), singleNews);
                        MyfnfHashMaps.getInstance().getAlBumImages().put(MyFnFSettings.LOGIN_USER_ID, commentsJson);
                    } else {
                        MyfnfHashMaps.getInstance().getAlBumImages().get(MyFnFSettings.LOGIN_USER_ID).put(singleNews.getIurl(), singleNews);
                    }
                }
            }

            if (js.getSeq() != null) {
                d = js.getSeq().split("/");
            }
            if (getMainRight() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos != null) {
                if (albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID)) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.PROFILE_IMAGES_COUNT = albumImages.getTotalImage();
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.profiePhotosTab.setText(Photos.PROFILE_IMAGES_STR + " (" + Photos.PROFILE_IMAGES_COUNT + ")");

                } else if (albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.COVER_IMAGE_ALBUM_ID)) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.COVER_IMAGES_COUNT = albumImages.getTotalImage();
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.coverPhotosTab.setText(Photos.COVER_IMAGES_STR + " (" + Photos.COVER_IMAGES_COUNT + ")");
                } else if (albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.FEED_IMAGE_ALBUM_ID)) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.FEED_IMAGES_COUNT = albumImages.getTotalImage();
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.feedPhotosTab.setText(Photos.FEED_IMAGES_STR + " (" + Photos.FEED_IMAGES_COUNT + ")");
                }
            }

            if (getMainRight() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.isLoading == true) {

                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.removeLoadingcontent();

                if ((GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_PROFILE_PHOTO && albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID))
                        || (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_COVER_PHOTO && albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.COVER_IMAGE_ALBUM_ID))
                        || (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_FEED_PHOTO && albumImages.getAlbId().equalsIgnoreCase(MyFnFSettings.FEED_IMAGE_ALBUM_ID))) {

                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.addMoreMyImages(albumImages.getImageList());
                } else {
                    if (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_PROFILE_PHOTO) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.addMyAllImages(MyFnFSettings.PROFILE_IMAGE_ALBUM_ID);
                    } else if (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_COVER_PHOTO) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.addMyAllImages(MyFnFSettings.COVER_IMAGE_ALBUM_ID);
                    } else if (GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.type == Photos.TYPE_MY_FEED_PHOTO) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.addMyAllImages(MyFnFSettings.FEED_IMAGE_ALBUM_ID);
                    }

                }

                if (d != null && d[0].equalsIgnoreCase(d[1])) {
                    GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.isLoading = false;
                }

            }
            if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null && SingleImageDetailsProfileCover.getSingleImgeDetails().isVisible() && d != null && d[0].equalsIgnoreCase(d[1])) {
                // SingleImageDetailsProfileCover.getSingleImgeDetails().loadMyAllImagesFromServer(MyfnfHashMaps.getInstance().getAlBumImages().get((SingleImageDetailsProfileCover.getSingleImgeDetails().imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).size());
                SingleImageDetailsProfileCover.getSingleImgeDetails().setPreviousNextImages();
            }

            if (ImagesInAlbumFolder.getInstance() != null && ImagesInAlbumFolder.getInstance().isLoading == true) {
                ImagesInAlbumFolder.getInstance().removeLoadingcontent();
                ImagesInAlbumFolder.getInstance().addMoreImages(albumImages.getImageList());
            }

            if (ChooseFromAlbum.getInstance() != null && ChooseFromAlbum.getInstance().isLoading == true) {
                ChooseFromAlbum.getInstance().removeLoadingContent();
                ChooseFromAlbum.getInstance().addMoreImages(albumImages.getImageList());
            }

            if (ProfileCoverChangePanel.instance != null && ProfileCoverChangePanel.getInstance().isLoading == true) {
                ProfileCoverChangePanel.getInstance().removeLoadingContent();
                ProfileCoverChangePanel.getInstance().addMoreMyImages(albumImages.getImageList());
            }

            if (ImagesInAlbumFolder.getInstance() != null) {
                ImagesInAlbumFolder.getInstance().isLoading = false;
            }

            if (ChooseFromAlbum.getInstance() != null) {
                ChooseFromAlbum.getInstance().isLoading = false;
            }

            if (ProfileCoverChangePanel.instance != null) {
                if (d != null && d[0].equalsIgnoreCase(d[1])) {
                    ProfileCoverChangePanel.getInstance().isLoading = false;
                }
            }
        } else {
            if (GuiRingID.getInstance() != null
                    && GuiRingID.getInstance().getMainRight() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome() != null
                    && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos != null) {
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.removeLoadingcontent();
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().photos.isLoading = false;
            }
            if (ImagesInAlbumFolder.getInstance() != null) {
                ImagesInAlbumFolder.getInstance().removeLoadingcontent();
                ImagesInAlbumFolder.getInstance().isLoading = false;
            }

            if (ChooseFromAlbum.getInstance() != null) {
                ChooseFromAlbum.getInstance().removeLoadingContent();
                ChooseFromAlbum.getInstance().isLoading = false;
            }

            if (ProfileCoverChangePanel.instance != null) {
                ProfileCoverChangePanel.getInstance().removeLoadingContent();
                ProfileCoverChangePanel.getInstance().isLoading = false;
            }
        }
    }

    private void processCommentsForImage() {
        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel2 = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()) {
                addImageCaptionCommentsPanel2 = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageID == js.getImageId()) {
                addImageCaptionCommentsPanel2 = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;
            }
            if (addImageCaptionCommentsPanel2 != null) {
                CommentsMap tamp_list = HelperMethods.mapComments(response);
                if (tamp_list != null && tamp_list.getComments() != null) {
                    for (final JsonFields singleNews : tamp_list.getComments()) {
                        singleNews.setIsRead(false);
                        addImageCaptionCommentsPanel2.getImageComments().put(singleNews.getCmnId(), singleNews);
                    }
                    /*if (addImageCaptionCommentsPanel2.getImageComments().size() <= 10) {
                     addImageCaptionCommentsPanel2.repaintAllcomments();
                     addImageCaptionCommentsPanel2.changeImageCommentAllinBar();
                     } else {
                     addImageCaptionCommentsPanel2.addPreviousComments();

                     }*/
                    addImageCaptionCommentsPanel2.addPreviousComments();
                }
            }
        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }
    }

    private void processCommentsForStatus() {
        try {
            CommentsMap tamp_list = HelperMethods.mapComments(response);
            if (tamp_list != null && tamp_list.getComments() != null) {
                for (final JsonFields singleNews : tamp_list.getComments()) {
                    singleNews.setIsRead(false);
                    if (NewsFeedMaps.getInstance().getComments().get(tamp_list.getNfId()) == null) {
                        Map<String, JsonFields> commentsJson = new ConcurrentHashMap<String, JsonFields>();
                        commentsJson.put(singleNews.getCmnId(), singleNews);
                        NewsFeedMaps.getInstance().getComments().put(tamp_list.getNfId(), commentsJson);
                    } else {
                        NewsFeedMaps.getInstance().getComments().get(tamp_list.getNfId()).put(singleNews.getCmnId(), singleNews);
                    }
                }

                Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

                SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                if (parentNfId != null) {
                    SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                    SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                    SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                    SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                    SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                    if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                    }
                } else {
                    singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                    singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                    singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                    singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                    singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
                }

                if (NewsFeedMaps.getInstance().getComments().get(tamp_list.getNfId()).size() <= 10) {
                    if (singleStatusPanelInHome != null) {
                        singleStatusPanelInHome.repaintAllcomments();
                        singleStatusPanelInHome.change_comments_number();
                    }
                    if (singleStatusPanelInMyBook != null) {
                        singleStatusPanelInMyBook.repaintAllcomments();
                        singleStatusPanelInMyBook.change_comments_number();
                    }
                    if (singleStatusPanelInFriendNewsFeed != null) {
                        singleStatusPanelInFriendNewsFeed.repaintAllcomments();
                        singleStatusPanelInFriendNewsFeed.change_comments_number();
                    }
                    if (singleStatusPanelInCircleNewsFeed != null) {
                        singleStatusPanelInCircleNewsFeed.repaintAllcomments();
                        singleStatusPanelInCircleNewsFeed.change_comments_number();
                    }
                    if (singleStatusPanelInNotification != null) {
                        singleStatusPanelInNotification.repaintAllcomments();
                        singleStatusPanelInNotification.change_comments_number();
                    }
                    if (shareFeedPanelInHome != null) {
                        shareFeedPanelInHome.repaintAllcomments();
                        shareFeedPanelInHome.change_comments_number();
                    }
                    if (shareFeedPanelInMyBook != null) {
                        shareFeedPanelInMyBook.repaintAllcomments();
                        shareFeedPanelInMyBook.change_comments_number();
                    }
                    if (shareFeedPanelInFriendNewsFeed != null) {
                        shareFeedPanelInFriendNewsFeed.repaintAllcomments();
                        shareFeedPanelInFriendNewsFeed.change_comments_number();
                    }
                    if (shareFeedPanelInCircleNewsFeed != null) {
                        shareFeedPanelInCircleNewsFeed.repaintAllcomments();
                        shareFeedPanelInCircleNewsFeed.change_comments_number();
                    }
                    if (shareFeedPanelInNotification != null) {
                        shareFeedPanelInNotification.repaintAllcomments();
                        shareFeedPanelInNotification.change_comments_number();
                    }
                } else {
                    if (singleStatusPanelInHome != null) {
                        singleStatusPanelInHome.addPreviousComments();
                    }
                    if (singleStatusPanelInMyBook != null) {
                        singleStatusPanelInMyBook.addPreviousComments();
                    }
                    if (singleStatusPanelInFriendNewsFeed != null) {
                        singleStatusPanelInFriendNewsFeed.addPreviousComments();
                    }
                    if (singleStatusPanelInCircleNewsFeed != null) {
                        singleStatusPanelInCircleNewsFeed.addPreviousComments();
                    }
                    if (singleStatusPanelInNotification != null) {
                        singleStatusPanelInNotification.addPreviousComments();
                    }
                    if (shareFeedPanelInHome != null) {
                        shareFeedPanelInHome.addPreviousComments();
                    }
                    if (shareFeedPanelInMyBook != null) {
                        shareFeedPanelInMyBook.addPreviousComments();
                    }
                    if (shareFeedPanelInFriendNewsFeed != null) {
                        shareFeedPanelInFriendNewsFeed.addPreviousComments();
                    }
                    if (shareFeedPanelInCircleNewsFeed != null) {
                        shareFeedPanelInCircleNewsFeed.addPreviousComments();
                    }
                    if (shareFeedPanelInNotification != null) {
                        shareFeedPanelInNotification.addPreviousComments();
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    private void processFetchLikesForImage() {
        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel2 = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;
            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel2 != null) {
                LikesMap tamp_list = HelperMethods.mapLikes(response);
                if (tamp_list != null && tamp_list.getLikes() != null) {
                    for (final SingleMemberInCircleDto singleNews : tamp_list.getLikes()) {
                        addImageCaptionCommentsPanel2.getImageLikes().put(singleNews.getUserIdentity(), singleNews);
                    }
                    if (ImageLikesPopup.getInstance() != null && ImageLikesPopup.getInstance().isVisible()) {
                        ImageLikesPopup.getInstance().addLikesFromServer(tamp_list.getLikes());
                    }
                    //  addImageCaptionCommentsPanel2.change_like_number();
                }
                tamp_list = null;
            }
        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }
    }

    private void processFetchLikesForStatus() {
        try {
            LikesMap tamp_list = HelperMethods.mapLikes(response);
            if (tamp_list != null && tamp_list.getLikes() != null) {
                for (final SingleMemberInCircleDto singleNews : tamp_list.getLikes()) {
                    if (NewsFeedMaps.getInstance().getLikes().get(tamp_list.getNfId().toString()) == null) {
                        Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                        commentsJson.put(singleNews.getUserIdentity(), singleNews);
                        NewsFeedMaps.getInstance().getLikes().put(tamp_list.getNfId().toString(), commentsJson);
                    } else {
                        NewsFeedMaps.getInstance().getLikes().get(tamp_list.getNfId().toString()).put(singleNews.getUserIdentity(), singleNews);
                    }
                }

                if (LikePopupSingleFeed.getInstance() != null
                        && LikePopupSingleFeed.getInstance().likeskey.equals(tamp_list.getNfId())
                        && LikePopupSingleFeed.getInstance().isVisible()) {
                    LikePopupSingleFeed.getInstance().addLikesFromServer(tamp_list.getLikes());
                }

                Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

                SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                if (parentNfId != null) {
                    SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                    SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                    SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                    SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                    SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                    if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                    }
                } else {
                    singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                    singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                    singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                    singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                    singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
                }

                if (singleStatusPanelInHome != null) {
                    singleStatusPanelInHome.change_like_number();
                }
                if (singleStatusPanelInMyBook != null) {
                    singleStatusPanelInMyBook.change_like_number();
                }
                if (singleStatusPanelInFriendNewsFeed != null) {
                    singleStatusPanelInFriendNewsFeed.change_like_number();
                }
                if (singleStatusPanelInCircleNewsFeed != null) {
                    singleStatusPanelInCircleNewsFeed.change_like_number();
                }
                if (singleStatusPanelInNotification != null) {
                    singleStatusPanelInNotification.change_like_number();
                }
                if (shareFeedPanelInHome != null) {
                    shareFeedPanelInHome.change_like_number();
                }
                if (shareFeedPanelInMyBook != null) {
                    shareFeedPanelInMyBook.change_like_number();
                }
                if (shareFeedPanelInFriendNewsFeed != null) {
                    shareFeedPanelInFriendNewsFeed.change_like_number();
                }
                if (shareFeedPanelInCircleNewsFeed != null) {
                    shareFeedPanelInCircleNewsFeed.change_like_number();
                }
                if (shareFeedPanelInNotification != null) {
                    shareFeedPanelInNotification.change_like_number();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("processFetchLikesForStatus ex==>" + e.getMessage());

        }
    }

    private void processFetchLikesForAnImageComment() {
        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel2 = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel2 = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel2 != null) {
                SingeImageCommentDetails singeImageCommentDetails = addImageCaptionCommentsPanel2.getCommentsOfthisImage().get(js.getCmnId());

                LikesMap tamp_list = HelperMethods.mapLikes(response);
                if (tamp_list != null && tamp_list.getLikes() != null) {
                    for (final SingleMemberInCircleDto singleNews : tamp_list.getLikes()) {
                        singeImageCommentDetails.getImageCommentsLikes().put(singleNews.getUserIdentity(), singleNews);
                    }
                    if (LikesInImageCommentsPopup.getInstance() != null
                            && LikesInImageCommentsPopup.getInstance().isVisible()) {
                        LikesInImageCommentsPopup.getInstance().addLikesFromServer(tamp_list.getLikes());
                    }

                }

            }

        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }
    }

    private void processFetchLikesForAComment() {
        try {
            LikesMap tamp_list = HelperMethods.mapLikes(response);
            if (tamp_list != null && tamp_list.getLikes() != null && js.getNfId() != null && js.getCmnId() != null) {
                String keys = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());
                for (final SingleMemberInCircleDto singleNews : tamp_list.getLikes()) {
                    if (NewsFeedMaps.getInstance().getLikes().get(keys) == null) {
                        Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                        commentsJson.put(singleNews.getUserIdentity(), singleNews);
                        NewsFeedMaps.getInstance().getLikes().put(keys, commentsJson);
                    } else {
                        NewsFeedMaps.getInstance().getLikes().get(keys).put(singleNews.getUserIdentity(), singleNews);
                    }

                }

                if (LikesInCommentsJdialog.getInstance() != null
                        //&& LikesInCommentsJdialog.getInstance().likeskey.equals(tamp_list.getNfId())
                        && LikesInCommentsJdialog.getInstance().isVisible()) {
                    LikesInCommentsJdialog.getInstance().addLikesFromServer(tamp_list.getLikes());
                }

            }
        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }
    }

    private void processUpdateLikeUnlikeImage() {
        try {
            /*
             //like
             {"pckFs":9722,"sucs":true,"imgId":9170,"ln":"Decimus Meridius","lkd":1,"actn":385,"uId":"ring8801834904918","fn":"Maximus"}
             //unlike
             {"pckFs":9760,"sucs":true,"imgId":9170,"ln":"Decimus Meridius","lkd":0,"actn":385,"uId":"ring8801834904918","fn":"Maximus"}
             */
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel != null) {
                //   AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;
                JsonFields likeUnlikeImageDto = HelperMethods.getOnlyNewsFeedDto(response);
                if (!addImageCaptionCommentsPanel.getImageLikes().isEmpty()) {
                    if (likeUnlikeImageDto.getLkd() > 0) {
                        SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
                        if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
                            UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
                            singleFriend.setUserIdentity(js.getUserIdentity());
                            singleFriend.setFullName(bac.getFullName());
                            // singleFriend.setLastName(bac.getLastName());
                            singleFriend.setProfileImage(bac.getProfileImage());
                        } else {
                            singleFriend.setUserIdentity(js.getUserIdentity());
                        }
                        addImageCaptionCommentsPanel.getImageLikes().put(js.getUserIdentity(), singleFriend);
                    } else {
                        addImageCaptionCommentsPanel.getImageLikes().remove(js.getUserIdentity());
                    }
                }
                if (likeUnlikeImageDto.getLkd() > 0) {
                    addImageCaptionCommentsPanel.increaseDecreseNL(true);
                    // addImageCaptionCommentsPanel.imageDTo.setNl(addImageCaptionCommentsPanel.imageDTo.getNl() + 1);
                } else {
                    addImageCaptionCommentsPanel.increaseDecreseNL(false);

                }
                addImageCaptionCommentsPanel.changeImageLikeAll();
            }
//            SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
//            if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
//                UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
//                singleFriend.setUserIdentity(js.getUserIdentity());
//                singleFriend.setFullName(bac.getFullName());
//                singleFriend.setLastName(bac.getLastName());
//                singleFriend.setProfileImage(bac.getProfileImage());
//            } else {
//                singleFriend.setUserIdentity(js.getUserIdentity());
//            }
//            // JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
//            if (NewsFeedMaps.getInstance().getImageLikes().get(js.getImageId()) == null) {
//                Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
//                commentsJson.put(js.getUserIdentity(), singleFriend);
//                NewsFeedMaps.getInstance().getImageLikes().put(js.getImageId(), commentsJson);
//            } else {
//                NewsFeedMaps.getInstance().getImageLikes().get(js.getImageId()).put(js.getUserIdentity(), singleFriend);
//            }
//
//            if (NewsFeedMaps.getInstance().getSingleStatusPanelImageViewer().get(js.getImageId()) != null) {
//                // NewsFeedMaps.getInstance().getViewFullImage().get(js.getImageId()).singleViewImage.c 
//            }

        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }
    }

//    private void processSmsList() {
//        if (js.getSuccess() && js.getSeq() != null) {
//            SmsListMapper mapper = HelperMethods.mapSmsList(response);
//            MyfnfHashMaps.getInstance().getTempSmsListPacket().put(mapper.getSeq(), mapper);
//
//            int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);
//            if (!MyfnfHashMaps.getInstance().getTempSmsListPacket().isEmpty() && MyfnfHashMaps.getInstance().getTempSmsListPacket().size() == seqTotal) {
//                List<SmsFields> smsFieldList = new ArrayList<SmsFields>();
//                Set<Long> deletedList = new HashSet<Long>();
//
//                for (Entry entry : MyfnfHashMaps.getInstance().getTempSmsListPacket().entrySet()) {
//                    SmsListMapper map = (SmsListMapper) entry.getValue();
//                    if (map != null && map.getSmsList() != null) {
//                        for (SmsFields sms : map.getSmsList()) {
//                            if (sms.getStatus() == AppConstants.HISTORY_STATUS_DELETED) {
//                                deletedList.add(sms.getId());
//                                if (sms.getUt() != null && sms.getUt() > SettingsConstants.FNF_SMS_LOG_UT) {
//                                    SettingsConstants.FNF_SMS_LOG_UT = sms.getUt();
//                                }
//                            } else {
//                                smsFieldList.add(sms);
//                            }
//
//                        }
//                    }
//                }
//
//                if (deletedList.size() > 0) {
//                    (new InsertIntoRingUserSettings()).start();
//                    (new DeleteFromSmsHistoryTable(deletedList)).start();
//                }
//
//                MyfnfHashMaps.getInstance().getTempSmsListPacket().clear();
//                (new InsertIntoSmsHistoryTable(smsFieldList)).start();
//
//            }
//        }
//    }
    private void processFriendContactList() {
        try {
            if (js.getSuccess()) {
                //friendContactListSeqCount++;
                FriendlistMapping mapper = HelperMethods.friendlist_mapping(response);
                String friendId = js.getFriendIdentity();

                if (mapper.getContactList() != null) {
                    for (final UserBasicInfo singleProfile : mapper.getContactList()) {
                        if (MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId) == null) {
                            MyfnfHashMaps.getInstance().getFriendsContactList().put(friendId, new ConcurrentHashMap<String, UserBasicInfo>());
                        }
                        MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId).put(singleProfile.getUserIdentity(), singleProfile);
                        if (MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null) {
                            MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendContactListPane.addSingleFriendFromServer(singleProfile);
                        }
                    }
                }


                /*int totalSeq = 0;
                 if (js.getSeq() != null && js.getSeq().contains("/")) {
                 totalSeq = Integer.valueOf(js.getSeq().split("/")[1]);
                 }

                 if (friendContactListSeqCount >= totalSeq) {
                 if (MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId) == null) {
                 MyfnfHashMaps.getInstance().getFriendsContactList().put(friendId, new ConcurrentHashMap<String, UserBasicInfo>());
                 } else {
                 MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId).clear();
                 }

                 for (Entry entry : FriendList.getInstance().getTempFriendContactList().entrySet()) {
                 UserBasicInfo data = (UserBasicInfo) entry.getValue();
                 MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId).put(data.getUserIdentity(), data);
                 }

                 MyfnfHashMaps.getInstance().getFriendsContactList().get(friendId).remove(MyFnFSettings.LOGIN_USER_ID);

                 FriendList.getInstance().getTempFriendContactList().clear();
                 if (MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null) {
                 MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendContactListPane.buildFriendList();
                 }
                 }*/
            } else {
                String friendId = js.getFriendIdentity();
                if (MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId) != null) {
                    MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendContactListPane.buildFriendList();
                    MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId).myFriendContactListPane.removeLoadingLabel();
                }
            }
        } catch (Exception e) {
        }
    }

    private void processMyNotifications() {
        if (js.getSuccess()) {
            List<JsonFields> notificationList = null;
            try {
                JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
                MyfnfHashMaps.getInstance().getTempNotifications().clear();
                for (JsonFields entry : HelperMethods.mapNotificationMap(response).getnList()) {
                    entry.setIsRead(false);
                    MyfnfHashMaps.getInstance().getTempNotifications().put(entry.getId(), entry);
                }

                if (MyfnfHashMaps.getInstance().getTempNotifications().size() > 0) {
                    notificationList = new ArrayList<JsonFields>();
                    for (Entry<String, JsonFields> entry : MyfnfHashMaps.getInstance().getTempNotifications().entrySet()) {

                        JsonFields jsonFields = entry.getValue();
                        notificationList.add(jsonFields);
                        MyfnfHashMaps.getInstance().getNotificationsMap().put(entry.getKey(), entry.getValue());
                    }
                    
                    new InsertIntoNotificationHistoryTable(notificationList).start();
                }

                if (GuiRingID.getInstance().getMainButtonsPanel().allNotification.getText().equals("")) {
                    GuiRingID.getInstance().getMainButtonsPanel().addNotification(onlyFeed.getTn());
                }

//                if (GuiRingID.getInstance().getMainLeftContainer().notificationHistoryContainer == null) {
//                    GuiRingID.getInstance().getMainButtonsPanel().addNotification(onlyFeed.getTn());
//                }
                if (MyfnfHashMaps.getInstance().getTempNotifications().size() >= 0
                        && GuiRingID.getInstance().getMainLeftContainer().notificationHistoryContainer != null) {
                    //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().totalNotifCount = GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().totalNotifCount + MyfnfHashMaps.getInstance().getTempNotifications().size();
                    //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().isLoading = false;
                    GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().removeLoadingcontent();
                    //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().addAllNotifications(notificationList, GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().totalNotifCount, false);
                    GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().addMoreContetns(MyfnfHashMaps.getInstance().getTempNotifications());
                }

//                if (GuiRingID.getInstance().getTopMenuBar().notificationDialog == null) {
//                    GuiRingID.getInstance().getTopMenuBar().addNotification(onlyFeed.getTn());
//                }
//                if (MyfnfHashMaps.getInstance().getTempNotifications().size() >= 0
//                        && getTopMenuBar().notificationDialog != null
//                        && getTopMenuBar().notificationDialog.isVisible()) {
//                    getTopMenuBar().notificationDialog.isLoading = false;
//                    getTopMenuBar().notificationDialog.removeLoadingcontent();
//                    getTopMenuBar().notificationDialog.addMoreContetns(MyfnfHashMaps.getInstance().getTempNotifications());
//                }
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("processMyNotifications ex==>" + response);
            }
        } else {

            if (GuiRingID.getInstance().getMainLeftContainer().notificationHistoryContainer != null && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().isDisplayable()) {
                GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().isLoading = true;
                GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().removeLoadingcontent();
            }

//            if (getTopMenuBar().notificationDialog != null
//                    && getTopMenuBar().notificationDialog.isVisible()) {
//                getTopMenuBar().notificationDialog.isLoading = true;
//                getTopMenuBar().notificationDialog.removeLoadingcontent();
//            }
        }
    }

    private void processSingleNotification() {
        try {
            JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
            if (onlyFeed.getId() != null) {
                onlyFeed.setIsRead(false);
                MyfnfHashMaps.getInstance().getNotificationsMap().put(onlyFeed.getId(), onlyFeed);

                List<JsonFields> notificationList = new ArrayList<JsonFields>();
                notificationList.add(onlyFeed);

                new InsertIntoNotificationHistoryTable(notificationList).start();

                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer() != null) {
                    GuiRingID.getInstance().getMainButtonsPanel().IncreaseNotification();

                    GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().notificationScrollPanel.getVerticalScrollBar().setValue(0);
                    GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().addSingleNotification(notificationList);

//                    if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().allNotificationPanel != null) {
//                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().allNotificationPanel.addAllNotifications();


//                    if (GuiRingID.getInstance().getTopMenuBar().allNotificationPanel != null) {
//                        GuiRingID.getInstance().getTopMenuBar().allNotificationPanel.addAllNotifications();

//                        //GuiRingID.getInstance().getTopMenuBar().allNotificationPanel.addSingleNotification(onlyFeed);
//                    }
                }

//                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getTopMenuBar() != null) {
//                    GuiRingID.getInstance().getTopMenuBar().IncreaseNotification();
//                    if (GuiRingID.getInstance().getTopMenuBar().allNotificationPanel != null) {
//                        GuiRingID.getInstance().getTopMenuBar().allNotificationPanel.addAllNotifications();
//                        //GuiRingID.getInstance().getTopMenuBar().allNotificationPanel.addSingleNotification(onlyFeed);
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("processAddAddressBookFriend==>" + response);
        }

    }

    private void processSingleStatusNotificationPanel() {
        try {
            NewsFeedMap tamp_list = HelperMethods.mapNewsFeed(response);
            if (tamp_list != null && tamp_list.getNewsFeedList() != null) {
                for (final NewsFeedWithMultipleImage singleNews : tamp_list.getNewsFeedList()) {
                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(singleNews.getNfId(), singleNews);

                    if (singleNews.getWhoShare() != null && singleNews.getWhoShare().size() > 0) {
                        for (SingleBookDetails sbd : singleNews.getWhoShare()) {
                            NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), singleNews.getNfId());
                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                            //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                            if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()) != null) {
                                NewsFeedMaps.getInstance().getShareNewsFeedId().get(singleNews.getNfId()).insertData(sbd.getNfId(), sbd.getTm());
                            } else {
                                SortedArrayList sortedArrayList = new SortedArrayList();
                                sortedArrayList.insertData(sbd.getNfId(), sbd.getTm());
                                NewsFeedMaps.getInstance().getShareNewsFeedId().put(singleNews.getNfId(), sortedArrayList);
                            }
                        }
                    }

                    Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

                    SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                    SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                    SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                    SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                    SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                    if (parentNfId != null) {
                        SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                        SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                        SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                        SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                        SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                        if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                        }
                    } else {
                        singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                        singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                        singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                        singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                        singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
                    }

                    if (singleStatusPanelInHome != null) {
                        singleStatusPanelInHome.statusDto = singleNews;
                    }
                    if (singleStatusPanelInMyBook != null) {
                        singleStatusPanelInMyBook.statusDto = singleNews;
                    }
                    if (singleStatusPanelInFriendNewsFeed != null) {
                        singleStatusPanelInFriendNewsFeed.statusDto = singleNews;
                    }
                    if (singleStatusPanelInCircleNewsFeed != null) {
                        singleStatusPanelInCircleNewsFeed.statusDto = singleNews;
                    }
                    if (singleStatusPanelInNotification != null) {
                        singleStatusPanelInNotification.statusDto = singleNews;
                    }
                    if (shareFeedPanelInHome != null) {
                        shareFeedPanelInHome.statusDto = singleNews;
                    }
                    if (shareFeedPanelInMyBook != null) {
                        shareFeedPanelInMyBook.statusDto = singleNews;
                    }
                    if (shareFeedPanelInFriendNewsFeed != null) {
                        shareFeedPanelInFriendNewsFeed.statusDto = singleNews;
                    }
                    if (shareFeedPanelInCircleNewsFeed != null) {
                        shareFeedPanelInCircleNewsFeed.statusDto = singleNews;
                    }
                    if (shareFeedPanelInNotification != null) {
                        shareFeedPanelInNotification.statusDto = singleNews;
                    }
                }
            }

        } catch (Exception e) {
            log.error("processSingleStatusNotificationPanel==>" + response);
        }

    }

    private void processFriendsAlbums() {
        try {
            if (js.getSuccess() && js.getSeq() != null) {
                AlbumMap mapper = HelperMethods.mapAlbumMap(response);
                NewsFeedMaps.getInstance().getTempFriendsAllbums().put(mapper.getSeq(), mapper);

                int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);

                String friendId = js.getFriendIdentity();

                if (NewsFeedMaps.getInstance().getFriendsAllbums().get(friendId) == null) {
                    NewsFeedMaps.getInstance().getFriendsAllbums().put(friendId, new ConcurrentHashMap<String, JsonFields>());
                }

                for (Entry entry : NewsFeedMaps.getInstance().getTempFriendsAllbums().entrySet()) {
                    AlbumMap map = (AlbumMap) entry.getValue();
                    for (JsonFields entity : map.getAlbumList()) {
                        NewsFeedMaps.getInstance().getFriendsAllbums().get(friendId).put(entity.getAlbId(), entity);
                    }
                }

                if (!NewsFeedMaps.getInstance().getTempFriendsAllbums().isEmpty() && NewsFeedMaps.getInstance().getTempFriendsAllbums().size() == seqTotal) {

                    CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(Long.valueOf(friendId));
                    if (circleDetails != null) {
                        circleDetails.getCircleAlbumPanel().buildAlbumsFolders();
                    }
                    MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId);
                    if (myFriendProfile != null) {
                        myFriendProfile.myFriendAlbumPanel.addFriendAlbumFolders();
                        myFriendProfile.myFriendAlbumPanel.isLoading = false;
                    }
                    NewsFeedMaps.getInstance().getTempFriendsAllbums().clear();
                }
            } else {
                CircleProfile circleDetails = MyfnfHashMaps.getInstance().getCircleProfiles().get(Long.valueOf(js.getFriendIdentity()));
                if (circleDetails != null) {
                    circleDetails.getCircleAlbumPanel().buildAlbumsFolders();
                }
                MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getFriendIdentity());
                if (myFriendProfile != null) {
                    myFriendProfile.myFriendAlbumPanel.addFriendAlbumFolders();
                    myFriendProfile.myFriendAlbumPanel.isLoading = false;
                }
            }
        } catch (Exception e) {
            log.error("processFriendsAlbums==>" + response);
        }
    }

    private void processFriendsAlbumImages() {
        try {
            if (js.getSuccess() && js.getSeq() != null) {
                AlbumImageMap mapper = HelperMethods.albumImageMap(response);
                NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().put(mapper.getSeq(), mapper);
                JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
                int seqTotal = Integer.parseInt(js.getSeq().split("/")[1]);

                String friendId = js.getFriendIdentity();
                String albumId = onlyFeed.getAlbId();
                if (albumId != null) {
                    if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId) == null) {
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().put(friendId, new ConcurrentHashMap<String, Map<String, JsonFields>>());
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).put(albumId, new ConcurrentHashMap<String, JsonFields>());
                    } else if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).get(albumId) == null) {
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).put(albumId, new ConcurrentHashMap<String, JsonFields>());
                    }
                } else {
                    if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId) == null) {
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().put(friendId, new ConcurrentHashMap<String, Map<String, JsonFields>>());
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).put("0", new ConcurrentHashMap<String, JsonFields>());
                    } else if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).get("0") == null) {
                        NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).put("0", new ConcurrentHashMap<String, JsonFields>());
                    }
                }

                for (Entry entry : NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().entrySet()) {
                    AlbumImageMap map = (AlbumImageMap) entry.getValue();
                    for (JsonFields entity : map.getImageList()) {
                        if (albumId != null) {
                            NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).get(albumId).put(entity.getIurl(), entity);
                        } else {
                            NewsFeedMaps.getInstance().getFriendsAlbumImages().get(friendId).get("0").put(entity.getIurl(), entity);
                        }
                    }
                }
                if (!NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().isEmpty() && NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().size() == seqTotal) {
                    /*long circleId = 0;
                     try {
                     circleId = Long.parseLong(friendId);
                     } catch (Exception ex) {
                     }*/
                    int i = 0;
                    for (Entry entry : NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().entrySet()) {
                        i++;
                        AlbumImageMap map = (AlbumImageMap) entry.getValue();
                        //  if (circleId > 0) {
                        if (ImagesInAlbumFolderCircle.getInstance() != null && ImagesInAlbumFolderCircle.getInstance().isLoading == true) {
                            ImagesInAlbumFolderCircle.getInstance().removeLoadingcontent();
                            ImagesInAlbumFolderCircle.getInstance().addMoreImages(map.getImageList());
                        }
                        //  } else {

                        MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId);
                        if (myFriendProfile != null && myFriendProfile.myFriendAlbumPanel != null && myFriendProfile.myFriendAlbumPanel.clicked_type == MyFriendAlbumPanel.TYPE_PHOTOS && myFriendProfile.myFriendAlbumPanel.isLoading == true) {
                            myFriendProfile.myFriendAlbumPanel.removeLoadingcontent();
                            myFriendProfile.myFriendAlbumPanel.addMoreFriendImages(map.getImageList());
                            if (i == seqTotal) {
                                myFriendProfile.myFriendAlbumPanel.isLoading = false;
                            }
                        }

                        if (ImagesInAlbumFolderFriend.getInstance() != null && ImagesInAlbumFolderFriend.getInstance().isLoading == true) {
                            ImagesInAlbumFolderFriend.getInstance().removeLoadingcontent();
                            ImagesInAlbumFolderFriend.getInstance().addMoreImages(map.getImageList());
                        }

                        if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                                && SingleImageDetailsProfileCover.getSingleImgeDetails().isVisible()
                                && i == seqTotal) {
                            SingleImageDetailsProfileCover.getSingleImgeDetails().setPreviousNextImages();
                        }

                        //  }
                    }
                    NewsFeedMaps.getInstance().getTempFriendsAllbumsImages().clear();
                }
            } else {

                /*long circleId = 0;
                 try {
                 circleId = Long.parseLong(js.getFriendId());
                 } catch (Exception ex) {
                 }*/
                //   if (circleId > 0) {
                if (ImagesInAlbumFolderCircle.getInstance() != null) {
                    ImagesInAlbumFolderCircle.getInstance().removeLoadingcontent();
                    ImagesInAlbumFolderCircle.getInstance().isLoading = false;
                }
                //  } else {
                MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getFriendIdentity());
                if (myFriendProfile != null && myFriendProfile.myFriendAlbumPanel != null) {
                    myFriendProfile.myFriendAlbumPanel.removeLoadingcontent();
                    myFriendProfile.myFriendAlbumPanel.isLoading = false;
                }
                if (ImagesInAlbumFolderFriend.getInstance() != null) {
                    ImagesInAlbumFolderFriend.getInstance().removeLoadingcontent();
                    ImagesInAlbumFolderFriend.getInstance().isLoading = false;
                }
            }

            //  }
        } catch (Exception e) {
            log.error("processFriendsAlbumImages==>" + response);
        }

    }

    private void processUpdateDeactivateAccount() {
        if (js.getFriendIdentity() != null) {
            try {
                if (js.getFriendIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    DeactivateAccountDAO.trancatUserInfo();
                    GuiRingID.getInstance().signoutActions(false);
                } else {
                    UserBasicInfo frienBasicInfo = FriendList.getInstance().getFriend_hash_map().get(js.getFriendIdentity());
                    FriendList.getInstance().getFriend_hash_map().remove(js.getFriendIdentity());
                    (new DeleteFromContactListTable(js.getFriendIdentity())).start();
                    //AcceptedFriendList.getInstance().buildFriendList();
                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                    if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().remove_content(js.getFriendIdentity());
                        }
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().remove_content(js.getFriendIdentity());
                        }
                    }
                    MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(js.getFriendIdentity());
                    MyFriendChatCallPanel myFriendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(js.getFriendIdentity());
                    if ((myFriendProfile != null && myFriendProfile.isDisplayable() && myFriendProfile.isVisible())
                            || (myFriendChatCallPanel != null && myFriendChatCallPanel.isDisplayable() && myFriendChatCallPanel.isVisible())) {
                        GuiRingID.getInstance().showUnknownProfile(frienBasicInfo);
                    }
//                    if (MyFriendProfile.getSeleected_user_identiy() != null && MyFriendProfile.getSeleected_user_identiy().equals(js.getFriendIdentity())) {
//                        GuiRingID.getInstance().showUnknownProfile(frienBasicInfo);
//                    }
                }
            } catch (Exception ee) {
                log.error("Deactivate Account = " + ee.getMessage());
                System.exit(0);
            }
        }
    }

    private void processUpdateAddStatus() {
        try {
            /*
             {"
             * actn":377,"stId":5260,"uId":"ring8801728119927",
             * "iurl":"http://38.108.92.154/auth/clients_image/ring8801728119927/profileimages/1408514654004.png",
             * "albId":"profileimages",
             * "albn":"Profile Images",
             * "tm":1408514654321,
             * "imageType":1,"sucs":true,"pckFs":17448,"fn":"Faiz",
             * "ln":"Ahmed","ih":124,"iw":300,"imT":2,"imgId":3279,
             * "pst":1}
             *              * *************
             * {"actn":377,
             * "stId":5271,
             * "uId":"ring8801911722319",
             * "sts":"asd",
             * "tm":1408517603243,
             * "imageType":1,
             * "sucs":true,
             * "pckFs":26658,
             * "fn":"Wasif",
             * "ln":"Islam",
             * "imageList":[
             * {"imgId":3306,"iurl":"http://38.108.92.154/auth/clients_image/ring8801911722319/default/1408517602442.jpg","cptn":"","ih":183,"iw":300,"imT":1}]}
             */
            ShareFeedResponse shareFeedResponse = HelperMethods.getShareFeedResponse(response);
            NewsFeedWithMultipleImage newsFeeds = null;
            if (shareFeedResponse.getSharedFeed() != null && shareFeedResponse.getSharedFeed().getNfId() != null && shareFeedResponse.getSharedFeed().getNfId() > 0) {
                newsFeeds = shareFeedResponse.getSharedFeed();
            } else {
                newsFeeds = HelperMethods.getNewsFeedWithMultipleImageDto(response);
            }

            newsFeeds.setNc(0L);
            newsFeeds.setNl(0L);
            newsFeeds.setIl((short) 0);
            if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId()) == null) {
                if (newsFeeds.getImageList() == null && newsFeeds.getIurl() != null) {
                    ArrayList<JsonFields> imageList = new ArrayList<JsonFields>();
                    JsonFields singleImag = (JsonFields) newsFeeds;
                    imageList.add(singleImag);
                    newsFeeds.setImageList(imageList);
                }
                NewsFeedMaps.getInstance().getAllNewsFeeds().put(newsFeeds.getNfId(), newsFeeds);
            } else if (newsFeeds.getWhoShare() != null && newsFeeds.getWhoShare().size() > 0) {
                NewsFeedWithMultipleImage nfs = NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId());
                nfs.setWhoShare(newsFeeds.getWhoShare());
                nfs.setTs(newsFeeds.getTs());
                nfs.setNs(newsFeeds.getNs());
            }
            NewsFeedMaps.getInstance().getAllNewfeedId().insertData(newsFeeds.getNfId(), newsFeeds.getTm());

            if (newsFeeds.getUserIdentity() != null && newsFeeds.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(newsFeeds.getNfId(), newsFeeds.getTm());
            }

            if (newsFeeds.getFriendIdentity() != null && !newsFeeds.getFriendIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(newsFeeds.getFriendIdentity()) != null) {
                    NewsFeedMaps.getInstance().getFriendNewsFeedId().get(newsFeeds.getFriendIdentity()).insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                } else {
                    SortedArrayList newfeed = new SortedArrayList();
                    newfeed.insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                    NewsFeedMaps.getInstance().getFriendNewsFeedId().put(newsFeeds.getFriendIdentity(), newfeed);
                }
            } else {
                if (newsFeeds.getUserIdentity() != null && !newsFeeds.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(newsFeeds.getUserIdentity()) != null) {
                        NewsFeedMaps.getInstance().getFriendNewsFeedId().get(newsFeeds.getUserIdentity()).insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                    } else {
                        SortedArrayList newfeed = new SortedArrayList();
                        newfeed.insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                        NewsFeedMaps.getInstance().getFriendNewsFeedId().put(newsFeeds.getUserIdentity(), newfeed);
                    }
                }
            }

            if (newsFeeds.getCircleId() != null && newsFeeds.getCircleId() > 0 && MyfnfHashMaps.getInstance().getCircleLists().containsKey(newsFeeds.getCircleId())) {
                if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(newsFeeds.getCircleId()) != null) {
                    NewsFeedMaps.getInstance().getCircleNewsFeedId().get(newsFeeds.getCircleId()).insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                } else {
                    SortedArrayList newfeed = new SortedArrayList();
                    newfeed.insertData(newsFeeds.getNfId(), newsFeeds.getTm());
                    NewsFeedMaps.getInstance().getCircleNewsFeedId().put(newsFeeds.getCircleId(), newfeed);
                }
            }

            if (newsFeeds.getWhoShare() != null && newsFeeds.getWhoShare().size() > 0) {
                for (SingleBookDetails sbd : newsFeeds.getWhoShare()) {
                    NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), newsFeeds.getNfId());
                    if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(sbd.getNfId()) == null) {
                        NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                    }
                    //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                    if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(newsFeeds.getNfId()) != null) {
                        NewsFeedMaps.getInstance().getShareNewsFeedId().get(newsFeeds.getNfId()).insertData(sbd.getNfId(), sbd.getTm());
                    } else {
                        SortedArrayList sortedArrayList = new SortedArrayList();
                        sortedArrayList.insertData(sbd.getNfId(), sbd.getTm());
                        NewsFeedMaps.getInstance().getShareNewsFeedId().put(newsFeeds.getNfId(), sortedArrayList);
                    }
                }
            }

//            System.out.println("BookHome.getBookHome().isDisplayable()=="+BookHome.getBookHome().isDisplayable());
//            if (BookHome.bookHome != null && (BookHome.getBookHome().scrollContent.getVerticalScrollBar().getValue() < (BookHome.getBookHome().postToFeeds.getHeight() + 32)||BookHome.getBookHome().isDisplayable())) {
//                BookHome.getBookHome().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId()));
//            } else {
//                BookHome.addUnreadNewsFeeds(newsFeeds.getNfId());
//            }
//            if (AllFeedsView.bookHome != null) {
//
//                if (!AllFeedsView.getBookHome().isDisplayable() || AllFeedsView.getBookHome().scrollContent.getVerticalScrollBar().getValue() > (AllFeedsView.getBookHome().postToFeeds.getHeight() + 32)) {
//                    AllFeedsView.addUnreadNewsFeeds(newsFeeds.getNfId());
//                } else {
//                    AllFeedsView.getBookHome().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId()));
//                }
//
//            }
            if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getAllFeedsView() != null) {

                if (!GuiRingID.getInstance().getMainRight().getAllFeedsView().isDisplayable() || GuiRingID.getInstance().getMainRight().getAllFeedsView().scrollContent.getVerticalScrollBar().getValue() > (GuiRingID.getInstance().getMainRight().getAllFeedsView().postToFeeds.getHeight() + 32)) {
                    AllFeedsView.addUnreadNewsFeeds(newsFeeds.getNfId());
                } else {
                    GuiRingID.getInstance().getMainRight().getAllFeedsView().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId()));
                }

            }
            if (newsFeeds.getUserIdentity() != null && newsFeeds.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                if (getMainRight().getMyProfilePanel().getMyFeeds() != null
                        && getMainRight().getMyProfilePanel().getMyFeeds().scrollContent.getVerticalScrollBar().getValue() < (getMainRight().getMyProfilePanel().getMyFeeds().postToFeeds.getHeight() + 32)) {
                    getMainRight().getMyProfilePanel().getMyFeeds().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(newsFeeds.getNfId()));
                }
            }

            if (newsFeeds.getFriendIdentity() != null && newsFeeds.getFriendIdentity().length() > 0 && !newsFeeds.getFriendIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(newsFeeds.getFriendIdentity());
                if (myFriendProfile != null
                        && myFriendProfile.myFriendNewsFeedPanel != null
                        && myFriendProfile.myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().getValue() < (myFriendProfile.myFriendNewsFeedPanel.postToFeeds.getHeight() + 32)) {
                    myFriendProfile.myFriendNewsFeedPanel.addSingleBookPost(newsFeeds);
                } else {
                    MyFriendNewsFeedPanel.addUnreadNewsFeeds(newsFeeds.getNfId(), newsFeeds.getFriendIdentity());
                }
            } else {
                if (newsFeeds.getUserIdentity() != null && newsFeeds.getUserIdentity().length() > 0 && !newsFeeds.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(newsFeeds.getUserIdentity());
                    if (myFriendProfile != null
                            && myFriendProfile.myFriendNewsFeedPanel != null
                            && myFriendProfile.myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().getValue() < (myFriendProfile.myFriendNewsFeedPanel.postToFeeds.getHeight() + 32)) {
                        myFriendProfile.myFriendNewsFeedPanel.addSingleBookPost(newsFeeds);
                    } else {
                        MyFriendNewsFeedPanel.addUnreadNewsFeeds(newsFeeds.getNfId(), newsFeeds.getUserIdentity());
                    }
                }
            }

            if (newsFeeds.getCircleId() != null && newsFeeds.getCircleId() > 0 && MyfnfHashMaps.getInstance().getCircleLists().containsKey(newsFeeds.getCircleId())) {
                CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(newsFeeds.getCircleId());
                if (circleProfile != null
                        && circleProfile.isDisplayable()
                        && circleProfile.getCircleNewsFeedPanel() != null
                        && circleProfile.getCircleNewsFeedPanel().scrollContent.getVerticalScrollBar().getValue() < (circleProfile.getCircleNewsFeedPanel().postToFeeds.getHeight() + 32)) {
                    circleProfile.getCircleNewsFeedPanel().addSingleBookPost(newsFeeds);
                } else {
                    addUnreadNewsFeeds(newsFeeds.getNfId(), newsFeeds.getCircleId());
                }
            }

        } catch (Exception e) {
        }
    }

    private void processUpdateEditStatus() {
        //{"actn":378,"nfId":5721,"sts":"asadsd Fsdfdsfsd","sucs":true,"pckFs":15024,"fn":"Shahadat","ln":"Hossain","lctn":""}
        try {
            if (js.getSuccess()) {
                JsonFields newfFeedWithMultipleImage = HelperMethods.getJsonFields(response);
                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId()) != null) {
                    NewsFeedWithMultipleImage editedStatus = NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId());
                    editedStatus.setStatus(newfFeedWithMultipleImage.getStatus());
                    editedStatus.setCptn(newfFeedWithMultipleImage.getCptn());

                    Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

                    SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                    SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                    SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                    SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                    SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                    SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                    if (parentNfId != null) {
                        SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                        SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                        SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                        SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                        SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                        if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                        }
                        if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                            shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                        }
                    } else {
                        singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                        singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                        singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                        singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                        singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
                    }

                    if (singleStatusPanelInHome != null) {
                        singleStatusPanelInHome.addStatusAndLocationInfor();
                    }
                    if (singleStatusPanelInMyBook != null) {
                        singleStatusPanelInMyBook.addStatusAndLocationInfor();
                    }
                    if (singleStatusPanelInFriendNewsFeed != null) {
                        singleStatusPanelInFriendNewsFeed.addStatusAndLocationInfor();
                    }
                    if (singleStatusPanelInCircleNewsFeed != null) {
                        singleStatusPanelInCircleNewsFeed.addStatusAndLocationInfor();
                    }
                    if (singleStatusPanelInNotification != null) {
                        singleStatusPanelInNotification.addStatusAndLocationInfor();
                    }
                    if (shareFeedPanelInHome != null) {
                        shareFeedPanelInHome.addStatusAndLocationInfor();
                    }
                    if (shareFeedPanelInMyBook != null) {
                        shareFeedPanelInMyBook.addStatusAndLocationInfor();
                    }
                    if (shareFeedPanelInFriendNewsFeed != null) {
                        shareFeedPanelInFriendNewsFeed.addStatusAndLocationInfor();
                    }
                    if (shareFeedPanelInCircleNewsFeed != null) {
                        shareFeedPanelInCircleNewsFeed.addStatusAndLocationInfor();
                    }
                    if (shareFeedPanelInNotification != null) {
                        shareFeedPanelInNotification.addStatusAndLocationInfor();
                    }

                }
            }
        } catch (Exception e) {
            log.error("processUpdateEditStatus==>" + response);
        }
    }

    private void processUpdateDeleteStatus() {
        try {
            NewsFeedMaps.getInstance().getAllNewsFeeds().remove(js.getNfId());
            NewsFeedMaps.getInstance().getAllNewfeedId().removeData(js.getNfId());
            NewsFeedMaps.getInstance().getComments().remove(js.getNfId());
            NewsFeedMaps.getInstance().getMyNewsFeedId().removeData(js.getNfId());

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            if (parentNfId != null) {
                NewsFeedWithMultipleImage parentNewsFeed = NewsFeedMaps.getInstance().getAllNewsFeeds().get(parentNfId);
                if (parentNewsFeed != null && parentNewsFeed.getWhoShare() != null) {
                    for (int j = 0; j < parentNewsFeed.getWhoShare().size(); j++) {
                        SingleBookDetails sharedFeed = parentNewsFeed.getWhoShare().get(j);
                        if (sharedFeed.getNfId().equals(js.getNfId())) {
                            parentNewsFeed.getWhoShare().remove(sharedFeed);
                            parentNewsFeed.setTs(parentNewsFeed.getTs() - 1);
                            parentNewsFeed.setNs(parentNewsFeed.getNs() - 1);
                            break;
                        }
                    }
                }
            }

            if (js.getFriendIdentity() != null && NewsFeedMaps.getInstance().getFriendNewsFeedId().get(js.getFriendIdentity()) != null) {
                NewsFeedMaps.getInstance().getFriendNewsFeedId().get(js.getFriendIdentity()).removeData(js.getNfId());
            }

            if (js.getCircleId() != null && NewsFeedMaps.getInstance().getCircleNewsFeedId().get(js.getCircleId()) != null) {
                NewsFeedMaps.getInstance().getCircleNewsFeedId().get(js.getCircleId()).removeData(js.getNfId());
            }

            if (parentNfId != null && !NewsFeedMaps.getInstance().getShareNewsFeedId().isEmpty() && NewsFeedMaps.getInstance().getShareNewsFeedId().get(parentNfId) != null) {
                NewsFeedMaps.getInstance().getShareNewsFeedId().get(parentNfId).removeData(js.getNfId());
            }

            if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getAllFeedsView() != null) {
                GuiRingID.getInstance().getMainRight().getAllFeedsView().removeIsAlreadyExist(js.getNfId());
            }

            SingleStatusPanelInFriendNewsFeed statusFriendNewsFeedPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
            if (statusFriendNewsFeedPanel != null && statusFriendNewsFeedPanel.friendIdentity != null) {
                MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(statusFriendNewsFeedPanel.friendIdentity);
                if (myFriendProfile != null && myFriendProfile.myFriendNewsFeedPanel != null) {
                    myFriendProfile.myFriendNewsFeedPanel.removeIsAlreadyExist(js.getNfId());
                }
            }

            SingleStatusPanelInCircleNewsFeed statusCircleNewsFeedPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
            if (statusCircleNewsFeedPanel != null && statusCircleNewsFeedPanel.circleId != null) {
                CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(statusCircleNewsFeedPanel.circleId);
                if (circleProfile != null && circleProfile.getCircleNewsFeedPanel() != null) {
                    circleProfile.getCircleNewsFeedPanel().removeIsAlreadyExist(js.getNfId());
                }
            }

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null) {
                    panelInHome.removeAShareFeed(js.getNfId());
                }
                if (panelInMyBook != null) {
                    panelInMyBook.removeAShareFeed(js.getNfId());
                }
                if (panelInFriendNewsFeed != null) {
                    panelInFriendNewsFeed.removeAShareFeed(js.getNfId());
                }
                if (panelInCircleNewsFeed != null) {
                    panelInCircleNewsFeed.removeAShareFeed(js.getNfId());
                }
                if (panelInNotification != null) {
                    panelInNotification.removeAShareFeed(js.getNfId());
                }
                NewsFeedMaps.getInstance().getParentFeedMap().remove(js.getNfId());
            }
        } catch (Exception e) {
            log.error("processUpdateDeleteStatus==>" + response);
        }
    }

    private void processUpdateLikeStatus() {
        try {
            SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
            if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
                UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
                singleFriend.setUserIdentity(js.getUserIdentity());
                singleFriend.setFullName(bac.getFullName());
                //singleFriend.setLastName(bac.getLastName());
                singleFriend.setProfileImage(bac.getProfileImage());
            } else {
                singleFriend.setUserIdentity(js.getUserIdentity());
            }
            if (NewsFeedMaps.getInstance().getLikes().get(js.getNfId().toString()) == null) {
                Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                commentsJson.put(js.getUserIdentity(), singleFriend);
                NewsFeedMaps.getInstance().getLikes().put(js.getNfId().toString(), commentsJson);
            } else {
                NewsFeedMaps.getInstance().getLikes().get(js.getNfId().toString()).put(js.getUserIdentity(), singleFriend);
            }

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId());
            if (newsFeeds != null) {
                if (js.getNl() == null) {
                    Long nl = newsFeeds.getNl();
                    Long likes = (nl != null ? nl : 0) + 1L;
                    newsFeeds.setNl(likes);
                } else {
                    newsFeeds.setNl(js.getNl() + 1);
                }
            }

            if (singleStatusPanelInHome != null) {
                singleStatusPanelInHome.change_like_number();
            }

            if (singleStatusPanelInMyBook != null) {
                singleStatusPanelInMyBook.change_like_number();
            }

            if (singleStatusPanelInFriendNewsFeed != null) {
                singleStatusPanelInFriendNewsFeed.change_like_number();
            }

            if (singleStatusPanelInCircleNewsFeed != null) {
                singleStatusPanelInCircleNewsFeed.change_like_number();
            }

            if (singleStatusPanelInNotification != null) {
                singleStatusPanelInNotification.change_like_number();
            }

            if (shareFeedPanelInHome != null) {
                shareFeedPanelInHome.change_like_number();
            }

            if (shareFeedPanelInMyBook != null) {
                shareFeedPanelInMyBook.change_like_number();
            }

            if (shareFeedPanelInFriendNewsFeed != null) {
                shareFeedPanelInFriendNewsFeed.change_like_number();
            }

            if (shareFeedPanelInCircleNewsFeed != null) {
                shareFeedPanelInCircleNewsFeed.change_like_number();
            }

            if (shareFeedPanelInNotification != null) {
                shareFeedPanelInNotification.change_like_number();
            }

        } catch (Exception e) {
            log.error("processUpdateLikeStatus==>" + response);
        }
    }

    private void processUpdateUnlikeStatus() {
        try {
            if (NewsFeedMaps.getInstance().getLikes().get(js.getNfId().toString()) != null) {
                NewsFeedMaps.getInstance().getLikes().get(js.getNfId().toString()).remove(js.getUserIdentity());
            }

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId());
            boolean hasactivity = false;
            if (newsFeeds != null) {
                Long nl = newsFeeds.getNl();
                Long likes = (nl != null && nl > 0 ? nl - 1L : nl);
                newsFeeds.setNl(likes);
                if (js.getAuId() != null && newsFeeds.getAuId() != null && js.getAuId().equals(newsFeeds.getAuId())) {
                    hasactivity = true;
                    newsFeeds.setActvt(null);
                    if (js.getUt() != null) {
                        newsFeeds.setUt(js.getUt());
                    }
                }

            }

            if (singleStatusPanelInHome != null) {
                singleStatusPanelInHome.change_like_number();
                if (hasactivity) {
                    singleStatusPanelInHome.addOrChangeActivityInfo();
                }
            }

            if (singleStatusPanelInMyBook != null) {
                singleStatusPanelInMyBook.change_like_number();
                if (hasactivity) {
                    singleStatusPanelInMyBook.addOrChangeActivityInfo();
                }
            }

            if (singleStatusPanelInFriendNewsFeed != null) {
                singleStatusPanelInFriendNewsFeed.change_like_number();
                if (hasactivity) {
                    singleStatusPanelInFriendNewsFeed.addOrChangeActivityInfo();
                }
            }

            if (singleStatusPanelInCircleNewsFeed != null) {
                singleStatusPanelInCircleNewsFeed.change_like_number();
                if (hasactivity) {
                    singleStatusPanelInCircleNewsFeed.addOrChangeActivityInfo();
                }
            }

            if (singleStatusPanelInNotification != null) {
                singleStatusPanelInNotification.change_like_number();
                if (hasactivity) {
                    singleStatusPanelInNotification.addOrChangeActivityInfo();
                }
            }

            if (shareFeedPanelInHome != null) {
                shareFeedPanelInHome.change_like_number();
                if (hasactivity) {
                    shareFeedPanelInHome.addOrChangeActivityInfo();
                }
            }

            if (shareFeedPanelInMyBook != null) {
                shareFeedPanelInMyBook.change_like_number();
                if (hasactivity) {
                    shareFeedPanelInMyBook.addOrChangeActivityInfo();
                }
            }

            if (shareFeedPanelInFriendNewsFeed != null) {
                shareFeedPanelInFriendNewsFeed.change_like_number();
                if (hasactivity) {
                    shareFeedPanelInFriendNewsFeed.addOrChangeActivityInfo();
                }
            }

            if (shareFeedPanelInCircleNewsFeed != null) {
                shareFeedPanelInCircleNewsFeed.change_like_number();
                if (hasactivity) {
                    shareFeedPanelInCircleNewsFeed.addOrChangeActivityInfo();
                }
            }

            if (shareFeedPanelInNotification != null) {
                shareFeedPanelInNotification.change_like_number();
                if (hasactivity) {
                    shareFeedPanelInNotification.addOrChangeActivityInfo();
                }
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("processUpdateUnlikeStatus ex==>" + response);
        }
    }

    private void processUpdateAddImageComment() {

        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel != null) {
                JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
                addImageCaptionCommentsPanel.getImageComments().put(js.getCmnId(), onlyFeed);
                if (addImageCaptionCommentsPanel.getComponentCount() > 0) {
                    addImageCaptionCommentsPanel.addAComment(onlyFeed);
                }
            }
        } catch (Exception e) {
            log.error("processUpdateAddImageComment==>" + response);
        }

    }

    private void processUpdateAddStatusComment() {
        try {
            //{"pckFs":30313,"sucs":true,"ln":"","tm":1419569473584,"nfId":1009,"cmnId":295,"nc":3,"uId":"2000002207","actn":381,"cmn":"56","fn":"Faiz Ahmed3"}
            JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
            if (NewsFeedMaps.getInstance().getComments().get(js.getNfId()) == null) {
                Map<String, JsonFields> commentsJson = new ConcurrentHashMap<String, JsonFields>();
                commentsJson.put(js.getCmnId(), onlyFeed);
                NewsFeedMaps.getInstance().getComments().put(js.getNfId(), commentsJson);
            } else {
                NewsFeedMaps.getInstance().getComments().get(js.getNfId()).put(js.getCmnId(), onlyFeed);
            }

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId());
            if (newsFeeds != null) {
                if (js.getNc() == null) {
                    Long nc = newsFeeds.getNc();
                    Long comments = (nc != null ? nc : 0) + 1L;
                    newsFeeds.setNc(comments);
                } else {
                    newsFeeds.setNc(js.getNc() + 1);
                }
            }

            if (singleStatusPanelInHome != null) {
                singleStatusPanelInHome.change_comments_number();
                if (singleStatusPanelInHome.commentsContiner.getComponentCount() > 0) {
                    singleStatusPanelInHome.repaintAllcomments();
                }
            }
            if (singleStatusPanelInMyBook != null) {
                singleStatusPanelInMyBook.change_comments_number();
                if (singleStatusPanelInMyBook.commentsContiner.getComponentCount() > 0) {
                    singleStatusPanelInMyBook.repaintAllcomments();
                }
            }
            if (singleStatusPanelInFriendNewsFeed != null) {
                singleStatusPanelInFriendNewsFeed.change_comments_number();
                if (singleStatusPanelInFriendNewsFeed.commentsContiner.getComponentCount() > 0) {
                    singleStatusPanelInFriendNewsFeed.repaintAllcomments();
                }
            }
            if (singleStatusPanelInCircleNewsFeed != null) {
                singleStatusPanelInCircleNewsFeed.change_comments_number();
                if (singleStatusPanelInCircleNewsFeed.commentsContiner.getComponentCount() > 0) {
                    singleStatusPanelInCircleNewsFeed.repaintAllcomments();
                }
            }
            if (singleStatusPanelInNotification != null) {
                singleStatusPanelInNotification.change_comments_number();
                if (singleStatusPanelInNotification.commentsContiner.getComponentCount() > 0) {
                    singleStatusPanelInNotification.repaintAllcomments();
                }
            }
            if (shareFeedPanelInHome != null) {
                shareFeedPanelInHome.change_comments_number();
                if (shareFeedPanelInHome.commentsContiner.getComponentCount() > 0) {
                    shareFeedPanelInHome.repaintAllcomments();
                }
            }
            if (shareFeedPanelInMyBook != null) {
                shareFeedPanelInMyBook.change_comments_number();
                if (shareFeedPanelInMyBook.commentsContiner.getComponentCount() > 0) {
                    shareFeedPanelInMyBook.repaintAllcomments();
                }
            }
            if (shareFeedPanelInFriendNewsFeed != null) {
                shareFeedPanelInFriendNewsFeed.change_comments_number();
                if (shareFeedPanelInFriendNewsFeed.commentsContiner.getComponentCount() > 0) {
                    shareFeedPanelInFriendNewsFeed.repaintAllcomments();
                }
            }
            if (shareFeedPanelInCircleNewsFeed != null) {
                shareFeedPanelInCircleNewsFeed.change_comments_number();
                if (shareFeedPanelInCircleNewsFeed.commentsContiner.getComponentCount() > 0) {
                    shareFeedPanelInCircleNewsFeed.repaintAllcomments();
                }
            }
            if (shareFeedPanelInNotification != null) {
                shareFeedPanelInNotification.change_comments_number();
                if (shareFeedPanelInNotification.commentsContiner.getComponentCount() > 0) {
                    shareFeedPanelInNotification.repaintAllcomments();
                }
            }
        } catch (UnknownException e) {
            log.error("processUpdateAddStatusComment==>" + response);
        }
    }

    private void processUpdateEditImageComment() {
        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel != null) {
                JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
                if (addImageCaptionCommentsPanel.getImageComments().get(js.getCmnId()) != null) {
                    addImageCaptionCommentsPanel.getImageComments().get(js.getCmnId()).setComment(onlyFeed.getComment());
                    if (addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId()) != null) {
                        addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId()).changeComment();
                    }
                }
            }
        } catch (Exception e) {
            log.error("processUpdateEditImageComment==>" + response);
        }
    }

    private void processUpdateEditComment() {
        //{"actn":389,"sucs":true,"stId":613,"cmnId":538,"tm":1404195674207,"uId":"ring8801617634317","pckFs":2486,"cmn":"Random comment 788 by ashraful","fn":"Ashraful","ln":"Alom(016)"}
        try {
            JsonFields onlyFeed = HelperMethods.getOnlyNewsFeedDto(response);
            if (NewsFeedMaps.getInstance().getComments().get(js.getNfId()) != null) {
                JsonFields updateOldComment = NewsFeedMaps.getInstance().getComments().get(js.getNfId()).get(js.getCmnId());
                updateOldComment.setComment(onlyFeed.getComment());

                String singleCommentId = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());

                Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

                SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                if (parentNfId != null) {
                    SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                    SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                    SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                    SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                    SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                    if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                    }
                    if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                        shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                    }
                } else {
                    singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                    singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                    singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                    singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                    singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
                }

                if (singleStatusPanelInHome != null) {
                    SingleCommentView singleCommentView = singleStatusPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (singleStatusPanelInMyBook != null) {
                    SingleCommentView singleCommentView = singleStatusPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (singleStatusPanelInFriendNewsFeed != null) {
                    SingleCommentView singleCommentView = singleStatusPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (singleStatusPanelInCircleNewsFeed != null) {
                    SingleCommentView singleCommentView = singleStatusPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (singleStatusPanelInNotification != null) {
                    SingleCommentView singleCommentView = singleStatusPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (shareFeedPanelInHome != null) {
                    SingleCommentView singleCommentView = shareFeedPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (shareFeedPanelInMyBook != null) {
                    SingleCommentView singleCommentView = shareFeedPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (shareFeedPanelInFriendNewsFeed != null) {
                    SingleCommentView singleCommentView = shareFeedPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (shareFeedPanelInCircleNewsFeed != null) {
                    SingleCommentView singleCommentView = shareFeedPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
                if (shareFeedPanelInNotification != null) {
                    SingleCommentView singleCommentView = shareFeedPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                    if (singleCommentView != null) {
                        singleCommentView.changeComment();
                    }
                }
            }
        } catch (Exception e) {
            log.error("processUpdateEditComment==>" + response);
        }
    }

    private void processUpdateDeleteImageComment() {
        try {
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel.getImageComments().remove(js.getCmnId());
                if (addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId()) != null) {
                    int bookcommentindex = addImageCaptionCommentsPanel.comments.getComponentZOrder(addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId()));
                    if (bookcommentindex > -1) {
                        addImageCaptionCommentsPanel.removeAComment(bookcommentindex);
                    }
                }

            }

        } catch (Exception e) {
            log.error("processUpdateDeleteImageComment==>" + response);
        }
    }

    private void processUpdateDeleteStatusComment() {
        try {
            //{"pckFs":13311,"sucs":true,"ln":"","nfId":1009,"cmnId":276,"actn":383,"ut":1419491772868,"fn":"Faiz Ahmed3","auId":2352}
            if (NewsFeedMaps.getInstance().getComments().get(js.getNfId()) != null) {
                NewsFeedMaps.getInstance().getComments().get(js.getNfId()).remove(js.getCmnId());
            }

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(js.getNfId());
            boolean hasactivity = false;
            if (newsFeeds != null) {
                Long nc = newsFeeds.getNc();
                Long comments = (nc != null && nc > 0 ? nc - 1L : nc);
                newsFeeds.setNc(comments);
                if (js.getAuId() != null && newsFeeds.getAuId() != null && js.getAuId().equals(newsFeeds.getAuId())) {
                    hasactivity = true;
                    newsFeeds.setActvt(null);
                    if (js.getUt() != null) {
                        newsFeeds.setUt(js.getUt());
                    }
                }
            }

            if (singleStatusPanelInHome != null) {
                singleStatusPanelInHome.change_comments_number();
                singleStatusPanelInHome.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    singleStatusPanelInHome.addOrChangeActivityInfo();
                }
            }
            if (singleStatusPanelInMyBook != null) {
                singleStatusPanelInMyBook.change_comments_number();
                singleStatusPanelInMyBook.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    singleStatusPanelInMyBook.addOrChangeActivityInfo();
                }
            }
            if (singleStatusPanelInFriendNewsFeed != null) {
                singleStatusPanelInFriendNewsFeed.change_comments_number();
                singleStatusPanelInFriendNewsFeed.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    singleStatusPanelInFriendNewsFeed.addOrChangeActivityInfo();
                }
            }
            if (singleStatusPanelInCircleNewsFeed != null) {
                singleStatusPanelInCircleNewsFeed.change_comments_number();
                singleStatusPanelInCircleNewsFeed.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    singleStatusPanelInCircleNewsFeed.addOrChangeActivityInfo();
                }
            }
            if (singleStatusPanelInNotification != null) {
                singleStatusPanelInNotification.change_comments_number();
                singleStatusPanelInNotification.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    singleStatusPanelInNotification.addOrChangeActivityInfo();
                }
            }
            if (shareFeedPanelInHome != null) {
                shareFeedPanelInHome.change_comments_number();
                shareFeedPanelInHome.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    shareFeedPanelInHome.addOrChangeActivityInfo();
                }
            }
            if (shareFeedPanelInMyBook != null) {
                shareFeedPanelInMyBook.change_comments_number();
                shareFeedPanelInMyBook.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    shareFeedPanelInMyBook.addOrChangeActivityInfo();
                }
            }
            if (shareFeedPanelInFriendNewsFeed != null) {
                shareFeedPanelInFriendNewsFeed.change_comments_number();
                shareFeedPanelInFriendNewsFeed.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    shareFeedPanelInFriendNewsFeed.addOrChangeActivityInfo();
                }
            }
            if (shareFeedPanelInCircleNewsFeed != null) {
                shareFeedPanelInCircleNewsFeed.change_comments_number();
                shareFeedPanelInCircleNewsFeed.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    shareFeedPanelInCircleNewsFeed.addOrChangeActivityInfo();
                }
            }
            if (shareFeedPanelInNotification != null) {
                shareFeedPanelInNotification.change_comments_number();
                shareFeedPanelInNotification.removeAComment(js.getNfId(), js.getCmnId());
                if (hasactivity) {
                    shareFeedPanelInNotification.addOrChangeActivityInfo();
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("processUpdateDeleteStatusComment==>" + response);
        }
    }

    private void processUpdateLikeUnlikeImageComment() {
        try {
            /*{"pckFs":12466,"id":387,"sucs":true,"imgId":9170,"ln":"samyoun","tm":1416381849419,"cmnId":1531,"lkd":1,"actn":397,"uId":"ring8801923128792","fn":"sirat"}*/
            AddImageCaptionCommentsPanel addImageCaptionCommentsPanel = null;
            if (SingleImgeDetails.getSingleImgeDetails() != null
                    && SingleImgeDetails.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().imageID == js.getImageId()
                    && SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel;

            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().imageDTo.getImageId().longValue() == js.getImageId()
                    && SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                addImageCaptionCommentsPanel = SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel;

            }
            if (addImageCaptionCommentsPanel != null) {
                if (addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId()) != null) {
                    SingeImageCommentDetails singeImageCommentDetails = addImageCaptionCommentsPanel.getCommentsOfthisImage().get(js.getCmnId());
                    SingleBookDetails obj = HelperMethods.getSingleBookDetails(response);
                    SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
                    if (FriendList.getInstance().getFriend_hash_map().get(obj.getUserIdentity()) != null) {
                        UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(obj.getUserIdentity());
                        singleFriend.setUserIdentity(obj.getUserIdentity());
                        singleFriend.setFullName(bac.getFullName());
                        //singleFriend.setLastName(bac.getLastName());
                        singleFriend.setProfileImage(bac.getProfileImage());
                    } else {
                        singleFriend.setUserIdentity(obj.getUserIdentity());
                    }
                    singeImageCommentDetails.getImageCommentsLikes().put(obj.getUserIdentity(), singleFriend);
                    long tl = singeImageCommentDetails.commentDto.getTl();
                    long likes = 0;
                    if (obj.getLkd() > 0) {
                        likes = (tl > 0 ? tl : 0) + 1L;
                    } else {
                        if (tl > 0) {
                            likes = tl - 1;
                        }

                    }
                    System.out.println("likes");
                    singeImageCommentDetails.commentDto.setTl(likes);
                    singeImageCommentDetails.change_like_number();
                }
            }
//            SingleBookDetails obj = HelperMethods.getSingleBookDetails(response);
//
//            Long singleCommentid = Long.valueOf(HelperMethods.makeStatusIDCommentIDKey(obj.getImageId(), obj.getCmnId()));
//
//            SingleImageComment singleImageComment = NewsFeedMaps.getInstance().getSingleStatusPanelImageViewer().get(obj.getImageId()).getCommentsOfthisImage().get(String.valueOf(singleCommentid));
//
//            if (singleImageComment != null) {
//                SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
//                if (FriendList.getInstance().getFriend_hash_map().get(obj.getUserIdentity()) != null) {
//                    UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(obj.getUserIdentity());
//                    singleFriend.setUserIdentity(obj.getUserIdentity());
//                    singleFriend.setFullName(bac.getFullName());
//                    singleFriend.setLastName(bac.getLastName());
//                    singleFriend.setProfileImage(bac.getProfileImage());
//                } else {
//                    singleFriend.setUserIdentity(obj.getUserIdentity());
//                }
//
//                if (NewsFeedMaps.getInstance().getImageLikes().get(singleCommentid) == null) {
//                    Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
//                    commentsJson.put(obj.getUserIdentity(), singleFriend);
//                    NewsFeedMaps.getInstance().getImageLikes().put(singleCommentid, commentsJson);
//                } else {
//                    NewsFeedMaps.getInstance().getImageLikes().get(singleCommentid).put(obj.getUserIdentity(), singleFriend);
//                }
//
//                if (NewsFeedMaps.getInstance().getImageComments().get(obj.getImageId()) != null) {
//                    JsonFields comment = NewsFeedMaps.getInstance().getImageComments().get(obj.getImageId()).get(obj.getCmnId());
//                    if (comment != null) {
//                        Long tl = comment.getTl();
//                        Long likes = 0L;
//                        if (obj.getLkd() > 0) {
//                            likes = (tl != null ? tl.longValue() : 0) + 1L;
//                        } else {
//                            likes = (tl != null ? tl.longValue() : 0) - 1L;
//                        }
//                        comment.setTl(likes);
//                    }
//                }
//
//                singleImageComment.change_like_number();
//            }

        } catch (Exception e) {
            log.error("processUpdateLikeUnlikeImageComment==>" + response);
        }

    }

    private void processUpdateLikeComment() {
        try {
            SingleMemberInCircleDto singleFriend = new SingleMemberInCircleDto();
            if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
                UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
                singleFriend.setUserIdentity(js.getUserIdentity());
                singleFriend.setFullName(bac.getFullName());
                // singleFriend.setLastName(bac.getLastName());
                singleFriend.setProfileImage(bac.getProfileImage());
            } else {
                singleFriend.setUserIdentity(js.getUserIdentity());
            }

            String singleCommentid = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());

            if (NewsFeedMaps.getInstance().getLikes().get(singleCommentid) == null) {
                Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                commentsJson.put(js.getUserIdentity(), singleFriend);
                NewsFeedMaps.getInstance().getLikes().put(singleCommentid, commentsJson);
            } else {
                NewsFeedMaps.getInstance().getLikes().get(singleCommentid).put(js.getUserIdentity(), singleFriend);
            }

            if (NewsFeedMaps.getInstance().getComments().get(js.getNfId()) != null) {
                JsonFields comment = NewsFeedMaps.getInstance().getComments().get(js.getNfId()).get(js.getCmnId());
                if (comment != null) {
                    Long tl = comment.getTl();
                    Long likes = (tl != null ? tl : 0) + 1L;
                    comment.setTl(likes);
                }
            }

            String singleCommentId = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            if (singleStatusPanelInHome != null) {
                SingleCommentView singleCommentView = singleStatusPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInMyBook != null) {
                SingleCommentView singleCommentView = singleStatusPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInFriendNewsFeed != null) {
                SingleCommentView singleCommentView = singleStatusPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInCircleNewsFeed != null) {
                SingleCommentView singleCommentView = singleStatusPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInNotification != null) {
                SingleCommentView singleCommentView = singleStatusPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInHome != null) {
                SingleCommentView singleCommentView = shareFeedPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInMyBook != null) {
                SingleCommentView singleCommentView = shareFeedPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInFriendNewsFeed != null) {
                SingleCommentView singleCommentView = shareFeedPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInCircleNewsFeed != null) {
                SingleCommentView singleCommentView = shareFeedPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInNotification != null) {
                SingleCommentView singleCommentView = shareFeedPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
        } catch (Exception e) {
            log.error("processUpdateLikeUnlikeImageComment==>" + response);
        }
    }

    private void processUpdateUnLikeComment() {
        try {
            JsonFields singleFriend = new JsonFields();
            if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
                UserBasicInfo bac = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
                singleFriend.setUserIdentity(js.getUserIdentity());
                singleFriend.setFullName(bac.getFullName());
                //singleFriend.setLastName(bac.getLastName());
                singleFriend.setProfileImage(bac.getProfileImage());
            } else {
                singleFriend.setUserIdentity(js.getUserIdentity());
            }

            String singleCommentid = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());

            if (NewsFeedMaps.getInstance().getLikes().get(singleCommentid) != null) {
                NewsFeedMaps.getInstance().getLikes().get(singleCommentid).remove(js.getUserIdentity());
            }

            if (NewsFeedMaps.getInstance().getComments().get(js.getNfId()) != null) {
                JsonFields comment = NewsFeedMaps.getInstance().getComments().get(js.getNfId()).get(js.getCmnId());
                if (comment != null) {
                    Long tl = comment.getTl();
                    Long likes = (tl != null && tl > 0 ? tl - 1L : tl);
                    comment.setTl(likes);
                }
            }

            String singleCommentId = HelperMethods.makeStatusIDCommentIDKey(js.getNfId(), js.getCmnId());

            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(js.getNfId());

            SingleStatusPanelInBookHome singleStatusPanelInHome = null;
            SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
            SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
            SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
            SingleStatusPanelInNotification singleStatusPanelInNotification = null;
            SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
            SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
            SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
            SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

            if (parentNfId != null) {
                SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(js.getNfId());
                }
                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(js.getNfId())) {
                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(js.getNfId());
                }
            } else {
                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(js.getNfId());
                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(js.getNfId());
                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(js.getNfId());
                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(js.getNfId());
            }

            if (singleStatusPanelInHome != null) {
                SingleCommentView singleCommentView = singleStatusPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInMyBook != null) {
                SingleCommentView singleCommentView = singleStatusPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInFriendNewsFeed != null) {
                SingleCommentView singleCommentView = singleStatusPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInCircleNewsFeed != null) {
                SingleCommentView singleCommentView = singleStatusPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (singleStatusPanelInNotification != null) {
                SingleCommentView singleCommentView = singleStatusPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInHome != null) {
                SingleCommentView singleCommentView = shareFeedPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInMyBook != null) {
                SingleCommentView singleCommentView = shareFeedPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInFriendNewsFeed != null) {
                SingleCommentView singleCommentView = shareFeedPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInCircleNewsFeed != null) {
                SingleCommentView singleCommentView = shareFeedPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
            if (shareFeedPanelInNotification != null) {
                SingleCommentView singleCommentView = shareFeedPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                if (singleCommentView != null) {
                    singleCommentView.change_like_number();
                }
            }
        } catch (Exception e) {
            log.error("processUpdateUnLikeComment==>" + response);
        }
    }

    private void processTypeImageDetails() {
        if (js.getSuccess()) {
            js.setMessage(response);
        }
    }

    private void processStartFriendChat() {
        ChatIntiatorDto initiatorDTO = HelperMethods.getCallingTimeDto(response);
        if (initiatorDTO.getRc() != null && initiatorDTO.getRc() == 1) {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(initiatorDTO.getFriendIdentity());
            if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null) {
                friendChatCallPanel.myFriendChatPanel.btnSendChat.requestFocus();
            }
            return;
        }
        if (initiatorDTO.getFriendIdentity() != null) {
            ChatRegistrationDTO entity = new ChatRegistrationDTO();
            //entity.setChatServerIp("192.168.1.125");
            entity.setChatServerIp(initiatorDTO.getChatServerIp());
            entity.setChatRegistrationPort(initiatorDTO.getChatRegistrationPort());
            entity.setDevice(initiatorDTO.getDevice());
            entity.setDeviceToken(initiatorDTO.getDeviceToken());
            entity.setFriendIdentity(initiatorDTO.getFriendIdentity());
            entity.setPresence(initiatorDTO.getPresence());
            UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(entity.getFriendIdentity());
            if (friendInfo != null) {
                entity.setDeviceToken(friendInfo.getDeviceToken());
            }
            ChatService.registerFriendChat(entity);

            if (friendInfo != null) {
                friendInfo.setLastOnlineTime(initiatorDTO.getLastOnlineTime());
                friendInfo.setPresence(entity.getPresence());

                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(entity.getFriendIdentity());
                if (friendChatCallPanel != null) {
                    friendChatCallPanel.setFriendProfileInfo(friendInfo);
                    friendChatCallPanel.refreshMyFriendChatCallPanelStatus();
                }

                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(entity.getFriendIdentity());
                if (friendProfile != null) {
                    friendProfile.setFriendProfileInfo(friendInfo);
                    friendProfile.buildStatusImage();
                }
            }
        }
    }

    private void processStartGroupChat() {
        try {

            ChatIntiatorDto initiatorDTO = HelperMethods.getCallingTimeDto(response);
            if (initiatorDTO.getSuccess()) {
                if (initiatorDTO.getGroupId() != null && initiatorDTO.getFriends() != null && initiatorDTO.getFriends().size() > 0) {

                    List<GroupInfoDTO> tempGroups = new ArrayList<GroupInfoDTO>();
                    List<GroupMemberDTO> tempGroupMembers = new ArrayList<GroupMemberDTO>();
                    List<String> groupMembers = new ArrayList<String>();

                    GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(initiatorDTO.getGroupId());
                    if (groupInfo != null) {
                        if (groupInfo.getMemberMap() == null) {
                            groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                        }
                        groupInfo.setGroupName(initiatorDTO.getGroupName());
                        if (initiatorDTO.getUt() != null) {
                            groupInfo.setGroupUt(initiatorDTO.getUt());
                        }
                        groupInfo.setOwnerUserTableId(initiatorDTO.getGroupOwnerUserTableId());

                        for (Entry<String, GroupMemberDTO> entry : groupInfo.getMemberMap().entrySet()) {
                            if (!initiatorDTO.getFriends().containsKey(entry.getKey())) {
                                entry.getValue().setIntegerStatus(StatusConstants.STATUS_DELETED);
                                tempGroupMembers.add(entry.getValue());
                            }
                        }

                        for (GroupMemberDTO groupMember : tempGroupMembers) {
                            groupInfo.getMemberMap().remove(groupMember.getUserIdentity());
                        }

                        for (Entry<String, String> entry : initiatorDTO.getFriends().entrySet()) {
                            GroupMemberDTO memberDTO = groupInfo.getMemberMap().get(entry.getKey());
                            if (memberDTO != null) {
                                memberDTO.setFullName(entry.getValue());
                            } else {
                                memberDTO = new GroupMemberDTO();
                                memberDTO.setGroupId(groupInfo.getGroupId());
                                memberDTO.setUserIdentity(entry.getKey());
                                memberDTO.setFullName(entry.getValue());
                                groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                            }
                            tempGroupMembers.add(memberDTO);
                            groupMembers.add(memberDTO.getUserIdentity());
                        }

                    } else {
                        groupInfo = new GroupInfoDTO();
                        groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                        groupInfo.setGroupId(initiatorDTO.getGroupId());
                        groupInfo.setGroupUt(initiatorDTO.getUt());
                        groupInfo.setGroupName(initiatorDTO.getGroupName());
                        groupInfo.setOwnerUserTableId(initiatorDTO.getGroupOwnerUserTableId());
                        MyfnfHashMaps.getInstance().getGroup_hash_map().put(initiatorDTO.getGroupId(), groupInfo);

                        for (Entry<String, String> entry : initiatorDTO.getFriends().entrySet()) {
                            GroupMemberDTO memberDTO = new GroupMemberDTO();
                            memberDTO.setGroupId(groupInfo.getGroupId());
                            memberDTO.setUserIdentity(entry.getKey());
                            memberDTO.setFullName(entry.getValue());
                            groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                            tempGroupMembers.add(memberDTO);
                            groupMembers.add(memberDTO.getUserIdentity());
                        }

                    }
                    tempGroups.add(groupInfo);

                    new InsertIntoGroupListTable(tempGroups).start();
                    new InsertIntoGroupMemberTable(tempGroupMembers).start();

                    if (initiatorDTO.getFriends().size() > 1 && groupInfo.getMemberMap().containsKey(MyFnFSettings.LOGIN_USER_ID)) {
                        ChatRegistrationDTO entity = new ChatRegistrationDTO();
                        //entity.setChatServerIp("192.168.1.125");
                        entity.setChatServerIp(initiatorDTO.getChatServerIp());
                        //entity.setChatRegistrationPort(1500);
                        entity.setChatRegistrationPort(initiatorDTO.getChatRegistrationPort());
                        entity.setFriendIdentity(initiatorDTO.getFriendIdentity());
                        entity.setGroupId(initiatorDTO.getGroupId());
                        ChatService.registerGroupChat(entity, groupMembers);
                    }

                    GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(js.getGroupId());
                    if (groupPanel != null) {
                        groupPanel.buildMembersPanel();
                    }

                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null
                            && GuiRingID.getInstance().getMainRight().getGroupMainView() != null
                            && GuiRingID.getInstance().getMainRight().getGroupMainView().isDisplayable()) {
                        GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                    }
                }
            }

        } catch (Exception ex) {

        }
    }

    private void processUpdateAddGroupChatMember() {
        try {
            JsonFields fields = HelperMethods.getJsonFields(response);
            if (js.getSuccess() != null) {
                if (fields.getFriendMap() != null && fields.getFriendMap().size() > 0) {

                    List<GroupInfoDTO> tempGroups = new ArrayList<GroupInfoDTO>();
                    List<GroupMemberDTO> tempGroupMembers = new ArrayList<GroupMemberDTO>();
                    List<String> groupMembers = new ArrayList<String>();

                    GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(fields.getGroupId());
                    if (groupInfo == null) {
                        groupInfo = new GroupInfoDTO();
                    }
                    if (groupInfo.getMemberMap() == null) {
                        groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                    }
                    groupInfo.setGroupId(fields.getGroupId());
                    groupInfo.setGroupUt(fields.getUt());
                    groupInfo.setGroupName(fields.getGroupName());
                    groupInfo.setOwnerUserTableId(fields.getGroupOwnerUserTableId());
                    MyfnfHashMaps.getInstance().getGroup_hash_map().put(fields.getGroupId(), groupInfo);

                    for (Entry<String, GroupMemberDTO> entry : groupInfo.getMemberMap().entrySet()) {
                        if (!fields.getFriendMap().containsKey(entry.getKey())) {
                            entry.getValue().setIntegerStatus(StatusConstants.STATUS_DELETED);
                            tempGroupMembers.add(entry.getValue());
                        }
                    }

                    for (GroupMemberDTO groupMember : tempGroupMembers) {
                        groupInfo.getMemberMap().remove(groupMember.getUserIdentity());
                    }

                    for (Entry<String, String> entry : fields.getFriendMap().entrySet()) {
                        GroupMemberDTO memberDTO = new GroupMemberDTO();
                        memberDTO.setGroupId(groupInfo.getGroupId());
                        memberDTO.setUserIdentity(entry.getKey());
                        memberDTO.setFullName(entry.getValue());
                        groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                        tempGroupMembers.add(memberDTO);
                        groupMembers.add(memberDTO.getUserIdentity());
                    }
                    tempGroups.add(groupInfo);

                    new InsertIntoGroupListTable(tempGroups).start();
                    new InsertIntoGroupMemberTable(tempGroupMembers).start();

                    if (fields.getChatServerIp() != null && fields.getChatRegistrationPort() != null) {
                        ChatRegistrationDTO entity = new ChatRegistrationDTO();
                        //entity.setChatServerIp("192.168.1.125");
                        entity.setChatServerIp(fields.getChatServerIp());
                        //entity.setChatRegistrationPort(1500);
                        entity.setChatRegistrationPort(fields.getChatRegistrationPort());
                        entity.setFriendIdentity(fields.getFriendIdentity());
                        entity.setGroupId(fields.getGroupId());
                        ChatService.registerGroupChat(entity, groupMembers);
                    }

                } else if (MyfnfHashMaps.getInstance().getGroup_hash_map().containsKey(fields.getGroupId())) {

                    GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(fields.getGroupId());
                    if (groupInfo.getMemberMap() == null) {
                        groupInfo.setMemberMap(new ConcurrentHashMap<String, GroupMemberDTO>());
                    }

                    List<GroupMemberDTO> tempGroupMembers = new ArrayList<GroupMemberDTO>();
                    GroupMemberDTO memberDTO = new GroupMemberDTO();
                    memberDTO.setGroupId(groupInfo.getGroupId());
                    memberDTO.setUserIdentity(fields.getFriendIdentity());
                    memberDTO.setFullName(fields.getName());
                    groupInfo.getMemberMap().put(memberDTO.getUserIdentity(), memberDTO);
                    tempGroupMembers.add(memberDTO);
                    new InsertIntoGroupMemberTable(tempGroupMembers).start();

                    List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                    ActivityDTO activityDTO = new ActivityDTO();
                    activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                    activityDTO.setMessageType(ActivityConstants.MSG_GROUP_ADD_MEMBER);
                    activityDTO.setGroupId(groupInfo.getGroupId());
                    activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                    activityDTO.setPacketID(js.getPacketId());
                    activityDTO.setMembers(new ConcurrentHashMap<String, String>());
                    activityDTO.getMembers().put(fields.getFriendIdentity(), fields.getName());
                    activityDTOs.add(activityDTO);
                    new InsertIntoRingActivityTable(activityDTOs).start();

                }

                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(fields.getGroupId());
                if (groupPanel != null) {
                    groupPanel.buildMembersPanel();
                    groupPanel.buildPopUpMenu();
                }

                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainRight() != null
                        && GuiRingID.getInstance().getMainRight().getGroupMainView() != null
                        && GuiRingID.getInstance().getMainRight().getGroupMainView().isDisplayable()) {
                    GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                }

            }
        } catch (Exception e) {
        }
    }

    private void processUpdateRemoveGroupChatMember() {

        try {
            JsonFields fields = HelperMethods.getJsonFields(response);
            if (js.getSuccess() != null) {
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(fields.getGroupId());
                if (fields.getFriendIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    new DeleteFromGroupListTable(fields.getGroupId()).start();
                    MyfnfHashMaps.getInstance().getGroup_hash_map().remove(fields.getGroupId());

                    ChatService.unregisterGroupChat(fields.getGroupId(), ChatConstants.PRESENCE_ONLINE);
                    GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(fields.getGroupId());
                    if (groupPanel != null
                            && groupPanel.isDisplayable()
                            && GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null) {
                        GuiRingID.getInstance().getMainRight().showGroupViewPanel();
                    }
                    MyfnfHashMaps.getInstance().getGroupPanelMap().remove(fields.getGroupId());

                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainLeftContainer() != null
                            && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
                        GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
                    }
                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainButtonsPanel() != null) {
                        GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(js.getGroupId() + "");
                    }

                } else if (groupInfo != null && groupInfo.getMemberMap() != null) {
                    GroupMemberDTO memberDTO = groupInfo.getMemberMap().get(fields.getFriendIdentity());
                    groupInfo.getMemberMap().remove(fields.getFriendIdentity());

                    if (groupInfo.getMemberMap().size() == 1 || !(groupInfo.getMemberMap().containsKey(MyFnFSettings.LOGIN_USER_ID))) {
                        ChatService.unregisterGroupChat(groupInfo.getGroupId(), ChatConstants.PRESENCE_ONLINE);
                    }
                    GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(fields.getGroupId());
                    if (groupPanel != null) {
                        groupPanel.buildMembersPanel();
                    }

                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null
                            && GuiRingID.getInstance().getMainRight().getGroupMainView() != null
                            && GuiRingID.getInstance().getMainRight().getGroupMainView().isDisplayable()) {
                        GuiRingID.getInstance().getMainRight().getGroupMainView().buildGroupList();
                    }
                    new DeleteFromGroupMemberListTable(fields.getGroupId(), fields.getFriendIdentity()).start();

                    List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                    ActivityDTO activityDTO = new ActivityDTO();
                    activityDTO.setActivityType(ActivityConstants.ACTIVITY_GROUP_UPDATE);
                    activityDTO.setMessageType(ActivityConstants.MSG_GROUP_REMOVE_MEMBER);
                    activityDTO.setGroupId(groupInfo.getGroupId());
                    activityDTO.setActivityBy(MyFnFSettings.LOGIN_USER_ID);
                    activityDTO.setPacketID(js.getPacketId());
                    activityDTO.setMembers(new ConcurrentHashMap<String, String>());
                    activityDTO.getMembers().put(fields.getFriendIdentity(), memberDTO != null ? memberDTO.getFullName() : fields.getFriendIdentity());
                    activityDTOs.add(activityDTO);
                    new InsertIntoRingActivityTable(activityDTOs).start();
                }
            }
        } catch (Exception e) {
        }
    }

    private void processUpdateChangeFriendAccess() {
        try {
            JsonFields fields = HelperMethods.getJsonFields(response);
            if (js.getSuccess() != null) {
                UserBasicInfo basicInfo = null;
                int prevContactType;

                for (Entry<String, UserBasicInfo> entry : FriendList.getInstance().getFriend_hash_map().entrySet()) {
                    UserBasicInfo temp = entry.getValue();
                    if (temp.getUserTableId().intValue() == fields.getUserTableId()) {
                        basicInfo = temp;
                        break;
                    }
                }

                if (basicInfo != null) {
                    prevContactType = basicInfo.getContactType();
                    boolean isDownGradeChange = fields.getContactType() == StatusConstants.ACCESS_CHAT_CALL && basicInfo.getContactType() == StatusConstants.ACCESS_FULL;
                    if (isDownGradeChange) {
                        basicInfo.setIncomingNotification(Boolean.FALSE);
                        basicInfo.setContactType(fields.getContactType());
                        basicInfo.setNewContactType(0);
                        basicInfo.setIsChangeRequester(Boolean.FALSE);
                    } else {
                        basicInfo.setIncomingNotification(Boolean.TRUE);
                        basicInfo.setNewContactType(fields.getContactType());
                        basicInfo.setIsChangeRequester(Boolean.FALSE);
                    }
                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();

                    if (GuiRingID.getInstance().getMainLeftContainer() != null
                            && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null
                            && basicInfo.getIncomingNotification()) {
                        GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().addIncomingRequestNotification();
                    }

                    if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().change_access(basicInfo.getUserIdentity());
                        }
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().change_access(basicInfo.getUserIdentity());
                        }
                    }

                    List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                    list.add(basicInfo);
                    new InsertIntoContactListTable(list).start();

                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(basicInfo.getUserIdentity());
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.setFriendProfileInfo(basicInfo.getUserIdentity());
                        friendChatCallPanel.refreshMyFriendChatCallPanelAccess();
                    }

                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(basicInfo.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(basicInfo.getUserIdentity());
                        friendProfile.refreshMyFriendPanel();
                    }
                    AllFriendList.changeAccessControl(basicInfo.getUserIdentity());

                    List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                    ActivityDTO activityDTO = new ActivityDTO();
                    activityDTO.setActivityType(ActivityConstants.ACTIVITY_ACCESS_REQUEST);
                    activityDTO.setMessageType(isDownGradeChange ? ActivityConstants.MSG_DOWNGRADE_ACCESS_CHANGE : ActivityConstants.MSG_PENDING_ACCESS_CHANGE_REQUEST);
                    activityDTO.setFriendIdentity(basicInfo.getUserIdentity());
                    activityDTO.setActivityBy(basicInfo.getUserIdentity());
                    activityDTO.setFromContactType(prevContactType);
                    activityDTO.setToContactType(fields.getContactType());
                    activityDTO.setPacketID(js.getPacketId());
                    activityDTOs.add(activityDTO);
                    new InsertIntoRingActivityTable(activityDTOs).start();
                }
            }
        } catch (Exception ex) {
        }
    }

    private void processUpdateAcceptFriendAccess() {
        try {
            JsonFields fields = HelperMethods.getJsonFields(response);
            if (js.getSuccess() != null) {

                UserBasicInfo basicInfo = null;
                int prevContactType;

                for (Entry<String, UserBasicInfo> entry : FriendList.getInstance().getFriend_hash_map().entrySet()) {
                    UserBasicInfo temp = entry.getValue();
                    if (temp.getUserTableId().intValue() == fields.getUserTableId()) {
                        basicInfo = temp;
                        break;
                    }
                }

                if (basicInfo != null) {
                    prevContactType = basicInfo.getContactType();
                    if (fields.getIsAccepted()) {
                        basicInfo.setContactType(fields.getContactType());
                    }
                    basicInfo.setNewContactType(0);
                    basicInfo.setIsChangeRequester(Boolean.FALSE);

                    List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                    list.add(basicInfo);
                    new InsertIntoContactListTable(list).start();

                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(basicInfo.getUserIdentity());
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.setFriendProfileInfo(basicInfo.getUserIdentity());
                        friendChatCallPanel.refreshMyFriendChatCallPanelAccess();
                    }

                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(basicInfo.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(basicInfo.getUserIdentity());
                        friendProfile.refreshMyFriendPanel();
                    }
                    //GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
                    if (GuiRingID.getInstance().getAddFriendMainPanel() != null) {
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getPendingFriendPanel().change_access(basicInfo.getUserIdentity());
                        }
                        if (GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel() != null
                                && GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel() != null) {
                            GuiRingID.getInstance().getAddFriendMainPanel().getAddFriendPanel().getMigrationPanel().change_access(basicInfo.getUserIdentity());
                        }
                    }
                    AllFriendList.changeAccessControl(basicInfo.getUserIdentity());

                    List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
                    ActivityDTO activityDTO = new ActivityDTO();
                    activityDTO.setActivityType(ActivityConstants.ACTIVITY_ACCESS_REQUEST);
                    activityDTO.setMessageType(fields.getIsAccepted() ? ActivityConstants.MSG_ACCEPTED_ACCESS_CHANGE_REQUEST : ActivityConstants.MSG_REJECTED_ACCESS_CHANGE_REQUEST);
                    activityDTO.setFriendIdentity(basicInfo.getUserIdentity());
                    activityDTO.setActivityBy(basicInfo.getUserIdentity());
                    activityDTO.setFromContactType(prevContactType);
                    activityDTO.setToContactType(fields.getContactType());
                    activityDTO.setPacketID(js.getPacketId());
                    activityDTOs.add(activityDTO);
                    new InsertIntoRingActivityTable(activityDTOs).start();
                }

            }
        } catch (Exception ex) {
        }
    }

    private MainRightDetailsView getMainRight() {
        return GuiRingID.getInstance() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    public CircleViewRight getCircleLeft() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getCircleViewRight();
        } else {
            return null;
        }
    }

    private MenuBarTop getTopMenuBar() {
        return GuiRingID.getInstance() != null ? GuiRingID.getInstance().getTopMenuBar() : null;
    }

    private void addUnreadNewsFeeds(Long nfId, Long groupId) {
        if (NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(groupId) == null) {
            NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().put(groupId, new HashSet<Long>());
        }
        int num = 1;
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtons() != null) {
            if (GuiRingID.getInstance().getMainButtons().getCircleNotification().getText() != null
                    && GuiRingID.getInstance().getMainButtons().getCircleNotification().getText().trim().length() > 0) {
                num = Integer.parseInt(GuiRingID.getInstance().getMainButtons().getCircleNotification().getText().trim()) + 1;
            }
            GuiRingID.getInstance().getMainButtons().getCircleNotification().setText(num + "");
            GuiRingID.getInstance().getMainButtons().getCircleNotification().revalidate();
            GuiRingID.getInstance().getMainButtons().getCircleNotification().repaint();
        }
        NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(groupId).add(nfId);
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainRight() != null
                && GuiRingID.getInstance().getMainRight().getCircleViewRight() != null) {
            GuiRingID.getInstance().getMainRight().getCircleViewRight().setNotificationCount();
        }
    }

}

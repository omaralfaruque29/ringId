
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.google.gson.GsonBuilder;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.communicator.ChatCommunication;
import com.ipv.chat.dto.ChatSettingsDTO;
import com.ipv.chat.listener.ChatActionListener;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ipvision.model.constants.DBConstants;
//import local.ua.UserAgentProfile;
import com.ipvision.model.JsonFields;
import com.ipvision.model.CreateCircle;
import com.ipvision.model.CircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.FriendlistMapping;
import com.ipvision.model.AlbumImageMap;
import com.ipvision.model.AlbumMap;
import com.ipvision.model.CommentsMap;
import com.ipvision.model.LikesMap;
import com.ipvision.model.NewsFeedMap;
import com.ipvision.model.SearchFreindlListMapping;
import com.ipvision.model.CircleListMap;
import com.ipvision.model.CircleMembersMap;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.NotificationMap;
import com.ipvision.model.ShareFeedResponse;
import com.ipvision.model.GroupListMapping;
import com.ipvision.service.call.CallSignalHandler;
import com.ipvision.model.call.CallerDto;
import com.ipvision.service.utility.call.TopLabelWrapper;
import com.ipvision.model.call.CallLogList;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.EducationListDTO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.SkillListDTO;
import com.ipvision.model.ChatIntiatorDto;
import com.ipvision.model.PressenceDto;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.UserDetailsInfoDTO;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.model.WorkListDTO;
import com.ipvision.model.MigrationFriendlistMapping;
import com.ipvision.service.ClearAllFeedsByBookHome;
import com.ipvision.service.ClearAllFeedsByCircleBook;
import com.ipvision.service.ClearAllFeedsByFriendBook;
import com.ipvision.service.ClearAllFeedsByMyBook;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.StickerDTO;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.model.ChatBgDTO;
import com.ipvision.service.utility.chat.ChatActionHandler;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import static com.ipvision.view.friendlist.FriendSearchPanel.getPriorityLevel;
import com.ipvision.service.auth.AuthenticationReceiverMain;
import com.ipvision.service.auth.PacketQueueProcessor;
import com.ipvision.service.image.LoadStickersFromRingMarket;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author FaizAhmed
 */
public class HelperMethods {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HelperMethods.class);

    public static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getExtension(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(imageName.lastIndexOf('.') + 1);
        }
        return imageName;
    }

    public static String getImageName(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(0, imageName.lastIndexOf('.'));
        }
        return imageName;
    }

    public static boolean check_string_contains_substring(String main_string, String string_to_test) {
        main_string = main_string.toLowerCase();
        string_to_test = string_to_test.toLowerCase();
        if (main_string.contains(string_to_test)) {
            return true;
        } else {
            return false;
        }
    }

    public static String convert_epoc_to_date_string(long epoc_date) {

        long date = epoc_date;
        String NormalDate = "";
        int hours = (int) (date / 3600);
        if (hours > 5) {
            NormalDate = new java.text.SimpleDateFormat("dd-MM-yy hh:mm aaa").format(new java.util.Date(date));
        } else {
            NormalDate = new java.text.SimpleDateFormat("dd-MM-yy").format(new java.util.Date(date));
        }
        return NormalDate;

    }

    public static String getRingID(String normalRingid) {
        StringBuilder stringBuilder = new StringBuilder(normalRingid);
        stringBuilder.insert(3, " ");
        stringBuilder.insert(7, " ");
        return StaticFields.RINGID_PREFIX + " " + stringBuilder.toString();
    }

    public static String convert_seconds_to_h_m_s(long millis_jsonStringToParse) {
        int seconds_jsonStringToParse = (int) (millis_jsonStringToParse / 1000);
        int hours = (int) (seconds_jsonStringToParse / 3600);
        int myremainder = (int) (seconds_jsonStringToParse % 3600);
        int minutes = (int) (myremainder / 60);
        int seconds = myremainder % 60;
        return (hours > 9 ? hours : ("0" + hours)) + ":" + (minutes > 9 ? minutes : ("0" + minutes)) + ":" + (seconds > 9 ? seconds : ("0" + seconds));
        //  return "";
    }

    public static String get_country_code_from_contry_name(String conuntry) {
        // String code = "";
        int index = 0;

        for (int f = 0; f < MyFnFSettings.COUNTRY_MOBILE_CODE.length; f++) {
            //  country_prefix_ComboBox.addItem(MyFnFSettings.COUNTRY_MOBILE_CODE[f][0].toString());
            if (MyFnFSettings.COUNTRY_MOBILE_CODE[f][0].equals(conuntry)) {
                index = f;
                break;
            }

        }
        return MyFnFSettings.COUNTRY_MOBILE_CODE[index][1];
    }

    public static int get_country_index_from_contry_code(String code) {
        // String code = "";
        int index = 0;

        for (int f = 0; f < MyFnFSettings.COUNTRY_MOBILE_CODE.length; f++) {
            //  country_prefix_ComboBox.addItem(MyFnFSettings.COUNTRY_MOBILE_CODE[f][0].toString());
            if (MyFnFSettings.COUNTRY_MOBILE_CODE[f][1].equals(code)) {
                return f;
                // index = f;
                //  break;
            }

        }
        return 0;
        // return MyFnFSettings.COUNTRY_MOBILE_CODE[index][0];
    }

    public static String get_country_name_from_contry_code(String code) {
        for (int f = 0; f < MyFnFSettings.COUNTRY_MOBILE_CODE.length; f++) {
            if (MyFnFSettings.COUNTRY_MOBILE_CODE[f][1].equals(code)) {
                return MyFnFSettings.COUNTRY_MOBILE_CODE[f][0];
            }

        }
        return "";
    }

    public static boolean hasDigit(String str) {
        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static UserBasicInfo get_all_for_a_profile(String user_id) {
        UserBasicInfo pp = new UserBasicInfo();
        for (String key : FriendList.getInstance().getFriend_hash_map().keySet()) {
            if (key.equals(user_id)) {
                pp = FriendList.getInstance().getFriend_hash_map().get(key);
                break;
            }
        }
        return pp;
    }

    public static List sortFriendList(List<UserBasicInfo> list) {
        Collections.sort(list, new Comparator<UserBasicInfo>() {
            @Override
            public int compare(UserBasicInfo one, UserBasicInfo other) {
                String onesFullName = one.getFullName().toUpperCase();
                String othersFullName = other.getFullName().toUpperCase();
                return onesFullName.compareTo(othersFullName);
            }
        });

        Collections.sort(list, new Comparator<UserBasicInfo>() {
            @Override
            public int compare(UserBasicInfo one, UserBasicInfo other) {
                Integer onesLevel = getPriorityLevel(one.getPresence(), one.getFriendShipStatus());
                Integer othersLevel = getPriorityLevel(other.getPresence(), other.getFriendShipStatus());
                return onesLevel.compareTo(othersLevel);
            }
        });
        return list;
    }

    public static void clearAllBookHomeUIMaps() {
        (new ClearAllFeedsByBookHome()).start();
    }

    public static void clearAllMyBookUIMaps() {
        (new ClearAllFeedsByMyBook()).start();
    }

    public static void clearAllFriendBookUIMaps() {
        (new ClearAllFeedsByFriendBook()).start();
    }

    public static void clearAllCircleBookUIMaps() {
        (new ClearAllFeedsByCircleBook()).start();
    }

    public static UserBasicInfo getFriendByMobileMumber(String mobileNo) {
        for (String key : FriendList.getInstance().getFriend_hash_map().keySet()) {
            UserBasicInfo entity = FriendList.getInstance().getFriend_hash_map().get(key);
            if (entity.getMobilePhone().equalsIgnoreCase(mobileNo)
                    || (entity.getMobilePhoneDialingCode() + entity.getMobilePhone()).equalsIgnoreCase(mobileNo)
                    || (entity.getMobilePhoneDialingCode().replace("+", "") + entity.getMobilePhone()).equalsIgnoreCase(mobileNo)) {
                return entity;
            }
        }
        return null;
    }

    public static String password_check(String password) {
        String pp = "";
        if (password.length() < 6) {
            pp = StaticFields.PASSWORD_TEXT + " must be atleast 6 characters";
            return pp;
        } else if (HelperMethods.check_string_contains_substring(password, ",")) {
            pp = NotificationMessages.COMMA_NOT_ALLOWED + "in " + StaticFields.PASSWORD_TEXT;
            return pp;
        } else if (password.startsWith(" ") || password.endsWith(" ")) {
            pp = StaticFields.PASSWORD_TEXT + " can't start or end with spaces";
            return pp;
        } else if (!MyFnFSettings.LOGIN_USER_ID.equals("") && HelperMethods.check_string_contains_substring(password, MyFnFSettings.LOGIN_USER_ID)) {
            pp = StaticFields.PASSWORD_TEXT + " can't contain  " + StaticFields.MY_FNF_NAME_TEXT;
            return pp;
        }
        return pp;
    }

    public static NotificationMap mapNotificationMap(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), NotificationMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static boolean check_mobile_number(String phoneNumber) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }

    }

    public static NewsFeedWithMultipleImage getNewsFeedWithMultipleImageDto(String jsonStringToParse) {
        return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), NewsFeedWithMultipleImage.class);
    }

    public static SingleBookDetails getSingleBookDetails(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SingleBookDetails.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static JsonFields getOnlyNewsFeedDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), JsonFields.class);
        } catch (Exception e) {
        }
        return null;

    }

    public static ShareFeedResponse getShareFeedResponse(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), ShareFeedResponse.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static FeedBackFields getFeedBackDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), FeedBackFields.class);

        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static CallLogList map_call_log(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CallLogList.class);

        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static FriendlistMapping friendlist_mapping(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), FriendlistMapping.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static MigrationFriendlistMapping migrationlist_mapping(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), MigrationFriendlistMapping.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static GroupListMapping grouplist_mapping(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), GroupListMapping.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static NewsFeedMap mapNewsFeed(String jsonStringToParse) {
        return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), NewsFeedMap.class);

    }

    public static CommentsMap mapComments(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CommentsMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static LikesMap mapLikes(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), LikesMap.class);

        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static AlbumMap mapAlbumMap(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), AlbumMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static AlbumImageMap albumImageMap(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), AlbumImageMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static SearchFreindlListMapping tamp_friendlist_mapping(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SearchFreindlListMapping.class);

        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }
//
//    public static SmsListMapper mapSmsList(String jsonStringToParse) {
//        try {
//            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SmsListMapper.class);
//
//        } catch (Exception e) {
//            log.error("Json parse failed " + jsonStringToParse);
//        }
//        return null;
//
//    }

    public static CircleMembersMap mapCircleMembersMap(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CircleMembersMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static CircleListMap getCircleListMap(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CircleListMap.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static SingleCircleDto getSingleCircleDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SingleCircleDto.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }

        return null;
    }

    public static CreateCircle createCircleMapper(String jsonStringToParse) {
        //  CreateCircle dd = new CreateCircle();
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CreateCircle.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static UserBasicInfo getUserBasicInfoDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), UserBasicInfo.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }

        return null;
    }

    public static JsonFields getJsonFields(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), JsonFields.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }
//CallerDto

    public static CallerDto getCallerDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CallerDto.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }

        return null;
    }

    public static ChatIntiatorDto getCallingTimeDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), ChatIntiatorDto.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }

        return null;
    }

    public static PressenceDto getPressenceDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), PressenceDto.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static CircleDto map_into_CircleDto(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), CircleDto.class);

        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;

    }

    public static UserDetailsInfoDTO getUserDetailsInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), UserDetailsInfoDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static WorkListDTO getWorkListInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), WorkListDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static EducationListDTO getEducationListInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), EducationListDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static WorkInfoDTO getWorkInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), WorkInfoDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static SkillListDTO getSkillListInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SkillListDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static SkillInfoDTO getSkillInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), SkillInfoDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static EducationInfoDTO getEducationInfo(String jsonStringToParse) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonStringToParse.trim(), EducationInfoDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonStringToParse);
        }
        return null;
    }

    public static StickerDTO mapSticket(String jsonString) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonString.trim(), StickerDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonString);
        }
        return null;
    }

    public static Map<String, String> mapActivityGroupMembers(String jsonString) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonString.trim(), Map.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonString);
        }
        return null;
    }

    public static String makeActivityGroupMembersMap(Map<String, String> map) {
        try {
            return new GsonBuilder().serializeNulls().create().toJson(map, Map.class);
        } catch (Exception e) {
            log.error("Json build failed! ");
        }
        return "";
    }

    public static ChatBgDTO mapChatBackground(String jsonString) {
        try {
            return new GsonBuilder().serializeNulls().create().fromJson(jsonString.trim(), ChatBgDTO.class);
        } catch (Exception e) {
            log.error("Json parse failed " + jsonString);
        }
        return null;
    }

    public static String makeStatusIDCommentIDKey(Long statusid, String commentId) {
        return statusid + commentId;
    }

    public static String getStatusIcon(UserBasicInfo userBasicInfo) {
        String status_image = GetImages.OFFLINE;
        if (userBasicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                && (userBasicInfo.getPresence() == (StatusConstants.PRESENCE_ONLINE)
                || userBasicInfo.getPresence() == StatusConstants.PRESENCE_AWAY)) {
            if (userBasicInfo.getDevice() != null) {
                if (userBasicInfo.getDevice() == 1) {
                    status_image = GetImages.ONLINE_DESKTOP;
                } else if (userBasicInfo.getDevice() == 2) {
                    status_image = GetImages.ONLINE_ANDROID;
                } else if (userBasicInfo.getDevice() == 3) {
                    status_image = GetImages.ONLINE_IOS;
                } else if (userBasicInfo.getDevice() == 4) {
                    status_image = GetImages.ONLINE_WINDOWS;
                } else if (userBasicInfo.getDevice() == 5) {
                    status_image = GetImages.ONLINE_WEB;
                } else {
                    status_image = GetImages.OFFLINE;
                }
            }
        }
        return status_image;
    }

    public static String getOfflineStatusIcon(UserBasicInfo userBasicInfo) {
        String status_image = GetImages.OFFLINE;
        return status_image;
    }

    public static String getUserFullName(UserBasicInfo entity) {

        if (entity == null) {
            return "";
        }/* else if (entity.getFullName() != null && entity.getFullName().trim().length() > 0 && entity.getLastName() != null && entity.getLastName().trim().length() > 0) {
         return fullName = entity.getFullName() + " " + entity.getLastName();
         } */ else if (entity.getFullName() != null && entity.getFullName().trim().length() > 0) {
            return entity.getFullName();
        } /*else if (entity.getLastName() != null && entity.getLastName().trim().length() > 0) {
         return fullName = entity.getLastName();
         } *///        else if (entity.getFullName() != null && entity.getFullName().trim().length() > 0) {
        //            fullName = entity.getFullName();
        //        } 
        else {
            return entity.getUserIdentity();
        }
        //return fullName;
    }

    public static int getGroupMembersCount(GroupInfoDTO entity) {
        int count = 0;
        if (entity != null && entity.getMemberMap() != null) {
            count = entity.getMemberMap().containsKey(MyFnFSettings.LOGIN_USER_ID) ? entity.getMemberMap().size() - 1 : entity.getMemberMap().size();
        }
        return count;
    }

    public static String getPhoneNumber(String countryCode, String phoneNumber) {
//        String firstPart = phoneNumber.length() > 3 ? phoneNumber.substring(0, phoneNumber.length() - 3) : "";
//        String lastPart = phoneNumber.replace(firstPart, "");
//        firstPart = firstPart.replaceAll("(.)", "X");
//        return countryCode + firstPart + lastPart;
        return countryCode + phoneNumber;
    }

    public static String getUnknownProfilePhoneNumber(String countryCode, String phoneNumber) {
        String firstPart = phoneNumber.length() > 3 ? phoneNumber.substring(0, phoneNumber.length() - 3) : "";
        String lastPart = phoneNumber.replace(firstPart, "");
        firstPart = firstPart.replaceAll("(.)", "X");
        lastPart = lastPart.replaceAll("(.)", "X");
        return countryCode + firstPart + lastPart;
    }

    public static String getShortRingID(String userIdentity, String countryCode) {

//        int initLength = 4 + (countryCode != null && countryCode.length() > 0 ? countryCode.length() - 1 : 0);
//        String hString = initLength < userIdentity.length() - 3 ? userIdentity.substring(initLength, userIdentity.length() - 3) : "";
//        String rString = hString.replaceAll("(.)", "X");
//        return userIdentity.replace(hString, rString);
//
// 
//        int initLength = 4 + (countryCode != null && countryCode.length() > 0 ? countryCode.length() - 1 : 0);
//        String hString = initLength < userIdentity.length() - 3 ? userIdentity.substring(initLength, userIdentity.length() - 3) : "";
//        String rString = hString.replaceAll("(.)", "X");
//        return userIdentity.replace(hString, rString);
        return userIdentity;
    }

    public static NewsFeedWithMultipleImage dataConversionForShareFeed(SingleBookDetails share) {
        NewsFeedWithMultipleImage newsFeeds = new NewsFeedWithMultipleImage();
        newsFeeds.setNfId(share.getNfId());
        newsFeeds.setUserIdentity(share.getUserIdentity());
        newsFeeds.setStatus(share.getSts());
        newsFeeds.setFullName(share.getFullName());
//        newsFeeds.setLastName(share.getLastName());
        newsFeeds.setTm(share.getAddedTime());
        newsFeeds.setType(share.getType());
        newsFeeds.setNc(share.getNc());
        newsFeeds.setNl(share.getNl());
        newsFeeds.setTs(share.getTs());
        newsFeeds.setNs(share.getNs());
        newsFeeds.setIl(share.getIl());
        newsFeeds.setIc(share.getIc());
        newsFeeds.setSuccess(share.getSuccess());
        newsFeeds.setLocation(share.getLctn());
        newsFeeds.setSfId(share.getSfId());
        newsFeeds.setCircleId(share.getCircleId());
        return newsFeeds;
    }

    public static String encryptPassword(String password) {
        String newPass = "";
        if (password != null && password.length() > 0) {
            for (int i = 0; i < password.length(); i++) {
                int val = (int) password.charAt(i);
                if (val < 2 || val > 126) {
                    newPass += password.charAt(i);
                } else if (val % 2 == 0) {
                    newPass += (char) (password.charAt(i) - 1);
                } else {
                    newPass += (char) (password.charAt(i) + 1);
                }
            }
        }
        return newPass;
    }

    public static String decryptPassword(String pass) {
        String newPass = "";
        if (pass != null && pass.trim().length() > 0) {
            for (int i = 0; i < pass.length(); i++) {
                int val = (int) pass.charAt(i);
                if (val < 2 || val > 126) {
                    newPass += pass.charAt(i);
                } else if (val % 2 == 0) {
                    newPass += (char) (pass.charAt(i) - 1);
                } else {
                    newPass += (char) (pass.charAt(i) + 1);
                }
            }
        }
        return newPass;
    }

    public static String getSystemMacAddress() {
        String result = "";
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            result = sb.toString();
        } catch (Exception e) {
        }
        return result;
    }

    public static String fetchFromUrl(String url) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(2500);
            conn.setDoOutput(true);

            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception ex) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }

    public static void showConfirmationDialogMessage(String msg) {
        new JOptionPanelBasics(JOptionPanelBasics.YES_NO_MESSAGE, msg).setVisible(true);
    }

    public static void showPlaneDialogMessage(String msg) {
        new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, msg).setVisible(true);
    }

    public static void showWarningDialogMessage(String msg) {
        new JOptionPanelBasics(JOptionPanelBasics.WARNING_MESSAGE, msg).setVisible(true);
    }

    public static boolean isInvalidDownloadUrl(String url) {
        return NewsFeedMaps.getInstance().getAllreadyTriedToDownload().contains(url);
    }

    public static UserBasicInfo getUserBasicInfo(Long userTableId) {
        UserBasicInfo basicInfo = null;

        if (userTableId != null) {
            for (UserBasicInfo entity : FriendList.getInstance().getFriend_hash_map().values()) {
                if (entity.getUserTableId() == userTableId.longValue()) {
                    basicInfo = entity;
                    break;
                }
            }
        }

        return basicInfo;
    }

    public static void createProfileImageDirectory() {
        try {
            File dir = new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdir();
                log.info("Creating Directory \"/" + MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + "\" .................");
            }
        } catch (Exception ex) {
        }
    }

    public static void createCoverImageDirectory() {
        try {
            File dir = new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdir();
                log.info("Creating Directory \"/" + MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + "\" .................");
            }
        } catch (Exception ex) {
        }
    }

    public static void createBookImageDirectory() {
        try {
            File dir = new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdir();
                log.info("Creating Directory \"/" + MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + "\" .................");
            }
        } catch (Exception ex) {
        }
    }

    public static void createChatFilesDirectory() {
        try {
            File dir = new File(MyFnFSettings.TEMP_CHAT_FILES_FOLDER);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdir();
                log.info("Creating Directory \"/" + MyFnFSettings.TEMP_CHAT_FILES_FOLDER + "\" .................");
            }
        } catch (Exception ex) {
        }
    }

    public static void createBrokenFilesDirectory() {
        try {
            File dir = new File(MyFnFSettings.TEMP_BROKEN_FILES_FOLDER);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdir();
                log.info("Creating Directory \"/" + MyFnFSettings.TEMP_BROKEN_FILES_FOLDER + "\" .................");
            }
        } catch (Exception ex) {
        }
    }

    public static void loadDefaultFont() {
        try {
            String fontString = MyFnFSettings.RESOURCE_FOLDER + MyFnFSettings.DEFAULT_FONT_NAME;
            File font_file = new File(fontString);
            DefaultSettings.APP_DEFAULT_FONT = Font.createFont(Font.TRUETYPE_FONT, font_file);
        } catch (Exception ex) {
            DefaultSettings.APP_DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }
    }

    public static void defaultSettings(Class<?> classType) {
        try {
            URI applicationURI = classType.getProtectionDomain().getCodeSource().getLocation().toURI();
            File applicationFile = new File(applicationURI);
            DBConstants.DB_NAME = applicationFile.getParent() + File.separator + DBConstants.DB_NAME;
            MyFnFSettings.RESOURCE_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.RESOURCE_FOLDER;
            // System.out.println(" MyFnFSettings.RESOURCE_FOLDER--"+ MyFnFSettings.RESOURCE_FOLDER);
            MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER;
            MyFnFSettings.TEMP_COVER_IMAGE_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.TEMP_COVER_IMAGE_FOLDER;
            MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER;
            MyFnFSettings.TEMP_CHAT_FILES_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.TEMP_CHAT_FILES_FOLDER;
            MyFnFSettings.TEMP_BROKEN_FILES_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.TEMP_BROKEN_FILES_FOLDER;
            // MyFnFSettings.MAIL_SETTING_FILE = applicationFile.getParent() + File.separator + MyFnFSettings.MAIL_SETTING_FILE;
            MyFnFSettings.SCRIPT_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.SCRIPT_FOLDER;
            MyFnFSettings.EMOTICON_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.EMOTICON_FOLDER;
            MyFnFSettings.LARGE_EMOTICON_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.LARGE_EMOTICON_FOLDER;
            MyFnFSettings.STICKER_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.STICKER_FOLDER;
            MyFnFSettings.CHAT_BG_FOLDER = applicationFile.getParent() + File.separator + MyFnFSettings.CHAT_BG_FOLDER;
            ChatHelpers.CHAT_DIRECTORY = new DownLoaderHelps();
        } catch (Exception e) {
        }

    }

    public static void bindMyProfileObject(JsonFields fields, boolean showErrorMsg) {
        try {
            if (fields.getUserTableId() != null) {
                MyFnFSettings.userProfile.setUserTableId(fields.getUserTableId());
            }
            if (fields.getCountry() != null) {
                MyFnFSettings.userProfile.setCountry(fields.getCountry());
            } else {
                MyFnFSettings.userProfile.setCountry("");
            }
            if (fields.getEmail() != null) {
                MyFnFSettings.userProfile.setEmail(fields.getEmail());
            }
            if (fields.getGender() != null) {
                MyFnFSettings.userProfile.setGender(fields.getGender());
            }
            if (fields.getMobilePhoneDialingCode() != null) {
                MyFnFSettings.userProfile.setMobilePhoneDialingCode(fields.getMobilePhoneDialingCode());
            } else {
                MyFnFSettings.userProfile.setMobilePhoneDialingCode("");
            }
            if (fields.getMobilePhone() != null) {
                MyFnFSettings.userProfile.setMobilePhone(fields.getMobilePhone());
            } else {
                MyFnFSettings.userProfile.setMobilePhone("");
            }
            if (fields.getProfileImage() != null) {
                MyFnFSettings.userProfile.setProfileImage(fields.getProfileImage());
            }
            if (fields.getProfileImageId() != null) {
                MyFnFSettings.userProfile.setProfileImageId(fields.getProfileImageId());
            }
            if (fields.getCoverImage() != null) {
                MyFnFSettings.userProfile.setCoverImage(fields.getCoverImage());
            }
            if (fields.getCoverImageId() != null) {
                MyFnFSettings.userProfile.setCoverImageId(fields.getCoverImageId());
            }
            if (fields.getCoverImageX() != null) {
                MyFnFSettings.userProfile.setCoverImageX(-fields.getCoverImageX());
            }
            if (fields.getCoverImageY() != null) {
                MyFnFSettings.userProfile.setCoverImageY(-fields.getCoverImageY());
            }
            if (fields.getWhatisInYourMind() != null) {
                MyFnFSettings.userProfile.setWhatisInYourMind(fields.getWhatisInYourMind());
            }
            if (fields.getPresence() != null) {
                MyFnFSettings.userProfile.setPresence(fields.getPresence());
            }
            if (fields.getBirthday() != null) {
                MyFnFSettings.userProfile.setBirthday(fields.getBirthday());
            }
            if (fields.getCurrentCity() != null) {
                MyFnFSettings.userProfile.setCurrentCity(fields.getCurrentCity());
            }
            if (fields.getHomeCity() != null) {
                MyFnFSettings.userProfile.setHomeCity(fields.getHomeCity());
            }
            if (fields.getMarriageDay() != null) {
                long mday = fields.getMarriageDay();
                if (mday == 0) {
                    MyFnFSettings.userProfile.setMarriageDay("");
                } else {
                    Date d = new Date(mday);
                    String strDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                    MyFnFSettings.userProfile.setMarriageDay(strDate);
                }
            }
            if (fields.getAboutMe() != null) {
                MyFnFSettings.userProfile.setAboutMe(fields.getAboutMe());
            }
            if (fields.getFnfHeaderVersion() != null) {
                MyFnFSettings.userProfile.setFnfHeaderVersion(fields.getFnfHeaderVersion());
            }
            if (fields.getEmoticonVersion() != null) {
                MyFnFSettings.userProfile.setEmoticonVersion(fields.getEmoticonVersion());
            }
            if (fields.getPrivacy() != null) {
                MyFnFSettings.userProfile.setPrivacy(fields.getPrivacy());
            }
            if (fields.getRingEmail() != null) {
                MyFnFSettings.userProfile.setRingEmail(fields.getRingEmail());
            }
            if (fields.getOfflineServerIP() != null) {
                MyFnFSettings.RING_OFFLINE_IM_IP = fields.getOfflineServerIP();
            }
            if (fields.getOfflineServerPort() != null) {
                MyFnFSettings.RING_OFFLINE_IM_PORT = fields.getOfflineServerPort();
            }
            if (fields.getLastStatus() != null) {
                MyFnFSettings.userProfile.setPresence(fields.getLastStatus());
            } else {
                MyFnFSettings.userProfile.setPresence(StatusConstants.PRESENCE_OFFLINE);
            }
        } catch (Exception e) {
            log.error("Exception :: " + e.getMessage());
            System.exit(0);
        }

        if (MyFnFSettings.userProfile.getPrivacy() != null && MyFnFSettings.userProfile.getPrivacy().length > 0) {
            short privacy[] = MyFnFSettings.userProfile.getPrivacy();
            try {
                MyFnFSettings.userProfile.setEmailPrivacy(privacy[0]);
                MyFnFSettings.userProfile.setMobilePrivacy(privacy[1]);
                MyFnFSettings.userProfile.setProfileImagePrivacy(privacy[2]);
                MyFnFSettings.userProfile.setBirthdayPrivacy(privacy[3]);
                MyFnFSettings.userProfile.setCoverImagePrivacy(privacy[4]);
            } catch (Exception e) {
            }
        }

        if (showErrorMsg && fields.getVersionMessage() != null && fields.getVersionMessage().length() > 0) {
            HelperMethods.showWarningDialogMessage(fields.getVersionMessage());
        }
    }

    public static void startVoiceAndChatSockets() {
        CallSignalHandler callSignalHandler = new CallSignalHandler();
        TopLabelWrapper.startSignalProcessor(callSignalHandler);

        ChatSettings.DEBUG = log.isDebugEnabled();
        ChatSettingsDTO entity = new ChatSettingsDTO();
        entity.setAuthServerIP(ServerAndPortSettings.AUTH_SERVER_IP);
        entity.setAuthServerPort(ServerAndPortSettings.AUTHENTICATION_PORT);
        entity.setSessionID(MyFnFSettings.LOGIN_SESSIONID);
        entity.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        entity.setFullName(MyFnFSettings.userProfile.getFullName());
        entity.setChatOfflineIP(MyFnFSettings.RING_OFFLINE_IM_IP);
        //entity.setChatOfflineIP("192.168.1.125");
        entity.setChatOfflinePort(MyFnFSettings.RING_OFFLINE_IM_PORT);
        //entity.setChatOfflinePort(1200);
        entity.setBufferSize(MyFnFSettings.RING_CHAT_BUFFER_SIZE);
        //entity.setBrokenPacketSize(101);
        entity.setBrokenPacketSize(MyFnFSettings.RING_CHAT_BROKEN_PACKET_SIZE);
        entity.setFileChunkSize(MyFnFSettings.RING_CHAT_FILE_CHUNK_SIZE);
        entity.setChatLocalPort(MyFnFSettings.RING_CHAT_LOCAL_PORT);
        entity.setChatPlatform(ChatConstants.PLATFORM_DESKTOP);

        ChatActionHandler chatActionHandler = new ChatActionHandler();
        ChatCommunication.getInstance().startService(entity, (ChatActionListener) chatActionHandler);
        RecentChatCallActivityDAO.fetchAndResendPendingChat();
    }

    public static void startAuthSockets() {
        AuthenticationReceiverMain.getInstance().initUdp();
//        PacketQueueProcessor packetQueueProcessor = new PacketQueueProcessor();
//        packetQueueProcessor.start();
    }

    public static String getUrlEncode(String src) {
        if (src != null && src.length() > 0) {
            String dst = src;
            try {
                dst = URLEncoder.encode(dst, "UTF-8");
                return dst;
            } catch (UnsupportedEncodingException e) {
                // e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());
            }

        }
        return src;
    }

    public static boolean isValidEmail(String mail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static boolean isValidNumber(String number) {
        String NUMBER_PATTERN = "[0-9]+";
        Pattern pattern = Pattern.compile(NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static String getPCMacAddress() {
        StringBuilder sb = new StringBuilder();
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }

        } catch (Exception e) {
            //  e.printStackTrace();
        }
        return sb.toString();
    }

    public static void setFriendListLoaded() {
        MyFnFSettings.FRIEND_LIST_LOADED = true;
        NewsFeedMaps.getInstance().getViewProfileImageQueue().getItem();
        new LoadStickersFromRingMarket().start();
    }

    public static String getShortName(String fullName) {
        String shortName = "";
        try {
            String[] split = fullName.split(" ");
            if (split.length > 0) {
                shortName += (split[0].charAt(0) + "").toUpperCase();

                if (split.length > 1) {
                    shortName += (split[split.length - 1].charAt(0) + "").toUpperCase();
                }
            }
        } catch (Exception ex) {
        }
        return shortName;
    }

}

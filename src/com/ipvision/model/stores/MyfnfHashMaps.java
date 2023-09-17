/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.stores;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.view.addfriend.SingleAddFriendPanel;
import com.ipvision.model.CircleMembersMap;
import com.ipvision.model.call.CallerDto;
import com.ipvision.view.leftdata.SingleChatHistoryPanel;
import com.ipvision.view.circle.SingleCirclePanel;
import com.ipvision.view.friendlist.SingleFriendPanel;
import com.ipvision.model.EmotionDtos;
import com.ipvision.model.JsonFields;
import com.ipvision.model.EmototiconMapper;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.view.SingleNotification;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.leftdata.SingleCallHistoryPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;

/**
 *
 * @author user
 */
public class MyfnfHashMaps {

    public static MyfnfHashMaps hash_maps;

    public static MyfnfHashMaps getInstance() {
        if (hash_maps == null) {
            hash_maps = new MyfnfHashMaps();
        }
        return hash_maps;
    }
    private Map<String, CallerDto> callInitiators = new ConcurrentHashMap<String, CallerDto>();

    public Map<String, CallerDto> getCallInitiators() {
        return callInitiators;
    }

    public void setCallInitiators(Map<String, CallerDto> callInitiators) {
        this.callInitiators = callInitiators;
    }
    private Map<String, Boolean> alreadyHaveThisAuthPakId = new ConcurrentHashMap<String, Boolean>();

    public Map<String, Boolean> getAlreadyHaveThisAuthPakId() {
        return alreadyHaveThisAuthPakId;
    }

    public void setAlreadyHaveThisAuthPakId(Map<String, Boolean> alreadyHaveThisAuthPakId) {
        this.alreadyHaveThisAuthPakId = alreadyHaveThisAuthPakId;
    }

    private Map<String, EmotionDtos> emotionHashmap_from_server = new ConcurrentHashMap<String, EmotionDtos>();

    public Map<String, EmotionDtos> getEmotionHashmap_from_server() {
        return emotionHashmap_from_server;
    }

    public void setEmotionHashmap_from_server(Map<String, EmotionDtos> emotionHashmap_from_server) {
        this.emotionHashmap_from_server = emotionHashmap_from_server;
    }
    private Map<String, FeedBackFields> packetGotResponseFromServer = new ConcurrentHashMap<String, FeedBackFields>();

    public Map<String, FeedBackFields> getPacketGotResponseFromServer() {
        return packetGotResponseFromServer;
    }

    public void setPacketGotResponseFromServer(Map<String, FeedBackFields> packetGotResponseFromServer) {
        this.packetGotResponseFromServer = packetGotResponseFromServer;
    }
//    private Map<String, List<SmsFields>> smsHistoryPacketFromServer = new ConcurrentHashMap<String, List<SmsFields>>();
//
//    public Map<String, List<SmsFields>> getSmsHistoryPacketFromServer() {
//        return smsHistoryPacketFromServer;
//    }
//
//    public void setSmsHistoryPacketFromServer(Map<String, List<SmsFields>> smsHistoryPacketFromServer) {
//        this.smsHistoryPacketFromServer = smsHistoryPacketFromServer;
//    }
//    private Map<String, String> bufferedImages = new ConcurrentHashMap<String, String>();
//
//    public Map<String, String> getBufferedImages() {
//        return bufferedImages;
//    }
//
//    public void setBufferedImages(Map<String, String> bufferedImages) {
//        this.bufferedImages = bufferedImages;
//    }
    private Map<String, Map<String, JsonFields>> alBumImages = new ConcurrentHashMap<String, Map<String, JsonFields>>();

    public Map<String, Map<String, JsonFields>> getAlBumImages() {
        return alBumImages;
    }

    public void setAlBumImages(Map<String, Map<String, JsonFields>> alBumImages) {
        this.alBumImages = alBumImages;
    }
    private Map<Long, Map<String, SingleMemberInCircleDto>> circleMembers = new ConcurrentHashMap<Long, Map<String, SingleMemberInCircleDto>>();

    public Map<Long, Map<String, SingleMemberInCircleDto>> getCircleMembers() {
        return circleMembers;
    }

    public void setCircleMembers(Map<Long, Map<String, SingleMemberInCircleDto>> groupMembers) {
        this.circleMembers = groupMembers;
    }
    private Map<Long, SingleCircleDto> circleLists = new ConcurrentHashMap<Long, SingleCircleDto>();

    public Map<Long, SingleCircleDto> getCircleLists() {
        return circleLists;
    }

    public void setCircleLists(Map<Long, SingleCircleDto> groupLists) {
        this.circleLists = groupLists;
    }
    private Map<Long, SingleCircleDto> tempCircleList = new ConcurrentHashMap<Long, SingleCircleDto>();

    public Map<Long, SingleCircleDto> getTempCircleList() {
        return tempCircleList;
    }

    public void setTempCircleList(Map<Long, SingleCircleDto> tempGroupList) {
        this.tempCircleList = tempGroupList;
    }
    private Map<String, Boolean> tempCircleMemberContainerforUpdate = new ConcurrentHashMap<String, Boolean>();

    public Map<String, Boolean> getTempCircleMemberContainerforUpdate() {
        return tempCircleMemberContainerforUpdate;
    }

    public void setTempCircleMemberContainerforUpdate(Map<String, Boolean> tempgroupMemberContainerforUpdate) {
        this.tempCircleMemberContainerforUpdate = tempgroupMemberContainerforUpdate;
    }

    private Map<String, GroupMemberDTO> tempGroupMemberContainerforUpdate = new ConcurrentHashMap<String, GroupMemberDTO>();

    public Map<String, GroupMemberDTO> getTempGroupMemberContainerforUpdate() {
        return tempGroupMemberContainerforUpdate;
    }

    public void setTempGroupMemberContainerforUpdate(Map<String, GroupMemberDTO> tempTagMemberContainerforUpdate) {
        this.tempGroupMemberContainerforUpdate = tempTagMemberContainerforUpdate;
    }

    private Map<String, Long> processingLikes = new ConcurrentHashMap<String, Long>();

    public Map<String, Long> getProcessingLikes() {
        return processingLikes;
    }

    public void setProcessingLikes(Map<String, Long> processingLikes) {
        this.processingLikes = processingLikes;
    }
    private Map<String, MyFriendProfile> myFriendProfiles = new ConcurrentHashMap<String, MyFriendProfile>();

    public Map<String, MyFriendProfile> getMyFriendProfiles() {
        return myFriendProfiles;
    }

    public void setMyFriendProfiles(Map<String, MyFriendProfile> myFriendProfiles) {
        this.myFriendProfiles = myFriendProfiles;
    }
    private Map<String, MyFriendChatCallPanel> myFriendChatCallPanel = new ConcurrentHashMap<String, MyFriendChatCallPanel>();

    public Map<String, MyFriendChatCallPanel> getMyFriendChatCallPanel() {
        return myFriendChatCallPanel;
    }

    public void setMyFriendChatCallPanel(Map<String, MyFriendChatCallPanel> myFriendChatCallPanel) {
        this.myFriendChatCallPanel = myFriendChatCallPanel;
    }

    private Map<Long, CircleProfile> circleProfiles = new ConcurrentHashMap<Long, CircleProfile>();

    public Map<Long, CircleProfile> getCircleProfiles() {
        return circleProfiles;
    }

    public void setCircleProfiles(Map<Long, CircleProfile> circleProfiles) {
        this.circleProfiles = circleProfiles;
    }
    private Map<Integer, ConcurrentHashMap<String, UserBasicInfo>> inviteFriendsContainer = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBasicInfo>>();

    public Map<Integer, ConcurrentHashMap<String, UserBasicInfo>> getInviteFriendsContainer() {
        return inviteFriendsContainer;
    }

    public void setInviteFriendsContainer(Map<Integer, ConcurrentHashMap<String, UserBasicInfo>> inviteFriendsContainer) {
        this.inviteFriendsContainer = inviteFriendsContainer;
    }
    private Map<String, EmototiconMapper> tempEmotionsPacket = new ConcurrentHashMap<String, EmototiconMapper>();

    public Map<String, EmototiconMapper> getTempEmotionsPacket() {
        return tempEmotionsPacket;
    }

    public void setTempEmotionsPacket(Map<String, EmototiconMapper> tempEmotionsPacket) {
        this.tempEmotionsPacket = tempEmotionsPacket;
    }

//    private Map<String, SmsListMapper> tempSmsListPacket = new ConcurrentHashMap<String, SmsListMapper>();
//
//    public Map<String, SmsListMapper> getTempSmsListPacket() {
//        return tempSmsListPacket;
//    }
//
//    public void setTempSmsListPacket(Map<String, SmsListMapper> tempSmsListPacket) {
//        this.tempSmsListPacket = tempSmsListPacket;
//    }
    private Map<String, Map<String, UserBasicInfo>> friendsContactList = new ConcurrentHashMap<String, Map<String, UserBasicInfo>>();

    public Map<String, Map<String, UserBasicInfo>> getFriendsContactList() {
        return friendsContactList;
    }

    public void setFriendsContactList(Map<String, Map<String, UserBasicInfo>> friendsContactList) {
        this.friendsContactList = friendsContactList;
    }
    private Map<Long, Map<String, Boolean>> tempCircleMembersStore = new ConcurrentHashMap<Long, Map<String, Boolean>>();

    public Map<Long, Map<String, Boolean>> getTempCircleMembersStore() {
        return tempCircleMembersStore;
    }

    public void setTempCircleMembersStore(Map<Long, Map<String, Boolean>> tempGroupMembersStore) {
        this.tempCircleMembersStore = tempGroupMembersStore;
    }

//    private Map<Long, Map<String, Long>> tempGroupMembersStore = new ConcurrentHashMap<Long, Map<String, Long>>();
//
//    public Map<Long, Map<String, Long>> getTempGroupMembersStore() {
//        return tempGroupMembersStore;
//    }
//
//    public void setTempGroupMembersStore(Map<Long, Map<String, Long>> tempTagMembersStore) {
//        this.tempGroupMembersStore = tempTagMembersStore;
//    }
    private Map<String, CircleMembersMap> tempCircleMembers = new ConcurrentHashMap<String, CircleMembersMap>();

    public Map<String, CircleMembersMap> getTempCircleMembers() {
        return tempCircleMembers;
    }

    public void setTempCircleMembers(Map<String, CircleMembersMap> tempGroupMembers) {
        this.tempCircleMembers = tempGroupMembers;
    }

    private Map<String, JsonFields> notificationsMap = new ConcurrentHashMap<String, JsonFields>();

    public Map<String, JsonFields> getNotificationsMap() {
        return notificationsMap;
    }

    public void setNotificationsMap(Map<String, JsonFields> notificationsMap) {
        this.notificationsMap = notificationsMap;
    }

    private Map<String, JsonFields> tempNotifications = new ConcurrentHashMap<String, JsonFields>();

    public Map<String, JsonFields> getTempNotifications() {
        return tempNotifications;
    }

    public void setTempNotifications(Map<String, JsonFields> tempNotifications) {
        this.tempNotifications = tempNotifications;
    }

    private Map<Long, SingleCirclePanel> singleCircleInMyCircle = new ConcurrentHashMap<Long, SingleCirclePanel>();

    public Map<Long, SingleCirclePanel> getSingleCircleInMyCircle() {
        return singleCircleInMyCircle;
    }

    public void setSingleCircleInMyCircle(Map<Long, SingleCirclePanel> singleGroupInMyGroup) {
        this.singleCircleInMyCircle = singleGroupInMyGroup;
    }
    private Map<Long, SingleCirclePanel> singleCircleInCircleOfMe = new ConcurrentHashMap<Long, SingleCirclePanel>();

    public Map<Long, SingleCirclePanel> getSingleCircleInCircleOfMe() {
        return singleCircleInCircleOfMe;
    }

    public void setSingleCircleInCircleOfMe(Map<Long, SingleCirclePanel> singleGroupInGroupOfMe) {
        this.singleCircleInCircleOfMe = singleGroupInGroupOfMe;
    }

    private Map<Long, GroupPanel> groupPanelMap = new ConcurrentHashMap<Long, GroupPanel>();

    public Map<Long, GroupPanel> getGroupPanelMap() {
        return groupPanelMap;
    }

    public void setGroupPanelMap(Map<Long, GroupPanel> aTagPanelMap) {
        groupPanelMap = aTagPanelMap;
    }

//    private Set<String> recentTagNameSet = new HashSet<String>();
//    
//    public Set<String> getRecentTagNameSet() {
//        return recentTagNameSet;
//    }
//
//    public void setRecentTagNameSet(Set<String> recentTagNameSet) {
//        this.recentTagNameSet = recentTagNameSet;
//    }    
    private Map<String, SingleFriendPanel> singleFriendPanel = new ConcurrentHashMap<String, SingleFriendPanel>();

    public Map<String, SingleFriendPanel> getSingleFriendPanel() {
        return singleFriendPanel;
    }

    public void setSingleFriendPanel(Map<String, SingleFriendPanel> singleFriendPanel) {
        this.singleFriendPanel = singleFriendPanel;
    }
    private Map<String, SingleAddFriendPanel> singleFriendPanelInFeed = new ConcurrentHashMap<String, SingleAddFriendPanel>();

    public Map<String, SingleAddFriendPanel> getSingleFriendPanelInFeed() {
        return singleFriendPanelInFeed;
    }

    public void setSingleFriendPanelInFeed(Map<String, SingleAddFriendPanel> singleFriendPanelInFeed) {
        this.singleFriendPanelInFeed = singleFriendPanelInFeed;
    }

    private Map<String, UserBasicInfo> unknowonProfileMap = new ConcurrentHashMap<String, UserBasicInfo>();

    public Map<String, UserBasicInfo> getUnknowonProfileMap() {
        return unknowonProfileMap;
    }

    public void setUnknowonProfileMap(Map<String, UserBasicInfo> unknowonProfileMap) {
        this.unknowonProfileMap = unknowonProfileMap;
    }

    private Map<Long, Map<Long, WorkInfoDTO>> workInfoMap = new ConcurrentHashMap<Long, Map<Long, WorkInfoDTO>>();

    public Map<Long, Map<Long, WorkInfoDTO>> getWorkInfoMap() {
        return workInfoMap;
    }

    public void setWorkInfoMap(Map<Long, Map<Long, WorkInfoDTO>> workInfoMap) {
        this.workInfoMap = workInfoMap;
    }

    private Map<Long, Map<Long, EducationInfoDTO>> educationInfoMap = new ConcurrentHashMap<Long, Map<Long, EducationInfoDTO>>();

    public Map<Long, Map<Long, EducationInfoDTO>> getEducationInfoMap() {
        return educationInfoMap;
    }

    public void setEducationInfoMap(Map<Long, Map<Long, EducationInfoDTO>> educationInfoMap) {
        this.educationInfoMap = educationInfoMap;
    }
    private Map<Long, Map<Long, SkillInfoDTO>> skillInfoMap = new ConcurrentHashMap<Long, Map<Long, SkillInfoDTO>>();

    public Map<Long, Map<Long, SkillInfoDTO>> getSkillInfoMap() {
        return skillInfoMap;
    }

    public void setSkillInfoMap(Map<Long, Map<Long, SkillInfoDTO>> skillInfoMap) {
        this.skillInfoMap = skillInfoMap;
    }

    private Map<String, ArrayList<String>> tempRecoverySuggestionsMap = new ConcurrentHashMap<String, ArrayList<String>>();

    public Map<String, ArrayList<String>> getTempRecoverySuggestionsMap() {
        return tempRecoverySuggestionsMap;
    }

    public void setTempRecoverySuggestionsMap(Map<String, ArrayList<String>> tempRecoverySuggestionsMap) {
        this.tempRecoverySuggestionsMap = tempRecoverySuggestionsMap;
    }

    private Map<String, SingleChatHistoryPanel> singleChatHistoryPanel = new ConcurrentHashMap<String, SingleChatHistoryPanel>();

    public Map<String, SingleChatHistoryPanel> getSingleChatHistoryPanel() {
        return singleChatHistoryPanel;
    }

    public void setSingleChatHistoryPanel(Map<String, SingleChatHistoryPanel> singleChatHistoryPanel) {
        this.singleChatHistoryPanel = singleChatHistoryPanel;
    }

    private Map<String, SingleNotification> singleNotificationHistoryPanel = new ConcurrentHashMap<String, SingleNotification>();

    public Map<String, SingleNotification> getSingleNotificationHistoryPanel() {
        return singleNotificationHistoryPanel;
    }

    public void setSingleNotificationHistorPanel(Map<String, SingleNotification> singleNotificationHistoryPanel) {
        this.singleNotificationHistoryPanel = singleNotificationHistoryPanel;
    }

    private Map<String, SingleCallHistoryPanel> singleCallHistoryPanel = new ConcurrentHashMap<String, SingleCallHistoryPanel>();

    public Map<String, SingleCallHistoryPanel> getSingleCallHistoryPanel() {
        return singleCallHistoryPanel;
    }

    public void setSingleCallHistoryPanel(Map<String, SingleCallHistoryPanel> singleCallHistoryPanel) {
        this.singleCallHistoryPanel = singleCallHistoryPanel;
    }

    private Map<Long, GroupInfoDTO> group_hash_map = new ConcurrentHashMap<>();

    public Map<Long, GroupInfoDTO> getGroup_hash_map() {
        return group_hash_map;
    }

    public void setGroup_hash_map(Map<Long, GroupInfoDTO> tag_hash_map) {
        this.group_hash_map = tag_hash_map;
    }

    public void clearHashMaps() {
        getMyFriendProfiles().clear();
        getGroupPanelMap().clear();
        getCircleProfiles().clear();
        getSingleFriendPanel().clear();
        getSingleFriendPanelInFeed().clear();
        getSingleCircleInMyCircle().clear();
        getSingleCircleInCircleOfMe().clear();
        getUnknowonProfileMap().clear();
        getWorkInfoMap().clear();
        getEducationInfoMap().clear();
        getSkillInfoMap().clear();
        getAlBumImages().clear();
    }

}

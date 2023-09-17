/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.stores;

import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.utility.HelperMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.dao.InsertIntoContactListTable;
//import local.ua.UserAgentProfile;
import com.ipvision.service.utility.SortedArrayListUserBasicInfo;

/**
 *
 * @author faizahmed
 */
public class FriendList {

    private Map<String, UserBasicInfo> temp_phone_friend_hash_map = new ConcurrentHashMap<String, UserBasicInfo>();

    public Map<String, UserBasicInfo> getTemp_phone_friend_hash_map() {
        return temp_phone_friend_hash_map;
    }

    public void setTemp_phone_friend_hash_map(Map<String, UserBasicInfo> temp_phone_friend_hash_map) {
        this.temp_phone_friend_hash_map = temp_phone_friend_hash_map;
    }
    private Map<String, UserBasicInfo> friend_hash_map = new ConcurrentHashMap<String, UserBasicInfo>();

    public Map<String, UserBasicInfo> getFriend_hash_map() {
        return friend_hash_map;
    }

    public void setFriend_hash_map(Map<String, UserBasicInfo> friend_hash_map) {
        this.friend_hash_map = friend_hash_map;
    }


    private Map<String, UserBasicInfo> tempContactList = new ConcurrentHashMap<String, UserBasicInfo>();

    public Map<String, UserBasicInfo> getTempContactList() {
        return tempContactList;
    }

    public void setTempContactList(Map<String, UserBasicInfo> tempContactList) {
        this.tempContactList = tempContactList;
    }
    /*private Map<String, UserBasicInfo> tempFriendContactList = new ConcurrentHashMap<String, UserBasicInfo>();

     public Map<String, UserBasicInfo> getTempFriendContactList() {
     return tempFriendContactList;
     }

     public void setTempFriendContactList(Map<String, UserBasicInfo> tempFriendContactList) {
     this.tempFriendContactList = tempFriendContactList;
     }*/
    public static FriendList contactListObject;

    public static FriendList getInstance() {
        if (contactListObject == null) {
            contactListObject = new FriendList();
        }
        return contactListObject;
    }

    public void add_single_friend_entry(UserBasicInfo friendInfo) {
        if (FriendList.getInstance().getFriend_hash_map() != null && FriendList.getInstance().getFriend_hash_map().get(friendInfo.getUserIdentity()) != null) {
            FriendList.getInstance().getFriend_hash_map().remove(friendInfo.getUserIdentity());
        }
        friendInfo.setFullName(HelperMethods.getUserFullName(friendInfo));
        FriendList.getInstance().getFriend_hash_map().put(friendInfo.getUserIdentity(), friendInfo);

        List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
        contactList.add(FriendList.getInstance().getFriend_hash_map().get(friendInfo.getUserIdentity()));
        new InsertIntoContactListTable(contactList).start();
    }

    private SortedArrayListUserBasicInfo peopleYouMayKnow = new SortedArrayListUserBasicInfo();

    public SortedArrayListUserBasicInfo getPeopleYouMayKnow() {
        return peopleYouMayKnow;
    }

    public void setPeopleYouMayKnow(SortedArrayListUserBasicInfo peopleYouMayKnow) {
        this.peopleYouMayKnow = peopleYouMayKnow;
    }
}

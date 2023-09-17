/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
//import local.ua.UserAgentProfile;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.UserBasicInfo;

/**
 *
 * @author faizahmed
 */
public class FriendlistMapping extends FeedBackFields {

    private ArrayList<UserBasicInfo> contactList = null;

    public ArrayList<UserBasicInfo> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<UserBasicInfo> contactList) {
        this.contactList = contactList;
    }
}

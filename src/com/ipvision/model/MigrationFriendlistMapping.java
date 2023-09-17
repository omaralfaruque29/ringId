/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.model.UserBasicInfo;
import java.util.ArrayList;

/**
 *
 * @author Wasif Islam
 */
public class MigrationFriendlistMapping {
     private ArrayList<UserBasicInfo> friendList = null;

    public ArrayList<UserBasicInfo> getMigrationFriendList() {
        return friendList;
    }

    public void setMigrationFriendList(ArrayList<UserBasicInfo> friendList) {
        this.friendList = friendList;
    }
     
}

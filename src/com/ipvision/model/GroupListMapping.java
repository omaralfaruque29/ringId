/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.GroupInfoDTO;

/**
 *
 * @author Wasif Islam
 */
public class GroupListMapping extends FeedBackFields {
    
     private ArrayList<GroupInfoDTO> tagList = null;

    public ArrayList<GroupInfoDTO> getGroupList() {
        return tagList;
    }

    public void setGroupList(ArrayList<GroupInfoDTO> tagList) {
        this.tagList = tagList;
    }
     
}

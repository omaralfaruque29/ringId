/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.model.UserBasicInfo;
import java.util.ArrayList;
//import local.ua.UserAgentProfile;

/**
 *
 * @author faizahmed
 */
public class SearchFreindlListMapping {

    private Integer scat; //search category
    private ArrayList<UserBasicInfo> searchedcontaclist = null;

    public ArrayList<UserBasicInfo> getSearchedcontaclist() {
        return searchedcontaclist;
    }

    public void setSearchedcontaclist(ArrayList<UserBasicInfo> searchedcontaclist) {
        this.searchedcontaclist = searchedcontaclist;
    }

    public Integer getSearchCategory() {
        return scat;
    }

    public void setSearchCategory(Integer scat) {
        this.scat = scat;
    }
}

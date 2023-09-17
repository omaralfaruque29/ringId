/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.model.FeedBackFields;

/**
 *
 * @author Wasif Islam
 */
public class UserDetailsInfoDTO extends FeedBackFields{
    
    private JsonFields userDetails;

    public JsonFields getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(JsonFields userDetails) {
        this.userDetails = userDetails;
    }
    
    
}

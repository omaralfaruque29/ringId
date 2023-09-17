/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.model.SingleBookDetails;

/**
 *
 * @author Faiz
 */
public class ShareFeedResponse extends SingleBookDetails {

    private NewsFeedWithMultipleImage sharedFeed;

    public NewsFeedWithMultipleImage getSharedFeed() {
        return sharedFeed;
    }

    public void setSharedFeed(NewsFeedWithMultipleImage sharedFeed) {
        this.sharedFeed = sharedFeed;
    }

}

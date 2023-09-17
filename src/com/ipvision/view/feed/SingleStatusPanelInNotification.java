/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.stores.NewsFeedMaps;

/**
 *
 * @author Shahadat
 */
public class SingleStatusPanelInNotification extends SingleFeedStructure {
    
    public SingleStatusPanelInNotification(final NewsFeedWithMultipleImage statusDto) {
        this.statusDto = statusDto;
        NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().put(this.statusDto.getNfId(), this);

//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        addOrChangeActivityInfo();
        addFriendInfo();
        if (statusDto.getTs() != null && statusDto.getTs() > 0 && statusDto.getWhoShare() != null && statusDto.getWhoShare().size() > 0) {
            addFriendDetailsifShared();
        } else {
            addStatusAndLocationInfor();
        }
        addLikeCommentsTexts();
        addImagePanelDetails();
        change_like_number();
        change_comments_number();
        change_shares_number();
        seperator.setVisible(false);
        FreindDetailsForFeeds.getInatance().sequenceNumberImagePane();
//            }
//        });
    }
    
}

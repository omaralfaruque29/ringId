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
 * @author Faiz
 */
public class SingleStatusPanelInBookHome extends SingleFeedStructure {

    public SingleStatusPanelInBookHome(final NewsFeedWithMultipleImage statusDto) {
        this.statusDto = statusDto;
        if (this.statusDto != null && this.statusDto.getNfId() != null) {
            NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().put(this.statusDto.getNfId(), this);
        }
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        addOrChangeActivityInfo();
        addFriendInfo();
        if (statusDto != null && statusDto.getTs() != null && statusDto.getTs() > 0 && statusDto.getWhoShare() != null && statusDto.getWhoShare().size() > 0) {
            addFriendDetailsifShared();
        } else {
            addStatusAndLocationInfor();
        }
        addLikeCommentsTexts();
        addImagePanelDetails();
        change_like_number();
        change_comments_number();
        change_shares_number();
    }
//        });
//    }

}

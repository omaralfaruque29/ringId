/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import java.awt.Dimension;
import com.ipvision.model.NewsFeedWithMultipleImage;

/**
 *
 * @author Dell
 */
public class SingleShareStatus extends SingleFeedStructure {

    public SingleShareStatus(NewsFeedWithMultipleImage statusDto) {
        this.statusDto = statusDto;
        // this.width = this.width + 18;
        shareFeedsContinerWrapper.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 0));

        addFriendInfo();

        if (statusDto.getTs() != null && statusDto.getTs() > 0 && statusDto.getWhoShare() != null && statusDto.getWhoShare().size() > 0) {
            addFriendDetailsifShared();
        } else {
            addStatusAndLocationInfor();
        }

        addImagePanelDetails();
        commentLikeBar.setVisible(false);
        seperator.setVisible(false);
        FreindDetailsForFeeds.getInatance().sequenceNumberImagePane();
        // FreindDetailsForFeeds.imagePaneMain.setVisible(false);
    }
}

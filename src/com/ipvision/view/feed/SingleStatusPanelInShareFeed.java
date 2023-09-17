/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.RingColorCode;
import javax.swing.border.MatteBorder;
import com.ipvision.model.NewsFeedWithMultipleImage;

/**
 *
 * @author Shahadat
 */
public class SingleStatusPanelInShareFeed extends SingleFeedStructure {

    public SingleStatusPanelInShareFeed(final NewsFeedWithMultipleImage statusDto, final Long parentNfID) {
        this.statusDto = statusDto;
        this.parentNfID = parentNfID;
        this.width = this.width - 50;
        //NewsFeedMaps.getInstance().getSingleStatusPanelInShareFeed().put(this.statusDto.getNfId(), this);

//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        addOrChangeActivityInfo();
        addFriendInfo();
        addStatusAndLocationInfor();
        addLikeCommentsTexts();
        addImagePanelDetails();
        change_like_number();
        change_comments_number();
        //change_shares_number();
        contentsHere.setBorder(new MatteBorder(1, 1, 0, 1, RingColorCode.COMMENTS_BORDER_COLOR));
        commentLikeBar.setBorder(null);
        sharedPanel.setVisible(false);
        seperator.setVisible(false);
        newCommentRows = 45;
        FreindDetailsForFeeds.getInatance().sequenceNumberImagePane();
        //FreindDetailsForFeeds.getInatance().imagePaneMain.setVisible(false);
        // FreindDetailsForFeeds.imagePaneMain.setVisible(false);
//            }
//        });
    }

}

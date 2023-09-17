/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.feed.SingleStatusPanelInNotification;
import com.ipvision.view.feed.SingleStatusPanelInShareFeed;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class FeedDeleteCommentRequest extends Thread {
//

    private Long statusId;
    private String commentId;
    private String singleCommentid;
    private int type;

    public FeedDeleteCommentRequest(Long statusId, String commentID, int type) {
        this.statusId = statusId;
        this.commentId = commentID;
        this.singleCommentid = statusId + commentID;
        this.type = type;
         setName(this.getClass().getName());

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"stId":16,"actn":183,"sId":"1396512223921nazmul","pckId":"1396512263101","cmnId":10}
                 {"actn":182,"pckId":"1409481486023ring8801717634317","sId":"1409481517136ring8801717634317","cmnId":4,"imgId":295}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(type);
                if (type == AppConstants.TYPE_DELETE_STATUS_COMMENT) {
                    pakToSend.setNfId(this.statusId);
                }
                pakToSend.setCmnId(this.commentId);
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {

                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (responsefields.getSuccess()) {
                            try {
                                if (type == AppConstants.TYPE_DELETE_STATUS_COMMENT) {
                                    if (NewsFeedMaps.getInstance().getComments().get(this.statusId) != null) {
                                        NewsFeedMaps.getInstance().getComments().get(this.statusId).remove(this.commentId);
                                    }

                                    JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusId);
                                    if (newsFeeds != null) {
                                        Long nc = newsFeeds.getNc();
                                        Long comments = (nc != null && nc > 0 ? nc - 1L : nc);
                                        newsFeeds.setNc(comments);

                                        Short ic = newsFeeds.getIc();
                                        Integer val = (ic != null ? ic : 0) - 1;
                                        newsFeeds.setIc(val.shortValue());
                                    }

                                    Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(statusId);

                                    SingleStatusPanelInBookHome singleStatusPanelInHome = null;
                                    SingleStatusPanelInMyBook singleStatusPanelInMyBook = null;
                                    SingleStatusPanelInFriendNewsFeed singleStatusPanelInFriendNewsFeed = null;
                                    SingleStatusPanelInCircleNewsFeed singleStatusPanelInCircleNewsFeed = null;
                                    SingleStatusPanelInNotification singleStatusPanelInNotification = null;
                                    SingleStatusPanelInShareFeed shareFeedPanelInHome = null;
                                    SingleStatusPanelInShareFeed shareFeedPanelInMyBook = null;
                                    SingleStatusPanelInShareFeed shareFeedPanelInFriendNewsFeed = null;
                                    SingleStatusPanelInShareFeed shareFeedPanelInCircleNewsFeed = null;
                                    SingleStatusPanelInShareFeed shareFeedPanelInNotification = null;

                                    if (parentNfId != null) {
                                        SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentNfId);
                                        SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentNfId);
                                        SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentNfId);
                                        SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentNfId);
                                        SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentNfId);

                                        if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(statusId)) {
                                            shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(statusId);
                                        }
                                        if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(statusId)) {
                                            shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(statusId);
                                        }
                                        if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(statusId)) {
                                            shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(statusId);
                                        }
                                        if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(statusId)) {
                                            shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(statusId);
                                        }
                                        if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(statusId)) {
                                            shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(statusId);
                                        }
                                    } else {
                                        singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(statusId);
                                        singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(statusId);
                                        singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(statusId);
                                        singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(statusId);
                                        singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(statusId);
                                    }

                                    if (singleStatusPanelInHome != null) {
                                        singleStatusPanelInHome.removeAComment(this.statusId, this.commentId);
                                        singleStatusPanelInHome.change_comments_number();
                                    }
                                    if (singleStatusPanelInMyBook != null) {
                                        singleStatusPanelInMyBook.removeAComment(this.statusId, this.commentId);
                                        singleStatusPanelInMyBook.change_comments_number();
                                    }

                                    if (singleStatusPanelInFriendNewsFeed != null) {
                                        singleStatusPanelInFriendNewsFeed.removeAComment(this.statusId, this.commentId);
                                        singleStatusPanelInFriendNewsFeed.change_comments_number();
                                    }

                                    if (singleStatusPanelInCircleNewsFeed != null) {
                                        singleStatusPanelInCircleNewsFeed.removeAComment(this.statusId, this.commentId);
                                        singleStatusPanelInCircleNewsFeed.change_comments_number();
                                    }

                                    if (singleStatusPanelInNotification != null) {
                                        singleStatusPanelInNotification.removeAComment(this.statusId, this.commentId);
                                        singleStatusPanelInNotification.change_comments_number();
                                    }

                                    if (shareFeedPanelInHome != null) {
                                        shareFeedPanelInHome.removeAComment(this.statusId, this.commentId);
                                        shareFeedPanelInHome.change_comments_number();
                                    }

                                    if (shareFeedPanelInMyBook != null) {
                                        shareFeedPanelInMyBook.removeAComment(this.statusId, this.commentId);
                                        shareFeedPanelInMyBook.change_comments_number();
                                    }

                                    if (shareFeedPanelInFriendNewsFeed != null) {
                                        shareFeedPanelInFriendNewsFeed.removeAComment(this.statusId, this.commentId);
                                        shareFeedPanelInFriendNewsFeed.change_comments_number();
                                    }

                                    if (shareFeedPanelInCircleNewsFeed != null) {
                                        shareFeedPanelInCircleNewsFeed.removeAComment(this.statusId, this.commentId);
                                        shareFeedPanelInCircleNewsFeed.change_comments_number();
                                    }

                                    if (shareFeedPanelInNotification != null) {
                                        shareFeedPanelInNotification.removeAComment(this.statusId, this.commentId);
                                        shareFeedPanelInNotification.change_comments_number();
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Ex DeleteCommentRequest-->" + e.getMessage());
                            }
                        } else {
                            HelperMethods.showPlaneDialogMessage("Failed!!" + responsefields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                HelperMethods.showPlaneDialogMessage("Can not process this comment delete request right now");
            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

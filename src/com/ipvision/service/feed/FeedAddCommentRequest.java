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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.view.feed.SingleFeedStructure;
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
public class FeedAddCommentRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedAddCommentRequest.class);
    private JsonFields commentDto;
    SingleFeedStructure singleFeedStructure;

    public FeedAddCommentRequest(JsonFields commentDto) {
        this.commentDto = commentDto;
        setName(this.getClass().getName());

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"stId":16,"actn":181,"sId":"1396510090303nazmul","comment":"this is a comment","pckId":"1396510637025"}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_ADD_STATUS_COMMENT);
                pakToSend.setNfId(commentDto.getNfId());
                pakToSend.setComment(commentDto.getComment());
                //   pakToSend.setUt(commentDto.getUt());

                Map<String, byte[]> packets = SendToServer.buildBrokenPacket(pakToSend, pakToSend.getAction(), pakId, ServerAndPortSettings.UPDATE_PORT);
                List<String> packetIds = new ArrayList<String>(packets.keySet());
                packetIds.add(pakId);
                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
                        SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (responsefields.getSuccess()) {
                            try {
                                commentDto.setCmnId(responsefields.getCmnId());
                                commentDto.setTm(responsefields.getTm());

                                if (NewsFeedMaps.getInstance().getComments().get(commentDto.getNfId()) == null) {
                                    Map<String, JsonFields> commentsJson = new ConcurrentHashMap<String, JsonFields>();
                                    commentsJson.put(responsefields.getCmnId(), commentDto);
                                    NewsFeedMaps.getInstance().getComments().put(commentDto.getNfId(), commentsJson);
                                } else {
                                    NewsFeedMaps.getInstance().getComments().get(commentDto.getNfId()).put(responsefields.getCmnId(), commentDto);
                                }

                                JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(commentDto.getNfId());
                                if (newsFeeds != null) {
                                    if (responsefields.getNc() == null) {
                                        Long nc = newsFeeds.getNc();
                                        Long comments = (nc != null ? nc : 0) + 1L;
                                        newsFeeds.setNc(comments);
                                    } else {
                                        newsFeeds.setNc(responsefields.getNc() + 1);
                                    }
                                    Short ic = newsFeeds.getIc();
                                    Integer val = (ic != null ? ic : 0) + 1;
                                    newsFeeds.setIc(val.shortValue());
                                }

                                Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(commentDto.getNfId());

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

                                    if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(commentDto.getNfId())) {
                                        shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(commentDto.getNfId());
                                    }
                                    if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(commentDto.getNfId())) {
                                        shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(commentDto.getNfId());
                                    }
                                    if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(commentDto.getNfId())) {
                                        shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(commentDto.getNfId());
                                    }
                                    if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(commentDto.getNfId())) {
                                        shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(commentDto.getNfId());
                                    }
                                    if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(commentDto.getNfId())) {
                                        shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(commentDto.getNfId());
                                    }
                                } else {
                                    singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(commentDto.getNfId());
                                    singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(commentDto.getNfId());
                                    singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(commentDto.getNfId());
                                    singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(commentDto.getNfId());
                                    singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(commentDto.getNfId());
                                }

                                if (singleStatusPanelInHome != null) {
                                    singleStatusPanelInHome.change_comments_number();
                                    singleStatusPanelInHome.addAComment(commentDto);
                                }
                                if (singleStatusPanelInMyBook != null) {
                                    singleStatusPanelInMyBook.change_comments_number();
                                    singleStatusPanelInMyBook.addAComment(commentDto);
                                }
                                if (singleStatusPanelInFriendNewsFeed != null) {
                                    singleStatusPanelInFriendNewsFeed.change_comments_number();
                                    singleStatusPanelInFriendNewsFeed.addAComment(commentDto);
                                }
                                if (singleStatusPanelInCircleNewsFeed != null) {
                                    singleStatusPanelInCircleNewsFeed.change_comments_number();
                                    singleStatusPanelInCircleNewsFeed.addAComment(commentDto);
                                }
                                if (singleStatusPanelInNotification != null) {
                                    singleStatusPanelInNotification.change_comments_number();
                                    singleStatusPanelInNotification.addAComment(commentDto);
                                }
                                if (shareFeedPanelInHome != null) {
                                    shareFeedPanelInHome.change_comments_number();
                                    shareFeedPanelInHome.addAComment(commentDto);
                                }
                                if (shareFeedPanelInMyBook != null) {
                                    shareFeedPanelInMyBook.change_comments_number();
                                    shareFeedPanelInMyBook.addAComment(commentDto);
                                }
                                if (shareFeedPanelInFriendNewsFeed != null) {
                                    shareFeedPanelInFriendNewsFeed.change_comments_number();
                                    shareFeedPanelInFriendNewsFeed.addAComment(commentDto);
                                }
                                if (shareFeedPanelInCircleNewsFeed != null) {
                                    shareFeedPanelInCircleNewsFeed.change_comments_number();
                                    shareFeedPanelInCircleNewsFeed.addAComment(commentDto);
                                }
                                if (shareFeedPanelInNotification != null) {
                                    shareFeedPanelInNotification.change_comments_number();
                                    shareFeedPanelInNotification.addAComment(commentDto);
                                }
                            } catch (Exception e) {
                                log.error("New Comment==>" + e.getMessage());
                            }

                        } else {
                            HelperMethods.showWarningDialogMessage(responsefields.getMessage());
                        }
                        SendToServer.removeAllBrokenPacketConfirmation(packetIds);
                        return;
                    }
                }
                HelperMethods.showWarningDialogMessage("Can not process this comment right now");
            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

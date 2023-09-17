/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
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
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz
 */
public class FeedLikeUnlikeStatusRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedLikeUnlikeStatusRequest.class);
    private int type;
    private Long statusId;
    SingleFeedStructure singleFeedStructure;
    long sfid;

    // private long ut;
    public FeedLikeUnlikeStatusRequest(int type, Long nfid, SingleFeedStructure singleFeedStructure1) {
        this.type = type;
        this.statusId = nfid;
        this.singleFeedStructure = singleFeedStructure1;
        setName(this.getClass().getSimpleName());
    }

    public FeedLikeUnlikeStatusRequest(int type, Long nfid, Long sfid, SingleFeedStructure singleFeedStructure1) {
        this.type = type;
        this.statusId = nfid;
        this.sfid = sfid;
        this.singleFeedStructure = singleFeedStructure1;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(type);
                pakToSend.setNfId(statusId);
                pakToSend.setSfId(this.sfid);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (response.getSuccess()) {
                            // {"actn":184,"pckId":"200000216081419566897960","sucs":true,"nfId":1217,"nl":0}
                            try {
                                if (type == AppConstants.TYPE_LIKE_STATUS) {

                                    SingleMemberInCircleDto addMe = new SingleMemberInCircleDto();
                                    addMe.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                    addMe.setFullName(MyFnFSettings.userProfile.getFullName());
                                    //addMe.setLastName(MyFnFSettings.userProfile.getLastName());

                                    if (NewsFeedMaps.getInstance().getLikes().get(statusId.toString()) == null) {
                                        Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                                        commentsJson.put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                        NewsFeedMaps.getInstance().getLikes().put(statusId.toString(), commentsJson);
                                    } else {
                                        NewsFeedMaps.getInstance().getLikes().get(statusId.toString()).put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                    }

                                    JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusId);
                                    if (newsFeeds != null) {
                                        if (response.getNl() == null) {
                                            Long nl = newsFeeds.getNl();
                                            Long likes = (nl != null ? nl : 0) + 1L;
                                            newsFeeds.setNl(likes);
                                        } else {
                                            newsFeeds.setNl(response.getNl() + 1);
                                        }
                                        Short il = newsFeeds.getIl();
                                        Integer val = (il != null ? il : 0) + 1;
                                        newsFeeds.setIl(val.shortValue());
                                    }

                                } else if (type == AppConstants.TYPE_UNLIKE_STATUS) {

                                    if (NewsFeedMaps.getInstance().getLikes() != null && NewsFeedMaps.getInstance().getLikes().get(statusId.toString()) != null) {
                                        if (NewsFeedMaps.getInstance().getLikes().get(statusId.toString()) != null) {
                                            NewsFeedMaps.getInstance().getLikes().get(statusId.toString()).remove(MyFnFSettings.LOGIN_USER_ID);
                                        } else {
                                            NewsFeedMaps.getInstance().getLikes().remove(statusId.toString());
                                        }
                                    }

                                    if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusId) != null) {
                                        JsonFields newsFeeds = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusId);
                                        Long nl = newsFeeds.getNl();
                                        Long likes = (nl != null && nl > 0 ? nl - 1L : nl);
                                        newsFeeds.setNl(likes);

                                        Short il = newsFeeds.getIl();
                                        Integer val = (il != null && il > 0 ? il - 1 : il);
                                        newsFeeds.setIl(val.shortValue());
                                    }
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
                                    singleStatusPanelInHome.change_like_number();
                                }
                                if (singleStatusPanelInMyBook != null) {
                                    singleStatusPanelInMyBook.change_like_number();
                                }
                                if (singleStatusPanelInFriendNewsFeed != null) {
                                    singleStatusPanelInFriendNewsFeed.change_like_number();
                                }
                                if (singleStatusPanelInCircleNewsFeed != null) {
                                    singleStatusPanelInCircleNewsFeed.change_like_number();
                                }
                                if (singleStatusPanelInNotification != null) {
                                    singleStatusPanelInNotification.change_like_number();
                                }
                                if (shareFeedPanelInHome != null) {
                                    shareFeedPanelInHome.change_like_number();
                                }
                                if (shareFeedPanelInMyBook != null) {
                                    shareFeedPanelInMyBook.change_like_number();
                                }
                                if (shareFeedPanelInFriendNewsFeed != null) {
                                    shareFeedPanelInFriendNewsFeed.change_like_number();
                                }
                                if (shareFeedPanelInCircleNewsFeed != null) {
                                    shareFeedPanelInCircleNewsFeed.change_like_number();
                                }
                                if (shareFeedPanelInNotification != null) {
                                    shareFeedPanelInNotification.change_like_number();
                                }

                            } catch (Exception e) {
                                //e.printStackTrace();
                                log.error("Exception in FeedLikeUnlikeStatusRequest ==>" + e.getMessage());
                                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                                MyfnfHashMaps.getInstance().getProcessingLikes().remove(statusId.toString());
                                return;
                            }

                        } else {
                            HelperMethods.showWarningDialogMessage(response.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        MyfnfHashMaps.getInstance().getProcessingLikes().remove(statusId.toString());

                        return;
                    }

                }
                HelperMethods.showWarningDialogMessage("Can not process like/unlike right now");
                // JOptionPane.showConfirmDialog(null, "Can not process like/unlike right now", "Like/Unlike failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                MyfnfHashMaps.getInstance().getProcessingLikes().remove(statusId.toString());
            } catch (Exception e) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

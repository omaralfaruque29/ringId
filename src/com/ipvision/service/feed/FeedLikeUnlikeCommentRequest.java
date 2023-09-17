/*
 * To change this template, choose Tools | Templates
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
import com.ipvision.view.feed.SingleCommentView;
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
 * @author Faiz Ahmed
 */
public class FeedLikeUnlikeCommentRequest extends Thread {

    private int type;
    private Long statusId;
    private String commentId;
    private String mapPak;
    private int lkd;

    public FeedLikeUnlikeCommentRequest(int type, Long statusId, String commentId, String key) {
        this.type = type;
        this.statusId = statusId;
        this.commentId = commentId;
        this.mapPak = key;
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
                pakToSend.setCmnId(commentId);
                pakToSend.setNfId(statusId);
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (response.getSuccess()) {
                            try {
                                if (type == AppConstants.TYPE_LIKE_COMMENT) {
                                    SingleMemberInCircleDto addMe = new SingleMemberInCircleDto();
                                    addMe.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                    addMe.setFullName(MyFnFSettings.userProfile.getFullName());
                                    //addMe.setLastName(MyFnFSettings.userProfile.getLastName());

                                    if (NewsFeedMaps.getInstance().getLikes().get(mapPak) == null) {
                                        Map<String, SingleMemberInCircleDto> commentsJson = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
                                        commentsJson.put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                        NewsFeedMaps.getInstance().getLikes().put(mapPak, commentsJson);
                                    } else {
                                        NewsFeedMaps.getInstance().getLikes().get(mapPak).put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                    }

                                    if (NewsFeedMaps.getInstance().getComments().get(statusId) != null) {
                                        JsonFields comment = NewsFeedMaps.getInstance().getComments().get(statusId).get(commentId);
                                        if (comment != null) {
                                            Long tl = comment.getTl();
                                            Long likes = (tl != null ? tl : 0) + 1L;
                                            comment.setTl(likes);

                                            Short il = comment.getIl();
                                            Integer val = (il != null ? il : 0) + 1;
                                            comment.setIl(val.shortValue());
                                        }
                                    }

                                } else if (type == AppConstants.TYPE_UNLIKE_COMMENT) {
                                    if (NewsFeedMaps.getInstance().getLikes().get(mapPak) != null) {
                                        NewsFeedMaps.getInstance().getLikes().get(mapPak).remove(MyFnFSettings.LOGIN_USER_ID);
                                    }
                                    if (NewsFeedMaps.getInstance().getComments().get(statusId) != null) {
                                        JsonFields comment = NewsFeedMaps.getInstance().getComments().get(statusId).get(commentId);
                                        if (comment != null) {
                                            Long tl = comment.getTl();
                                            Long likes = (tl != null && tl > 0 ? tl - 1L : tl);
                                            comment.setTl(likes);

                                            Short il = comment.getIl();
                                            Integer val = (il != null && il > 0 ? il - 1 : il);
                                            comment.setIl(val.shortValue());
                                        }
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
                                    SingleCommentView singleCommentView = singleStatusPanelInHome.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (singleStatusPanelInMyBook != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInMyBook.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (singleStatusPanelInFriendNewsFeed != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInFriendNewsFeed.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (singleStatusPanelInCircleNewsFeed != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInCircleNewsFeed.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (singleStatusPanelInNotification != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInNotification.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (shareFeedPanelInHome != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInHome.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (shareFeedPanelInMyBook != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInMyBook.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (shareFeedPanelInFriendNewsFeed != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInFriendNewsFeed.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (shareFeedPanelInCircleNewsFeed != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInCircleNewsFeed.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                                if (shareFeedPanelInNotification != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInNotification.getCommentsOfthisPost().get(mapPak);
                                    if (singleCommentView != null) {
                                        singleCommentView.change_like_number();
                                    }
                                }
                            } catch (Exception e) {
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
                MyfnfHashMaps.getInstance().getProcessingLikes().remove(statusId.toString());
            } catch (Exception e) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

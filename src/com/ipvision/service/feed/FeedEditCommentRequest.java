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
import com.ipvision.view.feed.SingleCommentView;
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
import com.ipvision.view.feed.NewComment;
import com.ipvision.view.utility.FeedUtils;

/**
 *
 * @author Faiz Ahmed
 */
public class FeedEditCommentRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedEditCommentRequest.class);
    private JsonFields commentDto;
    private NewComment newComment;

    public FeedEditCommentRequest(JsonFields commentDto) {
        this.commentDto = commentDto;
        setName(this.getClass().getSimpleName());
    }

    public FeedEditCommentRequest(JsonFields commentDto, NewComment newComment) {
        this.commentDto = commentDto;
        this.newComment = newComment;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"actn":189,
                 * "pckId":"1404195891153ring8801617634317",
                 * "sId":"1404195893925ring8801617634317",
                 * "stId":613,
                 * "cmn":"Random comment 731 by ashraful",
                 * "cmnId":538}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_EDIT_STATUS_COMMENT);
                pakToSend.setNfId(commentDto.getNfId());
                pakToSend.setComment(commentDto.getComment());
                pakToSend.setCmnId(commentDto.getCmnId());
                // SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);

                Map<String, byte[]> packets = SendToServer.buildBrokenPacket(pakToSend, pakToSend.getAction(), pakId, ServerAndPortSettings.UPDATE_PORT);
                List<String> packetIds = new ArrayList<String>(packets.keySet());
                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

//                for (int i = 1; i <= 7; i++) {
//                    Thread.sleep(500);
//                    if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
//                        SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
//                    } else {
                String makeResponsepak = FeedUtils.makeResponsePacket(pakId, AppConstants.TYPE_EDIT_STATUS_COMMENT);
                for (int i = 1; i <= 20; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak) == null) {
                        if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
                            if (i % 5 == 0) {
                                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                            }
                        }
                    } else {
                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak);
                        if (responsefields.getSuccess()) {
                            try {
                                if (NewsFeedMaps.getInstance().getComments().get(commentDto.getNfId()) != null) {
                                    JsonFields comment = NewsFeedMaps.getInstance().getComments().get(commentDto.getNfId()).get(commentDto.getCmnId());
                                    comment.setComment(commentDto.getComment());
                                }

                                String singleCommentId = HelperMethods.makeStatusIDCommentIDKey(commentDto.getNfId(), commentDto.getCmnId());

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
                                    SingleCommentView singleCommentView = singleStatusPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (singleStatusPanelInMyBook != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (singleStatusPanelInFriendNewsFeed != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (singleStatusPanelInCircleNewsFeed != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (singleStatusPanelInNotification != null) {
                                    SingleCommentView singleCommentView = singleStatusPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (shareFeedPanelInHome != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInHome.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (shareFeedPanelInMyBook != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInMyBook.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (shareFeedPanelInFriendNewsFeed != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInFriendNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (shareFeedPanelInCircleNewsFeed != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInCircleNewsFeed.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                                if (shareFeedPanelInNotification != null) {
                                    SingleCommentView singleCommentView = shareFeedPanelInNotification.getCommentsOfthisPost().get(singleCommentId);
                                    if (singleCommentView != null) {
                                        singleCommentView.changeComment();
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Edit comment==>" + e.getMessage());
                            }
                            if (newComment != null && !newComment.isVisible()) {
                                newComment.setVisible(true);
                            }
                        } else {
                            HelperMethods.showWarningDialogMessage("Can not process this comment right now");
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(makeResponsepak);
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

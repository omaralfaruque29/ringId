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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.feed.SingleStatusPanelInNotification;
import com.ipvision.view.feed.SingleStatusPanelInShareFeed;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.FeedUtils;

/**
 *
 * @author Wasif Islam
 */
public class FeedEditStatusRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedEditStatusRequest.class);
    private NewsFeedWithMultipleImage statusDto;

    public FeedEditStatusRequest(NewsFeedWithMultipleImage statusDto) {
        this.statusDto = statusDto;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                //{"actn":178,"pckId":"1407063378264ring8801717634317","sId":"1407063388764ring8801717634317","stId":2908,"sts":"EID MUBARAK"}
                statusDto.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                statusDto.setAction(AppConstants.TYPE_EDIT_STATUS);
                String pakId = SendToServer.getRanDomPacketID();
                statusDto.setPacketId(pakId);

                Map<String, byte[]> packets = SendToServer.buildBrokenPacket(statusDto, statusDto.getAction(), pakId, ServerAndPortSettings.UPDATE_PORT);
                List<String> packetIds = new ArrayList<String>(packets.keySet());
                packetIds.add(pakId);
                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

//                for (int i = 1; i <= 5; i++) {
//                    Thread.sleep(500);
//                    if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
//                        SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                String makeResponsepak = FeedUtils.makeResponsePacket(pakId, AppConstants.TYPE_EDIT_STATUS);
                for (int i = 1; i <= 20; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak) == null) {
                        if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
                            if (i % 5 == 0) {
                                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                            }
                        }
                    } else {
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak);
                        JsonFields fields = HelperMethods.getJsonFields(feedBackFields.getMessage());
                        if (fields != null && feedBackFields.getSuccess() && fields.getFriendIdentity() == null && fields.getCircleId() == null) {
                            Long parentNfId = NewsFeedMaps.getInstance().getParentFeedMap().get(fields.getNfId());

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

                                if (panelInHome != null && panelInHome.getSingleShareFeedPanel().containsKey(fields.getNfId())) {
                                    shareFeedPanelInHome = panelInHome.getSingleShareFeedPanel().get(fields.getNfId());
                                }
                                if (panelInMyBook != null && panelInMyBook.getSingleShareFeedPanel().containsKey(fields.getNfId())) {
                                    shareFeedPanelInMyBook = panelInMyBook.getSingleShareFeedPanel().get(fields.getNfId());
                                }
                                if (panelInFriendNewsFeed != null && panelInFriendNewsFeed.getSingleShareFeedPanel().containsKey(fields.getNfId())) {
                                    shareFeedPanelInFriendNewsFeed = panelInFriendNewsFeed.getSingleShareFeedPanel().get(fields.getNfId());
                                }
                                if (panelInCircleNewsFeed != null && panelInCircleNewsFeed.getSingleShareFeedPanel().containsKey(fields.getNfId())) {
                                    shareFeedPanelInCircleNewsFeed = panelInCircleNewsFeed.getSingleShareFeedPanel().get(fields.getNfId());
                                }
                                if (panelInNotification != null && panelInNotification.getSingleShareFeedPanel().containsKey(fields.getNfId())) {
                                    shareFeedPanelInNotification = panelInNotification.getSingleShareFeedPanel().get(fields.getNfId());
                                }
                            } else {
                                singleStatusPanelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(fields.getNfId());
                                singleStatusPanelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(fields.getNfId());
                                singleStatusPanelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(fields.getNfId());
                                singleStatusPanelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(fields.getNfId());
                                singleStatusPanelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(fields.getNfId());
                            }

                            if (singleStatusPanelInHome != null) {
                                singleStatusPanelInHome.addStatusAndLocationInfor();
                            }
                            if (singleStatusPanelInMyBook != null) {
                                singleStatusPanelInMyBook.addStatusAndLocationInfor();
                            }
                            if (singleStatusPanelInFriendNewsFeed != null) {
                                singleStatusPanelInFriendNewsFeed.addStatusAndLocationInfor();
                            }
                            if (singleStatusPanelInCircleNewsFeed != null) {
                                singleStatusPanelInCircleNewsFeed.addStatusAndLocationInfor();
                            }
                            if (singleStatusPanelInNotification != null) {
                                singleStatusPanelInNotification.addStatusAndLocationInfor();
                            }
                            if (shareFeedPanelInHome != null) {
                                shareFeedPanelInHome.addStatusAndLocationInfor();
                            }
                            if (shareFeedPanelInMyBook != null) {
                                shareFeedPanelInMyBook.addStatusAndLocationInfor();
                            }
                            if (shareFeedPanelInFriendNewsFeed != null) {
                                shareFeedPanelInFriendNewsFeed.addStatusAndLocationInfor();
                            }
                            if (shareFeedPanelInCircleNewsFeed != null) {
                                shareFeedPanelInCircleNewsFeed.addStatusAndLocationInfor();
                            }
                            if (shareFeedPanelInNotification != null) {
                                shareFeedPanelInNotification.addStatusAndLocationInfor();
                            }
                        } else {
                            HelperMethods.showWarningDialogMessage(fields.getVersionMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(makeResponsepak);
                        SendToServer.removeAllBrokenPacketConfirmation(packetIds);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.error("Can not edit this status==>" + ex.getMessage());
                HelperMethods.showWarningDialogMessage("Can not edit this status right now");
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

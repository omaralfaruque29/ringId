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
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.myprofile.MyBookHome;
import com.ipvision.view.feed.SingleFeedStructure;
import com.ipvision.view.feed.SingleNotificationDialog;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.feed.SingleStatusPanelInNotification;
import com.ipvision.model.FeedBackFields;

import com.ipvision.view.circle.CircleProfile;

import com.ipvision.model.SingleBookDetails;

import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class FeedDeleteStatusRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedDeleteStatusRequest.class);
    private Long statusId;
    private String friendId;
    private Long circleId;
    private Long parentId;

    public FeedDeleteStatusRequest(SingleFeedStructure singleFeedStructure) {
        this.statusId = singleFeedStructure.statusDto.getNfId();
        this.parentId = singleFeedStructure.parentNfID;
         setName(this.getClass().getName());

    }

    public FeedDeleteStatusRequest(Long statusId, String friendId) {
        this.friendId = friendId;
        this.statusId = statusId;
         setName(this.getClass().getName());
    }

    public FeedDeleteStatusRequest(Long statusId, Long circleId) {
        this.circleId = circleId;
        this.statusId = statusId;
         setName(this.getClass().getName());
    }

    private MyBookHome getMyBookHome() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyFeeds();
        } else {
            return null;
        }
    }

    private AllFeedsView getAllFeedView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getAllFeedsView();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {

            try {
                /*
                 {"stId":17,"actn":179,"sId":"1396510090303nazmul","pckId":"1396510119413"}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_DELETE_STATUS);
                pakToSend.setNfId(statusId);
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
                                NewsFeedMaps.getInstance().getAllNewsFeeds().remove(statusId);
                                NewsFeedMaps.getInstance().getAllNewfeedId().removeData(statusId);
                                NewsFeedMaps.getInstance().getComments().remove(statusId);
                                NewsFeedMaps.getInstance().getMyNewsFeedId().removeData(statusId);

                                if (friendId != null && !NewsFeedMaps.getInstance().getFriendNewsFeedId().isEmpty() && NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId) != null) {
                                    NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId).removeData(statusId);
                                }

                                if (circleId != null && !NewsFeedMaps.getInstance().getCircleNewsFeedId().isEmpty() && NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId) != null) {
                                    NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId).removeData(statusId);
                                }

                                if (parentId != null && !NewsFeedMaps.getInstance().getShareNewsFeedId().isEmpty() && NewsFeedMaps.getInstance().getShareNewsFeedId().get(parentId) != null) {
                                    NewsFeedMaps.getInstance().getShareNewsFeedId().get(parentId).removeData(statusId);
                                }

                                NewsFeedMaps.getInstance().getNotificationNewsFeedId().remove(statusId);

                                if (parentId != null) {
                                    NewsFeedWithMultipleImage parentNewsFeed = NewsFeedMaps.getInstance().getAllNewsFeeds().get(parentId);
                                    if (parentNewsFeed != null && parentNewsFeed.getWhoShare() != null) {
                                        for (int j = 0; j < parentNewsFeed.getWhoShare().size(); j++) {
                                            SingleBookDetails sharedFeed = parentNewsFeed.getWhoShare().get(j);
                                            if (sharedFeed.getNfId().equals(statusId)) {
                                                parentNewsFeed.getWhoShare().remove(sharedFeed);
                                                parentNewsFeed.setTs(parentNewsFeed.getTs() - 1);
                                                parentNewsFeed.setNs(parentNewsFeed.getNs() - 1);

                                                Short is = parentNewsFeed.getIShare();
                                                Integer val = (is != null ? is : 0) - 1;
                                                parentNewsFeed.setIShare(val.shortValue());

                                                break;
                                            }
                                        }
                                    }
                                }

                                if (getAllFeedView() != null) {
                                    getAllFeedView().removeIsAlreadyExist(statusId);
                                }

                                if (getMyBookHome() != null) {
                                    getMyBookHome().removeIsAlreadyExist(statusId);
                                }

                                if (friendId != null) {
                                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId);
                                    if (friendProfile != null && friendProfile.myFriendNewsFeedPanel != null) {
                                        friendProfile.myFriendNewsFeedPanel.removeIsAlreadyExist(statusId);
                                    }
                                }

                                if (circleId != null) {
                                    CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
                                    if (circleProfile != null && circleProfile.getCircleNewsFeedPanel() != null) {
                                        circleProfile.getCircleNewsFeedPanel().removeIsAlreadyExist(statusId);
                                    }
                                }

                                if (SingleNotificationDialog.getInstance() != null
                                        && NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(statusId) != null
                                        && SingleNotificationDialog.getInstance().getContent().getComponentZOrder(NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(statusId)) >= 0) {
                                    SingleNotificationDialog.getInstance().hideThis();
                                    NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().remove(statusId);
                                }

                                if (parentId != null) {
                                    SingleStatusPanelInBookHome panelInHome = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(parentId);
                                    SingleStatusPanelInMyBook panelInMyBook = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(parentId);
                                    SingleStatusPanelInFriendNewsFeed panelInFriendNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(parentId);
                                    SingleStatusPanelInCircleNewsFeed panelInCircleNewsFeed = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(parentId);
                                    SingleStatusPanelInNotification panelInNotification = NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().get(parentId);

                                    if (panelInHome != null) {
                                        panelInHome.change_shares_number();
                                        panelInHome.removeAShareFeed(statusId);
                                    }
                                    if (panelInMyBook != null) {
                                        panelInMyBook.change_shares_number();
                                        panelInMyBook.removeAShareFeed(statusId);
                                    }
                                    if (panelInFriendNewsFeed != null) {
                                        panelInFriendNewsFeed.change_shares_number();
                                        panelInFriendNewsFeed.removeAShareFeed(statusId);
                                    }
                                    if (panelInCircleNewsFeed != null) {
                                        panelInCircleNewsFeed.change_shares_number();
                                        panelInCircleNewsFeed.removeAShareFeed(statusId);
                                    }
                                    if (panelInNotification != null) {
                                        panelInNotification.change_shares_number();
                                        panelInNotification.removeAShareFeed(statusId);
                                    }
                                    NewsFeedMaps.getInstance().getParentFeedMap().remove(statusId);
                                }
                            } catch (Exception e) {
                               // e.printStackTrace();
                                log.error("Exception in FeedDeleteStatusRequest class ==>" + e.getMessage());
                            }

                        } else {
                            HelperMethods.showWarningDialogMessage("Failed!!" + responsefields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }
                HelperMethods.showWarningDialogMessage("Can not process this post delete request right now");
            } catch (Exception ex) {

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

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
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ShareFeedResponse;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.myprofile.MyBookHome;
import com.ipvision.view.feed.ShareThisFeedPopUp;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.service.utility.SortedArrayList;
import com.ipvision.view.utility.FeedUtils;

/**
 *
 * @author Faiz
 */
public class FeedShareStatusRequest extends Thread {

    private long newsfeedID;
    private JLabel loadingLabel;
    private JButton shareButton;
    private ShareThisFeedPopUp shareThisFeedJopUp;
    private String status;

    public FeedShareStatusRequest(String status, long sharedFeedID, JLabel loadingLabel, JButton sharButton, ShareThisFeedPopUp shareThisFeedJopUp) {
        this.newsfeedID = sharedFeedID;
        this.loadingLabel = loadingLabel;
        this.shareButton = sharButton;
        this.shareThisFeedJopUp = shareThisFeedJopUp;
        this.status = status;
         setName(this.getClass().getSimpleName());
    }

    private AllFeedsView getAllFeedView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getAllFeedsView();
        } else {
            return null;
        }
    }

    private MyBookHome getMyBookHome() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyFeeds();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                shareButton.setVisible(false);
                loadingLabel.setVisible(true);
                //boolean success = false;
                //[   actn = 177, pckId, sId, type = 2, sfId  = newsfeedID  ]
                String pakId = SendToServer.getRanDomPacketID();
                JsonFields feedBackFields = new JsonFields();
                feedBackFields.setAction(AppConstants.TYPE_ADD_STATUS);
                feedBackFields.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                feedBackFields.setSfId(newsfeedID);
                feedBackFields.setPacketId(pakId);
                feedBackFields.setType((short) 2);
                if (this.status != null && this.status.length() > 0) {
                    feedBackFields.setStatus(this.status);
                }

                Map<String, byte[]> packets = SendToServer.buildBrokenPacket(feedBackFields, feedBackFields.getAction(), pakId, ServerAndPortSettings.UPDATE_PORT);
                List<String> packetIds = new ArrayList<String>(packets.keySet());
                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);

                String makeResponsepak = FeedUtils.makeResponsePacket(pakId, AppConstants.TYPE_ADD_STATUS);
                for (int i = 1; i <= 20; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak) == null) {
                        if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
                            if (i % 5 == 0) {
                                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                            }
                        }
                    } else {
                        try {
                            FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak);
                            if (fields.getSuccess()) {
                                ShareFeedResponse shareFeedResponse = HelperMethods.getShareFeedResponse(fields.getMessage());
                                if (shareFeedResponse.getSharedFeed().getNfId() != null && shareFeedResponse.getSharedFeed().getNfId() > 0) {
                                    NewsFeedMaps.getInstance().getAllNewsFeeds().put(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed());
                                    NewsFeedMaps.getInstance().getAllNewfeedId().insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                    NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                    if (shareFeedResponse.getSharedFeed().getFriendIdentity() != null && shareFeedResponse.getSharedFeed().getFriendIdentity().length() > 0) {
                                        if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(shareFeedResponse.getSharedFeed().getFriendIdentity()) != null) {
                                            NewsFeedMaps.getInstance().getFriendNewsFeedId().get(shareFeedResponse.getSharedFeed().getFriendIdentity()).insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                        } else {
                                            SortedArrayList sortedArrayList = new SortedArrayList();
                                            sortedArrayList.insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                            NewsFeedMaps.getInstance().getFriendNewsFeedId().put(shareFeedResponse.getSharedFeed().getFriendIdentity(), sortedArrayList);
                                        }
                                    }
                                    if (shareFeedResponse.getSharedFeed().getCircleId() != null && shareFeedResponse.getSharedFeed().getCircleId() > 0) {
                                        if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(shareFeedResponse.getSharedFeed().getCircleId()) != null) {
                                            NewsFeedMaps.getInstance().getCircleNewsFeedId().get(shareFeedResponse.getSharedFeed().getCircleId()).insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                        } else {
                                            SortedArrayList sortedArrayList = new SortedArrayList();
                                            sortedArrayList.insertData(shareFeedResponse.getSharedFeed().getNfId(), shareFeedResponse.getSharedFeed().getTm());
                                            NewsFeedMaps.getInstance().getCircleNewsFeedId().put(shareFeedResponse.getSharedFeed().getCircleId(), sortedArrayList);
                                        }
                                    }

                                    if (shareFeedResponse.getSharedFeed().getWhoShare() != null && shareFeedResponse.getSharedFeed().getWhoShare().size() > 0) {
                                        for (SingleBookDetails sbd : shareFeedResponse.getSharedFeed().getWhoShare()) {
                                            NewsFeedMaps.getInstance().getParentFeedMap().put(sbd.getNfId(), shareFeedResponse.getSharedFeed().getNfId());
                                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(sbd.getNfId(), HelperMethods.dataConversionForShareFeed(sbd));
                                            //NewsFeedMaps.getInstance().getShareNewsFeedId().insertData(sbd.getNfId(), sbd.getTm());
                                            if (NewsFeedMaps.getInstance().getShareNewsFeedId().get(shareFeedResponse.getSharedFeed().getNfId()) != null) {
                                                NewsFeedMaps.getInstance().getShareNewsFeedId().get(shareFeedResponse.getSharedFeed().getNfId()).insertData(sbd.getNfId(), sbd.getTm());
                                            } else {
                                                SortedArrayList sortedArrayList = new SortedArrayList();
                                                sortedArrayList.insertData(sbd.getNfId(), sbd.getTm());
                                                NewsFeedMaps.getInstance().getShareNewsFeedId().put(shareFeedResponse.getSharedFeed().getNfId(), sortedArrayList);
                                            }
                                        }
                                    }

                                    if (getAllFeedView() != null
                                            && getAllFeedView().scrollContent.getVerticalScrollBar().getValue() < (getAllFeedView().postToFeeds.getHeight() + 32)) {
                                        getAllFeedView().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(shareFeedResponse.getSharedFeed().getNfId()));
                                    }

                                    if (shareFeedResponse.getSharedFeed().getUserIdentity() != null && shareFeedResponse.getSharedFeed().getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                        if (getMyBookHome() != null
                                                && getMyBookHome().scrollContent.getVerticalScrollBar().getValue() < (getMyBookHome().postToFeeds.getHeight() + 32)) {
                                            getMyBookHome().addSingleBookPost(NewsFeedMaps.getInstance().getAllNewsFeeds().get(shareFeedResponse.getSharedFeed().getNfId()));
                                        }
                                    }

                                    if (shareFeedResponse.getSharedFeed().getFriendIdentity() != null && !shareFeedResponse.getSharedFeed().getFriendIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                        MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(shareFeedResponse.getSharedFeed().getFriendIdentity());
                                        if (myFriendProfile != null
                                                && myFriendProfile.myFriendNewsFeedPanel != null
                                                && myFriendProfile.myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().getValue() < (myFriendProfile.myFriendNewsFeedPanel.postToFeeds.getHeight() + 32)) {
                                            myFriendProfile.myFriendNewsFeedPanel.addSingleBookPost(shareFeedResponse.getSharedFeed());
                                        }
                                    }

                                    if (shareFeedResponse.getSharedFeed().getCircleId() != null && shareFeedResponse.getSharedFeed().getCircleId() > 0) {
                                        CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(shareFeedResponse.getSharedFeed().getCircleId());
                                        if (circleProfile != null
                                                && circleProfile.getCircleNewsFeedPanel() != null
                                                && circleProfile.getCircleNewsFeedPanel().scrollContent.getVerticalScrollBar().getValue() < (circleProfile.getCircleNewsFeedPanel().postToFeeds.getHeight() + 32)) {
                                            circleProfile.getCircleNewsFeedPanel().addSingleBookPost(shareFeedResponse.getSharedFeed());
                                        }
                                    }
                                }
                                shareThisFeedJopUp.dispose_this();
                            } else {
                                HelperMethods.showWarningDialogMessage("Can not process share");
                            }
                        } catch (Exception e) {
                        } finally {
                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(makeResponsepak);
                            SendToServer.removeAllBrokenPacketConfirmation(packetIds);

                        }
                        break;

                        //success = true;
                    }

                }
//                if (success) {
//                    shareThisFeedJopUp.dispose_this();
//                } else {
//                    HelperMethods.showWarningDialogMessage("Can not process share");
////                    loadingLabel.setVisible(false);
////                    shareButton.setVisible(true);
//                }
            } catch (Exception e) {
                loadingLabel.setVisible(false);
                shareButton.setVisible(true);
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}

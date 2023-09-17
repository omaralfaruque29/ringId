/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.myprofile.MyBookHome;
import com.ipvision.model.FeedBackFields;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.service.utility.SortedArrayList;
import com.ipvision.view.utility.FeedUtils;

/**
 *
 * @author Faiz Ahmed
 */
public class FeedAddStatusRequest extends Thread {

    private JLabel loadingOrtimePanel;
    private String status;
    private JButton loginbutton;
    private short TYPE_IMAGE_STATUS = 1;
    private short TYPE_TEXT_STATUS = 2;
    private Long circleId;
    private String friendId;
    private int validity;

    public FeedAddStatusRequest(JLabel loadingOrtimePanel, String status, int validity, JButton loginbutton) {
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.status = status;
        this.loginbutton = loginbutton;
        this.validity = validity;
        setName(this.getClass().getName());
    }

    public FeedAddStatusRequest(JLabel loadingOrtimePanel, String status, String friend_id, int validity, JButton loginbutton) {
        this.friendId = friend_id;
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.status = status;
        this.loginbutton = loginbutton;
        this.validity = validity;
        setName(this.getClass().getName());
    }

    public FeedAddStatusRequest(JLabel loadingOrtimePanel, String status, Long circleId, int validity, JButton loginbutton) {
        this.circleId = circleId;
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.status = status;
        this.loginbutton = loginbutton;
        this.validity = validity;
        setName(this.getClass().getName());
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
//        return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyFeeds();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                //{"actn":177,"sts":"this from tester","sId":"1396506955321nazmul","pckId":"1396508280543"}
                //{ "iLike":1, "stId":5,"uId":"nazmul","iurl":"http://192.168.1.100:8080/auth/clients_image/nazmul/1398143097618/1398143002760.png","albId":"1398143097618","albn":"test","cptn":"","tm":1398143123614,"type":1,"nc":0,"nl":0}
                //"actn":177,"stId":628,"tm":1398340356344,"type":2,"pckId":"test_c1771398340359770","sucs":true}
                this.loginbutton.setEnabled(false);

                NewsFeedWithMultipleImage statusChange = new NewsFeedWithMultipleImage();
                statusChange.setAction(AppConstants.TYPE_ADD_STATUS);
                String pakId = SendToServer.getRanDomPacketID();
                statusChange.setPacketId(pakId);
                statusChange.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                statusChange.setStatus(this.status);
                if (friendId != null) {
                    statusChange.setFriendIdentity(friendId);
                }
                if (circleId != null) {
                    statusChange.setCircleId(circleId);
                }
                statusChange.setValidity(validity);
                statusChange.setType(TYPE_TEXT_STATUS);

                Map<String, byte[]> packets = SendToServer.buildBrokenPacket(statusChange, statusChange.getAction(), pakId, ServerAndPortSettings.UPDATE_PORT);
                List<String> packetIds = new ArrayList<String>(packets.keySet());
                SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));

//                for (int i = 1; i <= 5; i++) {
//                    Thread.sleep(500);
//                    if (SendToServer.checkAllBrokenPacketConfirmation(packets)) {
//                        SendToServer.sendAllBrokenPacket(packets, ServerAndPortSettings.UPDATE_PORT);
//                    } else {
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
                        this.loginbutton.setEnabled(true);
                        //{"actn":177,"nfId":1574,"uId":"2000002160","sts":"(an-happy)  (re-sad) 555","tm":1419942166027,"at":1419942166027,"type":2,"pckId":"20000021601131419942321407","sucs":true,"fn":"Faiz Ahmed2","ln":"","imT":1,"pst":1,"actvt":0}
                        FeedBackFields feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(makeResponsepak);
                        /* int count = 0;
                         while (feedBackFields == null) {
                         count++;
                         feedBackFields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                         System.err.println("-->" + count);
                         }*/

                        JsonFields fields = HelperMethods.getJsonFields(feedBackFields.getMessage());
                        if (fields != null && fields.getSuccess() && fields.getFriendIdentity() == null && fields.getCircleId() == null) {
                            this.loadingOrtimePanel.setIcon(null);
                            statusChange.setNfId(fields.getNfId());
                            statusChange.setTm(fields.getTm());
                            statusChange.setType(fields.getType());
                            statusChange.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                            statusChange.setStatus(this.status);

                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(fields.getNfId(), statusChange);
                            NewsFeedMaps.getInstance().getAllNewfeedId().insertData(statusChange.getNfId(), statusChange.getTm());
                            NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(statusChange.getNfId(), statusChange.getTm());

                            if (getAllFeedView() != null
                                    && getAllFeedView().scrollContent.getVerticalScrollBar().getValue() < (getAllFeedView().postToFeeds.getHeight() + 32)) {
                                getAllFeedView().addSingleBookPost(statusChange);
                            }

                            if (GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null && getMyBookHome() != null
                                    && getMyBookHome().scrollContent.getVerticalScrollBar().getValue() < (getMyBookHome().postToFeeds.getHeight() + 32)) {
                                getMyBookHome().addSingleBookPost(statusChange);
                            }
                        } else if (fields != null && fields.getSuccess() && fields.getFriendIdentity() != null && fields.getFriendIdentity().length() > 0) {
                            this.loadingOrtimePanel.setIcon(null);
                            statusChange.setNfId(fields.getNfId());
                            statusChange.setTm(fields.getTm());
                            statusChange.setType(fields.getType());
                            statusChange.setUserIdentity(fields.getUserIdentity());
                            statusChange.setFullName(fields.getFullName());
                            // statusChange.setLastName(fields.getLastName());
                            statusChange.setStatus(this.status);
                            statusChange.setFriendIdentity(fields.getFriendIdentity());
                            statusChange.setFfn(fields.getFfn());
                            //statusChange.setFln(fields.getFln());

                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(fields.getNfId(), statusChange);
                            NewsFeedMaps.getInstance().getAllNewfeedId().insertData(statusChange.getNfId(), statusChange.getTm());
                            NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(statusChange.getNfId(), statusChange.getTm());
                            if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(fields.getFriendIdentity()) != null) {
                                NewsFeedMaps.getInstance().getFriendNewsFeedId().get(fields.getFriendIdentity()).insertData(statusChange.getNfId(), statusChange.getTm());
                            } else {
                                SortedArrayList newfeed = new SortedArrayList();
                                newfeed.insertData(statusChange.getNfId(), statusChange.getTm());
                                NewsFeedMaps.getInstance().getFriendNewsFeedId().put(fields.getFriendIdentity(), newfeed);
                            }

                            if (getAllFeedView() != null
                                    && getAllFeedView().scrollContent.getVerticalScrollBar().getValue() < (getAllFeedView().postToFeeds.getHeight() + 32)) {
                                getAllFeedView().addSingleBookPost(statusChange);
                            }

                            if (GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null && getMyBookHome() != null
                                    && getMyBookHome().scrollContent.getVerticalScrollBar().getValue() < (getMyBookHome().postToFeeds.getHeight() + 32)) {
                                getMyBookHome().addSingleBookPost(statusChange);
                            }

                            if (MyfnfHashMaps.getInstance().getMyFriendProfiles() != null) {
                                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(fields.getFriendIdentity());
                                if (friendProfile != null
                                        && friendProfile.myFriendNewsFeedPanel != null
                                        && friendProfile.myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().getValue() < (friendProfile.myFriendNewsFeedPanel.postToFeeds.getHeight() + 32)) {
                                    friendProfile.myFriendNewsFeedPanel.addSingleBookPost(statusChange);
                                }
                            }
                        } else if (fields != null && fields.getSuccess() && fields.getCircleId() != null && fields.getCircleId() > 0) {
                            this.loadingOrtimePanel.setIcon(null);
                            statusChange.setNfId(fields.getNfId());
                            statusChange.setTm(fields.getTm());
                            statusChange.setType(fields.getType());
                            statusChange.setUserIdentity(fields.getUserIdentity());
                            statusChange.setFullName(fields.getFullName());
                            //statusChange.setLastName(fields.getLastName());
                            statusChange.setStatus(this.status);
                            statusChange.setCircleId(fields.getCircleId());
                            statusChange.setCircleName(fields.getCircleName());
                            statusChange.setFfn(fields.getFfn());
                            //statusChange.setFln(fields.getFln());

                            NewsFeedMaps.getInstance().getAllNewsFeeds().put(fields.getNfId(), statusChange);
                            NewsFeedMaps.getInstance().getAllNewfeedId().insertData(statusChange.getNfId(), statusChange.getTm());
                            NewsFeedMaps.getInstance().getMyNewsFeedId().insertData(statusChange.getNfId(), statusChange.getTm());
                            if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(fields.getCircleId()) != null) {
                                NewsFeedMaps.getInstance().getCircleNewsFeedId().get(fields.getCircleId()).insertData(statusChange.getNfId(), statusChange.getTm());
                            } else {
                                SortedArrayList newfeed = new SortedArrayList();
                                newfeed.insertData(statusChange.getNfId(), statusChange.getTm());
                                NewsFeedMaps.getInstance().getCircleNewsFeedId().put(fields.getCircleId(), newfeed);
                            }

                            if (getAllFeedView() != null
                                    && getAllFeedView().scrollContent.getVerticalScrollBar().getValue() < (getAllFeedView().postToFeeds.getHeight() + 32)) {
                                getAllFeedView().addSingleBookPost(statusChange);
                            }

                            if (GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null && getMyBookHome() != null
                                    && getMyBookHome().scrollContent.getVerticalScrollBar().getValue() < (getMyBookHome().postToFeeds.getHeight() + 32)) {
                                getMyBookHome().addSingleBookPost(statusChange);
                            }

                            if (MyfnfHashMaps.getInstance().getCircleProfiles() != null) {
                                CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(fields.getCircleId());
                                if (circleProfile != null
                                        && circleProfile.getCircleNewsFeedPanel() != null
                                        && circleProfile.getCircleNewsFeedPanel().scrollContent.getVerticalScrollBar().getValue() < (circleProfile.getCircleNewsFeedPanel().postToFeeds.getHeight() + 32)) {
                                    circleProfile.getCircleNewsFeedPanel().addSingleBookPost(statusChange);
                                }
                            }
                        } else {
                            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(makeResponsepak);
                        SendToServer.removeAllBrokenPacketConfirmation(packetIds);
                        return;
                    }

                }

                this.loginbutton.setEnabled(true);
                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));

            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("FeedAddStatusRequest exception==>" + ex.getMessage());
                this.loginbutton.setEnabled(true);
                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));

            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedAddStatusRequest.class);
}

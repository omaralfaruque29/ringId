/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Shahadat Hossain
 */
public class SendFriendsInfoRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendFriendsInfoRequest.class);
    private String friendId;
    private int action;
    private long time;
    private String albumId;
    private Short scl;
    private int st;
    private Long circleId;

    public SendFriendsInfoRequest(String friendId, int action, int start_limit, String albumId) {
        this.friendId = friendId;
        this.action = action;
        this.st = start_limit;
        this.albumId = albumId;
        setThreadName();
    }

    public SendFriendsInfoRequest(String friendId, int action, int st) {
        this.friendId = friendId;
        this.action = action;
        this.st = st;
        setThreadName();
    }

    public SendFriendsInfoRequest(String friendId, int action, long time, Short scl) {
        this.friendId = friendId;
        this.action = action;
        this.time = time;
        this.scl = scl;
        setThreadName();
    }

    public SendFriendsInfoRequest(Long circleId, int action, long time) {
        this.circleId = circleId;
        this.action = action;
        this.time = time;
        setThreadName();
    }

    public SendFriendsInfoRequest(Long circleId, int action, long time, Short scl) {
        this.circleId = circleId;
        this.action = action;
        this.time = time;
        this.scl = scl;
        setThreadName();
    }

    public SendFriendsInfoRequest(String friendId, int action, long time, String albumId, Short scl) {
        this.friendId = friendId;
        this.action = action;
        this.time = time;
        this.albumId = albumId;
        this.scl = scl;
        setThreadName();
    }

    public SendFriendsInfoRequest(Long circleId, int action, long time, String albumId, Short scl) {
        this.circleId = circleId;
        this.action = action;
        this.time = time;
        this.albumId = albumId;
        this.scl = scl;
        setThreadName();
    }

    private void setThreadName() {
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                JsonFields packet = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                packet.setPacketId(pakId);
                packet.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                packet.setAction(this.action);
                if (circleId != null && circleId > 0) {
                    packet.setCircleId(circleId);
                } else {
                    packet.setFriendIdentity(friendId);
                }
                if (scl != null && scl > 0) {
                    packet.setScl(scl);
                }
                /*if(this.action == AppConstants.TYPE_FRIEND_CONTACT_LIST) {
                 AuthMsgParser.friendContactListSeqCount = 0;
                 }*/

                if (this.action == AppConstants.TYPE_FRIEND_NEWSFEED || this.action == AppConstants.TYPE_CIRCLE_NEWSFEED) {
                    if (this.time > 0) {
                        packet.setTm(this.time);
                    }
                    packet.setLimit(10);
                }
                if (this.action == AppConstants.TYPE_FRIEND_ALBUM_IMAGES) {
                    packet.setStartLimit(st);
                }
                if (this.albumId != null && this.albumId.length() > 0) {
                    packet.setAlbId(albumId);
                }

                SendToServer.sendPacketAsString(packet, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500 * i);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(packet, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);

                        if (this.action == AppConstants.TYPE_FRIEND_NEWSFEED && friendId != null && friendId.length() > 0) {
                            MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId);
                            if (myFriendProfile != null && myFriendProfile.myFriendNewsFeedPanel != null) {
                                if (myFriendProfile.myFriendNewsFeedPanel.getContent().getComponentCount() <= 1
                                        || (myFriendProfile.myFriendNewsFeedPanel.getContent().getComponentCount() == 2
                                        && myFriendProfile.myFriendNewsFeedPanel.getContent().getComponent(1).equals(myFriendProfile.myFriendNewsFeedPanel.bottomLoadingLabel))) {
                                    int currentSize = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId) != null ? NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId).size() : 0;
                                    for (int j = 500; j < 10000 && currentSize < 10; j += 500) {
                                        Thread.sleep(500);
                                        currentSize = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId) != null ? NewsFeedMaps.getInstance().getFriendNewsFeedId().get(friendId).size() : 0;
                                    }

                                    myFriendProfile.myFriendNewsFeedPanel.initLoad();
                                } else if (scl != null && scl == 1) {
                                    Thread.sleep(1000);
                                    myFriendProfile.myFriendNewsFeedPanel.topLoad();
                                } else if (scl != null && scl == 2) {
                                    Thread.sleep(1000);
                                    myFriendProfile.myFriendNewsFeedPanel.bottomLoad();
                                }
                            }
                        } else if (this.action == AppConstants.TYPE_CIRCLE_NEWSFEED && circleId != null && circleId > 0) {
                            CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
                            if (circleProfile != null && circleProfile.getCircleNewsFeedPanel() != null) {
                                if (circleProfile.getCircleNewsFeedPanel().getContent().getComponentCount() <= 1
                                        || (circleProfile.getCircleNewsFeedPanel().getContent().getComponentCount() == 2
                                        && circleProfile.getCircleNewsFeedPanel().getContent().getComponent(1).equals(circleProfile.getCircleNewsFeedPanel().bottomLoadingLabel))) {
                                    int currentSize = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId) != null ? NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId).size() : 0;
                                    for (int j = 500; j < 10000 && currentSize < 10; j += 500) {
                                        Thread.sleep(500);
                                        currentSize = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId) != null ? NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId).size() : 0;
                                    }
                                    circleProfile.getCircleNewsFeedPanel().initLoad();
                                } else if (scl != null && scl == 1) {
                                    Thread.sleep(1000);
                                    circleProfile.getCircleNewsFeedPanel().topLoad();
                                } else if (scl != null && scl == 2) {
                                    Thread.sleep(1000);
                                    circleProfile.getCircleNewsFeedPanel().bottomLoad();
                                }
                            }
                        }

                        return;
                    }
                }

            } catch (Exception ex) {
            }
        }

        MyFriendProfile myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(friendId);
        if (myFriendProfile != null && myFriendProfile.myFriendNewsFeedPanel != null) {
            myFriendProfile.myFriendNewsFeedPanel.removeAllLoading();
        }
        if (!MyfnfHashMaps.getInstance().getCircleProfiles().isEmpty() && circleId != null) {
            CircleProfile circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
            if (circleProfile != null && circleProfile.getCircleNewsFeedPanel() != null) {
                circleProfile.getCircleNewsFeedPanel().removeAllLoading();
            }
        }
    }
}

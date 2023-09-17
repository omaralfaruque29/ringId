/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.view.GuiRingID;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.StickerStorer;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.utility.chat.ChatHashMap;

/**
 *
 * @author Faiz
 */
public class ClearAllFeedsByMyBook extends Thread {

    public ClearAllFeedsByMyBook() {
        setName(this.getClass().getName());
    }

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClearAllFeedsByMyBook.class);
    int mb = 1024 * 1024;
    Runtime runtime = Runtime.getRuntime();

    private AllFeedsView getAllFeedView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getAllFeedsView();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        try {
//
//            System.out.println("##### ********  ClearAllFeedsByMyBook  ******** #####");
//            System.out.println("Heap Used Memory:"
//                    + (runtime.totalMemory() - runtime.freeMemory()) / mb);
            if (!NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().isEmpty()) {
                for (String user : MyfnfHashMaps.getInstance().getMyFriendProfiles().keySet()) {
                    MyFriendProfile profile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user);
                    if (profile.myFriendNewsFeedPanel != null) {
                        profile.myFriendNewsFeedPanel.removeAllStatus();
                        profile.myFriendNewsFeedPanel = null;
                    }
                    if (profile.myFriendContactListPane != null) {
                        profile.myFriendContactListPane.removeAll();
                        profile.myFriendContactListPane = null;
                    }
                    if (profile.myFriendAlbumPanel != null) {
                        profile.myFriendAlbumPanel.removeAll();
                        profile.myFriendAlbumPanel = null;
                    }
                    if (profile.myFriendAboutPanel != null) {
                        profile.myFriendAboutPanel.removeAll();
                        profile.myFriendAboutPanel = null;
                    }

                    MyfnfHashMaps.getInstance().getMyFriendProfiles().remove(user);
                    profile.removeAll();
                    profile = null;

                    MyFriendChatCallPanel chatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(user);
                    if (chatCallPanel != null && !chatCallPanel.isDisplayable()) {
                        if (ChatHashMap.getInstance().getChatSingleChatPanel().get(user) != null) {
                            ChatHashMap.getInstance().getChatSingleChatPanel().get(user).clear();
                        }
                        MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().remove(user);
                        chatCallPanel.removeAll();
                        chatCallPanel = null;
                    }
                }
            }

            if (!NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().isEmpty()) {
                if (getAllFeedView() != null) {
                    getAllFeedView().removeLastNthStatus(getAllFeedView().getLoadedNewsFeeds().size() - getAllFeedView().LIMIT);
                }
            }

            if (!NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().isEmpty()) {
                for (long circleId : MyfnfHashMaps.getInstance().getCircleProfiles().keySet()) {
                    CircleProfile circle = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
                    if (circle.getCircleNewsFeedPanel() != null) {
                        circle.getCircleNewsFeedPanel().removeAllStatus();
                        circle.setCircleNewsFeedPanel(null);// = null;
                    }
                    if (circle.getCircleMembersListPanel() != null) {
                        circle.getCircleMembersListPanel().removeAll();
                        circle.setCircleMembersListPanel(null);// = null;
                    }
                }
            }
            StickerStorer.getInstance().getChatEmoticonsImageIconMap().clear();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Exception in ClearingAllFeedsByMyBook ==>" + e.getMessage());
        } finally {
            System.gc();
            Runtime.getRuntime().gc();
//            System.out.println("************************\n after clear heap space \n Used Memory:"
//                    + (runtime.totalMemory() - runtime.freeMemory()) / mb);
//            System.out.println("total memory:"
//                    + runtime.totalMemory() / mb);
//            System.out.println("maximum memory:"
//                    + runtime.totalMemory() / mb);
        }
    }

}

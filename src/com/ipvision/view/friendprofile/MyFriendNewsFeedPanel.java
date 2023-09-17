/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.view.feed.NewStatus;
import com.ipvision.view.feed.SingleStatusPanelInFriendNewsFeed;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.RingLayout;
import com.ipvision.service.utility.SortedArrayList;

/**
 * @author Shahadat Hossain
 */
public class MyFriendNewsFeedPanel extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyFriendNewsFeedPanel.class);
    private MyFriendProfile myFriendProfile;
    private JPanel content = new JPanel();
    public JScrollPane scrollContent;
    public NewStatus postToFeeds;

    int scrollValue = 0;
    int deletedHeight = 0;
    int mb = 1024 * 1024;
    public int LIMIT = 15;
    public int MEM_LIMIT = 30;
    public int currentV = 0;
    public boolean isTopLoading = false;
    public boolean isBottomLoading = false;
    public JLabel topLoadingLabel = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);
    public JLabel bottomLoadingLabel = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);
    private List<Long> loadedNewsFeeds = new ArrayList<Long>();

    public JPanel getContent() {
        return content;
    }

    public List<Long> getLoadedNewsFeeds() {
        return loadedNewsFeeds;
    }

    public MyFriendNewsFeedPanel(MyFriendProfile myFriendProfile) {
        this.myFriendProfile = myFriendProfile;

        if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity()) == null) {
            NewsFeedMaps.getInstance().getFriendNewsFeedId().put(myFriendProfile.getUserIdentity(), new SortedArrayList());
        }
        if (NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(myFriendProfile.getUserIdentity()) == null) {
            NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().put(myFriendProfile.getUserIdentity(), new HashSet<Long>());
        }

        setLayout(new BorderLayout());
        setOpaque(false);
        //   setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        initContainers();
    }

    private void initContainers() {
        try {
            postToFeeds = new NewStatus(this.myFriendProfile.getUserIdentity(), this.myFriendProfile.getFullName());
            content.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_VERTICAL_GAP, 0, 0, 0));
            content.setLayout(new RingLayout(5, 0, DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH, 1));
            content.setOpaque(false);
            content.add(postToFeeds);

            scrollContent = DesignClasses.getDefaultScrollPane(new JScrollPane(content) {
                @Override
                public void doLayout() {
                    super.doLayout();
                    content.setPreferredSize(content.getLayout().preferredLayoutSize(content));
                }
            });
            scrollContent.setOpaque(false);
            add(scrollContent, BorderLayout.CENTER);
            scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    int containerHeight = ((RingLayout) content.getLayout()).getMinColumnHeight();
                    int topHeight = e.getValue();
                    int barHeight = scrollContent.getVerticalScrollBar().getHeight();
                    int bottomHeight = containerHeight - (topHeight + barHeight);

                    if (scrollContent.getVerticalScrollBar().isVisible()) {
                        if (AddEmoticonsJDialog.currentInstance != null && AddEmoticonsJDialog.currentInstance.isVisible()) {
                            AddEmoticonsJDialog.currentInstance.setVisible(false);
                            AddEmoticonsJDialog.currentInstance.dispose();
                            AddEmoticonsJDialog.currentInstance = null;
                        }
                        if (JCustomMenuPopup.getInstance() != null && JCustomMenuPopup.getInstance().isDisplayable()) {
                            JCustomMenuPopup.getInstance().setVisible(false);
                            JCustomMenuPopup.getInstance().dispose();
                            JCustomMenuPopup.getInstance().setNull();
                        }
                        if (isTopLoading == false && topHeight < currentV && topHeight <= postToFeeds.getHeight() && e.getValueIsAdjusting()) {
                            // SCROLL TO TOP Forcefully
                            //System.err.println("SCROLL TO TOP Forcefully");
                            loadTopForcefully();
                        } else if (isTopLoading == false && topHeight < currentV && topHeight <= postToFeeds.getHeight() && !e.getValueIsAdjusting()) {
                            // SCROLL TO TOP
                            //System.err.println("SCROLL TO TOP");
                            loadNewsFeedsByScroll(1);
                        } else if (isBottomLoading == false && topHeight > 0 && topHeight > currentV && bottomHeight <= 64) {
                            // SCROLL TO BOTTOM
                            //System.err.println("SCROLL TO BOTTOM");
                            //System.err.println("containerHeight = " + containerHeight + ", bottomHeight = " + bottomHeight);
                            loadNewsFeedsByScroll(2);
                        }
                        currentV = topHeight;
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void addSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            removeIsAlreadyExist(status.getNfId());
            SingleStatusPanelInFriendNewsFeed single = new SingleStatusPanelInFriendNewsFeed(status);
            content.add(single, 1);
            content.revalidate();
            getLoadedNewsFeeds().add(0, status.getNfId());
            //System.err.println("### loadedNewsFeeds :: ADDED = " + status.getNfId());
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in addSingleBookPost ==>" + e.getMessage());
        }
    }

    public void editSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            int index = 0;
            SingleStatusPanelInFriendNewsFeed singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(status.getNfId());
            if (getLoadedNewsFeeds().contains(status.getNfId()) && singleStatusPanel != null) {
                index = content.getComponentZOrder(singleStatusPanel);
                content.remove(index);
                NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().remove(status.getNfId());
                Runtime.getRuntime().gc();

                SingleStatusPanelInFriendNewsFeed single = new SingleStatusPanelInFriendNewsFeed(status);
                content.add(single, index);
                content.revalidate();
                content.repaint();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in editSingleBookPost ==>" + e.getMessage());
        }
    }

    public void loadNewsFeedsByScroll(int type) {
        try {
            if (type == 1) {
                isTopLoading = true;
                SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
                SortedArrayList.Data firstData = sortedArrayList.getData(getLoadedNewsFeeds().get(0));
                int prevIndex = sortedArrayList.getIndex(firstData) - 1;

                if (prevIndex >= 0) {
                    topLoad(prevIndex);
                } else {
                    isTopLoading = false;
                    //content.add(topLoadingLabel, 0);
                    //(new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_NEWSFEED, firstData.getTm(), (short) type)).start();
                }
            } else if (type == 2) {
                isBottomLoading = true;
                SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
                SortedArrayList.Data lastData = sortedArrayList.getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));
                int nextIndex = sortedArrayList.getIndex(lastData) + 1;

                if (nextIndex < sortedArrayList.size()) {
                    bottomLoad(nextIndex);
                } else {
                    content.add(bottomLoadingLabel);
                    (new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_NEWSFEED, lastData.getTm(), (short) type)).start();
                }
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
            e.printStackTrace();
        }
    }

    public void initLoad() {
        try {
            int count = 1;
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
            if (sortedArrayList != null) {
                SortedArrayList newsFeeds = (SortedArrayList) sortedArrayList.clone();

                for (SortedArrayList.Data data : newsFeeds) {
                    if (count > LIMIT) {
                        break;
                    }
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(data.getNfId());
                    SingleStatusPanelInFriendNewsFeed single = new SingleStatusPanelInFriendNewsFeed(fields);
                    content.add(single);
                    getLoadedNewsFeeds().add(data.getNfId());
                    //System.err.println("### loadedNewsFeeds :: ADDED = " + data.getNfId());
                    count++;
                }

                content.remove(topLoadingLabel);
                content.remove(bottomLoadingLabel);
                content.revalidate();

                isTopLoading = false;
                isBottomLoading = false;
            }
        } catch (Exception e) {
            content.remove(topLoadingLabel);
            content.remove(bottomLoadingLabel);
            isTopLoading = false;
            isBottomLoading = false;
            e.printStackTrace();
        } finally {
            Runtime runtime = Runtime.getRuntime();
            long freeSpace = runtime.freeMemory() / mb;
            System.err.println(freeSpace + " = " + freeSpace + " < " + MEM_LIMIT);
            if (freeSpace < MEM_LIMIT) {
                HelperMethods.clearAllFriendBookUIMaps();
            }
        }
    }

    public void topLoad() {
        try {
            content.remove(topLoadingLabel);
            content.revalidate();
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
            SortedArrayList.Data firstData = sortedArrayList.getData(getLoadedNewsFeeds().get(0));

            int prevIndex = sortedArrayList.getIndex(firstData) - 1;
            if (prevIndex >= 0) {
                topLoad(prevIndex);
            } else {
                isTopLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            // e.printStackTrace();
            log.error("Error in topLoad method ==>" + e.getMessage());
        }
    }

    public void bottomLoad() {
        try {
            content.remove(bottomLoadingLabel);
            content.revalidate();
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
            SortedArrayList.Data lastData = sortedArrayList.getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));

            int nextIndex = sortedArrayList.getIndex(lastData) + 1;
            if (nextIndex < sortedArrayList.size()) {
                bottomLoad(nextIndex);
            } else {
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isBottomLoading = false;
            //e.printStackTrace();
            log.error("Error in bottomLoad method ==>" + e.getMessage());
        }
    }

    private void topLoad(final int prevIndex) {
        //System.err.println("### prevIndex = " + prevIndex);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
                    SortedArrayList.Data currData = sortedArrayList.get(prevIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    removeIsAlreadyExist(currData.getNfId());

                    SingleStatusPanelInFriendNewsFeed single = new SingleStatusPanelInFriendNewsFeed(fields);
                    content.add(single, 1);
                    content.revalidate();
                    getLoadedNewsFeeds().add(0, fields.getNfId());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                long freeSpace = runtime.freeMemory() / mb;
                                //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                if (freeSpace < MEM_LIMIT) {
                                    removeLastNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                    HelperMethods.clearAllFriendBookUIMaps();
                                }

                                if (scrollContent.getVerticalScrollBar().isVisible()) {
                                    setScrollValue(postToFeeds.getHeight() + 32);
                                }
                                isTopLoading = false;
                            } catch (Exception e) {
                                isTopLoading = false;
                                // e.printStackTrace();
                                log.error("Error in here ==>" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    isTopLoading = false;
                    // e.printStackTrace();
                    log.error("Error in here ==>" + e.getMessage());
                }
            }
        });
    }

    private void bottomLoad(final int nextIndex) {
        //System.err.println("### nextIndex = " + nextIndex);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    scrollValue = scrollContent.getVerticalScrollBar().getValue();
                    deletedHeight = 0;

                    SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getFriendNewsFeedId().get(myFriendProfile.getUserIdentity());
                    SortedArrayList.Data currData = sortedArrayList.get(nextIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    deletedHeight += removeIsAlreadyExist(currData.getNfId());

                    SingleStatusPanelInFriendNewsFeed single = new SingleStatusPanelInFriendNewsFeed(fields);
                    content.add(single);
                    content.revalidate();
                    getLoadedNewsFeeds().add(fields.getNfId());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                long freeSpace = runtime.freeMemory() / mb;
                                //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                if (freeSpace < MEM_LIMIT) {
                                    deletedHeight += removeFristNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                    HelperMethods.clearAllFriendBookUIMaps();
                                }

                                if (scrollContent.getVerticalScrollBar().isVisible()) {
                                    setScrollValue((scrollValue - deletedHeight) + 64);
                                }
                                isBottomLoading = false;
                            } catch (Exception e) {
                                isBottomLoading = false;
                                // e.printStackTrace();
                                log.error("Error in here ==>" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    isBottomLoading = false;
                    //  e.printStackTrace();
                    log.error("Error in here ==>" + e.getMessage());
                }
            }
        });
    }

    public void loadTopForcefully() {
        isTopLoading = true;
        isBottomLoading = true;
        content.add(bottomLoadingLabel, 1);
        content.revalidate();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    scrollContent.getVerticalScrollBar().setValue(0);
                    new InitLoadThread().start();
                } catch (Exception ex) {
                    content.remove(topLoadingLabel);
                    content.remove(bottomLoadingLabel);
                    isTopLoading = false;
                    isBottomLoading = false;
                    // ex.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());
                }
            }
        });
        content.revalidate();
    }

    class InitLoadThread extends Thread {

        @Override
        public void run() {
            try {
                removeAllStatus();
                initLoad();
            } catch (Exception ex) {
                content.remove(topLoadingLabel);
                content.remove(bottomLoadingLabel);
                isTopLoading = false;
                isBottomLoading = false;
                // ex.printStackTrace();
                log.error("Error in InitLoadThread ==>" + ex.getMessage());
            }
        }
    }

    public void removeAllStatus() {
        removeLastNthStatus(getLoadedNewsFeeds().size());
    }

    public int removeFristNthStatus(int number) {
        int height = 0;
        for (int i = 0; i < number; i++) {
            if (content.getComponent(1) instanceof SingleStatusPanelInFriendNewsFeed) {
                SingleStatusPanelInFriendNewsFeed singleStatusPanel = (SingleStatusPanelInFriendNewsFeed) content.getComponent(1);
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().remove(singleStatusPanel.statusDto.getNfId());
                content.remove(singleStatusPanel);
                content.revalidate();
                getLoadedNewsFeeds().remove(singleStatusPanel.statusDto.getNfId());
                Runtime.getRuntime().gc();
            }
        }
        return height;
    }

    public int removeLastNthStatus(int number) {
        int height = 0;
        for (int i = 0; i < number && content.getComponentCount() > 0; i++) {
            Component object = content.getComponent(content.getComponentCount() - 1);
            if (object instanceof SingleStatusPanelInFriendNewsFeed) {
                SingleStatusPanelInFriendNewsFeed singleStatusPanel = (SingleStatusPanelInFriendNewsFeed) object;
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().remove(singleStatusPanel.statusDto.getNfId());
                content.remove(singleStatusPanel);
                content.revalidate();
                getLoadedNewsFeeds().remove(singleStatusPanel.statusDto.getNfId());
                Runtime.getRuntime().gc();
            }
        }
        return height;
    }

    public int removeIsAlreadyExist(Long nfId) {
        int height = 0;
        if (getLoadedNewsFeeds().contains(nfId)) {
            SingleStatusPanelInFriendNewsFeed singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().get(nfId);
            height = singleStatusPanel.getHeight();
            NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().remove(nfId);
            content.remove(singleStatusPanel);
            content.revalidate();
            getLoadedNewsFeeds().remove(nfId);
            Runtime.getRuntime().gc();
            //System.err.println("### loadedNewsFeeds :: REMOVED = " + status.getNfId());
        } else {
            getLoadedNewsFeeds().remove(nfId);
            NewsFeedMaps.getInstance().getSingleStatusPanelInFriendNewsFeed().remove(nfId);
        }
        return height;
    }

    private void setScrollValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollContent.getVerticalScrollBar().setValue(value);
            }
        });
    }

    public static void addUnreadNewsFeeds(Long nfId, String friendId) {
//        if (NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId) == null) {
//            NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().put(friendId, new HashSet<Long>());
//        }
//        NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).add(nfId);
//        int currSize = NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).size();
//        if (currSize > 0) {
//            BookMain.getBookMain().homeCount.setText(String.valueOf(currSize));
//            BookMain.getBookMain().homeCount.setToolTipText(currSize + ((currSize > 1) ? " new posts" : " new post"));
//            BookMain.getBookMain().homeCount.revalidate();
//        } else {
//            BookMain.getBookMain().homeCount.setText("");
//            BookMain.getBookMain().homeCount.setToolTipText(null);
//            BookMain.getBookMain().homeCount.revalidate();
//        }
    }

    public static void removeUnreadNewsFeeds(Long nfId, String friendId) {
//        int prevSize = NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).size();
//        if (prevSize > 0 && NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).contains(nfId)) {
//            NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).remove(nfId);
//            int currSize = NewsFeedMaps.getInstance().getUnreadFriendNewsFeeds().get(friendId).size();
//            if (currSize > 0) {
//                BookMain.getBookMain().homeCount.setText(String.valueOf(currSize));
//                BookMain.getBookMain().homeCount.setToolTipText(currSize + ((currSize > 1) ? " new posts" : " new post"));
//                BookMain.getBookMain().homeCount.revalidate();
//            } else {
//                BookMain.getBookMain().homeCount.setText("");
//                BookMain.getBookMain().homeCount.setToolTipText(null);
//                BookMain.getBookMain().homeCount.revalidate();
//            }
//        }
    }

    public void removeAllLoading() {
        content.remove(topLoadingLabel);
        content.remove(bottomLoadingLabel);
        content.revalidate();
        isTopLoading = false;
        isBottomLoading = false;
    }

    public void addBottomLoading() {
        isTopLoading = true;
        isBottomLoading = true;
        content.add(bottomLoadingLabel);
        content.revalidate();
    }

}

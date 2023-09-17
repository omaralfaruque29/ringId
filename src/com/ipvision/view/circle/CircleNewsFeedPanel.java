/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

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
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.view.feed.NewStatus;
import com.ipvision.view.feed.SingleStatusPanelInCircleNewsFeed;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.RingLayout;
import com.ipvision.service.utility.SortedArrayList;

/**
 * @author Shahadat Hossain
 */
public class CircleNewsFeedPanel extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CircleNewsFeedPanel.class);
    private CircleProfile circleProfile;
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

    public CircleNewsFeedPanel(CircleProfile circleProfile) {
        this.circleProfile = circleProfile;

        if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId()) == null) {
            NewsFeedMaps.getInstance().getCircleNewsFeedId().put(circleProfile.getCircleId(), new SortedArrayList());
        }
        if (NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleProfile.getCircleId()) == null) {
            NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().put(circleProfile.getCircleId(), new HashSet<Long>());
        }

        setLayout(new BorderLayout());
        setOpaque(false);
        // setBackground(Color.CYAN);
        initContainers();
    }

    private void initContainers() {
        try {
            postToFeeds = new NewStatus(this.circleProfile.getCircleId(), this.circleProfile.getCircleName());
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
            SingleStatusPanelInCircleNewsFeed single = new SingleStatusPanelInCircleNewsFeed(status);
            content.add(single, 1);
            content.revalidate();
            getLoadedNewsFeeds().add(0, status.getNfId());
            removeUnreadNewsFeeds(status.getNfId(), circleProfile.getCircleId());
            //System.err.println("### loadedNewsFeeds :: ADDED = " + status.getNfId());
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Error in addSingleBookPost  ==>" + e.getMessage());
        }
    }

    public void editSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            int index = 0;
            SingleStatusPanelInCircleNewsFeed singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(status.getNfId());
            if (getLoadedNewsFeeds().contains(status.getNfId()) && singleStatusPanel != null) {
                index = content.getComponentZOrder(singleStatusPanel);
                content.remove(index);
                NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().remove(status.getNfId());
                Runtime.getRuntime().gc();

                SingleStatusPanelInCircleNewsFeed single = new SingleStatusPanelInCircleNewsFeed(status);
                content.add(single, index);
                content.revalidate();
                content.repaint();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Error in editSingleBookPost  ==>" + e.getMessage());
        }
    }

    public void loadNewsFeedsByScroll(int type) {
        try {
            if (type == 1) {
                isTopLoading = true;
                SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
                SortedArrayList.Data firstData = sortedArrayList.getData(getLoadedNewsFeeds().get(0));
                int prevIndex = sortedArrayList.getIndex(firstData) - 1;

                if (prevIndex >= 0) {
                    topLoad(prevIndex);
                } else {
                    isTopLoading = false;
                    //content.add(topLoadingLabel, 0);
                    //(new SendFriendsInfoRequest(circleProfile.getCircleId(), AppConstants.TYPE_CIRCLE_NEWSFEED, firstData.getTm(), (short) type)).start();
                }
            } else if (type == 2) {
                isBottomLoading = true;
                SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
                SortedArrayList.Data lastData = sortedArrayList.getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));
                int nextIndex = sortedArrayList.getIndex(lastData) + 1;

                if (nextIndex < sortedArrayList.size()) {
                    bottomLoad(nextIndex);
                } else {
                    content.add(bottomLoadingLabel);
                    (new SendFriendsInfoRequest(circleProfile.getCircleId(), AppConstants.TYPE_CIRCLE_NEWSFEED, lastData.getTm(), (short) type)).start();
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
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
            if (sortedArrayList != null) {
                SortedArrayList newsFeeds = (SortedArrayList) sortedArrayList.clone();

                for (SortedArrayList.Data data : newsFeeds) {
                    if (count > LIMIT) {
                        break;
                    }
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(data.getNfId());
                    SingleStatusPanelInCircleNewsFeed single = new SingleStatusPanelInCircleNewsFeed(fields);
                    content.add(single);
                    getLoadedNewsFeeds().add(data.getNfId());
                    removeUnreadNewsFeeds(data.getNfId(), circleProfile.getCircleId());
                    //System.err.println("### loadedNewsFeeds :: ADDED = " + data.getNfId());
                    count++;
                }
            }
            content.remove(topLoadingLabel);
            content.remove(bottomLoadingLabel);
            content.revalidate();

            isTopLoading = false;
            isBottomLoading = false;
        } catch (Exception e) {
            content.remove(topLoadingLabel);
            content.remove(bottomLoadingLabel);
            isTopLoading = false;
            isBottomLoading = false;
        } finally {
            Runtime runtime = Runtime.getRuntime();
            long freeSpace = runtime.freeMemory() / mb;
            //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
            if (freeSpace < MEM_LIMIT) {
                HelperMethods.clearAllCircleBookUIMaps();
            }
        }
    }

    public void topLoad() {
        try {
            content.remove(topLoadingLabel);
            content.revalidate();
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
            SortedArrayList.Data firstData = sortedArrayList.getData(getLoadedNewsFeeds().get(0));

            int prevIndex = sortedArrayList.getIndex(firstData) - 1;
            if (prevIndex >= 0) {
                topLoad(prevIndex);
            } else {
                isTopLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            e.printStackTrace();
        }
    }

    public void bottomLoad() {
        try {
            content.remove(bottomLoadingLabel);
            content.revalidate();
            SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
            SortedArrayList.Data lastData = sortedArrayList.getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));

            int nextIndex = sortedArrayList.getIndex(lastData) + 1;
            if (nextIndex < sortedArrayList.size()) {
                bottomLoad(nextIndex);
            } else {
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isBottomLoading = false;
            e.printStackTrace();
        }
    }

    private void topLoad(final int prevIndex) {
        //System.err.println("### prevIndex = " + prevIndex);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
                    if (sortedArrayList != null) {
                        SortedArrayList.Data currData = sortedArrayList.get(prevIndex);
                        NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                        removeIsAlreadyExist(currData.getNfId());

                        SingleStatusPanelInCircleNewsFeed single = new SingleStatusPanelInCircleNewsFeed(fields);
                        content.add(single, 1);
                        content.revalidate();
                        getLoadedNewsFeeds().add(0, fields.getNfId());
                        removeUnreadNewsFeeds(fields.getNfId(), circleProfile.getCircleId());

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Runtime runtime = Runtime.getRuntime();
                                    long freeSpace = runtime.freeMemory() / mb;
                                    //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                    if (freeSpace < MEM_LIMIT) {
                                        removeLastNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                        HelperMethods.clearAllCircleBookUIMaps();
                                    }

                                    if (scrollContent.getVerticalScrollBar().isVisible()) {
                                        setScrollValue(postToFeeds.getHeight() + 32);
                                    }
                                    isTopLoading = false;
                                } catch (Exception e) {
                                    isTopLoading = false;
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    isTopLoading = false;
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

                    SortedArrayList sortedArrayList = NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleProfile.getCircleId());
                    SortedArrayList.Data currData = sortedArrayList.get(nextIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    deletedHeight += removeIsAlreadyExist(currData.getNfId());

                    SingleStatusPanelInCircleNewsFeed single = new SingleStatusPanelInCircleNewsFeed(fields);
                    content.add(single);
                    content.revalidate();
                    getLoadedNewsFeeds().add(fields.getNfId());
                    removeUnreadNewsFeeds(fields.getNfId(), circleProfile.getCircleId());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                long freeSpace = runtime.freeMemory() / mb;
                                //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                if (freeSpace < MEM_LIMIT) {
                                    deletedHeight += removeFristNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                    HelperMethods.clearAllCircleBookUIMaps();
                                }

                                if (scrollContent.getVerticalScrollBar().isVisible()) {
                                    setScrollValue((scrollValue - deletedHeight) + 64);
                                }
                                isBottomLoading = false;
                            } catch (Exception e) {
                                isBottomLoading = false;
                                log.error("Circle feed error 376");
                            }
                        }
                    });
                } catch (Exception e) {
                    isBottomLoading = false;
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
            }
        }
    }

    public void removeAllStatus() {
        removeLastNthStatus(getLoadedNewsFeeds().size());
    }

    public int removeFristNthStatus(int number) {
        int height = 0;
        for (int i = 0; i < number; i++) {
            if (content.getComponent(1) instanceof SingleStatusPanelInCircleNewsFeed) {
                SingleStatusPanelInCircleNewsFeed singleStatusPanel = (SingleStatusPanelInCircleNewsFeed) content.getComponent(1);
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().remove(singleStatusPanel.statusDto.getNfId());
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
        for (int i = 0; i < number; i++) {
            Component object = content.getComponent(content.getComponentCount() - 1);
            if (object instanceof SingleStatusPanelInCircleNewsFeed) {
                SingleStatusPanelInCircleNewsFeed singleStatusPanel = (SingleStatusPanelInCircleNewsFeed) object;
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().remove(singleStatusPanel.statusDto.getNfId());
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
            SingleStatusPanelInCircleNewsFeed singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().get(nfId);
            height = singleStatusPanel.getHeight();
            NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().remove(nfId);
            content.remove(singleStatusPanel);
            content.revalidate();
            getLoadedNewsFeeds().remove(nfId);
            Runtime.getRuntime().gc();
            //System.err.println("### loadedNewsFeeds :: REMOVED = " + status.getNfId());
        } else {
            NewsFeedMaps.getInstance().getSingleStatusPanelInCircleNewsFeed().remove(nfId);
            getLoadedNewsFeeds().remove(nfId);
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

    public void addUnreadNewsFeeds(Long nfId, Long circleId) {
        if (NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId) == null) {
            NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().put(circleId, new HashSet<Long>());
        }
        NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId).add(nfId);
        getCircleViewPanel().setNotificationCount();

    }

    public void removeUnreadNewsFeeds(Long nfId, Long circleId) {
        int prevSize = NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId).size();
        if (prevSize > 0 && NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId).contains(nfId)) {
            NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId).remove(nfId);
            getCircleViewPanel().setNotificationCount();
        }
    }

    private CircleViewRight getCircleViewPanel() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getCircleViewRight();//.getc
        } else {
            return null;
        }
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

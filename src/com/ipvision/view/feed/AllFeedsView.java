/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.NewsFeeds;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.RingLayout;
import com.ipvision.service.utility.SortedArrayList;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Faiz Ahmed
 */
public class AllFeedsView extends JPanel implements AncestorListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AllFeedsView.class);
    private JPanel content = new JPanel();
    public JScrollPane scrollContent;
    public RingLayout layoutManager;
    public NewStatus postToFeeds;

    int scrollValue = 0;
    int deletedHeight = 0;
    int mb = 1024 * 1024;
    public int LIMIT = 20;
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

    public AllFeedsView() {
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        addAncestorListener(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initContainers();
                addBottomLoading();
            }
        });
    }

    private void initContainers() {
        try {
            postToFeeds = new NewStatus();
            layoutManager = new RingLayout(5, 0, DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH, 1);
            content.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_VERTICAL_GAP, 0, 0, 0));
            content.setLayout(layoutManager);
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
            DesignClasses.setArrowKeyScroll(scrollContent);
            add(scrollContent, BorderLayout.CENTER);
            scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    int containerHeight = layoutManager.getMinColumnHeight();
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
                            loadNewsFeedsByScroll(2);
                        }
                        currentV = topHeight;
                    }
                }

            });
        } catch (Exception e) {
            log.error("Book exception" + e.getMessage());
        }

    }

    public void addSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            removeIsAlreadyExist(status.getNfId());
            SingleStatusPanelInBookHome single = new SingleStatusPanelInBookHome(status);
            content.add(single, 1);
            content.revalidate();
            getLoadedNewsFeeds().add(0, status.getNfId());
            removeUnreadNewsFeeds(status.getNfId());
            // System.err.println("### loadedNewsFeeds :: ADDED = " + status.getNfId());
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Exception in addSingleBookPost ==>" + e.getMessage());
        }
    }

    public void editSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            int index = 0;
            SingleStatusPanelInBookHome singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(status.getNfId());
            if (getLoadedNewsFeeds().contains(status.getNfId()) && singleStatusPanel != null) {
                index = content.getComponentZOrder(singleStatusPanel);
                content.remove(index);
                NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(status.getNfId());
                Runtime.getRuntime().gc();

                SingleStatusPanelInBookHome single = new SingleStatusPanelInBookHome(status);
                content.add(single, index);
                content.revalidate();
                content.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNewsFeedsByScroll(int type) {
        try {
            //System.err.println("### loadNewsFeedsByScroll :: TYPE = " + type);
            if (type == 1) {
                isTopLoading = true;
                SortedArrayList.Data firstData = NewsFeedMaps.getInstance().getAllNewfeedId().getData(getLoadedNewsFeeds().get(0));
                int prevIndex = NewsFeedMaps.getInstance().getAllNewfeedId().getIndex(firstData) - 1;

                if (prevIndex >= 0) {
                    topLoad(prevIndex);
                } else {
                    isTopLoading = false;
                    //content.add(topLoadingLabel, 0);
                    //new NewsFeeds(firstData.getTm(), (short) type, 10).start();
                }

            } else if (type == 2) {
                isBottomLoading = true;
                SortedArrayList.Data lastData = NewsFeedMaps.getInstance().getAllNewfeedId().getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));
                int nextIndex = NewsFeedMaps.getInstance().getAllNewfeedId().getIndex(lastData) + 1;

                if (nextIndex < NewsFeedMaps.getInstance().getAllNewfeedId().size()) {
                    bottomLoad(nextIndex);
                } else {
                    content.add(bottomLoadingLabel);
                    if (lastData != null) {
                        new NewsFeeds(lastData.getTm(), (short) type, 10).start();
                    } else {
                        log.error("Error in bookhome");
                    }
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
            SortedArrayList newsFeeds = (SortedArrayList) NewsFeedMaps.getInstance().getAllNewfeedId().clone();

            for (SortedArrayList.Data data : newsFeeds) {
                if (count > LIMIT) {
                    break;
                }
                NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(data.getNfId());
                SingleStatusPanelInBookHome singleStatusPanelInHome = new SingleStatusPanelInBookHome(fields);
                content.add(singleStatusPanelInHome);

                getLoadedNewsFeeds().add(data.getNfId());
                removeUnreadNewsFeeds(data.getNfId());
                //System.err.println("### loadedNewsFeeds :: ADDED = " + data.getNfId());
                count++;
            }

            content.remove(topLoadingLabel);
            content.remove(bottomLoadingLabel);
            content.revalidate();
            content.repaint();
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
            //   System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
            if (freeSpace < MEM_LIMIT) {
                HelperMethods.clearAllBookHomeUIMaps();
            }
        }
    }

    public void topLoad() {
        try {
            content.remove(topLoadingLabel);
            content.revalidate();
            SortedArrayList.Data firstData = NewsFeedMaps.getInstance().getAllNewfeedId().getData(getLoadedNewsFeeds().get(0));

            int prevIndex = NewsFeedMaps.getInstance().getAllNewfeedId().getIndex(firstData) - 1;
            if (prevIndex >= 0) {
                topLoad(prevIndex);
            } else {
                isTopLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            // e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }
    }

    public void bottomLoad() {
        try {
            content.remove(bottomLoadingLabel);
            content.revalidate();
            SortedArrayList.Data lastData = NewsFeedMaps.getInstance().getAllNewfeedId().getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));

            int nextIndex = NewsFeedMaps.getInstance().getAllNewfeedId().getIndex(lastData) + 1;
            if (nextIndex < NewsFeedMaps.getInstance().getAllNewfeedId().size()) {
                bottomLoad(nextIndex);
            } else {
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isBottomLoading = false;
            //e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }
    }

    private void topLoad(final int prevIndex) {
        //System.err.println("### prevIndex = " + prevIndex);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SortedArrayList.Data currData = NewsFeedMaps.getInstance().getAllNewfeedId().get(prevIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    removeIsAlreadyExist(currData.getNfId());

                    SingleStatusPanelInBookHome single = new SingleStatusPanelInBookHome(fields);
                    content.add(single, 1);
                    content.revalidate();
                    getLoadedNewsFeeds().add(0, fields.getNfId());
                    removeUnreadNewsFeeds(fields.getNfId());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                long freeSpace = runtime.freeMemory() / mb;
                                //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                if (freeSpace < MEM_LIMIT) {
                                    removeLastNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                    HelperMethods.clearAllBookHomeUIMaps();
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
                    //e.printStackTrace();
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
                    int total = 0;
                    int index = nextIndex;
                    int diff = layoutManager.getMaxColumnHeight() - layoutManager.getMinColumnHeight();
                    scrollValue = scrollContent.getVerticalScrollBar().getValue();
                    deletedHeight = 0;

                    do {
                        SortedArrayList.Data currData = NewsFeedMaps.getInstance().getAllNewfeedId().get(index);
                        NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                        deletedHeight += removeIsAlreadyExist(currData.getNfId());

                        SingleStatusPanelInBookHome single = new SingleStatusPanelInBookHome(fields);
                        content.add(single);
                        content.revalidate();
                        content.repaint();

                        getLoadedNewsFeeds().add(fields.getNfId());
                        removeUnreadNewsFeeds(fields.getNfId());

                        index++;
                        total += 100;
                        if (fields.getImageList() != null && fields.getImageList().size() > 0) {
                            total += 150;
                        }
                        if (fields.getStatus() != null && fields.getStatus().length() > 600) {
                            total += 100;
                        }
                    } while (total < diff && index < NewsFeedMaps.getInstance().getAllNewfeedId().size());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                long freeSpace = runtime.freeMemory() / mb;
                                //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
                                if (freeSpace < MEM_LIMIT) {
                                    deletedHeight += removeFristNthStatus(getLoadedNewsFeeds().size() - LIMIT);
                                    HelperMethods.clearAllBookHomeUIMaps();
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
                    //e.printStackTrace();
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
                    //ex.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());
                }
            }
        });
        content.revalidate();
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtons() != null) {
            GuiRingID.getInstance().getMainButtons().setMainHomeButton(true);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtons() != null) {
            GuiRingID.getInstance().getMainButtons().setMainHomeButton(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
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
                log.error("Error in here ==>" + ex.getMessage());
                //ex.printStackTrace();
            }
        }
    }

    public void removeAllStatus() {
        removeLastNthStatus(getLoadedNewsFeeds().size());
    }

    public int removeFristNthStatus(int number) {
        int height = 0;
        for (int i = 1; i < number; i++) {
            if (content.getComponent(1) instanceof SingleStatusPanelInBookHome) {
                SingleStatusPanelInBookHome singleStatusPanel = (SingleStatusPanelInBookHome) content.getComponent(1);
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(singleStatusPanel.statusDto.getNfId());
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
            if (object instanceof SingleStatusPanelInBookHome) {
                SingleStatusPanelInBookHome singleStatusPanel = (SingleStatusPanelInBookHome) object;
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(singleStatusPanel.statusDto.getNfId());
                content.remove(singleStatusPanel);
                content.revalidate();
                getLoadedNewsFeeds().remove(singleStatusPanel.statusDto.getNfId());
                Runtime.getRuntime().gc();
            }
        }
        return height;
    }

    public int revalidateIsAlreadyExist(Long nfId) {
        int height = 0;
        if (getLoadedNewsFeeds().contains(nfId)) {
            SingleStatusPanelInBookHome singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(nfId);
            height = singleStatusPanel.getHeight();
            NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(nfId);
            content.getComponentZOrder(singleStatusPanel);
            content.remove(singleStatusPanel);
            content.revalidate();
            getLoadedNewsFeeds().remove(nfId);
            Runtime.getRuntime().gc();
            //System.err.println("### loadedNewsFeeds :: REMOVED = " + status.getNfId());
        } else {
            NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(nfId);
            getLoadedNewsFeeds().remove(nfId);
        }
        return height;
    }

    public int removeIsAlreadyExist(Long nfId) {
        int height = 0;
        if (getLoadedNewsFeeds().contains(nfId)) {
            SingleStatusPanelInBookHome singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(nfId);
            height = singleStatusPanel.getHeight();
            NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(nfId);
            content.remove(singleStatusPanel);
            content.revalidate();
            getLoadedNewsFeeds().remove(nfId);
            Runtime.getRuntime().gc();
            //System.err.println("### loadedNewsFeeds :: REMOVED = " + status.getNfId());
        } else {
            NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().remove(nfId);
            getLoadedNewsFeeds().remove(nfId);
        }
        return height;
    }

    public void setScrollValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollContent.getVerticalScrollBar().setValue(value);
            }
        });
    }

    public static void addUnreadNewsFeeds(Long nfId) {
        NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().add(nfId);
        int currSize = NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size();
        if (currSize > 0) {
            GuiRingID.getInstance().getHomeCount().setText(String.valueOf(currSize));
            GuiRingID.getInstance().getHomeCount().setToolTipText(currSize + ((currSize > 1) ? " new posts" : " new post"));
            GuiRingID.getInstance().getHomeCount().revalidate();

        } else {
            GuiRingID.getInstance().getHomeCount().setText("");
            GuiRingID.getInstance().getHomeCount().setToolTipText(null);
            GuiRingID.getInstance().getHomeCount().revalidate();
        }
    }

    public static void removeUnreadNewsFeeds(Long nfId) {
        int prevSize = NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size();
        if (prevSize > 0 && NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().contains(nfId)) {
            NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().remove(nfId);
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

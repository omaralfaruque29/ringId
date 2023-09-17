/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

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
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.MyNewsFeeds;
import com.ipvision.view.feed.NewStatus;
import com.ipvision.view.feed.SingleStatusPanelInMyBook;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.RingLayout;
import com.ipvision.service.utility.SortedArrayList;

/**
 *
 * @author Faiz Ahmed
 */
public class MyBookHome extends JPanel {
private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyBookHome.class);
    private final JPanel content = new JPanel();
    public NewStatus postToFeeds;
    public JScrollPane scrollContent;

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

    public MyBookHome() {
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        initContainers();
    }

    private void initContainers() {
        try {
            postToFeeds = new NewStatus();
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
            //   e.printStackTrace();
        
        }
    }

    public void addSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            removeIsAlreadyExist(status.getNfId());
            SingleStatusPanelInMyBook single = new SingleStatusPanelInMyBook(status);
            content.add(single, 1);
            content.revalidate();
            getLoadedNewsFeeds().add(0, status.getNfId());
        } catch (Exception e) {
          //  e.printStackTrace();
        log.error("Error in addSingleBookPost ==>" + e.getMessage());
        }
    }

    public void editSingleBookPost(NewsFeedWithMultipleImage status) {
        try {
            int index = 0;
            SingleStatusPanelInMyBook singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(status.getNfId());
            if (getLoadedNewsFeeds().contains(status.getNfId()) && singleStatusPanel != null) {
                index = content.getComponentZOrder(singleStatusPanel);
                content.remove(index);
                NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().remove(status.getNfId());
                Runtime.getRuntime().gc();

                SingleStatusPanelInMyBook single = new SingleStatusPanelInMyBook(status);
                content.add(single, index);
                content.revalidate();
                content.repaint();
            }
        } catch (Exception e) {
          //  e.printStackTrace();
         log.error("Error in editSingleBookPost ==>" + e.getMessage());
        }
    }

    public void loadNewsFeedsByScroll(int type) {
        try {
            if (type == 1) {
                isTopLoading = true;
                SortedArrayList.Data firstData = NewsFeedMaps.getInstance().getMyNewsFeedId().getData(getLoadedNewsFeeds().get(0));
                int prevIndex = NewsFeedMaps.getInstance().getMyNewsFeedId().getIndex(firstData) - 1;

                if (prevIndex >= 0) {
                    topLoad(prevIndex);
                } else {
                    isTopLoading = false;
                    //content.add(topLoadingLabel, 0);
                    //new MyNewsFeeds(firstData.getTm(), (short) type).start();
                }

            } else if (type == 2) {
                isBottomLoading = true;
                SortedArrayList.Data lastData = NewsFeedMaps.getInstance().getMyNewsFeedId().getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));
                int nextIndex = NewsFeedMaps.getInstance().getMyNewsFeedId().getIndex(lastData) + 1;

                if (nextIndex < NewsFeedMaps.getInstance().getMyNewsFeedId().size()) {
                    bottomLoad(nextIndex);
                } else {
                    content.add(bottomLoadingLabel);
                    new MyNewsFeeds(lastData.getTm(), (short) type).start();
                }
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
           // e.printStackTrace();
        log.error("Error in loadNewsFeedsByScroll ==>" + e.getMessage());
        }
    }

    public void initLoad() {
        try {
            int count = 1;
            SortedArrayList newsFeeds = (SortedArrayList) NewsFeedMaps.getInstance().getMyNewsFeedId().clone();
            for (SortedArrayList.Data data : newsFeeds) {
                if (count > LIMIT) {
                    break;
                }
                NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(data.getNfId());
                SingleStatusPanelInMyBook single = new SingleStatusPanelInMyBook(fields);
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
        } catch (Exception e) {
            content.remove(topLoadingLabel);
            content.remove(bottomLoadingLabel);
            isTopLoading = false;
            isBottomLoading = false;
            //e.printStackTrace();
         log.error("Error in initLoad ==>" + e.getMessage());
        } finally {
            Runtime runtime = Runtime.getRuntime();
            long freeSpace = runtime.freeMemory() / mb;
            //System.err.println((runtime.totalMemory() / mb) + " = " + freeSpace + " < " + MEM_LIMIT);
            if (freeSpace < MEM_LIMIT) {
                HelperMethods.clearAllMyBookUIMaps();
            }
        }
    }

    public void topLoad() {
        try {
            content.remove(topLoadingLabel);
            content.revalidate();
            SortedArrayList.Data firstData = NewsFeedMaps.getInstance().getMyNewsFeedId().getData(getLoadedNewsFeeds().get(0));
            int prevIndex = NewsFeedMaps.getInstance().getMyNewsFeedId().getIndex(firstData) - 1;
            if (prevIndex >= 0) {
                topLoad(prevIndex);
            } else {
                isTopLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            // e.printStackTrace();
        }
    }

    public void bottomLoad() {
        try {
            content.remove(bottomLoadingLabel);
            content.revalidate();
            SortedArrayList.Data lastData = NewsFeedMaps.getInstance().getMyNewsFeedId().getData(getLoadedNewsFeeds().get(getLoadedNewsFeeds().size() - 1));

            int nextIndex = NewsFeedMaps.getInstance().getMyNewsFeedId().getIndex(lastData) + 1;
            if (nextIndex < NewsFeedMaps.getInstance().getMyNewsFeedId().size()) {
                bottomLoad(nextIndex);
            } else {
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isBottomLoading = false;
            //   e.printStackTrace();
        }
    }

    private void topLoad(final int prevIndex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SortedArrayList.Data currData = NewsFeedMaps.getInstance().getMyNewsFeedId().get(prevIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    removeIsAlreadyExist(currData.getNfId());

                    SingleStatusPanelInMyBook single = new SingleStatusPanelInMyBook(fields);
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
                                    HelperMethods.clearAllMyBookUIMaps();
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    scrollValue = scrollContent.getVerticalScrollBar().getValue();
                    deletedHeight = 0;
                    SortedArrayList.Data currData = NewsFeedMaps.getInstance().getMyNewsFeedId().get(nextIndex);
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(currData.getNfId());
                    deletedHeight += removeIsAlreadyExist(currData.getNfId());
                    SingleStatusPanelInMyBook single = new SingleStatusPanelInMyBook(fields);
                    content.add(single);
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
                                    HelperMethods.clearAllMyBookUIMaps();
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
                } finally {
                    content.revalidate();
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
                   log.error("Error in loadTopForcefully ==>" + ex.getMessage());
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
                //ex.printStackTrace();
           log.error("Error in here ==>" + ex.getMessage());
            }
        }
    }

    public void removeAllStatus() {
        removeLastNthStatus(getLoadedNewsFeeds().size());
    }

    public int removeFristNthStatus(int number) {
        int height = 0;
        for (int i = 0; i < number; i++) {
            if (content.getComponent(1) instanceof SingleStatusPanelInMyBook) {
                SingleStatusPanelInMyBook singleStatusPanel = (SingleStatusPanelInMyBook) content.getComponent(1);
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().remove(singleStatusPanel.statusDto.getNfId());
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
            if (object instanceof SingleStatusPanelInMyBook) {
                SingleStatusPanelInMyBook singleStatusPanel = (SingleStatusPanelInMyBook) object;
                height += singleStatusPanel.getHeight();
                NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().remove(singleStatusPanel.statusDto.getNfId());
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
            SingleStatusPanelInMyBook singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().get(nfId);
            height = singleStatusPanel.getHeight();
            NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().remove(nfId);
            content.remove(singleStatusPanel);
            content.revalidate();
            getLoadedNewsFeeds().remove(nfId);
            Runtime.getRuntime().gc();
            //System.err.println("### loadedNewsFeeds :: REMOVED = " + status.getNfId());
        } else {
            NewsFeedMaps.getInstance().getSingleStatusPanelInMyBook().remove(nfId);
            getLoadedNewsFeeds().remove(nfId);
        }
        return height;
    }

    public void deleteAllFeeds() {
    }

    private void setScrollValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollContent.getVerticalScrollBar().setValue(value);
            }
        });
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

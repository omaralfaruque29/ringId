/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserBasicInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import com.ipvision.view.addfriend.AddFriendMainPanel;
import com.ipvision.service.DefaultRequest;
import com.ipvision.service.NotificationRequest;
import com.ipvision.service.singinsignup.SignOutActions;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.NewsFeeds;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.view.image.SingleImageDetailsProfileCover;
import com.ipvision.view.image.SingleImgeDetails;
import com.ipvision.view.utility.NotificationCounterOvalLabel;

/**
 *
 * @author Faiz Ahmed
 */
public final class GuiRingID extends UnitFrame {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GuiRingID.class);
    private static GuiRingID instance;

    public static GuiRingID getInstance() {
        return instance;
    }

    public static void setInstance(GuiRingID guiRingID) {
        instance = guiRingID;
    }

    private MainLeftContainer mainLeftContainer;
    private MainRightDetailsView mainRightDetailsView;
    private MyNamePanel myNamePanel;
    private MainButtonsPanel mainButtonsPanel;
    private JPanel main_right;
    private MainButtons mainButtons;
    private JPanel ringToolsContent;
    private RingToolsRightContainer ringToolsRightContainer;
    private RingToolsButtonPanel ringToolsButtonPanel;
    private RingToolsTitlePanel ringToolsTitlePanel;
    private AddFriendMainPanel addFriendMainPanel;

    public AddFriendMainPanel getAddFriendMainPanel() {
        if (addFriendMainPanel == null) {
            addFriendMainPanel = new AddFriendMainPanel();
        }
        return addFriendMainPanel;
    }

    public MyNamePanel getMyNamePanel() {
        return myNamePanel;
    }

    public void setMyNamePanel(MyNamePanel myNamePanel) {
        this.myNamePanel = myNamePanel;
    }

    public MainButtonsPanel getMainButtonsPanel() {
        return mainButtonsPanel;
    }

    public void setMainButtonsPanel(MainButtonsPanel mainButtonsPanel) {
        this.mainButtonsPanel = mainButtonsPanel;
    }

    public RingToolsRightContainer getRingToolsRightContainer() {
        return ringToolsRightContainer;
    }

    public void setRingToolsRightContainer(RingToolsRightContainer ringToolsRightContainer) {
        this.ringToolsRightContainer = ringToolsRightContainer;
    }

    public RingToolsButtonPanel getRingToolsButtonPanel() {
        return ringToolsButtonPanel;
    }

    public void setRingToolsButtonPanel(RingToolsButtonPanel ringToolsButtonPanel) {
        this.ringToolsButtonPanel = ringToolsButtonPanel;
    }

    public RingToolsTitlePanel getRingToolsTitlePanel() {
        return ringToolsTitlePanel;
    }

    public void setRingToolsTitlePanel(RingToolsTitlePanel ringToolsTitlePanel) {
        this.ringToolsTitlePanel = ringToolsTitlePanel;
    }

    public MainButtons getMainButtons() {
        return mainButtons;
    }

    public void setMainButtons(MainButtons mainButtons) {
        this.mainButtons = mainButtons;
    }

    /**/
    private MenuBarTop topMenuBar;

    public MenuBarTop getTopMenuBar() {
        return topMenuBar;
    }

    public void setTopMenuBar(MenuBarTop topMenuBar) {
        this.topMenuBar = topMenuBar;
    }

    private void setObject() {
        setInstance(this);
    }

    public GuiRingID() {
        setObject();
        MyFnFSettings.IS_FIRST_TIME_LOGIN = false;
        main_contentPane.addComponentListener(componentListener);

        buildDefaultMenubar();
        JPanel main_left = new JPanel();
        main_left.setBorder(new MatteBorder(0, 0, 0, DefaultSettings.DEFAULT_BORDER_WIDTH, RingColorCode.DEFAULT_BORDER_COLOR));
        main_left.setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        main_left.setPreferredSize(new Dimension(DefaultSettings.LEFT_FRIEND_LIST_WIDTH, 0));
        main_left.setBackground(RingColorCode.DEFAULT_SHADED_BACKGROUND_COLOR);
        main_contentPane.add(main_left, BorderLayout.WEST);

        JPanel myNameAndMainMenuItems = new JPanel();
        myNameAndMainMenuItems.setOpaque(false);
        myNameAndMainMenuItems.setLayout(new BorderLayout());
        main_left.add(myNameAndMainMenuItems, BorderLayout.NORTH);

        myNamePanel = new MyNamePanel();
        mainButtonsPanel = new MainButtonsPanel();
        myNameAndMainMenuItems.add(myNamePanel, BorderLayout.NORTH);
        myNameAndMainMenuItems.add(mainButtonsPanel, BorderLayout.CENTER);

        mainLeftContainer = new MainLeftContainer();
        main_left.add(mainLeftContainer, BorderLayout.CENTER);

        main_right = new JPanel();
        main_right.setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        main_right.setBackground(RingColorCode.DEFAULT_SHADED_BACKGROUND_COLOR);
        main_contentPane.add(main_right, BorderLayout.EAST);

        mainButtons = new MainButtons();
        main_right.add(mainButtons, BorderLayout.WEST);

        ringToolsContent = new JPanel(new BorderLayout());
        ringToolsContent.setOpaque(false);

        JPanel ringToolsTitleButtonItems = new JPanel();
        ringToolsTitleButtonItems.setOpaque(false);
        ringToolsTitleButtonItems.setLayout(new BorderLayout());
        ringToolsContent.add(ringToolsTitleButtonItems, BorderLayout.NORTH);

        ringToolsTitlePanel = new RingToolsTitlePanel();
        ringToolsButtonPanel = new RingToolsButtonPanel();
        ringToolsTitleButtonItems.add(ringToolsTitlePanel, BorderLayout.NORTH);
        ringToolsTitleButtonItems.add(ringToolsButtonPanel, BorderLayout.CENTER);

        ringToolsRightContainer = new RingToolsRightContainer();
        ringToolsContent.add(ringToolsRightContainer, BorderLayout.CENTER);

        mainRightDetailsView = new MainRightDetailsView();
        main_contentPane.add(mainRightDetailsView, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {

                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                    SingleImgeDetails.getSingleImgeDetails().setVisible(false);
                    SingleImgeDetails.setSingleImgeDetails(null);
                } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                    SingleImageDetailsForNotifications.getSingleImgeDetails().setVisible(false);
                    SingleImageDetailsForNotifications.setSingleImgeDetails(null);
                } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                    SingleImageDetailsProfileCover.getSingleImgeDetails().setVisible(false);
                    SingleImageDetailsProfileCover.setSingleImgeDetails(null);
                }
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.EXIT_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    try {
                        SignOutActions signOutActions = new SignOutActions(true);
                        signOutActions.doClean();
                        signOutActions.setVisible(true);
                    } catch (Exception ee) {
                        log.error("Can not exit" + ee.getMessage());
                        System.exit(0);
                    }
                }
            }
        });
        init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                addInitialData();
                sendDefaultRequestToServer();
            }
        });
    }

    private void init() {
        getMyNamePanel().init();
        getMainButtonsPanel().init();
        getRingToolsTitlePanel().init();
        getRingToolsButtonPanel().init();
        main_contentPane.revalidate();
        try {
            DefaultSettings.MONITOR_HALF_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
        } catch (Exception e) {
        }

    }

    private void addInitialData() {
        //   getMyNameAndNotificationPanel().change_myFullNameRingID();
        getMyNamePanel().change_myFullNameRingID();
        getMainButtonsPanel().doFriendlistButtonClick();
//        if (GuiRingID.getInstance().getMainLeftContainer() != null
//                && GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer() != null) {
//            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().initIncomingRequestNotification();
//        }
//        if (getMainButtonsPanel() != null) {
//            getMainButtonsPanel().addIncomingRequestNotification();
//        }
    }

    public void sendDefaultRequestToServer() {
        new DefaultRequest(AppConstants.TYPE_CONTACT_LIST).start();
        new DefaultRequest(AppConstants.TYPE_CIRCLE_LIST).start();
        new NotificationRequest(AppConstants.NOTIFICATION_MAX_UT, (short) 1).start();
        //new DefaultRequest(AppConstants.TYPE_YOU_MAY_KNOW_LIST).start();
        ChatService.sendOfflineChatRequest();
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            new NewsFeeds(0, 20).start();
        }
    }

    public void buildDefaultMenubar() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
        if (topMenuBar == null) {
            topMenuBar = new MenuBarTop();
        }
        main_contentPane.add(topMenuBar, BorderLayout.NORTH);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }
    /*getter setter*/

    public MainLeftContainer getMainLeftContainer() {
        return mainLeftContainer;
    }

    public void setMainLeftContainer(MainLeftContainer mainLeftContainer) {
        this.mainLeftContainer = mainLeftContainer;
    }

    public MainRightDetailsView getMainRight() {
        return mainRightDetailsView;
    }

    public void setMainRight(MainRightDetailsView mainRight) {
        this.mainRightDetailsView = mainRight;
    }
    /**/

    public void loadIntoLeftContainer(JPanel jPanel) {

        try {
            if (mainLeftContainer != null) {
                mainLeftContainer.removeAll();
                mainLeftContainer.add(jPanel);
                mainLeftContainer.revalidate();
                mainLeftContainer.repaint();
            }
        } catch (Exception e) {
            log.error("Error loading data in right main panel==> " + e.getMessage());
        }

    }

    public void loadIntoRingToolsRightContainer(final JPanel jPanel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ringToolsRightContainer != null) {
                        ringToolsRightContainer.removeAll();
                        ringToolsRightContainer.add(jPanel);
                        ringToolsRightContainer.revalidate();
                        ringToolsRightContainer.repaint();
                    }
                } catch (Exception e) {
                    log.error("Error loading data in ringToolsRightContainer => " + e.getMessage());
                }
            }
        });
    }

    public NotificationCounterOvalLabel getHomeCount() {
        if (getMainButtons() != null) {
            return getMainButtons().getBookHomeNotification();
        }
        return null;
    }

    public void signoutActions(boolean systemExit) {
        try {
            SignOutActions signOutActions = new SignOutActions(systemExit);
            signOutActions.doClean();
            signOutActions.setVisible(true);
        } catch (Exception e) {
            log.error("Error while sign up" + e.getMessage());
            System.exit(0);
        }

    }

    public void clear_all_hashmaps() {
        try {
            NewsFeedMaps.hash_maps = null;
            MyfnfHashMaps.hash_maps = null;
            FriendList.contactListObject = null;
        } catch (Exception e) {
        }
    }

    public JPanel getMainRightContent() {
        return (JPanel) getMainRight().getComponent(0);
    }

    public synchronized void loadIntoMainRightContent(final JPanel jPanel) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!(jPanel instanceof AddFriendMainPanel)) {
                        GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().setDefaultStickerImage();
                    } else {
                        //AllFriendList.setFriendSelectionColor(null, true);
                        getTopMenuBar().resetMenuColor(null);
                    }
                    getMainRight().removeAll();
                    getMainRight().add(jPanel, BorderLayout.CENTER);
                    getMainRight().revalidate();
                    getMainRight().repaint();
                } catch (Exception e) {
                }
            }
        });
    }

    public void showFriendProfile(String userIdentity) {
        getMainRight().showFriendProfile(userIdentity, null);
    }

    public void showFriendProfile(String userIdentity, Integer type) {
        getMainRight().showFriendProfile(userIdentity, type);
    }

    public void showMyProfile() {
        action_of_myProfile_button();
    }

    public void action_of_myProfile_button() {
        getMainRight().action_of_myProfile_button();
    }

    public void showUnknownProfile(UserBasicInfo basicInfo) {
        getMainRight().showUnknownProfile(basicInfo);
    }

    public void showProfileImageChange(int imageType) {
        getMainRight().showProfileImageChange(imageType);
    }

  //  SwingWorker<String, Void> worker = null;
    public boolean setRightContentVisible(final boolean isExpand) {
        boolean prevState = mainButtons.getWidth() > DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH;
        if (isExpand != prevState) {
            final int minWidth = isExpand
                    ? DefaultSettings.FRAME_DEFAULT_WIDTH + (DefaultSettings.RIGHT_RING_TOOLS_EXPAND_WIDTH - DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH)
                    : DefaultSettings.FRAME_DEFAULT_WIDTH;
            final int minHeight = DefaultSettings.FRAME_DEFAULT_HEIGHT;

            if (getExtendedState() != 6) {
                Dimension prev = getSize();
                if (!isExpand) {
                    rebuildRightView(isExpand);
                    setMinimumSize(new Dimension(minWidth, minHeight));
                }

                ExpandCollapseAnimation obj = new ExpandCollapseAnimation(prev, new Dimension(minWidth, minHeight), new CompleteListener() {
                    @Override
                    public void onComplete() {
                        if (isExpand) {
                            rebuildRightView(isExpand);
                            setMinimumSize(new Dimension(minWidth, minHeight));
                            getRingToolsButtonPanel().onClick_RingMarket();
                        }
                    }
                });
                obj.start();
            } else {
                rebuildRightView(isExpand);
                setMinimumSize(new Dimension(minWidth, minHeight));
                if (isExpand) {
                    getRingToolsButtonPanel().onClick_RingMarket();
                }
            }

            return true;
        }

        return false;
    }

    private interface CompleteListener {

        public void onComplete();
    }

    private void rebuildRightView(boolean isExpand) {
        mainButtons.setMainButtonsView(isExpand);
        main_right.removeAll();
        if (isExpand) {
            main_right.add(mainButtons, BorderLayout.NORTH);
            main_right.add(ringToolsContent, BorderLayout.CENTER);
        } else {
            main_right.add(mainButtons, BorderLayout.WEST);
        }
        main_right.revalidate();
        main_right.repaint();
    }

    public RightContentOnWindowResize onWindowResize;

    ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            if (onWindowResize == null || !onWindowResize.isRunning()) {
                onWindowResize = new RightContentOnWindowResize();
                onWindowResize.resizing();
                onWindowResize.startThread();
            } else {
                onWindowResize.resizing();
            }
        }
    };

    private class RightContentOnWindowResize extends Thread {

        private boolean running = true;
        private Long MAX_VALUE = (long) 1000;
        private Long time = 0L;
        private boolean prevStatus = false;

        public void resizing() {
            this.time = MAX_VALUE;
        }

        @Override
        public void run() {
            try {
                prevStatus = mainButtons.getWidth() > DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH;
                while (running) {
                    if (time <= 0) {
                        stopThread();
                        continue;
                    }

                    Thread.sleep(200);
                    time -= 200;

                    if (time <= 0) {
                        stopThread();
                    }
                }
            } catch (Exception e) {
                stopThread();
            }
        }

        public void startThread() {
            running = true;
            this.start();
        }

        public void stopThread() {
            refrestWindow();
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public void refrestWindow() {
            try {
                if (!prevStatus
                        && main_contentPane.getParent().getWidth() > (DefaultSettings.FRAME_DEFAULT_WIDTH + DefaultSettings.RIGHT_RING_TOOLS_EXPAND_WIDTH - DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH)
                        && GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getTopMenuBar() != null) {
                    GuiRingID.getInstance().getTopMenuBar().setExpand();
                }
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Exception in refrestWindow ==> " + ex.getMessage());
            }
        }

    }

    private class ExpandCollapseAnimation extends Thread {

        double x, y, dx, dy, pixel;
        private Dimension prev;
        private Dimension target;
        private CompleteListener listener;

        public ExpandCollapseAnimation(Dimension prev, Dimension target, CompleteListener listener) {
            this.prev = prev;
            this.target = target;
            this.listener = listener;
        }

        @Override
        public void run() {
            doInBackground();
        }

        public void doInBackground() {

            //System.err.println("TARGET ==> " + prev.getWidth() + " || " + prev.getHeight());
            //System.err.println("DEST   ==> " + target.getWidth() + " || " + target.getHeight());
            //System.err.println("DIFF   ==> " + (prev.getWidth() - target.getWidth()));
            dx = Math.abs(prev.getWidth() - target.getWidth());
            x = prev.getWidth();
            y = prev.getHeight();

            pixel = dx / 20;
            dx = 20;

            boolean dxGrow = true;
            if (prev.getWidth() < target.getWidth()) {
                dxGrow = false;
            }

            float i = 1f;
            while (i <= pixel) {
                if (dxGrow) {
                    x -= dx;
                } else {
                    x += dx;
                }

                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }

                setSize(new Dimension((int) x, (int) y));
                invalidate();

                i += 1;
            }
            setSize(new Dimension((int) target.getWidth(), (int) target.getHeight()));
            listener.onComplete();
        }

    }

}

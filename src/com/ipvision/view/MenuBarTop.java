/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.ChangeNotificationStateRequest;
import com.ipvision.service.ChangePresence;
import com.ipvision.service.NewsFeeds;
import com.ipvision.service.singinsignup.SignOutActions;
import com.ipvision.view.feed.AllNotificationPanel;
import com.ipvision.view.circle.CircleList;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.JsonFields;
import com.ipvision.view.settingsmenu.RingIDSettingsDialog;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.NotificationCounterOrangeLabel;
import com.ipvision.view.utility.NotificationCounterOvalLabel;
import com.ipvision.view.utility.ImagePane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;

/**
 *
 * @author Faiz
 */
public final class MenuBarTop extends JPanel implements ActionListener, MouseListener {

    public static final String BLOG = "Blog";
    public static final String FAQ = "FAQ";
    public static final String THIRD_PARTY_POLICY = "Third Party Policy";
    public static final String PRIVACY = "Privacy Policy";
    public static final String FEATURES = "Features";//FEATURES
    public static final String MEDIA_KIT = "Media Kit";
    private final String ONLINE_OFFLINE_STATUS = "Online Status";
    private final String ONLINE = "Online";
    private final String OFFLINE = "Offline";
    private final String SIGN_OUT = "Sign out";
    private final String CLOSE = "Close";

    private final String MENU_TYPE_BOOK_HOME = "MENU_TYPE_BOOK_HOME";
    private final String MENU_TYPE_CIRCLE = "MENU_TYPE_CIRCLE";
    private final String MENU_TYPE_GROUP = "MENU_TYPE_GROUP";
    private final String MENU_TYPE_ALLNOTIFICATION = "MENU_TYPE_ALLNOTIFICATION";
    private JMenuBar menuBar;
    private final JMenu mnuRingID;
    private final JMenu mnuAbout;
    private final JMenu menuSettings;
    private JMenu mnuOnlineStatus;
    private JMenuItem mnuItemOnline;
    private JMenuItem mnuItemOffline;
    private JMenuItem mnuItemSignOut;
    private JMenuItem mnuItemExit;
    private JMenuItem mnuItemBlog;
    private JMenuItem mnuItemFAQ;
    private JMenuItem mnuItemThirdPartyPolicy;
    private JMenuItem mnuItemPrivacy;
    private JMenuItem mnuItemFeatures;
    private JMenuItem mnuItemMediaKit;
    private RingIDSettingsDialog ringIDSettingsDialog;

    public final ImagePane bookHomePane = new ImagePane();
    public final ImagePane grpPane = new ImagePane();
    public final ImagePane circlePane = new ImagePane();
    public final ImagePane allNotificationPane = new ImagePane();
    public final ImagePane expandPane = new ImagePane();

    private final NotificationCounterOvalLabel bookHomeNotification = new NotificationCounterOvalLabel();
    private final NotificationCounterOrangeLabel circleNotification = new NotificationCounterOrangeLabel();
    private final NotificationCounterOrangeLabel groupNotification = new NotificationCounterOrangeLabel();
    private final NotificationCounterOrangeLabel allNotification = new NotificationCounterOrangeLabel();
    public boolean needToShowAllNotification = true;

    public BufferedImage bookHomeImg = null, bookHomeHoverImg = null, bookHomeSelectedImg = null;
    public BufferedImage expandImg = null, expandHoverImg = null;//, expandSelectedImg = null;
    public BufferedImage collapseImg = null, collapseHoverImg = null;//, collapseSelectedImg = null;
    public BufferedImage grpImg = null, grpHoverImg = null, grpSelectedImg = null;
    public BufferedImage circleImg = null, circleHoverImg = null, circleSelectedImg = null;
    public BufferedImage allNotificationImg = null, allNotificationHoverImg = null, allNotificationSelectedImg = null;

    public AllNotificationPanel allNotificationPanel = null;
    public JDialogNotification notificationDialog = null;
    public JDialogNotification allNotificationDialog = null;
    private BufferedImage bgImg = DesignClasses.return_buffer_image(GetImages.MENU_BAR_BG);

    public RingIDSettingsDialog getRingIDSettingsDialog() {
        return ringIDSettingsDialog;
    }

    public void setRingIDSettingsDialog(RingIDSettingsDialog ringIDSettingsDialog) {
        this.ringIDSettingsDialog = ringIDSettingsDialog;
    }

//    public NotificationCounterOrangeLabel getCircleNotification() {
//        return circleNotification;
//    }
//
//    public NotificationCounterOrangeLabel getGroupNotification() {
//        return groupNotification;
//    }
    public MenuBarTop() {
        try {
            bookHomeImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION));
            bookHomeHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION_H));
            bookHomeSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION_SELECTED));
            allNotificationImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION));
            allNotificationHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION_H));
            allNotificationSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION_SELECTED));
            grpImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP));
            grpHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP_H));
            grpSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP_SELECTED));
            circleImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP));
            circleHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP_H));
            circleSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP_SELECTED));
            expandImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EXPAND));
            expandHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EXPAND_H));
            collapseImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.COLLAPSE));
            collapseHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.COLLAPSE_H));
        } catch (Exception e) {
        }

        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(0, 32));

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(0, 0, 2, 0));
        container.setOpaque(false);
        add(container, BorderLayout.CENTER);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setOpaque(false);
        westPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        container.add(westPanel, BorderLayout.WEST);

        menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBorder(null);

        UIManager.put("Menu.background", RingColorCode.RING_THEME_COLOR);
        UIManager.put("Menu.foreground", RingColorCode.RING_TOP_MENU_FG_COLOR);
        UIManager.put("Menu.selectionBackground", RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
        UIManager.put("Menu.selectionForeground", RingColorCode.RING_TOP_MENU_SELECTED_FG_COLOR);
        UIManager.put("MenuItem.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("MenuItem.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);

        mnuRingID = getMenu(StaticFields.APP_NAME);
        menuBar.add(mnuRingID);
        menuBar.add(getSeperator());

        mnuAbout = getMenu("About");
        menuBar.add(mnuAbout);
        menuBar.add(getSeperator());

        menuSettings = getMenu("Settings");
        menuBar.add(menuSettings);
        menuSettings.addMouseListener(this);

        westPanel.add(menuBar, BorderLayout.WEST);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
//        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
//        eastPanel.setOpaque(false);
//        eastPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
//        eastPanel.add(bookHomeNotification);
//        eastPanel.add(bookHomePane);
//        eastPanel.add(circlePane);
//        eastPanel.add(grpPane);
//        eastPanel.add(allNotificationPane);
//        eastPanel.add(expandPane);
//        container.add(eastPanel, BorderLayout.EAST);

        init();
        changeMenuBarStatus();
    }

    public void changeMenuBarStatus() {
        if (MyFnFSettings.userProfile != null) {
            if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_ONLINE) {
                mnuItemOnline.setForeground(DefaultSettings.disable_font_color);
                mnuItemOnline.setEnabled(false);
                mnuItemOnline.removeActionListener(this);

                mnuItemOffline.setForeground(null);
                mnuItemOffline.setEnabled(true);
                mnuItemOffline.addActionListener(this);
            } else if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_DO_NOT_DISTURB) {
                mnuItemOffline.setForeground(DefaultSettings.disable_font_color);
                mnuItemOffline.setEnabled(false);
                mnuItemOffline.removeActionListener(this);

                mnuItemOnline.setForeground(null);
                mnuItemOnline.setEnabled(true);
                mnuItemOnline.addActionListener(this);
            }
        }
    }

    private void init() {
        UIManager.put("Menu.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("Menu.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);
        mnuOnlineStatus = getMenu1(ONLINE_OFFLINE_STATUS);
        mnuItemOnline = getMenuItem(ONLINE, GetImages.STATUS_ONLINE);
        mnuItemOffline = getMenuItem(OFFLINE, GetImages.STATUS_OFFLINE);
        mnuOnlineStatus.add(mnuItemOnline);
        mnuOnlineStatus.add(getMenuItemSeperator());
        mnuOnlineStatus.add(mnuItemOffline);

        mnuItemSignOut = getMenuItem(SIGN_OUT, null);
        mnuItemExit = getMenuItem(CLOSE, null);
        mnuItemBlog = getMenuItem(BLOG + " " + StaticFields.APP_NAME, null);
        mnuItemFAQ = getMenuItem(FAQ, null);
        mnuItemThirdPartyPolicy = getMenuItem(THIRD_PARTY_POLICY, null);
        mnuItemPrivacy = getMenuItem(PRIVACY, null);
        mnuItemFeatures = getMenuItem(FEATURES, null);
        mnuItemMediaKit = getMenuItem(MEDIA_KIT, null);
        //menuItemRingIDSettings = getMenuItem(RINGID_OPTIONS, null);

        mnuRingID.add(mnuOnlineStatus);
        mnuRingID.add(getMenuItemSeperator());
        mnuRingID.add(mnuItemSignOut);
        mnuRingID.add(getMenuItemSeperator());
        mnuRingID.add(mnuItemExit);
        removePopupBorder(mnuItemSignOut);
        mnuAbout.add(mnuItemBlog);
        mnuAbout.add(getMenuItemSeperator());
        mnuAbout.add(mnuItemPrivacy);
        mnuAbout.add(getMenuItemSeperator());
        mnuAbout.add(mnuItemFAQ);
        mnuAbout.add(getMenuItemSeperator());
        mnuAbout.add(mnuItemFeatures);
        mnuAbout.add(getMenuItemSeperator());
        mnuAbout.add(mnuItemThirdPartyPolicy);
        mnuAbout.add(getMenuItemSeperator());
        mnuAbout.add(mnuItemMediaKit);
        removePopupBorder(mnuItemBlog);
        //menuSettings.add(menuItemRingIDSettings);
        //removePopupBorder(menuItemRingIDSettings);

        allNotificationPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        allNotificationPane.setOpaque(false);
        allNotificationPane.setToolTipText("Notifications");
        allNotificationPane.setImage(allNotificationImg);
        allNotificationPane.addMouseListener(this);
        allNotificationPane.setRounded(false);
        allNotificationPane.add(allNotification);

        grpPane.setOpaque(false);
        grpPane.setToolTipText("Groups");
        grpPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        grpPane.setImage(grpImg);
        grpPane.addMouseListener(this);
        grpPane.setRounded(false);
        grpPane.add(groupNotification);

        circlePane.setOpaque(false);
        circlePane.setToolTipText("Circles");
        circlePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        circlePane.setImage(circleImg);
        circlePane.addMouseListener(this);
        circlePane.setRounded(false);
        circlePane.add(circleNotification);

        bookHomePane.setOpaque(false);
        bookHomePane.setToolTipText("Feed");
        bookHomePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bookHomePane.setImage(bookHomeSelectedImg);
        bookHomePane.addMouseListener(this);
        bookHomePane.setRounded(false);

        bookHomeNotification.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookHomeNotification.addMouseListener(this);

        expandPane.setOpaque(false);
        expandPane.setToolTipText("Show Ring Tools & App");
        expandPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        expandPane.setImage(expandImg);
        expandPane.addMouseListener(this);
        expandPane.setRounded(false);

    }

    private void exitMenuItemActionPerformed() {
        HelperMethods.showConfirmationDialogMessage(NotificationMessages.EXIT_NOTIFICAITON);
        if (JOptionPanelBasics.YES_NO) {
            SignOutActions.send_logout_request();
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (e.getSource() == mnuItemSignOut) {
                    HelperMethods.showConfirmationDialogMessage(NotificationMessages.SIGN_OUT_NOTIFICAITON);
                    if (JOptionPanelBasics.YES_NO) {
                        GuiRingID.getInstance().signoutActions(false);
                    }
                } else if (e.getSource() == mnuItemExit) {
                    exitMenuItemActionPerformed();
                } else if (e.getSource() == mnuItemBlog) {
                    new MenuAboutOpenWebPage(BLOG);

//                    RingAboutHtmlContainer blog = new RingAboutHtmlContainer(BLOG);
//                    GuiRingID.getInstance().loadIntoMainRightContent(blog);
//                    //AllFriendList.setFriendSelectionColor(null, true);
//                    CircleList.setCircleSelectionColor(null);
                } else if (e.getSource() == mnuItemFAQ) {
                    new MenuAboutOpenWebPage(FAQ);
//                    RingAboutHtmlContainer faq = new RingAboutHtmlContainer(FAQ);
//                    GuiRingID.getInstance().loadIntoMainRightContent(faq);
//                    //AllFriendList.setFriendSelectionColor(null, true);
//                    CircleList.setCircleSelectionColor(null);
                } else if (e.getSource() == mnuItemThirdPartyPolicy) {
                    new MenuAboutOpenWebPage(THIRD_PARTY_POLICY);
//                    RingAboutHtmlContainer thirdParty = new RingAboutHtmlContainer(THIRD_PARTY_POLICY);
//                    GuiRingID.getInstance().loadIntoMainRightContent(thirdParty);
//                    //AllFriendList.setFriendSelectionColor(null, true);
//                    CircleList.setCircleSelectionColor(null);

                } else if (e.getSource() == mnuItemPrivacy) {
                    new MenuAboutOpenWebPage(PRIVACY);
//                    RingAboutHtmlContainer privacy = new RingAboutHtmlContainer(PRIVACY);
//                    GuiRingID.getInstance().loadIntoMainRightContent(privacy);
//                    //AllFriendList.setFriendSelectionColor(null, true);
//                    CircleList.setCircleSelectionColor(null);

                } else if (e.getSource() == mnuItemFeatures) {
                    new MenuAboutOpenWebPage(FEATURES);
//                    RingAboutHtmlContainer info = new RingAboutHtmlContainer(INFO);
//                    GuiRingID.getInstance().loadIntoMainRightContent(info);
//                    //AllFriendList.setFriendSelectionColor(null, true);
//                    CircleList.setCircleSelectionColor(null);

                } else if (e.getSource() == mnuItemMediaKit) {
                    new MenuAboutOpenWebPage(MEDIA_KIT);
                } else if (e.getSource() == mnuItemOnline) {
                    new ChangePresence(StatusConstants.PRESENCE_ONLINE).start();
                } else if (e.getSource() == mnuItemOffline) {
                    new ChangePresence(StatusConstants.PRESENCE_DO_NOT_DISTURB).start();
                }
            }
        });

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == allNotificationPane) {
            if (needToShowAllNotification) {
                resetMenuColor(MENU_TYPE_ALLNOTIFICATION);
            } else {
                bookHomePane.setImage(bookHomeImg);
                allNotificationPane.setImage(allNotificationImg);
                circlePane.setImage(circleImg);
                grpPane.setImage(grpImg);
            }

            allNotification.setText("");
            allNotification.revalidate();
            //NotificationHistoryDAO.loadNotificationHistoryList(0, 1);    //this line needed if menubar action is required again
            if (needToShowAllNotification && MyfnfHashMaps.getInstance().getNotificationsMap() != null && MyfnfHashMaps.getInstance().getNotificationsMap().size() > 0) {
             //   allNotificationDialog = new JDialogNotification(allNotificationPane, 213, JDialogNotification.POPUP_ALL_NOTIFICATION);
                allNotificationDialog.setVisible(true);
                notificationDialog = allNotificationDialog;

             //   new AddToNotifications(allNotificationDialog).start();   //this line needed if menubar acton  is required again

                if (AppConstants.NOTIFICATION_MAX_UT > AppConstants.NOTIFICATION_LAST_MAX_UT) {
                    AppConstants.NOTIFICATION_LAST_MAX_UT = AppConstants.NOTIFICATION_MAX_UT;
                    new ChangeNotificationStateRequest().start();

                }

                needToShowAllNotification = false;
            } else {
                needToShowAllNotification = true;
            }
        } else if (e.getSource() == bookHomePane || e.getSource() == bookHomeNotification) {
            bookHomeNotification.setText("");
            bookHomeNotification.revalidate();
            bookHomeNotification.repaint();
            if (e.getSource() == bookHomeNotification) {
                HelperMethods.clearAllBookHomeUIMaps();
            }
            action_of_book_button();
        } else if (e.getSource() == circlePane || e.getSource() == circleNotification) {
            circleNotification.setText("");
            circleNotification.revalidate();
            circleNotification.repaint();
            action_of_circle_button();
        } else if (e.getSource() == grpPane || e.getSource() == groupNotification) {
            groupNotification.setText("");
            groupNotification.revalidate();
            groupNotification.repaint();
            action_of_group_button();
        } else if (e.getSource() == expandPane) {
            if (expandPane.getImage().equals(expandImg) || expandPane.getImage().equals(expandHoverImg)) {
                setExpand();
            } else {
                setCollapse();
            }

        } else if (e.getSource() == menuSettings) {
            if (ringIDSettingsDialog == null) {
                ringIDSettingsDialog = new RingIDSettingsDialog();
            }
            ringIDSettingsDialog.setVisible(true);
            ringIDSettingsDialog.toFront();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == this) {
            setBackground(DefaultSettings.ORANGE_BACKGROUND_COLOR_PRESSED);
            //  revalidate();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == this) {
            setBackground(DefaultSettings.ORANGE_BACKGROUND_COLOR);
            //  revalidate();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == bookHomePane) {
            bookHomePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!bookHomePane.getImage().equals(bookHomeSelectedImg)) {
                bookHomePane.setImage(bookHomeHoverImg);
            }
        } else if (e.getSource() == allNotificationPane) {
            allNotificationPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!allNotificationPane.getImage().equals(allNotificationSelectedImg)) {
                allNotificationPane.setImage(allNotificationHoverImg);
            }
        } else if (e.getSource() == grpPane) {
            grpPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!grpPane.getImage().equals(grpSelectedImg)) {
                grpPane.setImage(grpHoverImg);
            }
        } else if (e.getSource() == circlePane) {
            circlePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!circlePane.getImage().equals(circleSelectedImg)) {
                circlePane.setImage(circleHoverImg);
            }
        } else if (e.getSource() == expandPane) {
            expandPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (expandPane.getImage().equals(expandImg)) {
                expandPane.setImage(expandHoverImg);
            } else if (expandPane.getImage().equals(collapseImg)) {
                expandPane.setImage(collapseHoverImg);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == bookHomePane) {
            bookHomePane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!bookHomePane.getImage().equals(bookHomeSelectedImg)) {
                bookHomePane.setImage(bookHomeImg);
            }
        } else if (e.getSource() == allNotificationPane) {
            allNotificationPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!allNotificationPane.getImage().equals(allNotificationSelectedImg)) {
                allNotificationPane.setImage(allNotificationImg);
            }
        } else if (e.getSource() == grpPane) {
            grpPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!grpPane.getImage().equals(grpSelectedImg)) {
                grpPane.setImage(grpImg);
            }
        } else if (e.getSource() == circlePane) {
            circlePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!circlePane.getImage().equals(circleSelectedImg)) {
                circlePane.setImage(circleImg);
            }
        } else if (e.getSource() == expandPane) {
            expandPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (expandPane.getImage().equals(expandHoverImg)) {
                expandPane.setImage(expandImg);
            } else if (expandPane.getImage().equals(collapseHoverImg)) {
                expandPane.setImage(collapseImg);
            }
        }
    }

    private JMenu getMenu(String title) {
        JMenu jMenu = new JMenu(title);
        jMenu.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        jMenu.setBorder(null);
        return jMenu;
    }

    private JMenu getMenu1(String title) {
        JMenu jMenu = new JMenu(title);
        jMenu.setPreferredSize(new Dimension(180, 35));
        jMenu.setOpaque(true);
        jMenu.setBorder(null);
        return jMenu;
    }

    private JMenuItem getMenuItem(String title, String imageUrl) {
        JMenuItem jMenuItem = new JMenuItem(title);
        if (imageUrl != null) {
            jMenuItem.setIcon(DesignClasses.return_image(imageUrl));
        } else {
            //System.out.println("no icon image for MenuItem ==>" + title);
        }
        jMenuItem.setPreferredSize(new Dimension(180, 35));
        jMenuItem.setBorder(null);
        jMenuItem.setOpaque(true);
        jMenuItem.addActionListener(this);
        return jMenuItem;
    }

    public JPanel getSeperator() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); //7
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(15, 35));

//        JLabel seperator = new JLabel(seperatorImg);
//        seperator.setOpaque(false);
//        wrapper.add(seperator);
        return wrapper;
    }

    public JPanel getMenuItemSeperator() {
        JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        seperator.setBackground(new Color(0xe5e6e9));
        seperator.setPreferredSize(new Dimension(1, 1));
        return seperator;
    }

    private void removePopupBorder(JMenuItem menuItem) {
        JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
        popupMenu.setBorder(null);
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        popupMenu.setBackground(Color.WHITE);

    }

    private class AddToNotifications extends Thread {

        private final JDialogNotification jdialogNotification;

        public AddToNotifications(JDialogNotification dialog) {
            this.jdialogNotification = dialog;
        }

        @Override
        public void run() {
            try {
                List<JsonFields> notifList = NotificationHistoryDAO.loadNotificationHistoryList(0, 10);  
                if (notifList != null && notifList.size() > 0) {
               //     jdialogNotification.addAllNotifications(notifList, 10, true);
                }
            } catch (Exception e) {
            }
        }
    }

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
    }

    private void loadIntoMainRightContent(JPanel panel) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().loadIntoMainRightContent(panel);
        }
    }

    public synchronized void action_of_book_button() {
        GuiRingID.getInstance().getMainButtonsPanel().doFriendlistButtonClick();
        if (getMainRight() != null) {
            resetMenuColor(MENU_TYPE_BOOK_HOME);
            //AllFriendList.setFriendSelectionColor(null, true);
            CircleList.setCircleSelectionColor(null);
            if (getMainRight().getComponentCount() > 0 && getMainRight().getComponent(0) != getMainRight().getAllFeedsView()) {
                loadIntoMainRightContent(getMainRight().getAllFeedsView());
            }

            if (NewsFeedMaps.getInstance().getAllNewfeedId().hasDataFromServer() == false) {
                new NewsFeeds(0, 20).start();
            } else if (NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size() > 0 || getMainRight().getAllFeedsView().isDisplayable()) {
                getMainRight().getAllFeedsView().loadTopForcefully();
            }
            DesignClasses.setArrowKeyScroll(getMainRight().getAllFeedsView().scrollContent);
        }
    }

    public synchronized void action_of_group_button() {
        if (getMainRight() != null) {
            resetMenuColor(MENU_TYPE_GROUP);
            groupNotification.setText("");
            groupNotification.revalidate();
            groupNotification.repaint();
            getMainRight().showGroupViewPanel();
            /*            AllFriendList.setFriendSelectionColor(null);
             CircleList.setCircleSelectionColor(null);
             if (getMainRight().getComponentCount() > 0 && getMainRight().getComponent(0) != getMainRight().getAllFeedsView()) {
             loadIntoMainRightContent(getMainRight().getAllFeedsView());
             }
            
             if (NewsFeedMaps.getInstance().getAllNewfeedId().hasDataFromServer() == false) {
             new NewsFeeds(0).start();
             } else if (NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size() > 0 || getMainRight().getAllFeedsView().isDisplayable()) {
             getMainRight().getAllFeedsView().loadTopForcefully();
             }*/
        }
    }

    public synchronized void action_of_circle_button() {
        if (getMainRight() != null) {
            resetMenuColor(MENU_TYPE_CIRCLE);
            circleNotification.setText("");
            circleNotification.revalidate();
            circleNotification.repaint();
            getMainRight().showCircleViewPanel();
            /*            AllFriendList.setFriendSelectionColor(null);
             CircleList.setCircleSelectionColor(null);
             if (getMainRight().getComponentCount() > 0 && getMainRight().getComponent(0) != getMainRight().getAllFeedsView()) {
             loadIntoMainRightContent(getMainRight().getAllFeedsView());
             }
            
             if (NewsFeedMaps.getInstance().getAllNewfeedId().hasDataFromServer() == false) {
             new NewsFeeds(0).start();
             } else if (NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size() > 0 || getMainRight().getAllFeedsView().isDisplayable()) {
             getMainRight().getAllFeedsView().loadTopForcefully();
             }*/
        }
    }

    public void action_of_all_notifications() {
        allNotificationPanel = AllNotificationPanel.getInstance();
        allNotificationPanel.addAllNotifications();
        GuiRingID.getInstance().loadIntoMainRightContent(allNotificationPanel);
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(null);
    }

    public synchronized void action_of_book_button_notification() {
        if (getMainRight() != null) {
            //resetMenuColor(MENU_TYPE_BOOK);
            //AllFriendList.setFriendSelectionColor(null, true);
            CircleList.setCircleSelectionColor(null);
            if (getMainRight().getComponentCount() > 0 && getMainRight().getComponent(0) != getMainRight().getAllFeedsView()) {
                loadIntoMainRightContent(getMainRight().getAllFeedsView());
            }
        }
    }

    public void addNotification(int numbers) {
        String previous_num = allNotification.getText();
        int previous = 0;
        if (previous_num.length() > 0) {
            previous = Integer.parseInt(previous_num);
        }
        if (numbers > 0) {
            allNotification.setText(String.valueOf(numbers + previous));
        } else {
            if (previous > 0) {
                allNotification.setText(String.valueOf(previous));
            } else {
                allNotification.setText("");
            }
        }
        allNotification.revalidate();
    }

    public void IncreaseNotification() {
        String previous_num = allNotification.getText();
        int previous = 0;
        if (previous_num.length() > 0) {
            previous = Integer.parseInt(previous_num);
        }
        allNotification.setText(String.valueOf(1 + previous));

        allNotification.revalidate();
    }

    public void resetMenuColor(String selectedMenu) {
        try {
            if (selectedMenu == null) {
                bookHomePane.setImage(bookHomeImg);
                allNotificationPane.setImage(allNotificationImg);
                grpPane.setImage(grpImg);
                circlePane.setImage(circleImg);
            } else {
                if (selectedMenu.equalsIgnoreCase(MENU_TYPE_BOOK_HOME)) {
                    bookHomePane.setImage(bookHomeSelectedImg);
                    allNotificationPane.setImage(allNotificationImg);
                    grpPane.setImage(grpImg);
                    circlePane.setImage(circleImg);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_ALLNOTIFICATION)) {
                    bookHomePane.setImage(bookHomeImg);
                    grpPane.setImage(grpImg);
                    circlePane.setImage(circleImg);
                    allNotificationPane.setImage(allNotificationSelectedImg);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_CIRCLE)) {
                    bookHomePane.setImage(bookHomeImg);
                    grpPane.setImage(grpImg);
                    circlePane.setImage(circleSelectedImg);
                    allNotificationPane.setImage(allNotificationImg);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_GROUP)) {
                    bookHomePane.setImage(bookHomeImg);
                    grpPane.setImage(grpSelectedImg);
                    circlePane.setImage(circleImg);
                    allNotificationPane.setImage(allNotificationImg);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean setExpand() {
        boolean isStateChange = GuiRingID.getInstance().setRightContentVisible(true);
        //expandPane.setImage(collapseImg);
        //expandPane.setToolTipText("Hide Ring Tools & App");
        return isStateChange;
    }

    public boolean setCollapse() {
        boolean isStateChange = GuiRingID.getInstance().setRightContentVisible(false);
        //expandPane.setImage(expandImg);
        //expandPane.setToolTipText("Show Ring Tools & App");
        return isStateChange;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = new Rectangle(0, 0, bgImg.getWidth(), bgImg.getHeight());
        TexturePaint textrue = new TexturePaint(bgImg, r);
        g2.setPaint(textrue);
        g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
    }

}

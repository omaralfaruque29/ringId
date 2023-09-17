/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import com.ipvision.model.RecentDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.leftdata.CallHistoryContainer;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.view.utility.NotificationCounterOrangeLabel;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.SeparatorPanel;

/**
 *
 * @author Faiz
 */
public class MainButtonsPanel extends JPanel implements MouseListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MainButtonsPanel.class);
    public static final String MENU_TYPE_FRIENDLIST = "MENU_TYPE_FRIENDLIST";
    public static final String MENU_TYPE_CHAT = "MENU_TYPE_CHAT";
    public static final String MENU_TYPE_CALLHISTORY = "MENU_TYPE_CALLHISTORY";
    public static final String MENU_TYPE_ALL_NOTIFICATION = "MENU_TYPE_ALL_NOTIFICATION";

    private ImagePane friendPane = new ImagePane();
    private ImagePane callhistoryPane = new ImagePane();
    private ImagePane chathistoryPane = new ImagePane();
    private ImagePane allNotificationPane = new ImagePane();

    private final NotificationCounterOrangeLabel chatNotification = new NotificationCounterOrangeLabel();
    private final NotificationCounterOrangeLabel callhistoryNotification = new NotificationCounterOrangeLabel();
    public final NotificationCounterOrangeLabel allNotification = new NotificationCounterOrangeLabel();
    private JPanel seperator1, seperator2, seperator3;
    private JPanel topPanel;

    public BufferedImage friendImg = null, friendHoverImg = null, friendSelectedImg = null;
    public BufferedImage callhistoryImg = null, callhistoryHoverImg = null, callhistorySelectedImg = null;
    public BufferedImage chatImg = null, chatHoverImg = null, chatSelectedImg = null;
    public BufferedImage allNotificationImg = null, allNotificationHoverImg = null, allNotificationSelectedImg = null;

    public MainButtonsPanel() {
        setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
        setPreferredSize(new Dimension(DefaultSettings.LEFT_FRIEND_LIST_WIDTH, DefaultSettings.MENUBER_HEIGHT));
        setLayout(new BorderLayout());
    }

    public void init() {
        try {
            topPanel = new JPanel();
            topPanel.setOpaque(false);
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            add(topPanel, BorderLayout.CENTER);
            try {
                friendImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.FRIEND_ICON));
                friendHoverImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.FRIEND_ICON_H));
                friendSelectedImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.FRIEND_ICON_SELECTED));
            } catch (Exception e) {
            }
            friendPane.setImage(friendImg);
            friendPane.setToolTipText("Friends");
            friendPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 13, 2));
            friendPane.setRounded(false);
            friendPane.addMouseListener(this);

            seperator1 = new SeparatorPanel(1, 50, 8);
            seperator2 = new SeparatorPanel(1, 50, 8);
            seperator3 = new SeparatorPanel(1, 50, 8);
            try {
                callhistoryImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.CALLHISTORY_ICON));
                callhistoryHoverImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.CALLHISTORY_ICON_H));
                callhistorySelectedImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.CALLHISTORY_ICON_SELECTED));
            } catch (Exception e) {
            }
            callhistoryPane.setImage(callhistoryImg);
            callhistoryPane.setToolTipText("Call History");
            callhistoryPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 13, 2));
            callhistoryPane.add(callhistoryNotification);
            callhistoryPane.setRounded(false);
            callhistoryPane.addMouseListener(this);

            try {
                chatImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.MSG_ICON));
                chatHoverImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.MSG_ICON_H));
                chatSelectedImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.MSG_ICON_SELECTED));
            } catch (Exception e) {
            }
            chathistoryPane.setImage(chatImg);
            chathistoryPane.setToolTipText("Chats");
            chathistoryPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 13, 2));
            chathistoryPane.add(chatNotification);
            chathistoryPane.setRounded(false);
            chathistoryPane.addMouseListener(this);

            try {
                allNotificationImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION));
                allNotificationHoverImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION_H));
                allNotificationSelectedImg = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION_SELECTED));
            } catch (Exception e) {
            }
            allNotificationPane.setImage(allNotificationImg);
            allNotificationPane.setToolTipText("Notifications");
            allNotificationPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 13, 2));
            allNotificationPane.add(allNotification);
            allNotificationPane.setRounded(false);
            allNotificationPane.addMouseListener(this);

            topPanel.add(friendPane);
            topPanel.add(seperator1);
            topPanel.add(callhistoryPane);
            topPanel.add(seperator2);
            topPanel.add(chathistoryPane);
            topPanel.add(seperator3);
            topPanel.add(allNotificationPane);

        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Exception in MainButtonsPanel class ==>" + ex.getMessage());
        }
    }

    public void resetMainButtonsColor(String selectedMenu) {
        try {
            if (selectedMenu == null) {
                friendPane.setImage(friendImg);
                chathistoryPane.setImage(chatImg);
                callhistoryPane.setImage(callhistoryImg);
                allNotificationPane.setImage(allNotificationImg);
            } else {
                if (selectedMenu.equalsIgnoreCase(MENU_TYPE_FRIENDLIST)) {
                    friendPane.setImage(friendSelectedImg);
                    chathistoryPane.setImage(chatImg);
                    callhistoryPane.setImage(callhistoryImg);
                    allNotificationPane.setImage(allNotificationImg);
//                    friendPane.setBackground(RingColorCode.LEFT_PANEL_SELECTED_COLOR);
//                    chathistoryPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
//                    callhistoryPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_CALLHISTORY)) {
                    friendPane.setImage(friendImg);
                    chathistoryPane.setImage(chatImg);
                    callhistoryPane.setImage(callhistorySelectedImg);
                    allNotificationPane.setImage(allNotificationImg);
//                    friendPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
//                    chathistoryPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
//                    callhistoryPane.setBackground(RingColorCode.LEFT_PANEL_SELECTED_COLOR);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_CHAT)) {
                    friendPane.setImage(friendImg);
                    chathistoryPane.setImage(chatSelectedImg);
                    callhistoryPane.setImage(callhistoryImg);
                    allNotificationPane.setImage(allNotificationImg);
//                    friendPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
//                    chathistoryPane.setBackground(RingColorCode.LEFT_PANEL_SELECTED_COLOR);
//                    callhistoryPane.setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_ALL_NOTIFICATION)) {
                    friendPane.setImage(friendImg);
                    chathistoryPane.setImage(chatImg);
                    callhistoryPane.setImage(callhistoryImg);
                    allNotificationPane.setImage(allNotificationSelectedImg);
                }
            }
        } catch (Exception e) {
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
    
    

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
        //return GuiRingID.getInstance().getMainRight() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    public void doFriendlistButtonClick() {
        if (getMainRight().getComponent(0) != getMainLeftContainer().getFriendListContainer()) {
            getMainLeftContainer().loadIntoLeftContainer(getMainLeftContainer().getFriendListContainer());
        }
        /*if (incomingReqNotification.getText() != null
         && incomingReqNotification.getText().trim().length() > 0) {
         getMainLeftContainer().getFriendListContainer().action_pending_button();
         } else {
         getMainLeftContainer().getFriendListContainer().action_allfriends_button();
         //getMainLeftContainer().addFriendList();
         }*/
        getMainLeftContainer().getFriendListContainer().getAllFriendList().buildFriendList();
        //incomingReqNotification.setText("");
        resetMainButtonsColor(MENU_TYPE_FRIENDLIST);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == friendPane) {
            doFriendlistButtonClick();
        } else if (e.getSource() == chathistoryPane) {
            ChatHashMap.getInstance().getNotificationChat().clear();
            chatNotification.setText("");
            getMainLeftContainer().addChatHistoryList();
            resetMainButtonsColor(MENU_TYPE_CHAT);
            /*msg action codes here*/
        } else if (e.getSource() == callhistoryPane) {
            ChatHashMap.getInstance().getNotificationCall().clear();
            callhistoryNotification.setText("");
            getMainLeftContainer().addCallHistoryList();
            resetMainButtonsColor(MENU_TYPE_CALLHISTORY);
            /*call history action codes here*/
        } else if (e.getSource() == allNotificationPane) {
            //ChatHashMap.getInstance().getNotificationCall().clear();
            allNotification.setText("");
            getMainLeftContainer().addNotificationHistoryList();
            resetMainButtonsColor(MENU_TYPE_ALL_NOTIFICATION);
            /*all notification action codes here*/
        }
    }

//    public void action_recent() {
//        recentNotification.setText("");
//        recentNotification.revalidate();
//        recentNotification.repaint();
//        getMainLeftContainer().addRecentLeftMenus();
//        resetMainButtonsColor(MENU_TYPE_RECENT);
//    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (e.getSource() == friendPane) {
            if (!friendPane.getImage().equals(friendSelectedImg)) {
                friendPane.setImage(friendHoverImg);
            }
        } else if (e.getSource() == chathistoryPane) {
            if (!chathistoryPane.getImage().equals(chatSelectedImg)) {
                chathistoryPane.setImage(chatHoverImg);
            }
        } else if (e.getSource() == callhistoryPane) {
            if (!callhistoryPane.getImage().equals(callhistorySelectedImg)) {
                callhistoryPane.setImage(callhistoryHoverImg);
            }
        } else if (e.getSource() == allNotificationPane) {
            if (!allNotificationPane.getImage().equals(allNotificationSelectedImg)) {
                allNotificationPane.setImage(allNotificationHoverImg);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (e.getSource() == friendPane) {
            if (!friendPane.getImage().equals(friendSelectedImg)) {
                friendPane.setImage(friendImg);
            }
        } else if (e.getSource() == chathistoryPane) {
            if (!chathistoryPane.getImage().equals(chatSelectedImg)) {
                chathistoryPane.setImage(chatImg);
            }
        } else if (e.getSource() == callhistoryPane) {
            if (!callhistoryPane.getImage().equals(callhistorySelectedImg)) {
                callhistoryPane.setImage(callhistoryImg);
            }
        } else if (e.getSource() == allNotificationPane) {
            if (!allNotificationPane.getImage().equals(allNotificationSelectedImg)) {
                allNotificationPane.setImage(allNotificationImg);
            }
        }
    }

    public MainLeftContainer getMainLeftContainer() {
        return GuiRingID.getInstance().getMainLeftContainer() != null ? GuiRingID.getInstance().getMainLeftContainer() : null;
    }

    public void addChatNotification(String userId) {
        if (!chathistoryPane.getImage().equals(chatSelectedImg)) {
            if (!isInChatNotificationsList(userId)) {
                int num;
                ChatHashMap.getInstance().getNotificationChat().add(userId);
                if (chatNotification.getText().equals("")) {
                    num = 1;
                } else {
                    num = Integer.parseInt(chatNotification.getText()) + 1;
                }
                chatNotification.setText(String.valueOf(num));
                ImageHelpers.setAppIcon(GuiRingID.getInstance(), String.valueOf(num));
                chatNotification.revalidate();
            }
        }
    }

    public void clearChatNotification(String userId) {
        ChatHashMap.getInstance().getChatUnreadMessages().remove(userId);
        ChatHistoryContainer.clearNotificationLabel(userId);
    }

    boolean isInChatNotificationsList(String value) {
        for (String str : ChatHashMap.getInstance().getNotificationChat()) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }

    boolean isInCallNotificationsList(String value) {
        for (String str : ChatHashMap.getInstance().getNotificationCall()) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void addCallNotification(RecentDTO recentDTO) { //not outgoing
        if (!callhistoryPane.getImage().equals(callhistorySelectedImg) && recentDTO.getCallLog().getCallType().intValue() != 1) {
            if (!isInCallNotificationsList(recentDTO.getCallLog().getFriendIdentity())) {
                int num;
                ChatHashMap.getInstance().getNotificationCall().add(recentDTO.getCallLog().getFriendIdentity());
                if (callhistoryNotification.getText().equals("")) {
                    num = 1;
                } else {
                    num = Integer.parseInt(callhistoryNotification.getText()) + 1;
                }
                callhistoryNotification.setText(String.valueOf(num));
                ImageHelpers.setAppIcon(GuiRingID.getInstance(), String.valueOf(num));
                callhistoryNotification.revalidate();
            }
        }
    }

    public void clearCallNotification(String userId) {
        ChatHashMap.getInstance().getCallUnreadHistories().remove(userId);
        CallHistoryContainer.clearNotificationLabel(userId);
    }

    /*    public void addIncomingRequestNotification() {
     int num = 1;
     if (incomingReqNotification.getText() != null
     && incomingReqNotification.getText().trim().length() > 0) {
     num = Integer.parseInt(incomingReqNotification.getText().trim()) + 1;
     }
     incomingReqNotification.setText(num + "");
     incomingReqNotification.revalidate();
     }*/
}

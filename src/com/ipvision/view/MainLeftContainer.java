/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.JsonFields;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.service.ChangeNotificationStateRequest;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.ipvision.view.leftdata.CallHistoryContainer;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.view.leftdata.FriendListContainer;
import com.ipvision.view.leftdata.NotificationHistoryContainer;
import java.util.List;

/**
 *
 * @author Faiz
 */
public class MainLeftContainer extends JPanel {

    private FriendListContainer friendListContainer;
    private CallHistoryContainer callHistoryContainer;
    private ChatHistoryContainer chatHistoryContainer;
    public NotificationHistoryContainer notificationHistoryContainer;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MainLeftContainer.class);

    public MainLeftContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
    }

    public CallHistoryContainer getCallHistoryContainer() {
        if (callHistoryContainer == null) {
            callHistoryContainer = new CallHistoryContainer();
        }
        return callHistoryContainer;
    }

    public ChatHistoryContainer getChatHistoryContainer() {
        if (chatHistoryContainer == null) {
            chatHistoryContainer = new ChatHistoryContainer();
        }
        return chatHistoryContainer;
    }

    public NotificationHistoryContainer getNotificationHistoryContainer() {
        if (notificationHistoryContainer == null) {
            notificationHistoryContainer = new NotificationHistoryContainer();
        }
        return notificationHistoryContainer;
    }

    public FriendListContainer getFriendListContainer() {
        if (friendListContainer == null) {
            friendListContainer = new FriendListContainer();
        }
        return friendListContainer;
    }

    public void setFriendListContainer(FriendListContainer friendListContainer) {
        this.friendListContainer = friendListContainer;
    }

//    public void addInviteFriendLeftPanel() {
//        try {
//            //       GuiRingID.getInstance().getMainButtonsPanel().resetMainButtonsColor(null);
//            /*if (friendListPanel == null) {
//                friendListPanel = new ListPanel();
//            }*/
//            if (GuiRingID.getInstance().getMainRight().getComponent(0) != getFriendListContainer()) {
//                loadIntoLeftContainer(getFriendListContainer());
//            }
//            getFriendListContainer().loadInviteListData();
//
//        } catch (Exception e) {
//            log.error("Exception in initialization==>" + e.getMessage());
//        }
//    }
    /*    public void addFriendList() {
     try {
     if (GuiRingID.getInstance().getMainRight().getComponent(0) != getFriendListContainer()) {
     loadIntoLeftContainer(getFriendListContainer());
     }
     getFriendListContainer().loadFriendListData();
     } catch (Exception e) {
     log.error("Exception in initialization==>" + e.getMessage());
     }
     }*/
    public void addCallHistoryList() {
        try {
//            if (GuiRingID.getInstance().getMainRight().getComponent(0) != getFriendListContainer()) {
            loadIntoLeftContainer(getCallHistoryContainer());
//            }
            getCallHistoryContainer().loadCallListData();
        } catch (Exception e) {
            log.error("Exception in initialization==>" + e.getMessage());
        }
    }

    public void addChatHistoryList() {
        try {
//            if (GuiRingID.getInstance().getMainRight().getComponent(0) != getFriendListContainer()) {
            loadIntoLeftContainer(getChatHistoryContainer());
//            }
            getChatHistoryContainer().loadRecentChatHistory();
        } catch (Exception e) {
            log.error("Exception in initialization==>" + e.getMessage());
        }
    }

    public void addNotificationHistoryList() {
        try {
            new AddToNotifications().start();

        } catch (Exception e) {
            log.error("Exception in initialization==>" + e.getMessage());
        }
    }

    public void loadIntoLeftContainer(JPanel comp) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().loadIntoLeftContainer(comp);
        }
    }

    private class AddToNotifications extends Thread {

        @Override
        public void run() {
            try {
                List<JsonFields> notifList = null;
                if (GuiRingID.getInstance().getMainLeftContainer().notificationHistoryContainer == null) {
                    notifList = NotificationHistoryDAO.loadNotificationHistoryList(0, 15);
                    getNotificationHistoryContainer().firstRequest = true;
                }
                loadIntoLeftContainer(getNotificationHistoryContainer());
                if (getNotificationHistoryContainer().firstRequest && notifList != null && notifList.size() > 0) {
                    getNotificationHistoryContainer().addAllNotifications(notifList, notifList.size());
                }
                if (AppConstants.NOTIFICATION_MAX_UT > AppConstants.NOTIFICATION_LAST_MAX_UT) {
                    AppConstants.NOTIFICATION_LAST_MAX_UT = AppConstants.NOTIFICATION_MAX_UT;
                    new ChangeNotificationStateRequest().start();
                }
            } catch (Exception e) {
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendlist;

import com.desktopCall.dtos.CallSettingsDTo;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.call.MainClass;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.image.ImageObjects;

/**
 *
 * @author Faiz
 */
public class MiniCallChatPanel extends JPanel implements ActionListener {

    public String friendId;
    private final JButton btnCallToRingID;
    private final JButton btnVideoCall;
    final JButton btnChat;
    UserBasicInfo friendProfileInfo;

    public MiniCallChatPanel(String friendId2) {
        ImageObjects.initImageObjects();
        this.friendId = friendId2;
        friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(friendId);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        // setOpaque(false);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
//        setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR),
//                BorderFactory.createEmptyBorder(1, 0, 0, 0)));
        btnCallToRingID = DesignClasses.createImageButtonWithIcons(ImageObjects.voiceCallImage, ImageObjects.voiceCallImage_h, "Call to " + friendProfileInfo.getFullName());
        btnCallToRingID.addActionListener(this);
        btnVideoCall = DesignClasses.createImageButtonWithIcons(ImageObjects.vedioCallImage, ImageObjects.vedioCallImage_h, "Video call ");
        btnVideoCall.addActionListener(this);
        btnChat = DesignClasses.createImageButtonWithIcons(ImageObjects.chatImage, ImageObjects.chatImage_h, "Chat with " + friendProfileInfo.getFullName());
        btnChat.addActionListener(this);
        add(btnCallToRingID);
        add(btnVideoCall);
        add(btnChat);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCallToRingID) {
//            if (IncomingOutgoingUI.getIncomingOutgoingUI() == null) {
//                UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(friendId);
//                IncomingOutgoingUI incomingOutgoingUI = new IncomingOutgoingUI(false, friendProfileInfo);
//                incomingOutgoingUI.showNotification();
//            } else {
//                IncomingOutgoingUI.getIncomingOutgoingUI().showNotification();
//            }
            if (MainClass.getMainClass() == null) {
                CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                // callSettingsDTo.setDvc(friendProfileInfo.getDevice());
                callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                callSettingsDTo.setFndId(friendProfileInfo.getUserIdentity());
                callSettingsDTo.setFn(friendProfileInfo.getFullName());
                callSettingsDTo.setPrIm(friendProfileInfo.getProfileImage());
                callSettingsDTo.setIncomming(0);
                MainClass mainClass2 = new MainClass(callSettingsDTo);
                mainClass2.showWindow();
            } else {
                MainClass.getMainClass().showWindow();
            }
        } else if (e.getSource() == btnVideoCall) {
            HelperMethods.showWarningDialogMessage(NotificationMessages.VIDEO_CALL_NOTIFICATION);
        } else if (e.getSource() == btnChat) {
            MyFriendChatCallPanel friendChatCallPanel = GuiRingID.getInstance().getMainRight().getMyFriendChatCallPanel();
            if (friendChatCallPanel != null && friendChatCallPanel.friendIdentity.equalsIgnoreCase(friendId) && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                //GuiRingID.getInstance().getMainRight().myfriendSlider.next();
            } else {
                GuiRingID.getInstance().getMainRight().myfriendSlider.next();
            }
        }
    }
}

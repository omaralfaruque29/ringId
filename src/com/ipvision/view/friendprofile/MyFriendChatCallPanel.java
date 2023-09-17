/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserBasicInfo;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.ipvision.model.stores.FriendList;

/**
 *
 * @author Faiz
 */
public class MyFriendChatCallPanel extends JPanel {

    public String friendIdentity;
    public String lastChatSenderIdentity;
    public ProfileTopBar profileTopBar;
    public MyFriendChatPanel myFriendChatPanel;
    public MyfriendSlider myfriendSlider;
    private UserBasicInfo friendProfileInfo;

    public UserBasicInfo getFriendProfileInfo() {
        return this.friendProfileInfo;
    }

    public void setFriendProfileInfo(String userId) {
        if (!FriendList.getInstance().getFriend_hash_map().isEmpty() && FriendList.getInstance().getFriend_hash_map().get(userId) != null) {
            this.friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(userId);
        }
    }

    public void setFriendProfileInfo(UserBasicInfo friendProfileInfo) {
        this.friendProfileInfo = friendProfileInfo;
    }

    public MyFriendChatCallPanel(String friendIdentity, MyfriendSlider myfriendSlider) {
        this.friendIdentity = friendIdentity;
        this.myfriendSlider = myfriendSlider;
        setFriendProfileInfo(friendIdentity);
        setBackground(RingColorCode.DEFAULT_SHADED_BACKGROUND_COLOR);
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        profileTopBar = new ProfileTopBar(getThis());
        add(profileTopBar, BorderLayout.NORTH);
        myFriendChatPanel = new MyFriendChatPanel(getThis());
        add(myFriendChatPanel, BorderLayout.CENTER);
        //refreshMyFriendChatCallPanelAll();
    }

    private MyFriendChatCallPanel getThis() {
        return this;
    }

    /* *******************************************************
     * Utilities Methods
     * *******************************************************/
    public void refreshMyFriendChatCallPanelAll() {
        profileTopBar.buildFullNameAndRingID();
        profileTopBar.buildProfileImage();
        profileTopBar.buildLastOnlineStatus();
        profileTopBar.buildButtons(friendProfileInfo.getFriendShipStatus());
        myFriendChatPanel.buildChatWritingArea();
    }

    public void refreshMyFriendChatCallPanelStatus() {
        profileTopBar.buildLastOnlineStatus();
    }

    public void refreshMyFriendChatCallPanelImage() {
        profileTopBar.buildProfileImage();
    }

    public void refreshMyFriendChatCallPanelAccess() {
        profileTopBar.buildButtons(friendProfileInfo.getFriendShipStatus());
        myFriendChatPanel.buildChatWritingArea();
    }

    public void refreshMyFriendChatCallPanelFullName() {
        profileTopBar.buildFullNameAndRingID();
    }

}

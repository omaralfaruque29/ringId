/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;

/**
 *
 * @author Faiz
 */
public class FriendNameMouseListener implements MouseListener {

    Font original;
    UserBasicInfo userBasicInfo;

    public FriendNameMouseListener(String userid) {
        userBasicInfo = FriendList.getInstance().getFriend_hash_map().get(userid);
    }

    public FriendNameMouseListener(UserBasicInfo userBasicInfo) {
        this.userBasicInfo = userBasicInfo;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (userBasicInfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            e.getComponent().setFont(original);
            GuiRingID.getInstance().action_of_myProfile_button();
        } else if (FriendList.getInstance().getFriend_hash_map().get(userBasicInfo.getUserIdentity()) != null) {
            e.getComponent().setFont(original);
            GuiRingID.getInstance().showFriendProfile(userBasicInfo.getUserIdentity());
//            SingleFriendPanel singleFriendPanel = new SingleFriendPanel(userBasicInfo);
//            singleFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            //   AllFriendList.setFriendSelectionColor(userBasicInfo.getUserIdentity(), true);
        } else {
            e.getComponent().setFont(original);
            GuiRingID.getInstance().showUnknownProfile(userBasicInfo);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        original = e.getComponent().getFont();
        Map attributes = original.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        e.getComponent().setFont(original.deriveFont(attributes));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setFont(original);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}

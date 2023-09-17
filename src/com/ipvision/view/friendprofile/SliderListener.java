/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.model.constants.StatusConstants;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import static com.ipvision.view.friendprofile.MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL;
import static com.ipvision.view.friendprofile.MyfriendSlider.TYPE_MY_FRIEND_PROFILE;

/**
 *
 * @author Faiz
 */
public class SliderListener implements ActionListener {

    Component c1;
    Component c2;
    int steps;
    int step = 0;
    Timer timer;
    boolean isNext;
    MyfriendSlider myfriendSlider;

    public SliderListener(MyfriendSlider sliderPanel, int steps, Component c1, Component c2, boolean isNext) {
        this.steps = steps;
        this.c1 = c1;
        this.c2 = c2;
        this.isNext = isNext;
        this.myfriendSlider = sliderPanel;
        c2.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (c2 instanceof MyFriendChatCallPanel) {
            MyFriendChatCallPanel friendChatCallPanel = (MyFriendChatCallPanel) c2;
            friendChatCallPanel.myFriendChatPanel.hideTimerOnInit();
        }

        Rectangle bounds = c1.getBounds();
        int shift = bounds.width / steps;
        if (!isNext) {
            c1.setLocation(bounds.x - shift, bounds.y);
            c2.setLocation(bounds.x - shift + bounds.width, bounds.y);
        } else {
            c1.setLocation(bounds.x + shift, bounds.y);
            c2.setLocation(bounds.x + shift - bounds.width, bounds.y);
        }
        this.myfriendSlider.repaint();
        step++;
        if (step == steps) {
            timer.stop();
            c2.setVisible(false);
            CardLayout cl = (CardLayout) myfriendSlider.getLayout();
            if (!isNext) {
                cl.previous(this.myfriendSlider);
            } else {
                cl.next(this.myfriendSlider);
            }
            setType();
        }
    }

    public void setType() {
        if (c2 instanceof MyFriendChatCallPanel) {
            myfriendSlider.setType(TYPE_MY_FRIEND_CHAT_CALL);
            MyFriendChatCallPanel friendChatCallPanel = (MyFriendChatCallPanel) c2;
            friendChatCallPanel.myFriendChatPanel.hideTimerOnInit();
            friendChatCallPanel.myFriendChatPanel.loadInitHistory();
        } else if (c2 instanceof MyFriendProfile) {
            myfriendSlider.setType(TYPE_MY_FRIEND_PROFILE);
            MyFriendProfile friendProfile = (MyFriendProfile) c2;

            if (friendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                    && friendProfile.getFriendProfileInfo().getContactType() == StatusConstants.ACCESS_FULL) {
                if (friendProfile.myFriendNewsFeedPanel == null) {
                    friendProfile.setType(MyFriendProfile.TYPE_BOOK);
                    friendProfile.onTabClick();
                }
            } else {
                if (friendProfile.myFriendAboutPanel == null) {
                    friendProfile.setType(MyFriendProfile.TYPE_ABOUT);
                    friendProfile.onTabClick();
                }
            }
            
            MyFriendChatCallPanel friendChatCallPanel = (MyFriendChatCallPanel) c1;
            friendChatCallPanel.myFriendChatPanel.hideTimerOnInit();
        }
    }

}

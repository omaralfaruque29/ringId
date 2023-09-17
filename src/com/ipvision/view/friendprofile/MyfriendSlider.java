/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.view.friendlist.AllFriendList;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.ipvision.view.utility.SliderCardLayout;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Faiz
 */
public class MyfriendSlider extends JPanel implements AncestorListener {

    public static final int TYPE_MY_FRIEND_PROFILE = 1;
    public static final int TYPE_MY_FRIEND_CHAT_CALL = 2;
    private String friendIdentity = null;
    private int type = TYPE_MY_FRIEND_PROFILE;

    public MyfriendSlider(String friendIdentity, Integer type) {
        this.friendIdentity = friendIdentity;
        this.type = type != null ? type : TYPE_MY_FRIEND_PROFILE;
        setOpaque(false);
        addAncestorListener(this);
        setLayout(new SliderCardLayout());
    }

    public void addSliderComponent(JComponent c) {
        add(c, "" + getComponentCount());
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    public void previous() {
        SliderCardLayout cl = (SliderCardLayout) getLayout();
        Component currentComp = cl.getCurrentComponent(this);
        Component previousComp = cl.getPreviousComponent(this);
        SliderListener sl = new SliderListener(this, 10, currentComp, previousComp, false);
        Timer t = new Timer(30, sl);
        t.setInitialDelay(0);
        sl.timer = t;
        t.start();
    }

    public void next() {
        SliderCardLayout cl = (SliderCardLayout) getLayout();
        Component currentComp = cl.getCurrentComponent(this);
        Component nextComp = cl.getNextComponent(this);
        SliderListener sl = new SliderListener(this, 10, currentComp, nextComp, true);
        Timer t = new Timer(30, sl);
        t.setInitialDelay(0);
        sl.timer = t;
        t.start();
    }

    public String getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(String friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        AllFriendList.addSelection(friendIdentity);
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        AllFriendList.removeSelection();
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }

}

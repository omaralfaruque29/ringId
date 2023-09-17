/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Wasif Islam
 */
public class SinglePanelMouseListener extends MouseAdapter {

    private SingleAddFriendPanel single;
    private int type;
    //private SingleActionPanel buttonActionPanel;
    //private SingleAddFriendPanel selectedFriendPanel = null;

    public SinglePanelMouseListener(SingleAddFriendPanel single, int type) {
        this.single = single;
        this.type = type;
        this.single.buttonActionPanel.changeStatus(type);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //single.buttonActionPanel = buttonActionPanel;
        single.add(single.buttonActionPanel, BorderLayout.CENTER);
        single.friendInfoPanel.setVisible(false);
        single.buttonActionPanel.setVisible(true);
        single.revalidate();
        //addButtonActionPanel(single);
        AddFriendPanel.addButtonActionPanel(single.userIdentity);

    }

    /*private void addButtonActionPanel(SingleAddFriendPanel single) {
        if (selectedFriendPanel != null) {
            selectedFriendPanel.buttonActionPanel.setVisible(false);
            selectedFriendPanel.friendInfoPanel.setVisible(true); //off foucs
            selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        }

        if (single != null) {
            selectedFriendPanel = single;
            if (selectedFriendPanel.buttonActionPanel != null) {
                selectedFriendPanel.buttonActionPanel.setVisible(true);
            }
            selectedFriendPanel.friendInfoPanel.setVisible(false);
            selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
        }

    }*/
}

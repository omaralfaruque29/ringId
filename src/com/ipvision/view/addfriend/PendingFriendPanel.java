/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Sirat Samyoun
 */
public class PendingFriendPanel extends JPanel {

    private JPanel scrollContent;
    public Color shadowColor = new Color(0, 0, 0, 100);
    private static String INC_REQ = "Incoming Requests";
    private static String OUT_REQ = "Outgoing Requests";

    public PendingFriendPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.white);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(scrollContent, BorderLayout.NORTH);
        wrapper.add(panel);
        JScrollPane scrollPane = DesignClasses.getDefaultScrollPane(wrapper);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        //mainPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.ADD_FRIEND_CONTENT_PANEL_HEIGHT));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        add_contents();
    }

    public static boolean isIncoming(UserBasicInfo userBasicInfo) {
        if ((userBasicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING
                || (userBasicInfo.getNewContactType() != null && userBasicInfo.getNewContactType() > 0 && !userBasicInfo.getIsChangeRequester()))
                && !(userBasicInfo.getBlocked() != null && userBasicInfo.getBlocked() > 0)) {  //not blocked
            return true;
        }
        return false;
    }

    public static boolean isOutgoing(UserBasicInfo userBasicInfo) {
        if ((userBasicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_PENDING
                || (userBasicInfo.getNewContactType() != null && userBasicInfo.getNewContactType() > 0 && userBasicInfo.getIsChangeRequester()))
                && !(userBasicInfo.getBlocked() != null && userBasicInfo.getBlocked() > 0)) {  //not blocked
            return true;
        }
        return false;
    }

    public void change_access(final String userId) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userId);
                if (single != null) {
                    single.changeFriendShipStatus();
                    if (isIncoming(FriendList.getInstance().getFriend_hash_map().get(userId))) {
                        add_contents();
                    } else if (isOutgoing(FriendList.getInstance().getFriend_hash_map().get(userId))) {
                        add_contents();
                    } else {
                        scrollContent.remove(single);
                        incomingPanelTagReset();
                        outgoingPanelTagReset();
                        outgoingBorderLineReset();
                    }
                    scrollContent.revalidate();
                    scrollContent.repaint();
                } else {
                    add_contents();
                }
            }
        });
    }

    private void incomingPanelTagReset() {
        int i = 0;
        for (i = 0; i < scrollContent.getComponentCount(); i++) {
            if (scrollContent.getComponent(i).getName() != null && scrollContent.getComponent(i).getName().equals(INC_REQ)) {
                if ((i + 1) >= scrollContent.getComponentCount()
                        || !(scrollContent.getComponent(i + 1) instanceof SingleAddFriendPanel)) {
                    scrollContent.remove(i);
                    return;
                }
            }
        }
    }

    private void outgoingPanelTagReset() {
        int i = 0;
        for (i = 0; i < scrollContent.getComponentCount(); i++) {
            if (scrollContent.getComponent(i).getName() != null && scrollContent.getComponent(i).getName().equals(OUT_REQ)) {
                if ((i + 1) >= scrollContent.getComponentCount()
                        || !(scrollContent.getComponent(i + 1) instanceof SingleAddFriendPanel)) {
                    scrollContent.remove(i);
                    return;
                }
            }
        }
    }

    private void outgoingBorderLineReset() {
        int i = 0;
        for (i = 0; i < scrollContent.getComponentCount(); i++) {
            if (scrollContent.getComponent(i).getName() != null && scrollContent.getComponent(i).getName().equals(OUT_REQ)) {
                if (i == 0) {
                    return;
                }
                JPanel jp = (JPanel) scrollContent.getComponent(i - 1);
                jp.setBorder(null);
                return;
            }
        }
    }

    public void remove_content(final String userId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userId);
                if (single != null) {
                    scrollContent.remove(single);
                    MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().remove(userId);
                    incomingPanelTagReset();
                    outgoingPanelTagReset();
                    outgoingBorderLineReset();
                    scrollContent.revalidate();
                    scrollContent.repaint();
                } else {
                    add_contents();
                }
            }
        });
    }

    public void add_contents() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                scrollContent.removeAll();
                List<UserBasicInfo> listIncoming = new ArrayList<UserBasicInfo>();
                List<UserBasicInfo> listOutgoing = new ArrayList<UserBasicInfo>();
                for (UserBasicInfo u : FriendList.getInstance().getFriend_hash_map().values()) {
                    if (isIncoming(u)) {
                        listIncoming.add(u);
                    } else if (isOutgoing(u)) {
                        listOutgoing.add(u);
                    }
                }
                if (!listIncoming.isEmpty()) {
                    listIncoming = HelperMethods.sortFriendList(listIncoming);
                    scrollContent.add(DesignClasses.createlabelOvalPanel(INC_REQ));

                    for (final UserBasicInfo u : listIncoming) {
                        SingleAddFriendPanel single1 = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(u.getUserIdentity());
                        if (single1 == null) {
                            single1 = new SingleAddFriendPanel(u);
                            MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().put(u.getUserIdentity(), single1);
                        }
                        single1.friendInfoPanel.setVisible(true);
                        single1.buttonActionPanel.setVisible(false);

                        if (single1.mouseListener != null) {
                            single1.removeMouseListener(single1.mouseListener);
                        }
                        if (u.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
                            single1.mouseListener = new SinglePanelMouseListener(single1, SingleActionPanel.TYPE_INCOMING_REQUEST);
                        } else {
                            single1.mouseListener = new SinglePanelMouseListener(single1, SingleActionPanel.TYPE_INCOMING_CHANGE_ACCESS);
                        }
                        single1.addMouseListener(single1.mouseListener);
                        scrollContent.add(single1);
                    }
                }

                if (!listOutgoing.isEmpty()) {
                    listOutgoing = HelperMethods.sortFriendList(listOutgoing);
                    scrollContent.add(DesignClasses.createlabelOvalPanel(OUT_REQ));
                    for (final UserBasicInfo u : listOutgoing) {
                        SingleAddFriendPanel single2 = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(u.getUserIdentity());
                        if (single2 == null) {
                            single2 = new SingleAddFriendPanel(u);
                            MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().put(u.getUserIdentity(), single2);
                        }
                        single2.friendInfoPanel.setVisible(true);
                        single2.buttonActionPanel.setVisible(false);

                        if (single2.mouseListener != null) {
                            single2.removeMouseListener(single2.mouseListener);
                        }
                        if (u.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_PENDING) {
                            single2.mouseListener = new SinglePanelMouseListener(single2, SingleActionPanel.TYPE_OUTGOING_REQUEST);
                        } else {
                            single2.mouseListener = new SinglePanelMouseListener(single2, SingleActionPanel.TYPE_OUTGOING_CHANGE_ACCESS);
                        }
                        single2.addMouseListener(single2.mouseListener);
                        scrollContent.add(single2);
                    }
                    outgoingBorderLineReset();
                }
                scrollContent.revalidate();
                scrollContent.repaint();
            }
        });

    }
}

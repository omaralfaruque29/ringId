/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.MigrationFriendRequest;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Wasif Islam
 */
public class MigrationPanel extends JPanel {

    private JPanel scrollContent;

    public MigrationPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

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
        add(scrollPane, BorderLayout.CENTER);
        start_migration();
        add_contents();
    }

    public void start_migration() {
        new MigrationFriendRequest().start();
    }

    public void remove_content(final String userId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userId);
                if (single != null) {
                    scrollContent.remove(single);
                    MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().remove(userId);
                    scrollContent.revalidate();
                    scrollContent.repaint();
                } else {
                    add_contents();
                }
            }
        });
    }

    public void change_access(final String userId) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userId);
                if (single != null) {
                    single.changeFriendShipStatus();
                    if (!AddFriendMainPanel.isDisplayable(FriendList.getInstance().getFriend_hash_map().get(userId), SingleActionPanel.TYPE_FOR_MIGRATION)) {
                        scrollContent.remove(single);
                    }
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
                List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                for (UserBasicInfo u : FriendList.getInstance().getFriend_hash_map().values()) {
                    if (AddFriendMainPanel.isDisplayable(u, SingleActionPanel.TYPE_FOR_MIGRATION)) {
                        list.add(u);
                    }
                }
                list = HelperMethods.sortFriendList(list);

                for (final UserBasicInfo u : list) {

                    SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(u.getUserIdentity());

                    if (single == null) {
                        single = new SingleAddFriendPanel(u);
                        MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().put(u.getUserIdentity(), single);
                    }
                    if (single.mouseListener != null) {
                        single.removeMouseListener(single.mouseListener);
                    }
                    single.mouseListener = new SinglePanelMouseListener(single, SingleActionPanel.TYPE_FOR_MIGRATION);
                    single.addMouseListener(single.mouseListener);
                    single.friendInfoPanel.setVisible(true);
                    single.buttonActionPanel.setVisible(false);
                    scrollContent.add(single);
                }
                scrollContent.revalidate();
                scrollContent.repaint();
            }
        });

    }

}

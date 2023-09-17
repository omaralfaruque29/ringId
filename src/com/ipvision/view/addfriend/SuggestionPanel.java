/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.DefaultRequest;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Wasif Islam
 */
public class SuggestionPanel extends JPanel {

    private JPanel scrollContent;

    public SuggestionPanel() {
        if (FriendList.getInstance().getPeopleYouMayKnow().isEmpty()) {
            new DefaultRequest(AppConstants.TYPE_YOU_MAY_KNOW_LIST).start();
        }
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
        add_contents();
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

    public void add_contents() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        scrollContent.removeAll();
                        //List<UserBasicInfo> list = new ArrayList<UserBasicInfo>();
                        int len = FriendList.getInstance().getPeopleYouMayKnow().size();
                        if (len > 0) {
                            for (int i = 0; i < len; i++) {
                                /*UserBasicInfo entity = (UserBasicInfo) FriendList.getInstance().getPeopleYouMayKnow().get(i).getUserBasicInfo();
                                 {
                                 list.add(entity);
                                 }
                                 list = HelperMethods.sortFriendList(list);
                                 }
                                 for (final UserBasicInfo u : list) {*/
                                UserBasicInfo entity = (UserBasicInfo) FriendList.getInstance().getPeopleYouMayKnow().get(i).getUserBasicInfo();

                                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(entity.getUserIdentity());

                                if (single == null) {
                                    single = new SingleAddFriendPanel(entity);
                                    MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().put(entity.getUserIdentity(), single);
                                }
                                if (single.mouseListener != null) {
                                    single.removeMouseListener(single.mouseListener);
                                }
                                single.mouseListener = new SinglePanelMouseListener(single, SingleActionPanel.TYPE_INVITE_REQUEST);
                                single.addMouseListener(single.mouseListener);
                                single.friendInfoPanel.setVisible(true);
                                single.buttonActionPanel.setVisible(false);
                                scrollContent.add(single);
                            }

                            scrollContent.revalidate();
                            scrollContent.repaint();
                        }
                    }
                });
            }
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Wasif Islam
 */
public class SingleNotificationDialog extends JPanel {

    public Pattern reqular_expresion;
    private JScrollPane scrollPane;
    private Long stsId;
    private JPanel mainPanel;
    private JPanel contentWrapper;
    private JPanel scrollContentWrapper;
    JLabel backButton = DesignClasses.create_imageJlabel(GetImages.BACK_MINI);
    private static SingleNotificationDialog instance;
    private Map<String, SingleCommentView> commentsOfthisPost = new ConcurrentHashMap<String, SingleCommentView>();
    private JPanel previousMainRight;

    public Map<String, SingleCommentView> getCommentsOfthisPost() {
        return commentsOfthisPost;
    }

    public void setCommentsOfthisPost(Map<String, SingleCommentView> commentsOfthisPost) {
        this.commentsOfthisPost = commentsOfthisPost;
    }

    public static SingleNotificationDialog getInstance() {
        return instance;
    }

    public SingleNotificationDialog(Long status_id, JPanel prevMainRight) {
        NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().clear();
        this.stsId = status_id;
        this.instance = this;
        this.previousMainRight = prevMainRight;

        setLayout(new BorderLayout(DefaultSettings.HGAP, 0));
        setOpaque(false);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(5, 5));
        headerPanel.setBorder(new MatteBorder(1, 1, 0, 1, RingColorCode.DEFAULT_BORDER_COLOR));

        headerPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, DefaultSettings.SMALL_PANEL_HEIGHT));
        headerPanel.setOpaque(false);
        JLabel notificationLabel = DesignClasses.makeLableBold1("Feed Details", 18);
        notificationLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.add(notificationLabel, BorderLayout.WEST);
        notificationLabel.setOpaque(false);

        backButton.setBorder(new EmptyBorder(0, 0, 0, 10));
        headerPanel.add(backButton, BorderLayout.EAST);
        backButton.setToolTipText("Back");
        backButton.setOpaque(false);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GuiRingID.getInstance().loadIntoMainRightContent(previousMainRight);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
            }
        });

        // add(headerPanel, BorderLayout.NORTH);
        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentWrapper.setOpaque(false);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        contentWrapper.add(mainPanel);
        scrollContentWrapper = new JPanel();
        initContainers();
        setVisible(true);
    }

    private void initContainers() {

        try {
            scrollContentWrapper.setOpaque(false);
            scrollContentWrapper.setLayout(new BoxLayout(scrollContentWrapper, BoxLayout.Y_AXIS));

            JPanel newStatusAndstatusPanel = new JPanel(new BorderLayout(0, 5));
            newStatusAndstatusPanel.setOpaque(false);
            newStatusAndstatusPanel.add(scrollContentWrapper, BorderLayout.NORTH);

            JPanel nullPanle = new JPanel(new BorderLayout());
            nullPanle.setOpaque(false);
            nullPanle.add(newStatusAndstatusPanel, BorderLayout.CENTER);

            mainPanel.add(nullPanle, BorderLayout.CENTER);
            scrollPane = DesignClasses.getDefaultScrollPane(contentWrapper);
            scrollPane.setOpaque(false);
            scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            add(scrollPane, BorderLayout.CENTER);

            JLabel loading = DesignClasses.loadingLable(false);
            scrollContentWrapper.add(loading);
            scrollContentWrapper.revalidate();
            new AddDetailsOfNotifications(this.stsId).start();
        } catch (Exception e) {
        }
    }

    public void addSingleNotification(Long status_id, String user_id) {
        NewsFeedWithMultipleImage statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(status_id);
        if (statusDto != null) {
            scrollContentWrapper.removeAll();
            SingleStatusPanelInNotification single = new SingleStatusPanelInNotification(statusDto);
            scrollContentWrapper.add(single);
            scrollContentWrapper.revalidate();
        } else {
            scrollContentWrapper.removeAll();
            JPanel contentNotAvailablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            contentNotAvailablePanel.setOpaque(false);
            contentNotAvailablePanel.add(DesignClasses.makeJLabelFullName2("Content not found", 14));
            contentNotAvailablePanel.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
            scrollContentWrapper.add(contentNotAvailablePanel, BorderLayout.CENTER);
            scrollContentWrapper.revalidate();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
                scrollPane.getHorizontalScrollBar().setValue(0);

                scrollPane.revalidate();
                scrollPane.repaint();
            }
        });
    }

    private class AddDetailsOfNotifications extends Thread {

        Long statusID;

        public AddDetailsOfNotifications(Long status_id) {
            this.statusID = status_id;
        }

        @Override
        public void run() {
            try {
                JsonFields js = new JsonFields();
                js.setAction(AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION);
                String pakId = SendToServer.getRanDomPacketID();
                js.setPacketId(pakId);
                js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js.setNfId(this.statusID);
                SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields result = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        scrollContentWrapper.removeAll();
                        addSingleNotification(this.statusID, result.getUserIdentity());
                        scrollContentWrapper.revalidate();
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }

                scrollContentWrapper.removeAll();
                JPanel contentNotAvailablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                contentNotAvailablePanel.add(DesignClasses.makeJLabelFullName2("Content not found", 14));
                scrollContentWrapper.add(contentNotAvailablePanel, BorderLayout.CENTER);
                scrollContentWrapper.revalidate();
            } catch (Exception ed) {

                // System.out.println("ed-->" + ed.getMessage());
            }
        }
    }

    public void hideThis() {
        this.setVisible(false);
        //this.dispose();
        instance = null;
        NewsFeedMaps.getInstance().getSingleStatusPanelInNotification().clear();
        getCommentsOfthisPost().clear();
        System.gc();
        Runtime.getRuntime().gc();
    }
//    public void editMyPost(NewsFeedWithMultipleImage status) {
//        scrollContentWrapper.removeAll();
//        SingleStatusPanelInNotification single = new SingleStatusPanelInNotification(status);
//        scrollContentWrapper.add(single);
//        scrollContentWrapper.revalidate();
//        scrollContentWrapper.repaint();
//    }

    public JPanel getContent() {
        return scrollContentWrapper;
    }
}

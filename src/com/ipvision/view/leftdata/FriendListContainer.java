/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.leftdata;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.dao.InsertIntoContactListTable;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.utility.NotificationCounterRedLabel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.view.friendlist.FriendSearchPanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.ImagePane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Faiz Ahmed
 */
public class FriendListContainer extends JPanel {

    public JPanel friendListShade;
    public JPanel friendSearchWrapperPanel;
    public JPanel friendListWrapperPanel;
    public FriendSearchPanel friendSearchPanel;
    public AllFriendList allFriendList;
    public JPanel bottomPanel;
    public ImagePane addStickerPane;
    JPanel txtWrapper;
    public JLabel addFriendLbl1;
    public JLabel addFriendLbl2;
    public JLabel homeLabel;
    public BufferedImage addStickerImage = null, addStickerH = null, addStickerSelected = null;
    private final NotificationCounterRedLabel incomingReqNotification = new NotificationCounterRedLabel();

    public NotificationCounterRedLabel getIncomingReqNotification() {
        return incomingReqNotification;
    }

    public AllFriendList getAllFriendList() {
        if (allFriendList == null) {
            allFriendList = new AllFriendList();
        }
        return allFriendList;
    }

    public FriendSearchPanel getFriendSearchPanel() {
        return friendSearchPanel;
    }

    public FriendListContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setOpaque(false);
        friendSearchWrapperPanel = new JPanel();
        friendSearchWrapperPanel.setLayout(new BorderLayout());
        friendSearchWrapperPanel.setOpaque(false);
        friendSearchPanel = new FriendSearchPanel(this);
        friendSearchWrapperPanel.add(friendSearchPanel, BorderLayout.CENTER);
        add(friendSearchWrapperPanel, BorderLayout.NORTH);
        friendSearchWrapperPanel.revalidate();
        friendSearchWrapperPanel.repaint();

        JPanel wrapperPanel = new JPanel(new CardLayout());
        wrapperPanel.setOpaque(false);

        friendListShade = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(MyFnFSettings.LOGIN_SESSIONID != null
                        && MyFnFSettings.LOGIN_SESSIONID.length() > 0
                        && InternetUnavailablityCheck.isInternetAvailable
                                ? RingColorCode.RING_FRIEND_LIST_ONLINE_BG
                                : RingColorCode.RING_FRIEND_LIST_OFFLINE_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        friendListShade.setOpaque(false);
        wrapperPanel.add("FRIEND_LIST_SHADE", friendListShade);

        JPanel friendListPanel = new JPanel(new BorderLayout());
        friendListPanel.setOpaque(false);

        friendListWrapperPanel = new JPanel();
        friendListWrapperPanel.setLayout(new BoxLayout(friendListWrapperPanel, BoxLayout.Y_AXIS));
        friendListWrapperPanel.setOpaque(false);
        friendListPanel.add(friendListWrapperPanel, BorderLayout.NORTH);

        wrapperPanel.add("FRIEND_LIST", friendListPanel);
        friendListShade.setVisible(true);
        friendListPanel.setVisible(true);

        JScrollPane freindlistScorllPanel = DesignClasses.getDefaultScrollPaneThin(wrapperPanel);
        add(freindlistScorllPanel, BorderLayout.CENTER);

        addFriendPanel();

    }

    public void addIncomingRequestNotification() {
        int num = 1;
        if (incomingReqNotification.getText() != null
                && incomingReqNotification.getText().trim().length() > 0) {
            num = Integer.parseInt(incomingReqNotification.getText().trim()) + 1;
        }
        incomingReqNotification.setText(num + "");
        incomingReqNotification.revalidate();
    }

    public void initIncomingRequestNotification() {
        incomingReqNotification.setText("");
        incomingReqNotification.revalidate();
        int count = 0;
        for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
            if (basicInfo.getIncomingNotification()) {
                count++;
            }
        }
        if (count > 0) {
            incomingReqNotification.setText(count + "");
            incomingReqNotification.revalidate();
        }
    }

    private void addFriendPanel() {

        try {
            addStickerImage = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_FRIEND_STICKER));
            addStickerH = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_FRIEND_STICKER_H));
            addStickerSelected = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_FRIEND_STICKER_SELECTED));
        } catch (Exception e) {
        }

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());//(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);

        addStickerPane = new ImagePane();
        addStickerPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        addStickerPane.setBorder(new EmptyBorder(0, 0, 0, 30));
        addStickerPane.setOpaque(false);
        addStickerPane.setImage(addStickerImage);
        addStickerPane.addMouseListener(mouseListener);
        addStickerPane.setRounded(false);

        bottomPanel.add(addStickerPane, BorderLayout.WEST);

        addFriendLbl1 = new JLabel("+ Add");
        addFriendLbl1.setForeground(Color.BLACK);
        addFriendLbl2 = new JLabel("Friends");
        addFriendLbl2.setForeground(Color.BLACK);
        homeLabel = new JLabel(" " + "Home");

        homeLabel.setForeground(Color.BLACK);

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BorderLayout(0, 0));

        JPanel notifWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        notifWrapper.setOpaque(false);
        notifWrapper.setPreferredSize(new Dimension(50, 35));
        notifWrapper.add(incomingReqNotification);

        container.add(notifWrapper, BorderLayout.NORTH);

        txtWrapper = new JPanel();
        txtWrapper.setOpaque(false);
        txtWrapper.setBorder(new EmptyBorder(0, 5, 0, 0));
        txtWrapper.setLayout(new BoxLayout(txtWrapper, BoxLayout.Y_AXIS));
        txtWrapper.add(addFriendLbl1);
        txtWrapper.add(addFriendLbl2);
        container.add(txtWrapper, BorderLayout.CENTER);
        container.setPreferredSize(new Dimension(50, 70));
        addStickerPane.add(container);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addFriendIconClickedAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (addFriendLbl1.isVisible() && addFriendLbl2.isVisible()) {
                    GuiRingID.getInstance().loadIntoMainRightContent(GuiRingID.getInstance().getAddFriendMainPanel());
                } else {
                    GuiRingID.getInstance().getMainButtons().action_of_book_button();
                }

                if (incomingReqNotification.getText() != null
                        && incomingReqNotification.getText().trim().length() > 0) {
                    if (Integer.parseInt(incomingReqNotification.getText().trim()) > 0) {
                        incomingReqNotification.setText("");
                        incomingReqNotification.revalidate();
                        GuiRingID.getInstance().getAddFriendMainPanel().action_of_pending_tab_panel();

                        List<UserBasicInfo> contactList = new ArrayList<UserBasicInfo>();
                        for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
                            if (basicInfo.getIncomingNotification()) {
                                basicInfo.setIncomingNotification(Boolean.FALSE);
                                contactList.add(basicInfo);
                            }
                        }
                        if (!contactList.isEmpty()) {
                            (new InsertIntoContactListTable(contactList)).start();
                        }
                    } else {
                        GuiRingID.getInstance().getAddFriendMainPanel().action_of_search_tab_panel();
                    }
                } else {
                    GuiRingID.getInstance().getAddFriendMainPanel().action_of_search_tab_panel();
                }
            }
        });
    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            addFriendIconClickedAction();
            addStickerPane.setImage(addStickerSelected);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            addStickerPane.setImage(addStickerSelected);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (homeLabel.isVisible()) {
                addStickerPane.setImage(addStickerImage);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (addStickerPane.getImage() != addStickerSelected) {
                addStickerPane.setImage(addStickerH);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (addStickerPane.getImage() != addStickerSelected) {
                addStickerPane.setImage(addStickerImage);
            }
        }
    };

    public void setDefaultStickerImage() {
        addStickerPane.setImage(addStickerImage);
        addFriendLbl1.setForeground(Color.BLACK);
        addFriendLbl2.setForeground(Color.BLACK);
    }

    public void setHome(boolean set) {
        if (set) {

            txtWrapper.add(homeLabel);
            homeLabel.setVisible(true);
            addFriendLbl1.setVisible(false);
            addFriendLbl2.setVisible(false);

        } else {
            addFriendLbl1.setVisible(true);
            addFriendLbl2.setVisible(true);
            homeLabel.setVisible(false);
        }

    }

}

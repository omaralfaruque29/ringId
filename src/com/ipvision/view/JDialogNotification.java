/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.NotificationRequest;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.JsonFields;
//import static org.ipvision.MenuBarTop.incomingReqImg;

/**
 *
 * @author Faiz Ahmed
 */
public class JDialogNotification extends JDialog {

//    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JDialogNotification.class);
//    public static final int POPUP_ALL_NOTIFICATION = 1;
//    public static final int POPUP_INCOMING_REQUEST_NOTIFICATION = 2;
//    private JPanel contentPane;
//    private int width = 240;
//    private int popuptype;// barText;
//    private JLabel seeAllNotification;
//    public static int unReadChatSize = 0;
//    private JPanel friendListPanel;
//    public JLabel loading;
//    public boolean isLoading;
//    private JScrollPane scrollPane;
//    public JPanel notificationContainerPanel;
//    private Color borderColor = new Color(0xffe4c4);
//    private Image img = DesignClasses.return_image(GetImages.NOTIFICATION_DIALOG_BG).getImage();
//    private int limit = 0;
//    private int POPUP_WIDTH = 305;// + 25;
//    private int POPUP_HEIGHT = 382;// + 31;
//    private BufferedImage allNotificationImg = null;
//
//    public JDialogNotification(JComponent com, int location, int popuptype) {
//        this.popuptype = popuptype;
//        setUndecorated(true);
//        setResizable(false);
//        setLayout(new BorderLayout());
//        setBackground(new Color(0, 0, 0, 0));
//        setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
//        setMaximumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
//        initComponent(com, location, popuptype);
//
//        int location_x = (int) com.getLocationOnScreen().getX() - location - 27;
//        int location_y = (int) com.getLocationOnScreen().getY() + 16;
//        setLocation(location_x, location_y);
//    }
//
//    /*    public JDialogNotification(JComponent com, int location, int popuptype, int totalNotification, int height) {
//     this.popuptype = popuptype;
//     setUndecorated(true);
//     setResizable(false);
//     setLayout(new BorderLayout());
//     setBackground(new Color(0, 0, 0, 0));
//     setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
//     setMaximumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
//     initComponent(com, location, popuptype);
//    
//     int location_x = (int) com.getLocationOnScreen().getX() - location - 18;
//     int location_y = (int) com.getLocationOnScreen().getY() + 23;
//     setLocation(location_x, location_y);
//     }*/
//    /*        setMinimumSize(new Dimension(285, 361)); // 260,330
//     setMaximumSize(new Dimension(285, 361));*/
//    public void initComponent(JComponent com, int location, int popuptype) {
//        JPanel wrapperPanel = new ImagePanel();
//        wrapperPanel.setOpaque(false);
//        wrapperPanel.setLayout(new BorderLayout());
//        //wrapperPanel.setBorder(new EmptyBorder(12, 10, 5, 10));
//        wrapperPanel.setBorder(new EmptyBorder(29, 27, 28, 26));
//        setContentPane(wrapperPanel);
//
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowDeactivated(WindowEvent e) {
//                hideThis();
//            }
//        });
//
//        contentPane = new JPanel(new BorderLayout());
//        contentPane.setBorder(new EmptyBorder(5, 6, 3, 4));
//        contentPane.setOpaque(false);
//        //contentPane.setBackground(Color.RED);
//        wrapperPanel.add(contentPane, BorderLayout.CENTER);
//
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));//new ImagePanel(DesignClasses.return_image(GetImages.NOTIFICATION_ARROW).getImage());
//        topPanel.setOpaque(false);
//        topPanel.setPreferredSize(new Dimension(0, 25));
//
//        JLabel textLabel = new JLabel();
//        textLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
//        topPanel.add(textLabel, BorderLayout.PAGE_END);
//        contentPane.add(topPanel, BorderLayout.NORTH);
//
//        friendListPanel = new JPanel(new BorderLayout());
//        friendListPanel.setBackground(Color.WHITE);
//        notificationContainerPanel = new JPanel();
//        friendListPanel.add(notificationContainerPanel, BorderLayout.NORTH);
//        notificationContainerPanel.setLayout(new BoxLayout(notificationContainerPanel, BoxLayout.Y_AXIS));
//        notificationContainerPanel.setOpaque(false);
//        scrollPane = DesignClasses.getDefaultScrollPaneThin(friendListPanel);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        contentPane.add(scrollPane, BorderLayout.CENTER);
//        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));//new ImagePanel(DesignClasses.return_image(GetImages.NOTIFICATION_ARROW).getImage());
//        bottomPanel.setOpaque(false);
//        bottomPanel.setPreferredSize(new Dimension(0, 20));
//        contentPane.add(bottomPanel, BorderLayout.SOUTH);
//
//        seeAllNotification = new JLabel();
//        seeAllNotification.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
//        seeAllNotification.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        if (this.popuptype == POPUP_ALL_NOTIFICATION) {
//            textLabel.setText("Notifications");
//            seeAllNotification.setText("See All");
//            bottomPanel.add(seeAllNotification);
//            seeAllNotification.addMouseListener(mouse_listner);
//        } else if (this.popuptype == POPUP_INCOMING_REQUEST_NOTIFICATION) {
//            textLabel.setText("Incoming Request");
//        }
//        try {
//            allNotificationImg = ImageIO.read(new Object() {
//            }.getClass().getClassLoader().getResource(GetImages.ALL_NOTIFICATION));
//        } catch (Exception e) {
//        }
//
//        addloding();
//    }
//
//    private void addloding() {
//        loading = DesignClasses.loadingLable(true);
//        notificationContainerPanel.add(loading);
//        notificationContainerPanel.revalidate();
//    }
//
//    public void removeAllcontent() {
//        notificationContainerPanel.removeAll();
//        notificationContainerPanel.revalidate();
//    }
//
//    public void removeLoadingcontent() {
//        notificationContainerPanel.remove(loading);
//        notificationContainerPanel.revalidate();
//    }
//
//    public void addMoreContetns(Map<String, JsonFields> moreNotifMap) {
//        notificationContainerPanel.remove(loading);
//        //notificationContainerPanel.add(Box.createRigidArea(new Dimension(0, 2)));
//        List<JsonFields> finalList = new ArrayList<JsonFields>();
//
//        for (Map.Entry<String, JsonFields> entity : moreNotifMap.entrySet()) {
//            JsonFields js = entity.getValue();
//            finalList.add(js);
//        }
//
//        Collections.sort(finalList, new Comparator<JsonFields>() {
//            @Override
//            public int compare(JsonFields one, JsonFields other) {
//                Long onesTime = one.getUt();
//                Long othersTime = other.getUt();
//                return othersTime.compareTo(onesTime);
//            }
//        });
//
//        for (JsonFields js : finalList) {
//            SingleNotification single = new SingleNotification(js, this);
//            notificationContainerPanel.add(single);
//        }
//        notificationContainerPanel.revalidate();
//    }
//
//    public void addAllNotifications(List<JsonFields> finalList, final int notifCount, boolean firstTime) {
//        //removeAllcontent();
//        removeLoadingcontent();
//        isLoading = false;
//        if (firstTime) {
//            //  notificationContainerPanel.add(Box.createRigidArea(new Dimension(0, 2)));
//        }
//        Collections.sort(finalList, new Comparator<JsonFields>() {
//            @Override
//            public int compare(JsonFields one, JsonFields other) {
//                Long onesTime = one.getUt();
//                Long othersTime = other.getUt();
//                return othersTime.compareTo(onesTime);
//            }
//        });
//
//        int index = 1;
//        for (JsonFields js : finalList) {
//            SingleNotification single = new SingleNotification(js);
//            if (firstTime && index == 1) {
//                //single.setBorder(new MatteBorder(1, 0, 1, 0, borderColor));
//            }
//            notificationContainerPanel.add(single);
//            index++;
//        }
//
//        notificationContainerPanel.revalidate();
//        notificationContainerPanel.repaint();
//
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                int containerHeight = friendListPanel.getHeight();
//                final int topHeight = e.getValue();
//                int barHeight = scrollPane.getVerticalScrollBar().getHeight();
//                int bottomHeight = containerHeight - (topHeight + barHeight);
//                int totalCalHeight = (topHeight + barHeight + bottomHeight);
//                if (bottomHeight <= 0 && isLoading == false) {
//                    isLoading = true;
//                    addloding();
//                    final List<JsonFields> list = NotificationHistoryDAO.loadNotificationHistoryList(notifCount, 3);
//                    if (list != null && list.size() > 0) {
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                addAllNotifications(list, notifCount + 3, false);
//                                SwingUtilities.invokeLater(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        scrollPane.getVerticalScrollBar().setValue(topHeight);
//                                    }
//                                });
//                            }
//                        });
//                    } else {
//                        NotificationHistoryDAO.loadMinTimeFromNotificationHistory();
//                        new NotificationRequest(AppConstants.NOTIFICATION_MIN_UT, (short) 2).start();
//                    }
//                }
//            }
//        });
//
//    }
//
//    /*    public void addIncomingRequestNotifications() {
//     removeAllcontent();
//     notificationContainerPanel.add(Box.createRigidArea(new Dimension(0, 2)));
//     List<UserBasicInfo> tempList = new ArrayList<>();
//    
//     int index = 1;
//     for (UserBasicInfo basicInfo : FriendList.getInstance().getFriend_hash_map().values()) {
//     if (basicInfo.getFriendShipStatus() != null && basicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
//    
//     if (basicInfo.getIncomingNotification() != null && basicInfo.getIncomingNotification()) {
//     basicInfo.setIncomingNotification(false);
//     tempList.add(basicInfo);
//     }
//    
//     SingleIncomingRequestNotification single = new SingleIncomingRequestNotification(basicInfo.getUserIdentity(), this);
//     if (index == 1) {
//     single.setBorder(new MatteBorder(1, 0, 1, 0, borderColor));
//     }
//    
//     notificationContainerPanel.add(single);
//     index++;
//     }
//     }
//    
//     if (tempList.size() > 0) {
//     new InsertIntoContactListTable(tempList).start();
//     }
//    
//     notificationContainerPanel.revalidate();
//     notificationContainerPanel.repaint();
//     }*/
//    MouseListener mouse_listner = new MouseAdapter() {
//        Font original;
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//            original = e.getComponent().getFont();
//            Map attributes = original.getAttributes();
//            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
//            e.getComponent().setFont(original.deriveFont(attributes));
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//            e.getComponent().setFont(original);
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent arg0) {
//            if (arg0.getComponent() == seeAllNotification) {
//                hideThis();
//                javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        // NotificationHistoryDAO.loadNotificationHistoryList(0, -1);
//                        GuiRingID.getInstance().getTopMenuBar().action_of_all_notifications();
//                    }
//                });
//            }
//        }
//    };
//
////    public void hideThis() {
////        if (popuptype == POPUP_ALL_NOTIFICATION) {
////            if (GuiRingID.getInstance() != null
////                    && GuiRingID.getInstance().getTopMenuBar() != null
////                    && GuiRingID.getInstance().getTopMenuBar().allNotificationPane != null
////                    && GuiRingID.getInstance().getTopMenuBar().allNotificationPane.getCursor().getType() != Cursor.HAND_CURSOR) {
////                GuiRingID.getInstance().getTopMenuBar().needToShowAllNotification = true;
////                if (allNotificationImg != null) {
////                    GuiRingID.getInstance().getTopMenuBar().allNotificationPane.setImage(allNotificationImg);
////                }
////            }
////        }
////        /*        else if (popuptype == POPUP_INCOMING_REQUEST_NOTIFICATION) {
////         if (GuiRingID.getInstance().getTopMenuBar().incomingReqPane.getCursor().getType() != Cursor.HAND_CURSOR) {
////         GuiRingID.getInstance().getTopMenuBar().needToShowIncomingRequestNotification = true;
////         GuiRingID.getInstance().getTopMenuBar().incomingReqPane.setImage(incomingReqImg);
////         }
////         }*/
////        setVisible(false);
////    }
//
////    public class ImagePanel extends JPanel {
////
////        public ImagePanel() {
////            setOpaque(false);
////        }
////
////        @Override
////        protected void paintComponent(Graphics g) {
////            super.paintComponent(g);
////            g.drawImage(img, 0, 0, null);
////        }
////    }

}

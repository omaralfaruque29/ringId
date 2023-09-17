/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_WEEK;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import com.ipvision.model.dao.InsertIntoNotificationHistoryTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.SingleNotification;
import com.ipvision.service.DeleteNotificationRequest;
import com.ipvision.service.aboutme.UnknownProfileInfoRequest;
import com.ipvision.view.utility.call.CallUIHelpers;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.NotificationRequest;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundPanel;
import com.ipvision.view.image.SingleImageDetailsForNotifications;

/**
 *
 * @author Shahadat Hossain
 */
public class AllNotificationPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AllNotificationPanel.class);
    private SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
    public JPanel content = new JPanel();
    public static AllNotificationPanel allNotificationPanel;
    private JScrollPane scrollPane;
    public JLabel loading = DesignClasses.loadingLable(true);
    //public boolean isLoading;
    private JPanel scrollContent;
    private JPanel pnlSeeMore;
    private JLabel lblSeeMore;
    private JPanel loadingContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private String compDate = null;
    private String currDate = null;
    private int lastDate = 0;
    private int contentToDlt = 0;
    private JButton btnDltNotification;
    private SingleNotification singleNotification;
    private int totalPanel = 0;

    public static AllNotificationPanel getInstance() {
        if (allNotificationPanel == null) {
            allNotificationPanel = new AllNotificationPanel();
        }
        return allNotificationPanel;
    }

    public AllNotificationPanel() {
        setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP_5));
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        //setBackground(Color.WHITE);
        initContainers();
    }

    private void initContainers() {
        try {

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BorderLayout(5, 5));
            headerPanel.setPreferredSize(new Dimension(0, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + 1));
            headerPanel.setBackground(RingColorCode.SECOND_BAR_BG_COLOR);
            //headerPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
            //headerPanel.setBorder(DefaultSettings.DEFAULT_BORDER);
            headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));

            JLabel createGroup = DesignClasses.makeLableBold1("Notifications", 18);
            createGroup.setBorder(new EmptyBorder(0, 10, 0, 0));
            headerPanel.add(createGroup, BorderLayout.WEST);
            add(headerPanel, BorderLayout.NORTH);

            content.setOpaque(false);
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            JPanel notificationsContainer = new JPanel(new BorderLayout(0, 5));
            notificationsContainer.setOpaque(false);
            notificationsContainer.add(content, BorderLayout.NORTH);

            JPanel nullPanle = new JPanel(new FlowLayout(FlowLayout.CENTER));
            nullPanle.setOpaque(false);
            nullPanle.setBorder(new EmptyBorder(0, 45, 5, 45));
            //nullPanle.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
            //nullPanle.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
            nullPanle.add(notificationsContainer);

            scrollContent = new JPanel(new BorderLayout());
            scrollContent.setOpaque(false);
            scrollContent.setBorder(new EmptyBorder(0, 10, 5, 15));
            //scrollContent.setBackground(Color.WHITE);
            scrollContent.add(nullPanle, BorderLayout.CENTER);

            scrollPane = DesignClasses.getDefaultScrollPane(scrollContent);
            add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
        }
    }

//    public void addSingleNotification(JsonFields js) {
//        //GuiRingID.getInstance().getTopMenuBar().action_of_all_notifications();
//        addAllNotifications();
////        content.add(buildSingleNotification(js), 0);
////        //content.add(Box.createRigidArea(new Dimension(0, 2)));
////        content.revalidate();
////        content.repaint();
//    }
    public synchronized void addAllNotifications() {
        totalPanel = 0;
        List<JsonFields> finalList = new ArrayList<JsonFields>();
        //NotificationHistoryDAO.loadNotificationHistoryList(0, -1);   //this line needed if allnotificationpanel is required again

        content.removeAll();
        for (Map.Entry<String, JsonFields> entity : MyfnfHashMaps.getInstance().getNotificationsMap().entrySet()) {
            JsonFields js = entity.getValue();
            //js.setIds(js.getId());
            //js.setFriendIds(js.getFriendId());
            finalList.add(js);

        }

        Collections.sort(finalList, new Comparator<JsonFields>() {
            @Override
            public int compare(JsonFields one, JsonFields other) {
                Long onesTime = one.getUt();
                Long othersTime = other.getUt();
                return othersTime.compareTo(onesTime);
            }
        });

        for (JsonFields js : finalList) {
            filterNotifications(js);
        }
        addlblMoreNotification();
        content.revalidate();
        content.repaint();

        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
         scrollPane.getVerticalScrollBar().setValue(0);
         scrollPane.getHorizontalScrollBar().setValue(0);
         scrollPane.revalidate();
         scrollPane.repaint();
         }
         });*/
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                int containerHeight = scrollContent.getHeight();
//                int topHeight = e.getValue();
//                int barHeight = scrollPane.getVerticalScrollBar().getHeight();
//                int bottomHeight = containerHeight - (topHeight + barHeight);
//                int totalCalHeight = (topHeight + barHeight + bottomHeight);
//                if (bottomHeight <= 0 && isLoading == false && barHeight > 0) {
//                    isLoading = true;
//                    addlblMoreNotification();
//                    NotificationHistoryDAO.loadMinTimeFromNotificationHistory();
//                    new GetNotification(AppConstants.NOTIFICATION_MIN_UT, (short) 2).start();
//                }
//
//            }
//        });
    }

    private void filterNotifications(JsonFields js) {
        getCurrentDate(js);
        if (currDate.equalsIgnoreCase(compDate)) {
            content.add(buildSingleNotification(js));
            totalPanel = totalPanel + 1;
        } else {
            compDate = currDate;
            addGroup(js);
            content.add(buildSingleNotification(js));
            totalPanel = totalPanel + 1;
        }
    }

    private String getCurrentDate(JsonFields js) {
        Calendar calendar = new GregorianCalendar();
        if (js.getUt() != null && js.getUt() > 0) {
            calendar.setTimeInMillis(js.getUt());
        }
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return currDate = year + month + day;
    }

    private void addGroup(JsonFields js) {
        SimpleDateFormat ft = new SimpleDateFormat("dd MMMM yyyy");
        String group = ft.format(new Date(js.getUt()));

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(js.getUt());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        GregorianCalendar groupDay = new GregorianCalendar(year, month, day);
        String[] weekdays = new DateFormatSymbols().getWeekdays();
        String nameOfDay = weekdays[groupDay.get(DAY_OF_WEEK)];

        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        groupPanel.setBackground(RingColorCode.ALLNOTIFICATION_GROUP_PANEL_COLOR);
        groupPanel.setPreferredSize(new Dimension(0, 40));
        JLabel groupLabel = makeJLabel("" + group + ", " + nameOfDay, Font.BOLD, 13);
        groupPanel.add(groupLabel);
        content.add(groupPanel);
        content.revalidate();
    }

    public static JLabel makeJLabel(String text, int style, int font_size) {
        JLabel lblGroupName = new JLabel(text);
        lblGroupName.setOpaque(false);
        lblGroupName.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        //  lblGroupName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        try {
            lblGroupName.setFont(new Font(text, style, font_size));
            //jlble.setFont(getDefaultFont(Font.BOLD, font_size));
        } catch (Exception e) {
        }
        return lblGroupName;
    }

    private void addlblMoreNotification() {
        pnlSeeMore = new JPanel();
        pnlSeeMore.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlSeeMore.setBackground(RingColorCode.ALLNOTIFICATION_GROUP_PANEL_COLOR);
        pnlSeeMore.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        pnlSeeMore.setPreferredSize(new Dimension(0, 40));
        lblSeeMore = makeJLabel("See more...", Font.BOLD, 13);
        pnlSeeMore.add(lblSeeMore);
        mouseLiseraction(pnlSeeMore);
        content.add(pnlSeeMore);
        content.revalidate();

    }

    public void removelblMoreNotification() {
        content.remove(pnlSeeMore);
        content.revalidate();
    }

    public void addLoading() {
        loadingContainer.add(loading);
        content.add(loadingContainer);
        content.revalidate();
    }

    public void removeLoading() {
        content.remove(loadingContainer);
        content.revalidate();
    }

//    public void addMoreContetns(Map<String, JsonFields> moreNotifMap) {
//
////        content.removeAll();
////        content.add(Box.createRigidArea(new Dimension(0, 2)));
//
////        //content.add(Box.createRigidArea(new Dimension(0, 2)));
////        List<JsonFields> finalList = new ArrayList<JsonFields>();
////
////        for (Map.Entry<String, JsonFields> entity : moreNotifMap.entrySet()) {
////            JsonFields js = entity.getValue();
////            finalList.add(js);
////        }
////
////        Collections.sort(finalList, new Comparator<JsonFields>() {
////            @Override
////            public int compare(JsonFields one, JsonFields other) {
////                Long onesTime = one.getUt();
////                Long othersTime = other.getUt();
////                return othersTime.compareTo(onesTime);
////            }
////        });
////        
////        
////
////        for (JsonFields js : finalList) {
////            getCurrentDate(js);
////            if (currDate == lastDate) {
////                content.add(buildSingleNotification(js));
////                content.add(Box.createRigidArea(new Dimension(0, 2)));
////            } else {
////                filterNotifications(js);
////            }
////            lastDate = getCurrentDate(js);
////            content.add(buildSingleNotification(js));
////            content.add(Box.createRigidArea(new Dimension(0, 2)));
////        }
//        content.revalidate();
//        content.repaint();
//    }
    public JPanel buildSingleNotification(final JsonFields js) {

        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = new HTMLDocument();
        final Color bgColor;

        if (js.getIsRead()) {
            bgColor = RingColorCode.NOTIFICATION_SINGLE_SEEN_PANEL_COLOR;
        } else {
            bgColor = RingColorCode.NOTIFICATION_SINGLE_UNSEEN_PANEL_COLOR;
        }
        final JPanel singleNotification = new JPanel();
        singleNotification.setLayout(new BorderLayout(0, 0));
        //singleNotification.setPreferredSize(new Dimension(475, 40));
        singleNotification.setBackground(bgColor);
        singleNotification.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.ALLNOTIFICATION_GROUP_PANEL_COLOR));
        singleNotification.setName(js.getId());

        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(35, 40));
        singleNotification.add(iconPanel, BorderLayout.WEST);

        JPanel messageAndTimePanel = new JPanel(new BorderLayout(0, 0));
        messageAndTimePanel.setBorder(new EmptyBorder(3, 0, 0, 0));
        messageAndTimePanel.setOpaque(false);
        singleNotification.add(messageAndTimePanel, BorderLayout.CENTER);

        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        wrapPanel.setOpaque(false);

        final JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(5, 0, 5, 5));
        messagePanel.setOpaque(false);
        messageAndTimePanel.add(messagePanel, BorderLayout.WEST);
        messageAndTimePanel.add(wrapPanel, BorderLayout.EAST);

        final JTextPane message = new JTextPane();
        message.setEditable(false);
        message.setContentType("text/html");
        message.setOpaque(false);
        message.setEditorKit(kit);
        message.setDocument(doc);
        messagePanel.add(message, BorderLayout.CENTER);

        final JButton btnDltNotification = DesignClasses.createImageButton(GetImages.NOTIFICATION_DELETE, GetImages.NOTIFICATION_DELETE_H, "Delete");
        btnDltNotification.setToolTipText("Delete");
        btnDltNotification.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDltNotification.setVisible(false);
        JPanel pnlDltNotification = new JPanel();
        pnlDltNotification.setPreferredSize(new Dimension(16, 20));
        pnlDltNotification.setBorder(new EmptyBorder(0, 0, 0, 3));
        pnlDltNotification.setOpaque(false);
        pnlDltNotification.add(btnDltNotification);
        btnDltNotification.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                singleNotification.setBackground(RingColorCode.NOTIFICATION_SINGLE_HOVER_COLOR);
                btnDltNotification.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDltNotification.setVisible(false);
                singleNotification.setBackground(bgColor);
            }

        });

        btnDltNotification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelperMethods.showConfirmationDialogMessage("Want to delete this notification?");
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteNotificationRequest(js.getId()).start();
                } else {
                    btnDltNotification.setVisible(false);
                }
            }
        });
        JPanel timePanel = new JPanel();
        timePanel.setOpaque(false);
        timePanel.setBorder(new EmptyBorder(7, 0, 0, 7));
        wrapPanel.add(timePanel);
        wrapPanel.add(pnlDltNotification);

        JLabel timeLabel = DesignClasses.makeJLabelUnderFullName("");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        timePanel.add(timeLabel);

        iconPanel.add(changeNotificationImage(js.getMt()));
        this.addMessage(js, messagePanel, kit, doc);
        timeLabel.setText(js.getUt() + "");
        if (js.getUt() != null) {
            timeLabel.setText(FeedUtils.getShowableTimeNotification(js.getUt()));
            timeLabel.setToolTipText(FeedUtils.getActualDate(js.getUt()));
        } else {
            timeLabel.setText(FeedUtils.getShowableTimeNotification(System.currentTimeMillis()));
            timeLabel.setToolTipText(FeedUtils.getActualDate(System.currentTimeMillis()));
        }
        mouseLiseraction(singleNotification, singleNotification, js, btnDltNotification, bgColor);
        mouseLiseraction(message, singleNotification, js, btnDltNotification, bgColor);
        mouseLiseraction(timeLabel, singleNotification, js, btnDltNotification, bgColor);
        //singleNotification.setName(js.getId());
        return singleNotification;
    }

    public JLabel changeNotificationImage(int type) {
        JLabel iconLabel = new JLabel();
        iconLabel.setOpaque(false);
        try {
            if (type == StatusConstants.MESSAGE_ADD_FRIEND) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ADD_FRIEND);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ACCEPT_FRIEND || type == StatusConstants.MESSAGE_NOW_FRIEND) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ACCEPT_FRIEND);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ACCEPT_FRIEND_ACCESS);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_UPGRADE_FRIEND_ACCESS);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_DOWNGRADE_FRIEND_ACCESS);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_LIKE_COMMENT || type == StatusConstants.MESSAGE_LIKE_IMAGE || type == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT || type == StatusConstants.MESSAGE_LIKE_STATUS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_LIKE);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT || type == StatusConstants.MESSAGE_ADD_STATUS_COMMENT || type == StatusConstants.MESSAGE_IMAGE_COMMENT) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_COMMENT);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_SHARE_STATUS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_SHARE);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE || type == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_PROFILE_COVER_IMAGE);
                iconLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CIRCLE);
                iconLabel.setIcon(new ImageIcon(img));
            }

//            if (type == StatusConstants.NOTIFICATION_CONTACT) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CONTACT);
//                iconLabel.setIcon(new ImageIcon(img));
//            } 
//           else if (type == StatusConstants.NOTIFICATION_BOOK) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_BOOK);
//               iconLabel.setIcon(new ImageIcon(img));
//            } 
//            else if (type == StatusConstants.NOTIFICATION_SMS) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.SMS_NOTIFICATION);
//                iconLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_CIRCLE) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_GROUP);
//                iconLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_PROFILE) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CONTACT);
//                iconLabel.setIcon(new ImageIcon(img));
//            } 
        } catch (Exception ex) {
        }
        return iconLabel;
    }

    public void addMessage(JsonFields js, JPanel messagePanel, HTMLEditorKit kit, HTMLDocument doc) {
        String request_message = "";
        if (js.getMt() == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE) {
            request_message = NotificationMessages.MSG_UPDATE_PROFILE_IMAGE;
        } else if (js.getMt() == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE) {
            request_message = NotificationMessages.MSG_UPDATE_COVER_IMAGE;
        } else if (js.getMt() == StatusConstants.MESSAGE_ADD_FRIEND) {
            request_message = NotificationMessages.MSG_ADD_FRIEND;
        } else if (js.getMt() == StatusConstants.MESSAGE_ACCEPT_FRIEND) {
            request_message = NotificationMessages.MSG_ACCEPT_FRIEND;
        } else if (js.getMt() == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
            request_message = NotificationMessages.MSG_ADD_CIRCLE_MEMBER;
        } else if (js.getMt() == StatusConstants.MESSAGE_ADD_STATUS_COMMENT) {
            request_message = NotificationMessages.MSG_ADD_STATUS_COMMENT;
        } else if (js.getMt() == StatusConstants.MESSAGE_LIKE_STATUS) {
            request_message = NotificationMessages.MSG_LIKE_STATUS;
        } else if (js.getMt() == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT) {
            request_message = NotificationMessages.MSG_ADD_COMMENT_ON_COMMENT;
        } else if (js.getMt() == StatusConstants.MESSAGE_SHARE_STATUS) {
            request_message = NotificationMessages.MSG_SHARE_STATUS;
        } else if (js.getMt() == StatusConstants.MESSAGE_LIKE_IMAGE) {
            request_message = NotificationMessages.MSG_LIKE_IMAGE;
        } else if (js.getMt() == StatusConstants.MESSAGE_IMAGE_COMMENT) {
            request_message = NotificationMessages.MSG_IMAGE_COMMENT;
        } else if (js.getMt() == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT) {
            request_message = NotificationMessages.MSG_LIKE_IMAGE_COMMENT;
        } else if (js.getMt() == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS) {
            request_message = NotificationMessages.MSG_ACCEPT_FRIEND_ACCESS;
        } else if (js.getMt() == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS) {
            request_message = NotificationMessages.MSG_UPGRADE_FRIEND_ACCESS;
        } else if (js.getMt() == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
            request_message = NotificationMessages.MSG_DOWNGRADE_FRIEND_ACCESS;
        } else if (js.getMt() == StatusConstants.MESSAGE_NOW_FRIEND) {
            request_message = NotificationMessages.MSG_NOW_FRIEND;
        }

        try {
            kit.insertHTML(doc, doc.getLength(), setStyle(), 0, 0, null);
            if (js.getFriendIdentity() != null && js.getFndN() != null) {
                String text = "";

                if (js.getLoc() != null && js.getLoc() > 1) {
                    if (js.getLoc() == 2) {
                        text = "<div id=\"single\"><b>" + js.getFndN() + " and " + (js.getLoc() - 1) + " other" + " </b>" + request_message + "</div>";
                    } else {
                        text = "<div id=\"single\"><b>" + js.getFndN() + " and " + (js.getLoc() - 1) + " others" + " </b>" + request_message + "</div>";
                    }
                } else {
                    text = "<div id=\"single\"><b>" + js.getFndN() + " </b>" + request_message + "</div>";
                }

                kit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
                int row = (messagePanel.getPreferredSize().width / 350) + (messagePanel.getPreferredSize().width % 350 > 0 ? 1 : 0);
                messagePanel.setPreferredSize(new Dimension(350, 15 + (row * 15)));

            } else {
                String text = "<div id=\"single\">" + request_message + "</div>";
                kit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
            }
        } catch (Exception e) {
        }
    }

    private String htmlFont = "\"SolaimanLipi\",Vrinda,Arial,verdana,\"Arial Unicode MS\",Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";

    public String setStyle() {
        return "<style>\n"
                + "#single {line-height: 0px;letter-spacing: 1px; color:#4f4f4f; font-family:" + htmlFont + ";font-size: 9px;}\n"
                + "</style>";
    }

    public void mouseLiseraction(final JComponent dd, final JComponent panel, final JsonFields js, final JButton btnClose, final Color bg_color) {
        dd.addMouseListener(new MouseAdapter() {
            //Color bg_color;
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                //bg_color = panel.getBackground();
                panel.setBackground(RingColorCode.NOTIFICATION_SINGLE_HOVER_COLOR);
                btnClose.setVisible(true);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int msgType = js.getMt();
                List<JsonFields> notiFieldList = new ArrayList<JsonFields>();
                if (msgType == StatusConstants.MESSAGE_LIKE_IMAGE || msgType == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE || msgType == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE || msgType == StatusConstants.MESSAGE_IMAGE_COMMENT || msgType == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT) {
                    SingleImageDetailsForNotifications singleImageDetailsForNotifications = new SingleImageDetailsForNotifications(js.getImageId());
                    singleImageDetailsForNotifications.setVisible(true);
                    singleImageDetailsForNotifications.addloader();
                    JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(js.getId());
                    if (temp != null) {
                        temp.setIsRead(true);
                        notiFieldList.add(temp);
                    }

                } else if (msgType == StatusConstants.MESSAGE_ADD_FRIEND || msgType == StatusConstants.MESSAGE_NOW_FRIEND || msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND || msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS || msgType == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS || msgType == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
                    if (FriendList.getInstance().getFriend_hash_map().containsKey(js.getFriendIdentity())) {
                        GuiRingID.getInstance().showFriendProfile(js.getFriendIdentity());
                    } else {
                        if (MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(js.getFriendIdentity()) != null) {
                            GuiRingID.getInstance().showUnknownProfile(MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(js.getFriendIdentity()));
                        } else {
                            new UnknownProfileInfoRequest(js.getFriendIdentity()).start();
                        }
                    }
                    JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(js.getId());
                    if (temp != null) {
                        temp.setIsRead(true);
                        notiFieldList.add(temp);
                    }
                } else if (msgType == StatusConstants.MESSAGE_ADD_STATUS_COMMENT || msgType == StatusConstants.MESSAGE_LIKE_STATUS || msgType == StatusConstants.MESSAGE_LIKE_COMMENT || msgType == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT || msgType == StatusConstants.MESSAGE_SHARE_STATUS) {
                    //GuiRingID.getInstance().getMainRight().showNotificationStatus(js.getNfId());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final SingleStatusPanelInBookHome singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(js.getNfId());
                            if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getAllFeedsView() != null
                                    && GuiRingID.getInstance().getMainRight().getAllFeedsView().getLoadedNewsFeeds().contains(js.getNfId())
                                    && singleStatusPanel != null) {

                                GuiRingID.getInstance().getTopMenuBar().action_of_book_button_notification();
                                //                              GuiRingID.getInstance().getMainLeftContainer().addAcceptedFriendList();

                                new AnimatedGraphics(singleStatusPanel.getContentsHere(), singleStatusPanel.getContentsHere().getBorder());

                                int height = 0;
                                for (int i = 0; i < GuiRingID.getInstance().getMainRight().getAllFeedsView().getContent().getComponentZOrder(singleStatusPanel); i++) {
                                    JComponent component = (JComponent) GuiRingID.getInstance().getMainRight().getAllFeedsView().getContent().getComponent(i);
                                    height += component.getHeight();
                                }
                                GuiRingID.getInstance().getMainRight().getAllFeedsView().setScrollValue(height);
                            } else {
                                //GuiRingID.getInstance().getLeftMainMenuPanel().action_of_book_button_notification();
                                //GuiRingID.getInstance().getMainLeftContainer().addAcceptedFriendList();
                                // new SingleNotificationDialog((long) notificationObj.getNfId());
                                GuiRingID.getInstance().getMainRight().showNotificationStatus(js.getNfId());
                            }
                        }
                    }).start();
                    JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(js.getId());
                    if (temp != null) {
                        temp.setIsRead(true);
                        notiFieldList.add(temp);
                    }

                } else if (msgType == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
                    if (MyfnfHashMaps.getInstance().getCircleLists().get(js.getUt()) != null) {
                        GuiRingID.getInstance().getMainRight().showCircleProfile(js.getAcId());
                    } else {
                        String msg = "        Circle not found";
                        new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, msg).setVisible(true);
                    }

                    JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(js.getId());
                    if (temp != null) {
                        temp.setIsRead(true);
                        notiFieldList.add(temp);
                    }
                }
                if (notiFieldList.size() > 0) {
                    (new InsertIntoNotificationHistoryTable(notiFieldList)).start();
                }

                if (AllNotificationPanel.getInstance() != null && AllNotificationPanel.getInstance().isDisplayable()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < content.getComponentCount(); i++) {
                                if (content.getComponent(i).getName() != null && content.getComponent(i).getName().equals(js.getId())) {
                                    content.remove(i);
                                    js.setIsRead(true);
                                    content.add(buildSingleNotification(js), i);
                                    content.revalidate();
                                    break;
                                }

                            }
                        }
                    });

                }
//                if (GuiRingID.getInstance() != null
//                        && GuiRingID.getInstance().getTopMenuBar() != null
//                        && GuiRingID.getInstance().getTopMenuBar().allNotificationPanel != null
//                        && GuiRingID.getInstance().getTopMenuBar().allNotificationPanel.isDisplayable()) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < content.getComponentCount(); i++) {
//                                if (content.getComponent(i).getName() != null && content.getComponent(i).getName().equals(js.getId())) {
//                                    content.remove(i);
//                                    js.setIsRead(true);
//                                    content.add(buildSingleNotification(js), i);
//                                    content.revalidate();
//                                    break;
//                                }
//                            }
//                        }
//                    });
//                }

            }

            @Override

            public void mouseExited(MouseEvent e) {
                panel.setBackground(bg_color);
                btnClose.setVisible(false);
            }
        }
        );
    }

    public void mouseLiseraction(final JComponent panel) {
        panel.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getSource() == pnlSeeMore) {
                    panel.setBackground(RingColorCode.ALLNOTIFICATION_MORE_HOVER_COLOR);
                    original = lblSeeMore.getFont();
                    Map attributes = original.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    lblSeeMore.setFont(original.deriveFont(attributes));
                    lblSeeMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == pnlSeeMore) {
                    content.remove(pnlSeeMore);
                    addLoading();
                    List<JsonFields> notifList = NotificationHistoryDAO.loadNotificationHistoryList(GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().totalNotifCount, 15);
                    if (notifList != null && notifList.size() > 0) {
                        addAllNotifications();
                    } else {
                        if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().firstRequest) {
                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().LAST_REQUESTED_NOTIFICATION_MIN_UT = AppConstants.NOTIFICATION_MIN_UT + 1;
                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().firstRequest = false;
                        }
                        if (AppConstants.NOTIFICATION_MIN_UT < GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().LAST_REQUESTED_NOTIFICATION_MIN_UT) {
                            new GetNotification(AppConstants.NOTIFICATION_MIN_UT, (short) 2).start();
                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().LAST_REQUESTED_NOTIFICATION_MIN_UT = AppConstants.NOTIFICATION_MIN_UT;
                        } else {
                            removeLoading();
                        }

                    }

                    lblSeeMore.setFont(original);
                    content.revalidate();
                    content.repaint();
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getSource() == pnlSeeMore) {
                    panel.setBackground(RingColorCode.ALLNOTIFICATION_GROUP_PANEL_COLOR);
                    lblSeeMore.setFont(original);
                }

            }
        });

    }

    class GetNotification extends Thread {

        private long max_ut = 0;
        private short scl = 0;
        private int scrollPosition;

        public GetNotification(long max_ut, short scl) {
            this.max_ut = max_ut;
            this.scl = scl;
        }

        @Override
        public void run() {
            try {
                JsonFields notificationRequest = new JsonFields();
                notificationRequest.setAction(AppConstants.TYPE_MY_NOTIFICATIONS);
                String pakId = SendToServer.getRanDomPacketID();
                notificationRequest.setPacketId(pakId);
                notificationRequest.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                if (max_ut > 0) {
                    notificationRequest.setScl(scl);
                    notificationRequest.setUt(max_ut);
                }
                SendToServer.sendPacketAsString(notificationRequest, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(notificationRequest, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess() != null && fields.getSuccess() == false) {
                            //isLoading = true;
                            removeLoading();
                            removelblMoreNotification();
                        } else {
                            //isLoading = false;
                            removeLoading();
                            scrollPosition = scrollPane.getVerticalScrollBar().getValue();
                            addAllNotifications();
                            scrollPane.getVerticalScrollBar().setValue(scrollPosition);
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }
                }

            } catch (Exception e) {
                //e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());
            }
        }
    }

    class AnimatedGraphics implements ActionListener {

        private Color startColor = new Color(0xff8a00);   // where we start
        private Color endColor = new Color(0xE5E6E9);         // where we end
        private Color currentColor = startColor;
        private int animationDuration = 2000;   // each animation will take 2 seconds
        private long animStartTime;         // start time for each animation
        private JPanel panel;
        private Border originalBorder;
        private Timer timer;

        public AnimatedGraphics(JPanel panel, Border originalBorder) {
            this.panel = panel;
            this.originalBorder = originalBorder;
            timer = new Timer(10, this);
            timer.setInitialDelay(0);
            animStartTime = 0 + System.nanoTime() / 1000000;
            timer.start();
        }

        public void actionPerformed(ActionEvent ae) {
            // calculate elapsed fraction of animation
            long currentTime = System.nanoTime() / 1000000;
            long totalTime = currentTime - animStartTime;
            if (totalTime > animationDuration) {
                animStartTime = currentTime;
            }
            float fraction = (float) totalTime / animationDuration;
            fraction = Math.min(1.0f, fraction);
            // interpolate between start and end colors with current fraction
            int red = (int) (fraction * endColor.getRed()
                    + (1 - fraction) * startColor.getRed());
            int green = (int) (fraction * endColor.getGreen()
                    + (1 - fraction) * startColor.getGreen());
            int blue = (int) (fraction * endColor.getBlue()
                    + (1 - fraction) * startColor.getBlue());
            // set our new color appropriately
            currentColor = new Color(red, green, blue);
            panel.setBorder(new MatteBorder(1, 1, 1, 1, currentColor));
            if (fraction >= 1) {
                panel.setBorder(originalBorder);
                timer.stop();
            }

        }

    }

}

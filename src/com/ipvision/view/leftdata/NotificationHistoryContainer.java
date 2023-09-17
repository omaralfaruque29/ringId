/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.leftdata;

import com.ipvision.constants.AppConstants;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.JsonFields;
import com.ipvision.model.dao.NotificationHistoryDAO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.DeleteNotificationRequest;
import com.ipvision.service.NotificationRequest;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.SingleNotification;
import com.ipvision.view.feed.AllNotificationPanel;
import static com.ipvision.view.feed.AllNotificationPanel.makeJLabel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.utility.CategoryButtonPanel;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.JOptionPanelBasics;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author raj
 */
public class NotificationHistoryContainer extends JPanel {

    public JPanel NotificationHistoryShade, buttonPanel;
    public JPanel NotificationWrapperPanel;
    private JLabel editLabel;
    private JPanel NotificationCategoryPanel;
    private String CATEGORY_SELECT_ALL = "Select All";
    private String CATEGORY_DESELECT_ALL = "Deselect All";
    private String CATEGORY_CANCEL = "Cancel";
    private String CATEGORY_DELETE = "Delete";
    public CategoryButtonPanel.CustomPanel item;
    public CategoryButtonPanel categoryPnl;
    public List<SingleNotification> SingleNotificationPanels = new ArrayList<>();
    public JScrollPane notificationScrollPanel;
    public JPanel wrapperPanel;
    public boolean isLoading;
    public JLabel loading;
    public int totalNotifCount = 0;
    private JLabel lblSeeMore;
    public AllNotificationPanel allNotificationPanel = null;
    public long LAST_REQUESTED_NOTIFICATION_MIN_UT = 0;
    public boolean firstRequest = false;

    public NotificationHistoryContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setOpaque(false);
        wrapperPanel = new JPanel(new CardLayout());
        wrapperPanel.setOpaque(false);
        NotificationHistoryShade = new JPanel() {
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
        NotificationHistoryShade.setOpaque(false);
        wrapperPanel.add("NOTIFICATION_HISTORY_SHADE", NotificationHistoryShade);

        NotificationWrapperPanel = new JPanel();
        NotificationWrapperPanel.setLayout(new BoxLayout(NotificationWrapperPanel, BoxLayout.Y_AXIS));
        NotificationWrapperPanel.setOpaque(false);
        JPanel NotificationHolder = new JPanel(new BorderLayout());
        NotificationHolder.setOpaque(false);

        editLabel = new JLabel("Edit");
        editLabel.setBorder(new EmptyBorder(3, 12, 0, 0));
        editLabel.setOpaque(false);
        editLabel.setIcon(DesignClasses.return_image(GetImages.PEN_H));
        editLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        editLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editLabel.setForeground(RingColorCode.RING_THEME_COLOR);
        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (SingleNotification ins : SingleNotificationPanels) {
                    ins.checkboxPanel.setVisible(true);
                    ins.deleteBox.setVisible(true);
                }
                editLabel.setVisible(false);
                NotificationCategoryPanel.setVisible(true);
            }
        });

        NotificationCategoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        NotificationCategoryPanel.setOpaque(false);
        NotificationCategoryPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_FRIEND_LIST_WIDTH, DefaultSettings.MENUBER_HEIGHT));
        if (categoryPnl == null) {
            categoryPnl = new CategoryButtonPanel(mouseListener);
            categoryPnl.btnSelectAll.setText(CATEGORY_SELECT_ALL);
            categoryPnl.btnCancel.setText(CATEGORY_CANCEL);
            categoryPnl.btnDelete.setText(CATEGORY_DELETE);
        }

        NotificationCategoryPanel.add(categoryPnl);
        NotificationCategoryPanel.setVisible(false);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setVisible(false);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        buttonPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(6, 0, 0, 0),
                new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        buttonPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT - 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(editLabel);
        buttonPanel.add(NotificationCategoryPanel);

        JPanel pnlSeeMore = new JPanel(new FlowLayout(FlowLayout.LEFT, 90, 5));
        pnlSeeMore.setBackground(RingColorCode.ALLNOTIFICATION_GROUP_PANEL_COLOR);
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Graphics2D g2d = (Graphics2D) g;
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2d.setColor(new Color(100, 0, 0, 10));
//                int w = getWidth();
//                int h = getHeight();
//                g2d.fillRect(0, 0, w, h);
//            }

        //pnlSeeMore.setOpaque(false);
        pnlSeeMore.setPreferredSize(new Dimension(0, 30));
        final JLabel lblSeeMore = makeJLabel("See All...", Font.BOLD, 13);
        pnlSeeMore.add(lblSeeMore);
        lblSeeMore.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                original = lblSeeMore.getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblSeeMore.setFont(original.deriveFont(attributes));
                lblSeeMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblSeeMore.setFont(original);
                lblSeeMore.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                allNotificationPanel = AllNotificationPanel.getInstance();
                allNotificationPanel.addAllNotifications();
                GuiRingID.getInstance().loadIntoMainRightContent(allNotificationPanel);
            }

        });
        add(pnlSeeMore, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.NORTH);
        NotificationHolder.add(NotificationWrapperPanel, BorderLayout.NORTH);
        wrapperPanel.add("NOTIFICATION_HISTORY", NotificationHolder);
        NotificationHistoryShade.setVisible(true);
        NotificationHolder.setVisible(true);
        notificationScrollPanel = DesignClasses.getDefaultScrollPaneThin(wrapperPanel);
        notificationScrollPanel.getVerticalScrollBar().setValue(0);
        notificationScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(notificationScrollPanel, BorderLayout.CENTER);
        addloding();
    }

    public void addAllNotifications(List<JsonFields> finalList, final int notifCount) {
        removeLoadingcontent();
        totalNotifCount = totalNotifCount + notifCount;

        Collections.sort(finalList, new Comparator<JsonFields>() {
            @Override
            public int compare(JsonFields one, JsonFields other) {
                Long onesTime = one.getUt();
                Long othersTime = other.getUt();
                return othersTime.compareTo(onesTime);
            }
        });

        for (JsonFields js : finalList) {
            SingleNotification single = new SingleNotification(js);
            if (!editLabel.isVisible()) {
                single.deleteBox.setVisible(true);
                single.checkboxPanel.setVisible(true);
            } else {
                single.deleteBox.setVisible(false);
                single.checkboxPanel.setVisible(false);
            }

            SingleNotificationPanels.add(single);
            NotificationWrapperPanel.add(single);
        }

        NotificationWrapperPanel.revalidate();
        NotificationWrapperPanel.repaint();
        isLoading = false;
        if (NotificationWrapperPanel.getComponentCount() > 0) {
            buttonPanel.setVisible(true);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                notificationScrollPanel.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        int containerHeight = wrapperPanel.getHeight();
                        final int topHeight = e.getValue();
                        int barHeight = notificationScrollPanel.getVerticalScrollBar().getHeight();
                        int bottomHeight = containerHeight - (topHeight + barHeight);
                        int totalCalHeight = (topHeight + barHeight + bottomHeight);
                        if (bottomHeight <= 0 && isLoading == false) {
                            isLoading = true;
                            addloding();
                            final List<JsonFields> list = NotificationHistoryDAO.loadNotificationHistoryList(totalNotifCount, 5);
                            if (list != null && list.size() > 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        addAllNotifications(list, list.size());
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                notificationScrollPanel.getVerticalScrollBar().setValue(topHeight);
                                            }
                                        });
                                    }
                                });
                            } else {
                                NotificationHistoryDAO.loadMinTimeFromNotificationHistory();
                                if (firstRequest) {
                                    LAST_REQUESTED_NOTIFICATION_MIN_UT = AppConstants.NOTIFICATION_MIN_UT + 1;
                                    firstRequest = false;
                                }
                                if (AppConstants.NOTIFICATION_MIN_UT < LAST_REQUESTED_NOTIFICATION_MIN_UT) {
                                    new NotificationRequest(AppConstants.NOTIFICATION_MIN_UT, (short) 2).start();
                                    LAST_REQUESTED_NOTIFICATION_MIN_UT = AppConstants.NOTIFICATION_MIN_UT;
                                } else {
                                    removeLoadingcontent();
                                }

                            }
                        }
                    }
                });

            }
        });

    }

    public void addMoreContetns(Map<String, JsonFields> moreNotifMap) {
        removeLoadingcontent();
        List<JsonFields> finalList = new ArrayList<JsonFields>();

        for (Map.Entry<String, JsonFields> entity : moreNotifMap.entrySet()) {
            JsonFields js = entity.getValue();
            finalList.add(js);
        }

        MyfnfHashMaps.getInstance().getTempNotifications().clear();
        totalNotifCount = totalNotifCount + finalList.size();
        Collections.sort(finalList, new Comparator<JsonFields>() {
            @Override
            public int compare(JsonFields one, JsonFields other) {
                Long onesTime = one.getUt();
                Long othersTime = other.getUt();
                return othersTime.compareTo(onesTime);
            }
        });
        for (JsonFields js : finalList) {
            SingleNotification single = new SingleNotification(js);
            NotificationWrapperPanel.add(single);
            SingleNotificationPanels.add(single);
        }
        isLoading = false;
        NotificationWrapperPanel.revalidate();
    }

    public void addSingleNotification(List<JsonFields> finalList) {
        for (JsonFields js : finalList) {
            SingleNotification single = new SingleNotification(js);
            NotificationWrapperPanel.add(single, 0);
            SingleNotificationPanels.add(single);
        }
        NotificationCategoryPanel.revalidate();

    }

    private void addloding() {
        loading = DesignClasses.loadingLable(true);
        NotificationWrapperPanel.add(loading);
        NotificationWrapperPanel.revalidate();
    }

    public void removeLoadingcontent() {
        NotificationWrapperPanel.remove(loading);
        NotificationWrapperPanel.revalidate();
    }

    private boolean isAllPanelsSelected() {
        for (int i = 0; i < NotificationWrapperPanel.getComponentCount(); i++) {
            SingleNotification sp = (SingleNotification) NotificationWrapperPanel.getComponent(i);
            if (!sp.deleteBox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllPanelsDeselected() {
        for (int i = 0; i < NotificationWrapperPanel.getComponentCount(); i++) {
            SingleNotification sp = (SingleNotification) NotificationWrapperPanel.getComponent(i);
            if (sp.deleteBox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void action_all_selected() {
        if (isAllPanelsDeselected()) {
            categoryPnl.btnSelectAll.setText(CATEGORY_SELECT_ALL);
        } else if (isAllPanelsSelected()) {
            categoryPnl.btnSelectAll.setText(CATEGORY_DESELECT_ALL);
        }
        categoryPnl.btnSelectAll.revalidate();
    }

    MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            item = (CategoryButtonPanel.CustomPanel) e.getSource();
            if (item.text.equalsIgnoreCase(CATEGORY_SELECT_ALL) || (item.text.equalsIgnoreCase(CATEGORY_DESELECT_ALL))) {
                categoryPnl.btnSelectAll.setMouseClicked();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnSelectAll.isSelected = false;
                if (categoryPnl.btnSelectAll.lblText.getText().equalsIgnoreCase(CATEGORY_SELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(CATEGORY_DESELECT_ALL);
                    for (SingleNotification ins : SingleNotificationPanels) {
                        ins.deleteBox.setSelected(true);
                    }
                } else if (categoryPnl.btnSelectAll.lblText.getText().equalsIgnoreCase(CATEGORY_DESELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(CATEGORY_SELECT_ALL);
                    for (SingleNotification ins : SingleNotificationPanels) {
                        ins.deleteBox.setSelected(false);

                    }
                }
            } else if (item.text.equalsIgnoreCase(CATEGORY_CANCEL)) {
                categoryPnl.btnCancel.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnCancel.isSelected = false;
                for (SingleNotification ins : SingleNotificationPanels) {
                    ins.checkboxPanel.setVisible(false);
                    NotificationWrapperPanel.revalidate();
                    NotificationWrapperPanel.repaint();
                }
                editLabel.setVisible(true);
                NotificationCategoryPanel.setVisible(false);

            } else if (item.text.equalsIgnoreCase(CATEGORY_DELETE)) {
                List<String> notificationsToDelete = new ArrayList<>();
                categoryPnl.btnDelete.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.isSelected = false;

                for (SingleNotification ins : SingleNotificationPanels) {
                    if (ins.deleteBox.isSelected()) {
                        notificationsToDelete.add(ins.notificationObj.getId());
                    }
                }

                if (notificationsToDelete != null && notificationsToDelete.size() > 0) {
                    HelperMethods.showConfirmationDialogMessage("Do you want to completely delete these records?");
                } else {
                    new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, "Please select notification record to delete").setVisible(true);
                }
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteNotificationRequest(notificationsToDelete, SingleNotificationPanels).start();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            CategoryButtonPanel.CustomPanel item = (CategoryButtonPanel.CustomPanel) e.getSource();
            if (!item.isSelected) {
                item.setMouseEntered();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            CategoryButtonPanel.CustomPanel item = (CategoryButtonPanel.CustomPanel) e.getSource();
            if (!item.isSelected) {
                item.setMouseExited();
            }
        }

    };

}

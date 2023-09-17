/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import static java.awt.FlowLayout.LEFT;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import com.ipvision.model.dao.InsertIntoNotificationHistoryTable;
import com.ipvision.service.DeleteNotificationRequest;
import com.ipvision.view.feed.SingleStatusPanelInBookHome;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.service.aboutme.UnknownProfileInfoRequest;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RingLayout;
import com.ipvision.view.utility.RoundedCornerButton;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 *
 * @author Wasif Islam
 */
public class SingleNotification extends JPanel {

    private SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a, dd MMMM yyyy");
    public JLabel profileImageLabel;
    public JsonFields notificationObj;
    public JPanel leftImagePanel;
    public JPanel rightContentPanel;
    private Color bgColor;
    private JLabel timeAndtypeImageLabel;
    // private JDialogNotification jDialogNotification;
    private Color borderColor = new Color(0xffe4c4);
    private SingleNotification instance;
    //private JLabel lblDltNotification;
    //private JPanel pnlDltNotification;
    public JButton okBtn = new RoundedCornerButton("Ok", "Ok");
    public JButton cancelBtn = new RoundedCornerButton("Cancel", "Cancel");
    public JPanel wrapperDeletePanel;
    private JPanel wrapperPanel;
    public JPanel loadingContainer;
    public JPanel checkboxPanel;
    public JCheckBox deleteBox;
    public boolean mouseClicked = false;

    public SingleNotification(JsonFields js) {
        this.notificationObj = js;
        //this.jDialogNotification = jDialogNotification;
        this.instance = this;
        setLayout(new BorderLayout(2, 0));
        //setBorder(new MatteBorder(0, 0, 1, 0, borderColor));
        setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
        if (js.getIsRead()) {
            bgColor = RingColorCode.NOTIFICATION_SINGLE_SEEN_PANEL_COLOR;
        } else {
            bgColor = RingColorCode.NOTIFICATION_SINGLE_UNSEEN_PANEL_COLOR;
        }
        setBackground(bgColor);
        setName(js.getId());

        checkboxPanel = new JPanel(new BorderLayout());
        // checkboxPanel.setPreferredSize(new Dimension(25, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        checkboxPanel.setOpaque(false);
        deleteBox = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
        deleteBox.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
        deleteBox.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
        deleteBox.setOpaque(false);
        deleteBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().action_all_selected();
                }

            }
        });
        checkboxPanel.setVisible(false);
        checkboxPanel.add(deleteBox, BorderLayout.CENTER);

        leftImagePanel = new JPanel(new FlowLayout(LEFT, 2, 2));
        leftImagePanel.setOpaque(false);
        profileImageLabel = new JLabel();
        profileImageLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        leftImagePanel.add(checkboxPanel);
        leftImagePanel.add(profileImageLabel);
        add(leftImagePanel, BorderLayout.WEST);
        rightContentPanel = new JPanel(new BorderLayout(0, 0));
        addMessage = new JTextPane();
        addMessage.setEditable(false);
        addMessage.setContentType("text/html");
        addMessage.setOpaque(false);
        kit = new HTMLEditorKit();
        doc = new HTMLDocument();
        addMessage.setEditorKit(kit);
        addMessage.setDocument(doc);
        mouseLiseraction(addMessage, leftImagePanel);

//        lblDltNotification = new JLabel();
//        lblDltNotification.setToolTipText("Delete");
//        lblDltNotification.setIcon(DesignClasses.return_image(GetImages.NOTIFICATION_DELETE_H));
//        lblDltNotification.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        lblDltNotification.setVisible(false);
//        pnlDltNotification = new JPanel();
//        pnlDltNotification.setPreferredSize(new Dimension(13, 20));
//        pnlDltNotification.setOpaque(false);
//        pnlDltNotification.add(lblDltNotification);
        JPanel wrapperrightMessagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapperrightMessagePanel.setOpaque(false);
        wrapperrightMessagePanel.add(addMessage);
        //       wrapperrightMessagePanel.add(pnlDltNotification);
        //       mouseLiseraction(lblDltNotification, wrapperrightMessagePanel);

        rightContentPanel.add(wrapperrightMessagePanel, BorderLayout.NORTH);
        rightContentPanel.setBorder(new EmptyBorder(0, 2, 0, 0));
        rightContentPanel.setOpaque(false);
        timeAndtypeImageLabel = DesignClasses.makeJLabelUnderFullName("");
        timeAndtypeImageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        rightContentPanel.add(timeAndtypeImageLabel, BorderLayout.CENTER);
        wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(rightContentPanel);
        add(wrapperPanel, BorderLayout.CENTER);
        mouseLiseraction(timeAndtypeImageLabel, leftImagePanel);
        mouseLiseraction(this, leftImagePanel);

        //MyfnfHashMaps.getInstance().getSingleNotificationHistoryPanel().put(notificationObj.getId(), this);
        //addDeletePanel();
        this.addProfileImage();
        this.addMessage(js.getMt());
        this.addTime();
        this.changeNotificationImage(js.getMt());
    }
    private JTextPane addMessage;
    private HTMLEditorKit kit = new HTMLEditorKit();
    private HTMLDocument doc = new HTMLDocument();

    public void addLoadingImage() {
        rightContentPanel.add(DesignClasses.loadingLable(true));
        rightContentPanel.revalidate();
    }

    public void addMessage(int msgType) {

        String requset_message = "";
        if (msgType == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE) {
            requset_message = NotificationMessages.MSG_UPDATE_PROFILE_IMAGE;
        } else if (msgType == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE) {
            requset_message = NotificationMessages.MSG_UPDATE_COVER_IMAGE;
        } else if (msgType == StatusConstants.MESSAGE_ADD_FRIEND) {
            requset_message = NotificationMessages.MSG_ADD_FRIEND;
        } else if (msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND) {
            requset_message = NotificationMessages.MSG_ACCEPT_FRIEND;
        } else if (msgType == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
            requset_message = NotificationMessages.MSG_ADD_CIRCLE_MEMBER;
        } else if (msgType == StatusConstants.MESSAGE_ADD_STATUS_COMMENT) {
            requset_message = NotificationMessages.MSG_ADD_STATUS_COMMENT;
        } else if (msgType == StatusConstants.MESSAGE_LIKE_STATUS) {
            requset_message = NotificationMessages.MSG_LIKE_STATUS;
        } else if (msgType == StatusConstants.MESSAGE_LIKE_COMMENT) {
            requset_message = NotificationMessages.MSG_LIKE_COMMENT;
        } else if (msgType == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT) {
            requset_message = NotificationMessages.MSG_ADD_COMMENT_ON_COMMENT;
        } else if (msgType == StatusConstants.MESSAGE_SHARE_STATUS) {
            requset_message = NotificationMessages.MSG_SHARE_STATUS;
        } else if (msgType == StatusConstants.MESSAGE_LIKE_IMAGE) {
            requset_message = NotificationMessages.MSG_LIKE_IMAGE;
        } else if (msgType == StatusConstants.MESSAGE_IMAGE_COMMENT) {
            requset_message = NotificationMessages.MSG_IMAGE_COMMENT;
        } else if (msgType == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT) {
            requset_message = NotificationMessages.MSG_LIKE_IMAGE_COMMENT;
        } else if (msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS) {
            requset_message = NotificationMessages.MSG_ACCEPT_FRIEND_ACCESS;
        } else if (msgType == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS) {
            requset_message = NotificationMessages.MSG_UPGRADE_FRIEND_ACCESS;
        } else if (msgType == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
            requset_message = NotificationMessages.MSG_DOWNGRADE_FRIEND_ACCESS;
        } else if (msgType == StatusConstants.MESSAGE_NOW_FRIEND) {
            requset_message = NotificationMessages.MSG_NOW_FRIEND;
        }
        try {
            kit.insertHTML(doc, doc.getLength(), setStyle(), 0, 0, null);

            if (notificationObj != null && notificationObj.getFriendIdentity() != null && notificationObj.getFndN() != null && notificationObj.getFndN().trim().length() > 0) {
                if (notificationObj.getLoc() != null && notificationObj.getLoc() > 1) {
                    if (notificationObj.getLoc() == 2) {
                        kit.insertHTML(doc, doc.getLength(), addMessageText(notificationObj.getFndN() + " and " + (notificationObj.getLoc() - 1) + " other ", requset_message), 0, 0, null);
                    } else {
                        kit.insertHTML(doc, doc.getLength(), addMessageText(notificationObj.getFndN() + " and " + (notificationObj.getLoc() - 1) + " others ", requset_message), 0, 0, null);
                    }
                } else {
                    kit.insertHTML(doc, doc.getLength(), addMessageText(notificationObj.getFndN(), requset_message), 0, 0, null);
                }
            } else {
                kit.insertHTML(doc, doc.getLength(), addMessageText("", requset_message), 0, 0, null);
            }
        } catch (Exception es) {
        }
    }

    private String htmlFont = "\"SolaimanLipi\",Vrinda,Arial,verdana,\"Arial Unicode MS\",Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";

    public String setStyle() {
        return "<style>\n"
                + "#single {line-height: 0px;letter-spacing: 1px; color:#4f4f4f; font-family:" + htmlFont + ";font-size: 9px;width: 145px;}\n"
                + "</style>";
    }

    private String addMessageText(String name, String message) {
        return "<div  id=\"single\"><b>" + name + " </b>" + message + "</div>";
    }

    public void addProfileImage() {
        // boolean profileImageHas = false;
        if (notificationObj != null && notificationObj.getFriendIdentity() != null && notificationObj.getFriendIdentity().trim().length() > 0) {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(notificationObj.getFriendIdentity());
            if (friendProfileInfo != null) {
                short[] friendPrivacy = new short[5];
                if (friendProfileInfo.getPrivacy() != null) {
                    friendPrivacy = friendProfileInfo.getPrivacy();
                }
                //  int friendShipStatus = friendProfileInfo.getFriendShipStatus();
                short profileImagePrivacy = friendPrivacy[2];
                if (friendProfileInfo.getProfileImage() != null
                        && friendProfileInfo.getProfileImage().trim().length() > 0
                        && profileImagePrivacy > 0
                        && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || (profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) //   && (!HelperMethods.isInvalidDownloadUrl(friendProfileInfo.getProfileImage()))
                        ) {
                    if (!HelperMethods.isInvalidDownloadUrl(ImageHelpers.getThumbUrl(friendProfileInfo.getProfileImage()))
                            && !HelperMethods.isInvalidDownloadUrl(ImageHelpers.getCropUrl(friendProfileInfo.getProfileImage()))) {
                        //ImageHelpers.addProfileImageThumb(profileImageLabel, friendProfileInfo.getProfileImage(), 35);
                        ImageHelpers.addProfileImageThumbInQueue(profileImageLabel, friendProfileInfo.getProfileImage(), 35);
                    } else {
                        loadDefaultImage();
                    }
                } else {
                    loadDefaultImage();
                }
            } else {
                loadDefaultImage();
            }

        }

    }

    public void loadDefaultImage() {
//        //BufferedImage img = ImageHelpers.getUnknownImage35();
//        profileImageLabel.setIcon(ImageObjects.unknown_35);

        UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(notificationObj.getFriendIdentity());
        //BufferedImage img = null;
        if (friendProfileInfo != null && friendProfileInfo.getFullName() != null && friendProfileInfo.getFullName().trim().length() > 0) {
            ImageHelpers.addProfileImageThumbInQueue(profileImageLabel, friendProfileInfo.getProfileImage(), 35, HelperMethods.getShortName(friendProfileInfo.getFullName()));
            //img = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(friendProfileInfo.getFullName()));

        }
//        profileImageLabel.setIcon(new ImageIcon(img));
//        //BufferedImage img = ImageHelpers.getUnknownImage35();
//        //profileImageLabel.setIcon(ImageObjects.unknown_35);
//        profileImageLabel.revalidate();
    }

    public void addTime() {
        if (notificationObj != null && notificationObj.getFriendIdentity() != null && notificationObj.getFriendIdentity().trim().length() > 0) {
            if (notificationObj.getUt() != null) {
                timeAndtypeImageLabel.setText(FeedUtils.getShowableDate(notificationObj.getUt()));
                timeAndtypeImageLabel.setToolTipText(FeedUtils.getActualDate(notificationObj.getUt()));
            }
        } else {
            timeAndtypeImageLabel.setText(FeedUtils.getShowableDate(System.currentTimeMillis()));
            timeAndtypeImageLabel.setToolTipText(FeedUtils.getActualDate(System.currentTimeMillis()));
        }

    }

    public void changeNotificationImage(int type) {
        try {
            if (type == StatusConstants.MESSAGE_ADD_FRIEND) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ADD_FRIEND);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ACCEPT_FRIEND || type == StatusConstants.MESSAGE_NOW_FRIEND) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ACCEPT_FRIEND);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_ACCEPT_FRIEND_ACCESS);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_UPGRADE_FRIEND_ACCESS);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_DOWNGRADE_FRIEND_ACCESS);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_LIKE_COMMENT || type == StatusConstants.MESSAGE_LIKE_IMAGE || type == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT || type == StatusConstants.MESSAGE_LIKE_STATUS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_LIKE);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT || type == StatusConstants.MESSAGE_ADD_STATUS_COMMENT || type == StatusConstants.MESSAGE_IMAGE_COMMENT) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_COMMENT);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_SHARE_STATUS) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_SHARE);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE || type == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_PROFILE_COVER_IMAGE);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            } else if (type == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CIRCLE);
                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
            }

//            if (type == StatusConstants.NOTIFICATION_CONTACT) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CONTACT);
//                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_BOOK) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_BOOK);
//                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_SMS) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.SMS_NOTIFICATION);
//                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_CIRCLE) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_GROUP);
//                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
//            } else if (type == StatusConstants.NOTIFICATION_PROFILE) {
//                BufferedImage img = DesignClasses.getBufferImageFromImageList(GetImages.NOTIFICATION_CONTACT);
//                timeAndtypeImageLabel.setIcon(new ImageIcon(img));
//            }
        } catch (Exception ex) {
        }
    }

    private void addDeletePanel() {
        wrapperDeletePanel = new JPanel(new BorderLayout(5, 5));
        wrapperDeletePanel.setOpaque(false);
        JPanel pnlConfirmation = new JPanel();
        pnlConfirmation.setOpaque(false);
        pnlConfirmation.setBorder(new EmptyBorder(0, 2, 0, 0));
        JLabel lblConfirmation = new JLabel("Want to delete this notification?");
        //lblConfirmation.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        lblConfirmation.setForeground(new Color(0x4f4f4f));
        pnlConfirmation.add(lblConfirmation);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 10, 5, 0));
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        wrapperDeletePanel.add(pnlConfirmation, BorderLayout.NORTH);
        wrapperDeletePanel.add(btnPanel, BorderLayout.CENTER);
        mouseLiseraction(okBtn, btnPanel);
        mouseLiseraction(cancelBtn, btnPanel);
        wrapperDeletePanel.setVisible(false);
        wrapperPanel.add(wrapperDeletePanel);

    }

    public void addLoading() {
        loadingContainer = new JPanel();
        loadingContainer.setBorder(new EmptyBorder(3, 70, 3, 0));
        loadingContainer.setOpaque(false);
        JLabel loading = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);
        loadingContainer.add(loading);
        wrapperPanel.add(loadingContainer);
        loading.setVisible(true);
    }

    public void mouseLiseraction(final JComponent dd, final JPanel panel) {
        dd.addMouseListener(new MouseAdapter() {
            Color bg_color;

            @Override
            public void mouseEntered(MouseEvent e) {
                bg_color = instance.getBackground();
                instance.setBackground(RingColorCode.NOTIFICATION_SINGLE_HOVER_COLOR);
//                lblDltNotification.setVisible(true);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
//                if (e.getSource() == lblDltNotification) {
//                    rightContentPanel.setVisible(false);
//                    wrapperDeletePanel.setVisible(true);
//                    instance.setBackground(bg_color);
//                } 

                if (e.getSource() == okBtn) {
                    wrapperDeletePanel.setVisible(false);
                    addLoading();
                    new DeleteNotificationRequest(notificationObj.getId(), instance).start();
                    instance.setBackground(bg_color);

                } else if (e.getSource() == cancelBtn) {
                    wrapperDeletePanel.setVisible(false);
                    rightContentPanel.setVisible(true);
                    instance.setBackground(bg_color);

                } else {
                    mouseClicked = true;
                    int msgType = notificationObj.getMt();
                    // jDialogNotification.hideThis();
                    final List<JsonFields> notiFieldList = new ArrayList<JsonFields>();

                    if (msgType == StatusConstants.MESSAGE_LIKE_IMAGE || msgType == StatusConstants.MESSAGE_UPDATE_PROFILE_IMAGE || msgType == StatusConstants.MESSAGE_UPDATE_COVER_IMAGE || msgType == StatusConstants.MESSAGE_IMAGE_COMMENT || msgType == StatusConstants.MESSAGE_LIKE_IMAGE_COMMENT) {
                        SingleImageDetailsForNotifications sidfn = new SingleImageDetailsForNotifications(notificationObj.getImageId());
                        sidfn.setVisible(true);
                        sidfn.addloader();

                        JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(notificationObj.getId());
                        if (temp != null) {
                            temp.setIsRead(true);
                            notiFieldList.add(temp);
                        }

                    } else if (msgType == StatusConstants.MESSAGE_ADD_FRIEND || msgType == StatusConstants.MESSAGE_NOW_FRIEND || msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND || msgType == StatusConstants.MESSAGE_ACCEPT_FRIEND_ACCESS || msgType == StatusConstants.MESSAGE_UPGRADE_FRIEND_ACCESS || msgType == StatusConstants.MESSAGE_DOWNGRADE_FRIEND_ACCESS) {
                        if (FriendList.getInstance().getFriend_hash_map().containsKey(notificationObj.getFriendIdentity())) {
                            GuiRingID.getInstance().showFriendProfile(notificationObj.getFriendIdentity());
                        } else {
                            if (MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(notificationObj.getFriendIdentity()) != null) {
                                GuiRingID.getInstance().getMainRight().showUnknownProfile(MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(notificationObj.getFriendIdentity()));
                            } else {
                                new UnknownProfileInfoRequest(notificationObj.getFriendIdentity()).start();
                            }
                        }

                        JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(notificationObj.getId());
                        if (temp != null) {
                            temp.setIsRead(true);
                            notiFieldList.add(temp);
                        }

                    } else if (msgType == StatusConstants.MESSAGE_ADD_STATUS_COMMENT || msgType == StatusConstants.MESSAGE_LIKE_STATUS || msgType == StatusConstants.MESSAGE_LIKE_COMMENT || msgType == StatusConstants.MESSAGE_ADD_COMMENT_ON_COMMENT || msgType == StatusConstants.MESSAGE_SHARE_STATUS) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final SingleStatusPanelInBookHome singleStatusPanel = NewsFeedMaps.getInstance().getSingleStatusPanelInBookHome().get(notificationObj.getNfId());
                                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getAllFeedsView() != null
                                        && GuiRingID.getInstance().getMainRight().getAllFeedsView().getLoadedNewsFeeds().contains(notificationObj.getNfId())
                                        && singleStatusPanel != null) {

                                    GuiRingID.getInstance().getTopMenuBar().action_of_book_button_notification();
                                    //                                 GuiRingID.getInstance().getMainLeftContainer().addAcceptedFriendList();

                                    new AnimatedGraphics(singleStatusPanel.getContentsHere(), singleStatusPanel.getContentsHere().getBorder());

                                    //int height = 0;
                                    /*for (int i = 0; i < GuiRingID.getInstance().getMainRight().getAllFeedsView().getContent().getComponentZOrder(singleStatusPanel) - 1; i++) {
                                     JComponent component = (JComponent) GuiRingID.getInstance().getMainRight().getAllFeedsView().getContent().getComponent(i);
                                     height += component.getY();
                                     }*/
                                    int height = 0;
                                    height = singleStatusPanel.getY();

                                    JPanel feedConents = GuiRingID.getInstance().getMainRight().getAllFeedsView().getContent();
                                    int component_order = feedConents.getComponentZOrder(singleStatusPanel);
                                    if (component_order > 0) {
                                        if (((RingLayout) feedConents.getLayout()).getNumberOfColumn() > 1) {
                                            if (component_order > 2) {
                                                JComponent previous_component = (JComponent) feedConents.getComponent(component_order - 2);
                                                height = previous_component.getY();
                                            } else {
                                                JComponent previous_component = (JComponent) feedConents.getComponent(component_order - 1);
                                                height = previous_component.getY();
                                            }

                                        } else {
                                            JComponent previous_component = (JComponent) feedConents.getComponent(component_order - 1);
                                            height = previous_component.getY();
                                        }
                                    }

                                    GuiRingID.getInstance().getMainRight().getAllFeedsView().setScrollValue(height);
                                } else {
                                    GuiRingID.getInstance().getMainRight().showNotificationStatus(notificationObj.getNfId());
                                }
                            }
                        }).start();

                        JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(notificationObj.getId());
                        if (temp != null) {
                            temp.setIsRead(true);
                            notiFieldList.add(temp);
                        }

                    } else if (msgType == StatusConstants.MESSAGE_ADD_CIRCLE_MEMBER) {
                        if (MyfnfHashMaps.getInstance().getCircleLists().get(notificationObj.getAcId()) != null) {
                            GuiRingID.getInstance().getMainRight().showCircleProfile(notificationObj.getAcId());
                        } else {
                            String msg = "        Circle not found";
                            new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, msg).setVisible(true);
                        }

                        JsonFields temp = MyfnfHashMaps.getInstance().getNotificationsMap().get(notificationObj.getId());
                        if (temp != null) {
                            temp.setIsRead(true);
                            notiFieldList.add(temp);
                        }
                    }

                    if (notiFieldList.size() > 0) {
                        (new InsertIntoNotificationHistoryTable(notiFieldList)).start();
                    }

                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainLeftContainer() != null
                            && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer() != null
                            && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.isDisplayable()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponentCount(); i++) {
                                    if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(i).getName() != null && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(i).getName().equals(notificationObj.getId())) {
                                        //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.remove(i);
                                        notificationObj.setIsRead(true);
                                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(i).setBackground(Color.WHITE);
                                        //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.add(new SingleNotification(notificationObj), i);
                                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.revalidate();
                                        break;
                                    }
                                }
                            }
                        });
                    }

                }
            }

            @Override

            public void mouseExited(MouseEvent e) {
                if (mouseClicked) {
                    instance.setBackground(Color.WHITE);
                    mouseClicked = false;
                } else {
                    instance.setBackground(bg_color);
                }
            }
        }
        );
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

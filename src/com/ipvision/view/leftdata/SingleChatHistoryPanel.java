/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.leftdata;

import com.ipv.chat.ChatConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.friendprofile.MyfriendSlider;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Wasif Islam
 */
public class SingleChatHistoryPanel extends JPanel {

    private JLabel frienImageLabel = new JLabel();
    private JLabel fullNameLabel;
    private JLabel lastMessageLabel;
    private JPanel lastMessagePanel;
    int friendInfowidth = 230;
    public JPanel friendInfoPanel;
    public JPanel iconPanel;
    public JLabel timeLabel;
    JPanel timePanel;
    public RecentDTO recentDTO;
    public String contactId = null;
    private SingleChatHistoryPanel currInstance;
    private JLabel outgoingLabel;
    private JLabel incomingLabel;
    public JPanel checkboxPanel;
    public JCheckBox deleteBox;
    public JPanel checkBoxAndImagePanel;

    public RecentDTO getRecentDTO() {
        return recentDTO;
    }

    public void setRecentDTO(RecentDTO recentDTO) {
        this.recentDTO = recentDTO;
    }

    public SingleChatHistoryPanel(RecentDTO recentDTO) {
        this.currInstance = this;
        this.recentDTO = recentDTO;
        this.contactId = recentDTO.getContactId();
        this.setLayout(new BorderLayout());
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
        friendInfoPanel = new JPanel(new BorderLayout());
        friendInfoPanel.setOpaque(false);
        friendInfoPanel.setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE, 4, 0));
        friendInfoPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + 15));
        friendInfoPanel.addMouseListener(mouseListener);
        JPanel friendImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        friendImagePanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));//+ DefaultSettings.VGAP_5
        friendImagePanel.add(frienImageLabel);
        friendImagePanel.setOpaque(false);//Background(Color.cyan);//Opaque(false);

        checkBoxAndImagePanel = new JPanel(new BorderLayout());
        checkBoxAndImagePanel.setOpaque(false);
        checkboxPanel = new JPanel(new BorderLayout());
        checkboxPanel.setPreferredSize(new Dimension(25, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        checkboxPanel.setOpaque(false);
        deleteBox = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
        deleteBox.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
        deleteBox.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
        deleteBox.setOpaque(false);
        checkboxPanel.setVisible(false);
        checkboxPanel.add(deleteBox, BorderLayout.CENTER);
        checkBoxAndImagePanel.add(checkboxPanel, BorderLayout.WEST);
        checkBoxAndImagePanel.add(friendImagePanel, BorderLayout.CENTER);

        fullNameLabel = DesignClasses.makeJLabelFullName("", 13);
        fullNameLabel.setPreferredSize(new Dimension(125, 15));
        lastMessagePanel = new JPanel(new BorderLayout());
        lastMessagePanel.setOpaque(false);
        lastMessageLabel = DesignClasses.makeJLabelChatfullNameMe("");
        lastMessageLabel.setPreferredSize(new Dimension(125, 12));
        //lastMessageLabel.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        lastMessagePanel.add(lastMessageLabel, BorderLayout.NORTH);

        JPanel fullNameWithMsgWrapper = new JPanel(new BorderLayout());
        fullNameWithMsgWrapper.setOpaque(false);

        JPanel fullNameWhatInmindPanel = new JPanel(new BorderLayout());
        fullNameWhatInmindPanel.setOpaque(false);
        fullNameWhatInmindPanel.add(fullNameLabel, BorderLayout.NORTH);
        fullNameWhatInmindPanel.add(lastMessagePanel, BorderLayout.CENTER);
        fullNameWithMsgWrapper.add(fullNameWhatInmindPanel, BorderLayout.NORTH);

        JPanel frdImgNameMsgWrapper = new JPanel(new GridBagLayout());
        frdImgNameMsgWrapper.setOpaque(false);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        frdImgNameMsgWrapper.add(checkBoxAndImagePanel, cons);
        cons.gridx++;
        // cons.insets = new Insets(0, 5, 0, 0);
        frdImgNameMsgWrapper.add(fullNameWithMsgWrapper, cons);

        friendInfoPanel.add(frdImgNameMsgWrapper, BorderLayout.WEST);

        JPanel infoAndTimeWrapper = new JPanel(new BorderLayout());
        infoAndTimeWrapper.setPreferredSize(new Dimension(30, 45));
        infoAndTimeWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));
        infoAndTimeWrapper.setOpaque(false);
        iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        iconPanel.setOpaque(false);
        incomingLabel = DesignClasses.create_imageJlabel(GetImages.CHAT_INCOMING);
        outgoingLabel = DesignClasses.create_imageJlabel(GetImages.CHAT_OUTGOING);
        infoAndTimeWrapper.add(iconPanel, BorderLayout.NORTH);

        friendInfoPanel.add(infoAndTimeWrapper, BorderLayout.EAST);
        timePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        timePanel.setOpaque(false);
        timeLabel = DesignClasses.makeJLabelUnderFullName("");
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH - 10, 10));
        timePanel.add(timeLabel);
        friendInfoPanel.add(timePanel, BorderLayout.SOUTH);

        add(friendInfoPanel, BorderLayout.CENTER);

        MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().put(contactId, this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeAllInformation();
            }
        });
    }

    public void changeAllInformation() {
        changeFriendProfileImage();
        changeFullName();
        changeNotificationLabel();
        changeIconPanel();
        changeTimeLabel();
    }

    public void changeStatus() {
        changeNotificationLabel();
        changeIconPanel();
        changeTimeLabel();
    }

    public void clearNotificationLabel() {
        lastMessageLabel.setForeground(DefaultSettings.text_color2);
        lastMessagePanel.revalidate();
    }

    public void changeFriendProfileImage() {
        try {
            if (recentDTO.getContactType() == RecentContactDAO.TYPE_FRIEND) {
                UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(contactId);
                if (friendProfileInfo != null) {
                    short[] friendPrivacy = new short[5];
                    if (friendProfileInfo.getPrivacy() != null) {
                        friendPrivacy = friendProfileInfo.getPrivacy();
                    }
                    short profileImagePrivacy = friendPrivacy[2];
                    if (friendProfileInfo.getProfileImage() != null
                            && friendProfileInfo.getProfileImage().trim().length() > 0
                            && profileImagePrivacy > 0
                            && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) {
                        ImageHelpers.addProfileImageThumb(frienImageLabel, friendProfileInfo.getProfileImage());
                    } else {
                        BufferedImage img = ImageHelpers.getUnknownImage(true);
                        frienImageLabel.setIcon(new ImageIcon(img));
                    }
                }
            } else if (recentDTO.getContactType() == RecentContactDAO.TYPE_GROUP) {
                DesignClasses.setGroupProfileImage(frienImageLabel, Long.valueOf(contactId));
            }
        } catch (Exception ex) {
        }
        frienImageLabel.revalidate();
    }

    public void changeFullName() {
        try {
            if (recentDTO.getContactType() == RecentContactDAO.TYPE_FRIEND) {
                UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(contactId);
                fullNameLabel.setText(HelperMethods.getUserFullName(friendProfileInfo));
                //   fullNameLabel.setToolTipText(HelperMethods.getUserFullName(friendProfileInfo));
            } else if (recentDTO.getContactType() == RecentContactDAO.TYPE_GROUP) {
                long groupId = 0;
                try {
                    groupId = Long.parseLong(contactId);
                } catch (Exception ex) {
                }
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                String fullName = "No Name";
                if (groupInfo != null) {
                    fullName = groupInfo.getGroupName();
                }
                fullNameLabel.setText(fullName);
                //  fullNameLabel.setToolTipText(fullName);
            }
            fullNameLabel.revalidate();
        } catch (Exception e) {
        }
    }

    public String setMessageTypeLabel(MessageDTO m) {
        String msg = m.getMessage();
        int msgType = m.getMessageType();
        if (msgType == ChatConstants.TYPE_DOWNLOADED_STICKER_MSG) {
            msg = "Sticker Message";
        } else if (msgType == ChatConstants.TYPE_IMAGE_FILE_DIRECTORY) {
            msg = "Picture Message";
        } else if (msgType == ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA) {
            msg = "Ring Picture Message";
        } else if (msgType == ChatConstants.TYPE_AUDIO_FILE) {
            msg = "Audio Message";
        } else if (msgType == ChatConstants.TYPE_VIDEO_FILE) {
            msg = "Video Message";
        } else if (msgType == ChatConstants.TYPE_STREAM_FILE) {
            msg = msg.split("/")[1];
        }
        return msg;
    }

    public void changeNotificationLabel() {
        lastMessageLabel.setText("");
        RecentDTO recent = ChatHashMap.getInstance().getChatUnreadMessages().get(contactId);
        if (recent != null) {
            if (recent.getMessageDTO() != null) {
                String msg = setMessageTypeLabel(recent.getMessageDTO());
                lastMessageLabel.setText(msg);
            }
        } else {
            if (recentDTO != null) {
                if (recentDTO.getMessageDTO() != null) {
                    String msg = setMessageTypeLabel(recentDTO.getMessageDTO());
                    lastMessageLabel.setText(msg);
                }
            }
        }
        if (ChatHashMap.getInstance().getChatUnreadMessages().get(contactId) == null) {
            lastMessageLabel.setForeground(DefaultSettings.text_color2);
        } else {
            lastMessageLabel.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        }

        lastMessagePanel.revalidate();
    }

    public void changeIconPanel() {
        iconPanel.removeAll();
        if (recentDTO.getMessageDTO().getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            iconPanel.add(outgoingLabel);
            //  iconPanel.setToolTipText("Outgoing");
        } else {
            iconPanel.add(incomingLabel);
            // iconPanel.setToolTipText("Incoming");
        }
    }

    public void changeTimeLabel() {
        timeLabel.setText(FeedUtils.getShowableDateForLeftPane(recentDTO.getTime()));
        timeLabel.setToolTipText(FeedUtils.getActualDate(recentDTO.getTime()));
    }

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            MyFriendChatCallPanel myFriendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(contactId);
            if (myFriendChatCallPanel != null && myFriendChatCallPanel.isDisplayable() && myFriendChatCallPanel.isVisible()) {
                // do nothing;
            } else {
                changeNotificationLabel();
                if (recentDTO.getContactType() == RecentContactDAO.TYPE_FRIEND) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(contactId, MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL);
                } else if (recentDTO.getContactType() == RecentContactDAO.TYPE_GROUP) {
                    long groupId = 0;
                    try {
                        groupId = Long.parseLong(contactId);
                    } catch (Exception ex) {
                    }
                    GuiRingID.getInstance().getMainRight().showGroupPanel(groupId);
                }
            }
            ChatHistoryContainer.setChatSelectionColor(currInstance);
        }
    };
}

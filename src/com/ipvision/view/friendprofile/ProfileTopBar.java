/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.desktopCall.dtos.CallSettingsDTo;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.call.MainClass;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;

/**
 *
 * @author Faiz
 */
public class ProfileTopBar extends JPanel implements ActionListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileTopBar.class);
    MyFriendChatCallPanel myFriendChatCallPanel;
    private final JLabel profilePictureLabel;// = new JLabel();
    private final JPanel leftInfoPanel;
    private final JPanel rightInfoPanel;
    private JLabel friendFullNameLable;
    private JLabel friendOnlineStatusLable;
    // private JLabel friendLastOnlineTime;
    private final JButton btnCallToRingID;
    private final JButton btnVideoCall;
    private ImagePane profilePicPanel;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public ProfileTopBar(final MyFriendChatCallPanel myFriendChatCallPanel) {
        this.myFriendChatCallPanel = myFriendChatCallPanel;
        profilePictureLabel = new JLabel();
        setPreferredSize(new Dimension(0, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        setBackground(RingColorCode.SECOND_BAR_BG_COLOR);
        setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE, 0, 0));
        setLayout(new BorderLayout());

        leftInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftInfoPanel.setOpaque(false);
        add(leftInfoPanel, BorderLayout.WEST);

        rightInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightInfoPanel.setOpaque(false);
        JPanel holderRight = new JPanel(new BorderLayout());
        holderRight.setBorder(new EmptyBorder(3, 0, 0, 10));
        holderRight.setOpaque(false);
        holderRight.add(rightInfoPanel, BorderLayout.CENTER);
        add(holderRight, BorderLayout.EAST);

        btnCallToRingID = DesignClasses.createImageButtonWithIcons(ImageObjects.voiceCallImage, ImageObjects.voiceCallImage_h, "Call to " + myFriendChatCallPanel.getFriendProfileInfo().getFullName());
        btnCallToRingID.addActionListener(this);
        btnVideoCall = DesignClasses.createImageButtonWithIcons(ImageObjects.vedioCallImage, ImageObjects.vedioCallImage_h, "Video call ");
        btnVideoCall.addActionListener(this);

        addProfilepicturcircleLabel();
        addUserInfoStructure();
    }

    private void addProfilepicturcircleLabel() {
        profilePicPanel = new ImagePane();
        profilePicPanel.setLayout(null);
        profilePicPanel.setOpaque(false);
        try {
            profilePicPanel.setImage(ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC)));
        } catch (IOException ex) {
            log.error("PROFILE_PICTUR_CIRCLE error-->" + ex.getMessage());
        }
        profilePicPanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        profilePictureLabel.setBounds(4, 1, 40, 40);
        profilePicPanel.add(profilePictureLabel);
        profilePicPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profilePicPanel.setToolTipText("View " + myFriendChatCallPanel.getFriendProfileInfo().getFullName() + "'s Profile");
        profilePicPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                myFriendChatCallPanel.myfriendSlider.previous();
            }
        });
    }

    private void addUserInfoStructure() {
        String ringId = HelperMethods.getRingID(this.myFriendChatCallPanel.friendIdentity);

        JPanel friendInfo = new JPanel();
        friendInfo.setBorder(new EmptyBorder(6, 10, 0, 0));
        friendInfo.setLayout(new BorderLayout());
        friendInfo.setOpaque(false);
        leftInfoPanel.add(friendInfo);

        JPanel friendInfoWrapper = new JPanel();
        friendInfoWrapper.setOpaque(false);
        friendInfoWrapper.setLayout(new BoxLayout(friendInfoWrapper, BoxLayout.Y_AXIS));
        friendInfo.add(friendInfoWrapper);

        friendFullNameLable = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
        friendFullNameLable.setToolTipText(ringId);
        friendInfoWrapper.add(friendFullNameLable);

        friendOnlineStatusLable = new JLabel("");
        friendOnlineStatusLable.setBorder(new EmptyBorder(0, 19, 0, 0));
        friendOnlineStatusLable.setToolTipText(ringId);
        friendOnlineStatusLable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        friendOnlineStatusLable.setForeground(RingColorCode.FEED_UPDATED_STRING_COLOR);
        friendInfoWrapper.add(friendOnlineStatusLable);

    }

    public void buildButtons(Integer frndStatus) {
        rightInfoPanel.removeAll();
        if (frndStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED.intValue()) {
            rightInfoPanel.add(btnCallToRingID);
            rightInfoPanel.add(btnVideoCall);
        }
        rightInfoPanel.add(profilePicPanel);
        rightInfoPanel.revalidate();
        rightInfoPanel.repaint();
    }

    public void buildLastOnlineStatus() {
        try {
            friendOnlineStatusLable.setForeground(RingColorCode.FEED_UPDATED_STRING_COLOR);
            if (myFriendChatCallPanel.getFriendProfileInfo().getLastOnlineTime() != null
                    && myFriendChatCallPanel.getFriendProfileInfo().getLastOnlineTime() > 0
                    && myFriendChatCallPanel.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                    && myFriendChatCallPanel.getFriendProfileInfo().getPresence() == StatusConstants.PRESENCE_OFFLINE) {
                long diff = (new Date().getTime()) - myFriendChatCallPanel.getFriendProfileInfo().getLastOnlineTime();
                if (diff >= SECOND) {
                    StringBuilder text = new StringBuilder("Last online ");
                    if (diff > DAY) {
                        text.append(diff / DAY).append("d ");
                        diff %= DAY;
                    } else if (diff > HOUR) {
                        text.append(diff / HOUR).append("h ");
                        diff %= HOUR;
                    } else if (diff > MINUTE) {
                        text.append(diff / MINUTE).append("m ");
                        diff %= MINUTE;
                    } else if (diff > SECOND) {
                        text.append(diff / SECOND).append("s ");
                        diff %= SECOND;
                    }
                    text.append("ago");
                    friendOnlineStatusLable.setText(text.toString());
                }
            } else if (myFriendChatCallPanel.getFriendProfileInfo().getFriendShipStatus().intValue() != StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                    || myFriendChatCallPanel.getFriendProfileInfo().getPresence() == StatusConstants.PRESENCE_OFFLINE
                    || myFriendChatCallPanel.getFriendProfileInfo().getPresence() == StatusConstants.PRESENCE_DO_NOT_DISTURB) {
                friendOnlineStatusLable.setText("Offline");
            } else if (myFriendChatCallPanel.getFriendProfileInfo().getPresence() == StatusConstants.PRESENCE_ONLINE) {
                friendOnlineStatusLable.setText("Online");
                friendOnlineStatusLable.setForeground(new Color(0x0ad351));
            } else if (myFriendChatCallPanel.getFriendProfileInfo().getPresence() == StatusConstants.PRESENCE_AWAY) {
                friendOnlineStatusLable.setText("Away");
            }

            String status_image = HelperMethods.getStatusIcon(myFriendChatCallPanel.getFriendProfileInfo());
            friendFullNameLable.setIcon(DesignClasses.return_image(status_image));
        } catch (Exception e) {
        }
    }

    public void buildProfileImage() {
        ImageHelpers.addProfileImageThumb(profilePictureLabel, myFriendChatCallPanel.getFriendProfileInfo().getProfileImage(), 39, 1, RingColorCode.DEFAULT_FORGROUND_COLOR);
        //ImageHelpers.addProfileImageThumb(profilePictureLabel, myFriendChatCallPanel.getFriendProfileInfo().getProfileImage());
    }

    public void buildFullNameAndRingID() {
        if (friendFullNameLable != null) {
            if (this.myFriendChatCallPanel.getFriendProfileInfo() != null) {
                if ((this.myFriendChatCallPanel.getFriendProfileInfo().getFullName() != null)) {
                    friendFullNameLable.setText(this.myFriendChatCallPanel.getFriendProfileInfo().getFullName());
                } else {
                    friendFullNameLable.setText(this.myFriendChatCallPanel.friendIdentity);
                }
            }
        }

//        if (friendRingIDLable != null) {
//            String ringId = HelperMethods.getRingID(this.myFriendChatCallPanel.friendIdentity);
//            friendRingIDLable.setToolTipText(ringId);
//            friendRingIDLable.setText(ringId);
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCallToRingID) {
            if (MainClass.getMainClass() == null) {
                CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                callSettingsDTo.setFndId(myFriendChatCallPanel.getFriendProfileInfo().getUserIdentity());
                callSettingsDTo.setFn(myFriendChatCallPanel.getFriendProfileInfo().getFullName());
                callSettingsDTo.setPrIm(myFriendChatCallPanel.getFriendProfileInfo().getProfileImage());
                callSettingsDTo.setIncomming(0);
                MainClass mainClass2 = new MainClass(callSettingsDTo);
                mainClass2.showWindow();
            } else {
                MainClass.getMainClass().showWindow();
            }
        } else if (e.getSource() == btnVideoCall) {
            HelperMethods.showWarningDialogMessage(NotificationMessages.VIDEO_CALL_NOTIFICATION);
        }
    }
}

package com.ipvision.view.addfriend;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.image.ImageObjects;
import java.awt.event.MouseListener;

/**
 *
 * @author Sirat Samyoun
 */
public class SingleAddFriendPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleAddFriendPanel.class);
    public String userIdentity;
    private JLabel frienImageLabel;
    private JLabel fullNameLabel;
    private JPanel friendShipStatusPanel;
    public JLabel friendShipStatusLabel;
    public JPanel friendInfoPanel;
    public JLabel friendImageIconLabel;
    public JPanel friendIconImage;
    public UserBasicInfo userBasicInfo;
    public JPanel fullNameWhatInmindWrapper;
    public SingleActionPanel buttonActionPanel;
    private int thumbImageSize = 35;
    public MouseListener mouseListener;

    public SingleAddFriendPanel(UserBasicInfo userBasicInfo) {
        this.userBasicInfo = userBasicInfo;
        this.userIdentity = userBasicInfo.getUserIdentity();
        this.setLayout(new BorderLayout());
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        friendInfoPanel = new JPanel(new BorderLayout());
        friendInfoPanel.setPreferredSize(new Dimension(380, 61));
        friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        friendInfoPanel.setBorder(new EmptyBorder(5, 60, 0, 60));
        add(friendInfoPanel, BorderLayout.NORTH);
        friendInfoPanel.setVisible(true);
        buttonActionPanel = new SingleActionPanel(userBasicInfo, 0);
        add(buttonActionPanel, BorderLayout.CENTER);
        buttonActionPanel.setVisible(false);

        JPanel friendImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 8));
        //friendImagePanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + DefaultSettings.VGAP_5, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        frienImageLabel = new JLabel();
        friendImagePanel.add(frienImageLabel);
        friendImagePanel.setOpaque(false);

        fullNameLabel = DesignClasses.makeJLabelFullName("", 13);
        fullNameLabel.setPreferredSize(new Dimension(230, 15));
        friendShipStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        friendShipStatusPanel.setBorder(new EmptyBorder(4, 0, 0, 0));
        friendShipStatusPanel.setOpaque(false);
        friendShipStatusLabel = DesignClasses.makeJLabelUnderFullName("");
        if (userBasicInfo.getNumberOfMutualFriends() != null && userBasicInfo.getNumberOfMutualFriends() > 0) {
            if (userBasicInfo.getNumberOfMutualFriends() == 1) {
                friendShipStatusLabel.setText("1 Mutual Friend");
            } else {
                friendShipStatusLabel.setText(userBasicInfo.getNumberOfMutualFriends() + " Mutual Friends");
            }
            friendShipStatusPanel.setVisible(true);
        }
        friendShipStatusPanel.add(friendShipStatusLabel);
        fullNameWhatInmindWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fullNameWhatInmindWrapper.setBorder(new EmptyBorder(15, 7, 0, 0));
        fullNameWhatInmindWrapper.setBackground(Color.GREEN);
        fullNameWhatInmindWrapper.setOpaque(false);
        JPanel fullNameWhatInmindPanel = new JPanel(new BorderLayout(0, 0));
        fullNameWhatInmindPanel.setOpaque(false);
        fullNameWhatInmindPanel.add(fullNameLabel, BorderLayout.CENTER);
        fullNameWhatInmindPanel.add(friendShipStatusPanel, BorderLayout.SOUTH);
        fullNameWhatInmindWrapper.add(fullNameWhatInmindPanel);

        friendIconImage = new JPanel(new BorderLayout());
        friendIconImage.setOpaque(false);
        friendIconImage.setBorder(new EmptyBorder(0, 0, 0, 5));
        friendImageIconLabel = new JLabel();
        friendIconImage.add(friendImageIconLabel, BorderLayout.CENTER);
        //friendImageIconLabel.addMouseListener(this);

        friendInfoPanel.add(friendImagePanel, BorderLayout.WEST);
        friendInfoPanel.add(fullNameWhatInmindWrapper, BorderLayout.CENTER);
        friendInfoPanel.add(friendIconImage, BorderLayout.EAST);

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        changeFriendAllInformation();
//            }
//        });
    }

    public void changeUnknownInformation() {
        if (userBasicInfo.getProfileImage() != null) {
            ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, userBasicInfo.getProfileImage(), thumbImageSize, HelperMethods.getShortName(userBasicInfo.getFullName()));
        } else {
            BufferedImage img = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(userBasicInfo.getFullName()));//ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
            frienImageLabel.setIcon(new ImageIcon(img));
            img = null;
        }
        if ((userBasicInfo.getFullName() != null && userBasicInfo.getFullName().trim().length() > 0)) {
            fullNameLabel.setText(HelperMethods.getUserFullName(userBasicInfo));
            fullNameLabel.revalidate();
        }
    }

    public void changeFriendAllInformation() {
        if (FriendList.getInstance().getFriend_hash_map().get(userIdentity) != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    changeFriendProfileImage();
                }
            });
            changeFullName();
        } else {
            changeUnknownInformation();
        }
        changeFriendShipStatus();
        setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
    }

    public void changeFriendProfileImage() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            short[] friendPrivacy = new short[5];
            if (friendProfileInfo.getPrivacy() != null) {
                friendPrivacy = friendProfileInfo.getPrivacy();
            }
            short profileImagePrivacy = friendPrivacy[2];
            if (friendProfileInfo.getProfileImage() != null
                    && friendProfileInfo.getProfileImage().trim().length() > 0
                    && profileImagePrivacy > 0
                    && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) {
                ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, friendProfileInfo.getProfileImage(), thumbImageSize, HelperMethods.getShortName(userBasicInfo.getFullName()));
            } else {
                BufferedImage img = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(userBasicInfo.getFullName()));//ImageHelpers.getUnknownImage(true);
                frienImageLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ex) {
        }
        frienImageLabel.revalidate();
    }

    public void changeFullName() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            fullNameLabel.setText(HelperMethods.getUserFullName(friendProfileInfo));
            fullNameLabel.revalidate();
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in changeFullName ==>" + e.getMessage());
        }
    }

    public void changeFriendShipStatus() {
        try {

            friendShipStatusLabel.setText("");
            // friendShipStatusPanel.setVisible(false);
            // friendIconImage.setVisible(false);
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            if (friendProfileInfo != null) {
                if (friendProfileInfo.getBlocked() != null && friendProfileInfo.getBlocked() > 0) {
                    if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL) {
                        friendImageIconLabel.setIcon(ImageObjects.friend_call_chat_access);
                    } else if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
                        friendImageIconLabel.setIcon(ImageObjects.friend_full_access_default);
                    }
                    fullNameWhatInmindWrapper.setBorder(new EmptyBorder(10, 5, 0, 0));
                    //friendIconImage.setVisible(true);
                    //friendShipStatusLabel.setText("Blocked");
                    // friendShipStatusPanel.setVisible(true);
                } else if (friendProfileInfo.getFriendShipStatus() != null && friendProfileInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
                    /*                    if (friendProfileInfo.getUt() > 0) {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(friendProfileInfo.getUt()));
                     } else {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(System.currentTimeMillis()));
                     }*/
                    //friendShipStatusPanel.setVisible(true);
                    friendImageIconLabel.setIcon(ImageObjects.incomingIcon);
                    friendImageIconLabel.setToolTipText("Incoming Request");
                    //friendIconImage.setVisible(true);
                } else if (friendProfileInfo.getFriendShipStatus() != null && friendProfileInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_PENDING) {
                    /*                    if (friendProfileInfo.getUt() > 0) {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(friendProfileInfo.getUt()));
                     } else {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(System.currentTimeMillis()));
                     }*/
                    // friendShipStatusPanel.setVisible(true);
                    friendImageIconLabel.setIcon(ImageObjects.outgoingIcon);
                    friendImageIconLabel.setToolTipText("Outgoing Request");
                    // friendIconImage.setVisible(true);
                } else if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL) {
                    friendImageIconLabel.setIcon(ImageObjects.friend_call_chat_access);
                    fullNameWhatInmindWrapper.setBorder(new EmptyBorder(17, 5, 0, 0));
                    //friendIconImage.setVisible(true);
                } else if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
                    friendImageIconLabel.setIcon(ImageObjects.friend_full_access_default);
                    fullNameWhatInmindWrapper.setBorder(new EmptyBorder(17, 5, 0, 0));
                    //friendIconImage.setVisible(true);
                }
                if (friendProfileInfo.getNewContactType() != null && friendProfileInfo.getNewContactType() > 0) {
                    /*                    if (friendProfileInfo.getUt() > 0) {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(friendProfileInfo.getUt()));
                     } else {
                     friendShipStatusLabel.setText(FeedUtils.getDateForPending(System.currentTimeMillis()));
                     }*/
                    //friendShipStatusPanel.setVisible(true);

                    friendImageIconLabel.setIcon(ImageObjects.friend_call_chat_access);
                    if (friendProfileInfo.getIsChangeRequester()) {
                        friendImageIconLabel.setToolTipText("Access Change Request Sent");
                    } else {
                        friendImageIconLabel.setToolTipText("Access Change Request Received");
                    }
                    fullNameWhatInmindWrapper.setBorder(new EmptyBorder(10, 5, 0, 0));
                    //friendIconImage.setVisible(true);
                }
            } else {
                friendImageIconLabel.setIcon(ImageObjects.addIcon);
                if (userBasicInfo.getNumberOfMutualFriends() != null && userBasicInfo.getNumberOfMutualFriends() > 0) {
                    if (userBasicInfo.getNumberOfMutualFriends() == 1) {
                        friendShipStatusLabel.setText("1 Mutual Friend");
                    } else {
                        friendShipStatusLabel.setText(userBasicInfo.getNumberOfMutualFriends() + " Mutual Friends");
                    }
                    // friendShipStatusPanel.setVisible(true);
                }
                friendIconImage.setVisible(true);
                friendImageIconLabel.setToolTipText("Add as Friend");
            }
            friendShipStatusPanel.revalidate();
            friendShipStatusPanel.repaint();
            friendIconImage.revalidate();
            friendIconImage.repaint();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in SingleAddFriendPanelr class ==>" + e.getMessage());
        }
    }
}

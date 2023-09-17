/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.desktopCall.dtos.CallSettingsDTo;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.ipvision.service.AcceptFriendAccess;
import com.ipvision.service.AcceptFriendRequest;
import com.ipvision.service.BlockUnblockFriend;
import com.ipvision.service.ChangeFriendAccess;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.DeleteFreind;
import com.ipvision.service.FriendDetailsInfoRequest;
import com.ipvision.service.FriendPresenceInfo;
import com.ipvision.view.image.LoadCoverImageInLabel;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.call.MainClass;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.image.ImagePaneForCoverImage;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.ProfilePicBigPanel;
import com.ipvision.view.image.ViewProfileImage;
import com.ipvision.view.utility.MenuPanel;
import com.ipvision.view.utility.SeparatorPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Faiz Ahmed
 */
public class MyFriendProfile extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyFriendProfile.class);
    private JLabel friendFullNameLable;
    private JLabel friendRingIDLable;
    private JLabel statusLabel;
    public JButton btnAcceptRequest;
    private JButton btnAcceptAccessRequest;
    private JPanel tabContainer;
    private JPanel rightTabPanel;
    private JPanel tabPanel;
    public JPanel freindPan = new JPanel();
    private JPanel friendImageWrapper;
    private JPanel mainContentPanel;
    public MyFriendContactListPane myFriendContactListPane;
    public MyFriendNewsFeedPanel myFriendNewsFeedPanel;
    public MyFriendAlbumPanel myFriendAlbumPanel;
    public MyFriendAboutPanel myFriendAboutPanel;
    private UserBasicInfo friendProfileInfo;
    private String userIdentity;
    private static String seleected_user_identiy;
    private JButton btnChat;
    private JButton btnCallToRingID;
    private JButton btnVideoCall;
    public static final int TYPE_BOOK = 1;
    public static final int TYPE_CONTACTLIST = 2;
    public static final int TYPE_ALBUM = 3;
    public static final int TYPE_ABOUT = 4;
    public static final String MENU_TYPE_BOOK = "Post";
    public static final String MENU_TYPE_CONTACTLIST = "Friends";
    public static final String MENU_TYPE_ALBUM = "Photos";
    public static final String MENU_TYPE_ABOUT = "About";

    private MenuPanel aboutButton = new MenuPanel(MENU_TYPE_ABOUT, 70, 48);
    private MenuPanel bookButton = new MenuPanel(MENU_TYPE_BOOK, 70, 48);
    private MenuPanel contactListButton = new MenuPanel(MENU_TYPE_CONTACTLIST, 70, 48);
    private MenuPanel albumButton = new MenuPanel(MENU_TYPE_ALBUM, 70, 48);
    private int type = TYPE_BOOK;
    private JButton actionPopUpButton;
    private JMenuItem acceptRequest, deleteRequest, allowAccess, denyAccess;
    private JPopupMenu actionPopUp = new JPopupMenu();
    private JPopupMenu friendRequestPopUp, accessRequestPopUp;
    private JMenuItem mnuRemove;
    private JMenuItem mnuBlock;
    private JMenuItem mnuUnblock;
    private JMenuItem mnuChangeAccess;
    private ImagePaneForCoverImage coverImagePanel = new ImagePaneForCoverImage();
    private JLabel profilePictureLabel = new JLabel();
    private ImageMousListenerInBook profileImageClickListener;
    public String lastChatSenderIdentity = null;
    private ImageMousListenerInBook coverImageClickListener;
    int flag = 0;
    private JPanel loadingPanel;
    private JPanel container;
    public MyfriendSlider myfriendSlider;
    private JLabel blockImageLabel;
    private JPanel blockImagePanel;
    private JPanel statusPanel;
    JPanel myNamePanel;
    JPanel nameStatusWrapperPanel;

    public JPanel tabAndProfilelPanelWrapper;
    public JPanel scrollPnlTop;

    public JPanel getMainContentPanel() {
        return mainContentPanel;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyFriendProfile(String userIdentity, MyfriendSlider myfriendSlider) {
        this.myfriendSlider = myfriendSlider;
        this.userIdentity = userIdentity;
        setSeleected_user_identiy(userIdentity);
        setFriendProfileInfo(this.userIdentity);

        try {
            ImageObjects.initImageObjects();
            btnCallToRingID = DesignClasses.createImageButtonWithIcons(ImageObjects.voiceCallImage, ImageObjects.voiceCallImage_h, "Call to " + this.friendProfileInfo.getFullName());
            btnVideoCall = DesignClasses.createImageButtonWithIcons(ImageObjects.vedioCallImage, ImageObjects.vedioCallImage_h, "Video call ");
            btnChat = DesignClasses.createImageButtonWithIcons(ImageObjects.chatImage, ImageObjects.chatImage_h, "Chat with " + friendProfileInfo.getFullName());
        } catch (Exception e) {
            log.error("can not make call chat buttons");
        }

        //initContainers();
    }

    public void initContainers() {
        ImageObjects.initImageObjects();
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        try {

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);

            scrollPnlTop = new JPanel();
            scrollPnlTop.setOpaque(false);
            scrollPnlTop.setPreferredSize(new Dimension(11, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT + 35));
            wrapper.add(scrollPnlTop, BorderLayout.EAST);

            tabAndProfilelPanelWrapper = new JPanel();
            tabAndProfilelPanelWrapper.setLayout(new BoxLayout(tabAndProfilelPanelWrapper, BoxLayout.Y_AXIS));
            tabAndProfilelPanelWrapper.setOpaque(false);

            wrapper.add(tabAndProfilelPanelWrapper, BorderLayout.CENTER);
            add(wrapper, BorderLayout.NORTH);
            addProfileCoverOptions();

            tabContainer = new JPanel(new BorderLayout());
            tabContainer.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 50));
            tabContainer.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
            tabContainer.setBackground(Color.WHITE);

            tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            tabPanel.setOpaque(false);
            tabContainer.add(tabPanel, BorderLayout.WEST);

            rightTabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            rightTabPanel.setOpaque(false);
            tabContainer.add(rightTabPanel, BorderLayout.EAST);

            JPanel tabWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            tabWrapperPanel.setOpaque(false);
            tabWrapperPanel.add(tabContainer);

            tabAndProfilelPanelWrapper.add(tabWrapperPanel);

            buildTabPanel();
            buildRightTablPanel();
            buildFullName();
            //buildStatusImage();
            buildFrinedImage();

            container = new JPanel(new BorderLayout(0, 0));
            container.setOpaque(false);
            add(container, BorderLayout.CENTER);

            JPanel masterPanel = new JPanel();
            masterPanel.setOpaque(false);
            masterPanel.setLayout(new BorderLayout(0, 0));
            mainContentPanel = new JPanel(new BorderLayout(0, 0));
            mainContentPanel.setOpaque(false);
            masterPanel.add(mainContentPanel, BorderLayout.CENTER);
            container.add(masterPanel, BorderLayout.CENTER);

            btnCallToRingID.addActionListener(actionListener);
            btnVideoCall.addActionListener(actionListener);
            btnChat.addActionListener(actionListener);
            bookButton.addMouseListener(mouseListener);
            albumButton.addMouseListener(mouseListener);
            contactListButton.addMouseListener(mouseListener);
            aboutButton.addMouseListener(mouseListener);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    buildPopUpMenu();
                }
            }).start();
            if (this.friendProfileInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                    && this.friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
                setType(TYPE_BOOK);
            } else {
                setType(TYPE_ABOUT);
            }
            //onTabClick();
        } catch (Exception e) {
        }
    }

    public void loadPresenceStatus() {
        new FriendPresenceInfo(friendProfileInfo.getUserIdentity()).start();
    }

    private void buildTabPanel() {
        try {
            int friendShipStatus = this.friendProfileInfo.getFriendShipStatus();
            if (tabPanel != null) {
                tabPanel.removeAll();
                if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                    if (this.friendProfileInfo.getContactType() != StatusConstants.ACCESS_CHAT_CALL) {
                        tabPanel.add(bookButton);
                        tabPanel.add(new SeparatorPanel(1, 48, 8));
                    }
                    tabPanel.add(aboutButton);
                    tabPanel.add(new SeparatorPanel(1, 48, 8));
                    tabPanel.add(contactListButton);

                    if (this.friendProfileInfo.getContactType() != StatusConstants.ACCESS_CHAT_CALL) {
                        tabPanel.add(new SeparatorPanel(1, 48, 8));
                        tabPanel.add(albumButton);
                    }
                } else {
                    tabPanel.add(aboutButton);
                }

                tabPanel.revalidate();
                tabPanel.repaint();
            }
        } catch (Exception ex) {
        }

    }

    private JMenuItem getMenuItem(String title, String imageUrl) {
        JMenuItem jMenuItem = new JMenuItem(title);
        jMenuItem.setIcon(DesignClasses.return_image(imageUrl));
        jMenuItem.setPreferredSize(new Dimension(180, 35));
        jMenuItem.setBorder(null);
        jMenuItem.setOpaque(true);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    public JPanel getCustomSeperator() {
        JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        seperator.setBackground(new Color(0xe5e6e9));
        seperator.setPreferredSize(new Dimension(1, 1));
        return seperator;
    }

    private void addProfileCoverOptions() {
        coverImagePanel.setOpaque(false);
        coverImagePanel.setLayout(new BorderLayout());
        //coverImagePanel.setBorder(new EmptyBorder(0, 15, 0, 15));

        JPanel panel = new JPanel(new BorderLayout(0, 0));
        //panel.setBackground(Color.WHITE);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT));
        panel.add(coverImagePanel);
        panel.setBorder(new MatteBorder(0, 1, 0, 1, RingColorCode.FEED_BORDER_COLOR));

        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        arrowPanel.setOpaque(false);
        arrowPanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT, 0));
        actionPopUpButton = DesignClasses.createImageButton(GetImages.SETTING_MINI, GetImages.SETTING_MINI_H, "Options");
        arrowPanel.add(actionPopUpButton);
        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // setPopUpMenuItemVisibility();
                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 145, actionPopUp.getY() + 10);
            }
        });
        //  buildPopUpMenu();
        coverImagePanel.add(arrowPanel, BorderLayout.EAST);

//        ImagePane profilePicPanel = new ImagePane();
//        profilePicPanel.setLayout(null);
//        //profilePicPanel.setBackground(new Color(0, 0, 0, 0));
//        profilePicPanel.setOpaque(false);
//        try {
//            profilePicPanel.setImage(ImageIO.read(new Object() {
//            }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC_BIG)));
//        } catch (IOException ex) {
//            log.error("Buffer img error-->" + ex.getMessage());
//        }
//        profilePicPanel.setPreferredSize(new Dimension(150, 150));
//        profilePictureLabel.setBounds(29, 15, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
//        profilePicPanel.add(profilePictureLabel);
//        coverImagePanel.add(profilePicPanel, BorderLayout.WEST);
        ProfilePicBigPanel profilePicBigPanel = new ProfilePicBigPanel(profilePictureLabel);
        JPanel profileImageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        profileImageWrapper.setBorder(new EmptyBorder(38, 6, 0, 0));
        profileImageWrapper.setBackground(Color.GREEN);
        profileImageWrapper.setOpaque(false);
        profileImageWrapper.add(profilePicBigPanel);
        coverImagePanel.add(profileImageWrapper, BorderLayout.WEST);
        //coverImagePanel.add(profilePicBigPanel, BorderLayout.WEST);
        JPanel nameAndLoadingPanel = new JPanel(new BorderLayout());
        nameAndLoadingPanel.setOpaque(false);
//        JPanel nameWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel nameWrapperPanel = new JPanel(new BorderLayout(0, 0));
        nameWrapperPanel.setBorder(new EmptyBorder(69, 1, 27, 0));
        nameWrapperPanel.setBackground(Color.red);
        //nameWrapperPanel.setBorder(new EmptyBorder(90, 1, 50, 0));//80, 1, 10, 0
        nameWrapperPanel.setOpaque(false);
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setBorder(new EmptyBorder(24, 24, 0, 0));
        statusPanel.setBackground(Color.YELLOW);
        statusPanel.setOpaque(false);
        statusLabel = new JLabel();
        buildStatusImage();
        statusPanel.add(statusLabel);
        //nameWrapperPanel.add(statusPanel);
        //nameWrapperPanel.add(statusPanel, BorderLayout.WEST);
        myNamePanel = new JPanel();
        myNamePanel.setLayout(new BoxLayout(myNamePanel, BoxLayout.Y_AXIS));
        myNamePanel.setBorder(new EmptyBorder(22, 0, 28, 0));
        myNamePanel.setOpaque(false);
        //nameWrapperPanel.add(myNamePanel, BorderLayout.CENTER);
        //nameWrapperPanel.add(myNamePanel);

        friendFullNameLable = new JLabel("Friend Profile");
        friendFullNameLable.setForeground(Color.WHITE);
        try {
            Font font = DesignClasses.getDefaultFont(Font.BOLD, 15);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            friendFullNameLable.setFont(font);
        } catch (Exception e) {
        }
        //myNamePanel.add(statusImageLabel);
        JPanel pnlRingName = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlRingName.setOpaque(false);
        pnlRingName.add(friendFullNameLable);
        myNamePanel.add(pnlRingName);
        //myNamePanel.add(friendFullNameLable);
        friendRingIDLable = DesignClasses.makeJLabel_normal("Friend ringID", 13, Color.WHITE);
        JPanel pnlRingId = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlRingId.setOpaque(false);
        pnlRingId.add(friendRingIDLable);
        myNamePanel.add(pnlRingId);
        //myNamePanel.add(friendRingIDLable);
        blockImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));//new FlowLayout(FlowLayout.LEFT, 0, 0)
        //blockImagePanel.setBorder(new EmptyBorder(0, 35, 0, 0));
        blockImagePanel.setBackground(Color.CYAN);
        blockImagePanel.setOpaque(false);
        blockImageLabel = DesignClasses.makeJLabel_normal("Blocked", 13, Color.WHITE);
        //blockImageLabel.setIcon(DesignClasses.return_image(GetImages.BLOCK_IMAGE_ICON));
        blockImagePanel.add(blockImageLabel);
        myNamePanel.add(blockImagePanel);
        blockImagePanel.setVisible(false);
        if (friendProfileInfo.getBlocked() != null && friendProfileInfo.getBlocked() > 0) {
//            nameWrapperPanel.add(blockImagePanel, BorderLayout.WEST);          
            myNamePanel.setBorder(new EmptyBorder(14, 0, 20, 0));
            blockImagePanel.setVisible(true);
            statusPanel.setBorder(new EmptyBorder(16, 24, 0, 0));
            statusPanel.revalidate();
            statusPanel.repaint();
            myNamePanel.revalidate();
            myNamePanel.repaint();
        }
        nameStatusWrapperPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                Area areaOne = new Area(new Rectangle2D.Double(0, 0, w, h));
                Area areaTwo = new Area(new Ellipse2D.Double(- 96 + 16, - 15, 96, h + 30));
                areaOne.subtract(areaTwo);

                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fill(areaOne);
            }
        };
        nameStatusWrapperPanel.setOpaque(false);
        //nameStatusWrapperPanel.setBorder(new EmptyBorder(70, 1, 0, 0));
        nameStatusWrapperPanel.setLayout(new BorderLayout(0, 0));
        nameWrapperPanel.add(nameStatusWrapperPanel, BorderLayout.CENTER);
        nameStatusWrapperPanel.add(myNamePanel, BorderLayout.CENTER);
        nameStatusWrapperPanel.add(statusPanel, BorderLayout.WEST);
//        nameAndLoadingPanel.add(nameStatusWrapperPanel, BorderLayout.WEST);
//        nameStatusWrapperPanel.add(nameWrapperPanel);
        nameAndLoadingPanel.add(nameWrapperPanel, BorderLayout.WEST);
        loadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadingPanel.setOpaque(false);
        nameAndLoadingPanel.add(loadingPanel, BorderLayout.CENTER);

        coverImagePanel.add(nameAndLoadingPanel, BorderLayout.CENTER);

        friendImageWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        //friendImageWrapper.setBackground(Color.WHITE);
        friendImageWrapper.setOpaque(false);
        friendImageWrapper.add(panel);
        //add(friendImageWrapper, BorderLayout.NORTH);
        tabAndProfilelPanelWrapper.add(friendImageWrapper);

        friendImageWrapper.revalidate();
        friendImageWrapper.repaint();

    }

    public void buildFullName() {
        if (friendFullNameLable != null) {
            if (this.friendProfileInfo != null) {

                /*if ((this.friendProfileInfo.getFullName() != null && this.friendProfileInfo.getFullName().trim().length() > 0) && (this.friendProfileInfo.getLastName() != null && this.friendProfileInfo.getLastName().trim().length() > 0)) {
                 friendFullNameLable.setText(this.friendProfileInfo.getFullName() + " " + this.friendProfileInfo.getLastName());
                 } else*/ if ((this.friendProfileInfo.getFullName() != null && this.friendProfileInfo.getFullName().trim().length() > 0)) {
                    friendFullNameLable.setText(this.friendProfileInfo.getFullName());
                }/* else if ((this.friendProfileInfo.getLastName() != null && this.friendProfileInfo.getLastName().trim().length() > 0)) {
                 friendFullNameLable.setText(this.friendProfileInfo.getLastName());
                 } */ else {
                    friendFullNameLable.setText(HelperMethods.getRingID(this.friendProfileInfo.getUserIdentity()));
                }
            } else {
                friendFullNameLable.setText("No name");
            }
            friendFullNameLable.revalidate();
        }
        if (friendRingIDLable != null) {
            if (this.friendProfileInfo != null) {
                if (this.friendProfileInfo.getUserIdentity() != null && this.friendProfileInfo.getUserIdentity().trim().length() > 0) {
                    friendRingIDLable.setText("" + HelperMethods.getRingID(this.friendProfileInfo.getUserIdentity()));
                }
            } else {
                friendRingIDLable.setText("No ringID");
            }
            friendRingIDLable.revalidate();
        }
    }

    public void buildStatusImage() {
        try {
            String status_image = HelperMethods.getStatusIcon(this.getFriendProfileInfo());
            statusLabel.setIcon(DesignClasses.return_image(status_image));
        } catch (Exception e) {
        }
    }

    public void buildFrinedImage() {
        buildProfileImage();
        buildCoverImage();
    }

    private void buildProfileImage() {
        profilePictureLabel.setOpaque(false);
        try {
            if (this.friendProfileInfo != null) {
                short[] friendPrivacy = new short[5];
                int friendShipStatus = friendProfileInfo.getFriendShipStatus();
                short profileImagePrivacy = 2;
                if (this.friendProfileInfo.getPrivacy() != null) {
                    friendPrivacy = this.friendProfileInfo.getPrivacy();
                }

                if (friendProfileInfo.getPrivacy() != null) {
                    profileImagePrivacy = friendPrivacy[2];
                }
                //  imgProfile = null;
                //bufferProfileImage = null;
                //  flag = 0;

                profilePictureLabel.removeMouseListener(profileImageClickListener);
                if (this.friendProfileInfo.getProfileImage() != null
                        && this.friendProfileInfo.getProfileImage().trim().length() > 0) {
                    ViewProfileImage viewProfileImage = ImageHelpers.addProfileImageCrop(coverImagePanel, profilePictureLabel, this.friendProfileInfo.getProfileImage(), DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, true);
                    if (profileImagePrivacy > 0 && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED && profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND))) {
                        profilePictureLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        JsonFields imageDto = new JsonFields();
                        imageDto.setUserIdentity(friendProfileInfo.getUserIdentity());
                        imageDto.setFullName(friendProfileInfo.getFullName());
                        //imageDto.setLastName(friendProfileInfo.getLastName());
                        imageDto.setIurl(friendProfileInfo.getProfileImage());
                        imageDto.setImageId(friendProfileInfo.getProfileImageId());
                        imageDto.setImT(2);
                        profileImageClickListener = new ImageMousListenerInBook(imageDto, true, MyFnFSettings.PROFILE_IMAGE, friendProfileInfo.getUserIdentity());
                        profilePictureLabel.addMouseListener(profileImageClickListener);
                        new RemoveInvalidProfileImageListener(viewProfileImage).start();
                    }
                } else {
                    profilePictureLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    profileImageClickListener = new ImageMousListenerInBook(null, true, 0, null);
                    profilePictureLabel.addMouseListener(profileImageClickListener);
                    BufferedImage imgProfile = ImageHelpers.getUnknownImage(false);
                    profilePictureLabel.setIcon(new ImageIcon(imgProfile));
                    imgProfile.flush();
                    imgProfile = null;
                }
                profilePictureLabel.revalidate();
                profilePictureLabel.repaint();
                coverImagePanel.revalidate();
                coverImagePanel.repaint();

            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    class RemoveInvalidProfileImageListener extends Thread {

        ViewProfileImage viewProfileImage = null;

        public RemoveInvalidProfileImageListener(ViewProfileImage viewProfileImage) {
            this.viewProfileImage = viewProfileImage;
        }

        @Override
        public void run() {
            try {
//                while (viewProfileImage.isRunning()) {
//                    Thread.sleep(100);
//                }
                if (HelperMethods.isInvalidDownloadUrl(ImageHelpers.getThumbUrl(friendProfileInfo.getProfileImage()))
                        && HelperMethods.isInvalidDownloadUrl(ImageHelpers.getCropUrl(friendProfileInfo.getProfileImage()))) {
                    profilePictureLabel.removeMouseListener(profileImageClickListener);
                    profilePictureLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    profileImageClickListener = new ImageMousListenerInBook(null, true, 0, null);
                    profilePictureLabel.addMouseListener(profileImageClickListener);
                    profilePictureLabel.revalidate();
                }
            } catch (Exception ex) {

            }
        }
    }

    private void buildCoverImage() {
        if (coverImagePanel != null && this.friendProfileInfo != null) {
            short[] friendPrivacy = new short[5];
            if (this.friendProfileInfo.getPrivacy() != null) {
                friendPrivacy = this.friendProfileInfo.getPrivacy();
            }
            int friendShipStatus = this.friendProfileInfo.getFriendShipStatus();
            short coverImagePrivacy = friendPrivacy[4];
            coverImagePanel.removeMouseListener(coverImageClickListener);
            try {
                BufferedImage img = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                coverImagePanel.setImage(img, 0, 0);
                coverImagePanel.revalidate();
                coverImagePanel.repaint();

                if ((this.friendProfileInfo.getCoverImage() != null
                        && this.friendProfileInfo.getCoverImage().trim().length() > 0)) {
                    JsonFields imageDto = new JsonFields();
                    imageDto.setUserIdentity(this.friendProfileInfo.getUserIdentity());
                    imageDto.setFullName(this.friendProfileInfo.getFullName());
                    //imageDto.setLastName(this.friendProfileInfo.getLastName());
                    imageDto.setIurl(this.friendProfileInfo.getCoverImage());
                    imageDto.setImageId(this.friendProfileInfo.getCoverImageId());
                    imageDto.setImT(3);
                    int type_int;
                    if (coverImagePrivacy > 0 && (coverImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED && coverImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND))) {
                        type_int = 1;
                    } else {
                        type_int = 0;
                    }
                    coverImageClickListener = new ImageMousListenerInBook(imageDto, true, MyFnFSettings.COVER_IMAGE, friendProfileInfo.getUserIdentity());
                    new LoadCoverImageInLabel(type_int, friendImageWrapper, coverImagePanel, loadingPanel, imageDto, this.friendProfileInfo.getCoverImageX(), this.friendProfileInfo.getCoverImageY(), coverImageClickListener).start();
                    coverImagePanel.revalidate();
                    coverImagePanel.repaint();
                } /*else {
                 img = ImageIO.read(new Object() {
                 }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                 coverImagePanel.setImage(img, 0, 0);
                 coverImagePanel.revalidate();
                 coverImagePanel.repaint();
                 }*/

            } catch (IOException ex) {
                log.error("Buffer img error" + ex.getMessage());
            }
        }
    }

    public JButton buildAcceptButton() {
        if (btnAcceptRequest == null) {
            btnAcceptRequest = new RoundedCornerButton("Accept Request", "Accept Friend Request");
            //callPanel.add(btnAcceptRequest);
            btnAcceptRequest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    friendRequestPopUp = new JPopupMenu();
                    acceptRequest = new JMenuItem("Confirm as Friend", DesignClasses.return_image(GetImages.ADD_FRIEND_POPUP));
                    deleteRequest = new JMenuItem("Delete Request", DesignClasses.return_image(GetImages.DELETE));
                    acceptRequest.addActionListener(actionListener);
                    deleteRequest.addActionListener(actionListener);
                    friendRequestPopUp.add(acceptRequest);
                    friendRequestPopUp.add(deleteRequest);
                    friendRequestPopUp.show(btnAcceptRequest, friendRequestPopUp.getX(), friendRequestPopUp.getY() + 30);
                }
            });
        }
        return btnAcceptRequest;
    }

    public void buildRightTablPanel() {
        try {
            if (rightTabPanel != null) {
                rightTabPanel.removeAll();
                int friendShipStatus = this.friendProfileInfo.getFriendShipStatus();
                if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                    if (this.friendProfileInfo.getNewContactType() != null
                            && this.friendProfileInfo.getNewContactType() > 0
                            && (this.friendProfileInfo.getIsChangeRequester() == null || this.friendProfileInfo.getIsChangeRequester() == false)) {
                        final String toText = this.friendProfileInfo.getNewContactType() == StatusConstants.ACCESS_CHAT_CALL ? "Chat & call" : "Full Profile";
                        final String fromText = this.friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL ? "Chat & call" : "Full Profile";
                        btnAcceptAccessRequest = new RoundedCornerButton("Accept Access Request", "Accept access permission change request from '" + fromText + "' to '" + toText + "'");
                        rightTabPanel.add(btnAcceptAccessRequest);
                        btnAcceptAccessRequest.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                accessRequestPopUp = new JPopupMenu();
                                allowAccess = new JMenuItem("Accept Access Request");
                                denyAccess = new JMenuItem("Deny Access Request");
                                allowAccess.addActionListener(actionListener);
                                denyAccess.addActionListener(actionListener);
                                accessRequestPopUp.add(allowAccess);
                                accessRequestPopUp.add(denyAccess);
                                accessRequestPopUp.show(btnAcceptAccessRequest, accessRequestPopUp.getX(), accessRequestPopUp.getY() + 30);
                            }
                        });
                    }
                    rightTabPanel.add(btnCallToRingID);
                    rightTabPanel.add(btnVideoCall);
                } else if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
                    rightTabPanel.add(buildAcceptButton());
                }
                rightTabPanel.add(btnChat);
                rightTabPanel.revalidate();
                rightTabPanel.repaint();
            }
        } catch (Exception e) {
        }
    }

    public void buildPopUpMenu() {
        UIManager.put("Menu.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("Menu.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);
        //UIManager.put("Menu.selectionBackground", RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
        //UIManager.put("Menu.selectionForeground", RingColorCode.RING_TOP_MENU_SELECTED_FG_COLOR);
        UIManager.put("MenuItem.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("MenuItem.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);

        mnuUnblock = getMenuItem("Unblock User", GetImages.BLOCK);

        mnuBlock = getMenuItem("Block User", GetImages.BLOCK);

        mnuChangeAccess = getMenuItem("Change Access", GetImages.CHANGE_ACCESS);

        mnuRemove = getMenuItem("Remove from Contacts", GetImages.DELETE);

        actionPopUp.removeAll();
        JPopupMenu popupMenu = actionPopUp;
        popupMenu.setBorder(null);
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        popupMenu.setBackground(Color.WHITE);

        if (friendProfileInfo != null && friendProfileInfo.getFriendShipStatus() != null && friendProfileInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
            if (friendProfileInfo.getBlocked() != null && friendProfileInfo.getBlocked() > 0) {
                popupMenu.add(mnuUnblock);
                popupMenu.add(getCustomSeperator());
            } else {
                popupMenu.add(mnuBlock);
                popupMenu.add(getCustomSeperator());
            }
            if (friendProfileInfo.getNewContactType() == null || friendProfileInfo.getNewContactType() <= 0) {
                popupMenu.add(mnuChangeAccess);
                popupMenu.add(getCustomSeperator());
            }
        }
        popupMenu.add(mnuRemove);
    }

    /* *******************************************************
     * Utilities Methods
     * *******************************************************/
    public void refreshMyFriendPanel() {
        buildFullName();
        buildStatusImage();
        buildFrinedImage();
        buildRightTablPanel();
        buildTabPanel();
        buildPopUpMenu();
        if (this.friendProfileInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                && this.friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
            setType(TYPE_BOOK);
        } else {
            setType(TYPE_ABOUT);
        }
        onTabClick();
    }

    public void refreshMyFriendInformations() {
        if (myFriendAboutPanel != null) {
            myFriendAboutPanel.loadFriendInformation();
        }
    }

    public void onTabClick() {
        if (mainContentPanel != null) {
            mainContentPanel.removeAll();
            this.setTabSelectionColor(type);
            this.scrollPnlTop.setVisible(false);
            this.tabAndProfilelPanelWrapper.revalidate();
            this.tabAndProfilelPanelWrapper.repaint();

            if (type == MyFriendProfile.TYPE_BOOK) {
                boolean isLoadingFirstTime = false;
                if (NewsFeedMaps.getInstance().getFriendNewsFeedId().get(userIdentity) == null
                        || NewsFeedMaps.getInstance().getFriendNewsFeedId().get(userIdentity).hasDataFromServer() == false) {
                    (new SendFriendsInfoRequest(userIdentity, AppConstants.TYPE_FRIEND_NEWSFEED, 0)).start();
                    isLoadingFirstTime = true;
                }
                if (this.myFriendNewsFeedPanel == null) {
                    this.myFriendNewsFeedPanel = new MyFriendNewsFeedPanel(this);
                    mainContentPanel.add(this.myFriendNewsFeedPanel, BorderLayout.CENTER);
                    if (isLoadingFirstTime) {
                        this.myFriendNewsFeedPanel.addBottomLoading();
                    }
                } else {
                    mainContentPanel.add(this.myFriendNewsFeedPanel, BorderLayout.CENTER);
                }
                if (isLoadingFirstTime == false) {
                    this.myFriendNewsFeedPanel.loadTopForcefully();
                }
                myFriendNewsFeedPanel.scrollContent.getViewport().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (myFriendNewsFeedPanel != null && myFriendNewsFeedPanel.scrollContent.getVerticalScrollBar().isVisible()) {
                            scrollPnlTop.setVisible(true);
                        } else {
                            scrollPnlTop.setVisible(false);
                        }
                        tabAndProfilelPanelWrapper.revalidate();
                        tabAndProfilelPanelWrapper.repaint();
                    }
                });
            } else if (type == MyFriendProfile.TYPE_CONTACTLIST) {
                if (this.myFriendContactListPane == null) {
                    this.myFriendContactListPane = new MyFriendContactListPane(this);
                    mainContentPanel.add(this.myFriendContactListPane, BorderLayout.CENTER);
                } else {
                    mainContentPanel.add(this.myFriendContactListPane, BorderLayout.CENTER);
                }
                if (myFriendContactListPane != null
                        && myFriendContactListPane.freindlistScorllPanel.getVerticalScrollBar() != null
                        && myFriendContactListPane.freindlistScorllPanel.getVerticalScrollBar().isVisible()) {
                    scrollPnlTop.setVisible(true);
                } else {
                    scrollPnlTop.setVisible(false);
                }

                myFriendContactListPane.freindlistScorllPanel.getViewport().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (myFriendContactListPane != null && myFriendContactListPane.freindlistScorllPanel.getVerticalScrollBar().isVisible()) {
                            scrollPnlTop.setVisible(true);
                        } else {
                            scrollPnlTop.setVisible(false);
                        }
                        tabAndProfilelPanelWrapper.revalidate();
                        tabAndProfilelPanelWrapper.repaint();
                    }
                });
            } else if (type == MyFriendProfile.TYPE_ALBUM) {
                if (this.myFriendAlbumPanel == null) {
                    this.myFriendAlbumPanel = new MyFriendAlbumPanel(this);
                    mainContentPanel.add(this.myFriendAlbumPanel, BorderLayout.CENTER);
                } else {
                    mainContentPanel.add(this.myFriendAlbumPanel, BorderLayout.CENTER);
                    myFriendAlbumPanel.tabChange(MyFriendAlbumPanel.TYPE_PHOTOS);
                }
                myFriendAlbumPanel.scrollContent.getViewport().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (myFriendAlbumPanel != null && myFriendAlbumPanel.scrollContent.getVerticalScrollBar().isVisible()) {
                            scrollPnlTop.setVisible(true);
                        } else {
                            scrollPnlTop.setVisible(false);
                        }
                        tabAndProfilelPanelWrapper.revalidate();
                        tabAndProfilelPanelWrapper.repaint();
                    }
                });
            } else if (type == MyFriendProfile.TYPE_ABOUT) {
                if (this.myFriendAboutPanel == null) {
                    new FriendDetailsInfoRequest(this.getUserIdentity()).start();
                    this.myFriendAboutPanel = new MyFriendAboutPanel(this);
                    mainContentPanel.add(this.myFriendAboutPanel, BorderLayout.CENTER);
                } else {
                    mainContentPanel.add(this.myFriendAboutPanel, BorderLayout.CENTER);
                    myFriendAboutPanel.loadFriendInformation();
                }
                myFriendAboutPanel.freindInfoScorllPanel.getViewport().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (myFriendAboutPanel != null && myFriendAboutPanel.freindInfoScorllPanel.getVerticalScrollBar().isVisible()) {
                            scrollPnlTop.setVisible(true);
                        } else {
                            scrollPnlTop.setVisible(false);
                        }
                        tabAndProfilelPanelWrapper.revalidate();
                        tabAndProfilelPanelWrapper.repaint();
                    }
                });
            }
            mainContentPanel.validate();
            mainContentPanel.repaint();
        }
    }

    public void setTabSelectionColor(int type) {
        try {
            if (type == MyFriendProfile.TYPE_BOOK) {
                bookButton.setSelected();
                contactListButton.setExit();
                albumButton.setExit();
                aboutButton.setExit();
            } else if (type == MyFriendProfile.TYPE_CONTACTLIST) {
                bookButton.setExit();
                contactListButton.setSelected();
                albumButton.setExit();
                aboutButton.setExit();
            } else if (type == MyFriendProfile.TYPE_ALBUM) {
                bookButton.setExit();
                contactListButton.setExit();
                albumButton.setSelected();
                aboutButton.setExit();
            } else if (type == MyFriendProfile.TYPE_ABOUT) {
                bookButton.setExit();
                contactListButton.setExit();
                albumButton.setExit();
                aboutButton.setSelected();
            }
        } catch (Exception e) {
        }
    }

    /* *******************************************************
     * Getter & Setter Methods
     * *******************************************************/
    public UserBasicInfo getFriendProfileInfo() {
        return this.friendProfileInfo;
    }

    public void setFriendProfileInfo(String userId) {
        if (!FriendList.getInstance().getFriend_hash_map().isEmpty() && FriendList.getInstance().getFriend_hash_map().get(userId) != null) {
            this.friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(userId);
        }
    }

    public void setFriendProfileInfo(UserBasicInfo friendProfileInfo) {
        this.friendProfileInfo = friendProfileInfo;
    }

    public static String getSeleected_user_identiy() {
        return seleected_user_identiy;
    }

    public static void setSeleected_user_identiy(String seleected_user_identiy) {
        MyFriendProfile.seleected_user_identiy = seleected_user_identiy;
    }

    public String getUserIdentity() {
        return this.userIdentity;
    }

    public String getFullName() {
        return this.friendProfileInfo.getFullName();
    }


    /* *******************************************************
     * Event Handler Methods
     * *******************************************************/
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnChat) {
                myfriendSlider.next();//previous();
            } else if (e.getSource() == btnCallToRingID) {
                if (MainClass.getMainClass() == null) {
                    CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                    callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                    callSettingsDTo.setFndId(friendProfileInfo.getUserIdentity());
                    callSettingsDTo.setFn(friendProfileInfo.getFullName());
                    callSettingsDTo.setPrIm(friendProfileInfo.getProfileImage());
                    callSettingsDTo.setIncomming(0);
                    MainClass mainClass2 = new MainClass(callSettingsDTo);
                    mainClass2.showWindow();
                } else {
                    MainClass.getMainClass().showWindow();
                }
            } else if (e.getSource() == btnVideoCall) {
                HelperMethods.showWarningDialogMessage(NotificationMessages.VIDEO_CALL_NOTIFICATION);
            } else if (e.getSource() == mnuRemove) {
                setType(TYPE_ABOUT);
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteFreind(userIdentity).start();
                }
            } else if (e.getSource() == mnuBlock) {
                new BlockUnblockFriend(userIdentity, friendProfileInfo.getUserTableId(), 1).start();
                blockFriendProfile();
            } else if (e.getSource() == mnuUnblock) {
                new BlockUnblockFriend(userIdentity, friendProfileInfo.getUserTableId(), 0).start();
                unBlockFriendProfile();
            } else if (e.getSource() == mnuChangeAccess) {
                JDialogContactType dialog = new JDialogContactType();
                if (JDialogContactType.contactType > 0 && (friendProfileInfo.getContactType() != JDialogContactType.contactType)) {
                    new ChangeFriendAccess(friendProfileInfo.getUserIdentity(), friendProfileInfo.getUserTableId(), JDialogContactType.contactType).start();
                }
            } else if (e.getSource() == acceptRequest) {
                if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL) {
                    new AcceptFriendRequest(userIdentity, btnAcceptRequest, StatusConstants.ACCESS_CHAT_CALL).start();
                } else {
                    JDialogContactType dialog = new JDialogContactType();
                    if (JDialogContactType.contactType > 0) {
                        new AcceptFriendRequest(userIdentity, btnAcceptRequest, JDialogContactType.contactType).start();
                    }
                }
            } else if (e.getSource() == deleteRequest) {
                setType(TYPE_ABOUT);
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteFreind(userIdentity).start();
                }
            } else if (e.getSource() == allowAccess) {
                new AcceptFriendAccess(userIdentity, friendProfileInfo.getUserTableId(), friendProfileInfo.getNewContactType(), true, btnAcceptAccessRequest).start();
            } else if (e.getSource() == denyAccess) {
                new AcceptFriendAccess(userIdentity, friendProfileInfo.getUserTableId(), friendProfileInfo.getNewContactType(), false, btnAcceptAccessRequest).start();
            }
        }
    };

    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bookButton) {
                setType(TYPE_BOOK);
                onTabClick();
            } else if (e.getSource() == contactListButton) {
                setType(TYPE_CONTACTLIST);
                onTabClick();
            } else if (e.getSource() == albumButton) {
                setType(TYPE_ALBUM);
                onTabClick();
            } else if (e.getSource() == aboutButton) {
                setType(TYPE_ABOUT);
                onTabClick();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

            if (e.getSource() == bookButton) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (!bookButton.isSelected) {
                    bookButton.setEntered();
                }
            } else if (e.getSource() == contactListButton) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (!contactListButton.isSelected) {
                    contactListButton.setEntered();
                }
            } else if (e.getSource() == albumButton) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (!albumButton.isSelected) {
                    albumButton.setEntered();
                }
            } else if (e.getSource() == aboutButton) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (!aboutButton.isSelected) {
                    aboutButton.setEntered();
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

            if (e.getSource() == bookButton) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!bookButton.isSelected) {
                    bookButton.setExit();
                }
            } else if (e.getSource() == contactListButton) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!contactListButton.isSelected) {
                    contactListButton.setExit();
                }
            } else if (e.getSource() == albumButton) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!albumButton.isSelected) {
                    albumButton.setExit();
                }
            } else if (e.getSource() == aboutButton) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!aboutButton.isSelected) {
                    aboutButton.setExit();
                }
            }
        }
    };

    public void blockFriendProfile() {
        myNamePanel.setBorder(new EmptyBorder(13, 0, 20, 0));
        blockImagePanel.setVisible(true);
        statusPanel.setBorder(new EmptyBorder(15, 24, 0, 0));
        statusPanel.revalidate();
        statusPanel.repaint();
        myNamePanel.revalidate();
        myNamePanel.repaint();
    }

    public void unBlockFriendProfile() {
        myNamePanel.setBorder(new EmptyBorder(22, 0, 28, 0));
        statusPanel.setBorder(new EmptyBorder(24, 24, 0, 0));
        statusPanel.revalidate();
        statusPanel.repaint();
        blockImagePanel.setVisible(false);
        myNamePanel.revalidate();
        myNamePanel.repaint();
    }

    public void checkSelectedTab() {
        if (type == TYPE_BOOK && myFriendNewsFeedPanel == null) {
            onTabClick();
        } else if (type == TYPE_CONTACTLIST && myFriendContactListPane == null) {
            onTabClick();
        } else if (type == TYPE_ALBUM && myFriendAlbumPanel == null) {
            onTabClick();
        } else if (type == TYPE_ABOUT && myFriendAboutPanel == null) {
            onTabClick();
        }
    }
}

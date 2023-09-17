/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.image.LoadCoverImageInLabel;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.service.aboutme.UnknownProfileInfoRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.image.ImagePaneForCoverImage;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Shahadat Hossain
 */
public class UnknownProfile extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UnknownProfile.class);
    private JButton btnSendFriendRequest;
    private JPanel tabPanel;
    private JPanel rightTabPanel;
    public UserBasicInfo friendProfileInfo;
    MyFriendProfile myFriendProfile;
    private JPanel friendImageWrapper;
    private ImagePaneForCoverImage coverImagePanel = new ImagePaneForCoverImage();
    private JLabel profilePictureLabel = new JLabel();
    private JScrollPane freindInfoScorllPanel;
    private JPanel downPanel;
    private JLabel myNameLabel;
    private JPanel myName;
    private JLabel friendFullNameLable;
    private JLabel friendRingIDLable;
    private JPanel country;
    private JPanel phoneNumber;
    private JPanel gender;
    private JPanel birthDay;
    private JPanel loadingPanel;
    private JPanel single;
    private GridBagConstraints con;
    private ImageMousListenerInBook coverImageClickListener;
    int type_int = 0;
    int width = 290;
    int commonHeight = 45;
    int gap = 3;
    int flag = 0;

    public UnknownProfile(UserBasicInfo friendProfileInfo, MyFriendProfile myFriendProfile) {
        this.friendProfileInfo = friendProfileInfo;
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);
        //setBackground(Color.WHITE);
        this.myFriendProfile = myFriendProfile;
        type_int = 0;
        initContainers();

    }

    public UnknownProfile(UserBasicInfo friendProfileInfo) {
        this.friendProfileInfo = friendProfileInfo;
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);//setBackground(Color.WHITE);
        this.myFriendProfile = null;
        type_int = 0;
        initContainers();

    }

    public UnknownProfile(UserBasicInfo friendProfileInfo, int type_int) {
        this.friendProfileInfo = friendProfileInfo;
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);// setBackground(Color.WHITE);
        this.myFriendProfile = null;
        this.type_int = type_int;
        initContainers();

    }

    private void initContainers() {
        try {
            //   System.out.println("iasubuasvb");
            JPanel topPortions = new JPanel(new BorderLayout(0, 0));
            topPortions.setOpaque(false);
            friendImageWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            friendImageWrapper.setOpaque(false);
            friendImageWrapper.setBorder(new MatteBorder(0, 1, 0, 1, RingColorCode.DEFAULT_BORDER_COLOR));

            JPanel imageContainer = new JPanel(new BorderLayout(0, 0));
            imageContainer.setOpaque(false);
            imageContainer.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT));
            friendImageWrapper.add(imageContainer);

            coverImagePanel.setOpaque(false);
            coverImagePanel.setLayout(new BorderLayout());
            //coverImagePanel.setBorder(new EmptyBorder(0, 15, 0, 15));
            imageContainer.add(coverImagePanel);

            ImagePane profilePicPanel = new ImagePane();
            profilePicPanel.setLayout(null);
            profilePicPanel.setOpaque(false);
            try {
                profilePicPanel.setImage(ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC_BIG)));
            } catch (IOException ex) {
            }
            profilePicPanel.setPreferredSize(new Dimension(110, 150));

//            profilePictureLabel.setBounds(29, 15, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
            profilePictureLabel.setBounds(13, 26, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
            profilePicPanel.add(profilePictureLabel);
            JPanel profileImageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            profileImageWrapper.setBorder(new EmptyBorder(38, 6, 0, 0));
            profileImageWrapper.setBackground(Color.GREEN);
            profileImageWrapper.setOpaque(false);
            profileImageWrapper.add(profilePicPanel);
            coverImagePanel.add(profileImageWrapper, BorderLayout.WEST);
            //coverImagePanel.add(profilePicPanel, BorderLayout.WEST);

            JPanel nameAndLoadingPanel = new JPanel(new BorderLayout());
            nameAndLoadingPanel.setOpaque(false);
            JPanel nameWrapperPanel = new JPanel(new BorderLayout(0, 0));
            nameWrapperPanel.setBorder(new EmptyBorder(70, 0, 27, 0));
            nameWrapperPanel.setOpaque(false);
            JPanel myNamePanel = new JPanel();
            myNamePanel.setLayout(new BoxLayout(myNamePanel, BoxLayout.Y_AXIS));
            myNamePanel.setBorder(new EmptyBorder(23, 30, 0, 5));
            myNamePanel.setOpaque(false);
            JPanel nameStatusWrapperPanel = new JPanel() {
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
            //nameWrapperPanel.add(myNamePanel);
            friendFullNameLable = new JLabel("Unknown Profile");
            friendFullNameLable.setForeground(Color.WHITE);
            try {
                Font font = DesignClasses.getDefaultFont(Font.BOLD, 15);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
                HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
                attrs.put(TextAttribute.TRACKING, .02);
                font = font.deriveFont(attrs);
                friendFullNameLable.setFont(font);
            } catch (Exception e) {
            }
            friendRingIDLable = DesignClasses.makeJLabel_normal("Unknown ringID", 13, Color.WHITE);
            //  myNamePanel.add(statusImageLabel);
            myNamePanel.add(friendFullNameLable);
            myNamePanel.add(friendRingIDLable);
            nameAndLoadingPanel.add(nameWrapperPanel, BorderLayout.WEST);

            loadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            loadingPanel.setOpaque(false);
            nameAndLoadingPanel.add(loadingPanel, BorderLayout.CENTER);

            coverImagePanel.add(nameAndLoadingPanel, BorderLayout.CENTER);

            topPortions.add(friendImageWrapper, BorderLayout.CENTER);

            JPanel tabAndCallPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            tabAndCallPanelWrapper.setOpaque(false);

            tabPanel = new JPanel(new BorderLayout(DefaultSettings.HGAP, 0));
            tabPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 35));
            tabPanel.setBackground(Color.WHITE);
            tabPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));

            rightTabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            rightTabPanel.setBorder(new EmptyBorder(0, 0, 0, 2));
            rightTabPanel.setOpaque(false);
            tabPanel.add(rightTabPanel, BorderLayout.EAST);
            tabAndCallPanelWrapper.add(tabPanel);

            topPortions.add(tabAndCallPanelWrapper, BorderLayout.SOUTH);

            add(topPortions, BorderLayout.NORTH);

            this.buildMainInformationPanel();
        } catch (Exception e) {
        }
    }

    private void buildMainInformationPanel() {
        JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        mainContainer.setOpaque(false);

        downPanel = new JPanel(new BorderLayout());
        downPanel.setOpaque(false);
        downPanel.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);

        JPanel nullInfo = new JPanel(new BorderLayout());
        nullInfo.setBackground(Color.white);
        // nullInfo.setBorder(new EmptyBorder(18, 25, 18, 25));//.setBorder(new EmptyBorder(0, 19, 7, 0));
        nullInfo.add(downPanel, BorderLayout.CENTER);

        mainContainer.add(nullInfo, BorderLayout.CENTER);
        freindInfoScorllPanel = DesignClasses.getDefaultScrollPane(mainContainer);
        this.add(freindInfoScorllPanel, BorderLayout.CENTER);

        addRightContent();
        loadFriendInformation();
    }

    private void addRightContent() {
        try {
            single = new JPanel();
            single.setOpaque(false);
            single.setLayout(new GridBagLayout());
            con = new GridBagConstraints();
            con.gridx = 0;
            con.gridy = 0;
            //con.insets = new Insets(2, 2, 2, 2);

            downPanel.add(single, BorderLayout.CENTER);

            myName = nullPanel();
            single.add(myName, con);
            con.gridy++;

            country = nullPanel();

            phoneNumber = nullPanel();

            gender = nullPanel();

            birthDay = nullPanel();

        } catch (Exception e) {
        }
    }

    public void loadFriendInformation() {
        if (friendProfileInfo != null) {
            buildFullNameInCover();
            changeFriendFullName();
            chagefirstName();
            //    changeLastname();
            // changeCountry();
            changemobileNumber();
            changeGender();
            changeBirthDay();
            single.add(Box.createRigidArea(new Dimension(1, 1)), con);
            con.gridy++;
            buildFrinedImage();
            if (friendProfileInfo.getUserIdentity() != null) {
                buildButtonPanel();
            } else {
                new UnknownProfileInfoRequest(friendProfileInfo).start();
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    freindInfoScorllPanel.getVerticalScrollBar().setValue(0);
                }
            });
        }
    }

    private JPanel createUnitAboutPanel(String title, String value) {
        JPanel contanier = new JPanel(new FlowLayout());
        contanier.setPreferredSize(new Dimension(550, 35));
        contanier.setOpaque(false);

        // container.setLayout(null);
        contanier.setBorder(new EmptyBorder(1, 0, 1, 2));

        JLabel name = DesignClasses.makeJLabel_normal(title, 14, DefaultSettings.BLACK_FONT);
        name.setBorder(new EmptyBorder(0, 0, 2, 0));
        JLabel valuelabel = DesignClasses.makeJLabel_normal(value, 14, DefaultSettings.BLACK_FONT);
        valuelabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(100, 30));
        namePanel.setOpaque(false);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = DesignClasses.makeJLabel_normal(":    ", 14, DefaultSettings.BLACK_FONT);
        colon.setBorder(new EmptyBorder(0, 0, 2, 0));
        JPanel colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(20, 30));
        colonPanel.setOpaque(false);
        colonPanel.add(colon, BorderLayout.CENTER);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setPreferredSize(new Dimension(250, 30));
        valuePanel.setOpaque(false);
        valuePanel.add(valuelabel, BorderLayout.CENTER);

        contanier.add(namePanel);
        contanier.add(colonPanel);
        contanier.add(valuePanel);

        JPanel extraPanel = new JPanel(new BorderLayout());
        extraPanel.setPreferredSize(new Dimension(140, 30));
        extraPanel.setOpaque(false);
        contanier.add(extraPanel);

        return contanier;
    }

    public void buildFullNameInCover() {
        if (friendFullNameLable != null) {
            if (this.friendProfileInfo != null) {
                /*if ((this.friendProfileInfo.getFullName() != null && this.friendProfileInfo.getFullName().trim().length() > 0) && (this.friendProfileInfo.getLastName() != null && this.friendProfileInfo.getLastName().trim().length() > 0)) {
                 friendFullNameLable.setText(this.friendProfileInfo.getFullName() + " " + this.friendProfileInfo.getLastName());
                 } else */
                if ((this.friendProfileInfo.getFullName() != null && this.friendProfileInfo.getFullName().trim().length() > 0)) {
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
                    friendRingIDLable.setText(HelperMethods.getRingID(this.friendProfileInfo.getUserIdentity()));
                }
            } else {
                friendFullNameLable.setText("No ringID");
            }
            friendFullNameLable.revalidate();
        }
    }

    public void changeFriendFullName() {
        if (myNameLabel != null) {
            if (friendProfileInfo.getFullName().trim().length() > 0 /*&& friendProfileInfo.getLastName() != null && friendProfileInfo.getLastName().trim().length() > 0*/) {
                myNameLabel.setText(friendProfileInfo.getFullName() /*+ " " + friendProfileInfo.getLastName()*/);
            } else {
                myNameLabel.setText("No name");
            }
            myNameLabel.revalidate();
        }
    }

    public void chagefirstName() {
        if (myName != null) {
            try {
                String name = "";
                if (friendProfileInfo != null) {
                    name = friendProfileInfo.getFullName() /*+ " " + friendProfileInfo.getLastName()*/;
                }
                myName.removeAll();
                myName.setOpaque(false);
                JPanel titlePanel = createUnitAboutPanel("Name", name);
                myName.add(titlePanel, BorderLayout.CENTER);
                myName.revalidate();
                myName.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in chagefirstName ==>" + e.getMessage());
            }
        }
    }

    public void changeCountry() {
        if (country != null) {
            try {
                country.removeAll();
                country.setOpaque(false);
                if (friendProfileInfo.getCountry() != null && friendProfileInfo.getCountry().trim().length() > 0) {
                    single.add(country, con);
                    con.gridy++;
                    String countryName = friendProfileInfo.getCountry();
                    JPanel titleandEditLabel = createUnitAboutPanel("" + StaticFields.COUNTRY_TEXT + "  ", countryName);
                    country.add(titleandEditLabel, BorderLayout.CENTER);
                    country.setVisible(true);
                } else {
                    country.setVisible(false);
                }

                country.revalidate();
                country.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in changeCountry ==>" + e.getMessage());
            }
        }
    }

    public void changemobileNumber() {
        if (phoneNumber != null) {
            try {
                phoneNumber.removeAll();
                phoneNumber.setOpaque(false);
                if (friendProfileInfo.getMobilePhone() != null && friendProfileInfo.getMobilePhone().trim().length() > 0 && HelperMethods.check_mobile_number(friendProfileInfo.getMobilePhone().trim())) {
                    single.add(phoneNumber, con);
                    con.gridy++;
                    String mobileNmuber = HelperMethods.getUnknownProfilePhoneNumber(friendProfileInfo.getMobilePhoneDialingCode(), friendProfileInfo.getMobilePhone());

                    JPanel titleandEditLabel = createUnitAboutPanel("" + StaticFields.PHONE_NO_TEXT + "  ", mobileNmuber);
                    phoneNumber.add(titleandEditLabel, BorderLayout.CENTER);
                    phoneNumber.setVisible(true);
                } else {
                    phoneNumber.setVisible(false);
                }

                phoneNumber.revalidate();
                phoneNumber.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changemobileNumber ==>" + e.getMessage());
            }
        }
    }

    public void changeGender() {
        if (gender != null) {
            try {
                gender.removeAll();
                gender.setOpaque(false);
                if (friendProfileInfo.getGender() != null
                        && friendProfileInfo.getGender().trim().length() > 0
                        && !friendProfileInfo.getGender().equalsIgnoreCase("null")) {
                    single.add(gender, con);
                    con.gridy++;
                    JPanel titleandEditLabel = createUnitAboutPanel("" + StaticFields.GENDER_TEXT + "  ", friendProfileInfo.getGender());
                    gender.add(titleandEditLabel, BorderLayout.CENTER);
                    gender.setVisible(true);
                } else {
                    gender.setVisible(false);
                }

                gender.revalidate();
                gender.repaint();
            } catch (Exception e) {
                //   e.printStackTrace();
                log.error("Error in changeGender ==>" + e.getMessage());
            }
        }
    }

    public void changeBirthDay() {
        if (birthDay != null) {
            try {
                birthDay.removeAll();
                birthDay.setOpaque(false);
                short[] friendPrivacy = new short[4];
                if (friendProfileInfo.getPrivacy() != null) {
                    friendPrivacy = friendProfileInfo.getPrivacy();
                }
                short birthdayPrivacy = friendPrivacy[3];

                String birthday = "";
                String strDate = "";
                if (friendProfileInfo.getBirthday() != null
                        && !friendProfileInfo.getBirthday().equals("")
                        && !friendProfileInfo.getBirthday().equalsIgnoreCase("null")
                        && friendProfileInfo.getBirthday().trim().length() > 0
                        && birthdayPrivacy > 0
                        && birthdayPrivacy == AppConstants.PRIVACY_SHORT_PUBLIC) {
                    birthday = friendProfileInfo.getBirthday();
                    Date d = new Date();
                    d = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                    strDate = new SimpleDateFormat("MMMM d, yyyy").format(d);
                    single.add(birthDay, con);
                    con.gridy++;
                    JPanel titleandEditLabel = createUnitAboutPanel("" + StaticFields.BIRTH_DAY_TEXT + "  ", strDate);
                    birthDay.add(titleandEditLabel, BorderLayout.CENTER);
                    birthDay.setVisible(true);
                } else {
                    birthDay.setVisible(false);
                }

                birthDay.revalidate();
                birthDay.repaint();
            } catch (Exception e) {
            }
        }
    }

    private JPanel nullPanel() {
        JPanel basicJpanel = new JPanel(new BorderLayout());
        basicJpanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 35));
        basicJpanel.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        //  basicJpanel.setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR));
        return basicJpanel;
    }

    public void buildFrinedImage() {
        buildProfileImage();
        buildCoverImage();
    }

    private void buildProfileImage() {
        try {
            if (this.friendProfileInfo != null) {
                short[] friendPrivacy = new short[5];
                //    int friendShipStatus = StatusConstants.FRIENDSHIP_STATUS_ACCEPTED;
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

                if (this.friendProfileInfo.getProfileImage() != null
                        && this.friendProfileInfo.getProfileImage().trim().length() > 0) {
                    //    && profileImagePrivacy > 0
                    //    && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || (profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND))) {
                    //      flag = 1;
                    ImageHelpers.addProfileImageThumb(profilePictureLabel, this.friendProfileInfo.getProfileImage(), DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, true);
//                    imgProfile = DesignClasses.getBufferImageFromurl95(this.friendProfileInfo.getProfileImage());
//                    bufferProfileImage = DesignClasses.getOrigialProfileImage(this.friendProfileInfo.getProfileImage());
//                    /*
//                     *  NEED TO REMOVE
//                     */
//                    if (bufferProfileImage == null) {
//                        bufferProfileImage = imgProfile;                    
//                    }
                    /**
                     * *if (profileImagePrivacy > 0 && (profileImagePrivacy ==
                     * AppConstants.PRIVACY_SHORT_PUBLIC || (profileImagePrivacy
                     * == AppConstants.PRIVACY_SHORT_ONLY_FRIEND))) {
                     * profilePictureLabel.setCursor(new
                     * Cursor(Cursor.HAND_CURSOR)); JsonFields imageDto = new
                     * JsonFields();
                     * imageDto.setUserIdentity(friendProfileInfo.getUserIdentity());
                     * imageDto.setFirstName(friendProfileInfo.getFirstName());
                     * imageDto.setLastName(friendProfileInfo.getLastName());
                     * imageDto.setIurl(friendProfileInfo.getProfileImage());
                     * imageDto.setImageId(friendProfileInfo.getProfileImageId());
                     * imageDto.setImT(2);
                     * profilePictureLabel.addMouseListener(new
                     * ImageMousListenerInBook(imageDto, true)); }**
                     */

                } else {
                    BufferedImage imgProfile = ImageHelpers.getUnknownImage(false);
                    profilePictureLabel.setIcon(new ImageIcon(imgProfile));
                    imgProfile.flush();
                    imgProfile = null;
                }

                profilePictureLabel.revalidate();
                profilePictureLabel.repaint();
                coverImagePanel.revalidate();

            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("failed to build propic unknonw friend" + e.getMessage());

        }
    }

    private void buildCoverImage() {
        if (coverImagePanel != null && this.friendProfileInfo != null) {
            short[] friendPrivacy = new short[5];
            if (this.friendProfileInfo.getPrivacy() != null) {
                friendPrivacy = this.friendProfileInfo.getPrivacy();
            }
            short coverImagePrivacy = friendPrivacy[4];

            coverImagePanel.removeMouseListener(coverImageClickListener);

            try {
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
                    if (coverImagePrivacy > 0 && (coverImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC)) {
                        type_int = 1;
                    } else {
                        type_int = 0;
                    }
                    coverImageClickListener = new ImageMousListenerInBook(imageDto, true);
                    new LoadCoverImageInLabel(type_int, friendImageWrapper, coverImagePanel, loadingPanel, imageDto, this.friendProfileInfo.getCoverImageX(), this.friendProfileInfo.getCoverImageY(), coverImageClickListener).start();
                } else {
                    BufferedImage img = ImageIO.read(new Object() {
                    }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                    coverImagePanel.setImage(img, 0, 0);
                    coverImagePanel.revalidate();
                }

            } catch (IOException ex) {
                log.error("Buffer img error" + ex.getMessage());
            }
        }
    }

    public void buildButtonPanel() {
        try {
            if (this.friendProfileInfo != null) {
                btnSendFriendRequest = new RoundedCornerButton("Add as Friend", "Add as Friend");
                //DesignClasses.createImageButton(GetImages.FRIEND_REQUEST, GetImages.FRIEND_REQUEST_H, "Friend Request");
                //         rightTabPanel.setBorder(DEFAULT_BOOK_BORDER);
                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.add(Box.createRigidArea(new Dimension(0, 6)), BorderLayout.CENTER);

                buttonPanel.add(btnSendFriendRequest, BorderLayout.SOUTH);
                buttonPanel.setOpaque(false);
                rightTabPanel.add(buttonPanel);
                btnSendFriendRequest.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JDialogContactType dialog = new JDialogContactType();
                        if (JDialogContactType.contactType > 0) {
                            if (myFriendProfile != null) {
                                new SendFriendRequest(friendProfileInfo.getUserIdentity(), btnSendFriendRequest, myFriendProfile.myFriendContactListPane, JDialogContactType.contactType).start();
                            } else {

                                new SendFriendRequest(friendProfileInfo.getUserIdentity(), btnSendFriendRequest, JDialogContactType.contactType).start();

                                if (type_int == 3) {
                                    MyfnfHashMaps.getInstance().getInviteFriendsContainer().remove(friendProfileInfo.getUserIdentity());

                                    /*if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel() != null) {
                                     GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getFriendSearchPanel().addSearchResultInContainer();
                                     } else {
                                     // System.out.println("getInviteFriendLeftPanel()==null");
                                     }*/
                                    if (GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel() != null) {
                                        GuiRingID.getInstance().getAddFriendMainPanel().getSearchFriendPanel().addSearchResultInContainer();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("getInviteFriendLeftPanel()==null" + e.getMessage());
        }
    }
}

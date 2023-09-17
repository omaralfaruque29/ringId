/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
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
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.image.LoadCoverImageInLabel;
import com.ipvision.service.MyNewsFeeds;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.image.ImagePaneForCoverImage;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.ProfilePicBigPanel;
import com.ipvision.view.image.ViewProfileImage;
import com.ipvision.view.utility.MenuPanel;
import com.ipvision.view.utility.SeparatorPanel;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Faiz Ahmed
 */
public class MyProfilePanel extends JPanel implements ActionListener, AncestorListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyProfilePanel.class);
    public JPanel myProfilePic;
    public JPanel myUsername;
    public JScrollPane myInfoScroller;
    private JPanel imageWrapperPanel;
    private ImagePaneForCoverImage coverImagePanel = new ImagePaneForCoverImage();
    private JLabel profilePictureLabel = new JLabel();
    private JButton actionPopUpButton;
    public JLabel notVerified;
    public JPopupMenu actionPopUp = new JPopupMenu();
    private ProfilePopupMenus profilePopupMenus;
    private JLabel loadingLabel = new JLabel();
    private JLabel myNameLabel;
    private JLabel myRingidLabel;
    int gap = 3;
    int maxImageWidth = 200;
    int maxImageHeight = 200;
    int commonHeight = 45;
    private JPanel tabContainer;
    private JPanel tabPanel;
    private JPanel container;
    private JPanel mainContentPanel;
    private JPanel loadingPanel;
    private ImageMousListenerInBook profileImageClickListener;
    private ImageMousListenerInBook coverImageClickListener;
    public static final int TYPE_ABOUT = 1;
    public static final int TYPE_MY_BOOK = 2;
    public static final int TYPE_ALBUM = 3;
    public static final String MENU_TYPE_BOOK = "Post";
    public static final String MENU_TYPE_ALBUM = "Photos";
    public static final String MENU_TYPE_ABOUT = "About";

    private MenuPanel aboutButton = new MenuPanel(MENU_TYPE_ABOUT, 70, 48);
    private MenuPanel bookButton = new MenuPanel(MENU_TYPE_BOOK, 70, 48);
    private MenuPanel albumButton = new MenuPanel(MENU_TYPE_ALBUM, 70, 48);
    private int type = TYPE_MY_BOOK;
    private MyBookHome myFeeds;
    private AlbumsHome albumsHome;
    private MyProfileAboutPanel myProfileAbout;
    public JPanel tabAndProfilelPanelWrapper;
    public JPanel scrollPnlTop;

    public MyProfileAboutPanel getMyProfileAbout() {
        if (myProfileAbout == null) {
            myProfileAbout = new MyProfileAboutPanel();
        }
        return myProfileAbout;
    }

    public MyBookHome getMyFeeds() {
        return myFeeds;
    }

    public void setMyFeeds(MyBookHome myFeeds) {
        this.myFeeds = myFeeds;
    }

    public AlbumsHome getAlbumsHome() {
        return albumsHome;
    }

    public void setAlbumsHome(AlbumsHome albumsHome) {
        this.albumsHome = albumsHome;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyProfilePanel() {
//        //htmlHelp = new HtmlHelpers();
//       // calender = Calendar.getInstance();
//        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
//            //reqularex = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
//        }
        addAncestorListener(this);
    }

    public void initContainers() {
        try {
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel wrapper = new JPanel(new BorderLayout());
            //wrapper.setBackground(Color.red);
            wrapper.setOpaque(false);

            scrollPnlTop = new JPanel();
            scrollPnlTop.setBackground(Color.blue);
            scrollPnlTop.setOpaque(false);
            scrollPnlTop.setPreferredSize(new Dimension(11, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT + 35));
            wrapper.add(scrollPnlTop, BorderLayout.EAST);

            tabAndProfilelPanelWrapper = new JPanel();
            tabAndProfilelPanelWrapper.setLayout(new BoxLayout(tabAndProfilelPanelWrapper, BoxLayout.Y_AXIS));
            tabAndProfilelPanelWrapper.setOpaque(false);

            wrapper.add(tabAndProfilelPanelWrapper, BorderLayout.CENTER);

            add(wrapper, BorderLayout.NORTH);

            addProfileCoverOptions();
            buildTabPanel();

            container = new JPanel(new BorderLayout(0, 0));
            container.setOpaque(false);

            add(container, BorderLayout.CENTER);

            mainContentPanel = new JPanel(new BorderLayout());
            mainContentPanel.setOpaque(false);
            container.add(mainContentPanel, BorderLayout.CENTER);

            onTabClick();
        } catch (Exception e) {
        }
    }

    private void buildTabPanel() {
        tabContainer = new JPanel(new BorderLayout(DefaultSettings.HGAP, 0));
        tabContainer.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 50));
//        tabContainer.setBackground(DefaultSettings.COMMENTS_LIKE_BAR_COLOR);
        tabContainer.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        tabContainer.setBackground(Color.WHITE);
        tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setOpaque(false);
        tabContainer.add(tabPanel, BorderLayout.WEST);

        JPanel tabWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabWrapperPanel.setOpaque(false);
        tabWrapperPanel.add(tabContainer);

        tabAndProfilelPanelWrapper.add(tabWrapperPanel);

        tabPanel.removeAll();
        tabPanel.add(bookButton);
        tabPanel.add(new SeparatorPanel(1, 48, 8));
        bookButton.addMouseListener(mouseListener);
        tabPanel.add(albumButton);
        tabPanel.add(new SeparatorPanel(1, 48, 8));
        albumButton.addMouseListener(mouseListener);
        tabPanel.add(aboutButton);
        aboutButton.addMouseListener(mouseListener);

    }

    private void addProfileCoverOptions() {
        coverImagePanel.setOpaque(false);
        coverImagePanel.setLayout(new BorderLayout());
//        coverImagePanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        //coverImagePanel.setBorder(new EmptyBorder(0, 0, 0, 1));

        JPanel panel = new JPanel(new BorderLayout(0, 0));
        // panel.setBackground(Color.WHITE);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT));
        panel.setBorder(new MatteBorder(0, 1, 0, 1, RingColorCode.FEED_BORDER_COLOR));
        panel.add(coverImagePanel);

        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        arrowPanel.setOpaque(false);
        arrowPanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT, 0));
        actionPopUpButton = DesignClasses.createImageButton(GetImages.SETTING_MINI, GetImages.SETTING_MINI_H, "Options");
        arrowPanel.add(actionPopUpButton);
        if (profilePopupMenus == null) {
            profilePopupMenus = new ProfilePopupMenus(0);
        }
        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePopupMenus.setPopUpMenuItemVisibilityMyProfile();
                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 165, actionPopUp.getY() + 10);
            }
        });
        profilePopupMenus.buildPopUpMenuMyProfile();
        coverImagePanel.add(arrowPanel, BorderLayout.EAST);
        ProfilePicBigPanel profilePicBigPanel = new ProfilePicBigPanel(profilePictureLabel);
        //profilePicBigPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        JPanel profileImageWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        profileImageWrapper.setBorder(new EmptyBorder(38, 6, 0, 0));
        profileImageWrapper.setOpaque(false);
        profileImageWrapper.add(profilePicBigPanel);
        coverImagePanel.add(profileImageWrapper, BorderLayout.WEST);
//        coverImagePanel.add(profilePicBigPanel, BorderLayout.WEST);
        JPanel nameAndLoadingPanel = new JPanel(new BorderLayout());
        nameAndLoadingPanel.setOpaque(false);
        JPanel nameWrapperPanel = new JPanel(new BorderLayout(0, 0));//(new FlowLayout(FlowLayout.LEFT));
        nameWrapperPanel.setBorder(new EmptyBorder(70, 0, 27, 0));
        nameWrapperPanel.setOpaque(false);

        JPanel myNamePanel = new JPanel();
        myNamePanel.setBorder(new EmptyBorder(23, 30, 0, 5));
        myNamePanel.setLayout(new BoxLayout(myNamePanel, BoxLayout.Y_AXIS));
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
        myNameLabel = new JLabel("Friend Profile");
        myNameLabel.setForeground(Color.WHITE);
        try {
            Font font = DesignClasses.getDefaultFont(Font.BOLD, 15);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            myNameLabel.setFont(font);
        } catch (Exception e) {
        }
        myNamePanel.add(myNameLabel);
        myRingidLabel = DesignClasses.makeJLabel_normal("My ringID", 13, Color.WHITE);
        myNamePanel.add(myRingidLabel);
        nameAndLoadingPanel.add(nameWrapperPanel, BorderLayout.WEST);

        loadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadingPanel.setOpaque(false);
        nameAndLoadingPanel.add(loadingPanel, BorderLayout.CENTER);
        coverImagePanel.add(nameAndLoadingPanel, BorderLayout.CENTER);

        imageWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        //imageWrapperPanel.setBackground(Color.WHITE);
        imageWrapperPanel.setOpaque(false);
        imageWrapperPanel.add(panel);

        tabAndProfilelPanelWrapper.add(imageWrapperPanel);
        //add(imageWrapperPanel, BorderLayout.NORTH);

    }

    public void change_myFullName() {
        if (myNameLabel != null && MyFnFSettings.userProfile != null) {
            if (MyFnFSettings.userProfile.getFullName() != null && MyFnFSettings.userProfile.getFullName().trim().length() > 0) {
                myNameLabel.setText(MyFnFSettings.userProfile.getFullName());
            } else {
                myNameLabel.setText("No name");
            }
            myNameLabel.revalidate();
        }
        if (myRingidLabel != null && MyFnFSettings.userProfile != null) {
            if (MyFnFSettings.userProfile.getUserIdentity() != null && MyFnFSettings.userProfile.getUserIdentity().trim().length() > 0) {
                myRingidLabel.setText(HelperMethods.getRingID(MyFnFSettings.userProfile.getUserIdentity()));
            } else {
                myRingidLabel.setText("No ringID");
            }
            myRingidLabel.revalidate();
        }
    }

    private void addAbout() {
        JPanel myInformations = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        myInformations.setBorder(new EmptyBorder(5, 0, 0, 0));
        myInformations.setOpaque(false);

        myInfoScroller = DesignClasses.getDefaultScrollPane(myInformations);
        myInfoScroller.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (myInfoScroller.getVerticalScrollBar().isVisible()) {
                    scrollPnlTop.setVisible(true);
                } else {
                    scrollPnlTop.setVisible(false);
                }
                tabAndProfilelPanelWrapper.revalidate();
                tabAndProfilelPanelWrapper.repaint();
            }
        });
        if (myProfileAbout == null) {
            myProfileAbout = new MyProfileAboutPanel();
        }
        myInformations.add(myProfileAbout);

        mainContentPanel.add(myInfoScroller, BorderLayout.CENTER);
    }

    public void changeMyImage() {
        changeProfileImage();
        changeCoverImage();
    }

    private void changeProfileImage() {
        profilePictureLabel.setOpaque(false);
        try {
            if (MyFnFSettings.userProfile != null) {

                profilePictureLabel.removeMouseListener(profileImageClickListener);
                if (MyFnFSettings.userProfile.getProfileImage() != null
                        && MyFnFSettings.userProfile.getProfileImage().trim().length() > 0) {
                    ViewProfileImage viewProfileImage = ImageHelpers.addProfileImageCrop(coverImagePanel, profilePictureLabel, MyFnFSettings.userProfile.getProfileImage(), DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, true);
                    profilePictureLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    JsonFields imageDto = new JsonFields();
                    imageDto.setUserIdentity(MyFnFSettings.userProfile.getUserIdentity());
                    imageDto.setFullName(MyFnFSettings.userProfile.getFullName());
                    //imageDto.setLastName(MyFnFSettings.userProfile.getLastName());
                    imageDto.setIurl(MyFnFSettings.userProfile.getProfileImage());
                    imageDto.setImageId(MyFnFSettings.userProfile.getProfileImageId());
                    imageDto.setImT(2);
                    profileImageClickListener = new ImageMousListenerInBook(imageDto, true, MyFnFSettings.PROFILE_IMAGE, MyFnFSettings.userProfile.getUserIdentity());
                    profilePictureLabel.addMouseListener(profileImageClickListener);
                    new RemoveInvalidProfileImageListener(viewProfileImage).start();
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

                GuiRingID.getInstance().getMyNamePanel().change_profile_image();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in changeProfileImage ==>" + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aboutButton) {
            setType(TYPE_ABOUT);
            onTabClick();
        } else if (e.getSource() == bookButton) {
            setType(TYPE_MY_BOOK);
            onTabClick();
        } else if (e.getSource() == albumButton) {
            setType(TYPE_ALBUM);////new ChangeEmail("ab@yopmail.com", null, null).start();
            // new ChangeMobileNumber("62116211",null,null).start();
            onTabClick();
        }
    }

    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bookButton) {
                setType(TYPE_MY_BOOK);
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

                if (HelperMethods.isInvalidDownloadUrl(ImageHelpers.getThumbUrl(MyFnFSettings.userProfile.getProfileImage()))
                        && HelperMethods.isInvalidDownloadUrl(ImageHelpers.getCropUrl(MyFnFSettings.userProfile.getProfileImage()))) {
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

    private void changeCoverImage() {
        try {
            if (coverImagePanel != null && MyFnFSettings.userProfile != null) {
                //loadingPanel.remove(loadingLabel);
                //coverImagePanel.setImage(null);
                coverImagePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                BufferedImage img = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                coverImagePanel.setImage(img, 0, 0);
                coverImagePanel.revalidate();
                coverImagePanel.repaint();
                coverImagePanel.removeMouseListener(coverImageClickListener);

                if ((MyFnFSettings.userProfile.getCoverImage() != null
                        && MyFnFSettings.userProfile.getCoverImage().trim().length() > 0)) {
                    JsonFields imageDto = new JsonFields();
                    imageDto.setUserIdentity(MyFnFSettings.userProfile.getUserIdentity());
                    imageDto.setFullName(MyFnFSettings.userProfile.getFullName());
                    //imageDto.setLastName(MyFnFSettings.userProfile.getLastName());
                    imageDto.setIurl(MyFnFSettings.userProfile.getCoverImage());
                    imageDto.setImageId(MyFnFSettings.userProfile.getCoverImageId());
                    imageDto.setImT(3);
                    coverImageClickListener = new ImageMousListenerInBook(imageDto, true, MyFnFSettings.COVER_IMAGE, MyFnFSettings.userProfile.getUserIdentity());
                    new LoadCoverImageInLabel(imageWrapperPanel, coverImagePanel, loadingPanel, imageDto, MyFnFSettings.userProfile.getCoverImageX(), MyFnFSettings.userProfile.getCoverImageY(), coverImageClickListener).start();
                } /*else {
                 coverImagePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                 img = ImageIO.read(new Object() {
                 }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                 coverImagePanel.setImage(img, 0, 0);
                 coverImagePanel.repaint();
                 coverImagePanel.revalidate();
                 imageWrapperPanel.revalidate();
                 }*/

                imageWrapperPanel.revalidate();
            }
        } catch (Exception ex) {
            log.error("Buffer img error1-->" + ex.getMessage());
        }
    }

    public void setImageUploadingLoader(boolean isProfileImage) {
        if (isProfileImage) {
            profilePictureLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_60));
            profilePictureLabel.revalidate();
            profilePictureLabel.repaint();
            coverImagePanel.revalidate();
            coverImagePanel.repaint();
            profilePictureLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            loadingLabel = new JLabel();
            loadingLabel.setOpaque(false);
            loadingLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_60));
            loadingPanel.add(loadingLabel);
            loadingPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            coverImagePanel.revalidate();
            coverImagePanel.repaint();
            imageWrapperPanel.revalidate();
        }
    }

    public void setTabSelectionColor(int type) {
        try {
            if (type == TYPE_MY_BOOK) {
                bookButton.setSelected();
                albumButton.setExit();
                aboutButton.setExit();
            } else if (type == TYPE_ALBUM) {
                bookButton.setExit();
                albumButton.setSelected();
                aboutButton.setExit();
            } else if (type == TYPE_ABOUT) {
                bookButton.setExit();
                albumButton.setExit();
                aboutButton.setSelected();
            }
        } catch (Exception e) {
        }
    }

    private void addMyBook() {
        try {
            boolean isLoadingFirstTime = false;
            if (NewsFeedMaps.getInstance().getMyNewsFeedId().hasDataFromServer() == false) {
                new MyNewsFeeds(0).start();
                isLoadingFirstTime = true;
            }
            if (this.myFeeds == null) {
                this.myFeeds = new MyBookHome();
                if (isLoadingFirstTime) {
                    this.myFeeds.addBottomLoading();
                }
            }
            mainContentPanel.add(this.myFeeds);
            if (isLoadingFirstTime == false) {
                this.myFeeds.loadTopForcefully();
            }
            this.myFeeds.scrollContent.getViewport().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (myFeeds != null && myFeeds.scrollContent.getVerticalScrollBar() != null) {
                        if (myFeeds.scrollContent.getVerticalScrollBar().isVisible()) {
                            scrollPnlTop.setVisible(true);
                        } else {
                            scrollPnlTop.setVisible(false);
                        }
                    }
                    tabAndProfilelPanelWrapper.revalidate();
                    tabAndProfilelPanelWrapper.repaint();
                }
            });
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        } catch (Exception e) {
        }
    }

    private void addMyAlbum() {
        try {
            if (albumsHome == null) {
                albumsHome = new AlbumsHome();
            }

            if (albumsHome.getPhotos() != null
                    && albumsHome.getPhotos().scrollContent.getVerticalScrollBar() != null
                    && albumsHome.getPhotos().scrollContent.getVerticalScrollBar().isVisible()) {
                scrollPnlTop.setVisible(true);
            } else {
                scrollPnlTop.setVisible(false);
            }

            if (mainContentPanel.getComponentCount() <= 0 || mainContentPanel.getComponent(0) != albumsHome) {
                mainContentPanel.add(albumsHome);
            }
            albumsHome.getPhotos().scrollContent.getViewport().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (albumsHome.getPhotos().scrollContent.getVerticalScrollBar().isVisible()) {
                        scrollPnlTop.setVisible(true);
                    } else {
                        scrollPnlTop.setVisible(false);
                    }
                    tabAndProfilelPanelWrapper.revalidate();
                    tabAndProfilelPanelWrapper.repaint();
                }
            });
        } catch (Exception e) {
        }
    }

    public boolean onTabClick() {
        boolean isNewInstance = true;
        mainContentPanel.removeAll();
        this.setTabSelectionColor(type);
        this.scrollPnlTop.setVisible(false);
        this.tabAndProfilelPanelWrapper.revalidate();
        this.tabAndProfilelPanelWrapper.repaint();
        if (type == TYPE_MY_BOOK) {
            addMyBook();
        } else if (type == TYPE_ABOUT) {
            addAbout();
        } else if (type == TYPE_ALBUM) {
            addMyAlbum();
        }
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        return isNewInstance;
    }

    public void checkSelectedTab() {
        if (type == TYPE_MY_BOOK && myFeeds == null) {
            onTabClick();
        }
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMyNamePanel().changeBgImage(true);
        }

    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMyNamePanel().changeBgImage(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }
}

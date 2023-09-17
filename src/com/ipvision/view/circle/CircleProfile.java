/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.DeleteCircle;
import com.ipvision.service.LeaveCircle;
import com.ipvision.view.image.LoadCoverImageInLabel;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.image.ImagePaneForCoverImage;
import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.MenuPanel;
import com.ipvision.view.utility.SeparatorPanel;
import java.awt.Cursor;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleProfile extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CircleProfile.class);
    private JPanel tabPanel;
    private int adminStatus;
    private long circleId;
    private JPanel circleInfo = new JPanel();
    private ImagePaneForCoverImage coverImagePanel;// = new ImagePaneForCoverImage();
    private JPanel coverImageWrapper;
    private SingleCircleDto singleCircleDto;
    private JButton actionPopUpButton;
//    private JPopupMenu actionPopUp;
    //private JMenuItem mnuShowCoverImage;
//    public JMenuItem mnuLeaveCircle;
//    public JMenuItem mnuDeleteCircle;
    JCustomMenuPopup leavePopup = null;
    JCustomMenuPopup deletePopup = null;
    private final String MNU_DELETE = "Delete this Circle";
    private final String MNU_LEAVE = "Leave from Circle";

    private JPanel mainContentPanel;
    private ImageMousListenerInBook coverImageClickListener;

    public static final String MAKE_ADMIN = "Make Admin";
    public static final String REMOVE_FROM_ADMIN = "Remove from Admin";
    public static final String ONLY_MEMBER = "Only Member";
    public static final String SUPER_ADMIN = "Super Admin";
    public static final String ADMIN = "Admin";

    public static final int TYPE_MEMBERS = 1;
    public static final int TYPE_BOOK = 0;
    private int type = TYPE_BOOK;
    
    public static final String MENU_TYPE_BOOK = "Post";
    public static final String MENU_TYPE_CONTACTLIST = "Members";

    private MenuPanel bookButton = new MenuPanel(MENU_TYPE_BOOK, 70, 48);
    private MenuPanel contactListButton = new MenuPanel(MENU_TYPE_CONTACTLIST, 70, 48);
    
    private JPanel tabAndProfilePanel;
    private JPanel container;
    private JLabel myNameLabel;
    private JPanel loadingPanel;
    private JLabel loadingLabel = new JLabel();
    public JPanel scrollPnlTop;
    public JPanel tabAndProfilelPanelWrapper;
    private JPanel tabContainer;

    private CircleNewsFeedPanel circleNewsFeedPanel;
    private CircleAlbumPanel circleAlbumPanel;
    private CircleMembersListPanel circleMembersListPanel;

    public CircleNewsFeedPanel getCircleNewsFeedPanel() {
        return circleNewsFeedPanel;
    }

    public void setCircleNewsFeedPanel(CircleNewsFeedPanel circleNewsFeedPanel) {
        this.circleNewsFeedPanel = circleNewsFeedPanel;
    }

    public CircleAlbumPanel getCircleAlbumPanel() {
        return circleAlbumPanel;
    }

    public void setCircleAlbumPanel(CircleAlbumPanel circleAlbumPanel) {
        this.circleAlbumPanel = circleAlbumPanel;
    }

    public CircleMembersListPanel getCircleMembersListPanel() {
        return circleMembersListPanel;
    }

    public void setCircleMembersListPanel(CircleMembersListPanel circleMembersListPanel) {
        this.circleMembersListPanel = circleMembersListPanel;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return this.singleCircleDto.getCircleName();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean check_already_in_circle(String user_id) {
        if (MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).get(user_id) != null) {
            return true;
        }
        return false;
    }

    public CircleProfile(long circleId) {
        this.circleId = circleId;
        coverImagePanel = new ImagePaneForCoverImage();
        singleCircleDto = MyfnfHashMaps.getInstance().getCircleLists().get(this.circleId);

        if (singleCircleDto != null && singleCircleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
            adminStatus = 1;
        }
        initContainers();
    }

    private void initContainers() {
        try {
            setLayout(new BorderLayout(DefaultSettings.HGAP, 0));
            setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);

            JPanel wrapper = new JPanel(new BorderLayout());
            //wrapper.setBackground(Color.red);
            wrapper.setOpaque(false);
            add(wrapper, BorderLayout.NORTH);

            scrollPnlTop = new JPanel();
            scrollPnlTop.setOpaque(false);
            scrollPnlTop.setPreferredSize(new Dimension(11, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT + 35));
            wrapper.add(scrollPnlTop, BorderLayout.EAST);
            scrollPnlTop.setVisible(false);

            tabAndProfilelPanelWrapper = new JPanel();
            tabAndProfilelPanelWrapper.setLayout(new BoxLayout(tabAndProfilelPanelWrapper, BoxLayout.Y_AXIS));
            tabAndProfilelPanelWrapper.setOpaque(false);

            wrapper.add(tabAndProfilelPanelWrapper, BorderLayout.CENTER);
            addProfileCoverOptions();
            container = new JPanel(new BorderLayout(0, 0));
            container.setOpaque(false);
            add(container, BorderLayout.CENTER);
            ////////////////////////////
//             tabContainer.setBackground(DefaultSettings.COMMENTS_LIKE_BAR_COLOR);
            /*JPanel tabContainer = new JPanel(new BorderLayout(DefaultSettings.HGAP, 0));
             tabContainer.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH + 2, 35));
             tabContainer.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_BOOK_BORDER_COLOR));
             tabContainer.setBackground(Color.WHITE);
             tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
             tabPanel.setOpaque(false);
             tabContainer.add(tabPanel, BorderLayout.WEST);

             JPanel tabWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
             tabWrapperPanel.setOpaque(false);
             tabWrapperPanel.add(tabContainer);
             container.add(tabWrapperPanel, BorderLayout.NORTH);*/
////////////////////////////////////////////
//            JPanel tabAndProfilelPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//            tabAndProfilelPanelWrapper.setOpaque(false);
//            tabAndProfilelPanelWrapper.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 35));
//            //     tabAndProfilelPanelWrapper.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
//            tabAndProfilePanel = new JPanel(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
//            tabAndProfilePanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 50, 38));
//            tabAndProfilePanel.setOpaque(false);
//
//            tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 9));
//            tabPanel.setOpaque(false);
//            tabAndProfilePanel.add(tabPanel, BorderLayout.WEST);
//
//            tabAndProfilelPanelWrapper.add(tabAndProfilePanel);
//            
////////////////////////////////////////////

            JPanel masterPanel = new JPanel();
            masterPanel.setOpaque(false);
            masterPanel.setLayout(new BorderLayout());

            mainContentPanel = new JPanel(new BorderLayout());
            mainContentPanel.setOpaque(false);
            masterPanel.add(mainContentPanel, BorderLayout.CENTER);
            container.add(masterPanel, BorderLayout.CENTER);

            changeCoverImage();
            buildTabPanel();
            this.setType(CircleProfile.TYPE_BOOK);
            this.onTabClick();

        } catch (Exception e) {
          //  e.printStackTrace();
        log.error("Error in initContainers ==>" + e.getMessage());
        }
    }

    public void addProfileCoverOptions() {
        try {

            coverImagePanel.setOpaque(false);
            coverImagePanel.setLayout(new BorderLayout());
            coverImagePanel.setBorder(new EmptyBorder(0, 15, 0, 15));

            JPanel panel = new JPanel(new BorderLayout(0, 0));
            panel.setOpaque(false);
            panel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT));
            // panel.setBorder(new MatteBorder(0, 1, 0, 1, DefaultSettings.DEFAULT_FEED_BORDER_COLOR));
            panel.setBorder(DefaultSettings.DEFAULT_FEED_BORDER);
            panel.add(coverImagePanel);

            JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            arrowPanel.setOpaque(false);
            arrowPanel.setPreferredSize(new Dimension(DefaultSettings.SMALL_PANEL_HEIGHT, 0));

            leavePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
            leavePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);

            deletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
            deletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);

            actionPopUpButton = DesignClasses.createImageButton(GetImages.SETTING_MINI, GetImages.SETTING_MINI_H, "Options");
            arrowPanel.add(actionPopUpButton);
            actionPopUpButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (InternetUnavailablityCheck.isInternetAvailable) {
                        if (adminStatus == 1) {
                            deletePopup.setVisible(actionPopUpButton, 155, -5);
                        } else {
                            leavePopup.setVisible(actionPopUpButton, 155, -5);
                        }
                    }
//                    actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 145, actionPopUp.getY() + 10);
                }
            });
            //  buildActionPopup();
            coverImagePanel.add(arrowPanel, BorderLayout.EAST);

            JPanel nameAndLoadingPanel = new JPanel(new BorderLayout());
            nameAndLoadingPanel.setOpaque(false);
            JPanel nameWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            nameWrapperPanel.setBorder(new EmptyBorder(70, 13, 0, 0));
            nameWrapperPanel.setOpaque(false);
            JPanel myNamePanel = new JPanel();
            myNamePanel.setBackground(new Color(204, 204, 204, 50));
            nameWrapperPanel.add(myNamePanel);

            myNameLabel = DesignClasses.makeLableBold1(this.singleCircleDto.getCircleName(), Color.WHITE, 18);
            myNamePanel.add(myNameLabel);
            nameAndLoadingPanel.add(nameWrapperPanel, BorderLayout.WEST);

            loadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            loadingPanel.setOpaque(false);
            nameAndLoadingPanel.add(loadingPanel, BorderLayout.CENTER);

            coverImagePanel.add(nameAndLoadingPanel, BorderLayout.CENTER);

            coverImageWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            coverImageWrapper.setOpaque(false);
            coverImageWrapper.add(panel);
            tabAndProfilelPanelWrapper.add(coverImageWrapper);
//            add(coverImageWrapper, BorderLayout.NORTH);
            coverImageWrapper.revalidate();
            coverImageWrapper.repaint();

        } catch (Exception ex) {
        }
    }

    private void changeCoverImage() {
        try {
            coverImagePanel.removeMouseListener(coverImageClickListener);
            if (singleCircleDto != null && this.singleCircleDto.getCoverImage() != null && this.singleCircleDto.getCoverImage().trim().length() > 0) {
                JsonFields imageDto = new JsonFields();
                imageDto.setUserIdentity(this.singleCircleDto.getCircleName());
                imageDto.setIurl(this.singleCircleDto.getCoverImage());
                imageDto.setImageId(this.singleCircleDto.getCoverImageId());
                coverImageClickListener = new ImageMousListenerInBook(imageDto, true);
                new LoadCoverImageInLabel(coverImageWrapper, coverImagePanel, null, imageDto, 0, 0, coverImageClickListener).start();
            } else {
                BufferedImage img = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
                coverImagePanel.setImage(img);
                coverImagePanel.revalidate();
            }
        } catch (Exception ex) {
            // log.error("Buffer img error1-->" + ex.getMessage());
        }
    }
    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_LEAVE)) {
                leavePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new LeaveCircle(circleId).start();
                }

            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {
                deletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteCircle(circleId).start();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }
    };
//    public void buildActionPopup() {
//        actionPopUp = new JPopupMenu();
//        if (InternetUnavailablityCheck.isInternetAvailable) {
//            if (adminStatus == 1) {
//                mnuDeleteCircle = new JMenuItem("Delete Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
//                actionPopUp.add(mnuDeleteCircle);
//                mnuDeleteCircle.addActionListener(actionListener);
//            } else {
//                mnuLeaveCircle = new JMenuItem("Leave Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
//                actionPopUp.add(mnuLeaveCircle);
//                mnuLeaveCircle.addActionListener(actionListener);
//            }
//        }
//    }

    private void buildTabPanel() {
        try {
            tabContainer = new JPanel(new BorderLayout(DefaultSettings.HGAP, 0));
            tabContainer.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, 50));
            tabContainer.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
            tabContainer.setBackground(Color.WHITE);
            tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            tabPanel.setOpaque(false);
            tabContainer.add(tabPanel, BorderLayout.WEST);

            JPanel tabWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            tabWrapperPanel.setOpaque(false);
            tabWrapperPanel.add(tabContainer);
            tabAndProfilelPanelWrapper.add(tabWrapperPanel);

            if (tabPanel != null) {
                tabPanel.removeAll();
                tabPanel.add(bookButton);
                tabPanel.add(new SeparatorPanel(1, 48, 8));
                bookButton.addMouseListener(mouseListener);
                tabPanel.add(contactListButton);
                contactListButton.addMouseListener(mouseListener);
            }
        } catch (Exception ex) {
        }
    }

    public UserBasicInfo getUserInfo(SingleMemberInCircleDto js) {
        UserBasicInfo basicinfo = new UserBasicInfo();
        if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
            basicinfo = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
        } else {
            if (js.getUserIdentity() != null) {
                basicinfo.setUserIdentity(js.getUserIdentity());
                basicinfo.setFullName(js.getUserIdentity());
            }
            if (js.getFullName() != null) {
                basicinfo.setFullName(js.getFullName());
            } else {
                basicinfo.setFullName(js.getUserIdentity());
            }
            /*if (js.getLastName() != null) {
             basicinfo.setLastName(js.getLastName());
             } else {
             basicinfo.setLastName("");
             }*/
        }

        return basicinfo;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if (actionPopUpButton != null && e.getSource() == actionPopUpButton) {
//                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 150, actionPopUp.getY() + 10);
//            }
//             else if (mnuDeleteCircle != null && e.getSource() == mnuDeleteCircle) {
//                //int dialogResult = JOptionPane.showConfirmDialog(null, NotificationMessages.DELETE_NOTIFICAITON, StaticFields.APP_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                // if (dialogResult == JOptionPane.YES_OPTION) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new DeleteCircle(circleId).start();
//                }
//            } else if (mnuLeaveCircle != null && e.getSource() == mnuLeaveCircle) {
//                //     int dialogResult = JOptionPane.showConfirmDialog(null, NotificationMessages.LEAVE_NOTIFICAITON, StaticFields.APP_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                //     if (dialogResult == JOptionPane.YES_OPTION) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new LeaveCircle(circleId).start();
//                }
//            } 
            if (bookButton != null && e.getSource() == bookButton) {
                setType(CircleProfile.TYPE_BOOK);
                onTabClick();
//            } else if (albumButton != null && e.getSource() == albumButton) {
//                onTabClick(CircleProfile.TYPE_ALBUM);
            } else if (contactListButton != null && e.getSource() == contactListButton) {
                setType(CircleProfile.TYPE_MEMBERS);
                onTabClick();
            }
        }
    };

    public void onTabClick() {
        mainContentPanel.removeAll();
        this.setTabSelectionColor(type);
        this.scrollPnlTop.setVisible(false);
        tabAndProfilelPanelWrapper.revalidate();
        tabAndProfilelPanelWrapper.repaint();
        if (type == CircleProfile.TYPE_BOOK) {
            boolean isLoadingFirstTime = false;
            if (NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId) == null || NewsFeedMaps.getInstance().getCircleNewsFeedId().get(circleId).isEmpty()) {
                (new SendFriendsInfoRequest(circleId, AppConstants.TYPE_CIRCLE_NEWSFEED, 0)).start();
                isLoadingFirstTime = true;
            }
            if (this.circleNewsFeedPanel == null) {
                this.circleNewsFeedPanel = new CircleNewsFeedPanel(this);
                mainContentPanel.add(this.circleNewsFeedPanel, BorderLayout.CENTER);
                if (isLoadingFirstTime) {
                    this.circleNewsFeedPanel.addBottomLoading();
                }
            } else {
                mainContentPanel.add(this.circleNewsFeedPanel, BorderLayout.CENTER);
            }
            if (isLoadingFirstTime == false) {
                this.circleNewsFeedPanel.loadTopForcefully();
            }
            this.circleNewsFeedPanel.scrollContent.getViewport().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (circleNewsFeedPanel != null && circleNewsFeedPanel.scrollContent.getVerticalScrollBar().isVisible()) {
                        scrollPnlTop.setVisible(true);
                    } else {
                        scrollPnlTop.setVisible(false);
                    }
                    tabAndProfilelPanelWrapper.revalidate();
                    tabAndProfilelPanelWrapper.repaint();
                }
            });
//        } else if (type == CircleProfile.TYPE_ALBUM) {
//            if (this.circleAlbumPanel == null) {
//                this.circleAlbumPanel = new CircleAlbumPanel(this);
//                mainContentPanel.add(this.circleAlbumPanel, BorderLayout.CENTER);
//            } else {
//                mainContentPanel.add(this.circleAlbumPanel, BorderLayout.CENTER);
//                this.circleAlbumPanel.loadFriendList();
//            }
//            this.circleAlbumPanel.downContent.getViewport().addChangeListener(new ChangeListener() {
//                @Override
//                public void stateChanged(ChangeEvent e) {
//                    if (circleAlbumPanel.downContent.getVerticalScrollBar().isVisible()) {
//                        scrollPnlTop.setVisible(true);
//                    } else {
//                        scrollPnlTop.setVisible(false);
//                    }
//                    tabAndProfilelPanelWrapper.revalidate();
//                    tabAndProfilelPanelWrapper.repaint();
//                }
//            });
        } else if (type == CircleProfile.TYPE_MEMBERS) {
            if (this.circleMembersListPanel == null) {
                this.circleMembersListPanel = new CircleMembersListPanel(this.circleId);
                mainContentPanel.add(this.circleMembersListPanel, BorderLayout.CENTER);
            } else {
                mainContentPanel.add(this.circleMembersListPanel, BorderLayout.CENTER);
            }
            if (circleMembersListPanel != null
                    && circleMembersListPanel.viewMemberListScrollPane.getVerticalScrollBar() != null
                    && circleMembersListPanel.viewMemberListScrollPane.getVerticalScrollBar().isVisible()) {
                scrollPnlTop.setVisible(true);
            } else {
                scrollPnlTop.setVisible(false);
            }

            this.circleMembersListPanel.viewMemberListScrollPane.getViewport().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (circleMembersListPanel != null && circleMembersListPanel.viewMemberListScrollPane.getVerticalScrollBar().isVisible()) {
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

    public void setTabSelectionColor(int type) {
        try {
            if (type == TYPE_BOOK) {
                bookButton.setSelected();
                contactListButton.setExit();
            } else if (type == TYPE_MEMBERS) {
                bookButton.setExit();
                contactListButton.setSelected();
            }
        } catch (Exception e) {
        }
    }

    public void checkSelectedTab() {
        if (type == TYPE_BOOK && circleNewsFeedPanel == null) {
            onTabClick();
        } else if (type == TYPE_MEMBERS && circleMembersListPanel == null) {
            onTabClick();
        }
    }
    
    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bookButton) {
                setType(TYPE_BOOK);
                onTabClick();
            } else if (e.getSource() == contactListButton) {
                setType(TYPE_MEMBERS);
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
            } 
        }
    };
}

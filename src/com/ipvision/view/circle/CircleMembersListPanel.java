/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.AddMemberInCircle;
import com.ipvision.service.DeleteCircleMember;
import com.ipvision.service.EditCircleMembers;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Wasif Islam
 */
public class CircleMembersListPanel extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CircleMembersListPanel.class);
    private long circleId;
    private int default_content_width = 635;
    private int default_content_height = 75;
    public JPanel memberListPanel;
    private JLabel lblMembers;
    private Map<String, SingleMemberInCircleDto> circleMembers;
    public JScrollPane viewMemberListScrollPane;
    private boolean loggedUserIsAdmin;
    private boolean loggedUserIsSuperAdmin;
    private JPanel circleMembersListPanel;
    private final JButton addButton;// = DesignClasses.createImageButton(GetImages.ADD_MEMBERS, GetImages.ADD_MEMBERS_H, "Add Members");
    private final JButton showMembers;// = DesignClasses.createImageButton(GetImages.SHOW_MEMBERS, GetImages.SHOW_MEMBERS_H, "Show Members");
    public final JButton saveButton;// = DesignClasses.createImageButton(GetImages.SAVE_CHANGES, GetImages.SAVE_CHANGES_H, "Save Changes");
    public final JButton resetButton;// = DesignClasses.createImageButton(GetImages.RESET_CHANGES, GetImages.RESET_CHANGES_H, "Reset Changes");
    private JPanel buttonPanel;
    private DropTarget dropTarget;
    private JLabel pleaswait;
    public JPanel contactListPanel;

    public CircleMembersListPanel(long circleId) {
        addButton = new RoundedCornerButton("+ Add Members", "Add Members");///DesignClasses.createImageButton(GetImages.ADD_CONTACT_INSTRUCTION, GetImages.ADD_CONTACT_INSTRUCTION, "Add Members");
        showMembers = new RoundedCornerButton("Show Members");//= DesignClasses.createImageButton(GetImages.SHOW_MEMBERS, GetImages.SHOW_MEMBERS_H, "Show Members");
        saveButton = new RoundedCornerButton("Save Changes");//= DesignClasses.createImageButton(GetImages.SAVE_CHANGES, GetImages.SAVE_CHANGES_H, "Save Changes");
        resetButton = new RoundedCornerButton("Reset Changes");//= DesignClasses.createImageButton(GetImages.RESET_CHANGES, GetImages.RESET_CHANGES_H, "Reset Changes");
        this.circleId = circleId;
        this.loggedUserIsSuperAdmin = MyfnfHashMaps.getInstance().getCircleLists().get(circleId).getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID);
        this.circleMembers = MyfnfHashMaps.getInstance().getCircleMembers().get(circleId);
        this.loggedUserIsAdmin = check_if_admin();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        initContents();
    }

    public void initContents() {
        JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainContainer.setOpaque(false);
        this.contactListPanel = new JPanel();
        this.contactListPanel.setLayout(new BoxLayout(this.contactListPanel, BoxLayout.Y_AXIS));
        this.contactListPanel.setOpaque(false);

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BorderLayout(5, 0));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        buttonPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH - 10, 40));
        lblMembers = DesignClasses.titleOfFriendList("Circle Members", 1, 13);
        lblMembers.setVisible(false);
        contactListPanel.add(buttonPanel);

        circleMembersListPanel = new JPanel();
        circleMembersListPanel.setLayout(new GridLayout(0, 3, 5, 5));
        circleMembersListPanel.setOpaque(false);
        //circleMembersListPanel.setMinimumSize(new Dimension(default_content_width, 300));
        contactListPanel.add(circleMembersListPanel);

        JPanel contactListContainer = new JPanel();
        contactListContainer.setLayout(new BorderLayout());
        contactListContainer.setBackground(Color.WHITE);
        contactListContainer.setBorder(new EmptyBorder(0, 5, 10, 5));
        contactListContainer.add(contactListPanel, BorderLayout.CENTER);
        JPanel wrpper = new JPanel(new BorderLayout());
        wrpper.setOpaque(false);
        wrpper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        wrpper.add(contactListContainer, BorderLayout.CENTER);
        mainContainer.add(wrpper);

        viewMemberListScrollPane = DesignClasses.getDefaultScrollPane(mainContainer);
        viewMemberListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(viewMemberListScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(actionListener);
        showMembers.addActionListener(actionListener);
        resetButton.addActionListener(actionListener);
        saveButton.addActionListener(actionListener);

        buildMembersList();
    }

    private boolean check_if_admin() {
        if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleLists().isEmpty()) {
            if (MyfnfHashMaps.getInstance().getCircleLists().get(circleId).getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                return true;
            } else if (MyfnfHashMaps.getInstance().getCircleMembers().get(circleId) != null
                    && MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).get(MyFnFSettings.LOGIN_USER_ID) != null
                    && MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).get(MyFnFSettings.LOGIN_USER_ID).isAdmin()) {
                return true;
            }
        }
        return false;
    }

    public void buildMembersList() {
        try {
            viewMemberListScrollPane.setBorder(null);
            this.circleMembersListPanel.removeAll();

            if (dropTarget != null) {
                dropTarget.removeDropTargetListener(dropTargetListener);
            }

            circleMembers = MyfnfHashMaps.getInstance().getCircleMembers().get(circleId);
            int sz = this.circleMembers.size() - 1;
            lblMembers.setText("Circle Members" + " (" + sz + ")");
            if (circleMembers.size() > 0) {
                lblMembers.setVisible(true);
                for (String userid : circleMembers.keySet()) {
                    if (!userid.equals(MyFnFSettings.LOGIN_USER_ID)) {
                        SingleMemberInCircleDto singleUser = circleMembers.get(userid);
                        circleMembersListPanel.add(addContent(singleUser));
//                        SingleCircleMember s =new SingleCircleMember(singleUser);
//                       circleMembersListPanel.add(s.addd(singleUser));

                    }
                }
            } else {
                lblMembers.setVisible(false);
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    viewMemberListScrollPane.getVerticalScrollBar().setValue(0);
                }
            });
            buttonPanel(1);
            circleMembersListPanel.revalidate();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in buildMembersList ==>" + e.getMessage());
        }
    }

    public void buildAddCircleMembers() {
        try {
            viewMemberListScrollPane.setBorder(BorderFactory.createDashedBorder(DefaultSettings.APP_DEFAULT_MENUBAR_BG_HOVER_COLOR, 1.5f, 4.0f, 2.0f, false));
            this.circleMembersListPanel.removeAll();

            dropTarget = new DropTarget(this.viewMemberListScrollPane, dropTargetListener);

            boolean isAdded = false;
            Map<String, Boolean> map = MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId);
            if (map != null && !map.isEmpty()) {
                for (String userIdentity : map.keySet()) {
                    UserBasicInfo entity = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
                    if (entity != null) {
                        this.circleMembersListPanel.add(addDroppedContent(entity));
                        isAdded = true;
                    }
                }
            }
            if (!isAdded) {
                //JLabel createDragLabel = DesignClasses.makeJLabel_normal("Drag here from contact list that you want to add", 12, DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                lblMembers.setText("Drag here from your friendlist");
                lblMembers.setVisible(true);
                //this.circleMembersListPanel.add(createDragLabel);
            } else {
                lblMembers.setVisible(false);
            }
            buttonPanel(2);
            circleMembersListPanel.revalidate();
        } catch (Exception e) {
            /// e.printStackTrace();
            log.error("Error in buildAddCircleMembers method ==>" + e.getMessage());
        }
    }

    private JPanel addContent(final SingleMemberInCircleDto entity) {
        final JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.setPreferredSize(new Dimension(190, 50));
        rowPanel.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        rowPanel.setBackground(Color.WHITE);
        try {
            String name = "No Name";
            /*if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0) && (entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
             name = entity.getFullName() + " " + entity.getLastName();
             } else */
            if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0)) {
                name = entity.getFullName();
            } /*else if ((entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
             name = entity.getLastName();
             }*/

            UserBasicInfo friend_info = FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity());
            JLabel frienImageLabel = new JLabel();
//            if (friend_info != null) {
//                BufferedImage img = DesignClasses.getBufferImageFromurl(friend_info.getProfileImage(), default_content_height - 36);
//                frienImageLabel.setIcon(new ImageIcon(img));
//            } else {
//                BufferedImage img = DesignClasses.getUnknownImage(true);
//                img = DesignClasses.scaleImage(default_content_height - 36, default_content_height - 36, img);
//                frienImageLabel.setIcon(new ImageIcon(img));
//            }

            if (friend_info != null && friend_info.getProfileImage() != null) {
                ImageHelpers.addProfileImageThumb(frienImageLabel, friend_info.getProfileImage());
            } else {
                BufferedImage img2 = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
                frienImageLabel.setIcon(new ImageIcon(img2));
                img2.flush();
                img2 = null;
            }
            JPanel friendImagePanel = new JPanel(new GridLayout());
            friendImagePanel.setBorder(new EmptyBorder(3, 3, 3, 0));
            //friendImagePanel.setPreferredSize(new Dimension(default_content_height - 6, default_content_height - 6));
            friendImagePanel.setOpaque(false);
            friendImagePanel.add(frienImageLabel);
            rowPanel.add(friendImagePanel, BorderLayout.WEST);

            JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            wrapperPanel.setBorder(new EmptyBorder(3, 0, 3, 0));
            wrapperPanel.setOpaque(false);
            //wrapperPanel.setBackground(Color.red);

            JPanel infoContainer = new JPanel();
            infoContainer.setPreferredSize(new Dimension(138, 30));
            infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
            infoContainer.setOpaque(false);
            //infoContainer.setBackground(Color.GREEN);

            JLabel fullNameLabel = DesignClasses.makeAncorLabelDefaultColor(name, 0, 12);
            fullNameLabel.setPreferredSize(new Dimension(138, 15));

            JPanel fullNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            fullNamePanel.setOpaque(false);
            fullNamePanel.add(fullNameLabel);
            infoContainer.add(fullNamePanel);

            String displayIdentity = CircleProfile.ONLY_MEMBER;
            if (MyfnfHashMaps.getInstance().getCircleLists().get(circleId) != null && MyfnfHashMaps.getInstance().getCircleLists().get(circleId).getSuperAdmin().equals(entity.getUserIdentity())) {
                displayIdentity = CircleProfile.SUPER_ADMIN;
            } else if (entity.isAdmin()) {
                displayIdentity = CircleProfile.ADMIN;
            }

            final JLabel memberInfoLabel = DesignClasses.makeJLabel_normal(displayIdentity, 9, DefaultSettings.APP_DEFAULT_FONT_COLOR);

            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            statusPanel.setOpaque(false);
            statusPanel.add(memberInfoLabel);
            infoContainer.add(statusPanel);

            JPanel buttonPanel = new JPanel(new GridLayout());
            buttonPanel.setPreferredSize(new Dimension(20, 0));
            buttonPanel.setOpaque(false);

            if (loggedUserIsAdmin) {
                boolean show = false;
                final JButton arrowButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Settings");

                ///
                ///   
//                final JPopupMenu popupMore = new JPopupMenu();
//                final JMenuItem removeMember = new JMenuItem("Remove Member", DesignClasses.return_image(GetImages.CLOSE_MINI));
//                final JMenuItem adminMemberLabel; //= new JMenuItem(entity.isAdmin() ? CircleProfile.REMOVE_FROM_ADMIN : CircleProfile.MAKE_ADMIN);
//                if (entity.isAdmin()) {
//                    adminMemberLabel = new JMenuItem(CircleProfile.REMOVE_FROM_ADMIN, DesignClasses.return_image(GetImages.CLOSE_MINI));
//                } else {
//                    adminMemberLabel = new JMenuItem(CircleProfile.MAKE_ADMIN);
//                }
                if (loggedUserIsSuperAdmin || !entity.isAdmin()) {
//                    popupMore.add(removeMember);
                    show = true;
                }
                if (loggedUserIsSuperAdmin) {
//                    popupMore.add(adminMemberLabel);
                    show = true;
                }
                if (show) {
                    buttonPanel.add(arrowButton);
                }
                final String mnu_RemoveMember = "Remove Member";
                final String mnu_MAKE_ADMIN = CircleProfile.MAKE_ADMIN;
                final String mnu_REMOVE_FROM_ADMIN = CircleProfile.REMOVE_FROM_ADMIN;
                final JCustomMenuPopup memeberAndMakeAdminPopup = new JCustomMenuPopup(null, JCustomMenuPopup.TYPE_JOIN_LEAVE);

                final JCustomMenuPopup memeberAndRemoveAdminPopup = new JCustomMenuPopup(null, JCustomMenuPopup.TYPE_JOIN_LEAVE);

                final JCustomMenuPopup onlyRemoveMemberPopup = new JCustomMenuPopup(null, JCustomMenuPopup.TYPE_ONLY_LEAVE);

                MouseListener menuListener = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                        if (menu.text.equalsIgnoreCase(mnu_RemoveMember)) {
                            onlyRemoveMemberPopup.hideThis();
                            HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                            if (JOptionPanelBasics.YES_NO) {
                                new DeleteCircleMember(circleId, entity.getUserIdentity()).start();
                            }
                        } else if (menu.text.equalsIgnoreCase(mnu_REMOVE_FROM_ADMIN)) {
                            memeberAndRemoveAdminPopup.hideThis();
                            if (entity.isAdmin()) {
                                memberInfoLabel.setText(CircleProfile.ONLY_MEMBER);
                                new EditCircleMembers(memberInfoLabel, circleId, entity.getUserIdentity(), Boolean.FALSE).start();
                            }
                        } else if (menu.text.equalsIgnoreCase(mnu_MAKE_ADMIN)) {

                            memeberAndMakeAdminPopup.hideThis();
                            memberInfoLabel.setText(CircleProfile.ADMIN);
                            new EditCircleMembers(memberInfoLabel, circleId, entity.getUserIdentity(), Boolean.TRUE).start();

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
                memeberAndMakeAdminPopup.setMouseListener(menuListener);
                memeberAndRemoveAdminPopup.setMouseListener(menuListener);
                onlyRemoveMemberPopup.setMouseListener(menuListener);

                memeberAndMakeAdminPopup.addMenu(mnu_RemoveMember, GetImages.LEAVE_PHOTO);
                memeberAndMakeAdminPopup.addMenu(mnu_MAKE_ADMIN, GetImages.EDIT_PHOTO);// need proper icon for "add admin", now temporary used edit_photo

                memeberAndRemoveAdminPopup.addMenu(mnu_RemoveMember, GetImages.LEAVE_PHOTO);
                memeberAndRemoveAdminPopup.addMenu(mnu_REMOVE_FROM_ADMIN, GetImages.DELETE_PHOTO);// need proper icon for "remove from admin", now temporary used delete_photo

                onlyRemoveMemberPopup.addMenu(mnu_RemoveMember, GetImages.LEAVE_PHOTO);

                arrowButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (InternetUnavailablityCheck.isInternetAvailable) {
//                            popupMore.pack();
//                            Point pos = new Point();
//                            pos.x = arrowButton.getWidth() - 5;
//                            pos.y = arrowButton.getHeight() - 15;
//                            popupMore.show(arrowButton, pos.x, pos.y);
                            if (loggedUserIsSuperAdmin) {
                                if (entity.isAdmin()) {
                                    memeberAndRemoveAdminPopup.setVisible(arrowButton, 159, -16);
                                } else {

                                    memeberAndMakeAdminPopup.setVisible(arrowButton, 159, -16);
                                }

                            } else if (!entity.isAdmin()) {
                                onlyRemoveMemberPopup.setVisible(arrowButton, 159, -16);
                            }

                        }
                    }
                });

                rowPanel.add(buttonPanel, BorderLayout.EAST);

//                removeMember.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
//                        if (JOptionPanelBasics.YES_NO) {
//                            new DeleteCircleMember(circleId, entity.getUserIdentity()).start();
//                        }
//                    }
//                });
//
//                adminMemberLabel.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        if (entity.isAdmin()) {
//                            adminMemberLabel.setText(CircleProfile.MAKE_ADMIN);
//                            memberInfoLabel.setText(CircleProfile.ONLY_MEMBER);
//                            new EditCircleMembers(adminMemberLabel, memberInfoLabel, circleId, entity.getUserIdentity(), Boolean.FALSE).start();
//                        } else {
//                            adminMemberLabel.setText(CircleProfile.REMOVE_FROM_ADMIN);
//                            memberInfoLabel.setText(CircleProfile.ADMIN);
//                            new EditCircleMembers(adminMemberLabel, memberInfoLabel, circleId, entity.getUserIdentity(), Boolean.TRUE).start();
//                        }
//                    }
//                });
            }

            fullNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            fullNameLabel.addMouseListener(new MouseAdapter() {
                Font original;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (entity.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().action_of_myProfile_button();
                    } else if (FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity()) != null) {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().showFriendProfile(entity.getUserIdentity());
                    } else {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().showUnknownProfile(getUserInfo(entity));
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    original = e.getComponent().getFont();
                    Map attributes = original.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    e.getComponent().setFont(original.deriveFont(attributes));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    e.getComponent().setFont(original);
                }
            });
            wrapperPanel.add(infoContainer);
            rowPanel.add(wrapperPanel, BorderLayout.CENTER);

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Exception in here ==>" + e.getMessage());
        }

        return rowPanel;

    }

    private JPanel addDroppedContent(final UserBasicInfo entity) {
        final JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        rowPanel.setBackground(Color.WHITE);

        try {
            String name = "No Name";
            /*if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0) && (entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
             name = entity.getFullName() + " " + entity.getLastName();
             } else*/ if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0)) {
                name = entity.getFullName();
            } /*else if ((entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
             name = entity.getLastName();
             }*/

            UserBasicInfo friend_info = FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity());
            JLabel frienImageLabel = new JLabel();
//            if (friend_info != null) {
//                BufferedImage img = DesignClasses.getBufferImageFromurl(friend_info.getProfileImage(), default_content_height - 36);
//                frienImageLabel.setIcon(new ImageIcon(img));
//            } else {
//                BufferedImage img = DesignClasses.getUnknownImage(true);
//                img = DesignClasses.scaleImage(default_content_height - 36, default_content_height - 36, img);
//                frienImageLabel.setIcon(new ImageIcon(img));
//            }
            if (friend_info.getProfileImage() != null) {
                ImageHelpers.addProfileImageThumb(frienImageLabel, friend_info.getProfileImage());
            } else {
                BufferedImage img2 = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
                frienImageLabel.setIcon(new ImageIcon(img2));
                img2 = null;
            }
            JPanel friendImagePanel = new JPanel(new GridLayout());
            friendImagePanel.setBorder(new EmptyBorder(3, 3, 3, 0));
            //friendImagePanel.setPreferredSize(new Dimension(default_content_height - 6, default_content_height - 6));
            friendImagePanel.setOpaque(false);
            friendImagePanel.add(frienImageLabel);
            rowPanel.add(friendImagePanel, BorderLayout.WEST);

            JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            wrapperPanel.setBorder(new EmptyBorder(3, 0, 3, 0));
            wrapperPanel.setOpaque(false);
            //wrapperPanel.setBackground(Color.red);

            JPanel infoContainer = new JPanel();
            infoContainer.setPreferredSize(new Dimension(138, 33));
            infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
            infoContainer.setOpaque(false);
            //infoContainer.setBackground(Color.GREEN);

            JLabel fullNameLabel = DesignClasses.makeAncorLabelDefaultColor(name, 0, 12);
            fullNameLabel.setPreferredSize(new Dimension(138, 15));

            JPanel fullNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            fullNamePanel.setOpaque(false);
            fullNamePanel.add(fullNameLabel);
            infoContainer.add(fullNamePanel);

            final JLabel memberInfoLabel = DesignClasses.makeJLabel_normal(CircleProfile.ADMIN, 9, DefaultSettings.APP_DEFAULT_FONT_COLOR);
            final JButton removeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Remove");
            final JCheckBox checkBox = new JCheckBox("");
            checkBox.setOpaque(false);
            checkBox.setToolTipText("Admin");
            try {
                boolean admin_true = MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId).get(entity.getUserIdentity());
                checkBox.setSelected(admin_true);
                checkBox.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).put(entity.getUserIdentity(), true);
                        }
                        if (e.getStateChange() != ItemEvent.SELECTED) {
                            MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).put(entity.getUserIdentity(), false);
                        }
                    }
                });
            } catch (Exception e) {
               // e.printStackTrace();
                log.error("Exception in here ==>" + e.getMessage());
            }
            JPanel buttonPanel = new JPanel(new GridLayout());
            buttonPanel.setPreferredSize(new Dimension(20, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(removeButton);
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).remove(entity.getUserIdentity());
                        buildAddCircleMembers();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            rowPanel.add(buttonPanel, BorderLayout.EAST);

            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            statusPanel.setOpaque(false);
            statusPanel.add(memberInfoLabel);
            statusPanel.add(checkBox);
            infoContainer.add(statusPanel);

            fullNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fullNameLabel.addMouseListener(new MouseAdapter() {
                Font original;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (entity.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().action_of_myProfile_button();
                    } else if (FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity()) != null) {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().showFriendProfile(entity.getUserIdentity());
                    } else {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().showUnknownProfile(getUserInfo(entity));
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    original = e.getComponent().getFont();
                    Map attributes = original.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    e.getComponent().setFont(original.deriveFont(attributes));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    e.getComponent().setFont(original);
                }
            });

            wrapperPanel.add(infoContainer);
            rowPanel.add(wrapperPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            //e.printStackTrace();
        log.error("Exception in here ==>" + e.getMessage());
        }

        return rowPanel;
    }

    DropTargetListener dropTargetListener = new DropTargetListener() {

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                Transferable transferable = dtde.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    String dragContents = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    dtde.getDropTargetContext().dropComplete(true);
                    addDroppedMember(dragContents);
                } else {
                    dtde.rejectDrop();
                }
            } catch (IOException e) {
                dtde.rejectDrop();
            } catch (UnsupportedFlavorException e) {
                dtde.rejectDrop();
            }
        }

    };

    private void addDroppedMember(String userIdentity) {
        if (MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId) == null) {
            MyfnfHashMaps.getInstance().getTempCircleMembersStore().put(this.circleId, new ConcurrentHashMap<String, Boolean>());
        }
        UserBasicInfo basicinfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
        if (basicinfo != null && Objects.equals(basicinfo.getFriendShipStatus(), StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) && MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId).get(userIdentity) == null) {
            if (!check_already_in_circle(userIdentity)) {
                MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId).put(userIdentity, false);
                buildAddCircleMembers();
                // pleaswait.setText("");
            } else {
                // pleaswait.setText("Already in Circle");
            }
        } else {
            // pleaswait.setText("Not in your friendlist");
        }
    }

    public boolean check_already_in_circle(String user_id) {
        if (MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).get(user_id) != null) {
            return true;
        }
        return false;
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

    private void buttonPanel(int type) {
        try {

            if (buttonPanel != null) {
                buttonPanel.removeAll();

                if (type == 1) {
                    buttonPanel.add(lblMembers, BorderLayout.WEST);
                    if (check_if_admin()) {
                        buttonPanel.add(addButton, BorderLayout.EAST);
                    }

                } else if (type == 2) {
                    if (MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId) != null && MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId).size() > 0) {
                        pleaswait = DesignClasses.makeAncorLabel("Total selected members: " + MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(this.circleId).size(), 0, 11);
                        if (InternetUnavailablityCheck.isInternetAvailable) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }

                        JPanel saveCancel = new JPanel(new FlowLayout());
                        saveCancel.setPreferredSize(new Dimension(250, 0));
                        saveCancel.setOpaque(false);
                        saveCancel.add(saveButton);
                        saveCancel.add(resetButton);

                        buttonPanel.add(saveCancel, BorderLayout.WEST);
                        buttonPanel.add(pleaswait, BorderLayout.CENTER);
                    } else {
                        buttonPanel.add(lblMembers, BorderLayout.WEST);
                    }

                    buttonPanel.add(showMembers, BorderLayout.EAST);

                }

                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        } catch (Exception e) {
        }

    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                buildAddCircleMembers();
            } else if (e.getSource() == showMembers) {
                buildMembersList();
            } else if (e.getSource() == resetButton) {
                MyfnfHashMaps.getInstance().getTempCircleMembersStore().clear();
                buildAddCircleMembers();
            } else if (e.getSource() == saveButton) {
                pleaswait.setText("Please wait...");
                saveButton.setVisible(false);
                resetButton.setVisible(false);
                if (MyfnfHashMaps.getInstance().getTempCircleMembersStore().size() > 0) {
                    new AddMemberInCircle(pleaswait, circleId, saveButton, resetButton).start();
                }
            }
        }

    };
}

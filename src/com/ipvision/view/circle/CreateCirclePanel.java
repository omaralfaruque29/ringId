/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Faiz Ahmed
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.CreateCircleRequest;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.view.utility.ImageHelpers;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import com.ipvision.view.utility.CenterLayout;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class CreateCirclePanel extends JPanel implements AncestorListener{

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateCirclePanel.class);
    //  private JPanel container;
    private JPanel changeablePanel;
    private JTextField circle_Name_text_field;
    private JPanel rightCircleMember;
    private JPanel selectedFriends;
    private JLabel pleaseWait;
    private JLabel addedMme;
    private JButton buttonCreate;
    private JButton buttonReset;
    private JButton buttonBack;
    private static String defaultCircleName = "Enter Circle Name..";

    public CreateCirclePanel() {
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        setLayout(new CenterLayout(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 20));
        initContainers();
    }

    private void initContainers() {
        try {
            addAncestorListener(this);
            JPanel wrapperPanel = new JPanel();
           // wrapperPanel.setBackground(Color.WHITE);
            wrapperPanel.setOpaque(false);
            wrapperPanel.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP + 2));
            wrapperPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

            JPanel createResetBackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            createResetBackPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 50));
            createResetBackPanel.setBackground(Color.WHITE);
            createResetBackPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));

            buttonCreate = new RoundedCornerButton("Create", "Create");
            buttonReset = new RoundedCornerButton("Reset", "Reset");
            buttonBack = new RoundedCornerButton("Back", "Back");

            createResetBackPanel.add(Box.createRigidArea(new Dimension(175, 40)));
            createResetBackPanel.add(buttonCreate);
            createResetBackPanel.add(Box.createRigidArea(new Dimension(40, 40)));
            createResetBackPanel.add(buttonReset);
            createResetBackPanel.add(Box.createRigidArea(new Dimension(40, 40)));
            createResetBackPanel.add(buttonBack);

            wrapperPanel.add(createResetBackPanel, BorderLayout.NORTH);

//            JPanel headerPanel = new JPanel();
//            headerPanel.setLayout(new BorderLayout(5, 5));
//            //   headerPanel.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
//            headerPanel.setOpaque(false);
//            JLabel headLabel = DesignClasses.makeLableBold1("Create New Group", 18);
//            headLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
//            headerPanel.add(headLabel, BorderLayout.WEST);
//
//            JPanel backPanel = new JPanel();
//            backPanel.setOpaque(false);
//            backPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//            backPanel.add(backButton);
//            backButton.addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    GuiRingID.getInstance().getMainRight().showGroupViewPanel();
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
//                }
//            });
//
//            headerPanel.add(backPanel, BorderLayout.EAST);
//
//            add(headerPanel, BorderLayout.NORTH);
//
            changeablePanel = new JPanel();
            changeablePanel.setBorder((new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
            //changeablePanel.setOpaque(false);
            changeablePanel.setBackground(Color.WHITE);
            changeablePanel.setLayout(new BorderLayout(DefaultSettings.HGAP + 2, DefaultSettings.VGAP + 2));
          
            JPanel createGroupName_selectedMemberPanel = new JPanel(new BorderLayout());
            createGroupName_selectedMemberPanel.setOpaque(false);
            createGroupName_selectedMemberPanel.setBorder(new EmptyBorder(20, 55, 2, 55));
            // topInviteContainer.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            JLabel createGroupTitle = DesignClasses.makeJLabelFullName("Circle Name:");
            createGroupTitle.setBorder(new EmptyBorder(1, 0, 1, 5));

            circle_Name_text_field = DesignClasses.makeTextFieldLimitedTextSize(defaultCircleName, 230, 25, 100);
            circle_Name_text_field.setForeground(DefaultSettings.disable_font_color);// at first font color make grey 
            circle_Name_text_field.grabFocus();
            circle_Name_text_field.setFocusable(true);
            circle_Name_text_field.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    circle_Name_text_field.setText("");
                    circle_Name_text_field.setForeground(null);// when it focused gained -->its set null that means default font color --->black
                }

                @Override
                public void focusLost(FocusEvent e) {

                }
            });

            JPanel groupNamepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            groupNamepanel.setOpaque(false);
            groupNamepanel.add(createGroupTitle);
            groupNamepanel.add(circle_Name_text_field);
            groupNamepanel.setBorder(new EmptyBorder(15, 0, 0, 0));
            createGroupName_selectedMemberPanel.add(groupNamepanel, BorderLayout.WEST);

            JPanel createNewGroup = DesignClasses.createlabelOvalPanel("          Create New Circle          ");
            createGroupName_selectedMemberPanel.add(createNewGroup, BorderLayout.NORTH);
            changeablePanel.add(createGroupName_selectedMemberPanel, BorderLayout.NORTH);

            pleaseWait = DesignClasses.makeJLabelFullName("");
            pleaseWait.setBorder(new EmptyBorder(0, 60, 0, 5));

            JPanel centerOfchangeablePanel = new JPanel();
            centerOfchangeablePanel.setOpaque(false);
            centerOfchangeablePanel.setLayout(new BorderLayout());
            changeablePanel.add(centerOfchangeablePanel, BorderLayout.CENTER);

            JPanel addMemberPanel = new JPanel();
            addMemberPanel.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
            addMemberPanel.setOpaque(false);

            JPanel notifyStringPanel = new JPanel(new BorderLayout());
            //  addMemberString.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            notifyStringPanel.setOpaque(false);

            addedMme = DesignClasses.makeJLabelFullName("");
            addedMme.setBorder(new EmptyBorder(15, 15, 2, 5));
            addedMme.setOpaque(false);
            createGroupName_selectedMemberPanel.add(addedMme, BorderLayout.EAST);
//
//            JPanel buttonsPanel = new JPanel();
//            buttonsPanel.setOpaque(false);
//            buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
//

            buttonCreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonCreate.grabFocus();

                    if (circle_Name_text_field.getText().trim().length() > 0 && !(circle_Name_text_field.getText().equals(defaultCircleName))) {

                        if (MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().size() > 0) {
                            new CreateCircleRequest(pleaseWait, buttonCreate, circle_Name_text_field.getText()).start();
                        } else {
                            pleaseWait.setBackground(Color.RED);

                            pleaseWait.setText("No member added!!!!");

                        }
                    } else {
                        pleaseWait.setBackground(Color.RED);
                        pleaseWait.setText("Please add Circle name!!!!");
                    }
                }
            });

            buttonReset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        circle_Name_text_field.setText(defaultCircleName);
                        circle_Name_text_field.setForeground(DefaultSettings.disable_font_color);
                        pleaseWait.setText("");
                        buttonReset.grabFocus();
                        clear_selected_members();
                        resetNumberofAddedMembers();
                        addSelectedCircleFreindList();
                    } catch (Exception eee) {
                    }
                }
            });
            buttonBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        GuiRingID.getInstance().getMainRight().showCircleViewPanel();
                    } catch (Exception eee) {
                    }
                }
            });
//
//            buttonsPanel.add(buttonCreate);
//            buttonsPanel.add(buttonReset);
//
            if (InternetUnavailablityCheck.isInternetAvailable) {
                buttonCreate.setEnabled(true);
            } else {
                buttonCreate.setEnabled(false);
            }

//            addMemberString.setBorder(new EmptyBorder(0, 60, 0, 0));
//            addMemberString.add(buttonsPanel, BorderLayout.EAST);
            notifyStringPanel.add(pleaseWait, BorderLayout.WEST);
            addMemberPanel.add(notifyStringPanel, BorderLayout.PAGE_START);

            rightCircleMember = returnPanel();
            rightCircleMember.setOpaque(false);
            // rightGroupmember.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            addMemberPanel.add(rightCircleMember, BorderLayout.CENTER);

            centerOfchangeablePanel.add(addMemberPanel, BorderLayout.CENTER);
            wrapperPanel.add(changeablePanel, BorderLayout.CENTER);

            addRightPanelDetails();
            resetNumberofAddedMembers();

            add(wrapperPanel);
        } catch (Exception ex) {
        }
    }

    private void addRightPanelDetails() {
        DropAreaPanel selectedFriendCon = new DropAreaPanel(this);
        selectedFriendCon.setLayout(new BorderLayout());//selectedFriendCon.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0)); // new WrapLayout(WrapLayout.LEFT, 10, 10)
        selectedFriendCon.setBorder(new EmptyBorder(2, 60, 2, 60));
        selectedFriendCon.setOpaque(false);

        selectedFriends = new JPanel();;//new FlowLayout(FlowLayout.LEFT, 5,0));
        selectedFriends.setLayout(new WrapLayout(WrapLayout.LEFT, 25, 10));
        selectedFriends.setOpaque(false);

        JScrollPane freindlistScorllPanel = DesignClasses.getDefaultScrollPane(selectedFriends);//new JScrollPane(selectedFriendCon);
        freindlistScorllPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        freindlistScorllPanel.setBorder(BorderFactory.createDashedBorder(DefaultSettings.APP_DEFAULT_MENUBAR_BG_HOVER_COLOR, 1.5f, 4.0f, 2.0f, false));

        selectedFriendCon.add(freindlistScorllPanel);
        rightCircleMember.add(selectedFriendCon, BorderLayout.CENTER);
        addSelectedCircleFreindList();
//               DropAreaPanel selectedFriendCon = new DropAreaPanel(this);
//        selectedFriendCon.setLayout(new BorderLayout());
//        //selectedFriendCon.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0)); // new WrapLayout(WrapLayout.LEFT, 10, 10)
//        selectedFriendCon.setOpaque(false);
//
//        selectedFriends = new JPanel();;//new FlowLayout(FlowLayout.LEFT, 5,0));
//        selectedFriends.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10));
//        //selectedFriends.setPreferredSize(new Dimension(200, 0));
//        selectedFriends.setOpaque(false);
//        selectedFriendCon.add(selectedFriends, BorderLayout.NORTH);
//        //Scrollbar  scrollbar = new Scrollbar(Scrollbar.VERTICAL);
//        // selectedFriends.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));//(new BoxLayout(selectedFriends, BoxLayout.Y_AXIS));
//
//        JScrollPane freindlistScorllPanel = DesignClasses.getDefaultScrollPane(selectedFriendCon);//new JScrol
    }

    private void addDroppedMember(String userIdentity) {
        UserBasicInfo basicinfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
        if (basicinfo != null && Objects.equals(basicinfo.getFriendShipStatus(), StatusConstants.FRIENDSHIP_STATUS_ACCEPTED)) {
            if (MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().get(userIdentity) == null) {
                MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().put(userIdentity, false);
                addSelectedCircleFreindList();
                pleaseWait.setText("");
            } 
//            else {
//                pleaseWait.setBackground(Color.RED);
//                pleaseWait.setText("Already selected");
//            }
        } else {
            pleaseWait.setBackground(Color.RED);
            pleaseWait.setText("Not in your friendlist");
        }
    }

    public synchronized void addSelectedCircleFreindList() {
        boolean isAdded = false;

        if (selectedFriends != null && selectedFriends.isDisplayable()) {
            try {
                if (selectedFriends != null) {
                    selectedFriends.removeAll();
                    if (MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate() != null && !MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().isEmpty()) {
                        for (String key : MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().keySet()) {
                            UserBasicInfo single_profile = FriendList.getInstance().getFriend_hash_map().get(key);
                            if (single_profile != null) {
                                isAdded = true;
                                selectedFriends.add(create_selected_SingleFriends(single_profile));

                                resetNumberofAddedMembers();
                            }
                        }
                    }
                }
                selectedFriends.revalidate();
                selectedFriends.repaint();
            } catch (Exception eee) {
            }
        }

        if (!isAdded) {
            addedMme.setVisible(false);
            JLabel createDragLabel = DesignClasses.makeJLabel_normal("                                 Drag here from contact list that you want to add ", 12, DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
            selectedFriends.add(createDragLabel);
        } else {
            addedMme.setVisible(true);
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

    private JPanel create_selected_SingleFriends(final UserBasicInfo entity) {

        final JPanel holder = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        holder.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        holder.setBackground(Color.WHITE);
        // holder.setOpaque(false);
        holder.add(Box.createRigidArea(new Dimension(2, 1)));

        final JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.setPreferredSize(new Dimension(210, 70));
        // rowPanel.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        rowPanel.setBackground(Color.WHITE);
        holder.add(rowPanel);
        holder.add(Box.createRigidArea(new Dimension(2, 1)));
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
            if (friend_info != null) {
//                BufferedImage img = DesignClasses.getBufferImageFromurl(friend_info.getProfileImage(), default_content_height - 36);
//                frienImageLabel.setIcon(new ImageIcon(img));
                //ImageHelpers.addProfileImageThumb(frienImageLabel, friend_info.getProfileImage(), DefaultSettings.PROFILE_PIC_WIDTH);
                ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, friend_info.getProfileImage(), 39, HelperMethods.getShortName(entity.getFullName()));
                //ImageHelpers.addProfileImageThumb(frienImageLabel, friend_info.getProfileImage(),39);
            }
//            else {
//                BufferedImage img = ImageHelpers.getUnknownImage39();
//                // img = DesignClasses.scaleImage(default_content_height - 36, default_content_height - 36, img);
//                frienImageLabel.setIcon(new ImageIcon(img));
//                img.flush();
//                img = null;
//
//            }
            JPanel friendImagePanel = new JPanel(new GridLayout());
            friendImagePanel.setBorder(new EmptyBorder(3, 3, 3, 0));
            //friendImagePanel.setPreferredSize(new Dimension(default_content_height - 6, default_content_height - 6));
            friendImagePanel.setOpaque(false);
            friendImagePanel.add(frienImageLabel);
            rowPanel.add(friendImagePanel, BorderLayout.WEST);

            JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            wrapperPanel.setBorder(new EmptyBorder(12, 0, 3, 0));
            wrapperPanel.setOpaque(false);

            JPanel infoContainer = new JPanel();
            //   infoContainer.setPreferredSize(new Dimension(138, 33));
            infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
            infoContainer.setOpaque(false);

            JLabel fullNameLabel = DesignClasses.makeAncorLabelDefaultColor(name, 0, 12);
            fullNameLabel.setPreferredSize(new Dimension(125, 15));

            JPanel fullNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            fullNamePanel.setOpaque(false);
            fullNamePanel.add(fullNameLabel);
            infoContainer.add(fullNamePanel);

            final JLabel memberInfoLabel = DesignClasses.makeJLabel_normal(CircleProfile.ADMIN, 9, DefaultSettings.APP_DEFAULT_FONT_COLOR);
            final JButton removeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_BOLD, GetImages.BUTTON_CLOSE_BOLD_H, "Remove");

            final JCheckBox checkBox = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
            checkBox.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
            checkBox.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
            checkBox.setOpaque(false);
            checkBox.setToolTipText("Admin");
            try {
                boolean admin_true = MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().get(entity.getUserIdentity());
                checkBox.setSelected(admin_true);
                checkBox.addItemListener(
                        new ItemListener() {
                            public void itemStateChanged(ItemEvent e) {
                                if (e.getStateChange() == ItemEvent.SELECTED) {
                                    MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().put(entity.getUserIdentity(), true);
                                }
                                if (e.getStateChange() != ItemEvent.SELECTED) {
                                    MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().put(entity.getUserIdentity(), false);
                                }
                            }
                        });
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());
            }
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            //c.insets(1,1,1,1);
            c.insets = new Insets(-40, 0, 0, 0);// -40 for remove button on upper right corner 
            //   buttonPanel.setPreferredSize(new Dimension(20, 0));
            buttonPanel.setOpaque(false);
            //buttonPanel.add(Box.createRigidArea(new Dimension(1, 1)), c);
            // c.gridy++;
            buttonPanel.add(removeButton, c);

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().remove(entity.getUserIdentity());
                        selectedFriends.remove(holder);
                        selectedFriends.revalidate();
                        selectedFriends.repaint();
                        resetNumberofAddedMembers();

                        if (MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().isEmpty()) {
                            JLabel createDragLabel = DesignClasses.makeJLabel_normal("                                 Drag here from contact list that you want to add", 12, DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                            selectedFriends.add(createDragLabel);
                            addedMme.setVisible(false);
                        } else {
                            addedMme.setVisible(true);
                        }
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
            //  e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }

        return holder;
        /* JPanel rowPanel = new JPanel();
         rowPanel.setLayout(new BorderLayout(3, 3));
         rowPanel.setPreferredSize(new Dimension(350, 55));
         rowPanel.setOpaque(false);
         try {

         short[] friendPrivacy = new short[4];
         if (entity.getPrivacy() != null) {
         friendPrivacy = entity.getPrivacy();
         }
         int friendShipStatus = entity.getFriendShipStatus();
         short profileImagePrivacy = friendPrivacy[2];
         BufferedImage img = null;
         String fullName = "";

         if (entity.getProfileImage() != null
         && entity.getProfileImage().trim().length() > 0
         && profileImagePrivacy > 0
         && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || (profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND && friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED))) {
         img = DesignClasses.getBufferImageFromurlFriendList(entity.getProfileImage(), 45);
         } else {
         img = DesignClasses.getBufferImageFromurl(null);
         img = DesignClasses.scaleImage(45, 45, img);
         }

         if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0) && (entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
         fullName = entity.getFullName() + " " + entity.getLastName();
         } else if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0)) {
         fullName = entity.getFullName();
         } else if ((entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
         fullName = entity.getLastName();
         } else {
         fullName = entity.getUserIdentity();
         }

         JLabel fullNameLabel = DesignClasses.makeJLabelFullName(fullName);
         final JButton removeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Remove");

         JLabel user_image = new JLabel();
         user_image.setOpaque(false);
         user_image.setIcon(new ImageIcon(img));

         JPanel user_image_panel = new JPanel(new GridBagLayout());
         user_image_panel.setBackground(Color.WHITE);
         user_image_panel.setBorder(DefaultSettings.DEFAULT_BORDER);
         user_image_panel.setPreferredSize(new Dimension(55, 55));
         user_image_panel.add(user_image);

         rowPanel.add(user_image_panel, BorderLayout.WEST);

         JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
         mainPanel.setBackground(Color.WHITE);
         mainPanel.setBorder(DefaultSettings.DEFAULT_BORDER);
         rowPanel.add(mainPanel, BorderLayout.CENTER);

         JPanel fullNamePanel = new JPanel(new GridLayout(2, 1));
         fullNamePanel.setBorder(new EmptyBorder(10, 10, 0, 0));
         fullNamePanel.setOpaque(false);
         fullNamePanel.add(fullNameLabel);
         mainPanel.add(fullNamePanel, BorderLayout.CENTER);

         JPanel closePanel = new JPanel(new BorderLayout());
         closePanel.setOpaque(false);
         closePanel.setBorder(new EmptyBorder(5, 0, 0, 5));
         closePanel.setPreferredSize(new Dimension(35, 55));
         closePanel.add(removeButton, BorderLayout.NORTH);
         mainPanel.add(closePanel, BorderLayout.LINE_END);

         removeButton.addMouseListener(new MouseListener() {
         @Override
         public void mouseClicked(MouseEvent e) {
         try {
         removeButton.grabFocus();
         MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().remove(entity.getUserIdentity());
         addSelectedCircleFreindList();
         resetNumberofAddedMembers();
         } catch (Exception edd) {
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
         }

         @Override
         public void mouseExited(MouseEvent e) {
         }
         });

         JLabel makAdmin = DesignClasses.makeJLabelFullName("Admin");
         makAdmin.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);

         final JCheckBox checkBox = new JCheckBox("");
         checkBox.setOpaque(false);

         JPanel admin = new JPanel(new BorderLayout());
         admin.setOpaque(false);
         admin.setPreferredSize(new Dimension(0, 20));
         admin.setBorder(new EmptyBorder(0, 10, 5, 0));
         admin.add(makAdmin, BorderLayout.WEST);
         admin.add(checkBox);

         mainPanel.add(admin, BorderLayout.SOUTH);

         try {
         boolean admin_true = MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().get(entity.getUserIdentity());
         checkBox.setSelected(admin_true);

         checkBox.addItemListener(
         new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == ItemEvent.SELECTED) {
         MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().put(entity.getUserIdentity(), true);
         }
         if (e.getStateChange() != ItemEvent.SELECTED) {
         MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().put(entity.getUserIdentity(), false);
         }
         }
         });
         } catch (Exception e) {
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
         GuiRingID.getInstance().showUnknownProfile(entity);
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

         } catch (Exception e) {
         }
         return rowPanel;*/
    }

    public void resetNumberofAddedMembers() {
        addedMme.setText("Selected Members : " + MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().size());
    }

    private JPanel returnPanel() {
        JPanel nullPan = new JPanel();
        nullPan.setLayout(new BorderLayout());
        nullPan.setOpaque(false);
        return nullPan;
    }

    public void clear_selected_members() {
        MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().clear();

    }
     @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainCircleButton(true);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainCircleButton(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }

    public class DropAreaPanel extends JPanel implements DropTargetListener {

        private CreateCirclePanel createNewCirclePanel;

        public DropAreaPanel(CreateCirclePanel createNewCirclePanel) {
            this.createNewCirclePanel = createNewCirclePanel;
            new DropTarget(this, this);
        }

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
                    createNewCirclePanel.addDroppedMember(dragContents);
                } else {
                    dtde.rejectDrop();
                }
            } catch (IOException e) {
                dtde.rejectDrop();
            } catch (UnsupportedFlavorException e) {
                dtde.rejectDrop();
            }
        }
    }
    
}
